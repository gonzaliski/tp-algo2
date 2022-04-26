package edu.unsam.algo2

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

class ActualizadorDestino(var repositorio: Repositorio<Destino> = Repositorio()) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    lateinit var serviceDestino: ServiceDestino

    fun updateDestinos() {
        parseDestinos(serviceDestino.getDestinos())
            .forEach {
                if(it.id != null) repositorio.update(it)
                else repositorio.create(it)
            }
    }

    private fun parseDestinos(destinos: String): MutableList<Destino> {
        println(destinos)
        return gson.fromJson(
            destinos,
            object : TypeToken<MutableList<Destino>>() {}.type
        )
    }

}