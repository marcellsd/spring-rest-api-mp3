package com.marcell.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.marcell.springrestapi.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto,Integer>{
}
