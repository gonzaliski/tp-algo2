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
            fecha_alta = LocalDate.of(
                /* year = */ LocalDate.now().year - 2, // antiguedad es siempre 2
                /* month = */ 5,
                /* dayOfMonth = */ 11
            ),
            pais_residencia = ""
        )
        describe("Destino local") {
            val destino = Destino(
                pais = "Argentina",
                cuidad = "MDQ",
                costo_base = 25_000.0
            )
            describe("Pais de residencia distinto al pais destino") {
                usuario.pais_residencia = "Uruguay"
                it("No tiene recargo ni descuento") {
                    destino.costo(usuario = usuario) shouldBe destino.costo_base
                }
            }
        }
    }
})