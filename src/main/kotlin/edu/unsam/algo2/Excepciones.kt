package edu.unsam.algo2

class InvalidAction(mensaje: String) : IllegalAccessException(mensaje)

class InvalidIdException(mensaje: String) : IllegalArgumentException(mensaje)

class InvalidElementException(mensaje: String) : NullPointerException(mensaje)

class SinDestinosDeseadosException(mensaje: String): NullPointerException(mensaje)

class SinDestinosVisitadosException(mensaje: String): NullPointerException(mensaje)