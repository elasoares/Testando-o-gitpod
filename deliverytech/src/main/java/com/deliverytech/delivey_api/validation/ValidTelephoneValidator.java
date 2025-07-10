package com.deliverytech.delivey_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTelephoneValidator implements ConstraintValidator<ValidTelephone, String>{
    @Override
    public boolean isValid(String telephone, ConstraintValidatorContext context){
        if(telephone == null || telephone.trim().isEmpty()){
            return true; // Considerar nulo/vazio como válido (use @NotBlank junto se for obrigatório
        }

        // Regex para telefone brasileiro:
        // Permite formatos como: (DD)XXXXX-XXXX, DDXXXXX-XXXX, DDXXXXXXXXX
        // Remove caracteres não numéricos para a contagem de dígitos
        String cleanedPhone = telephone.replace("[^0-9]", "");
        // Valida se tem 10 (DDD + 8 ou 9 dígitos) ou 11 (DDD + 9 dígitos)
        return cleanedPhone.matches("^\\d{10}$") || cleanedPhone.matches("^\\d{11}$");
    }
}
