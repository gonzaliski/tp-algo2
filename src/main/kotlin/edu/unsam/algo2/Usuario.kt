package edu.unsam.algo2

import java.time.LocalDate

class Usuario(
    val nombre: String,
    val apellido: String,
    val username: String,
    val fechaAlta: LocalDate,
    var paisResidencia: String
) {

    fun antiguedad() = LocalDate.now().year - fechaAlta.year

}