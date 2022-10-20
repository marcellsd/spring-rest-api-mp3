package com.marcell.springrestapi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.marcell.springrestapi.exception.RegraNegocioException;
import com.marcell.springrestapi.model.Cliente;
import com.marcell.springrestapi.model.ItemPedido;
import com.marcell.springrestapi.model.Pedido;
import com.marcell.springrestapi.model.Produto;
import com.marcell.springrestapi.repository.ClienteRepository;
import com.marcell.springrestapi.repository.ItemPedidoRepository;
import com.marcell.springrestapi.repository.PedidoRepository;
import com.marcell.springrestapi.repository.ProdutoRepository;
import com.marcell.springrestapi.rest.dto.ClientePedidosDTO;
import com.marcell.springrestapi.rest.dto.DetalhesItemPedidoDTO;
import com.marcell.springrestapi.rest.dto.DetalhesPedidoDTO;
import com.marcell.springrestapi.service.ClienteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService{


    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clientesRepository;

    @Override
    public ClientePedidosDTO getClientePedidos(Integer id) {
        
        ClientePedidosDTO clienteDTO = new ClientePedidosDTO();

        Cliente cliente = clientesRepository.findById(id)
        .orElseThrow(()-> new RegraNegocioException("Id do Cliente não encontrado."));

        clienteDTO.setNomeCliente(cliente.getNome());
        clienteDTO.setCpfCliente(cliente.getCpf());
        

        if (cliente != null){
            List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);
            List<DetalhesPedidoDTO> pedidosDTO = new ArrayList<DetalhesPedidoDTO>();
            
            for (Pedido pedido : pedidos) {
                List<DetalhesItemPedidoDTO> produtosDTO = new ArrayList<DetalhesItemPedidoDTO>();
                DetalhesPedidoDTO pedidoDTO = new DetalhesPedidoDTO();
                pedidoDTO.setId(pedido.getId());
                
               List<ItemPedido> produtos = pedido.getItens();
               
               for(ItemPedido produto : produtos){
                    DetalhesItemPedidoDTO produtoDTO = new DetalhesItemPedidoDTO();
                    Produto item = produto.getProduto();
                    produtoDTO.setDescricao(item.getDescricao());
                    produtoDTO.setQuantidade(produto.getQuantidade());
                    produtosDTO.add(produtoDTO);

               }

                pedidoDTO.setItens(produtosDTO);
                pedidoDTO.setTotal(pedido.getTotal());
                pedidosDTO.add(pedidoDTO);
            };

            clienteDTO.setPedidos(pedidosDTO);
            
        }

        return clienteDTO;
    }
    
}
