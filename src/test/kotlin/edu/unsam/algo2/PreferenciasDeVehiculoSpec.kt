package edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate


class UsuarioConPreferenciasSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    // Arrange - Given
    val destinosDeseados = mutableListOf(
        Destino(ciudad = "San Martin", "Argentina", costoBase = 14_000.0)
    )
    val usuario = Usuario(
        nombre = "Alejandro",
        apellido = "Zapata",
        username = "azapata",
        paisResidencia = "Argentina",
        fechaAlta = LocalDate.now(),
        diasDisponibles = 10,
        criterio = Relajado,
        destinosDeseados = destinosDeseados,
        vehiculoPreferencia = Neofilo,
    )
    describe("Tests con usuario Neofilo...") {
        usuario.vehiculoPreferencia = Neofilo

        describe("vehiculo: Moto...") {
            it("moto antigua") {
                // Act - When
                val motoAntigua = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = LocalDate.now().minusYears(Vehiculo.antiguedadMax.toLong()).year,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(motoAntigua).shouldBeFalse()
            }
            it("moto nueva") {
                // Act - When
                val motoNueva = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = LocalDate.now().minusYears(1).year,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(motoNueva).shouldBeTrue()
            }
        }
    }
    describe("Tests con usuario Supersticioso...") {
        usuario.vehiculoPreferencia = Supersticioso

        describe("vehiculo: Moto...") {
            it("con anio de fabricacion impar") {
                // Act - When
                val motoAntigua = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = 2001,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(motoAntigua).shouldBeFalse()
            }
            it("con anio de fabricacion par") {
                // Act - When
                val motoNueva = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = 2000,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(motoNueva).shouldBeTrue()
            }
        }
    }

    describe("Tests con usuario Caprichoso...") {
        usuario.vehiculoPreferencia = Caprichoso

        describe("vehiculo: Moto...") {
            it("cuyas iniciales (marca y modelo) no coinciden") {
                // Act - When
                val moto = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = 2001,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeFalse()
            }
            it("cuyas iniciales (marca y modelo) coinciden") {
                // Act - When
                val moto = Moto(
                    marca = "asd",
                    modelo = "Aqwe",
                    anioFabricacion = 2000,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeTrue()
            }
        }
    }

    describe("Tests con usuario Selectivo...") {
        val marcaDeseada = "hYunDai"
        usuario.vehiculoPreferencia = Selectivo(marcaDeseada)

        describe("vehiculo: Moto...") {
            it("cuya marca no es la deseada") {
                // Act - When
                val moto = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = 2001,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeFalse()
            }
            it("cuya marca es la deseada") {
                // Act - When
                val moto = Moto(
                    marca = marcaDeseada,
                    modelo = "Aqwe",
                    anioFabricacion = 2000,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeTrue()
            }
        }
    }

    describe("Tests con usuario SinLimite...") {
        usuario.vehiculoPreferencia = SinLimite

        describe("vehiculo: Moto...") {
            it("sin kilometraje libre") {
                // Act - When
                val moto = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = 2001,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeFalse()
            }
            it("con kilometraje libre") {
                // Act - When
                val moto = Moto(
                    marca = "marca",
                    modelo = "Aqwe",
                    anioFabricacion = 2000,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = true,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeTrue()
            }
        }
    }

    describe("Tests con usuario Combinado...") {
        usuario.vehiculoPreferencia = Combinado(
            mutableListOf()
        )

        describe("vehiculo: Moto...") {
            it("sin ninguna preferencia") {
                // Act - When
                val moto = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = 2001,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                usuario.leGusta(moto).shouldBeTrue()
            }
            it("cuando todas sus preferencias se cumplen") {
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(SinLimite)
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(Neofilo)
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(Supersticioso)
                val anioDeseado = LocalDate.now()
                    .apply {
                        minusYears((year % 2).toLong())
                    }.year // Es este año si es par, o si no lo es, entonces el anterior
                // Act - When
                val moto = Moto(
                    marca = "asd",
                    modelo = "qwe",
                    anioFabricacion = anioDeseado,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = true,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeTrue()
            }
            it("ninguna preferencia se cumple") {
                val marcaDeseada = "abc"
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(SinLimite)
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(Neofilo)
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(Selectivo(marcaDeseada))
                // Act - When
                val moto = Moto(
                    marca = "marcaDeseada",
                    modelo = "Aqwe",
                    anioFabricacion = LocalDate.now().minusYears(3).year,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = false,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeFalse()
            }
            it("algunas preferencias se cumplen") {
                val marcaDeseada = "abc"
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(SinLimite)
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(Neofilo)
                (usuario.vehiculoPreferencia as Combinado).addPreferencia(Selectivo(marcaDeseada))
                // Act - When
                val moto = Moto(
                    marca = marcaDeseada,
                    modelo = "Aqwe",
                    anioFabricacion = 2000,
                    costoDiario = 100.0,
                    diasDeAlquiler = 3,
                    kilometrajeLibre = true,
                    cilindrada = 200
                )

                // Assert - Then
                usuario.leGusta(moto).shouldBeFalse()
            }
        }
    }
})
