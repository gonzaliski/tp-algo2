package edu.unsam.algo2

interface Criterio {
    fun puedeRealizar(itinerario: Itinerario): Boolean
}

object Relajado : Criterio {

    override fun puedeRealizar(itinerario: Itinerario): Boolean = true
}

class Precavido(val usuario: Usuario) : Criterio {

    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return usuario.conoce(itinerario.destino) || usuario.algunAmigoConoce(itinerario.destino)
    }
}

object Localista : Criterio {
    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return itinerario.tieneDestinoLocal()
    }
}

class Soniador(val usuario: Usuario) : Criterio {
    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return usuario.esDestinoDeseado(itinerario.destino) || usuario.esDestinoCaro(itinerario.destino)
    }
}

object Activo : Criterio {
    override fun puedeRealizar(itinerario: Itinerario): Boolean {
        return itinerario.tieneActividadesTodosLosDias()
    }
}

class Exigente(var porcentaje: Double, var dificultad: Dificultad) : Criterio {
    override fun puedeRealizar(
        itinerario: Itinerario
    ): Boolean {
        return itinerario.porcentajeDeActividades(dificultad) >= porcentaje
    }
}