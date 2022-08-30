package edu.unsam.algo2

open class Repositorio<T : Entidad> {
    var nextID = Entidad.ID_INICIAL + 1
        private set
    val elementos: MutableList<T> = mutableListOf()

    /**Agrega un nuevo objeto a la colección, y le asigna un identificador único (id).
     * El identificador puede ser autoincremental para evitar que se repita.*/
    fun create(elemento: T) {
        elemento.validarEntidad()
        validarID(elemento.id)
        elemento.id = getNewID()
        elementos.add(elemento)
    }

    private fun getNewID(): Int = nextID++

    private fun validarID(id: Int) {
        if (elementos.any { elemento -> elemento.id == id }) {
            throw InvalidIdException("Error ID: $id. Ya existe un elemento con ese ID")
        }
    }

    /** Elimina el objeto de la colección.*/
    fun delete(elemento: T) {
        val elementoAEliminar = getById(elemento.id)
        elementos.remove(elementoAEliminar)
    }

    private fun existeElemento(elemento: T) {
        if (!elementos.contains(elemento)) {
            throw InvalidElementException("Elemento invalido: El elemento no ha sido encontrado")
        }
    }

    /**Modifica el objeto dentro de la colección.
     * De no existir el objeto buscado, es decir, un objeto con ese id, se debe lanzar una excepción.*/
    fun update(elemento: T) {
        elemento.validarEntidad()
        if (elemento.esNuevo()) throw InvalidElementException("Elemento invalido: El elemento no existe en el repositorio")
        val elementoEncontrado = getById(elemento.id)

        elementoEncontrado.actualizarDatos(elemento)
    }

    /**Retorna el objeto cuyo id sea el recibido como parámetro.*/
    fun getById(id: Int): T {
        val elementoRequerido = elementos.find { id == it.id }
        esNoNulo(elementoRequerido)
        return elementoRequerido!!
    }

    private fun esNoNulo(elementoRequerido: T?) {
        if (elementoRequerido == null) {
            throw InvalidElementException("Elemento invalido: No se encontro un elemento con ese ID")
        }

    }

    /**Devuelve los objetos que coincidan con la búsqueda de acuerdo a los siguientes criterios:*/
    fun search(value: String): List<T> = elementos.filter { it.coincideCon(value) }

}

class RepositorioDeUsuarios : Repositorio<Usuario>() {
    fun usuariosQueConocenDestino(destino: Destino) = elementos.filter { usr -> usr.conoce(destino) }
}

class RepositorioDeItinerarios : Repositorio<Itinerario>() {
    fun itinerariosDe(usuario: Usuario) = elementos.filter { it.fueCreadoPor(usuario) }

    /* Transferir todos sus itinerarios al amigo que menos destinos visitados tenga */
    fun transferirItinerarios(creadorOriginal: Usuario, nuevoCreador: Usuario) {
        itinerariosDe(creadorOriginal).forEach {
            it.actualizarCreador(nuevoCreador)
        }
    }
}