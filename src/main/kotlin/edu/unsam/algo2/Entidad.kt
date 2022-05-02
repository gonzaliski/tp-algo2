package edu.unsam.algo2

interface Entidad {
    companion object {
        const val ID_INICIAL = 0
    }

    var id: Int

    fun coincideCon(value: String): Boolean
    fun <T> actualizarDatos(elemento: T)

    fun esNuevo(): Boolean = id == ID_INICIAL
}
