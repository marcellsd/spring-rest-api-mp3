package com.marcell.springrestapi.service.impl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.marcell.springrestapi.enums.StatusPedido;
import com.marcell.springrestapi.exception.PedidoNaoEncontradoException;
import com.marcell.springrestapi.exception.RegraNegocioException;
import com.marcell.springrestapi.model.Cliente;
import com.marcell.springrestapi.model.Estoque;
import com.marcell.springrestapi.model.ItemPedido;
import com.marcell.springrestapi.model.Pedido;
import com.marcell.springrestapi.model.Produto;
import com.marcell.springrestapi.repository.ClienteRepository;
import com.marcell.springrestapi.repository.EstoqueRepository;
import com.marcell.springrestapi.repository.ItemPedidoRepository;
import com.marcell.springrestapi.repository.PedidoRepository;
import com.marcell.springrestapi.repository.ProdutoRepository;
import com.marcell.springrestapi.rest.dto.ItemPedidoDTO;
import com.marcell.springrestapi.rest.dto.PedidoDTO;
import com.marcell.springrestapi.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    
    private final PedidoRepository repository;
    private final ClienteRepository clientesRepository;
    private final ProdutoRepository produtosRepository;
    private final ItemPedidoRepository itemsPedidoRepository;
    private final EstoqueRepository estoqueRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        //pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        float total = 0;
        for (ItemPedido itemPedido : itemsPedido) {
            total = total + itemPedido.getQuantidade().floatValue() * itemPedido.getProduto().getPreco().floatValue();
        }
        pedido.setTotal(new BigDecimal(total));
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }
    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: "+ idProduto
                                    ));
                    if (checarAtualizarEstoque(produto, dto.getQuantidade())){
                        ItemPedido itemPedido = new ItemPedido();
                        itemPedido.setQuantidade(dto.getQuantidade());
                        itemPedido.setPedido(pedido);
                        itemPedido.setProduto(produto);
                        return itemPedido;
                    }
                    else return null;
                    
                }).collect(Collectors.toList());

    }
    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        
        return repository.findByIdFetchItens(id);
    }
    @Override
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException() );
        
    }
    @Override
    public void deletar(Integer id) {
        Pedido pedido = repository.findById(id)
        .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado."));
        
        if (pedido != null){
            List<ItemPedido> itemPedidos = itemsPedidoRepository.findByPedidoId(id);
            if (itemPedidos != null){
                for (ItemPedido itemPedido: itemPedidos){
                    itemsPedidoRepository.delete(itemPedido);
                }
            }
            repository.delete(pedido);
        } 


    }

    public boolean checarAtualizarEstoque(Produto produto, Integer quantidade){
        boolean check = false;
        Estoque estoque = estoqueRepository.findByProduto(produto);
        if (estoque == null){
            throw(new RegraNegocioException("Estoque do produto"+ produto.getDescricao() +"não encontrado."));
        }
        else{
            if (estoque.getQuantidade()<quantidade) {
                throw(new RegraNegocioException("Estoque do produto"+ produto.getDescricao() +"não possui uma quantidade suficiente."));   
            }
            else{
                estoque.setQuantidade(estoque.getQuantidade() - quantidade);
                check = true;
            }
        }
        return check;
    }

}
