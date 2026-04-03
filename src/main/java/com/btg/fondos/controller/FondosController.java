package com.btg.fondos.controller;

import com.btg.fondos.document.Inscripcion;
import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.models.FondosRequestDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.service.FondosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fondos")
@RequiredArgsConstructor
public class FondosController {

    private final FondosService fondosService;

    @PostMapping("/suscribir")
    public ResponseEntity<FondosResponseDto> suscribir(@RequestBody FondosRequestDto request) {
        return ResponseEntity.ok(fondosService.suscribirFondo(request));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<FondosResponseDto> cancelar(@RequestBody FondosRequestDto request) {
        return ResponseEntity.ok(fondosService.cancelarSuscripcion(request));
    }

    @GetMapping("/transacciones/{idCliente}")
    public ResponseEntity<List<InscripcionDto>> transacciones(@PathVariable String idCliente) {
        return ResponseEntity.ok(fondosService.consultarTransacciones(idCliente));
    }
}
