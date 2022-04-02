
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
        diasDisponibles = 15,
        criterio = Criterio.Relajado
    ).apply {
        destinosDeseados = mutableListOf(
            Destino(
                pais = "Italia",
                ciudad = "Roma",
                costoBase = 3.0
            ),

            Destino(
                pais ="Runcuncun",
                ciudad = "Cucurun",
                costoBase = 3.0
            )
        )
        destinosVisitados = mutableListOf(
            Destino(
                pais = "Chile",
                ciudad = "Santiago",
                costoBase = 3.0
            )
        )
    }


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
            destino = usuario.destinosDeseados.first(),
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            dificultad = Actividad.Dificultad.BAJA,
                            descripcion = "asdjasdja",
                            inicio = LocalTime.now(),
                            fin = LocalTime.now().plusHours(2),
                            costo = 0.0

                        ),
                        Actividad(
                            dificultad = Actividad.Dificultad.MEDIA,
                            descripcion = "asdjasdjsa",
                            inicio = LocalTime.now(),
                            fin = LocalTime.now().plusHours(2),
                            costo = 0.0                    )
                    )
                )
            )
        )

        usuario.puedePuntuar(itinerario) shouldBe true

    }

    it("No puede puntuar porque es el creador"){
        val itinerario = Itinerario(
            creador = usuario,
            destino = usuario.destinosDeseados.first(),
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            dificultad = Actividad.Dificultad.BAJA,
                            descripcion = "asdjasdja",
                            inicio = LocalTime.now(),
                            fin = LocalTime.now().plusHours(2),
                            costo = 0.0
                        ),
                        Actividad(
                            dificultad = Actividad.Dificultad.BAJA,
                            descripcion = "asdjasdja",
                            inicio = LocalTime.now(),
                            fin = LocalTime.now().plusHours(2),
                            costo = 0.0
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
            destino = Destino(
                pais = "Bolivia",
                ciudad = "La Paz",
                costoBase = 3.0
            ),
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            dificultad = Actividad.Dificultad.BAJA,
                            descripcion = "asdjasdja",
                            inicio = LocalTime.now(),
                            fin = LocalTime.now().plusHours(2),
                            costo = 0.0
                        ),
                        Actividad(
                            dificultad = Actividad.Dificultad.BAJA,
                            descripcion = "asdjasdja",
                            inicio = LocalTime.now(),
                            fin = LocalTime.now().plusHours(2),
                            costo = 0.0
                        )
                    )
                )
            )
        )

        usuario.puedePuntuar(itinerario) shouldBe false
    }

})

