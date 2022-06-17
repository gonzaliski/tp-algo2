package edu.unsam.algo2

class InvalidAction(mensaje: String) : IllegalAccessException(mensaje)

class InvalidIdException(mensaje: String) : IllegalArgumentException(mensaje)

class InvalidElementException(mensaje: String) : NullPointerException(mensaje)
