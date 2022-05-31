package edu.unsam.algo2

class Viaje(
    var itinerario: Itinerario,
    val vehiculo: Vehiculo
) {

    fun costo(): Double = itinerario.costo() + vehiculo.costoTotal()

    fun esLocal() = itinerario.tieneDestinoLocal()

    fun destino() = itinerario.destino

}

