package model;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class LogEstoque {
    private Integer id;
    private String fluxo;
    private String motivo;
    private String dataMovimentacao;
    private String idProduto;
    private Integer qtdProduto;
    
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
     * @return the fluxo
     */
    public String getFluxo() {
        return fluxo;
    }

    /**
     * @param fluxo the fluxo to set
     */
    public void setFluxo(String fluxo) {
        this.fluxo = fluxo;
    }

    /**
     * @return the dataMovimentacao
     */
    public String getDataMovimentacao() {
        return dataMovimentacao;
    }

    /**
     * @param dataMovimentacao the dataMovimentacao to set
     */
    public void setDataMovimentacao(String dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    /**
     * @return the qtdProduto
     */
    public Integer getQtdProduto() {
        return qtdProduto;
    }

    /**
     * @param qtdProduto the qtdProduto to set
     */
    public void setQtdProduto(Integer qtdProduto) {
        this.qtdProduto = qtdProduto;
    }

    /**
     * @return the idProduto
     */
    public String getIdProduto() {
        return idProduto;
    }

    /**
     * @param idProduto the idProduto to set
     */
    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
}
