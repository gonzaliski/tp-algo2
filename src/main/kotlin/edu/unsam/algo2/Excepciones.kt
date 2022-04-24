package edu.unsam.algo2

class InvalidIdException(val id :Int?, val mensaje: String) : IllegalArgumentException("Error ID: $id. $mensaje")  {
}

class InvalidElementException(val mensaje: String) : NullPointerException("Elemento invalido: $mensaje")  {
}