package com.santidev.aprendiendotesting.ejemploBasicoDeNotas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//aca va a ir lo que se va a testear
class NotaViewModel(
  private val repository: NotaRepository
): ViewModel() {
  
  //el mutable = solo el ViewModel lo modifica.
  //<List<Nota>> = es el tipo de dato que guarda, es una lista con data class.
  //emptyList = es el valor inicial como arranca, en este caso, VACIO.
  private val _notas = MutableStateFlow<List<Nota>>(emptyList())
  
  //la variable es de solo lectura, esta variable observa solo la UI
  val notas: StateFlow<List<Nota>> = _notas.asStateFlow()
  
  //esta funcion carga las notas actuales que tiene el usuario
  fun cargarNotas() {
    viewModelScope.launch {
      _notas.value = repository.obtenerNotas()
    }
  }
  
  fun agregarNota(titulo: String, contenido: String) {
    
    //viewModelScope = este es el espacio de "vida" que tiene el ViewModel
    //.launch = es el lanzador de la corrutina
    viewModelScope.launch {
      
      //esta variable "absorve" los datos del id, titulo y contenido
      val nota = Nota(
        id = (_notas.value.size + 1),
        titulo = titulo,
        contenido = contenido
      )
      //agrega la nota con la funcion agregarNota de la funcion del (NotaRepository)
      repository.agregarNota(nota)
      
      //actualiza el estado cuando termina de cargar
      _notas.value = repository.obtenerNotas()
    }
  }
  
  //esta funcion elimina una nota segun el ID.
  //y actualiza las notas con (obtenerNotas) despues de ser eliminados... osea las carga de nuevo
  fun eliminarNota(id: Int) {
    viewModelScope.launch {
      repository.eliminarNota(id)
      _notas.value = repository.obtenerNotas()
    }
  }
  
}