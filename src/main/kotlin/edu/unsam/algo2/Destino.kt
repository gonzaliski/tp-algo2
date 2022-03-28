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
        return costo + recargo(costo) - descuentoPara(usuario, costo)
    }
    fun recargo(costo:Double):Double = if (!esLocal())  costo *0.2 else 0.0
    fun descuentoPara(usuario: Usuario, costo: Double):Double = if (usuario.paisResidencia == pais)  (0.01 * costoBase) * minOf(usuario.antiguedad(), 15) else 0.0
}
