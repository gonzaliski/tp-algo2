package edu.unsam.algo2

import java.time.LocalDate

abstract class Vehiculo(
    val marca: String,
    val modelo: String,
    val anioFabricacion: Int,
    var costoDiario: Double,
    val diasDeAlquiler: Int,
    var kilometrajeLibre: Boolean
) : Identificable {
    override var id: Int? = null

    fun costoBase() = costoDiario * diasDeAlquiler

    fun antiguedad() = LocalDate.now().year - anioFabricacion
    fun tieneConvenio() = empresasConvenio.contains(marca)

    companion object {
        val empresasConvenio: MutableList<String> = mutableListOf("Honda")
    }

    /**El valor de búsqueda debe coincidir exactamente con la marca o con el comienzo del modelo.*/
    override fun coincideCon(value: String): Boolean {
        return marca == value || modelo.startsWith(value)
    }

}


class Moto(
    marca: String,
    modelo: String,
    anioFabricacion: Int,
    costoDiario: Double,
    diasDeAlquiler: Int,
    kilometrajeLibre: Boolean, override var id: Int?
) : Vehiculo(
    marca,
    modelo,
    anioFabricacion,
    costoDiario,
    diasDeAlquiler,
    kilometrajeLibre
) {

}