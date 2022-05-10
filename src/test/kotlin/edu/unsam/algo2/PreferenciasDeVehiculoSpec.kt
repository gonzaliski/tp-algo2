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
})
