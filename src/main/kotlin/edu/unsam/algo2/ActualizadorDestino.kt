package edu.unsam.algo2

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ActualizadorDestino(var repositorio: Repositorio<Destino>, var serviceDestino: ServiceDestino) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun updateDestinos() {
        parseDestinos(serviceDestino.getDestinos())
            .forEach {
                actualizarOCrear(it)
            }
    }

    private fun actualizarOCrear(destino: Destino) {
        if (destino.esNuevo()) repositorio.create(destino)
        else repositorio.update(destino)
    }

    private fun parseDestinos(destinos: String): MutableList<Destino> {
        println(destinos)
        return gson.fromJson(
            destinos,
            object : TypeToken<MutableList<Destino>>() {}.type
        )
    }

}