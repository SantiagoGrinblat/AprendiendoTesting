package com.santidev.pruebatesting.funciones.versionesDeTestingListaCarrito

class ListaCarritoMejorada {
  
  private val productos = mutableListOf<ProductoVersionMejorada>()
  //se utiliza una lista que puede cambiar con el tiempo.
  //por que mutableListOf y no listOf?
  //listOf = es una lista fija, no se puede modificar una vez creada
  //mutableListOf = es una lista flexible, podes agregar y eliminar cuando quieras
  
  fun agregarProducto(nombre: String, precio: Double): List<ProductoVersionMejorada> {
    //este if verifica si el nombre O el precio no reciben los datos correctos
    //mientas que uno de los datos sea invalido, tira el error
    if (nombre.isBlank() || precio <= 0) {
      throw IllegalArgumentException("Mensaje de error: El nombre O el precio son incorrectos")
      //throw para la ejecucion en ese momento.
      //si el nombre es invalido, explota y no sigue.
    }
      productos.add(ProductoVersionMejorada(nombre, precio))
      return productos.toList()
  }
  //se crea una clase ProductoVersionMejorada con el nombre y el precio del producto.
  //lo agregamos a la lista interna de productos
  //y hacemos que nos retorne la lista de productos, con los elementos ya agregados.
  
  fun eliminarProducto(nombre: String): List<ProductoVersionMejorada> {
    if (nombre.isBlank()) { //<- este if verifica que el nombre no este vacio
      throw IllegalArgumentException("El nombre del producto esta vacio")
    }
    if (productos.none {it.nombre == nombre}) { //<- este if analiza que haya un producto con ese nombre
      throw IllegalArgumentException("No hay un producto con ese nombre")
    }
    productos.removeIf { it.nombre == nombre }
    return productos.toList()
  }
  //eliminamos el producto basandonos en el nombre
  //y retornamos la "nueva" lista de productos
  //se usan dos 'ifs' por separado, porque son dos validaciones distintas
  
  fun calcularTotal(): Double {
    return productos.sumOf { it.precio }
  }
  //calcularTotal() ya no recibe parametros porque los precios estan dentro de cada Producto.
  //eso es una mejora real sobre a la version base donde habia que pasar una lista de precios por separado.
  //donde teniamos que crear una nueva lista de precios imaginaria.
  
  fun estaVacio(): Boolean {
    return productos.isEmpty()
  }
  
  fun cantidadDeProductos(): Int {
    return productos.size
  }
  
}