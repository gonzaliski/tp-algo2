package edu.unsam.algo2

interface Funcionalidad {
    fun realizarAccion(viaje: Viaje, usuario: Usuario)
}

/* Enviar a los amigos que desean el destino del viaje un mail avisando que visitaron el destino. */
class AvisoPorMail(val mailSender: MailSender) : Funcionalidad {
    override fun realizarAccion(viaje: Viaje, usuario: Usuario) {
        usuario.amigosConDestinoDeseado(viaje.destino())
            .forEach { enviarMail(it, usuario, viaje.destino()) }
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
     proponer destinos locales para cuando quiera armar un nuevo viaje.
* */
class ModificarCriterio : Funcionalidad {
    override fun realizarAccion(viaje: Viaje, usuario: Usuario) {
        if (viaje.esLocal()) {
            usuario.criterio = Localista
        }
    }
}

/* Agregar a los itinerarios a puntuar el itinerario del viaje.*/
class AgregarParaPuntuar : Funcionalidad {
    override fun realizarAccion(viaje: Viaje, usuario: Usuario) {
        usuario.itinerariosAPuntuar.add(viaje.itinerario)
    }
}

/* TODO: Si el viaje no tiene un vehículo con convenio, el usuario se convierte en selectivo,
    y le pasan a gustar los vehículos de una marca con convenio. */