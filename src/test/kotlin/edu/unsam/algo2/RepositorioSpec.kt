package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.date.shouldNotHaveSameDayAs
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

internal class RepositorioSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Repositorio de Usuarios") {
        // Arrange - Given
        val usuario = Usuario(
            nombre = "lala",
            apellido = "lele",
            username = "lalalele",
            email = "",
            paisResidencia = "Colombia",
            fechaAlta = LocalDate.now(),
            diasDisponibles = 3,
            criterio = Localista,
            destinosDeseados = mutableListOf(
                Destino(
                    ciudad = "Salta",
                    pais = "Argentina",
                    costoBase = 500.0
                )
            ),
            vehiculoPreferencia = Caprichoso
        )
        val repo = Repositorio<Usuario>()

        describe("Dado un Repositorio vacio...") {
            describe("al intentar crear un usuario...") {
                // Act - When
                repo.create(usuario)

                it("se le asigna un ID") {
                    // Assert - Then
                    usuario.id.shouldNotBeNull()
                    usuario.id shouldBe 1
                }

                it("se agrega a la lista") {
                    // Assert - Then
                    repo.elementos.contains(usuario).shouldBeTrue()
                }

            }

            it("al intentar eliminar un usuario, lanza error") {
                // Act - When
                val bloque = {
                    repo.delete(usuario)
                }

                // Assert - Then
                shouldThrowExactly<InvalidElementException>(bloque)
                shouldThrowMessage("Elemento invalido: No se encontro un elemento con ese ID", bloque)
            }

            it("al intentar cambiar un usuario, lanza un error") {
                val bloque = {
                    repo.update(usuario)
                }
                shouldThrowExactly<InvalidElementException>(bloque)
            }

            it("al intentar buscar un Id da error") {
                // Assert - Then

                val bloque = {
                    repo.getById(1)
                }

                // Assert - Then
                shouldThrowExactly<InvalidElementException>(bloque)
            }

            it("al intentar una busqueda, devuelve una lista vacia") {
                // Assert - Then
                val bloque = {
                    repo.search(usuario.username)
                }

                // Assert - Then
                bloque().shouldBeEmpty()
            }
        }

        describe("Dado un repositorio con un usuario") {
            // Arrange - Given
            val usuarioEnRepo = Usuario(
                nombre = "pepe",
                apellido = "perez",
                username = "pperez",
                email = "pepe.perez@mail.com",
                paisResidencia = "Bolivia",
                fechaAlta = LocalDate.now().minusDays(2),
                diasDisponibles = 5,
                criterio = Activo,
                destinosDeseados = mutableListOf(Destino("Bariloche", "Argentina", costoBase = 230_000.0)),
                vehiculoPreferencia = Supersticioso
            )
            repo.create(usuarioEnRepo)

            describe("al intentar crear un usuario...") {
                // Act - When
                repo.create(usuario)

                it("se le asigna un ID") {
                    // Assert - Then
                    usuario.id.shouldNotBeNull()
                    usuario.id shouldBe 2
                }

                it("se agrega a la lista") {
                    // Assert - Then
                    repo.elementos.contains(usuario).shouldBeTrue()
                    repo.elementos.contains(usuarioEnRepo).shouldBeTrue()
                    repo.elementos.size shouldBe 2
                }
            }

            describe("al intentar eliminar un usuario") {
                it("que no existe, lanza error") {
                    // Act - When
                    val bloque = {
                        repo.delete(usuario)
                    }

                    // Assert - Then
                    shouldThrowExactly<InvalidElementException>(bloque)
                    shouldThrowMessage("Elemento invalido: No se encontro un elemento con ese ID", bloque)
                }
                it("que existe en el repo, lo elimina") {
                    // Act - When
                    repo.delete(usuarioEnRepo)

                    repo.elementos.contains(usuarioEnRepo).shouldBeFalse()
                    repo.elementos.size shouldBe 0
                    shouldThrowExactly<InvalidElementException> { repo.getById(usuarioEnRepo.id) }
                }
            }

            describe("Al intentar modificar un usuario") {
                it("que está en el repo, se actualiza") {
                    // Act - When
                    usuario.id = usuarioEnRepo.id
                    repo.update(usuario)

                    usuario.vehiculoPreferencia shouldBe usuarioEnRepo.vehiculoPreferencia
                    usuario.nombre shouldBe usuarioEnRepo.nombre
                    usuario.apellido shouldBe usuarioEnRepo.apellido
                    usuario.username shouldBe usuarioEnRepo.username
                    usuario.fechaAlta shouldNotHaveSameDayAs usuarioEnRepo.fechaAlta
                }
            }
            describe("Al intentar buscar un usuario") {
                it("que está en el repo, lo obtengo") {
                    val usuarioEncontrado = repo.getById(1)

                    usuarioEncontrado.shouldBe(usuarioEnRepo)
                }
            }

            describe("Al intentar una busqueda") {
                it("hay coincidencia por nombre") {
                    repo.search("pepe") shouldContain usuarioEnRepo
                }
                it("hay coincidencia por apellido") {
                    repo.search("perez") shouldContain usuarioEnRepo
                }
                it("hay coincidencia por username") {
                    repo.search("pperez") shouldContain usuarioEnRepo
                }
            }

            it("al intentar agregar otro usuario con un ID que ya existe, lanza error") {
                shouldThrowExactly<InvalidIdException> {
                    repo.create(
                        Usuario(
                            nombre = "a",
                            apellido = "b",
                            username = "c",
                            email = "abc@mail.com",
                            paisResidencia = "d",
                            fechaAlta = LocalDate.now(),
                            diasDisponibles = 5,
                            criterio = Relajado,
                            destinosDeseados = mutableListOf(
                                Destino(
                                    ciudad = "Salta",
                                    pais = "Argentina",
                                    costoBase = 500.0
                                )
                            ),
                            vehiculoPreferencia = SinLimite
                        ).apply { id = 1 }
                    )
                }
            }
        }
    }

    describe("Repositorio de Destinos") {
        val repo = Repositorio<Destino>()
        val destinoEnRepo = Destino("Bariloche", "Argentina", costoBase = 230_000.0)

        repo.create(destinoEnRepo)

        describe("Al intentar una búsqueda") {
            it("hay coincidencia por pais") {
                repo.search("barilo") shouldContain destinoEnRepo
            }
            it("hay coincidencia por ciudad") {
                repo.search("argent") shouldContain destinoEnRepo
            }
        }
    }

    describe("Repositorio de Itinerarios") {
        val usuario = Usuario(
            nombre = "lala",
            apellido = "lele",
            username = "lalalele",
            email = "",
            paisResidencia = "Colombia",
            fechaAlta = LocalDate.now(),
            diasDisponibles = 3,
            criterio = Localista,
            destinosDeseados = mutableListOf(
                Destino(
                    ciudad = "Salta",
                    pais = "Argentina",
                    costoBase = 500.0
                )
            ),
            vehiculoPreferencia = Caprichoso
        )
        val repo = Repositorio<Itinerario>()
        val itinerarioEnRepo = Itinerario(
            usuario, Destino("Bariloche", "Argentina", costoBase = 230_000.0), dias = mutableListOf(
                DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            descripcion = "una descripcion",
                            inicio = LocalTime.of(15, 0, 0),
                            fin = LocalTime.of(18, 30, 0),
                            costo = 86.0
                        )
                    )
                )
            )
        )

        repo.create(itinerarioEnRepo)

        describe("Al intentar una búsqueda") {
            it("hay coincidencia por destino") {
                repo.search("barilo") shouldContain itinerarioEnRepo
            }
            it("hay coincidencia por actividad") {
                repo.search("desc") shouldContain itinerarioEnRepo
            }
        }
    }

    describe("Repositorio de Vehiculos") {
        val repo = Repositorio<Vehiculo>()
        val vehiculoEnRepo = Moto(
            marca = "abc",
            modelo = "def",
            anioFabricacion = 2003,
            costoDiario = 200.0,
            diasDeAlquiler = 6,
            kilometrajeLibre = true,
            cilindrada = 420
        )

        repo.create(vehiculoEnRepo)

        describe("Al intentar una búsqueda") {
            it("hay coincidencia por marca") {
                repo.search("aBC") shouldContain vehiculoEnRepo
            }
            it("hay coincidencia por modelo") {
                repo.search("DE") shouldContain vehiculoEnRepo
            }
        }
    }
})
