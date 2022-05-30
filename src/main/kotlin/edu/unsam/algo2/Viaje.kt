package edu.unsam.algo2

class Viaje(
    var usuario: Usuario,
    var itinerario: Itinerario,
    val vehiculo: Vehiculo
) {

    fun costo(): Double = itinerario.costo() + vehiculo.costoTotal()

    fun esLocal() = itinerario.tieneDestinoLocal()

    fun destino() = itinerario.destino

    fun usuario() = itinerario.creador

}

interface Funcionalidad {
    var activo: Boolean
    fun realizar(viaje: Viaje)
}

class AvisoPorMail(override var activo: Boolean, var amigos: List<Usuario>, val mailSender: MailSender) : Funcionalidad {
    override fun realizar(viaje: Viaje) {
        amigos.forEach { enviarMail(it, viaje.usuario(), viaje.destino()) }
    }

    fun enviarMail(receptor: Usuario, emisor: Usuario, destino: Destino) {
        mailSender.sendMail(
            Mail(
                from = "app@holamundo.com",
                to = receptor.username,
                subject = "Visitaron un destino que te puede interesar",
                content = "Hola! ${receptor.nombre}, ${emisor.nombre + emisor.apellido} visitó ${destino}."
            )
        )

    }

}

/*
     Si el viaje no es local, modificar el criterio de selección
     de itinerario a localista, esto le asegura al usuario que la app va a
     proponer destinos locales para cuando quiera  armar un nuevo viaje).
* */
class ModificarCriterio(override var activo: Boolean) : Funcionalidad {
    override fun realizar(viaje: Viaje) {
        if (viaje.esLocal()) {
            viaje.usuario().criterio = Localista
        }
    }
}

// Agregar a los itinerarios a puntuar el itinerario del viaje.

class Puntuar(override var activo: Boolean, var puntaje: Int): Funcionalidad {
    override fun realizar(viaje: Viaje) {
        viaje.usuario().puntuar(viaje.itinerario, puntaje)
    }
}

