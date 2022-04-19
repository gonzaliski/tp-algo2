package edu.unsam.algo2

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    var dias: MutableList<DiaDeItinerario> = mutableListOf(),
    var puntuaciones: MutableMap<Usuario, Int> = mutableMapOf()

): NivelDificultad {

    init {
        require(cantidadDias() > 0) {
            "Las actividades del dia deben tener al menos 1 actividad "
        }
        require(!seSolapanActividades()) {
            "Los horarios de las actividades no se pueden solapar"
        }

    }

    fun actividadesOrdenadas(actividades: MutableList<Actividad>) =
        actividades.sortedBy { actividad -> actividad.inicio }

    fun seSolapanActividades(): Boolean = dias.any { dia -> dia.seSolapanActividades() }

    // Suma duracion de actividades y dividir por cantidad de dias
    fun duracionPromedioPorDia() = dias.sumOf { dia -> dia.duracion() } / cantidadDias()

    fun cantidadDias() = dias.size

    /**
     * La dificultad de un itinerario está dada por las dificultades de las actividades diarias que lo componen,
     * es decir que si prevalecen más actividades de un tipo de dificultad, esta debería ser la del itinerario,
     * llegado el caso que las cantidades sean iguales, se establece la de mayor dificultad.
     */

    override fun dificultades() = dias.map { it.dificultad() }

    fun costo() = dias.sumOf { dia -> dia.costo() }

    fun tieneActividadesTodosLosDias(): Boolean = dias.all { dia -> dia.tieneActividades() }

    fun actividades() = dias.flatMap { dia -> dia.actividades }

    fun actividadesPorDia() = dias.map { dia -> dia.actividades }

    fun actividadesDeDificultad(dificultad: Dificultad) =
        actividades().count { actividad -> actividad.dificultad == dificultad }

    fun cantidadTotalActividades() = actividades().size

    fun porcentajeDeActividades(dificultad: Dificultad): Double =
        (actividadesDeDificultad(dificultad) / cantidadTotalActividades().toDouble()) * 100

    fun puedeSerEditadoPor(usuario: Usuario): Boolean =
        fueCreadoPor(usuario) || (creador.esAmigoDe(usuario) && usuario.conoce(destino))

    fun fueCreadoPor(usuario: Usuario): Boolean = usuario == creador

    fun recibirPuntajeDe(usuario: Usuario, puntuacion: Int) {
        puntuaciones[usuario] = puntuacion
    }

    fun fuePuntuadoPor(usuario: Usuario): Boolean = puntuaciones.keys.contains(usuario)

    fun tieneDestinoLocal(): Boolean = destino.esLocal()
}

class DiaDeItinerario(var actividades: MutableList<Actividad>): NivelDificultad {
    fun costo() = actividades.sumOf { actividad -> actividad.costo }

    fun duracion() = actividades.sumOf { actividad -> actividad.duracion() }

    fun tieneActividades() = actividades.isNotEmpty()

    fun seSolapanActividades() = actividades.any { actividad ->
        val actividadesSinActividad = actividadesSin(actividad)
        actividad.seSolapaConAlguna(actividadesSinActividad)
    }

    /** Devuelve una lista de actividades pero sin la actividad recibida */
    fun actividadesSin(actividad: Actividad) = actividades.filterNot { it == actividad }

    override fun dificultades() = actividades.map { it.dificultad }
}