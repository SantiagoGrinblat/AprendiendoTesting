package com.santidev.pruebatesting.funciones.usandoMock.EjemploBasicoDeMockk

class Servicio {
  fun obtenerMensaje(): String {
    return "Hola desde la clase"
  }
  fun buscarUsuario(id: Int): String {
    return "Usuario simulado numero $id"
  }
}

class MiClase(private val servicio: Servicio) {
  fun procesarSaludo(): String {
    return servicio.obtenerMensaje()
  }
  fun obtenerUsuario(id: Int): String {
    return servicio.buscarUsuario(id)
  }
}
