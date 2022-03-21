package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class DestinoSpec : DescribeSpec({
    describe("Usuario con mucha antigüedad (+15) con pais residencia distinto a pais destino") {
        val user = Usuario(
            nombre = "Carlos",
            apellido ="Gomez",
            username = "cgomez",
            fecha_alta = LocalDate.of(2003, 11, 25),
            pais_residencia = "Chile"
        )
        describe("Destino no local") {
            val destino = Destino(
                pais = "Brasil",
                ciudad = "Florianopolis",
                costo_base = 30000.0
            )
            it("El costo es de 36000") {
                destino.costo(user) shouldBe 36000.0
            }
        }
    }
})