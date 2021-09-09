package br.com.sysve.conversor;

import br.com.sysve.dtos.ProdutoDto;
import br.com.sysve.entities.Produto;
import br.com.sysve.entities.ProdutoTransacao;


import java.math.BigDecimal;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        Produto p =  new Produto();
        p.setNome("teste");
        p.setValorVenda(new BigDecimal("1.55"));
        p.setId(154l);
        ProdutoTransacao pt = new ProdutoTransacao();
        pt.setQuantidade(new BigDecimal("798"));
        p.setProdutoTransacao(new ArrayList<>());
        p.getProdutoTransacao().add(pt);
        System.out.println(p.getValorVenda());

        ProdutoDto dto = new ProdutoDto();
        IConversor c = new Conversor();

        c.entityToDto(null, p,dto, 1);

        System.out.println(dto);

    }
}
