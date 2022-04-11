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
    var destinosDeseados: MutableList<Destino>
) {
    val amigos: MutableList<Usuario> = mutableListOf()
    var destinosVisitados: MutableList<Destino> = mutableListOf()

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
        require(!itinerario.puntuaciones.keys.contains(this)) {
            "El usuario ya ha puntuado este itinerario"
        }
        itinerario.puntuaciones[this] = puntuacion


    }

    fun antiguedad() = ChronoUnit.YEARS.between(fechaAlta, LocalDate.now()).toInt()

    fun conoce(destino: Destino) = destinosDeseados.contains(destino) || destinosVisitados.contains(destino)

    fun puedePuntuar(itinerario: Itinerario): Boolean = itinerario.creador != this && conoce(itinerario.destino)

    fun puedeRealizar(itinerario: Itinerario): Boolean =
        diasDisponibles >= itinerario.cantidadDias() && criterio.puedeRealizar(itinerario)

    fun esDestinoCaro(destinoItinerario: Destino): Boolean =
        destinosDeseados.maxOf { destino -> destino.costo(this) } < destinoItinerario.costo(this)

    fun esAmigoDe(usuario: Usuario): Boolean = amigos.contains(usuario)
}

interface Criterio {
    fun puedeRealizar(itinerario: Itinerario): Boolean
}


object Relajado : Criterio {

    override fun puedeRealizar(itinerario: Itinerario): Boolean = true
}
//voy
class Precavido(val usuario: Usuario) : Criterio {

    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return usuario.conoce(itinerario.destino) || usuario.amigos.any { it.conoce(itinerario.destino) }
    }
}

object Localista : Criterio {
    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return itinerario.destino.esLocal() // O igual a Argentina?
    }
}

class Soniador(val usuario: Usuario) : Criterio {
    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return usuario.destinosDeseados.contains(itinerario.destino) || usuario.esDestinoCaro(itinerario.destino)
    }
}

object Activo : Criterio {
    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return itinerario.tieneActividadesTodosLosDias()
    }
}

class Exigente(var porcentaje: Double, var dificultad: Actividad.Dificultad) : Criterio {
    override fun puedeRealizar(
        itinerario: Itinerario
    ): Boolean {
        return itinerario.porcentajeDeActividades(dificultad) >= porcentaje
    }
}
