package edu.unsam.algo2

import java.time.LocalTime
import java.time.temporal.ChronoUnit

class Actividad(
    var costo: Double,
    var descripcion: String,
    var inicio: LocalTime,
    var fin: LocalTime,
    var dificultad: Dificultad
) {

    fun duracion() = ChronoUnit.MINUTES.between(fin, inicio)

    enum class Dificultad(valor: Int) {
        BAJA(1), MEDIA(2), ALTA(3)
    }
}