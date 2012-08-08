package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import javax.swing.JOptionPane;
import model.TipoProduto;
import persistence.TipoProdutoDAO;

/**
 * @author Vitor Freitas
 * contact vitorfs@gmail.com
 */
public class ManterTipoProdutoController extends Observable {
    private static final ManterTipoProdutoController INSTANCE = new ManterTipoProdutoController();
    
    public static ManterTipoProdutoController getInstance(){
        return INSTANCE;
    }
    private ManterTipoProdutoController(){
        
    }
    
    public List<TipoProduto> getTipoProdutos() throws SQLException, ClassNotFoundException{
        return TipoProdutoDAO.getInstance().getTipoProdutos();
    }
    
    public void delete(TipoProduto tipoProduto) throws SQLException, ClassNotFoundException{
        TipoProdutoDAO.getInstance().delete(tipoProduto);
        setChanged();
        notifyObservers();
        
    }
    
    public void save(TipoProduto tipoProduto) throws Exception, SQLException, ClassNotFoundException{
        try {
            if (TipoProdutoDAO.getInstance().getTipoProduto(tipoProduto.getCodigo())!=null) {
                TipoProdutoDAO.getInstance().update(tipoProduto);
            } else {
                TipoProdutoDAO.getInstance().insert(tipoProduto);
            }
            setChanged();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        } finally {
            notifyObservers();
        }
    }
    
}
