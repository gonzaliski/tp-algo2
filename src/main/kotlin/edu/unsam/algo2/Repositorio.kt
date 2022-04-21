package edu.unsam.algo2

// val repo = Repositorio<Usuario>()

class Repositorio<T : Identidad> {
    var nextID = 0
        private set
    val elementos: MutableList<T> = mutableListOf()

    /**Agrega un nuevo objeto a la colección, y le asigna un identificador único (id).
     * El identificador puede ser autoincremental para evitar que se repita.*/
    fun create(elemento: T) {
        elemento.id = nextID++
        elementos.add(elemento)
    }

    /** Elimina el objeto de la colección.*/
    fun delete(elemento: T){
        elementos.remove(elemento)
    }

    /**Modifica el objeto dentro de la colección.
     * De no existir el objeto buscado, es decir, un objeto con ese id, se debe lanzar una excepción.*/
    fun update(elemento: T){
        val elementoEncontrado = elemento.id?.let { getById(it) }
            ?: throw java.lang.NullPointerException("El elemento buscado no ha sido encontrado")

        //TODO: preguntar que es lo que hay que modificar en caso de que el objeto se encuentre en la coleccion.
    }


    /**Retorna el objeto cuyo id sea el recibido como parámetro.*/
    fun getById(id: Int):T?{
        return elementos.find{id == it.id}
    }

    /**Devuelve los objetos que coincidan con la búsqueda de acuerdo a los siguientes criterios:*/
    fun search(value: String):List<T>{
        return elementos.filter{it.coincideCon(value)}
    }



}