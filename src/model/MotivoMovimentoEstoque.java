package model;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class MotivoMovimentoEstoque {
    private Integer id;
    private String descricao;
    private Integer icAtivo;

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
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the icAtivo
     */
    public Integer getIcAtivo() {
        return icAtivo;
    }

    /**
     * @param icAtivo the icAtivo to set
     */
    public void setIcAtivo(Integer icAtivo) {
        this.icAtivo = icAtivo;
    }
    
    public String toString(){
        return descricao;
    }
    
}
