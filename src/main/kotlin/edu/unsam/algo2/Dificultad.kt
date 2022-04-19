package edu.unsam.algo2

enum class Dificultad {
    BAJA, MEDIA, ALTA
}

interface NivelDificultad {
    fun dificultades(): List<Dificultad>

    fun dificultadesAgrupadas() = dificultades().groupingBy { it }.eachCount().toSortedMap(compareByDescending { it })

    fun dificultad(): Dificultad = dificultadesAgrupadas().maxByOrNull { it.value }?.key ?: Dificultad.BAJA
}