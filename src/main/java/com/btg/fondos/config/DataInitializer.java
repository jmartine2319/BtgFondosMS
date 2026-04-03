package com.btg.fondos.config;

import com.btg.fondos.document.ClienteDocument;
import com.btg.fondos.document.ProductoDocument;
import com.btg.fondos.document.UsuarioDocument;
import com.btg.fondos.dto.ClienteDto;
import com.btg.fondos.repository.ClienteRepository;
import com.btg.fondos.repository.ProductoRepository;
import com.btg.fondos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            List<ProductoDocument> productoDocuments = List.of(
                    crearProducto("1", "FPV_BTG_PACTUAL_RECAUDADORA", "FPV", 75_000),
                    crearProducto("2", "FPV_BTG_PACTUAL_ECOPETROL",   "FPV", 125_000),
                    crearProducto("3", "DEUDAPRIVADA",                 "FIC", 50_000),
                    crearProducto("4", "FDO-ACCIONES",                 "FIC", 250_000),
                    crearProducto("5", "FPV_BTG_PACTUAL_DINAMICA",    "FPV", 100_000)
            );
            productoRepository.saveAll(productoDocuments);
        }
        if(clienteRepository.count()==0){
            ClienteDto clienteDto = new ClienteDto();
            clienteDto.setId("1");
            clienteDto.setApellido("Lopez");
            clienteDto.setNombre("Andres");
            clienteDto.setEmail("correo@correo.com");
            clienteDto.setCiudad("Bogota");
            clienteDto.setSaldo(500_000L);
            clienteDto.setTipoNotificacion("correo");
            clienteRepository.save(crearCliente(clienteDto));
        }
        if(usuarioRepository.count()==0){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            UsuarioDocument usuarioDocument = UsuarioDocument.builder()
                    .id("1")
                    .password(passwordEncoder.encode("123456"))
                    .username("julian")
                    .rol("admin").build();
            usuarioRepository.save(usuarioDocument);
        }
    }

    private ProductoDocument crearProducto(String id, String nombre, String tipo, long monto) {
        return ProductoDocument.builder()
                .id(id)
                .nombre(nombre)
                .tipoProducto(tipo)
                .monto(monto).build();
    }

    private ClienteDocument crearCliente(ClienteDto cliente){

        return ClienteDocument.builder().
                id(cliente.getId()).
                nombre(cliente.getNombre()).
                apellido(cliente.getApellido()).
                ciudad(cliente.getCiudad()).
                saldo(cliente.getSaldo()).
                tipoNotificacion(cliente.getTipoNotificacion()).
                email(cliente.getEmail()).
                telefono(cliente.getEmail()).
                build();
    }
}
