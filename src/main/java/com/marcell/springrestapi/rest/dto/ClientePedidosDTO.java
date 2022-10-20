package com.marcell.springrestapi.rest.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientePedidosDTO {
    String nomeCliente;
    String cpfCliente;
    List<DetalhesPedidoDTO> pedidos;
}
