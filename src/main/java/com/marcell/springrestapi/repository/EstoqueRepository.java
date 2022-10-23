package com.marcell.springrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcell.springrestapi.model.Estoque;
import com.marcell.springrestapi.model.Produto;


public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Estoque findByProduto(Produto produto);
}
