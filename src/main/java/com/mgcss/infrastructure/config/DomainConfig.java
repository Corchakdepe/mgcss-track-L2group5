package com.mgcss.infrastructure.config;

import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.ClienteService;
import com.mgcss.service.SolicitudService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public SolicitudService solicitudService(
            SolicitudRepository solicitudRepository,
            TecnicoRepository tecnicoRepository,
            ClienteRepository clienteRepository) {
        // Spring inyectará automáticamente los Adapters aquí porque implementan las interfaces
        return new SolicitudService(solicitudRepository, tecnicoRepository, clienteRepository);
    }

    @Bean
    public ClienteService clienteService(ClienteRepository clienteRepository) {
        return new ClienteService(clienteRepository);
    }
}