package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import model.PagamentoMostruario;
import persistence.FinanceiroDAO;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class FinanceiroController extends Observable {
    private static final FinanceiroController INSTANCE = new FinanceiroController();
    
    private FinanceiroController(){
        
    }
    
    public static FinanceiroController getInstance(){
        return INSTANCE;
    }    
    
    public List<PagamentoMostruario> getListaPagamentosEmAberto() throws Exception, SQLException, ClassNotFoundException{
        return FinanceiroDAO.getInstance().listarPagamentosEmAberto();
    }    
}
