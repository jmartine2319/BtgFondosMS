package com.btg.fondos.service;

import com.btg.fondos.document.ClienteDocument;
import com.btg.fondos.dto.ClienteDto;
import com.btg.fondos.models.FondosResponseDto;
import com.btg.fondos.repository.ClienteRepository;
import com.btg.fondos.service.impl.ClientesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientesServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClientesServiceImpl clientesService;

    @Test
    void inscribirCliente_retornaMensajeConNombreYApellido() {
        ClienteDto request = new ClienteDto();
        request.setNombre("Ana");
        request.setApellido("Garcia");
        request.setEmail("ana@test.com");
        request.setTelefono("3109876543");
        request.setCiudad("Bogota");
        request.setTipoNotificacion("EMAIL");

        when(clienteRepository.save(any(ClienteDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        FondosResponseDto response = clientesService.inscribirCliente(request);

        assertThat(response.getMensaje()).contains("Ana").contains("Garcia").contains("inscrito correctamente");
    }

    @Test
    void inscribirCliente_guardaClienteConSaldoInicial500000() {
        ClienteDto request = new ClienteDto();
        request.setNombre("Carlos");
        request.setApellido("Lopez");
        request.setEmail("carlos@test.com");
        request.setTelefono("3001112233");
        request.setCiudad("Medellin");
        request.setTipoNotificacion("SMS");

        when(clienteRepository.save(any(ClienteDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        clientesService.inscribirCliente(request);

        ArgumentCaptor<ClienteDocument> captor = ArgumentCaptor.forClass(ClienteDocument.class);
        verify(clienteRepository).save(captor.capture());

        ClienteDocument guardado = captor.getValue();
        assertThat(guardado.getSaldo()).isEqualTo(500_000L);
        assertThat(guardado.getNombre()).isEqualTo("Carlos");
        assertThat(guardado.getApellido()).isEqualTo("Lopez");
        assertThat(guardado.getEmail()).isEqualTo("carlos@test.com");
        assertThat(guardado.getTelefono()).isEqualTo("3001112233");
        assertThat(guardado.getCiudad()).isEqualTo("Medellin");
        assertThat(guardado.getTipoNotificacion()).isEqualTo("SMS");
    }

    @Test
    void inscribirCliente_llamaRepositorioSave() {
        ClienteDto request = new ClienteDto();
        request.setNombre("Luis");
        request.setApellido("Torres");
        request.setEmail("luis@test.com");
        request.setTipoNotificacion("EMAIL");

        when(clienteRepository.save(any(ClienteDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        clientesService.inscribirCliente(request);

        verify(clienteRepository).save(any(ClienteDocument.class));
    }
}
