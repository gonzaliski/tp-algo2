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

    describe("Tests de Autos..."){
        describe("sin convenio..."){
            val auto = Auto(
                marca = "bmw",
                modelo = "x1 2022",
                anioFabricacion = 2022,
                costoDiario = 1.0,
                diasDeAlquiler = 1,
                kilometrajeLibre = false,
                esHatchback = true
            )
            it("que es hatchback"){
                auto.costoTotal() shouldBe auto.costoBase() * 1.1
            }
            it("que no es hatchback"){
                val autoSinHatchback = Auto(
                    marca = "bmw",
                    modelo = "x1 2022",
                    anioFabricacion = 2022,
                    costoDiario = 1.0,
                    diasDeAlquiler = 1,
                    kilometrajeLibre = false,
                    esHatchback = false
                )

                autoSinHatchback.costoTotal() shouldBe auto.costoBase() * 1.25
            }
        }
        describe("con convenio..."){
            val auto = Auto(
                marca = "honda",
                modelo = "HR-V",
                anioFabricacion = 2022,
                costoDiario = 1.0,
                diasDeAlquiler = 1,
                kilometrajeLibre = false,
                esHatchback = true
            )
            it("que es hatchback"){
                val agregadoHatchback = auto.costoBase() * 0.1
                val descuentoConvenio = (auto.costoBase() + agregadoHatchback) * 0.1
                auto.costoTotal() shouldBe auto.costoBase() + agregadoHatchback - descuentoConvenio
            }
            it("que no es hatchback"){
                val autoSinHatchback = Auto(
                    marca = "honda",
                    modelo = "HR-V",
                    anioFabricacion = 2022,
                    costoDiario = 1.0,
                    diasDeAlquiler = 1,
                    kilometrajeLibre = false,
                    esHatchback = false
                )
                val agregadoHatchback = autoSinHatchback.costoBase() * 0.25
                val descuentoConvenio = (autoSinHatchback.costoBase() + agregadoHatchback) * 0.1
                autoSinHatchback.costoTotal() shouldBe autoSinHatchback.costoBase() + agregadoHatchback - descuentoConvenio
            }
        }
    }
})