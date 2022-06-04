package edu.unsam.algo2

import java.time.LocalDate

abstract class Vehiculo(
    var marca: String,
    var modelo: String,
    var anioFabricacion: Int,
    var costoDiario: Double,
    var diasDeAlquiler: Int,
    var kilometrajeLibre: Boolean
) : Entidad {
    init {
        validarEntidad()
    }

    override fun validarEntidad() {
        require(marca.isNotBlank()) {
            "Los vehiculos deben tener una marca."
        }
        require(modelo.isNotBlank()) {
            "Los vehiculos deben tener un modelo."
        }
        require(anioFabricacion > 1920) {
            "Los vehiculos deben tener un año de fabricacion valido."
        }
        require(costoDiario > 0) {
            "Los vehiculos deben tener un costo diario mayor a 0."
        }
        require(diasDeAlquiler > 0) {
            "Los vehiculos deben tener por lo menos 1 dia de alquiler."
        }
    }

    override var id: Int = Entidad.ID_INICIAL

    companion object {
        fun primerEmpresaConConvenio(): String = empresasConvenio.first()

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

    override fun <T> actualizarDatos(elemento: T) {
        actualizacionGeneral(elemento)
        actualizacionExtra(elemento)
    }

    abstract fun <T> actualizacionExtra(elemento: T)

    private fun <T> actualizacionGeneral(elemento: T) {
        (elemento as Vehiculo).let {
            marca = it.marca
            modelo = it.modelo
            anioFabricacion = it.anioFabricacion
            costoDiario = it.costoDiario
            diasDeAlquiler = it.diasDeAlquiler
            kilometrajeLibre = it.kilometrajeLibre
        }
    }

}


class Moto(
    marca: String,
    modelo: String,
    anioFabricacion: Int,
    costoDiario: Double,
    diasDeAlquiler: Int,
    kilometrajeLibre: Boolean,
    var cilindrada: Int

) : Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre) {
    override fun costoParticular(): Double =
        if (cilindrada > cilindradaMax) (costoExtraPorCilindrada * diasDeAlquiler) else 0.0 //hacer algo mas general para el precio extra por cilindrada

    override fun <T> actualizacionExtra(elemento: T) {
        (elemento as Moto).let { cilindrada = it.cilindrada }
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
    var esHatchback: Boolean

) : Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre) {

    fun porcentajeHatchback() = if (esHatchback) 0.1 else 0.25
    override fun costoParticular() = costoBase() * porcentajeHatchback()

    override fun <T> actualizacionExtra(elemento: T) {
        (elemento as Auto).let { esHatchback = it.esHatchback }
    }
}


class Camioneta(
    marca: String,
    modelo: String,
    anioFabricacion: Int,
    costoDiario: Double,
    diasDeAlquiler: Int,
    kilometrajeLibre: Boolean,
    var esTodoTerreno: Boolean

) : Vehiculo(marca, modelo, anioFabricacion, costoDiario, diasDeAlquiler, kilometrajeLibre) {

    fun diasDeExceso() =
        maxOf(diasDeAlquiler - diasDeAlquilerMax, 0) //buscar algo mas general para la resta del 7 (seria maximo)

    fun costoPorExceso() = 10000 + (1000 * diasDeExceso())
    fun costoTodoTerreno() = if (esTodoTerreno) 1.5 else 1.0
    override fun costoParticular() = costoPorExceso() * costoTodoTerreno()

    override fun <T> actualizacionExtra(elemento: T) {
        (elemento as Camioneta).let { esTodoTerreno = it.esTodoTerreno }
    }

    companion object {
        val diasDeAlquilerMax: Int = 7
    }
}


