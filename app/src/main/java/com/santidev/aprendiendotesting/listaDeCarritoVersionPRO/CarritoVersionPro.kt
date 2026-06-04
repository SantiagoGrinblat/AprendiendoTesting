package com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO

class CarritoVersionPro(
  private val stockService: StockService,
  private val pagoService: PagoService,
  private val descuentoService: DescuentoService
) {
  
  private val productosItems = mutableListOf<Producto>()
  
  fun agregarProducto(producto: Producto) {
    if (producto.precio <= 0) throw IllegalArgumentException("Precio invalido")
    if (producto.cantidad <= 0) throw IllegalArgumentException("Cantidad invalida")
    val existente = productosItems.find { it.id == producto.id }
    if (existente != null) {
      val index = productosItems.indexOf(existente)
      productosItems[index] = existente.copy(cantidad = existente.cantidad + 1)
    } else {
      productosItems.add(producto)
    }
  }
  
  fun calcularTotal(): Double {
    return productosItems.sumOf { it.precio * it.cantidad }
  }
  
  fun calcularTotalConDescuento(cupon: String): Double {
    val descuento = descuentoService.validarCupon(cupon)
    if (descuento < 0 || descuento >= 100) {
      throw IllegalArgumentException("Descuento invalido")
    }
    val total = calcularTotal()
    return total - (total * descuento / 100)
  }
  
  fun realizarCompra(): Boolean {
    if (productosItems.isEmpty()) throw IllegalArgumentException("El carrito esta vacio")
    val total = calcularTotal()
    
    productosItems.forEach { producto ->
      if (!stockService.verificarStock(producto.id, producto.cantidad)) {
        throw IllegalArgumentException("Stock insuficiente para el producto ${producto.nombre}")
      }
    }
    
    val pagoProcesado = pagoService.procesarPago(total)
    
    if (pagoProcesado) {
      productosItems.forEach { productoItem ->
        stockService.reducirStock(productoItem.id, productoItem.cantidad)
      }
    }
    
    return pagoProcesado
  }
  
}