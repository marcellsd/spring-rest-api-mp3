package com.marcell.springrestapi.rest.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalhesPedidoDTO {
    Integer id;
    List<DetalhesItemPedidoDTO> itens;
    BigDecimal total;
}
