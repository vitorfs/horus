/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ContatoFornecedor;
import model.Fornecedor;

/**
 *
 * @author Vitor
 */
public final class FornecedorDAO extends DAO{
    
    private FornecedorDAO() {
    }
    
    public static FornecedorDAO getInstance() {
        return FornecedorDAOHolder.INSTANCE;
    }
    
    private static class FornecedorDAOHolder {

        private static final FornecedorDAO INSTANCE = new FornecedorDAO();
    }
    
    public void insert(Fornecedor fornecedor) throws Exception, SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            
            st = conn.prepareStatement("insert into t_fornecedor (id, nome_fantasia, razao_social, cnpj, telefone1, telefone2, site, logradouro, numero, complemento, bairro, municipio, uf, cep, ic_ativo) "
                                                     + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            setParam(st, 1, fornecedor.getId());
            setParam(st, 2, fornecedor.getNomeFantasia());
            setParam(st, 3, fornecedor.getRazaoSocial());
            setParam(st, 4, fornecedor.getCnpj());
            setParam(st, 5, fornecedor.getTelefone1());
            setParam(st, 6, fornecedor.getTelefone2());
            setParam(st, 7, fornecedor.getSite());
            setParam(st, 8, fornecedor.getLogradouro());
            setParam(st, 9, fornecedor.getNumero());
            setParam(st, 10, fornecedor.getComplemento());
            setParam(st, 11, fornecedor.getBairro());
            setParam(st, 12, fornecedor.getMunicipio());
            setParam(st, 13, fornecedor.getUf());
            setParam(st, 14, fornecedor.getCep());
            st.setInt(15, fornecedor.getIcAtivo());
            st.execute();
            
            for (ContatoFornecedor c : fornecedor.getContatoFornecedor()){
                st = conn.prepareStatement("INSERT INTO `horus`.`t_contato_fornecedor` "
                                            + "(`nome`, "
                                            + "`cpf`, "
                                            + "`email`, "
                                            + "`telefone1`, "
                                            + "`telefone2`, "
                                            + "`id_fornecedor`) "
                                            + " VALUES "
                                            + " (?,?,?,?,?,?) ");
                st.setString(1, c.getNome());
                st.setString(2, c.getCpf());
                st.setString(3, c.getEmail());
                st.setString(4, c.getTelefone1());
                st.setString(5, c.getTelefone2());
                st.setInt(6, c.getFornecedor().getId());
                st.execute();
            }
            
            conn.commit();
        } catch(Exception e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st);
            
        }
    }
    
    public void update(Fornecedor fornecedor) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement("update t_fornecedor set nome_fantasia = ?, "
                    + "razao_social = ?, "
                    + "cnpj = ?, "
                    + "telefone1 = ?, "
                    + "telefone2 = ?, "
                    + "site = ?, "
                    + "logradouro = ?, "
                    + "numero = ?, "
                    + "complemento = ?, "
                    + "bairro = ?, "
                    + "municipio = ?, "
                    + "uf = ?, "
                    + "cep = ?, "
                    + "ic_ativo = ? "
                    + "where id = ?");
            
            setParam(st, 1, fornecedor.getNomeFantasia());
            setParam(st, 2, fornecedor.getRazaoSocial());
            setParam(st, 3, fornecedor.getCnpj());
            setParam(st, 4, fornecedor.getTelefone1());
            setParam(st, 5, fornecedor.getTelefone2());
            setParam(st, 6, fornecedor.getSite());
            setParam(st, 7, fornecedor.getLogradouro());
            setParam(st, 8, fornecedor.getNumero());
            setParam(st, 9, fornecedor.getComplemento());
            setParam(st, 10, fornecedor.getBairro());
            setParam(st, 11, fornecedor.getMunicipio());
            setParam(st, 12, fornecedor.getUf());
            setParam(st, 13, fornecedor.getCep());
            st.setInt(14, fornecedor.getIcAtivo());
            st.setInt(15, fornecedor.getId());
            
            st.execute();
            
            st = conn.prepareStatement("delete from t_contato_fornecedor where id_fornecedor = "+fornecedor.getId());
            st.execute();
            
            for (ContatoFornecedor c : fornecedor.getContatoFornecedor()){
                st = conn.prepareStatement("INSERT INTO `horus`.`t_contato_fornecedor` "
                                            + "(`nome`, "
                                            + "`cpf`, "
                                            + "`email`, "
                                            + "`telefone1`, "
                                            + "`telefone2`, "
                                            + "`id_fornecedor`) "
                                            + " VALUES "
                                            + " (?,?,?,?,?,?) ");
                st.setString(1, c.getNome());
                st.setString(2, c.getCpf());
                st.setString(3, c.getEmail());
                st.setString(4, c.getTelefone1());
                st.setString(5, c.getTelefone2());
                st.setInt(6, c.getFornecedor().getId());
                st.execute();
            }
            
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st, null);   
        }
    }
    
    public void delete(Fornecedor fornecedor) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("delete from t_fornecedor where id = ?");
            st.setInt(1, fornecedor.getId());
            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, null);   
        }
         
    }
    
    public Integer getNextId() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select max(id)+1 as next_value from t_fornecedor");
            rs = st.executeQuery();
            rs.first();
            
            Integer id = rs.getInt("next_value");
            
            id = (id==0?1:id);
            
            return id;
            
        } catch (SQLException e) {
            return 0;
        } catch (ClassNotFoundException e){
            return 0;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    public Fornecedor getFornecedor(Integer id) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select * from t_fornecedor where id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.first()){
                Fornecedor fornecedor = new Fornecedor();        
                fornecedor.setId(rs.getInt("id"));
                fornecedor.setNomeFantasia(rs.getString("nome_fantasia"));
                fornecedor.setRazaoSocial(rs.getString("razao_social"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setTelefone1(rs.getString("telefone1"));
                fornecedor.setTelefone2(rs.getString("telefone2"));
                fornecedor.setSite(rs.getString("site"));
                fornecedor.setLogradouro(rs.getString("logradouro"));
                fornecedor.setNumero(rs.getString("numero"));
                fornecedor.setComplemento(rs.getString("complemento"));
                fornecedor.setBairro(rs.getString("bairro"));
                fornecedor.setMunicipio(rs.getString("municipio"));
                fornecedor.setUf(rs.getString("uf"));
                fornecedor.setCep(rs.getString("cep"));
                fornecedor.setIcAtivo(rs.getInt("ic_ativo"));
                fornecedor.setContatoFornecedor(null);
                return fornecedor;
            }
            else {
                return null;
            }
            
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    private List<Fornecedor> getListFornecedores(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
            
            while (rs.next()){
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(rs.getInt("id"));
                fornecedor.setNomeFantasia(rs.getString("nome_fantasia"));
                fornecedor.setRazaoSocial(rs.getString("razao_social"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setTelefone1(rs.getString("telefone1"));
                fornecedor.setTelefone2(rs.getString("telefone2"));
                fornecedor.setSite(rs.getString("site"));
                fornecedor.setLogradouro(rs.getString("logradouro"));
                fornecedor.setNumero(rs.getString("numero"));
                fornecedor.setComplemento(rs.getString("complemento"));
                fornecedor.setBairro(rs.getString("bairro"));
                fornecedor.setMunicipio(rs.getString("municipio"));
                fornecedor.setUf(rs.getString("uf"));
                fornecedor.setCep(rs.getString("cep"));
                fornecedor.setIcAtivo(rs.getInt("ic_ativo"));
                fornecedor.setContatoFornecedor(null);
                fornecedores.add(fornecedor);
            }
            return fornecedores;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    public List<Fornecedor> getFornecedores() throws SQLException, ClassNotFoundException{
        return getListFornecedores("SELECT * FROM T_FORNECEDOR");
    }
    
    public List<Fornecedor> getFornecedoresAtivos() throws SQLException, ClassNotFoundException{
        return getListFornecedores("SELECT * FROM T_FORNECEDOR WHERE IC_ATIVO = 1");
    }    
    
    public boolean possuiProdutosAtivos(Fornecedor fornecedor) throws SQLException, ClassNotFoundException{
        String sql = "select count(*) as qtd from t_produto where id_fornecedor = "+fornecedor.getId()+" and ic_ativo = 1";
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            rs.first();
            
            int qtd = rs.getInt("qtd");
            
            if (qtd == 0)
                return false;
            else
                return true;
            
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e){
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    public List<ContatoFornecedor> getContatosFornecedor(Fornecedor fornecedor) throws SQLException, ClassNotFoundException{
        String sql = "select * from t_contato_fornecedor where id_fornecedor ="+fornecedor.getId();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<ContatoFornecedor> contatos = new ArrayList<ContatoFornecedor>();
            
            while (rs.next()){
                ContatoFornecedor c = new ContatoFornecedor();
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setEmail(rs.getString("email"));
                c.setTelefone1(rs.getString("telefone1"));
                c.setTelefone2(rs.getString("telefone2"));
                c.setFornecedor(fornecedor);
                contatos.add(c);
            }
            return contatos;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }        
    }
            
}
