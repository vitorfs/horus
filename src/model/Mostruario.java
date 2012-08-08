package model;

import java.util.List;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class Mostruario {
    private Integer id;
    private Revendedora revendedora;
    private String dataRetirada;
    private String dataAcerto;
    private String dataFechamento;
    private List<Produto> produtos;
    private Float valorTotal;
    private Integer qtdItens;
    private Integer status;
    private Integer qtdItensFechamento;
    private Float valorFechamento;
    private Float percentualComissaoFechamento;
    private List<PagamentoMostruario> pagamento;
    private Float valorEmAberto;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the dataRetirada
     */
    public String getDataRetirada() {
        return dataRetirada;
    }

    /**
     * @param dataRetirada the dataRetirada to set
     */
    public void setDataRetirada(String dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    /**
     * @return the dataAcerto
     */
    public String getDataAcerto() {
        return dataAcerto;
    }

    /**
     * @param dataAcerto the dataAcerto to set
     */
    public void setDataAcerto(String dataAcerto) {
        this.dataAcerto = dataAcerto;
    }

    /**
     * @return the dataFechamento
     */
    public String getDataFechamento() {
        return dataFechamento;
    }

    /**
     * @param dataFechamento the dataFechamento to set
     */
    public void setDataFechamento(String dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    /**
     * @return the produtos
     */
    public List<Produto> getProdutos() {
        return produtos;
    }

    /**
     * @param produtos the produtos to set
     */
    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    /**
     * @return the revendedora
     */
    public Revendedora getRevendedora() {
        return revendedora;
    }

    /**
     * @param revendedora the revendedora to set
     */
    public void setRevendedora(Revendedora revendedora) {
        this.revendedora = revendedora;
    }

    /**
     * @return the valorTotal
     */
    public Float getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the qtdItens
     */
    public Integer getQtdItens() {
        return qtdItens;
    }

    /**
     * @param qtdItens the qtdItens to set
     */
    public void setQtdItens(Integer qtdItens) {
        this.qtdItens = qtdItens;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the qtdItensFechamento
     */
    public Integer getQtdItensFechamento() {
        return qtdItensFechamento;
    }

    /**
     * @param qtdItensFechamento the qtdItensFechamento to set
     */
    public void setQtdItensFechamento(Integer qtdItensFechamento) {
        this.qtdItensFechamento = qtdItensFechamento;
    }

    /**
     * @return the valorFechamento
     */
    public Float getValorFechamento() {
        return valorFechamento;
    }

    /**
     * @param valorFechamento the valorFechamento to set
     */
    public void setValorFechamento(Float valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    /**
     * @return the percentualComissaoFechamento
     */
    public Float getPercentualComissaoFechamento() {
        return percentualComissaoFechamento;
    }

    /**
     * @param percentualComissaoFechamento the percentualComissaoFechamento to set
     */
    public void setPercentualComissaoFechamento(Float percentualComissaoFechamento) {
        this.percentualComissaoFechamento = percentualComissaoFechamento;
    }

    /**
     * @return the pagamento
     */
    public List<PagamentoMostruario> getPagamento() {
        return pagamento;
    }

    /**
     * @param pagamento the pagamento to set
     */
    public void setPagamento(List<PagamentoMostruario> pagamento) {
        this.pagamento = pagamento;
    }

    /**
     * @return the valorEmAberto
     */
    public Float getValorEmAberto() {
        return valorEmAberto;
    }

    /**
     * @param valorEmAberto the valorEmAberto to set
     */
    public void setValorEmAberto(Float valorEmAberto) {
        this.valorEmAberto = valorEmAberto;
    }
    
}
