package edu.unsam.algo2

import java.time.LocalTime
import java.time.temporal.ChronoUnit

class Actividad(

    var dificultad: Dificultad = Dificultad.BAJA
) {
    var descripcion: String = ""
        get() = field
        set(value){
            if(!value.isEmpty()){
                field = value
            }else{
                throw error("Descripcion de la actividad ser nulo o vacio")
            }
        }

    var inicio: LocalTime = LocalTime.now()
        get() = field
        set(value){
            if(value < fin){
                field = value
            }else{
                throw error("El horario de inicio debe ser menor que el de fin")
            }
        }
    var fin: LocalTime = LocalTime.now()
        get() = field
        set(value){
            if(value > inicio){
                field = value
            }else{
                throw error("El horario de fin debe ser mayor que el de inicio")
            }
        }
    var costo: Double = 0.0
        get() = field
        set(value){
            if(value >= 0){
                field = value
            }else{
                throw error("Costo no puede ser menor a 0")
            }
        }
    fun duracion() = ChronoUnit.MINUTES.between(fin, inicio)

    enum class Dificultad(valor: Int) {
        BAJA(1), MEDIA(2), ALTA(3)
    }
}