package edu.unsam.algo2

class Destino(
    var pais: String,
    var ciudad: String,
    val costo_base: Double
) {

    fun esLocal() =
        pais.lowercase().trim() == "Argentina".lowercase().trim()

    fun costo(usuario: Usuario): Double {
        var costo: Double = costo_base

        if (!esLocal()) {
            costo *= 1.2
        }
        if (usuario.pais_residencia == pais) {
            costo -= (0.01 * costo_base) * minOf(usuario.antiguedad(), 15)
        }

        return costo
    }

}
