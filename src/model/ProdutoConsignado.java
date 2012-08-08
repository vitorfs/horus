package model;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class ProdutoConsignado extends Produto{
    private String dataRetirada;
    private Revendedora revendedora;
    private Integer quantidade;

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
     * @return the quantidade
     */
    public Integer getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
