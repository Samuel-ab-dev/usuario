package com.seleste.usuario.business;

import com.seleste.usuario.business.DTO.EnderecoDTO;
import com.seleste.usuario.business.DTO.TelefoneDTO;
import com.seleste.usuario.business.DTO.UsuarioDTO;
import com.seleste.usuario.business.converter.UsuarioConverter;
import com.seleste.usuario.infrastructure.entity.Endereco;
import com.seleste.usuario.infrastructure.entity.Telefone;
import com.seleste.usuario.infrastructure.entity.Usuario;
import com.seleste.usuario.infrastructure.exceptions.ConflictException;
import com.seleste.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.seleste.usuario.infrastructure.repository.EnderecoRepository;
import com.seleste.usuario.infrastructure.repository.TelefoneRepository;
import com.seleste.usuario.infrastructure.repository.UsuarioRepository;
import com.seleste.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        emailExiste(usuarioDTO.getEmail());
        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado" + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado" + e.getCause());

        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                            () -> new ResourceNotFoundException("Email não encontrado: " + email)
                    )
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado: " + e.getCause());
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaUsuario(String token, UsuarioDTO usuarioDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ?
                passwordEncoder.encode(usuarioDTO.getSenha()) :
                null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado")
        );

        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        if (usuarioDTO.getSenha() != null) {
            usuario.setSenha(
                    passwordEncoder.encode(usuarioDTO.getSenha())
            );
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioConverter.paraUsuarioDTO(usuarioSalvo);
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco enderecoId = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Endereço(s) não localizado(s): " + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoId);

        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        return usuarioConverter.paraEnderecoDTO(enderecoSalvo);
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone telefoneId = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Telefone(s) não localizado(s): " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneId);

        Telefone telefoneSalvo = telefoneRepository.save(telefone);

        return usuarioConverter.paraTelefoneDTO(telefoneSalvo);
    }
}
