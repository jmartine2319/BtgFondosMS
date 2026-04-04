package com.btg.fondos.controller;

import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.dto.ProductoDto;
import com.btg.fondos.models.FondosRequestDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.service.FondosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para las operaciones realizadas de los fondos
 */
@RestController
@RequestMapping("/fondos")
@RequiredArgsConstructor
public class FondosController {

    private final FondosService fondosService;

    /**
     * Servicio ara suscribirse un cliente a un producto
     * @param request cuerpo de la peticion para susbribirse a un producto
     * @return respuesta de la peticion
     */
    @PostMapping("/suscribir")
    public ResponseEntity<FondosResponseDto> suscribir(@RequestBody FondosRequestDto request) {
        return ResponseEntity.ok(fondosService.suscribirFondo(request));
    }

    /**
     * Servicio para cancelar una suscripcion de un producto
     * @param request cuerpo de peticion a cancelar
     * @return respuesta de la cancelacion
     */
    @PostMapping("/cancelar")
    public ResponseEntity<FondosResponseDto> cancelar(@RequestBody FondosRequestDto request) {
        return ResponseEntity.ok(fondosService.cancelarSuscripcion(request));
    }

    /**
     * Servicio para consultar las transacciones registradas
     * @param idCliente id del cliente a consultar
     * @return transacciones del cliente generadas
     */
    @GetMapping("/transacciones/{idCliente}")
    public ResponseEntity<List<InscripcionDto>> transacciones(@PathVariable String idCliente) {
        return ResponseEntity.ok(fondosService.consultarTransacciones(idCliente));
    }

    /**
     * Servicio para cancelar una suscripcion de un producto
     * @return lista de productos
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDto>> productos() {
        return ResponseEntity.ok(fondosService.consultarProductos());
    }
}
