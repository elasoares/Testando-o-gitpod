package com.deliverytech.delivey_api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


//Essa é uma anotação dentro da sua anotação! Ela define onde a  @ValidTelePhone pode ser usada.
@Target({ElementType.FIELD, ElementType.PARAMETER}) 

//RetentionPolicy.RUNTIME significa que o Java manterá a informação dessa anotação disponível 
//em tempo de execução (ou seja, quando o programa estiver rodando). Isso é crucial porque o 
//Spring e as bibliotecas de validação precisam "ler" essa anotação enquanto o código está 
//funcionando para poder aplicar a validação.
@Retention(RetentionPolicy.RUNTIME)

//O Constraint é a anotação mais importante para validações customizadas! 
//Ela liga a sua @ValidTelephone à classe que realmente contém a lógica de como validar um telefone.
@Constraint(validatedBy = ValidTelephoneValidator.class)

//O Documented É uma anotação simples que indica que essa anotação deve 
//ser incluída na documentação gerada pelo Javadoc (uma ferramenta para criar documentação de código Java)
//Pra que serve: Ajuda outros desenvolvedores (ou você mesmo no futuro) a entender
// o que essa anotação faz, olhando a documentação.
@Documented

// Essa é a declaração da sua anotação customizada.
// O @interface é a palavra-chave que diz ao Java que você está criando uma anotação.
public @interface ValidTelephone{

    // Isso define o texto da mensagem de erro padrão que será mostrada se a validação falhar.
    String message() default "Formato de telefone inválido. Use (DDD)XXXXX-XXXX ou (DDD)XXXXXXXXX.";




    //As Classes são atributos avançados que podem ser usados para 
    //agrupar validações e para a maioria dos casos simples, podemos  deixá-los como estão (default{}). 
    //Eles não vão interferir no funcionamento básico da sua validação.
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
    
}