package edu.unsam.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    var nombre: String,
    var apellido: String,
    var username: String,
    var email: String,
    var paisResidencia: String,
    val fechaAlta: LocalDate,
    var diasDisponibles: Int,
    var criterio: Criterio,
    var destinosDeseados: MutableList<Destino>,
    var vehiculoPreferencia: PreferenciaDeVehiculo
) : Entidad {

    val amigos: MutableList<Usuario> = mutableListOf()
    var destinosVisitados: MutableList<Destino> = mutableListOf()
    val itinerariosAPuntuar: MutableList<Itinerario> = mutableListOf()
    val viajeObservers: MutableList<ViajeObserver> = mutableListOf()
    var tareas: MutableList<Tarea> = mutableListOf()

    fun realizarTareas() {
        tareas.forEach { it.execute(this) }
    }

    override var id: Int = Entidad.ID_INICIAL

    fun leGusta(vehiculo: Vehiculo) = vehiculoPreferencia.leGusta(vehiculo)

    init {
        validarEntidad()
    }

    override fun validarEntidad() {
        require(nombre.isNotBlank()) {
            "El nombre del usuario no puede ser nulo o vacio"
        }

        require(apellido.isNotBlank()) {
            "El apellido del usuario no puede ser nulo o vacio"
        }

        require(username.isNotBlank()) {
            "El username del usuario no puede ser nulo o vacio"
        }

        require(paisResidencia.isNotBlank()) {
            "El pais del usuario no puede ser nulo o vacio"
        }

        require(fechaAlta <= LocalDate.now()) {
            "La fecha de alta no puede ser posterior a la del día."
        }

        require(diasDisponibles > 0) {
            "Los días para viajar, deben ser mayores a cero."
        }
        require(destinosDeseados.size > 0) {
            "Todos los usuarios deben tener al menos un destino deseado."
        }
    }

    /* El usuario confirma realizar el viaje. Esto debe actualizar los destinos visitados por él. */
    fun realizar(viaje: Viaje) {
        agregarDestinoVisitado(viaje.destino())
        viajeObservers.forEach { it.viajeRealizado(viaje, this) }
    }

    fun activarObserver(observer: ViajeObserver) {
        viajeObservers.add(observer)
    }

    /* Darle un puntaje a los itinerarios a puntuar.
    Para eso de debe especificar el puntaje que queremos darle a todos los itinerarios */
    fun puntuarTodos(puntuacion: Int) {
        itinerariosAPuntuar.forEach { puntuar(it, puntuacion) }
    }

    fun agregarItinerarioAPuntuar(itinerario: Itinerario) {
        itinerariosAPuntuar.add(itinerario)
    }

    fun agregarDestinoVisitado(destino: Destino) {
        destinosVisitados.add(destino)
    }

    fun puntuar(itinerario: Itinerario, puntuacion: Int) {
        require(puntuacion in 1..10) {
            "Puntuacion solo puede ser un valor entre 1 y 10"
        }
        require(!itinerario.fuePuntuadoPor(this)) {
            "El usuario ya ha puntuado este itinerario"
        }
        if (!puedePuntuar(itinerario))
            throw InvalidAction("Acción inválida: ${this.username} no puede puntuar el itinerario de ${itinerario.creador}")
        itinerario.recibirPuntajeDe(this, puntuacion)
    }

    fun antiguedad() = ChronoUnit.YEARS.between(fechaAlta, LocalDate.now()).toInt()

    fun conoce(destino: Destino) = esDestinoDeseado(destino) || esDestinoVisitado(destino)

    fun esDestinoDeseado(destino: Destino): Boolean = destinosDeseados.contains(destino)

    fun esDestinoVisitado(destino: Destino): Boolean = destinosVisitados.contains(destino)

    fun puedePuntuar(itinerario: Itinerario): Boolean = !itinerario.fueCreadoPor(this) && conoce(itinerario.destino)

    fun puedeRealizar(itinerario: Itinerario): Boolean =
        diasDisponibles >= itinerario.cantidadDias() && criterio.puedeRealizar(itinerario)

    fun esDestinoCaro(destinoItinerario: Destino): Boolean =
        destinosDeseados.maxOf { destino -> destino.costo(this) } < destinoItinerario.costo(this)

    fun esAmigoDe(usuario: Usuario): Boolean = amigos.contains(usuario)

    fun agregarAmigo(usuario: Usuario) {
        amigos.add(usuario)
    }

    fun agregarDestinosDeseados(destinos: List<Destino>) {
        destinosDeseados.addAll(destinos)
    }

    fun destinoMasCaro() = destinosDeseados.maxByOrNull { it.costo(this) }
        ?: throw SinDestinosDeseadosException("No se encontró el destino más caro")

    fun algunAmigoConoce(destino: Destino): Boolean = amigos.any { it.conoce(destino) }

    /** El valor de búsqueda debe coincidir parcialmente con su nombre o apellido,
     * o exáctamente con su username.
     */
    override fun coincideCon(value: String): Boolean {
        return nombre.contains(value, ignoreCase = true) ||
                apellido.contains(value, ignoreCase = true) ||
                username.equals(value, ignoreCase = true)
    }

    override fun <T> actualizarDatos(elemento: T) {
        val usuario = elemento as Usuario
        nombre = usuario.nombre
        apellido = usuario.apellido
        username = usuario.username
        paisResidencia = usuario.paisResidencia
//            fechaAlta = usuario.fechaAlta // No se puede actualizar porque es constante
        diasDisponibles = usuario.diasDisponibles
        criterio = usuario.criterio
        destinosDeseados = usuario.destinosDeseados
        vehiculoPreferencia = usuario.vehiculoPreferencia
    }

    fun amigosConDestinoDeseado(destino: Destino): List<Usuario> = amigos.filter { it.esDestinoDeseado(destino) }

    fun totalDestinosVisitados() = destinosVisitados.size

    fun amigoConMenorDestinosVisitados(): Usuario =
        amigos.minByOrNull { it.totalDestinosVisitados() }
            ?: throw SinDestinosVisitadosException("No se encontró amigos con la menor cantidad de destinos visitados")

    fun modificarPreferencia(nuevaPreferenciaDeVehiculo: PreferenciaDeVehiculo) {
        vehiculoPreferencia = nuevaPreferenciaDeVehiculo
    }

    fun modificarCriterio(nuevoCriterio: Criterio) {
        criterio = nuevoCriterio
    }

    fun hacerseAmigoDeLosQuePueda(usuarios: List<Usuario>) {
        usuariosNoAmigos(usuarios).forEach { usr -> this.agregarAmigo(usr) }
    }

    private fun usuariosNoAmigos(usuarios: List<Usuario>) =
        usuarios.filter { usr -> puedeSerAmigo(usr) }

    private fun puedeSerAmigo(usuario: Usuario) = !esAmigoDe(usuario) && usuario != this

    fun agregarTarea(tarea: Tarea) {
        tareas.add(tarea)
    }

    fun agregarMultiplesTareas(tareas: List<Tarea>) {
        tareas.forEach { agregarTarea(it) }
    }

    fun agregarDestinoMasCaroDeAmigos() {
        agregarDestinosDeseados(destinosMasCaroDeAmigos())
    }

    private fun destinosMasCaroDeAmigos(): List<Destino> = amigos.map { amigo -> amigo.destinoMasCaro() }

    fun agregarVariosItinerariosAPuntuar(itinerarios: List<Itinerario>) {
        itinerariosAPuntuar.addAll(itinerarios)
    }

}
