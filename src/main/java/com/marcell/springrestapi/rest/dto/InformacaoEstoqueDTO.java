package com.marcell.springrestapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoEstoqueDTO {
    Integer idEstoque;
    Integer idProduto;
    String nomeProduto;
    Integer quantidade;
    
}
