package edu.unsam.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    var nombre: String,
    var apellido: String,
    var username: String,
    var paisResidencia: String,
    val fechaAlta: LocalDate,
    var diasDisponibles: Int,
    var criterio: Criterio,
    var destinosDeseados: MutableList<Destino>,
    var vehiculoPreferencia: VehiculoPreferencia
): Identidad {
    val amigos: MutableList<Usuario> = mutableListOf()
    var destinosVisitados: MutableList<Destino> = mutableListOf()
    override var id: Int? = null

    fun leGusta(vehiculo: Vehiculo) = vehiculoPreferencia.leGusta(vehiculo)
    
    init {
        require(nombre.isNotBlank()) {
            "El nombre del usuario no puede ser nulo o vacio"
        }

        require(apellido.isNotBlank()) {
            "El apellido del usuario no puede ser nulo o vacio"
        }

        require(username.isNotBlank()) {
            "El username del usuario no puede ser nulo o vacio"
        }

        require(paisResidencia.isNotBlank()) {
            "El pais del usuario no puede ser nulo o vacio"
        }

        require(fechaAlta <= LocalDate.now()) {
            "La fecha de alta no puede ser posterior a la del día."
        }

        require(diasDisponibles > 0) {
            "Los días para viajar, deben ser mayores a cero."
        }
        require(destinosDeseados.size > 0) {
            "Todos los usuarios deben tener al menos un destino deseado."
        }
    }

    fun puntuar(itinerario: Itinerario, puntuacion: Int) {
        require(puntuacion in 1..10) {
            "Puntuacion solo puede ser un valor entre 1 y 10"
        }
        require(!itinerario.fuePuntuadoPor(this)) {
            "El usuario ya ha puntuado este itinerario"
        }
        itinerario.recibirPuntajeDe(this, puntuacion)
    }

    fun antiguedad() = ChronoUnit.YEARS.between(fechaAlta, LocalDate.now()).toInt()

    fun conoce(destino: Destino) = esDestinoDeseado(destino) || esDestinoVisitado(destino)

    fun esDestinoDeseado(destino: Destino): Boolean = destinosDeseados.contains(destino)

    fun esDestinoVisitado(destino: Destino): Boolean = destinosVisitados.contains(destino)

    fun puedePuntuar(itinerario: Itinerario): Boolean = !itinerario.fueCreadoPor(this) && conoce(itinerario.destino)

    fun puedeRealizar(itinerario: Itinerario): Boolean =
        diasDisponibles >= itinerario.cantidadDias() && criterio.puedeRealizar(itinerario)

    fun esDestinoCaro(destinoItinerario: Destino): Boolean =
        destinosDeseados.maxOf { destino -> destino.costo(this) } < destinoItinerario.costo(this)

    fun esAmigoDe(usuario: Usuario): Boolean = amigos.contains(usuario)

    fun algunAmigoConoce(destino: Destino): Boolean = amigos.any { it.conoce(destino) }

    /** El valor de búsqueda debe coincidir parcialmente con su nombre o apellido,
     * o exáctamente con su username.
     */
    override fun coincideCon(value: String): Boolean {
        return nombre.contains(value) || apellido.contains(value) || value == username
    }
}
