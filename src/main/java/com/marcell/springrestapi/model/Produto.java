package com.marcell.springrestapi.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // nao obrigatório - vai ser igual o nome do atributo
    private Integer id;

    @Column
    private String descricao;

    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "estoque_produto", joinColumns = {
            @JoinColumn(name = "produto_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "estoque_id", referencedColumnName = "id") })
    private Estoque estoque;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", descricao=" + descricao + ", preco=" + preco + "]";
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

}
