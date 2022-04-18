package edu.unsam.algo2

interface VehiculoPreferencia {
    fun leGusta(vehiculo: Vehiculo): Boolean
}

object Neofilo : VehiculoPreferencia {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.antiguedad() < 2
}

/** solo quiere vehículos fabricados en años pares*/
object Supersticioso : VehiculoPreferencia {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.anioFabricacion.isEven()
}

/** le gustan los vehículos cuya inicial de la marca, coincida con la inicial del modelo. */
object Caprichoso : VehiculoPreferencia {
    override fun leGusta(vehiculo: Vehiculo): Boolean =
        vehiculo.marca.trim().first() == vehiculo.modelo.trim().first() // TODO: Delegar a Vehiculo
    // vehiculo.coincidenInciales()
}

/** siempre que el vehículo sea de una marca específica.*/
class Selectivo(private val marca: String) : VehiculoPreferencia {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.marca == marca
}

/** quiere que tenga el kilometraje libre*/
object SinLimite : VehiculoPreferencia {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.kilometrajeLibre
}

/** es la combinación de 2 ó más, de las formas de determinar mencionadas antes, en cuyo caso, para que le
 * guste un vehículo se tienen que cumplir todas las condiciones impuestas por las distintas formas.*/
class Combinado(private val preferencias: MutableList<VehiculoPreferencia>) : VehiculoPreferencia {
    override fun leGusta(vehiculo: Vehiculo): Boolean =
        preferencias.all { preferencia -> preferencia.leGusta(vehiculo) }
}