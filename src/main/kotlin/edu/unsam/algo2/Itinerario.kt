package edu.unsam.algo2

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    var dias: MutableList<DiaDeItinerario> // Lista de dias, cada dia tiene una lista de actividades
) {

    class DiaDeItinerario(var actividades: MutableList<Actividad>) {
        fun costo() = actividades.sumOf { actividad -> actividad.costo }

        fun duracion() = actividades.sumOf { actividad -> actividad.duracion() }
    }

    // Suma duracion de actividades y dividir por cantidad de dias
    fun duracionPromedioPorDia() = dias.sumOf { dia -> dia.duracion() } / dias.size

    fun cantidadDias() = dias.size

    /**
     * La dificultad de un itinerario está dada por las dificultades de las actividades diarias que lo componen,
     * es decir que si prevalecen más actividades de un tipo de dificultad, esta debería ser la del itinerario,
     * llegado el caso que las cantidades sean iguales, se establece la de mayor dificultad.
     */
    fun dificultad(): Actividad.Dificultad? {
        // Obtenemos todas las dificultades
        val dificultades = dias.flatMap { dia -> dia.actividades.map { actividad -> actividad.dificultad } } // [ALTA, BAJA, MEDIA, ALTA]
        val dificultadesAgrupadas = dificultades.groupingBy { it }.eachCount() //{ BAJA: 1, MEDIA: 1, ALTA: 2 }
        return dificultadesAgrupadas.maxWithOrNull { a, b ->
            // Compara el valor de la tupla
            var comparacion = a.value.compareTo(b.value) // 0, 1, -1

            if(comparacion == 0){ // Si tienen el mismo valor
                return@maxWithOrNull a.key.compareTo(b.key)  // Compara por dificultad
            }

            return@maxWithOrNull comparacion
        }?.key
    }

    fun costo() = dias.sumOf { dia -> dia.costo() }
}
