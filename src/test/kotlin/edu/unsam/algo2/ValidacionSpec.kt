package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class ValidacionSpec : DescribeSpec({
    describe("Validacion de destino"){
        it("No tira error xD"){
            val destino = Destino().apply{
                pais = "Argentina"
                ciudad = "Runcuncun"
                costoBase = 25_000.0
            }
            destino.pais shouldBe "Argentina"
        }
    }
    describe("Funcion init a ver si labura"){
        it("Nombre deberia ser lo que le pasemos"){
            val destino = Destino().apply{
                pais = "Argentina"
                ciudad = "Runcuncun"
                costoBase = 25_000.0
            }
            val usuario = Usuario(
                nombre = "Peperoni",
                fechaAlta = LocalDate.now(),
                diasDisponibles = 15,
                criterio = Criterio.Relajado
            ).apply{
                apellido = "Peter"
                username = "ppeter"
                paisResidencia = "Venezuela"
            }
            destino.pais shouldBe "Argentina"
        }
    }
    describe("Test de fecha de alta, dias disponibles y destinos deseados "){
        it("Nombre deberia ser lo que le pasemos"){
            val destino = Destino().apply{
                pais = "Argentina"
                ciudad = "Runcuncun"
                costoBase = 25_000.0
            }
            val usuario = Usuario(
                nombre = "Peperoni",
                fechaAlta = LocalDate.now().plusDays(3),
                diasDisponibles = 0,
                criterio = Criterio.Relajado
            ).apply{
                apellido = "Peter"
                username = "ppeter"
                paisResidencia = "Venezuela"
                destinosDeseados = mutableListOf()

            }
            destino.pais shouldBe "Argentina"
        }
    }
})