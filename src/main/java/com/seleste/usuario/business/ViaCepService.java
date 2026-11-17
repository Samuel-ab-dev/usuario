package com.seleste.usuario.business;

import com.seleste.usuario.business.DTO.CepDTO;
import com.seleste.usuario.infrastructure.client.ViaCepClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final ViaCepClient cepClient;

    public CepDTO buscarDadosEndereco(String cep) {
        return cepClient.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep) {

        String cepFormatado = cep.replaceAll("[.-]", "");

        if (cepFormatado == null || !cepFormatado.matches("\\d{8}")) {
            throw new IllegalArgumentException("Cep inválido: " + cep);
        }

        return cepFormatado;
    }
}
