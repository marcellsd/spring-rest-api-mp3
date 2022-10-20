package com.marcell.springrestapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalhesItemPedidoDTO {
    String descricao;
    Integer quantidade;
}
