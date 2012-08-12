package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import model.Mostruario;
import model.PagamentoMostruario;
import model.Produto;
import persistence.MostruarioDAO;
import util.Horus;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class ManterMostruarioController extends Observable{
    private static final ManterMostruarioController INSTANCE = new ManterMostruarioController();
    
    private ManterMostruarioController(){
        
    }
    
    public static ManterMostruarioController getInstance(){
        return INSTANCE;
    }
    
    public void inserirNovoMostruario(Mostruario mostruario) throws Exception, SQLException, ClassNotFoundException{
        if (mostruario.getRevendedora()==null)
            throw new Exception("Associe o mostruario a uma revendedora antes de salvar.");
        
        if (!Horus.isValidDate(mostruario.getDataRetirada()))
            throw new Exception("Data de Retirada inválida.");
        
        if (!Horus.isValidDate(mostruario.getDataAcerto()))
            throw new Exception("Data de Acerto inválida.");
        
        if (mostruario.getProdutos().isEmpty())
            throw new Exception("Nenhum produto foi informado.");
        
        MostruarioDAO.getInstance().insert(mostruario);
        
        setChanged();
        notifyObservers();
    }
    
    public void atualizarMostruario(Mostruario mostruario) throws Exception, SQLException, ClassNotFoundException {
        if (mostruario.getRevendedora()==null)
            throw new Exception("Associe o mostruario a uma revendedora antes de salvar.");
        
        if (!Horus.isValidDate(mostruario.getDataRetirada()))
            throw new Exception("Data de Retirada inválida.");
        
        if (!Horus.isValidDate(mostruario.getDataAcerto()))
            throw new Exception("Data de Acerto inválida.");
        
        if (mostruario.getProdutos().isEmpty())
            throw new Exception("Nenhum produto foi informado.");
        
        MostruarioDAO.getInstance().update(mostruario);
        
        setChanged();
        notifyObservers();        
    }
    
    public Mostruario getMostruario(int id) throws SQLException, ClassNotFoundException {
        return MostruarioDAO.getInstance().getMostruario(id);
    }    
    
    public List<Mostruario> listarMostruarios(boolean exibirTudo) throws SQLException, ClassNotFoundException {
        return MostruarioDAO.getInstance().listarMostruarios(exibirTudo);
    }
    
    public List<Produto> listarProdutosMostruario(Integer idMostruario, Integer status) throws SQLException, ClassNotFoundException {
        return MostruarioDAO.getInstance().listarProdutosMostruario(idMostruario, status);
    }
    
    public List<Produto> listarProdutosMostruario(Integer idMostruario) throws SQLException, ClassNotFoundException {
        return MostruarioDAO.getInstance().listarProdutosMostruario(idMostruario);
    }    
    
    public Map preencherDisponibilidade(Integer idMostruario) throws SQLException, ClassNotFoundException {
        return MostruarioDAO.getInstance().preencherDisponibilidade(idMostruario);
    }
    
    public void validarPgtoFechamento(List<PagamentoMostruario> pgtos) throws Exception{
        if (pgtos!=null){
            for (PagamentoMostruario pgto : pgtos){
                        if (!Horus.isValidDate(pgto.getDataPrevisao())){
                            throw new Exception("Data de pagamento inválida! (dd/mm/aaaa)");
                        }
                        if (pgto.isPago()) {
                            if (!Horus.isValidDate(pgto.getDataRealizado())){
                                throw new Exception("Data de pagamento realizado inválida! (dd/mm/aaaa)");
                            }
                        }
                    }        
        }
    }
    
    public void fecharMostruario(Mostruario mostruario, 
                                List<Produto> produtosVendidos,
                                List<Produto> produtosDevolvidos) 
            throws Exception, SQLException, ClassNotFoundException{ 
        
        validarPgtoFechamento(mostruario.getPagamento());
        
        MostruarioDAO.getInstance().fecharMostruario(mostruario, produtosVendidos, produtosDevolvidos);
        setChanged();
        notifyObservers();
    }
    
    public void excluirMostruario(Mostruario mostruario) throws SQLException, ClassNotFoundException {
        MostruarioDAO.getInstance().delete(mostruario);
        setChanged();
        notifyObservers();
    }  
    
    public List<PagamentoMostruario> listarFormaPgto(Mostruario mostruario) throws SQLException, ClassNotFoundException, Exception{
        return MostruarioDAO.getInstance().listarFormaPgto(mostruario);
    }
    
    public void atualizarpagamento(Mostruario mostruario) throws Exception, SQLException, ClassNotFoundException {
        validarPgtoFechamento(mostruario.getPagamento());
        MostruarioDAO.getInstance().atualizarpagamento(mostruario);
        setChanged();
        notifyObservers();        
    }
    
    
    public void editarMostruario(Mostruario mostruario, 
                                List<Produto> produtosVendidos,
                                List<Produto> produtosDevolvidos) 
            throws Exception, SQLException, ClassNotFoundException{     
        
        MostruarioDAO.getInstance().editarMostruario(mostruario, produtosVendidos, produtosDevolvidos);
        setChanged();
        notifyObservers();    
    }
}
