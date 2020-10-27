package com.github.marcoscoutozup.proposta.cartao;

import com.github.marcoscoutozup.proposta.avisos.AvisoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "cartao", url = "${host.cartao}")
public interface CartaoClient {

    //1
    @GetMapping("/api/cartoes")
    CartaoResponse pesquisarCartaoPorIdDaProposta(@RequestParam String idProposta);

    @PostMapping("/api/cartoes/{idCartao}/bloqueios")
    ResponseEntity bloquearCartao(@PathVariable UUID idCartao, @RequestBody Map bloqueioRequest);

    @PostMapping("/api/cartoes/{idCartao}/avisos")                                  //2
    ResponseEntity enviarAvisoDeViagem(@PathVariable UUID idCartao, @RequestBody AvisoRequest avisoRequest);

    @PostMapping("/api/cartoes/{idCartao}/carteiras")
    ResponseEntity associarCarteira(@RequestBody Map carteiraRequest, @PathVariable UUID idCartao);

}
