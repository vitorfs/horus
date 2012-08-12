package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Mostruario;
import model.PagamentoMostruario;
import model.Revendedora;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class FinanceiroDAO extends DAO {
    private static final FinanceiroDAO INSTANCE = new FinanceiroDAO();
    
    private FinanceiroDAO(){
        
    }
    
    public static FinanceiroDAO getInstance(){
        return INSTANCE;
    }    
    
    
    public List<PagamentoMostruario> listarPagamentosEmAberto() throws SQLException, ClassNotFoundException, Exception{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "select date_format(a.data_prev_pgto, '%d/%m/%Y') as data_prev_pgto, "
                    +"a.vl_pgto, "
                   + "b.id as id_mostruario, "
                   + "c.id as id_revendedora, "
                   + "c.nome "
                +"from t_pgto_mostruario a, " 
                    +"t_mostruario b, "
                    +"t_revendedora c "
                +"where a.id_mostruario = b.id "
                    +"and b.id_revendedora = c.id "
                    +"and a.data_rlzd_pgto is null "
               + "order by a.data_prev_pgto";
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<PagamentoMostruario> listaPgto = new ArrayList<PagamentoMostruario>();
            
            while (rs.next()){
                PagamentoMostruario pgto = new PagamentoMostruario();
                pgto.setValorPgto(rs.getFloat("vl_pgto"));
                pgto.setDataPrevisao(rs.getString("data_prev_pgto"));
                Mostruario m = new Mostruario();
                m.setId(rs.getInt("id_mostruario"));
                Revendedora r = new Revendedora();
                r.setId(rs.getInt("id_revendedora"));
                r.setNome(rs.getString("nome"));
                m.setRevendedora(r);
                pgto.setMostruario(m);
                listaPgto.add(pgto);
            }
            
            return listaPgto;
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }    
    
}
