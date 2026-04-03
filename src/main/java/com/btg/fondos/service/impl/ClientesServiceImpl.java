package com.btg.fondos.service.impl;

import com.btg.fondos.document.ClienteDocument;
import com.btg.fondos.dto.ClienteDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.repository.ClienteRepository;
import com.btg.fondos.service.ClientesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientesServiceImpl implements ClientesService {

    private final ClienteRepository clienteRepository;
    private String mensaje = "Cliente {0} {1} inscrito correctamente";

    /**
     * Metodo para registrar un cliente a la plataforma BTG
     * @param request datos entrada del cliente
     * @return respuesta del proceso
     */
    @Override
    public FondosResponseDto inscribirCliente(ClienteDto request) {
        ClienteDocument clienteDoc = ClienteDocument.builder()
                .saldo(500_000L)
                .nombre(request.getNombre())
                .email(request.getEmail())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .ciudad(request.getCiudad())
                .tipoNotificacion(request.getTipoNotificacion()).build();
        clienteRepository.save(clienteDoc);
        return FondosResponseDto.builder()
                .mensaje(MessageFormat.format(mensaje,request.getNombre(),request.getApellido()))
                .build();
    }
}
