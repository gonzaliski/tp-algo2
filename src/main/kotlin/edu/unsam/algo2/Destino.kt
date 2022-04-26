package edu.unsam.algo2

class Destino(
    var ciudad: String,
    var pais: String,
    var costoBase: Double
) : Entidad {

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
        pais.trim().equals(LOCAL.trim(), ignoreCase = true)

    fun costo(usuario: Usuario): Double {
        return costoBase + recargo(costoBase) - descuentoPara(usuario)
    }

    fun recargo(costo: Double): Double = if (!esLocal()) costo * 0.2 else 0.0

    fun descuentoPara(usuario: Usuario): Double =
        if (usuario.paisResidencia.equals(pais, ignoreCase = true)) (0.01 * costoBase) * minOf(
            usuario.antiguedad(),
            15
        ) else 0.0

    override var id: Int? = null

    /**El valor de búsqueda debe coincidir parcialmente con el nombre del país, o con el nombre de la ciudad.*/
    override fun coincideCon(value: String): Boolean {
        return pais.contains(value, ignoreCase = true) || ciudad.contains(value, ignoreCase = true)
    }

    override fun <T> actualizarDatos(elemento: T) {
        with(elemento as Destino) {
            ciudad = elemento.ciudad
            pais = elemento.pais
            costoBase = elemento.costoBase
        }
    }
}