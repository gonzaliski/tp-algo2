package edu.unsam.algo2

class Destino(
    var pais: String,
    var ciudad: String,
    val costoBase: Double
) {

    fun esLocal() =
        pais.lowercase().trim() == "Argentina".lowercase().trim()

    fun costo(usuario: Usuario): Double {
        var costo: Double = costoBase

        if (!esLocal()) {
            costo *= 1.2
        }
        if (usuario.paisResidencia == pais) {
            costo -= (0.01 * costoBase) * minOf(usuario.antiguedad(), 15)
        }

        return costo
    }

}
