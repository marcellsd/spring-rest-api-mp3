package com.marcell.springrestapi.rest.dto;

import com.marcell.springrestapi.model.Estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueDTO {
    private Integer quantidade;
    private Integer produtoId;
}
