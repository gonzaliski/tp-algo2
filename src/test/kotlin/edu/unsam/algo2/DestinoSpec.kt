package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class DestinoSpec : DescribeSpec({
    describe("Usuario con poca antiguedad ") {
        val usuario = Usuario(
            nombre = "Pepe",
            apellido = "Rodriguez",
            username = "peper",
            fechaAlta = LocalDate.now().minusYears(2),
            paisResidencia = "Uruguay",
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
        )
        describe("Destino local") {
            val destino = Destino(
                pais = "Argentina",
                ciudad = "MDQ",
                costoBase = 25_000.0
            )


            it("No tiene recargo ni descuento") {
                destino.costo(usuario = usuario) shouldBe destino.costoBase
            }
        }
    }

    describe("Usuario con mucha antigüedad  y pais residencia igual a pais destino") {
        val pepe = Usuario(
            nombre = "Pepe",
            apellido = "Perez",
            username = "peperez",
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Argentina",
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

    describe("Usuario con mucha antiguedad y pais de residencia igual al destino local") {
        val usuario = Usuario(
            nombre = "Pepe",
            apellido = "Rodriguez",
            username = "peper",
            paisResidencia = "Argentina",
            fechaAlta = LocalDate.now().minusYears(16),
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
        )
        describe("Destino local") {
            val destino = Destino(
                pais = "Argentina",
                ciudad = "MDQ",
                costoBase = 1000.00
            )

            it("Con descuento del 1% por cada año de antiguedad") {
                destino.costo(usuario) shouldBe 850.00 //15% sobre el costo base del destino
            }
        }

        describe("Pais de residencia distinto al pais destino") {
            val destino = Destino(
                pais = "Estados Unidos",
                ciudad = "New York",
                costoBase = 5000.00
            )

            it("Con recargo del 20% por ser destino NO local") {
                destino.esLocal().shouldBeFalse()
                destino.costo(usuario) shouldBe 6000.00
            }
        }

    }

    describe("Usuario con mucha antigüedad  con pais residencia distinto a pais destino") {
        val user = Usuario(
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

    describe("Test con usuario con mucha antiguedad, pais de residencia igual al pais destino ") {
        val frank = Usuario(
            nombre = "Frank",
            apellido = "Arnold",
            username = "frarnold",
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Inglaterra",
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
        )

        describe("Test con destino no local") {
            val londres = Destino(
                pais = "Inglaterra",
                ciudad = "Londres",
                costoBase = 60000.0
            )

            it("recargo demás por no ser destino local y descuento por antiguedad , calculamos costo") {
                londres.costo(frank) shouldBe 63000.0
            }
        }
    }
})


