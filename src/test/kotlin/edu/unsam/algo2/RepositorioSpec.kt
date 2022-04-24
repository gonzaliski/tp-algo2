package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class RepositorioSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val usuario = Usuario(
        nombre = "lala",
        apellido = "lele",
        username = "lalalele",
        paisResidencia = "Colombia",
        fechaAlta = LocalDate.now(),
        diasDisponibles = 3,
        criterio = Localista,
        destinosDeseados = mutableListOf(
            Destino(
                ciudad = "Salta",
                pais = "Salta",
                costoBase = 500.0
            )
        ),
        vehiculoPreferencia = Caprichoso
    )

    describe("Repositorio de Usuarios") {
        // Arrange - Given
        val repo = Repositorio<Usuario>()

        describe("Dado un Repositorio vacio...") {
            describe("al intentar crear un usuario...") {
                // Act - When
                repo.create(usuario)

                it("se le asigna un ID") {
                    // Assert - Then
                    usuario.id.shouldNotBeNull()
                    usuario.id shouldBe repo.nextID - 1
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
                shouldThrowMessage("Elemento invalido: El elemento no ha sido encontrado", bloque)
            }

            it("al intentar cambiar un usuario, lanza un error") {
                val bloque = {
                    repo.update(usuario)
                }
                shouldThrowExactly<InvalidElementException>(bloque)
                shouldThrowMessage("Elemento invalido: El elemento no ha sido encontrado", bloque)
            }

            it("al intentar buscar un Id, devuelve null y da error") {
                // Assert - Then

                val bloque = {
                    repo.getById(0)
                }

                // Assert - Then
                shouldThrowExactly<InvalidElementException>(bloque)
                shouldThrowMessage("Elemento invalido: No se encontro un elemento con ese ID", bloque)
            }

            it("al intentar una busqueda, devuelve una lista vacia") {
                // Assert - Then
                val bloque = {
                    repo.search(usuario.username)
                }

                // Assert - Then
                shouldThrowExactly<InvalidElementException>(bloque)
                shouldThrowMessage("Elemento invalido: No existen elementos con ese criterio", bloque)
            }
        }
    }
})
