package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.style.DescribeSpec
import java.time.LocalDate
import java.time.LocalTime

internal class ValidacionSpec : DescribeSpec({
    describe("Validacion de destino") {
        it("No tira error ") {
            shouldNotThrowAny {
                Destino(
                    pais = "Argentina",
                    ciudad = "Runcuncun",
                    costoBase = 25_000.0
                )
            }
        }
    }
    describe("Validaciones en Usuario") {
        val destino = Destino(
            pais = "Argentina",
            ciudad = "Runcuncun",
            costoBase = 25_000.0
        )
        it("Usuario bien creado no falla") {
            shouldNotThrowAny {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 15,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino),
                    vehiculoPreferencia = Caprichoso
                )
            }
        }
        it("Usuario falla con fecha de alta en el futuro") {
            val bloque = {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now().plusDays(3),
                    diasDisponibles = 2,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino),
                    vehiculoPreferencia = Caprichoso
                )
            }
            shouldThrowMessage("La fecha de alta no puede ser posterior a la del día.", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Usuario falla sin destinos deseados") {
            val bloque = {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 2,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(),
                    vehiculoPreferencia = Caprichoso
                )
            }
            shouldThrowMessage("Todos los usuarios deben tener al menos un destino deseado.", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Usuario falla si no tiene dias para viajar") {
            val bloque = {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 0,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino),
                    vehiculoPreferencia = Caprichoso
                )
            }
            shouldThrowMessage("Los días para viajar, deben ser mayores a cero.", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
    }
    describe("Actividades con horarios solapados tira error") {
        val actividad1 = Actividad(
            dificultad = Dificultad.MEDIA,
            descripcion = "dasdjasdja",
            inicio = LocalTime.of(12, 0, 0),
            fin = LocalTime.of(18, 0, 0),
            costo = 3.0
        )
        val actividad2 = Actividad(
            dificultad = Dificultad.MEDIA,
            descripcion = "agsdjasdja",
            inicio = actividad1.inicio.plusHours(1),
            fin = actividad1.fin.plusHours(2),
            costo = 3.0
        )
        val bloque = {
            Itinerario(
                creador = Usuario(
                    nombre = "Carlos",
                    apellido = "Gomez",
                    username = "cgomez",
                    fechaAlta = LocalDate.now().minusYears(16),
                    paisResidencia = "Chile",
                    diasDisponibles = 1,
                    criterio = Relajado,
                    destinosDeseados = mutableListOf(
                        Destino(
                            pais = "Argentina",
                            ciudad = "MDQ",
                            costoBase = 25_000.0
                        ),
                        Destino(
                            pais = "Argentina",
                            ciudad = "Buenos Aires",
                            costoBase = 25_000.0
                        )
                    ),
                    vehiculoPreferencia = Caprichoso
                ),
                destino = Destino(
                    pais = "Chile",
                    ciudad = "Santiago",
                    costoBase = 2.0
                ),
                dias = mutableListOf(
                    DiaDeItinerario(
                        mutableListOf(
                            actividad1,
                            actividad2
                        )
                    )
                ),
            )
        }
        it("Inicio de la segunda esta en medio del horario de la primera") {
            shouldThrowMessage("Los horarios de las actividades no se pueden solapar", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Inicio y Fin de la segunda estan en medio del horario de la primera") {
            actividad2.apply {
                inicio = actividad1.inicio.plusHours(2)
                fin = actividad1.fin.minusHours(2)
            }
            shouldThrowMessage("Los horarios de las actividades no se pueden solapar", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Fin de la segunda esta en medio del horario de la primera") {
            actividad2.apply {
                inicio = actividad1.inicio.minusHours(1)
                fin = actividad1.fin.minusHours(1)
            }
            shouldThrowMessage("Los horarios de las actividades no se pueden solapar", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Inicio de la primera esta en medio del horario de la segunda") {
            actividad1.apply {
                inicio = actividad2.inicio.plusHours(1)
                fin = actividad2.fin.plusHours(1)
            }
            shouldThrowMessage("Los horarios de las actividades no se pueden solapar", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Inicio y Fin de la primera estan en medio del horario de la segunda") {
            actividad1.apply {
                inicio = actividad2.inicio.plusHours(2)
                fin = actividad2.fin.minusHours(2)
            }
            shouldThrowMessage("Los horarios de las actividades no se pueden solapar", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Fin de la primera esta en medio del horario de la segunda") {
            actividad1.apply {
                inicio = actividad2.inicio.minusHours(1)
                fin = actividad2.fin.minusHours(1)
            }
            shouldThrowMessage("Los horarios de las actividades no se pueden solapar", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
    }

    describe("puntuaciones") {
        val user = Usuario(
            nombre = "Peperoni",
            fechaAlta = LocalDate.now(),
            diasDisponibles = 15,
            criterio = Relajado,
            apellido = "Peter",
            username = "ppeter",
            paisResidencia = "Venezuela",
            destinosDeseados = mutableListOf(
                Destino(
                    pais = "Argentina",
                    ciudad = "MDQ",
                    costoBase = 25_000.0
                ),
                Destino(
                    pais = "Argentina",
                    ciudad = "Buenos Aires",
                    costoBase = 25_000.0
                )
            ),
            vehiculoPreferencia = Caprichoso
        )
        val itinerario = Itinerario(
            creador = user,
            destino = Destino(
                pais = "Argentina",
                ciudad = "Runcuncun",
                costoBase = 25_000.0
            ),
            dias = mutableListOf(
                DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            dificultad = Dificultad.MEDIA,
                            descripcion = "dasdjasdja",
                            inicio = LocalTime.of(12, 30, 0, 0),
                            fin = LocalTime.of(14, 30, 0, 0),
                            costo = 3.0
                        ),
                        Actividad(
                            dificultad = Dificultad.MEDIA,
                            descripcion = "agsdjasdja",
                            inicio = LocalTime.of(15, 0, 0, 0),
                            fin = LocalTime.of(16, 30, 0, 0),
                            costo = 3.0
                        )
                    )
                )
            ),
        )


        it("Puntuacion no esta entre 1 y 10 tira error") {
            val bloque = {
                user.puntuar(itinerario, 11)
            }
            shouldThrowMessage("Puntuacion solo puede ser un valor entre 1 y 10", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
        it("Usuario no puede puntuar mas de una vez") {
            val bloque = {
                user.puntuar(itinerario, 10)
                user.puntuar(itinerario, 10)
            }
            shouldThrowMessage("El usuario ya ha puntuado este itinerario", bloque)
            shouldThrowExactly<IllegalArgumentException>(bloque)
        }
    }
})