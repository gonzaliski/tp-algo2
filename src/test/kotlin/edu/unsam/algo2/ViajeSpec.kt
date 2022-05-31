package edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalTime

class ViajeSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Dado un viaje y un usuario") {
        val usuario = Usuario(
            nombre = "asd",
            apellido = "qwe",
            username = "asdqwe",
            paisResidencia = "zxc",
            fechaAlta = LocalDate.now(),
            diasDisponibles = 4,
            criterio = Relajado,
            destinosDeseados = mutableListOf(
                Destino(
                    ciudad = "Birmingham",
                    pais = "Inglaterra",
                    costoBase = 123.000
                )
            ),
            vehiculoPreferencia = SinLimite
        )
        val destino = Destino(
            ciudad = "ccc",
            pais = "fff",
            costoBase = 230.0
        )
        val itinerario = Itinerario(
            creador = usuario,
            destino = destino,
            dias = mutableListOf(
                DiaDeItinerario(
                    actividades = mutableListOf(
                        Actividad(
                            Dificultad.MEDIA,
                            descripcion = "...",
                            inicio = LocalTime.NOON,
                            fin = LocalTime.NOON.plusMinutes(30),
                            costo = 50.0
                        )
                    )
                )
            )
        )
        val viaje = Viaje(
            itinerario = itinerario,
            vehiculo = Auto(
                marca = "pko",
                modelo = "oij",
                anioFabricacion = 2016,
                costoDiario = 156.0,
                diasDeAlquiler = 3,
                kilometrajeLibre = true,
                esHatchback = false
            )
        )
        describe("que no activó ningún comportamiento extra") {
            it("al realizar el viaje, se agrega a sus destinos visitados") {
                usuario.realizar(viaje)

                usuario.destinosVisitados shouldContain destino
                usuario.criterio shouldNotBe Localista
                usuario.itinerariosAPuntuar shouldNotContain itinerario
            }
        }

        describe("que activó el aviso por mail") {
            val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
            usuario.activarAvisoPorMail(mockedMailSender)

            it("al realizar el viaje, se envía a sus amigos con el destino como deseado") {
                // Arrange
                val amigoConDestinoDeseado = Usuario(
                    nombre = "Billy",
                    apellido = "Kimpbell",
                    username = "billy",
                    paisResidencia = "Inglaterra",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 2,
                    criterio = Activo,
                    destinosDeseados = mutableListOf(destino),
                    vehiculoPreferencia = Supersticioso
                )
                usuario.amigos.add(amigoConDestinoDeseado)

                // Act
                usuario.realizar(viaje)

                // Assert
                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "app@holamundo.com",
                            to = amigoConDestinoDeseado.username,
                            subject = "Visitaron un destino que te puede interesar",
                            content = "Hola! ${amigoConDestinoDeseado.nombre}, ${usuario.nombre + usuario.apellido} visitó ${destino}."
                        )
                    )
                }
                usuario.destinosVisitados shouldContain destino
                usuario.criterio shouldNotBe Localista
                usuario.itinerariosAPuntuar shouldNotContain itinerario
            }
        }
    }
})