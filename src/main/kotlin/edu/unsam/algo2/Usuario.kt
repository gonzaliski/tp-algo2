package edu.unsam.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    val nombre: String,
    val apellido: String,
    val username: String,
    val fechaAlta: LocalDate,
    var paisResidencia: String,
    var diasDisponibles: Int
) {

    val amigos: MutableList<Usuario> = mutableListOf()
    val destinosDeseados: MutableList<Destino> = mutableListOf()
    val destinosVisitados: MutableList<Destino> = mutableListOf()

    fun antiguedad() = ChronoUnit.YEARS.between(fechaAlta, LocalDate.now()).toInt()

    fun conoce(destino: Destino) = destinosDeseados.contains(destino) || destinosVisitados.contains(destino)

    fun puedePuntuar(itinerario: Itinerario): Boolean = itinerario.creador != this && conoce(itinerario.destino)

}