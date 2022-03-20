package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class DestinoSpec: DescribeSpec ({
    describe("Usuario con 16 años de antiguedad y pais de residencia Argentina") {
        val usuario = Usuario("Pepe","Rodriguez", "peper", LocalDate.of(LocalDate.now().year - 16,3,20),"Argentina")

        describe("Destino local") {
            val destino = Destino("Argentina", "MDQ", 1000.00)
            it("Con descuento del 1% por cada año de antiguedad"){
                destino.costo(usuario) shouldBe 850.00 //15% sobre el costo base del destino
            }
        }

        describe("Pais de residencia distinto al pais destino") {
            val destino = Destino("Estados Unidos","New York",5000.00)//reasigno un nuevo destino no local
            it("Con recargo del 20% por ser destino NO local") {
                destino.esLocal() shouldBe false
                destino.costo(usuario) shouldBe 6000.00
            }
        }

    }

})










