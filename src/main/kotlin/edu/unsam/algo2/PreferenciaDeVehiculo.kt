package edu.unsam.algo2

interface PreferenciaDeVehiculo {
    fun leGusta(vehiculo: Vehiculo): Boolean
}

object Neofilo : PreferenciaDeVehiculo {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.noEsMuyAntiguo()
}

/** solo quiere vehículos fabricados en años pares*/
object Supersticioso : PreferenciaDeVehiculo {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.anioFabricacionPar()
}

/** le gustan los vehículos cuya inicial de la marca, coincida con la inicial del modelo. */
object Caprichoso : PreferenciaDeVehiculo {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.coincidenInciales()
}

/** siempre que el vehículo sea de una marca específica.*/
class Selectivo(private val marca: String) : PreferenciaDeVehiculo {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.esDeMarca(marca)
}

/** quiere que tenga el kilometraje libre*/
object SinLimite : PreferenciaDeVehiculo {
    override fun leGusta(vehiculo: Vehiculo): Boolean = vehiculo.kilometrajeLibre
}

/** es la combinación de 2 ó más, de las formas de determinar mencionadas antes, en cuyo caso, para que le
 * guste un vehículo se tienen que cumplir todas las condiciones impuestas por las distintas formas.*/
class Combinado(private val preferencias: MutableList<PreferenciaDeVehiculo>) : PreferenciaDeVehiculo {
    override fun leGusta(vehiculo: Vehiculo): Boolean =
        preferencias.all { preferencia -> preferencia.leGusta(vehiculo) }
    fun addPreferencia(preferenciaDeVehiculo: PreferenciaDeVehiculo) =
        if(!preferencias.contains(preferenciaDeVehiculo))preferencias.add(preferenciaDeVehiculo) else println("Ya tiene esta preferencia")
    fun removePreferencia(preferenciaDeVehiculo: PreferenciaDeVehiculo) =
        if(preferencias.contains(preferenciaDeVehiculo))preferencias.remove(preferenciaDeVehiculo) else println("No posee esta preferencia")
}