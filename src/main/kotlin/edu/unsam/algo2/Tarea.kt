package edu.unsam.algo2

abstract class Tarea (val usuario: Usuario, var nombre: String, val mailSender: MailSender) {

    fun execute(){
        enviarMail(nombre, usuario.username, "Se realizo la tarea: $nombre", "Se realizo la tarea: $nombre" )
        doExecute()
    }

    fun enviarMail(from:String, to:String, subject:String,message:String){
        mailSender.sendMail( Mail(from, to, subject, message) )
    }

    abstract fun doExecute()
}

class PuntuarItinerarioTarea(usuario: Usuario, nombre: String, mailSender: MailSender, val puntuacion: Int): Tarea(usuario, nombre, mailSender){
    override fun doExecute() {
        usuario.puntuarTodos(puntuacion)
    }
}

class TransferirItinerariosTarea(usuario: Usuario, nombre: String, mailSender: MailSender): Tarea(usuario, nombre, mailSender){
    override fun doExecute() {
        usuario.transferirItinerarios(usuario.amigoConMenorDestinosVisitados())
    }
}

class HacerseAmigoTarea(usuario: Usuario, nombre: String, mailSender: MailSender, val usuarios:List<Usuario>, val destino: Destino): Tarea(usuario, nombre, mailSender){
    override fun doExecute() {
        usuariosQueConocenDestino().forEach { usr -> usuario.agregarAmigo(usr) }
    }
    fun usuariosQueConocenDestino() = usuarios.filter { usr -> usr.conoce(destino) }
}

class agregarDestinoMasCaroTarea(usuario: Usuario, nombre: String, mailSender: MailSender): Tarea(usuario, nombre, mailSender) {
    override fun doExecute() {
        usuario.agregarDestinosDeseados(destinoMasCaroDeAmigos())
    }


    fun destinoMasCaroDeAmigos(): List<Destino> = usuario.amigos.map { amigo -> amigo.destinoMasCaro() }
}