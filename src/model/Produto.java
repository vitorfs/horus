package model;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class Produto {
    private Integer id;
    private String codigo;
    private Fornecedor fornecedor;
    private TipoProduto tipoProduto;
    private String nome;
    private String descricao;
    private Float valorEntrada;
    private Float valorSaida;
    private String codItemForn;
    private Integer icAtivo;
    private Integer qtdEstoque;
    private Integer qtdConsignado;
    private Integer qtdTotal;
    private Integer idProdutoMostruario;
    private Integer icVendido;
    private String data;
    
    public Produto(){
        
    }
    
    public Produto(Integer icAtivo){
        this.icAtivo = icAtivo;
    }

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
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the fornecedor
     */
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return the tipoProduto
     */
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    /**
     * @param tipoProduto the tipoProduto to set
     */
    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * @return the valorEntrada
     */
    public Float getValorEntrada() {
        return valorEntrada;
    }

    /**
     * @param valorEntrada the valorEntrada to set
     */
    public void setValorEntrada(Float valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    /**
     * @return the valorSaida
     */
    public Float getValorSaida() {
        return valorSaida;
    }

    /**
     * @param valorSaida the valorSaida to set
     */
    public void setValorSaida(Float valorSaida) {
        this.valorSaida = valorSaida;
    }

    /**
     * @return the codItemForn
     */
    public String getCodItemForn() {
        return codItemForn;
    }

    /**
     * @param codItemForn the codItemForn to set
     */
    public void setCodItemForn(String codItemForn) {
        this.codItemForn = codItemForn;
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

    /**
     * @return the qtdEstoque
     */
    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    /**
     * @param qtdEstoque the qtdEstoque to set
     */
    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    /**
     * @return the qtdConsignado
     */
    public Integer getQtdConsignado() {
        return qtdConsignado;
    }

    /**
     * @param qtdConsignado the qtdConsignado to set
     */
    public void setQtdConsignado(Integer qtdConsignado) {
        this.qtdConsignado = qtdConsignado;
    }

    /**
     * @return the qtdTotal
     */
    public Integer getQtdTotal() {
        return qtdTotal;
    }

    /**
     * @param qtdTotal the qtdTotal to set
     */
    public void setQtdTotal(Integer qtdTotal) {
        this.qtdTotal = qtdTotal;
    }
    
    @Override
    public String toString(){
        return this.codigo;
    }

    /**
     * @return the idProdutoMostruario
     */
    public Integer getIdProdutoMostruario() {
        return idProdutoMostruario;
    }

    /**
     * @param idProdutoMostruario the idProdutoMostruario to set
     */
    public void setIdProdutoMostruario(Integer idProdutoMostruario) {
        this.idProdutoMostruario = idProdutoMostruario;
    }

    /**
     * @return the icVendido
     */
    public Integer getIcVendido() {
        return icVendido;
    }

    /**
     * @param icVendido the icVendido to set
     */
    public void setIcVendido(Integer icVendido) {
        this.icVendido = icVendido;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

}
