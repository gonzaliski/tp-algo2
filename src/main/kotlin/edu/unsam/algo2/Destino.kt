package edu.unsam.algo2

class Destino(
    var ciudad: String,
    var pais: String,
    var costoBase: Double
) {

    init {
        require(ciudad.isNotBlank()) {
            "Nombre de ciudad no puede ser nulo o vacio"
        }

        require(pais.isNotBlank()) {
            "Nombre de pais no puede ser nulo o vacio"
        }

        require(costoBase > 0.0) {
            "Costo base no puede ser menor o igual a 0"
        }
    }


    companion object {
        var LOCAL = "Argentina"
    }

    fun esLocal() =
        pais.lowercase().trim() == LOCAL.lowercase().trim()

    fun costo(usuario: Usuario): Double {
        return costoBase + recargo(costoBase) - descuentoPara(usuario)
    }

    fun recargo(costo: Double): Double = if (!esLocal()) costo * 0.2 else 0.0

    fun descuentoPara(usuario: Usuario): Double =
        if (usuario.paisResidencia == pais) (0.01 * costoBase) * minOf(usuario.antiguedad(), 15) else 0.0
}