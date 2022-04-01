package edu.unsam.algo2

class Destino(
) {
    var ciudad: String = "Runcuncun"
        get() = field
        set(value){
            if(!value.isEmpty()){
                field = value
            }else{
                throw error("Nombre de ciudad no puede ser nulo o vacio")
            }
        }
    var pais: String = "Argentina"
        get() = field
        set(value){
            if(!value.isEmpty()){
                field = value
            }else{
                throw error("Nombre de pais no puede ser nulo o vacio")
            }
        }
    var costoBase: Double = 3.0
        get() = field
        set(value){
            if(value > 0){
                field = value
            }else{
                throw error("Costo base no puede ser menor o igual a 0")
            }
        }
    companion object {
        var LOCAL = "Argentina"
    }
    
    fun esLocal() =
        pais.lowercase().trim() == LOCAL.lowercase().trim()

    fun costo(usuario: Usuario): Double {
        var costo: Double = costoBase
        return costo + recargo(costo) - descuentoPara(usuario, costo)
    }

    fun recargo(costo: Double): Double = if (!esLocal()) costo * 0.2 else 0.0
    fun descuentoPara(usuario: Usuario, costo: Double): Double =
        if (usuario.paisResidencia == pais) (0.01 * costoBase) * minOf(usuario.antiguedad(), 15) else 0.0
}
