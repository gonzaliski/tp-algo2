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
            diasDisponibles = 0
        ),
        Destino(pais = "", "", 0.0), mutableListOf(
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
    it("con misma cantidad devuelve dificultad mayor") {
        itinerario.dificultad() shouldBe Actividad.Dificultad.MEDIA
    }

    it("devuelve la dificultad que mas se repite (ALTA)") {
        itinerario.dias.first().actividades.addAll(
            listOf(
                Actividad(
                    0.0,
                    "",
                    LocalTime.now(),
                    LocalTime.now(),
                    Actividad.Dificultad.ALTA
                ), Actividad(
                    0.0,
                    "",
                    LocalTime.now(),
                    LocalTime.now(),
                    Actividad.Dificultad.ALTA
                )
            )
        )
        println(itinerario.dificultad())
        itinerario.dificultad() shouldBe Actividad.Dificultad.ALTA
    }

    it("devuelve la dificultad que mas se repite (MEDIA)") {
        itinerario.dias.first().actividades.addAll(
            listOf(
                Actividad(
                    0.0,
                    "",
                    LocalTime.now(),
                    LocalTime.now(),
                    Actividad.Dificultad.MEDIA
                ), Actividad(
                    0.0,
                    "",
                    LocalTime.now(),
                    LocalTime.now(),
                    Actividad.Dificultad.MEDIA
                )
            )
        )
        println(itinerario.dificultad())
        itinerario.dificultad() shouldBe Actividad.Dificultad.MEDIA
    }
})