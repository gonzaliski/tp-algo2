package edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class VehiculoSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Test de Motos...") {
        describe("sin convenio...") {
            val moto = Moto(
                marca = "bmw",
                modelo = "C 400 X",
                anioFabricacion = 2019,
                costoDiario = 1.0,
                diasDeAlquiler = 3,
                kilometrajeLibre = false,
                cilindrada = 350
            )
            it("con mucha cilindrada") {
                moto.costoTotal() shouldBe moto.costoBase() + 500.0 * moto.diasDeAlquiler
            }
            it("con poca cilindrada") {
                val motoPocaCilindrada = Moto(
                    marca = "bmw",
                    modelo = "C 400 X",
                    anioFabricacion = 2019,
                    costoDiario = 1.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 249
                )

                motoPocaCilindrada.costoTotal() shouldBe motoPocaCilindrada.costoBase()
            }
        }
        describe("con convenio...") {
            val moto = Moto(
                marca = "honda",
                modelo = "adv 350",
                anioFabricacion = 2019,
                costoDiario = 1.0,
                diasDeAlquiler = 3,
                kilometrajeLibre = false,
                cilindrada = 330
            )
            it("con mucha cilindrada") {
                val costoAlquiler = moto.costoBase() + 500.0 * moto.diasDeAlquiler
                moto.costoTotal() shouldBe costoAlquiler * 0.9
            }
            it("con poca cilindrada") {
                val motoPocaCilindrada = Moto(
                    marca = "honda",
                    modelo = "adv 350",
                    anioFabricacion = 2019,
                    costoDiario = 1.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 249
                )

                motoPocaCilindrada.costoTotal() shouldBe motoPocaCilindrada.costoBase() * 0.9
            }
        }
    }
})