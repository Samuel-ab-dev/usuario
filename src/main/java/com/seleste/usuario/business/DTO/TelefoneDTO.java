package com.seleste.usuario.business.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDTO {
    private Long id;
    private String numero;
    private String ddd;
}
