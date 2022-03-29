package edu.unsam.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    val nombre: String,
    val apellido: String,
    val username: String,
    val fechaAlta: LocalDate,
    var paisResidencia: String,
    var diasDisponibles: Int,
    var criterio: Criterio
) {

    val amigos: MutableList<Usuario> = mutableListOf()
    val destinosDeseados: MutableList<Destino> = mutableListOf()
    val destinosVisitados: MutableList<Destino> = mutableListOf()

    fun antiguedad() = ChronoUnit.YEARS.between(fechaAlta, LocalDate.now()).toInt()

    fun conoce(destino: Destino) = destinosDeseados.contains(destino) || destinosVisitados.contains(destino)

    fun puedePuntuar(itinerario: Itinerario): Boolean = itinerario.creador != this && conoce(itinerario.destino)

    fun puedeRealizar(itinerario: Itinerario): Boolean =
        diasDisponibles >= itinerario.cantidadDias() && criterio.puedeRealizar(itinerario, this)

    fun esDestinoCaro(destinoItinerario: Destino): Boolean =
        destinosDeseados.maxOf { destino -> destino.costo(this) } > destinoItinerario.costo(this)
}

sealed class Criterio() {
    abstract fun puedeRealizar(itinerario: Itinerario, usuario: Usuario): Boolean

    object Relajado : Criterio() {
        override fun puedeRealizar(itinerario: Itinerario, usuario: Usuario): Boolean = true
    }

    object Precavido : Criterio() {
        override fun puedeRealizar(itinerario: Itinerario, usuario: Usuario): Boolean {
            return usuario.conoce(itinerario.destino) || usuario.amigos.any { it.conoce(itinerario.destino) }
        }
    }

    object Localista : Criterio() {
        override fun puedeRealizar(itinerario: Itinerario, usuario: Usuario): Boolean {
            return itinerario.destino.esLocal() // O igual a Argentina?
        }
    }

    object Soniador : Criterio() {
        override fun puedeRealizar(itinerario: Itinerario, usuario: Usuario): Boolean {
            return usuario.destinosDeseados.contains(itinerario.destino) || usuario.esDestinoCaro(itinerario.destino)
        }
    }

    object Activo : Criterio() {
        override fun puedeRealizar(itinerario: Itinerario, usuario: Usuario): Boolean {
            return itinerario.tieneActividadesTodosLosDias()
        }
    }

    class Exigente(var porcentaje: Double, var dificultad: Actividad.Dificultad) : Criterio() {
        override fun puedeRealizar(
            itinerario: Itinerario,
            usuario: Usuario
        ): Boolean {
            return itinerario.porcentajeDeActividades(dificultad) >= porcentaje
        }
    }
}
