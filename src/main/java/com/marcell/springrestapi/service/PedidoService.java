package com.marcell.springrestapi.service;

import java.util.Optional;

import com.marcell.springrestapi.enums.StatusPedido;
import com.marcell.springrestapi.model.Pedido;
import com.marcell.springrestapi.rest.dto.PedidoDTO;

public interface PedidoService {
    Pedido salvar( PedidoDTO dto );
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
    void deletar(Integer id);
    
}
