package com.santidev.aprendiendotesting.ejemploBasicoDeNotas

//esto es lo que se va a mockear
interface NotaRepository {
  suspend fun obtenerNotas(): List<Nota>
  suspend fun agregarNota(nota: Nota)
  suspend fun eliminarNota(id: Int)
}