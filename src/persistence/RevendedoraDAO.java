package persistence;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Revendedora;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class RevendedoraDAO extends DAO {
    private static final RevendedoraDAO INSTANCE = new RevendedoraDAO();
    
    private RevendedoraDAO(){
        
    }
    
    public static RevendedoraDAO getInstance(){
        return INSTANCE;
    }
    
    public void insert(Revendedora revendedora) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("insert into t_revendedora (nome, cpf, data_nascimento, email, telefone1, telefone2, comissao, ic_ativo, logradouro, numero, complemento, bairro, municipio, uf, cep) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            setParam(st, 1, revendedora.getNome());
            setParam(st, 2, revendedora.getCpf());
            setParam(st, 3, revendedora.getDataNascimento());
            setParam(st, 4, revendedora.getEmail());
            setParam(st, 5, revendedora.getTelefone1());
            setParam(st, 6, revendedora.getTelefone2());
            setParam(st, 7, revendedora.getComissao());
            setParam(st, 8, revendedora.getIcAtivo());
            setParam(st, 9, revendedora.getLogradouro());
            setParam(st, 10, revendedora.getNumero());
            setParam(st, 11, revendedora.getComplemento());
            setParam(st, 12, revendedora.getBairro());
            setParam(st, 13, revendedora.getMunicipio());
            setParam(st, 14, revendedora.getUf());
            setParam(st, 15, revendedora.getCep());
            st.execute();
        } catch (SQLException e){
            throw e;
        } finally {
            closeResources(conn, st);
        }
    }
    
    public void update(Revendedora revendedora) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;        
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("update t_revendedora set nome = ?,"
                    + " cpf = ?,"
                    + " data_nascimento = ?,"
                    + " email = ?,"
                    + " telefone1 = ?,"
                    + " telefone2 = ?,"
                    + " comissao = ?,"
                    + " ic_ativo = ?,"
                    + " logradouro = ?,"
                    + " numero = ?,"
                    + " complemento = ?,"
                    + " bairro = ?,"
                    + " municipio = ?,"
                    + " uf = ?,"
                    + " cep = ?"
                    + " where id = ?");
            
            setParam(st, 1, revendedora.getNome());
            setParam(st, 2, revendedora.getCpf());
            setParam(st, 3, revendedora.getDataNascimento());
            setParam(st, 4, revendedora.getEmail());
            setParam(st, 5, revendedora.getTelefone1());
            setParam(st, 6, revendedora.getTelefone2());
            setParam(st, 7, revendedora.getComissao());
            setParam(st, 8, revendedora.getIcAtivo());
            setParam(st, 9, revendedora.getLogradouro());
            setParam(st, 10, revendedora.getNumero());
            setParam(st, 11, revendedora.getComplemento());
            setParam(st, 12, revendedora.getBairro());
            setParam(st, 13, revendedora.getMunicipio());
            setParam(st, 14, revendedora.getUf());
            setParam(st, 15, revendedora.getCep());
            setParam(st, 16, revendedora.getId());
            
            st.execute();
            
        } catch (SQLException e){
            throw e;
        } finally {
            closeResources(conn, st);
        }        
    }
    
    public void delete(Revendedora revendedora) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;        

        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("delete from t_revendedora where id = ?");
            st.setInt(1, revendedora.getId());
            st.execute();            
        } catch (SQLException e){
            throw e;
        } finally {
            closeResources(conn, st);
        }        
    }
    
    public Revendedora getRevendedora(Integer id) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select * from t_revendedora where id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                Revendedora revendedora = new Revendedora();
                revendedora.setId(rs.getInt("id"));
                revendedora.setNome(rs.getString("nome"));
                revendedora.setCpf(rs.getString("cpf"));
                revendedora.setDataNascimento(rs.getString("data_nascimento"));
                revendedora.setEmail(rs.getString("email"));
                revendedora.setTelefone1(rs.getString("telefone1"));
                revendedora.setTelefone2(rs.getString("telefone2"));
                revendedora.setComissao(rs.getFloat("comissao"));
                revendedora.setIcAtivo(rs.getInt("ic_ativo"));
                revendedora.setLogradouro(rs.getString("logradouro"));
                revendedora.setNumero(rs.getString("numero"));
                revendedora.setComplemento(rs.getString("complemento"));
                revendedora.setBairro(rs.getString("bairro"));
                revendedora.setMunicipio(rs.getString("municipio"));
                revendedora.setUf(rs.getString("uf"));
                revendedora.setCep(rs.getString("cep"));
                return revendedora;
            } else
                return null;
            
        } catch (SQLException e){
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
        
    }
    
    private List<Revendedora> getListRevendedora(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<Revendedora> revendedoras = new ArrayList<Revendedora>();
            
            while (rs.next()) {
                Revendedora revendedora = new Revendedora();
                revendedora.setId(rs.getInt("id"));
                revendedora.setNome(rs.getString("nome"));
                revendedora.setCpf(rs.getString("cpf"));
                revendedora.setDataNascimento(rs.getString("data_nascimento"));
                revendedora.setEmail(rs.getString("email"));
                revendedora.setTelefone1(rs.getString("telefone1"));
                revendedora.setTelefone2(rs.getString("telefone2"));
                revendedora.setComissao(rs.getFloat("comissao"));
                revendedora.setIcAtivo(rs.getInt("ic_ativo"));
                revendedora.setLogradouro(rs.getString("logradouro"));
                revendedora.setNumero(rs.getString("numero"));
                revendedora.setComplemento(rs.getString("complemento"));
                revendedora.setBairro(rs.getString("bairro"));
                revendedora.setMunicipio(rs.getString("municipio"));
                revendedora.setUf(rs.getString("uf"));
                revendedora.setCep(rs.getString("cep"));
                revendedoras.add(revendedora);
            }
            return revendedoras;
        } catch (SQLException e){
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    public List<Revendedora> getRevendedoras() throws SQLException, ClassNotFoundException {
        return getListRevendedora("select * from t_revendedora");
    }
    
    public List<Revendedora> getRevendedorasAtivo() throws SQLException, ClassNotFoundException {
        return getListRevendedora("select * from t_revendedora where ic_ativo = 1");
    }
    
    public boolean possuiMostruarioConsignado(Revendedora revendedora) throws SQLException, ClassNotFoundException {
        String sql = "select count(*) as qtd from t_mostruario where ic_status = 0 and id_revendedora = "+revendedora.getId();
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
}
