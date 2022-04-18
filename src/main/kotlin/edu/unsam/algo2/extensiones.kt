package edu.unsam.algo2

import java.time.LocalTime

fun LocalTime.isBetween(start: LocalTime, end: LocalTime) = this > start && this < end

fun Int.isEven() = this % 2 == 0