package edu.unsam.algo2

interface Entidad {
    var id: Int?

    fun coincideCon(value: String): Boolean
    fun <T> actualizarDatos(elemento: T)
}
