package controller;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import model.Revendedora;
import persistence.RevendedoraDAO;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class ManterRevendedoraController extends Observable{
    private static final ManterRevendedoraController INSTANCE = new ManterRevendedoraController();
    
    private ManterRevendedoraController(){
        
    }
    
    public static ManterRevendedoraController getInstance(){
        return INSTANCE;
    }
    
    
    public Revendedora ajustaAtributosRevendedora(Revendedora revendedora){
        revendedora.setNome(Controller.ajustaAtributo(revendedora.getNome()));
        revendedora.setCpf(Controller.ajustaAtributo(revendedora.getCpf()));
        revendedora.setEmail(Controller.ajustaAtributo(revendedora.getEmail()));
        revendedora.setTelefone1(Controller.ajustaAtributo(revendedora.getTelefone1(),"[' ()-]"));
        revendedora.setTelefone2(Controller.ajustaAtributo(revendedora.getTelefone2(),"[' ()-]"));
        revendedora.setLogradouro(Controller.ajustaAtributo(revendedora.getLogradouro()));
        revendedora.setNumero(Controller.ajustaAtributo(revendedora.getNumero()));
        revendedora.setComplemento(Controller.ajustaAtributo(revendedora.getComplemento()));
        revendedora.setBairro(Controller.ajustaAtributo(revendedora.getBairro()));
        revendedora.setMunicipio(Controller.ajustaAtributo(revendedora.getMunicipio()));
        revendedora.setUf(Controller.ajustaAtributo(revendedora.getUf()));
        revendedora.setCep(Controller.ajustaAtributo(revendedora.getCep()));
        return revendedora;
    }
    
    public void save(Revendedora revendedora) throws SQLException, ClassNotFoundException, Exception{
        revendedora = ajustaAtributosRevendedora(revendedora);
        String mensagem="";

        if (revendedora.getNome()==null)
            throw new Exception("Informe o nome da revendedora!");
        if (revendedora.getComissao()==null)
            throw new Exception("Informe o valor da comissão da revendedora!");
        
        if (revendedora.getId()==null||revendedora.getId()==0)
            RevendedoraDAO.getInstance().insert(revendedora);
        else{
            if (revendedora.getIcAtivo()==0){
                if (!RevendedoraDAO.getInstance().possuiMostruarioConsignado(revendedora))
                    RevendedoraDAO.getInstance().update(revendedora);
                else
                    throw new Exception("Não é possível desativar uma revendedora com mostruário em aberto.");
            } else
                RevendedoraDAO.getInstance().update(revendedora);
        }
        
        setChanged();
        notifyObservers();
    }
    
    public List<Revendedora> getRevendedoras(boolean exibirTudo) throws SQLException, ClassNotFoundException{
        if (exibirTudo){
            return RevendedoraDAO.getInstance().getRevendedoras();
        }
        else
            return RevendedoraDAO.getInstance().getRevendedorasAtivo();
    }    

    public List<Revendedora> getRevendedorasAtivas() throws SQLException, ClassNotFoundException{
        return RevendedoraDAO.getInstance().getRevendedorasAtivo();
    }    
    
    public void delete(Revendedora revendedora) throws SQLException, ClassNotFoundException{
        RevendedoraDAO.getInstance().delete(revendedora);
        setChanged();
        notifyObservers();
    }
    
}
