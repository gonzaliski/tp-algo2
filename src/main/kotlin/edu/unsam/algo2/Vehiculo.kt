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
    modelo:String,
    anioFabricacion:Int,
    costoDiario:Double,
    diasDeAlquiler:Int,
    kilometrajeLibre: Boolean,
    val cilindrada: Int

): Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre){


    fun costoCilindrada() = if( cilindrada > 250 ) (500 * diasDeAlquiler) else 0 //hacer algo mas general para el precio extra por cilindrada
    fun costoDeAlquiler() = costoBase() + costoCilindrada() // este metodo podria ser interfaz (?
}

class Auto(
    marca: String,
    modelo:String,
    anioFabricacion:Int,
    costoDiario:Double,
    diasDeAlquiler:Int,
    kilometrajeLibre: Boolean,
    val esHatchback: Boolean

): Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre){

    fun porcentajeHatchback() = if( esHatchback ) 0.1  else 0.25
    fun costoHatchback() = costoBase() * porcentajeHatchback()
    fun costoDeAlquiler() = costoBase() + costoHatchback()
}


class Camioneta(
    marca: String,
    modelo:String,
    anioFabricacion:Int,
    costoDiario:Double,
    diasDeAlquiler:Int,
    kilometrajeLibre: Boolean,
    var esTodoTerreno: Boolean

): Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre){

    fun alquilerExcesivo() = diasDeAlquiler > 7
    fun diasDeExceso() = maxOf( diasDeAlquiler - 7, 1 ) //buscar algo mas general para la resta del 7 (seria maximo)
    fun costoPorExceso() = if( !alquilerExcesivo() ) 10000 else (10000 + (1000 * diasDeExceso())) //intentar desacoplar
    fun costoTodoTerreno() = if( esTodoTerreno ) (costoPorExceso() * 0.5) else 0.0      //duda del 50%
    fun costoDeAlquiler() = costoBase() + costoPorExceso() + costoTodoTerreno()
}


