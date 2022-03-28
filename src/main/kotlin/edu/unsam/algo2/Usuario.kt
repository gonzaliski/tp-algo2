package edu.unsam.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    val nombre: String,
    val apellido: String,
    val username: String,
    val fechaAlta: LocalDate,
    var paisResidencia: String
) {

    fun antiguedad() = ChronoUnit.YEARS.between(fechaAlta, LocalDate.now()).toInt()

}