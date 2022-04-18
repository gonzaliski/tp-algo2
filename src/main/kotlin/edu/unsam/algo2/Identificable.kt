package edu.unsam.algo2

interface Identificable {
    var id: Int?

    fun coincideCon(value: String): Boolean
}
