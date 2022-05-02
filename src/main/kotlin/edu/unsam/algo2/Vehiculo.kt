package edu.unsam.algo2

import java.time.LocalDate

abstract class Vehiculo(
    val marca: String,
    val modelo: String,
    val anioFabricacion: Int,
    var costoDiario: Double,
    val diasDeAlquiler: Int,
    var kilometrajeLibre: Boolean
) : Entidad {
    override var id: Int = Entidad.ID_INICIAL

    companion object {
        val empresasConvenio: MutableList<String> = mutableListOf("Honda")
        val antiguedadMax: Int = 2
    }

    fun costoBase() = costoDiario * diasDeAlquiler
    fun antiguedad() = LocalDate.now().year - anioFabricacion
    fun tieneConvenio() = empresasConvenio.any { it.equals(marca, ignoreCase = true) }

    // Calculo del costo
    abstract fun costoParticular(): Double
    fun subTotal() = costoBase() + costoParticular()
    fun costoTotal() = subTotal() * descuentoPorConvenio()
    fun descuentoPorConvenio() = if (this.tieneConvenio()) 0.9 else 1.0

    fun anioFabricacionPar() = anioFabricacion.isEven()
    fun coincidenInciales() = marca.trim().first().equals(modelo.trim().first(), ignoreCase = true)
    fun esDeMarca(marcaDeseada: String) = marcaDeseada.equals(marca, ignoreCase = true)
    fun noEsMuyAntiguo() = antiguedad() < antiguedadMax

    /**El valor de búsqueda debe coincidir exactamente con la marca o con el comienzo del modelo.*/
    override fun coincideCon(value: String): Boolean {
        return esDeMarca(value) || modelo.startsWith(value, ignoreCase = true)
    }


}


class Moto(
    marca: String,
    modelo: String,
    anioFabricacion: Int,
    costoDiario: Double,
    diasDeAlquiler: Int,
    kilometrajeLibre: Boolean,
    val cilindrada: Int

) : Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre) {
    override fun costoParticular(): Double =
        if (cilindrada > cilindradaMax) (costoExtraPorCilindrada * diasDeAlquiler) else 0.0 //hacer algo mas general para el precio extra por cilindrada

    override fun <T> actualizarDatos(elemento: T) {
        val moto = elemento as Moto
//            marca = moto.marca
//            modelo = moto.modelo
//            anioFabricacion = moto.anioFabricacion
        costoDiario = moto.costoDiario
//            diasDeAlquiler = moto.diasDeAlquiler
        kilometrajeLibre = moto.kilometrajeLibre
//            cilindrada = moto.cilindrada
    }

    companion object {
        val cilindradaMax: Double = 250.0
        val costoExtraPorCilindrada: Double = 500.0
    }
}

class Auto(
    marca: String,
    modelo: String,
    anioFabricacion: Int,
    costoDiario: Double,
    diasDeAlquiler: Int,
    kilometrajeLibre: Boolean,
    val esHatchback: Boolean

) : Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre) {

    fun porcentajeHatchback() = if (esHatchback) 0.1 else 0.25
    override fun costoParticular() = costoBase() * porcentajeHatchback()

    override fun <T> actualizarDatos(elemento: T) {
        val auto = elemento as Auto
//            marca = auto.marca
//            modelo = auto.modelo
//            anioFabricacion = auto.anioFabricacion
        costoDiario = auto.costoDiario
//            diasDeAlquiler = auto.diasDeAlquiler
        kilometrajeLibre = auto.kilometrajeLibre
//            esHatchback = auto.esHatchback
    }
}


class Camioneta(
    marca: String,
    modelo: String,
    anioFabricacion: Int,
    costoDiario: Double,
    diasDeAlquiler: Int,
    kilometrajeLibre: Boolean,
    val esTodoTerreno: Boolean

) : Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre) {

    fun diasDeExceso() =
        maxOf(diasDeAlquiler - diasDeAlquilerMax, 0) //buscar algo mas general para la resta del 7 (seria maximo)

    fun costoPorExceso() = 10000 + (1000 * diasDeExceso())
    fun costoTodoTerreno() = if (esTodoTerreno) 1.5 else 1.0
    override fun costoParticular() = costoPorExceso() * costoTodoTerreno()

    override fun <T> actualizarDatos(elemento: T) {
        val camioneta = elemento as Camioneta
//            marca = camioneta.marca
//            modelo = camioneta.modelo
//            anioFabricacion = camioneta.anioFabricacion
        costoDiario = camioneta.costoDiario
//            diasDeAlquiler = camioneta.diasDeAlquiler
        kilometrajeLibre = camioneta.kilometrajeLibre
//            esTodoTerreno = camioneta.esTodoTerreno
    }

    companion object {
        val diasDeAlquilerMax: Int = 7
    }
}


