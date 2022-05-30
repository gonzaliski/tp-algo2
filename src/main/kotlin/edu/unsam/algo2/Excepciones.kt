package edu.unsam.algo2

class InvalidAction(usuario: Usuario, itinerario: Itinerario) :
    IllegalAccessException("Acción inválida: ${usuario.username} no puede puntuar el itinerario de ${itinerario.creador}")

class InvalidIdException(id: Int?, mensaje: String) : IllegalArgumentException("Error ID: $id. $mensaje")

class InvalidElementException(mensaje: String) : NullPointerException("Elemento invalido: $mensaje")
