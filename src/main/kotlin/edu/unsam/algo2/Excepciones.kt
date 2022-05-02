package edu.unsam.algo2

class InvalidIdException(id :Int?, mensaje: String) : IllegalArgumentException("Error ID: $id. $mensaje")

class InvalidElementException(mensaje: String) : NullPointerException("Elemento invalido: $mensaje")
