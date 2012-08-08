package model;

/**
 * @author Vitor Freitas
 * contact vitorfs@gmail.com
 */
public class TipoProduto {
    private String codigo;
    private String descricao;

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        if (codigo.length() > 3)
            this.codigo = null;
        else
            this.codigo = codigo;
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
    
    @Override
    public String toString(){
        return this.descricao;
    }
    
}
