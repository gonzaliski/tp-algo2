package edu.unsam.algo2

abstract class Tarea(var nombre: String, val mailSender: MailSender) {

    fun execute(usuario: Usuario) {
        doExecute(usuario)
        enviarMail(usuario)
    }

    fun enviarMail(usuario: Usuario) {
        mailSender.sendMail(Mail(nombre, usuario.email, mensaje(), mensaje()))
    }

    fun mensaje() = "Se realizo la tarea: $nombre"

    abstract fun doExecute(usuario: Usuario)
}

class PuntuarItinerarioTarea(nombre: String, mailSender: MailSender, val puntuacion: Int) : Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        usuario.puntuarTodos(puntuacion)
    }
}

class TransferirItinerariosTarea(nombre: String, mailSender: MailSender) : Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        usuario.transferirItinerarios(usuario.amigoConMenorDestinosVisitados())
    }
}

// TODO: Acá también, crear u
class HacerseAmigoTarea(nombre: String, mailSender: MailSender, val usuarios: List<Usuario>, val destino: Destino) :
    Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        usuariosQueConocenDestino().forEach { usr -> usuario.agregarAmigo(usr) }
    }

    fun usuariosQueConocenDestino() = usuarios.filter { usr -> usr.conoce(destino) }
}

class agregarDestinoMasCaroTarea(nombre: String, mailSender: MailSender) : Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        usuario.agregarDestinosDeseados(destinoMasCaroDeAmigos(usuario))
    }


    fun destinoMasCaroDeAmigos(usuario: Usuario): List<Destino> = usuario.amigos.map { amigo -> amigo.destinoMasCaro() }
}