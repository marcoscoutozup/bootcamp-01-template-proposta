package com.github.marcoscoutozup.proposta.carteira;

import com.github.marcoscoutozup.proposta.carteira.enums.TipoCarteira;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(generator = "uuid4")
    private UUID id;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING) //1
    private TipoCarteira tipoCarteira;

    @Deprecated
    public Carteira() {
    }

    public Carteira(@NotBlank @Email String email, @NotNull TipoCarteira tipoCarteira) {
        this.email = email;
        this.tipoCarteira = tipoCarteira;
    }

    public boolean verificarParidadeDeCarteira(TipoCarteira tipoCarteira){
        Assert.notNull(tipoCarteira, "O tipo de carteira não pode ser nula");
        return this.tipoCarteira.equals(tipoCarteira);
    }

    public UUID getId() {
        return id;
    }
}
