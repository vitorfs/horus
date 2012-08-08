package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.TipoProduto;

/**
 * @author Vitor Freitas
 * contact vitorfs@gmail.com
 */
public final class TipoProdutoDAO extends DAO{
    private static final TipoProdutoDAO INSTANCE = new TipoProdutoDAO();
    
    private TipoProdutoDAO(){
    }
    
    public static TipoProdutoDAO getInstance(){
        return INSTANCE;
    }
    
    public void insert(TipoProduto tipoProduto) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("insert into t_tipo_produto (codigo, descricao) values (upper(?), ?)");
            st.setString(1, tipoProduto.getCodigo());
            st.setString(2, tipoProduto.getDescricao());
            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st);
            
        }
    }
    
    public void update(TipoProduto tipoProduto) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("update t_tipo_produto set descricao = ? where codigo = ?");
            st.setString(1, tipoProduto.getDescricao());
            st.setString(2, tipoProduto.getCodigo());
            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st);
        }
    }
    
    public void delete(TipoProduto tipoProduto) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("delete from t_tipo_produto where codigo = ?");
            st.setString(1, tipoProduto.getCodigo());
            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st);
        }
    }
    
    public TipoProduto getTipoProduto(String codigo) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select * from t_tipo_produto where codigo = ?");
            st.setString(1, codigo);
            rs = st.executeQuery();
            
            if (rs.first()){
                TipoProduto tipoProduto = new TipoProduto();        
                tipoProduto.setCodigo(rs.getString("codigo"));
                tipoProduto.setDescricao(rs.getString("descricao"));                
                return tipoProduto;
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
    
    public List<TipoProduto> getTipoProdutos() throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select * from t_tipo_produto");
            rs = st.executeQuery();
            List<TipoProduto> tipoProdutos = new ArrayList<TipoProduto>();
            
            while (rs.next()){
                TipoProduto tipoProduto = new TipoProduto();
                tipoProduto.setCodigo(rs.getString("codigo"));
                tipoProduto.setDescricao(rs.getString("descricao"));
                tipoProdutos.add(tipoProduto);
            }
            
            return tipoProdutos;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }    
    
}
