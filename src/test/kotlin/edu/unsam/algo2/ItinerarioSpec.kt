package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class ItinerarioSpec : DescribeSpec({
    val itinerario = Itinerario(
        creador = Usuario(
            nombre = "Carlos",
            apellido = "Gomez",
            username = "cgomez",
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Chile",
            diasDisponibles = 1,
            criterio = Relajado,
            destinosDeseados = mutableListOf(
                Destino(
                    pais = "Argentina",
                    ciudad = "MDQ",
                    costoBase = 25_000.0
                ),
                Destino(
                    pais = "Argentina",
                    ciudad = "Buenos Aires",
                    costoBase = 25_000.0
                )
            ),
            vehiculoPreferencia = Caprichoso
        ),
        destino = Destino(
            pais = "Chile",
            ciudad = "Santiago",
            costoBase = 2.0
        ),
        dias = mutableListOf(
            DiaDeItinerario(
                mutableListOf(
                    Actividad(
                        dificultad = Dificultad.BAJA,
                        descripcion = "asdjasdja",
                        inicio = LocalTime.of(12, 30, 0, 0),
                        fin = LocalTime.of(14, 30, 0, 0),
                        costo = 0.0

                    ),
                    Actividad(
                        dificultad = Dificultad.MEDIA,
                        descripcion = "asdjasdjsa",
                        inicio = LocalTime.of(15, 30, 0, 0),
                        fin = LocalTime.of(17, 30, 0, 0),
                        costo = 0.0
                    )
                )
            )
        ),

        )
    it("con misma cantidad devuelve dificultad mayor") {
        itinerario.dificultad() shouldBe Dificultad.MEDIA
    }

    it("devuelve la dificultad que mas se repite (ALTA)") {
        itinerario.dias.first().actividades.addAll(
            mutableListOf(
                Actividad(
                    dificultad = Dificultad.ALTA,
                    descripcion = "asdjasdja",
                    inicio = LocalTime.of(12, 30, 0, 0),
                    fin = LocalTime.of(14, 30, 0, 0),
                    costo = 0.0
                ),
                Actividad(
                    dificultad = Dificultad.ALTA,
                    descripcion = "asdjasdja",
                    inicio = LocalTime.of(15, 30, 0, 0),
                    fin = LocalTime.of(17, 30, 0, 0),
                    costo = 0.0
                )
            )
        )
        println(itinerario.dificultad())
        itinerario.dificultad() shouldBe Dificultad.ALTA
    }

    it("devuelve la dificultad que mas se repite (MEDIA)") {
        itinerario.dias.first().actividades.addAll(
            mutableListOf(
                Actividad(
                    dificultad = Dificultad.MEDIA,
                    descripcion = "dasdjasdja",
                    inicio = LocalTime.of(12, 30, 0, 0),
                    fin = LocalTime.of(14, 30, 0, 0),
                    costo = 0.0
                ),
                Actividad(
                    dificultad = Dificultad.MEDIA,
                    descripcion = "agsdjasdja",
                    inicio = LocalTime.of(15, 30, 0, 0),
                    fin = LocalTime.of(17, 30, 0, 0),
                    costo = 0.0
                )
            )
        )
        println(itinerario.dificultad())
        itinerario.dificultad() shouldBe Dificultad.MEDIA
    }

    describe("Dado un itinerario y un usuario...") {
        // Arrange - Given
        val pehuajo = Destino(
            ciudad = "Pehuajo",
            pais = "Argentina",
            costoBase = 65_000.0
        )
        val nomeacuerdo = Destino(
            ciudad = "Nomeacuerdo",
            pais = "Nomeacuerdo",
            costoBase = 0.01
        )
        val paris = Destino(
            ciudad = "Paris",
            pais = "Francia",
            costoBase = 951_000.0
        )
        val creador = Usuario(
            nombre = "Reina",
            apellido = "Batata",
            username = "batataqueen",
            paisResidencia = nomeacuerdo.pais,
            fechaAlta = LocalDate.now(),
            diasDisponibles = 2,
            criterio = Localista,
            destinosDeseados = mutableListOf(pehuajo),
            vehiculoPreferencia = Caprichoso
        )
        val itinerarioPehuajo = Itinerario(
            creador = creador,
            destino = pehuajo,
            dias = mutableListOf(
                DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            Dificultad.MEDIA,
                            descripcion = "Un poquitito caminando y otro poquitito a pie",
                            inicio = LocalTime.of(10, 0, 0),
                            fin = LocalTime.of(16, 0, 0),
                            costo = 600.0
                        )
                    )
                )
            ),
            puntuaciones = mutableMapOf(),
        )
        val usuario = Usuario(
            nombre = "Manuelita",
            apellido = "La Tortuga",
            username = "ninjaturtle",
            paisResidencia = pehuajo.pais,
            fechaAlta = LocalDate.now(),
            diasDisponibles = 2,
            criterio = Localista,
            destinosDeseados = mutableListOf(
                nomeacuerdo,
                paris
            ),
            vehiculoPreferencia = Caprichoso
        )
        it("que NO es CREADOR NI AMIGO del creador, NO puede editar el itinerario") {
            // Assert - Then
            itinerarioPehuajo.puedeSerEditadoPor(usuario).shouldBeFalse()
        }

        describe("que NO es CREADOR pero SI AMIGO del creador...") {
            // Act - When
            creador.amigos.add(usuario)
            it("Usuario que NO CONOCE el destino NO puede editar el itinerario") {
                // Assert - Then
                itinerarioPehuajo.puedeSerEditadoPor(usuario).shouldBeFalse()
            }

            it("Usuario que SI CONOCE el destino puede editar el itinerario") {
                usuario.destinosVisitados.add(pehuajo)
                // Assert - Then
                itinerarioPehuajo.puedeSerEditadoPor(usuario).shouldBeTrue()
            }
        }

        it("que ES CREADOR del itinerario SI puede editar el itinerario") {
            // Assert - Then
            itinerarioPehuajo.puedeSerEditadoPor(creador).shouldBeTrue()
        }
    }
})
