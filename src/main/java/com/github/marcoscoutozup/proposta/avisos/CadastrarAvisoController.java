package com.github.marcoscoutozup.proposta.avisos;

import com.github.marcoscoutozup.proposta.biometria.Biometria;
import com.github.marcoscoutozup.proposta.biometria.BiometriaDTO;
import com.github.marcoscoutozup.proposta.cartao.Cartao;
import com.github.marcoscoutozup.proposta.exception.StandardError;
import com.github.marcoscoutozup.proposta.validator.requestbloqueiocartao.RequestBloqueioCartao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cartoes")
@Validated
public class CadastrarAvisoController {

    private EntityManager entityManager;
    private Logger logger = LoggerFactory.getLogger(Biometria.class);

    public CadastrarAvisoController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/{idCartao}/aviso")
    @Transactional
    public ResponseEntity cadastrarBiometria(@PathVariable UUID idCartao,
                                             @RequestBody @Valid AvisoDTO dto,   //1
                                             @RequestBloqueioCartao HttpServletRequest request,
                                             UriComponentsBuilder uri){

        //2
        Optional<Cartao> cartaoProcurado = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        //3
        if(cartaoProcurado.isEmpty()){
            logger.warn("[CADASTRO DE AVISO] O número do cartão não foi encontrado. Id: {}", idCartao);
            //4
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardError(Arrays.asList("Cartão não encontrado")));
        }

        //5
        Aviso aviso = dto.toAviso(request);
        entityManager.persist(aviso);
        logger.warn("[CADASTRO DE AVISO] Aviso cadastrado: {}", aviso.toString());

        Cartao cartao = cartaoProcurado.get();
        cartao.incluirAvisoDeViagem(aviso);
        entityManager.merge(cartao);
        logger.warn("[CADASTRO DE AVISO] Aviso associado ao cartão: {}", cartao.toString());

        return ResponseEntity
                .created(uri.path("/avisos/{id}")
                        .buildAndExpand(idCartao)
                        .toUri())
                .build();
    }
}