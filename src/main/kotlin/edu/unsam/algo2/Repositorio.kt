package edu.unsam.algo2

// val repo = Repositorio<Usuario>()

class Repositorio<T : Entidad> {
    var nextID = 0
        private set
    val elementos: MutableList<T> = mutableListOf()

    /**Agrega un nuevo objeto a la colección, y le asigna un identificador único (id).
     * El identificador puede ser autoincremental para evitar que se repita.*/
    fun create(elemento: T) {
        validarID(elemento.id)
        elemento.id = nextID++
        elementos.add(elemento)
    }
    fun validarID(id: Int?) {
        if (elementos.any { elemento -> elemento.id == id }) {
            throw InvalidIdException(id, "Ya existe un elemento con ese ID")
        }
    }

    /** Elimina el objeto de la colección.*/
    fun delete(elemento: T) {
        existeElemento(elemento)
        elementos.remove(elemento)
    }
    fun existeElemento(elemento: T) {
        if (!elementos.contains(elemento)) {
            throw InvalidElementException("El elemento no ha sido encontrado")
        }
    }

    /**Modifica el objeto dentro de la colección.
     * De no existir el objeto buscado, es decir, un objeto con ese id, se debe lanzar una excepción.*/
    fun update(elemento: T) {
        val elementoEncontrado = elemento.id?.let { getById(it) }
            ?: throw InvalidElementException("El elemento no ha sido encontrado")

        elemento.actualizarDatos(elementoEncontrado)
    }


    /**Retorna el objeto cuyo id sea el recibido como parámetro.*/
    fun getById(id: Int): T? {
        val elementoRequerido = elementos.find { id == it.id }
        existeID(elementoRequerido)
        return elementoRequerido
    }
    fun existeID(elementoRequerido: T?) {
        if (elementoRequerido == null) {
            throw InvalidElementException("No se encontro un elemento con ese ID")
        }

    }

    /**Devuelve los objetos que coincidan con la búsqueda de acuerdo a los siguientes criterios:*/
    fun search(value: String): List<T> {
        val busqueda = elementos.filter { it.coincideCon(value) }
        existeElementoConCriterio(busqueda)
        return busqueda
    }

    fun existeElementoConCriterio(busqueda: List<T>) {
        if (busqueda.isNullOrEmpty()) {
            throw InvalidElementException("No existen elementos con ese criterio")
        }
    }

    fun existe(elemento: T) = elementos.any { elemento.id == it.id }

    fun existenTodos(lista: MutableList<T>) = lista.all { elemento -> existe(elemento) }

}