package edu.unsam.algo2

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    var dias: MutableList<DiaDeItinerario> = mutableListOf(),
    var puntuaciones: MutableMap<Usuario, Int> = mutableMapOf()

) {

    init {
        require(cantidadDias() > 0) {
            "Las actividades del dia deben tener al menos 1 actividad "
        }
        require(dias.all { dia -> !seSolapanActividades(dia.actividades) }) {
            "Los horarios de las actividades no se pueden solapar"
        }

    }

    fun actividadesOrdenadas(actividades: MutableList<Actividad>) = actividades.sortedBy { actividad -> actividad.inicio }

    fun seSolapanActividades(actividades: MutableList<Actividad>): Boolean {
        val actividadesOrdenadas = actividadesOrdenadas(actividades)

        val resultList = actividadesOrdenadas.mapIndexed { index, actividad ->

            if(index != actividadesOrdenadas.lastIndex){
              return@mapIndexed ( actividad.seSolapaCon( actividadesOrdenadas[index+1] ))
            }else{ return@mapIndexed false}
        }
        return resultList.any { it }
    }

    class DiaDeItinerario(var actividades: MutableList<Actividad>) {
        fun costo() = actividades.sumOf { actividad -> actividad.costo }

        fun duracion() = actividades.sumOf { actividad -> actividad.duracion() }
    }

    // Suma duracion de actividades y dividir por cantidad de dias
    fun duracionPromedioPorDia() = dias.sumOf { dia -> dia.duracion() } / cantidadDias()

    fun cantidadDias() = dias.size

    /**
     * La dificultad de un itinerario está dada por las dificultades de las actividades diarias que lo componen,
     * es decir que si prevalecen más actividades de un tipo de dificultad, esta debería ser la del itinerario,
     * llegado el caso que las cantidades sean iguales, se establece la de mayor dificultad.
     */

    fun dificultadesItinerario() = dias.flatMap { dia -> dia.actividades.map { actividad -> actividad.dificultad } }

    fun dificultadesAgrupadas() = dificultadesItinerario().groupBy { it }

    fun dificultad(): Actividad.Dificultad?{
        val dificultadMaxima = dificultadesAgrupadas().maxWithOrNull(compareBy(
            {it.value.size}, // Primero comparo por cantidad de veces que se repite (tamanio de la lista)
            {it.key} // En caso de empate comparo por valor de dificultad: BAJA < MEDIA < ALTA
        ))

        return dificultadMaxima?.key
    }

    fun costo() = dias.sumOf { dia -> dia.costo() }

    fun tieneActividadesTodosLosDias(): Boolean = dias.all { dia -> dia.actividades.isNotEmpty() }

    fun actividades() = dias.flatMap { dia -> dia.actividades }

    fun actividadesPorDia() = dias.map { dia -> dia.actividades }

    fun actividadesDeDificultad(dificultad: Actividad.Dificultad) =
        actividades().count { actividad -> actividad.dificultad == dificultad }

    fun cantidadTotalActividades() = actividades().size

    fun porcentajeDeActividades(dificultad: Actividad.Dificultad) =
        actividadesDeDificultad(dificultad) / cantidadTotalActividades()

    fun puedeSerEditadoPor(usuario: Usuario): Boolean =
        fueCreadoPor(usuario) || (creador.esAmigoDe(usuario) && usuario.conoce(destino))

    fun fueCreadoPor(usuario: Usuario): Boolean = usuario == creador
}
