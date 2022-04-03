package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.style.DescribeSpec
import java.time.LocalDate

internal class ValidacionSpec : DescribeSpec({
    describe("Validacion de destino") {
        it("No tira error xD") {
            shouldNotThrowAny {
                Destino(
                    pais = "Argentina",
                    ciudad = "Runcuncun",
                    costoBase = 25_000.0
                )
            }
        }
    }
    describe("Validaciones en Usuario") {
        val destino = Destino(
            pais = "Argentina",
            ciudad = "Runcuncun",
            costoBase = 25_000.0
        )
        it("Usuario bien creado no falla") {
            shouldNotThrowAny {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 15,
                    criterio = Criterio.Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino)
                )
            }
        }
        it("Usuario falla con fecha de alta en el futuro") {
            shouldThrowMessage("La fecha de alta no puede ser posterior a la del día.") {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now().plusDays(3),
                    diasDisponibles = 2,
                    criterio = Criterio.Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino)
                )
            }
        }
        it("Usuario falla sin destinos deseados") {
            shouldThrowMessage("Todos los usuarios deben tener al menos un destino deseado.") {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 2,
                    criterio = Criterio.Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf()
                )
            }
        }
        it("Usuario falla si no tiene dias para viajar") {
            shouldThrowMessage("Los días para viajar, deben ser mayores a cero.") {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 0,
                    criterio = Criterio.Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino)
                )
            }
        }
    }
})