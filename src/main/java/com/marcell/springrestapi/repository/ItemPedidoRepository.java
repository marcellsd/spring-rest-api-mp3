package com.marcell.springrestapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marcell.springrestapi.model.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido,Integer>{
    
    List<ItemPedido> findByPedidoId(Integer id);

}
