package com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO

interface StockService {
  fun verificarStock(
    productoId: Int,
    cantidad: Int
  ) : Boolean
  
  fun reducirStock(
    productoId: Int,
    cantidad: Int
  )
}

interface PagoService {
  fun procesarPago(monto: Double) : Boolean
}

interface DescuentoService {
  fun validarCupon(cupon: String) : Double
}