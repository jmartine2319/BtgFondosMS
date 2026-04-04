package com.btg.fondos.service.impl;

import com.btg.fondos.document.ClienteDocument;
import com.btg.fondos.document.InscripcionDocument;
import com.btg.fondos.document.ProductoDocument;
import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.dto.ProductoDto;
import com.btg.fondos.mapper.InscripcionMapper;
import com.btg.fondos.mapper.ProductoMapper;
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
    private final ProductoMapper productoMapper;

    /**
     * Metodo para suscribir un fondo a un cliente
     * @param request datos de entrada para suscripcion
     * @return resultado de la suscripcion
     */
    @Override
    public FondosResponseDto suscribirFondo(FondosRequestDto request) {
        ClienteDocument clienteDocument = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ProductoDocument productoDocument = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (clienteDocument.getSaldo().compareTo(productoDocument.getMonto()) < 0) {
            return FondosResponseDto.builder()
                    .mensaje("No tiene saldo disponible para vincularse al fondo " + productoDocument.getNombre())
                    .saldo(clienteDocument.getSaldo())
                    .build();
        }

        if (inscripcionRepository.existsByIdClienteAndIdProductoAndEstado(
                request.getIdCliente(), request.getIdProducto(), "ACTIVO")) {
            return FondosResponseDto.builder()
                    .mensaje("Ya tiene una suscripción activa al fondo " + productoDocument.getNombre())
                    .saldo(clienteDocument.getSaldo())
                    .build();
        }

        clienteDocument.setSaldo(clienteDocument.getSaldo()- productoDocument.getMonto());
        clienteRepository.save(clienteDocument);

        InscripcionDocument inscripcion = new InscripcionDocument();
        inscripcion.setIdCliente(request.getIdCliente());
        inscripcion.setIdProducto(request.getIdProducto());
        inscripcion.setEstado("ACTIVO");
        inscripcion.setFechaApertura(LocalDate.now());
        inscripcion.setMonto(productoDocument.getMonto());
        inscripcionRepository.save(inscripcion);

        enviarNotificacion(clienteDocument, productoDocument);

        return FondosResponseDto.builder()
                .mensaje("Suscripción al fondo " + productoDocument.getNombre() + " realizada exitosamente")
                .saldo(clienteDocument.getSaldo())
                .transaccion(inscripcionMapper.toDto(inscripcion))
                .build();
    }

    /**
     * Metodo para cancelar una suscripcion de un cliente
     * @param request cliente a cancelar suscripcion
     * @return respuesta del proceso
     */
    @Override
    public FondosResponseDto cancelarSuscripcion(FondosRequestDto request) {
        ClienteDocument clienteDocument = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ProductoDocument productoDocument = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        InscripcionDocument inscripcion = inscripcionRepository
                .findByIdClienteAndIdProductoAndEstado(
                        request.getIdCliente(), request.getIdProducto(), "ACTIVO")
                .orElseThrow(() -> new RuntimeException(
                        "No tiene suscripción activa al fondo " + productoDocument.getNombre()));

        clienteDocument.setSaldo(clienteDocument.getSaldo()+inscripcion.getMonto());
        clienteRepository.save(clienteDocument);

        inscripcion.setEstado("CANCELADO");
        inscripcion.setFechaCancelacion(LocalDate.now());
        inscripcionRepository.save(inscripcion);

        return FondosResponseDto.builder()
                .mensaje("Suscripción al fondo " + productoDocument.getNombre() + " cancelada. Monto retornado: COP $" + inscripcion.getMonto())
                .saldo(clienteDocument.getSaldo())
                .transaccion(inscripcionMapper.toDto(inscripcion))
                .build();
    }

    /**
     * Metodo para realizar consulta transacciones de un cliente
     * @param idCliente id del cliente a consultar
     * @return lista de transacciones consultadas
     */
    @Override
    public List<InscripcionDto> consultarTransacciones(String idCliente) {
        List<InscripcionDocument> listaIns= inscripcionRepository.findByIdCliente(idCliente);
        List<InscripcionDto> listaDto = new ArrayList<>();
        listaDto=listaIns.stream().map(inscripcionMapper::toDto).collect(Collectors.toList());
        return listaDto;
    }

    /**
     * Metodo para enviar notificacion
     * @param clienteDocument cliente a enviar notificacion
     * @param productoDocument producto del cliente a notificar
     */
    private void enviarNotificacion(ClienteDocument clienteDocument, ProductoDocument productoDocument) {
        String tipo = clienteDocument.getTipoNotificacion();
        if ("EMAIL".equalsIgnoreCase(tipo)) {
            log.info("[EMAIL] Para: {} | Suscripción exitosa al fondo: {}", clienteDocument.getEmail(), productoDocument.getNombre());
        } else if ("SMS".equalsIgnoreCase(tipo)) {
            log.info("[SMS] Para: {} | Suscripción exitosa al fondo: {}", clienteDocument.getTelefono(), productoDocument.getNombre());
        }
    }

    /**
     * Metodo para consultar todos los productos
     * @return lista de productos registrados
     */
    public List<ProductoDto> consultarProductos(){
        List<ProductoDocument> productos = productoRepository.findAll();
        return productos.stream().map(productoMapper::toDto).collect(Collectors.toList());
    }
}
