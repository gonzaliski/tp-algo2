package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class UsuarioSpec: DescribeSpec ({
    val usuario = Usuario(
        nombre = "Pepe",
        apellido = "Peter",
        username = "ppeter",
        fechaAlta = LocalDate.now(),
        paisResidencia = "",
        diasDisponibles = 15
    )

    val destinosDeseados = listOf(
        Destino("Italia", "Roma", 0.0),
        Destino("Runcuncun", "Cucurun", 0.0)
    )
    val destinosVisitados = listOf(
        Destino("Chile", "Santiago", 0.0)
    )
    usuario.destinosDeseados.addAll(destinosDeseados)
    usuario.destinosVisitados.addAll(destinosVisitados)

    it("Puede puntuar itinerario de otro cuyo destino conoce"){
        val itinerario = Itinerario(
            creador = Usuario(
                nombre = "Carlos",
                apellido = "Gomez",
                username = "cgomez",
                fechaAlta = LocalDate.now().minusYears(16),
                paisResidencia = "Chile",
                diasDisponibles = 0
            ),
            usuario.destinosDeseados.first(), mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            0.0,
                            "",
                            LocalTime.now(),
                            LocalTime.now(),
                            Actividad.Dificultad.BAJA
                        ),
                        Actividad(
                            0.0,
                            "",
                            LocalTime.now(),
                            LocalTime.now(),
                            Actividad.Dificultad.MEDIA
                        )
                    )
                )
            )
        )

        usuario.puedePuntuar(itinerario) shouldBe true

    }

    it("No puede puntuar porque es el creador"){
        val itinerario = Itinerario(
            creador = usuario,
            usuario.destinosDeseados.first(), mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            0.0,
                            "",
                            LocalTime.now(),
                            LocalTime.now(),
                            Actividad.Dificultad.BAJA
                        ),
                        Actividad(
                            0.0,
                            "",
                            LocalTime.now(),
                            LocalTime.now(),
                            Actividad.Dificultad.MEDIA
                        )
                    )
                )
            )
        )

        usuario.puedePuntuar(itinerario) shouldBe false
    }

    it("No puede puntuar porque no conoce el destino"){
        val itinerario = Itinerario(
            creador = Usuario(
                nombre = "Carlos",
                apellido = "Gomez",
                username = "cgomez",
                fechaAlta = LocalDate.now().minusYears(16),
                paisResidencia = "Chile",
                diasDisponibles = 0
            ),
            Destino("", "", 0.0), mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            0.0,
                            "",
                            LocalTime.now(),
                            LocalTime.now(),
                            Actividad.Dificultad.BAJA
                        ),
                        Actividad(
                            0.0,
                            "",
                            LocalTime.now(),
                            LocalTime.now(),
                            Actividad.Dificultad.MEDIA
                        )
                    )
                )
            )
        )

        usuario.puedePuntuar(itinerario) shouldBe false
    }

})