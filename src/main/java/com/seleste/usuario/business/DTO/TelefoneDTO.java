package com.seleste.usuario.business.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDTO {
    private String numero;
    private String ddd;
}
