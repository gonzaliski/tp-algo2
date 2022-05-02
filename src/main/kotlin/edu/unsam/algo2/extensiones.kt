package edu.unsam.algo2

import java.time.LocalTime

fun LocalTime.isBetween(start: LocalTime, end: LocalTime) = this > start && this < end

fun Int.isEven() = this % 2 == 0

/**
 * Valida que la colección no contenga exactamente
 * una cierta cantidad de elementos de la otra.
 *
 * Lanza una excepción ShouldNotContainExactly
 */
fun <E> Collection<E>.shouldNotContain(exactly: Int, ofCollection: Collection<E>) {
    val notContained: Int = ofCollection.count { !this.contains(it) }
    if (notContained != exactly)
        throw ShouldNotContainExactly("Collection should not contain exactly $exactly of $ofCollection")
}