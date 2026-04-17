package com.example.marketplace.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.marketplace.model.CategoriaProduto;
import com.example.marketplace.model.ItemCarrinho;
import com.example.marketplace.model.Produto;
import com.example.marketplace.model.ResumoCarrinho;
import com.example.marketplace.model.SelecaoCarrinho;
import com.example.marketplace.repository.ProdutoRepository;

@Service
public class ServicoCarrinho {

    private final ProdutoRepository repositorioProdutos;

    public ServicoCarrinho(ProdutoRepository repositorioProdutos) {
        this.repositorioProdutos = repositorioProdutos;
    }

    public ResumoCarrinho construirResumo(List<SelecaoCarrinho> selecoes) {

        List<ItemCarrinho> itens = new ArrayList<>();

        // =========================
        // Monta os itens do carrinho
        // =========================
        for (SelecaoCarrinho selecao : selecoes) {
            Produto produto = repositorioProdutos.buscarPorId(selecao.getProdutoId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Produto não encontrado: " + selecao.getProdutoId()));

            itens.add(new ItemCarrinho(produto, selecao.getQuantidade()));
        }

        // =========================
        // Calcula subtotal
        // =========================
        BigDecimal subtotal = itens.stream()
                .map(ItemCarrinho::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // =========================
        // Calcula descontos
        // =========================
        int totalItens = itens.stream().mapToInt(ItemCarrinho::getQuantidade).sum();
        BigDecimal descontoQuantidade = calcularDescontoQuantidade(totalItens);
        BigDecimal descontoCategoria = calcularDescontoCategoria(itens);
        BigDecimal percentualDesconto = aplicarLimiteMaximo(descontoQuantidade, descontoCategoria);
        BigDecimal valorDesconto = subtotal.multiply(percentualDesconto).divide(new BigDecimal("100"), 2,
                RoundingMode.HALF_UP);
        BigDecimal total = subtotal.subtract(valorDesconto);

        return new ResumoCarrinho(itens, subtotal, percentualDesconto, valorDesconto, total);
    }

    /**
     * Calcula o desconto por quantidade de itens no carrinho
     * - 1 item = 0%
     * - 2 itens = 5%
     * - 3 itens = 7%
     * - 4 ou mais itens = 10%
     */
    private BigDecimal calcularDescontoQuantidade(int totalItens) {
        if (totalItens >= 4) {
            return new BigDecimal("10");
        } else if (totalItens == 3) {
            return new BigDecimal("7");
        } else if (totalItens == 2) {
            return new BigDecimal("5");
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcula o desconto por categoria
     * O desconto é aplicado por item, não por categoria única
     * - CAPINHA = 3%
     * - CARREGADOR = 5%
     * - FONE = 3%
     * - PELICULA = 2%
     * - SUPORTE = 2%
     */
    private BigDecimal calcularDescontoCategoria(List<ItemCarrinho> itens) {
        BigDecimal descontoTotal = BigDecimal.ZERO;

        for (ItemCarrinho item : itens) {
            CategoriaProduto categoria = item.getProduto().getCategoria();
            BigDecimal descontoCategoria = obterDescontoCategoria(categoria);
            BigDecimal descontoParaEsteItem = descontoCategoria.multiply(BigDecimal.valueOf(item.getQuantidade()));
            descontoTotal = descontoTotal.add(descontoParaEsteItem);
        }

        return descontoTotal;
    }

    /**
     * Retorna o percentual de desconto para uma categoria específica
     */
    private BigDecimal obterDescontoCategoria(CategoriaProduto categoria) {
        return switch (categoria) {
            case CAPINHA -> new BigDecimal("3");
            case CARREGADOR -> new BigDecimal("5");
            case FONE -> new BigDecimal("3");
            case PELICULA -> new BigDecimal("2");
            case SUPORTE -> new BigDecimal("2");
        };
    }

    /**
     * Aplica o limite máximo de 25% no desconto total
     */
    private BigDecimal aplicarLimiteMaximo(BigDecimal descontoQuantidade, BigDecimal descontoCategoria) {
        BigDecimal descontoTotal = descontoQuantidade.add(descontoCategoria);
        BigDecimal limiteMaximo = new BigDecimal("25");

        if (descontoTotal.compareTo(limiteMaximo) > 0) {
            return limiteMaximo;
        }

        return descontoTotal;
    }
}
