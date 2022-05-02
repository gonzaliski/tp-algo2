package edu.unsam.algo2

class Repositorio<T : Entidad> {
    var nextID = Entidad.ID_INICIAL + 1
        private set
    val elementos: MutableList<T> = mutableListOf()

    /**Agrega un nuevo objeto a la colección, y le asigna un identificador único (id).
     * El identificador puede ser autoincremental para evitar que se repita.*/
    fun create(elemento: T) {
        validarID(elemento.id)
        elemento.id = getNewID()
        elementos.add(elemento)
    }

    private fun getNewID(): Int = nextID++

    private fun validarID(id: Int) {
        if (elementos.any { elemento -> elemento.id == id }) {
            throw InvalidIdException(id, "Ya existe un elemento con ese ID")
        }
    }

    /** Elimina el objeto de la colección.*/
    fun delete(elemento: T) {
        existeElemento(elemento)
        elementos.remove(elemento)
    }

    private fun existeElemento(elemento: T) {
        if (!elementos.contains(elemento)) {
            throw InvalidElementException("El elemento no ha sido encontrado")
        }
    }

    /**Modifica el objeto dentro de la colección.
     * De no existir el objeto buscado, es decir, un objeto con ese id, se debe lanzar una excepción.*/
    fun update(elemento: T) {
        if (elemento.esNuevo()) throw InvalidElementException("El elemento no existe en el repositorio")
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
            throw InvalidElementException("No se encontro un elemento con ese ID")
        }

    }

    /**Devuelve los objetos que coincidan con la búsqueda de acuerdo a los siguientes criterios:*/
    fun search(value: String): List<T> = elementos.filter { it.coincideCon(value) }

}