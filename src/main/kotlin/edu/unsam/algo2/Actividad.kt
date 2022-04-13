package edu.unsam.algo2

import java.time.LocalTime
import java.time.temporal.ChronoUnit

class Actividad(

    var dificultad: Dificultad = Dificultad.BAJA,
    var descripcion: String,
    var inicio: LocalTime,
    var fin: LocalTime,
    var costo: Double
) {

    init {
        require(descripcion.isNotBlank()) {
            "Descripcion de la actividad ser nulo o vacio"
        }

        require(inicio < fin) {
            "El horario de inicio debe ser menor que el de fin"
        }

        require(costo >= 0.0) {
            "Costo no puede ser menor a 0"
        }
    }

    fun duracion() = ChronoUnit.MINUTES.between(fin, inicio)

    /** Este metodo funciona para lista de Actividades ordenadas por su inicio ascendentemente */
    fun seSolapaCon(actividad: Actividad): Boolean = this.fin > actividad.inicio

}