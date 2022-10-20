package com.marcell.springrestapi.service;

import com.marcell.springrestapi.rest.dto.ClientePedidosDTO;

public interface ClienteService {
    ClientePedidosDTO getClientePedidos(Integer id);
}
