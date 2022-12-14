package edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.plusOrMinus
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
                moto.costoTotal() shouldBe 1503.0
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

                motoPocaCilindrada.costoTotal() shouldBe 3
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
                moto.costoTotal() shouldBe 1352.7
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

                motoPocaCilindrada.costoTotal() shouldBe 2.7
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
                auto.costoTotal() shouldBe 1.1
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

                autoSinHatchback.costoTotal() shouldBe 1.25
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
                auto.costoTotal() shouldBe 0.99.plusOrMinus(0.0001)
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
                autoSinHatchback.costoTotal() shouldBe 1.125
            }
        }
    }

    describe("Tests de Camionetas..."){
        describe("sin convenio..."){
            val marcaSinConvenio = "alknsfd"
            describe("con pocos dias de alquiler...") {
                val diasDeAlquiler = 7
                it("que es todo terreno") {
                    val camioneta = Camioneta(
                        marca = marcaSinConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = true
                    )
                    camioneta.costoTotal() shouldBe 15_007
                }
                it("que no es todo terreno") {
                    val camioneta = Camioneta(
                        marca = marcaSinConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = false
                    )

                    camioneta.costoTotal() shouldBe 10_007
                }
            }
            describe("con muchos dias de alquiler...") {
                val diasDeAlquiler = 8
                it("que es todo terreno") {
                    val camioneta = Camioneta(
                        marca = marcaSinConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = true
                    )
                    camioneta.costoTotal() shouldBe 16_508.0
                }
                it("que no es todo terreno") {
                    val camioneta = Camioneta(
                        marca = marcaSinConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = false
                    )

                    camioneta.costoTotal() shouldBe 11_008.0
                }
            }
        }
        describe("con convenio..."){
            val marcaConConvenio = "honda"
            describe("con pocos dias de alquiler..."){
                val diasDeAlquiler = 7
                it("que es todo terreno"){
                    val camioneta = Camioneta(
                        marca = marcaConConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = true
                    )
                    camioneta.costoTotal() shouldBe 13_506.3.plusOrMinus(0.0001)
                }
                it("que no es todo terreno"){
                    val camioneta = Camioneta(
                        marca = marcaConConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = false
                    )
                    camioneta.costoTotal() shouldBe 9_006.3.plusOrMinus(0.0001)
                }
            }
            describe("con muchos dias de alquiler..."){
                val diasDeAlquiler = 8
                it("que es todo terreno"){
                    val camioneta = Camioneta(
                        marca = marcaConConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = true
                    )
                    camioneta.costoTotal() shouldBe 14_857.2
                }
                it("que no es todo terreno"){
                    val camioneta = Camioneta(
                        marca = marcaConConvenio,
                        modelo = "2022",
                        anioFabricacion = 2022,
                        costoDiario = 1.0,
                        diasDeAlquiler = diasDeAlquiler,
                        kilometrajeLibre = false,
                        esTodoTerreno = false
                    )
                    camioneta.costoTotal() shouldBe 9_907.2
                }
            }
        }
    }
})