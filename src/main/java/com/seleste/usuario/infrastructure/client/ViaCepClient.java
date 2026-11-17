package com.seleste.usuario.infrastructure.client;

import com.seleste.usuario.business.DTO.CepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface ViaCepClient {
    @GetMapping("/ws/{cep}/json/")
    CepDTO buscaDadosEndereco(@PathVariable("cep") String cep);
}
