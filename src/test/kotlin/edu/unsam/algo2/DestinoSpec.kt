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
    
      describe("Test con usuario con mucha antiguedad (+15), pais de residencia igual al pais destino ") {
        val frank = Usuario( 
                nombre = "Frank", 
                apellido = "Arnold", 
                username = "frarnold", 
                fecha_alta = LocalDate.of(2003, 2, 5),
                pais_residencia = "Inglaterra"
            )

        describe("Test con destino no local"){
            val londres = Destino(
                pais = "Inglaterra",
                ciudad = "Londres",
                costo_base = 60000.0
            )
            it("20% más por no ser destino local y descuento por antiguedad (15%), costo de 63000"){
                londres.costo(frank) shouldBe 63000.0
            }
        }

        
    }
})

