package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class DestinoSpec : DescribeSpec({
    describe("Usuario con poca antiguedad (-15)") {
        val usuario = Usuario(
            nombre = "Pepe",
            apellido = "Rodriguez",
            username = "peper",
            fechaAlta = LocalDate.now().minusYears(2),
            paisResidencia = ""
        )
        describe("Destino local") {
            val destino = Destino(
                pais = "Argentina",
                ciudad = "MDQ",
                costoBase = 25_000.0
            )
            describe("Pais de residencia distinto al pais destino") {
                usuario.paisResidencia = "Uruguay"
                it("No tiene recargo ni descuento") {
                    destino.costo(usuario = usuario) shouldBe destino.costoBase
                }
            }
        }
    }

    describe("Usuario con mucha antigüedad (+15) y pais residencia igual a pais destino") {
        val pepe = Usuario(
            nombre = "Pepe",
            apellido = "Perez",
            username = "peperez",
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Argentina"
        )
        describe("Destino local") {
            val buenosAires = Destino(
                pais = "Argentina",
                ciudad = "Buenos Aires",
                costoBase = 40_000.0
            )
            it("El costo es de 34000") {
                buenosAires.costo(pepe) shouldBe 34_000.0
            }
        }
    }

    describe("Usuario con 16 años de antiguedad y pais de residencia Argentina") {
        val usuario = Usuario("Pepe", "Rodriguez", "peper", LocalDate.now().minusYears(16), "Argentina")

        describe("Destino local") {
            val destino = Destino("Argentina", "MDQ", 1000.00)
            it("Con descuento del 1% por cada año de antiguedad") {
                destino.costo(usuario) shouldBe 850.00 //15% sobre el costo base del destino
            }
        }

        describe("Pais de residencia distinto al pais destino") {
            val destino = Destino("Estados Unidos", "New York", 5000.00)//reasigno un nuevo destino no local
            it("Con recargo del 20% por ser destino NO local") {
                destino.esLocal() shouldBe false
                destino.costo(usuario) shouldBe 6000.00
            }
        }

    }

    describe("Usuario con mucha antigüedad (+15) con pais residencia distinto a pais destino") {
        val user = Usuario(
            nombre = "Carlos",
            apellido = "Gomez",
            username = "cgomez",
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Chile"
        )
        describe("Destino no local") {
            val destino = Destino(
                pais = "Brasil",
                ciudad = "Florianopolis",
                costoBase = 30000.0
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
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Inglaterra"
        )

        describe("Test con destino no local") {
            val londres = Destino(
                pais = "Inglaterra",
                ciudad = "Londres",
                costoBase = 60000.0
            )
            it("20% más por no ser destino local y descuento por antiguedad (15%), costo de 63000") {
                londres.costo(frank) shouldBe 63000.0
            }
        }
    }
})


