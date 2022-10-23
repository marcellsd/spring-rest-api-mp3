
package com.marcell.springrestapi.rest.controllers;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import org.springframework.data.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.marcell.springrestapi.model.Estoque;
import com.marcell.springrestapi.model.Produto;
import com.marcell.springrestapi.repository.EstoqueRepository;
import com.marcell.springrestapi.repository.ProdutoRepository;
import com.marcell.springrestapi.rest.dto.EstoqueDTO;
import com.marcell.springrestapi.rest.dto.InformacaoEstoqueDTO;


@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {
    @Autowired
    private EstoqueRepository repository;
    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    @ResponseStatus(CREATED)
    public InformacaoEstoqueDTO save(@RequestBody EstoqueDTO estoqueDTO){
        Estoque estoque = new Estoque();
        estoque.setQuantidade(estoqueDTO.getQuantidade());
        Produto produto = produtoRepository
        .findById(estoqueDTO.getProdutoId())
        .orElseThrow( () ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Estoque não encontrado."));
        produto.setEstoque(estoque);
        estoque.setProduto(produto);
        repository.save(estoque);
        return InformacaoEstoqueDTO
                .builder()
                .idEstoque(estoque.getId())
                .idProduto(estoque.getProduto().getId())
                .nomeProduto(estoque.getProduto().getDescricao())
                .quantidade(estoque.getQuantidade())
                .build();
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update( @PathVariable Integer id, @RequestBody Estoque estoque ){
        repository
                .findById(id)
                .map( p -> {
                   estoque.setId(p.getId());
                   repository.save(estoque);
                   return estoque;
                }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id){
        repository
                .findById(id)
                .map( p -> {
                    repository.delete(p);
                    return Void.TYPE;
                }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado."));
    }

    @GetMapping("{id}")
    public Estoque getById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado."));
    }


    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Estoque updateEstoque(@PathVariable Integer id,
                                 @RequestBody Integer quantidade){
       return repository.findById(id)
        .map( oldEstoque -> {
            oldEstoque.setQuantidade(quantidade);
            repository.save(oldEstoque);
            return oldEstoque;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Estoque não encontrado"));
    }

    @GetMapping
    public List<InformacaoEstoqueDTO> findEstoqueByNomeProduto(Produto produto){
        ExampleMatcher matcher = ExampleMatcher
                                    .matching()
                                    .withIgnoreCase()
                                    .withStringMatcher(
                                            ExampleMatcher.StringMatcher.CONTAINING );

        Example example = Example.of(produto, matcher);
        List<Produto> produtoList = produtoRepository.findAll(example);
        List<Estoque> estoqueList = new ArrayList<Estoque>();
        if (produtoList.size()>0) {
           for (Produto produto2 : produtoList) {
             estoqueList.add(repository.findByProduto(produto2));
           }
        }
        List<InformacaoEstoqueDTO> estoqueListDTO = new ArrayList<InformacaoEstoqueDTO>();

        if (estoqueList.size()>0){
            for ( Estoque estoque : estoqueList) {
                estoqueListDTO.add(InformacaoEstoqueDTO
                                    .builder()
                                    .idEstoque(estoque.getId())
                                    .idProduto(estoque.getProduto().getId())
                                    .nomeProduto(estoque.getProduto().getDescricao())
                                    .quantidade(estoque.getQuantidade())
                                    .build());                
            }           
        }

        return estoqueListDTO;
    }
}


