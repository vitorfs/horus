/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import model.ContatoFornecedor;
import model.Fornecedor;
import persistence.FornecedorDAO;

/**
 *
 * @author Vitor
 */
public class ManterFornecedorController extends Observable{
    
    private ManterFornecedorController() {
    }
    
    public static ManterFornecedorController getInstance() {
        return ManterFornecedorControllerHolder.INSTANCE;
    }
    
    private static class ManterFornecedorControllerHolder {

        private static final ManterFornecedorController INSTANCE = new ManterFornecedorController();
    }
           
    public void save(Fornecedor fornecedor) throws Exception, SQLException, ClassNotFoundException{       
        fornecedor.setNomeFantasia(Controller.ajustaAtributo(fornecedor.getNomeFantasia()));
        fornecedor.setRazaoSocial(Controller.ajustaAtributo(fornecedor.getRazaoSocial()));
        fornecedor.setCnpj(Controller.ajustaAtributo(fornecedor.getCnpj(),"['./-]"));
        fornecedor.setTelefone1(Controller.ajustaAtributo(fornecedor.getTelefone1(),"[' ()-]"));
        fornecedor.setTelefone2(Controller.ajustaAtributo(fornecedor.getTelefone2(),"[' ()-]"));
        fornecedor.setSite(Controller.ajustaAtributo(fornecedor.getSite()));
        fornecedor.setLogradouro(Controller.ajustaAtributo(fornecedor.getLogradouro()));
        fornecedor.setNumero(Controller.ajustaAtributo(fornecedor.getNumero()));
        fornecedor.setComplemento(Controller.ajustaAtributo(fornecedor.getComplemento()));
        fornecedor.setBairro(Controller.ajustaAtributo(fornecedor.getBairro()));
        fornecedor.setMunicipio(Controller.ajustaAtributo(fornecedor.getMunicipio()));
        fornecedor.setUf(Controller.ajustaAtributo(fornecedor.getUf()));
        fornecedor.setCep(Controller.ajustaAtributo(fornecedor.getCep(),"['-]"));
        
        String mensagem = "";
        
        if (fornecedor.getNomeFantasia()==null)
            throw new Exception("Informe o nome da empresa!");
        
        if (FornecedorDAO.getInstance().getFornecedor(fornecedor.getId())==null){
            FornecedorDAO.getInstance().insert(fornecedor);
            mensagem = "Fornecedor incluído com sucesso!";
            setChanged();
        }
        else{
            if (fornecedor.getIcAtivo()==0){
                if (FornecedorDAO.getInstance().possuiProdutosAtivos(fornecedor))
                    throw new Exception("Não é possível desativar um fornecedor com produtos ativos.");
            }
            FornecedorDAO.getInstance().update(fornecedor);
            mensagem = "Fornecedor alterado com sucesso!";
            setChanged();
        }
        notifyObservers(mensagem);
    }
    
    public Integer getNextId() {
        return FornecedorDAO.getInstance().getNextId();
    }
    
    public List<Fornecedor> getFornecedores(boolean exibirTudo) throws SQLException, ClassNotFoundException{
        if (exibirTudo)
            return FornecedorDAO.getInstance().getFornecedores();
        else
            return FornecedorDAO.getInstance().getFornecedoresAtivos();
    }

    public List<Fornecedor> getFornecedoresAtivos() throws SQLException, ClassNotFoundException{
        return FornecedorDAO.getInstance().getFornecedoresAtivos();
    }
    
    public void delete(Fornecedor fornecedor) throws SQLException, ClassNotFoundException{
        FornecedorDAO.getInstance().delete(fornecedor);
        setChanged();
        notifyObservers();
    }
    
    public List<ContatoFornecedor> getContatosFornecedor(Fornecedor fornecedor) throws SQLException, ClassNotFoundException {
        return FornecedorDAO.getInstance().getContatosFornecedor(fornecedor);
    }
}
