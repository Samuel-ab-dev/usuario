package com.seleste.usuario.business.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
}
