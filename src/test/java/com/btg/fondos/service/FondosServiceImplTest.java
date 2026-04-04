package com.btg.fondos.service;

import com.btg.fondos.document.ClienteDocument;
import com.btg.fondos.document.InscripcionDocument;
import com.btg.fondos.document.ProductoDocument;
import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.mapper.InscripcionMapper;
import com.btg.fondos.models.FondosRequestDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.repository.ClienteRepository;
import com.btg.fondos.repository.InscripcionRepository;
import com.btg.fondos.repository.ProductoRepository;
import com.btg.fondos.service.impl.FondosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FondosServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private InscripcionRepository inscripcionRepository;
    @Mock
    private InscripcionMapper inscripcionMapper;

    @InjectMocks
    private FondosServiceImpl fondosService;

    private ClienteDocument cliente;
    private ProductoDocument producto;
    private FondosRequestDto request;

    @BeforeEach
    void setUp() {
        cliente = ClienteDocument.builder()
                .id("cli1")
                .nombre("Juan")
                .apellido("Perez")
                .saldo(1_000_000L)
                .tipoNotificacion("EMAIL")
                .email("juan@test.com")
                .telefono("3001234567")
                .build();

        producto = ProductoDocument.builder()
                .id("prod1")
                .nombre("FPV BTG Pactual Recaudadora")
                .monto(75_000L)
                .build();

        request = new FondosRequestDto();
        request.setIdCliente("cli1");
        request.setIdProducto("prod1");
    }

    // ── suscribirFondo ──────────────────────────────────────────────────────

    @Test
    void suscribirFondo_exitoso() {
        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.existsByIdClienteAndIdProductoAndEstado("cli1", "prod1", "ACTIVO")).thenReturn(false);

        InscripcionDocument inscripcionGuardada = new InscripcionDocument();
        inscripcionGuardada.setIdCliente("cli1");
        inscripcionGuardada.setIdProducto("prod1");
        inscripcionGuardada.setEstado("ACTIVO");
        inscripcionGuardada.setMonto(75_000L);
        inscripcionGuardada.setFechaApertura(LocalDate.now());

        InscripcionDto inscripcionDto = InscripcionDto.builder()
                .idCliente("cli1").idProducto("prod1").estado("ACTIVO").monto(75_000L).build();

        when(inscripcionMapper.toDto(any(InscripcionDocument.class))).thenReturn(inscripcionDto);

        FondosResponseDto response = fondosService.suscribirFondo(request);

        assertThat(response.getMensaje()).contains("exitosamente");
        assertThat(response.getSaldo()).isEqualTo(925_000L);
        assertThat(response.getTransaccion()).isNotNull();
        verify(clienteRepository).save(any(ClienteDocument.class));
        verify(inscripcionRepository).save(any(InscripcionDocument.class));
    }

    @Test
    void suscribirFondo_saldoInsuficiente_retornaMensajeError() {
        cliente.setSaldo(10_000L); // menor que el monto del producto (75_000)
        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.of(producto));

        FondosResponseDto response = fondosService.suscribirFondo(request);

        assertThat(response.getMensaje()).contains("No tiene saldo disponible");
        assertThat(response.getSaldo()).isEqualTo(10_000L);
        verifyNoInteractions(inscripcionRepository);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void suscribirFondo_yaExisteSuscripcionActiva_retornaMensajeError() {
        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.existsByIdClienteAndIdProductoAndEstado("cli1", "prod1", "ACTIVO")).thenReturn(true);

        FondosResponseDto response = fondosService.suscribirFondo(request);

        assertThat(response.getMensaje()).contains("Ya tiene una suscripción activa");
        verify(inscripcionRepository, never()).save(any());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void suscribirFondo_clienteNoEncontrado_lanzaExcepcion() {
        when(clienteRepository.findById("cli1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fondosService.suscribirFondo(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cliente no encontrado");
    }

    @Test
    void suscribirFondo_productoNoEncontrado_lanzaExcepcion() {
        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fondosService.suscribirFondo(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado");
    }

    @Test
    void suscribirFondo_notificacionSms_noLanzaExcepcion() {
        cliente.setTipoNotificacion("SMS");
        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.existsByIdClienteAndIdProductoAndEstado("cli1", "prod1", "ACTIVO")).thenReturn(false);
        when(inscripcionMapper.toDto(any())).thenReturn(InscripcionDto.builder().build());

        FondosResponseDto response = fondosService.suscribirFondo(request);

        assertThat(response.getMensaje()).contains("exitosamente");
    }

    // ── cancelarSuscripcion ─────────────────────────────────────────────────

    @Test
    void cancelarSuscripcion_exitosa() {
        InscripcionDocument inscripcion = new InscripcionDocument();
        inscripcion.setIdCliente("cli1");
        inscripcion.setIdProducto("prod1");
        inscripcion.setEstado("ACTIVO");
        inscripcion.setMonto(75_000L);

        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.findByIdClienteAndIdProductoAndEstado("cli1", "prod1", "ACTIVO"))
                .thenReturn(Optional.of(inscripcion));
        when(inscripcionMapper.toDto(any(InscripcionDocument.class)))
                .thenReturn(InscripcionDto.builder().estado("CANCELADO").monto(75_000L).build());

        FondosResponseDto response = fondosService.cancelarSuscripcion(request);

        assertThat(response.getMensaje()).contains("cancelada");
        assertThat(response.getSaldo()).isEqualTo(1_075_000L);
        assertThat(inscripcion.getEstado()).isEqualTo("CANCELADO");
        assertThat(inscripcion.getFechaCancelacion()).isEqualTo(LocalDate.now());
        verify(clienteRepository).save(cliente);
        verify(inscripcionRepository).save(inscripcion);
    }

    @Test
    void cancelarSuscripcion_sinSuscripcionActiva_lanzaExcepcion() {
        when(clienteRepository.findById("cli1")).thenReturn(Optional.of(cliente));
        when(productoRepository.findById("prod1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.findByIdClienteAndIdProductoAndEstado("cli1", "prod1", "ACTIVO"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> fondosService.cancelarSuscripcion(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No tiene suscripción activa");
    }

    @Test
    void cancelarSuscripcion_clienteNoEncontrado_lanzaExcepcion() {
        when(clienteRepository.findById("cli1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fondosService.cancelarSuscripcion(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cliente no encontrado");
    }

    // ── consultarTransacciones ──────────────────────────────────────────────

    @Test
    void consultarTransacciones_retornaListaMapeada() {
        InscripcionDocument doc1 = new InscripcionDocument();
        doc1.setIdCliente("cli1");
        doc1.setEstado("ACTIVO");

        InscripcionDocument doc2 = new InscripcionDocument();
        doc2.setIdCliente("cli1");
        doc2.setEstado("CANCELADO");

        InscripcionDto dto1 = InscripcionDto.builder().idCliente("cli1").estado("ACTIVO").build();
        InscripcionDto dto2 = InscripcionDto.builder().idCliente("cli1").estado("CANCELADO").build();

        when(inscripcionRepository.findByIdCliente("cli1")).thenReturn(List.of(doc1, doc2));
        when(inscripcionMapper.toDto(doc1)).thenReturn(dto1);
        when(inscripcionMapper.toDto(doc2)).thenReturn(dto2);

        List<InscripcionDto> result = fondosService.consultarTransacciones("cli1");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(InscripcionDto::getEstado).containsExactly("ACTIVO", "CANCELADO");
    }

    @Test
    void consultarTransacciones_sinTransacciones_retornaListaVacia() {
        when(inscripcionRepository.findByIdCliente("cli1")).thenReturn(List.of());

        List<InscripcionDto> result = fondosService.consultarTransacciones("cli1");

        assertThat(result).isEmpty();
    }
}
