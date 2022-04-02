package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class ItinerarioSpec : DescribeSpec({
    val itinerario = Itinerario(
        creador = Usuario(
            nombre = "Carlos",
            apellido = "Gomez",
            username = "cgomez",
            fechaAlta = LocalDate.now().minusYears(16),
            paisResidencia = "Chile",
            diasDisponibles = 1,
            criterio = Criterio.Relajado
        ).apply {
            destinosDeseados = mutableListOf(
                Destino(
                    pais = "Argentina",
                    ciudad = "MDQ",
                    costoBase = 25_000.0
                ),
                Destino(
                    pais = "Argentina",
                    ciudad = "Buenos Aires",
                    costoBase = 25_000.0
                )
            )
        },
        destino = Destino(
            pais = "Chile",
            ciudad = "Santiago",
            costoBase = 2.0
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
                        dificultad = Actividad.Dificultad.MEDIA,
                        descripcion = "asdjasdjsa",
                        inicio = LocalTime.now(),
                        fin = LocalTime.now().plusHours(2),
                        costo = 0.0                    )
                )
            )
        ),

        )
    it("con misma cantidad devuelve dificultad mayor") {
        itinerario.dificultad() shouldBe Actividad.Dificultad.MEDIA
    }

    it("devuelve la dificultad que mas se repite (ALTA)") {
        itinerario.dias.first().actividades.addAll(
            mutableListOf(
                Actividad(
                    dificultad = Actividad.Dificultad.ALTA,
                    descripcion = "asdjasdja",
                    inicio = LocalTime.now(),
                    fin = LocalTime.now().plusHours(2),
                    costo = 0.0                ),
                Actividad(
                    dificultad = Actividad.Dificultad.ALTA,
                    descripcion = "asdjasdja",
                    inicio = LocalTime.now(),
                    fin = LocalTime.now().plusHours(2),
                    costo = 0.0                )
            )
        )
        println(itinerario.dificultad())
        itinerario.dificultad() shouldBe Actividad.Dificultad.ALTA
    }

    it("devuelve la dificultad que mas se repite (MEDIA)") {
        itinerario.dias.first().actividades.addAll(
            mutableListOf(
                Actividad(
                    dificultad = Actividad.Dificultad.MEDIA,
                    descripcion = "dasdjasdja",
                    inicio = LocalTime.now(),
                    fin = LocalTime.now().plusHours(2),
                    costo = 0.0                ),
                Actividad(
                    dificultad = Actividad.Dificultad.MEDIA,
                    descripcion = "agsdjasdja",
                    inicio = LocalTime.now(),
                    fin = LocalTime.now().plusHours(2),
                    costo = 0.0                )
            )
        )
        println(itinerario.dificultad())
        itinerario.dificultad() shouldBe Actividad.Dificultad.MEDIA
    }
})
