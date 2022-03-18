package edu.unsam.algo2

import java.time.LocalDate

class Usuario(
    val nombre: String,
    val apellido: String,
    val username: String,
    val fecha_alta: LocalDate,
    var pais_residencia: String
) {

    fun antiguedad() = LocalDate.now().year - fecha_alta.year

}