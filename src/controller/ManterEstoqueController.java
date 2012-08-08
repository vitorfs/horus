package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import model.LogEstoque;
import model.MotivoMovimentoEstoque;
import model.Produto;
import model.ProdutoConsignado;
import persistence.EstoqueDAO;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class ManterEstoqueController extends Observable{
    private static final ManterEstoqueController INSTANCE = new ManterEstoqueController();
    
    private ManterEstoqueController(){
        
    }
    
    public static ManterEstoqueController getInstance(){
        return INSTANCE;
    }
    
    public Produto getProdutoEstoque(String codigo) throws Exception, SQLException, ClassNotFoundException{
        return EstoqueDAO.getInstance().getProdutoEstoque(codigo);
    }
    
    public List<Produto> getEstoque() throws SQLException, ClassNotFoundException{
        return EstoqueDAO.getInstance().getProdutosEstoque();
    }
    
    public List<Produto> getEstoque(int order, int orientacao) throws SQLException, ClassNotFoundException{
        String strOrder = null;

        switch(order){
            case 0: strOrder = "codigo";break;
            case 1: strOrder = "codigo_origem_fornecedor";break;
            case 2: strOrder = "nome";break;
            case 3: strOrder = "t.descricao";break;
            case 4: strOrder = "nome_fantasia";break;
            case 5: strOrder = "valor_saida";break;
            case 6: strOrder = "qtd_estoque";break;
            case 7: strOrder = "qtd_consig";break;
            case 8: strOrder = "qtd_total";break;
            default: strOrder = "codigo";break;
        }
        
        if (orientacao == 0)
            strOrder += " asc";
        else
            strOrder += " desc";
        return EstoqueDAO.getInstance().getProdutosEstoque(strOrder);
    }
    
    public List<Produto> getProdutosDisponiveisEstoque() throws SQLException, ClassNotFoundException {
        return EstoqueDAO.getInstance().getProdutosDisponiveisEstoque();
    }
    
    public void movimentarEstoque(String codigo, int qtd) throws SQLException, ClassNotFoundException {
        EstoqueDAO.getInstance().movimentarEstoque(codigo, qtd);
        setChanged();
        notifyObservers();
    }
    
    public List<MotivoMovimentoEstoque> listarMotivoMovimentoEstoque() throws SQLException, ClassNotFoundException {
        return EstoqueDAO.getInstance().listarMotivoMovimentoEstoque();
    }
    
    
    public Produto getProdutoCodFornecedor(String cod) throws Exception, SQLException, ClassNotFoundException {
        return EstoqueDAO.getInstance().getProdutoCodFornecedor(cod);
    }      
    
    
    public List<LogEstoque> getHistoricoEstoque() throws Exception, SQLException, ClassNotFoundException {
        return EstoqueDAO.getInstance().getHistoricoEstoque();
    }
    
    public List<ProdutoConsignado> consultarProdutoConsignado(Produto produto) throws Exception, SQLException, ClassNotFoundException{
        return EstoqueDAO.getInstance().consultarProdutoConsignado(produto);
    }
    
}
