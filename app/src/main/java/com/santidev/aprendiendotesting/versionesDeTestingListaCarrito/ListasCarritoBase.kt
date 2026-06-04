package com.santidev.pruebatesting.funciones.versionesDeTestingListaCarrito

class ListasCarritoBase {
  private val productos = mutableListOf<String>()
  
  fun agregarProducto(producto: String): List<String> {
    productos.add(producto)
    return productos.toList()
  }
  
  fun eliminarProducto(producto: String): List<String> {
    productos.remove(producto)
    return productos.toList()
  }
  
  fun calcularTotal(precios: List<Double>): Double {
    return precios.sum()
  }
  
  fun estaVacio(): Boolean {
    return productos.isEmpty()
  }
  
  fun cantidadDeProductos(): Int {
    return productos.size
  }
}