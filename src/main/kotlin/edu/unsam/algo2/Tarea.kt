package edu.unsam.algo2

abstract class Tarea(var nombre: String, private val mailSender: MailSender) {

    fun execute(usuario: Usuario) {
        doExecute(usuario)
        enviarMail(usuario)
    }

    fun enviarMail(usuario: Usuario) {
        mailSender.sendMail(Mail(nombre, usuario.email, mensaje(), mensaje()))
    }

    private fun mensaje() = "Se realizo la tarea: $nombre"

    abstract fun doExecute(usuario: Usuario)
}

class PuntuarItinerarioTarea(nombre: String, mailSender: MailSender, private val puntuacion: Int) :
    Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        usuario.puntuarTodos(puntuacion)
    }
}

class TransferirItinerariosTarea(
    nombre: String,
    mailSender: MailSender,
    private val repositorioDeItinerarios: RepositorioDeItinerarios,
) : Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        val usuarioReceptor = usuario.amigoConMenorDestinosVisitados()
        repositorioDeItinerarios.transferirItinerarios(usuario, usuarioReceptor)
    }
}

class HacerseAmigoTarea(
    nombre: String,
    mailSender: MailSender,
    private val repositorioDeUsuarios: RepositorioDeUsuarios,
    val destino: Destino
) :
    Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        val usuarios = repositorioDeUsuarios.usuariosQueConocenDestino(destino)
        usuario.hacerseAmigoDeLosQuePueda(usuarios)
    }
}

class AgregarDestinoMasCaroTarea(nombre: String, mailSender: MailSender) : Tarea(nombre, mailSender) {
    override fun doExecute(usuario: Usuario) {
        usuario.agregarDestinoMasCaroDeAmigos()
    }
}