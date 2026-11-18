package com.seleste.usuario.controller;

import com.seleste.usuario.business.DTO.CepDTO;
import com.seleste.usuario.business.DTO.EnderecoDTO;
import com.seleste.usuario.business.DTO.TelefoneDTO;
import com.seleste.usuario.business.DTO.UsuarioDTO;
import com.seleste.usuario.business.UsuarioService;
import com.seleste.usuario.business.ViaCepService;
import com.seleste.usuario.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Métodos dos Usuários.")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ViaCepService cepService;

    @PostMapping
    @Operation(summary = "Salva Usuario", description = "Cria um novo usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Usuário já cadastrado.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return
                ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Usuário", description = "Login do usuário.")
    @ApiResponse(responseCode = "200", description = "Login feito com sucesso.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<String> loginUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.autenticarUsuário(usuarioDTO));
    }

    @GetMapping
    @Operation(summary = "Busca Dados do Usuário por Email",
            description = "Busca os dados do Usuário através do Email.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deleta Usuário",
            description = "Deleta os dados do Usuário através do Email.")
    @ApiResponse(responseCode = "200", description = "Usuário deletado.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Atualiza Usuário",
            description = "Atualiza os dados do Usuário.")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<UsuarioDTO> atualizaUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                      @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaUsuario(token, usuarioDTO));
    }

    @PutMapping("/endereco")
    @Operation(summary = "Atualiza Endereço",
            description = "Atualiza os dados de endereço do Usuário.")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                        @RequestParam("id") Long id) {

        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoDTO));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualiza Telefone",
            description = "Atualiza os dados de telefone do Usuário.")
    @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                        @RequestParam("id") Long id) {

        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, telefoneDTO));
    }

    @PostMapping("/endereco")
    @Operation(summary = "Cadastra Endereço",
            description = "Cadastra novo endereço de usuário.")
    @ApiResponse(responseCode = "200", description = "Endereço cadastrado com sucesso.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                        @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, enderecoDTO));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Cadastra Telefone",
            description = "Cadastra novo telefone de usuário.")
    @ApiResponse(responseCode = "200", description = "Telefone cadastrado com sucesso.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                        @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, telefoneDTO));
    }

    @GetMapping("/endereco/{cep}")
    @Operation(summary = "Busca Dados do Cep",
            description = "Busca dados do cep do usuário")
    @ApiResponse(responseCode = "200", description = "Informações resgatadas com sucesso.")
    @ApiResponse(responseCode = "403", description = "Informações não encontradas.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    public ResponseEntity<CepDTO> buscarDadosCep(@PathVariable("cep") String cep) {
        return ResponseEntity.ok(cepService.buscarDadosEndereco(cep));
    }
}
