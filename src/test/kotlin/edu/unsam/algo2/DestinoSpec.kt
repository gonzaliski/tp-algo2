package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class DestinoSpec : DescribeSpec({
    describe("Usuario con mucha antigüedad (+15) y pais residencia igual a pais destino") {
        val pepe = Usuario(
            nombre = "Pepe",
            apellido ="Perez",
            username = "peperez",
            fecha_alta = LocalDate.of(2006, 3, 18),
            pais_residencia = "Argentina"
        )
        describe("Destino local") {
            val buenosAires = Destino(
                pais = "Argentina",
                ciudad = "Buenos Aires",
                costo_base = 40_000.0
            )
            it("El costo es de 34000") {
                buenosAires.costo(pepe) shouldBe 34_000.0
            }
        }
    }
})