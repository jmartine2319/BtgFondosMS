package com.btg.fondos.service.impl;

import com.btg.fondos.document.Cliente;
import com.btg.fondos.document.Inscripcion;
import com.btg.fondos.document.Producto;
import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.mapper.InscripcionMapper;
import com.btg.fondos.models.FondosRequestDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.repository.ClienteRepository;
import com.btg.fondos.repository.InscripcionRepository;
import com.btg.fondos.repository.ProductoRepository;
import com.btg.fondos.service.FondosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FondosServiceImpl implements FondosService {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final InscripcionMapper inscripcionMapper;

    /**
     * Metodo para suscribir un fondo a un cliente
     * @param request datos de entrada para suscripcion
     * @return resultado de la suscripcion
     */
    @Override
    public FondosResponseDto suscribirFondo(FondosRequestDto request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (cliente.getSaldo().compareTo(producto.getMonto()) < 0) {
            return FondosResponseDto.builder()
                    .mensaje("No tiene saldo disponible para vincularse al fondo " + producto.getNombre())
                    .saldo(cliente.getSaldo())
                    .build();
        }

        if (inscripcionRepository.existsByIdClienteAndIdProductoAndEstado(
                request.getIdCliente(), request.getIdProducto(), "ACTIVO")) {
            return FondosResponseDto.builder()
                    .mensaje("Ya tiene una suscripción activa al fondo " + producto.getNombre())
                    .saldo(cliente.getSaldo())
                    .build();
        }

        cliente.setSaldo(cliente.getSaldo()-producto.getMonto());
        clienteRepository.save(cliente);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdCliente(request.getIdCliente());
        inscripcion.setIdProducto(request.getIdProducto());
        inscripcion.setEstado("ACTIVO");
        inscripcion.setFechaApertura(LocalDate.now());
        inscripcion.setMonto(producto.getMonto());
        inscripcionRepository.save(inscripcion);

        enviarNotificacion(cliente, producto);

        return FondosResponseDto.builder()
                .mensaje("Suscripción al fondo " + producto.getNombre() + " realizada exitosamente")
                .saldo(cliente.getSaldo())
                .transaccion(inscripcion)
                .build();
    }

    /**
     * Metodo para cancelar una suscripcion de un cliente
     * @param request cliente a cancelar suscripcion
     * @return respuesta del proceso
     */
    @Override
    public FondosResponseDto cancelarSuscripcion(FondosRequestDto request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Inscripcion inscripcion = inscripcionRepository
                .findByIdClienteAndIdProductoAndEstado(
                        request.getIdCliente(), request.getIdProducto(), "ACTIVO")
                .orElseThrow(() -> new RuntimeException(
                        "No tiene suscripción activa al fondo " + producto.getNombre()));

        cliente.setSaldo(cliente.getSaldo()+inscripcion.getMonto());
        clienteRepository.save(cliente);

        inscripcion.setEstado("CANCELADO");
        inscripcion.setFechaCancelacion(LocalDate.now());
        inscripcionRepository.save(inscripcion);

        return FondosResponseDto.builder()
                .mensaje("Suscripción al fondo " + producto.getNombre() + " cancelada. Monto retornado: COP $" + inscripcion.getMonto())
                .saldo(cliente.getSaldo())
                .transaccion(inscripcion)
                .build();
    }

    /**
     * Metodo para realizar consulta transacciones de un cliente
     * @param idCliente id del cliente a consultar
     * @return lista de transacciones consultadas
     */
    @Override
    public List<InscripcionDto> consultarTransacciones(String idCliente) {
        List<Inscripcion> listaIns= inscripcionRepository.findByIdCliente(idCliente);
        List<InscripcionDto> listaDto = new ArrayList<>();
        listaDto=listaIns.stream().map(inscripcionMapper::toDto).collect(Collectors.toList());
        return listaDto;
    }

    /**
     * Metodo para enviar notificacion
     * @param cliente cliente a enviar notificacion
     * @param producto producto del cliente a notificar
     */
    private void enviarNotificacion(Cliente cliente, Producto producto) {
        String tipo = cliente.getTipoNotificacion();
        if ("EMAIL".equalsIgnoreCase(tipo)) {
            log.info("[EMAIL] Para: {} | Suscripción exitosa al fondo: {}", cliente.getEmail(), producto.getNombre());
        } else if ("SMS".equalsIgnoreCase(tipo)) {
            log.info("[SMS] Para: {} | Suscripción exitosa al fondo: {}", cliente.getTelefono(), producto.getNombre());
        }
    }
}
