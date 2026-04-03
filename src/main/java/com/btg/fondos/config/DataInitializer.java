package com.btg.fondos.config;

import com.btg.fondos.document.Cliente;
import com.btg.fondos.document.Producto;
import com.btg.fondos.dto.ClienteDto;
import com.btg.fondos.repository.ClienteRepository;
import com.btg.fondos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            List<Producto> productos = List.of(
                    crearProducto("1", "FPV_BTG_PACTUAL_RECAUDADORA", "FPV", 75_000),
                    crearProducto("2", "FPV_BTG_PACTUAL_ECOPETROL",   "FPV", 125_000),
                    crearProducto("3", "DEUDAPRIVADA",                 "FIC", 50_000),
                    crearProducto("4", "FDO-ACCIONES",                 "FIC", 250_000),
                    crearProducto("5", "FPV_BTG_PACTUAL_DINAMICA",    "FPV", 100_000)
            );
            productoRepository.saveAll(productos);
        }
        if(clienteRepository.count()==0){
            ClienteDto clienteDto = ClienteDto.builder()
                    .id("1")
                    .apellido("Lopez")
                    .nombre("Andres")
                    .email("correo@correo.com")
                    .ciudad("Bogota")
                    .saldo(500_000L)
                    .tipoNotificacion("correo").build();
            clienteRepository.save(crearCliente(clienteDto));
        }
    }

    private Producto crearProducto(String id, String nombre, String tipo, long monto) {
        return Producto.builder()
                .id(id)
                .nombre(nombre)
                .tipoProducto(tipo)
                .monto(monto).build();
    }

    private Cliente crearCliente(ClienteDto cliente){

        return Cliente.builder().
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
