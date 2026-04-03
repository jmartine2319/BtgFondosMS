package com.btg.fondos.controller;

import com.btg.fondos.dto.ClienteDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.service.ClientesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para administrar clientes
 */
@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClientesController {
    private final ClientesService clientesService;

    /**
     * Controller para agregar un nuevo cliente
     * @param request datos cliente nuevo
     * @return respuesta del proceso
     */
    @PostMapping("registrar")
    public FondosResponseDto inscribirCliente(@RequestBody ClienteDto request){
        return clientesService.inscribirCliente(request);
    }

}
