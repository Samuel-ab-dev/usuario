package com.seleste.usuario.business;

import com.seleste.usuario.business.DTO.UsuarioDTO;
import com.seleste.usuario.business.converter.UsuarioConverter;
import com.seleste.usuario.infrastructure.entity.Usuario;
import com.seleste.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {


private final UsuarioRepository usuarioRepository;
private final UsuarioConverter usuarioConverter;

public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
    usuario = usuarioRepository.save(usuario);
    return usuarioConverter.paraUsuarioDTO(usuario);
}
}
