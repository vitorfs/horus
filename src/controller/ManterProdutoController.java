package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import model.Produto;
import persistence.ProdutoDAO;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class ManterProdutoController extends Observable{
    private static final ManterProdutoController INSTANCE = new ManterProdutoController();
    
    private ManterProdutoController(){
        
    }
    
    public static ManterProdutoController getInstance(){
        return INSTANCE;
    }
    
    public Produto getProduto(Integer id) throws SQLException, ClassNotFoundException{
        return ProdutoDAO.getInstance().getProduto(id);
    }
    
    public Produto getProduto(String cod, Integer disp, Integer dispAlt) throws Exception, SQLException, ClassNotFoundException{
        Produto produto = null;
        if (cod!=null&&cod.trim().length()!=0){
            produto = ProdutoDAO.getInstance().getProduto(cod, disp, dispAlt);
            if (produto!=null)
                return produto;
            else
                throw new Exception("Produto não localizado.");
        }
        else
            throw new Exception("Produto não localizado.");
    }    
    
    public List<Produto> getProdutos() throws SQLException, ClassNotFoundException{
        return ProdutoDAO.getInstance().getProdutos();
    }
    
    public List<Produto> getProdutos(Produto filtro, int limit, int offset) throws SQLException, ClassNotFoundException{
        return ProdutoDAO.getInstance().getProdutos(filtro, limit, offset);
    }
    
    public Integer getNextId() {
        return ProdutoDAO.getInstance().getNextId();
    }    
    
    public Integer countProdutos(Produto filtro){
        return ProdutoDAO.getInstance().countProdutos(filtro);
    }
    
    private Produto ajustaAtributosProduto(Produto produto){
        produto.setNome(Controller.ajustaAtributo(produto.getNome()));
        produto.setDescricao(Controller.ajustaAtributo(produto.getDescricao()));
        produto.setCodItemForn(Controller.ajustaAtributo(produto.getCodItemForn()));
        //produto.setValorEntrada(null);
        //produto.setValorSaida();        
        return produto;
    }
    
    public void save(Produto produto) throws SQLException, ClassNotFoundException, Exception{
        produto = ajustaAtributosProduto(produto);
        String mensagem = "";
        if (produto.getNome()==null)
            throw new Exception("Informe o nome do produto!");
        
        if (produto.getTipoProduto()==null)
            throw new Exception("Informe o tipo do produto!");
        
       /* if (produto.getDescricao()==null)
            throw new Exception("Informe a descrição do produto!");*/
        
        if (produto.getFornecedor()==null)
            throw new Exception("Informe o fornecedor do produto!");
        
        if (ProdutoDAO.getInstance().getProduto(produto.getId())==null){
            ProdutoDAO.getInstance().insert(produto);
            setChanged();
            mensagem = "Produto incluído com sucesso!";
        }
        else {
            Produto validacao = ProdutoDAO.getInstance().getProduto(produto.getId());

            if (validacao.getQtdConsignado()>0) {
                /*if (!produto.getValorSaida().equals(validacao.getValorSaida()))
                    throw new Exception("Não é possível alterar o preço de um produto consignado.");
                else*/ if (!produto.getTipoProduto().getCodigo().equals(validacao.getTipoProduto().getCodigo()))
                    throw new Exception("Não é possível alterar o tipo de um produto consignado.");
                else if (!produto.getFornecedor().getId().equals(validacao.getFornecedor().getId()))
                    throw new Exception("Não é possível alterar o fornecedor de um produto consignado.");
                /*else if (!produto.getNome().equals(validacao.getNome()))
                    throw new Exception("Não é possível alterar o nome de um produto consignado.");*/
            }
            
            if (validacao.getQtdConsignado()+validacao.getQtdEstoque()>0)
                if (produto.getIcAtivo()==0)
                    throw new Exception("Não é possível desativar um produto com peças em estoque.");
            
            ProdutoDAO.getInstance().update(produto);
            setChanged();
            mensagem = "Produto alterado com sucesso!";
        }
        notifyObservers(mensagem);
    }
    
    public void delete(Produto produto) throws Exception, SQLException, ClassNotFoundException{
        Produto validacao;
        validacao = ProdutoDAO.getInstance().getProduto(produto.getId());
        if (validacao.getQtdConsignado()+validacao.getQtdEstoque()>0)
            throw new Exception("Este produto possui peças em estoque.");
        ProdutoDAO.getInstance().delete(produto);
        setChanged();
        notifyObservers();
    }
    
}
