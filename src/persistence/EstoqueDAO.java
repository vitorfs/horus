package persistence;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.LogEstoque;
import model.MotivoMovimentoEstoque;
import model.Produto;
import model.ProdutoConsignado;
import model.Revendedora;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class EstoqueDAO extends DAO{
    private static final EstoqueDAO INSTANCE = new EstoqueDAO();
    
    private EstoqueDAO(){
        
    }
    
    public static EstoqueDAO getInstance(){
        return INSTANCE;
    }
    
    public void movimentarEstoque(String codigo, int qtdEstoque) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement("update horus_produto set qtd_estoque = (qtd_estoque + ?) where codigo = ?");
            st.setInt(1, qtdEstoque);
            st.setString(2, codigo);
            st.execute();
            
            String fluxo;
            Integer idMotivo;
            Integer qtdProduto;
            
            if (qtdEstoque > 0){
                fluxo = "E";
                idMotivo = 1;
                qtdProduto = qtdEstoque;
            } else {
                fluxo = "S";
                idMotivo = 3;
                qtdProduto = qtdEstoque * -1;
            }
            
            st = conn.prepareStatement("INSERT INTO `horus`.`t_log_estoque` "
                    + "(`fluxo`, "
                    + "`id_motivo`, "
                    + "`id_produto`, "
                    + "`dt_movimentacao`, "
                    + "`qtd_produto`) "
                    + "VALUES "
                    + "(?, ?"
                    + ", (select id from horus_produto where codigo = '"+codigo+"')"
                    + ", now()"
                    + ", ?)");

            st.setString(1, fluxo);
            st.setInt(2, idMotivo);
            st.setInt(3, qtdProduto);

            st.execute();

            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st);
        }
    }    
    
    
    public List<MotivoMovimentoEstoque> listarMotivoMovimentoEstoque() throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select * from t_motivo_movimento_estoque where ic_ativo = 1 and upper(descricao) not in ('ENTRADA', 'VENDA')");
            rs = st.executeQuery();
            List<MotivoMovimentoEstoque> motivos = new ArrayList<MotivoMovimentoEstoque>();
            
            while (rs.next()){
                MotivoMovimentoEstoque motivo = new MotivoMovimentoEstoque();
                motivo.setId(rs.getInt("id"));
                motivo.setDescricao(rs.getString("descricao"));
                motivo.setIcAtivo(rs.getInt("ic_ativo"));
                motivos.add(motivo);
            }
            
            return motivos;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }    
    
    
    public List<Produto> getProdutosDisponiveisEstoque() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * "
                        +"FROM HORUS_PRODUTO P "
                        +"INNER JOIN T_FORNECEDOR F ON P.ID_FORNECEDOR = F.ID "
                        +"INNER JOIN T_TIPO_PRODUTO T ON P.CODIGO_TIPO_PRODUTO = T.CODIGO "
                        +"WHERE P.QTD_ESTOQUE > 0 "
                        +"ORDER BY P.QTD_ESTOQUE DESC";
        
        return ProdutoDAO.getInstance().getListProdutos(sql);
    }
    
    
    public Produto getProdutoCodFornecedor(String cod) throws Exception, SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("SELECT * FROM HORUS_PRODUTO WHERE codigo_origem_fornecedor = ? AND IC_ATIVO = 1");
            st.setString(1, cod);
            rs = st.executeQuery();
            
            if (rs.first()){
                Produto produto = new Produto();
                produto.setCodigo(rs.getString("codigo"));
                produto.setId(rs.getInt("id"));
                produto.setFornecedor(FornecedorDAO.getInstance().getFornecedor(rs.getInt("id_fornecedor")));
                produto.setTipoProduto(TipoProdutoDAO.getInstance().getTipoProduto(rs.getString("codigo_tipo_produto")));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setValorEntrada(rs.getFloat("valor_entrada"));
                produto.setValorSaida(rs.getFloat("valor_saida"));
                produto.setCodItemForn(rs.getString("codigo_origem_fornecedor"));
                produto.setIcAtivo(rs.getInt("ic_ativo"));
                produto.setQtdEstoque(rs.getInt("qtd_estoque"));
                produto.setQtdConsignado(rs.getInt("qtd_consig"));
                produto.setQtdTotal(rs.getInt("qtd_total"));

                if (produto.getIcAtivo()==0){
                    throw new Exception("Este produto foi desativado!");
                }
                
                return produto;
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
    
    public List<ProdutoConsignado> consultarProdutoConsignado(Produto produto) throws Exception, SQLException, ClassNotFoundException{
        String sql = "select a.id_produto, "
                        +" d.codigo, "
                        +" d.nome, "
                        +" a.vl_produto, "
                        //+" b.data_retirada, "
                        +"date_format(b.data_retirada, '%d/%m/%Y') as data_retirada, "
                        +" c.nome, "
                        +" count(1) as qtd "
                     +" from t_produto_mostruario a  "
                     +" inner join t_mostruario b on a.id_mostruario = b.id "
                     +" inner join t_revendedora c on b.id_revendedora = c.id "
                     +" inner join horus_produto d on d.id = a.id_produto "
                    +" where d.id = '"+produto.getId()+"' "
                     +" and b.ic_status = 0 "
                    +" group by a.id_produto, d.codigo, d.nome, a.vl_produto, c.nome";
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<ProdutoConsignado> produtos = new ArrayList<ProdutoConsignado>();
            
            while (rs.next()){
                ProdutoConsignado p = new ProdutoConsignado();
                p.setId(rs.getInt("a.id_produto"));
                p.setCodigo(rs.getString("d.codigo"));
                p.setNome(rs.getString("d.nome"));
                p.setValorSaida(rs.getFloat("a.vl_produto"));
                p.setDataRetirada(rs.getString("data_retirada"));
                Revendedora revendedora = new Revendedora();
                revendedora.setNome(rs.getString("c.nome"));
                p.setRevendedora(revendedora);
                p.setQuantidade(rs.getInt("qtd"));
                produtos.add(p);
            }
            return produtos;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    public Produto getProdutoEstoque(String cod) throws Exception, SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("SELECT * FROM HORUS_PRODUTO WHERE CODIGO = ?");
            st.setString(1, cod);
            rs = st.executeQuery();
            
            if (rs.first()){
                Produto produto = new Produto();
                produto.setCodigo(rs.getString("codigo"));
                produto.setId(rs.getInt("id"));
                produto.setFornecedor(FornecedorDAO.getInstance().getFornecedor(rs.getInt("id_fornecedor")));
                produto.setTipoProduto(TipoProdutoDAO.getInstance().getTipoProduto(rs.getString("codigo_tipo_produto")));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setValorEntrada(rs.getFloat("valor_entrada"));
                produto.setValorSaida(rs.getFloat("valor_saida"));
                produto.setCodItemForn(rs.getString("codigo_origem_fornecedor"));
                produto.setIcAtivo(rs.getInt("ic_ativo"));
                produto.setQtdEstoque(rs.getInt("qtd_estoque"));
                produto.setQtdConsignado(rs.getInt("qtd_consig"));
                produto.setQtdTotal(rs.getInt("qtd_total"));
                
                if (produto.getIcAtivo()==0){
                    throw new Exception("Este produto foi desativado!");
                }
                return produto;
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
    
    
    
    public List<Produto> getProdutosEstoque() throws SQLException, ClassNotFoundException{
        String sql = "SELECT "+
                            "* "+
                        "FROM "+
                            "`HORUS_PRODUTO` `P` "+
                                "INNER JOIN "+
                            "`T_FORNECEDOR` `F` ON `P`.`ID_FORNECEDOR` = `F`.`ID` "+
                                "INNER JOIN "+
                            "`T_TIPO_PRODUTO` `T` ON `P`.`CODIGO_TIPO_PRODUTO` = `T`.`CODIGO` "+
                        "WHERE "+
                            "`P`.`QTD_TOTAL` <> 0 AND `P`.`IC_ATIVO` = 1 ORDER BY `P`.`codigo_tipo_produto`, `P`.`nome`";
        
        return ProdutoDAO.getInstance().getListProdutos(sql);
    }
    
    
    public List<Produto> getProdutosEstoque(String order) throws SQLException, ClassNotFoundException{
        String sql = "SELECT "+
                            "* "+
                        "FROM "+
                            "`HORUS_PRODUTO` `P` "+
                                "INNER JOIN "+
                            "`T_FORNECEDOR` `F` ON `P`.`ID_FORNECEDOR` = `F`.`ID` "+
                                "INNER JOIN "+
                            "`T_TIPO_PRODUTO` `T` ON `P`.`CODIGO_TIPO_PRODUTO` = `T`.`CODIGO` "+
                        "WHERE "+
                            "`P`.`QTD_TOTAL` <> 0 AND `P`.`IC_ATIVO` = 1 ORDER BY " + order;
        
        return ProdutoDAO.getInstance().getListProdutos(sql);
    }    
    
    public List<LogEstoque> getHistoricoEstoque() throws Exception, SQLException, ClassNotFoundException {
        String sql = "select case a.fluxo "
                    + "when 'E' then 'Entrada' "
                    + "when 'S' then 'Saída' "
                    + "end fluxo, "
                    + "b.codigo, "
                    + "c.descricao, "
                    + "a.qtd_produto, "
                    + "date_format(a.dt_movimentacao, '%d/%m/%Y %r') as dt_movimentacao "
                    + "from t_log_estoque a inner join horus_produto b on a.id_produto = b.id "
                    + "inner join t_motivo_movimento_estoque c on a.id_motivo = c.id "
                    + "order by a.dt_movimentacao desc";
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            List<LogEstoque> logs = new ArrayList<LogEstoque>();
            
            while (rs.next()){
                LogEstoque log = new LogEstoque();
                log.setFluxo(rs.getString("fluxo"));
                log.setIdProduto(rs.getString("b.codigo"));
                log.setMotivo(rs.getString("c.descricao"));
                log.setQtdProduto(rs.getInt("qtd_produto"));
                log.setDataMovimentacao(rs.getString("dt_movimentacao"));
                
                logs.add(log);
            }
            
            return logs;
        } catch (Exception e){
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
}
