package edu.unsam.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    var nombre: String,
    val fechaAlta: LocalDate,
    var diasDisponibles: Int,
    var criterio: Criterio
) {
    val amigos: MutableList<Usuario> = mutableListOf()
    var destinosDeseados: MutableList<Destino> = mutableListOf()
    var destinosVisitados: MutableList<Destino> = mutableListOf()

    init{
        require(nombre.isNotBlank()){
            "El nombre del usuario no puede ser nulo o vacio"
        }

        require(fechaAlta <= LocalDate.now()){
            "La fecha de alta no puede ser posterior a la del día."
        }
        require(diasDisponibles > 0){
            "Los días para viajar, deben ser mayores a cero."
        }
        require(destinosDeseados.size > 0){
            "Todos los usuarios deben tener al menos un destino deseado."
        }
    }

    var apellido: String = "a"
        get() = field
        set(value){
            if(value.isNotEmpty()){
                field = value
            }else{
                throw error("El apellido del usuario no puede ser nulo o vacio")
            }
        }
    var username: String = ""
        get() = field
        set(value){
            if(!value.isEmpty()){
                field = value
            }else{
                throw error("El username del usuario no puede ser nulo o vacio")
            }
        }
    var paisResidencia: String = ""
        get() = field
        set(value){
            if(!value.isEmpty()){
                field = value
            }else{
                throw error("El pais de residencia del usuario no puede ser nulo o vacio")
            }
        }




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
