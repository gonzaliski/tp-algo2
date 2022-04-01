
package edu.unsam.algo2
/*
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
        diasDisponibles = 15,
        criterio = Criterio.Relajado
    )

    val destinosDeseados = listOf(
        Destino().apply {
            pais = "Italia"
            ciudad = "Roma"
            costoBase = 3.0
        },
        Destino().apply {
            pais ="Runcuncun"
            ciudad = "Cucurun"
            costoBase = 3.0
        }
    )
    val destinosVisitados = listOf(
        Destino().apply {
            pais = "Chile"
            ciudad = "Santiago"
            costoBase = 3.0
        }
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
                diasDisponibles = 0,
                criterio = Criterio.Relajado
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
                diasDisponibles = 0,
                criterio = Criterio.Relajado
            ),
            Destino().apply {
                pais = "Bolivia"
                ciudad = "La Paz"
                costoBase = 3.0
            }, mutableListOf(
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

 */