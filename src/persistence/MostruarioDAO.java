package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Fornecedor;
import model.Mostruario;
import model.PagamentoMostruario;
import model.Produto;
import model.Revendedora;
import model.TipoProduto;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class MostruarioDAO extends DAO{
    private static final MostruarioDAO INSTANCE = new MostruarioDAO();
    
    private MostruarioDAO(){
        
    }
    
    public static MostruarioDAO getInstance(){
        return INSTANCE;
    }
        
    public void insert(Mostruario mostruario) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        Statement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
                        
            
            st = conn.createStatement();
            st.execute("insert into t_mostruario (id_revendedora, data_retirada, data_acerto) values ("+mostruario.getRevendedora().getId()+", STR_TO_DATE('"+mostruario.getDataRetirada()+"', '%d/%m/%Y'), STR_TO_DATE('"+mostruario.getDataAcerto()+"', '%d/%m/%Y'))", 
                    Statement.RETURN_GENERATED_KEYS);                        

            ResultSet keys = st.getGeneratedKeys();
            int id = 1;
            
            while (keys.next()) {
                id = keys.getInt(1);
            }
            
            for (Produto produto : mostruario.getProdutos()){
                st.execute("insert into t_produto_mostruario (id_mostruario, id_produto, vl_produto, data_inclusao) values ("+id+", "+produto.getId()+","+produto.getValorSaida()+",NOW())");
                st.execute("update t_produto set qtd_estoque = (qtd_estoque - 1), qtd_consig = (qtd_consig + 1) where id = "+produto.getId());
            }
            
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st);
        }        
    }
    
    public void update(Mostruario mostruario) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        Statement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
                        
            
            st = conn.createStatement();
            st.execute("update t_mostruario "
                    + "set id_revendedora = "+mostruario.getRevendedora().getId()
                    + ", data_retirada = STR_TO_DATE('"+mostruario.getDataRetirada()+"', '%d/%m/%Y') "
                    + ", data_acerto = STR_TO_DATE('"+mostruario.getDataAcerto()+"', '%d/%m/%Y') "
                    + " where id = "+mostruario.getId());

            st.execute("update t_produto a "
                        +"inner join (select b.id_produto, " 
                                    +"count(*) as qtd "
                                    +"from t_produto_mostruario b "
                                    +"where b.id_mostruario = "+mostruario.getId()
                                    +" group by b.id_produto) c "
                        +"on a.id = c.id_produto "
                        +"set a.qtd_estoque = (a.qtd_estoque + c.qtd), "
                        +"a.qtd_consig = (a.qtd_consig - c.qtd)");
            
            st.execute("delete from t_produto_mostruario where id_mostruario ="+mostruario.getId());
            
            for (Produto produto : mostruario.getProdutos()){
                String data;
                if (produto.getData()!=null){
                    data = " STR_TO_DATE('"+produto.getData()+"', '%d/%m/%Y') ";
                } else {
                    data = " NULL ";
                }
                st.execute("insert into t_produto_mostruario (id_mostruario, id_produto, vl_produto, data_inclusao) values ("+mostruario.getId()+", "+produto.getId()+","+produto.getValorSaida()+","+data+")");
                st.execute("update t_produto set qtd_estoque = (qtd_estoque - 1), qtd_consig = (qtd_consig + 1) where id = "+produto.getId());
            }
            
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st);
        }                
    }
    
    public void delete(Mostruario mostruario) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        Statement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
                        
            
            st = conn.createStatement();

            st.execute("update t_produto a "
                        +"inner join (select b.id_produto, " 
                                    +"count(*) as qtd "
                                    +"from t_produto_mostruario b "
                                    +"where b.id_mostruario = "+mostruario.getId()
                                    +" group by b.id_produto) c "
                        +"on a.id = c.id_produto "
                        +"set a.qtd_estoque = (a.qtd_estoque + c.qtd), "
                        +"a.qtd_consig = (a.qtd_consig - c.qtd)");
            
            st.execute("delete from t_produto_mostruario where id_mostruario ="+mostruario.getId());
            
            st.execute("delete from t_mostruario where id = "+mostruario.getId());
            
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st);
        }         
    }
    
    public List<Mostruario> listarMostruarios(boolean exibirTudo) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql;
        sql = "select a.id, "
                +"a.id_revendedora, "
                +"date_format(a.data_retirada, '%d/%m/%Y') as data_retirada, "
                +"date_format(a.data_acerto, '%d/%m/%Y') as data_acerto, "
                +"date_format(a.data_fechamento, '%d/%m/%Y') as data_fechamento, "
                +"a.ic_status, "
                +"a.qtd_venda, "
                +"a.vl_total, "
                +"a.comissao, "
                +"b.*, "

                +"(select sum(c.vl_produto) from t_produto_mostruario c "
                + "where c.id_mostruario = a.id group by c.id_mostruario) "
                + "as valor_mostruario, "

                +"(select sum(d.vl_pgto) from t_pgto_mostruario d "
                + "where d.data_rlzd_pgto is null and d.id_mostruario = a.id "
                + "group by d.id_mostruario) "
                + "as valor_aberto,"
                
                +"(select count(*) "
                +"from t_produto_mostruario e "
                +"where e.id_mostruario = a.id "
                +"group by e.id_mostruario) as qtd_itens "
            +"from t_mostruario a "
            +"inner join t_revendedora b "
            +"on a.id_revendedora = b.id ";
        if (!exibirTudo)
            sql += " where a.ic_status <> 2";
        sql += " order by 1 desc";
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<Mostruario> mostruarios = new ArrayList<Mostruario>();
            
            while (rs.next()){
                Mostruario m = new Mostruario();
                Revendedora r = new Revendedora();
                
                m.setId(rs.getInt("id"));
                m.setDataAcerto(rs.getString("data_acerto"));
                m.setDataRetirada(rs.getString("data_retirada"));
                m.setDataFechamento(rs.getString("data_fechamento"));
                m.setValorTotal(rs.getFloat("valor_mostruario"));
                m.setQtdItens(rs.getInt("qtd_itens"));
                
                m.setStatus(rs.getInt("a.ic_status"));
                m.setQtdItensFechamento(rs.getInt("a.qtd_venda"));
                m.setValorFechamento(rs.getFloat("a.vl_total"));
                m.setPercentualComissaoFechamento(rs.getFloat("a.comissao"));
                m.setValorEmAberto(rs.getFloat("valor_aberto"));
                
                r.setId(rs.getInt("b.id"));
                r.setNome(rs.getString("b.nome"));
                r.setCpf(rs.getString("b.cpf"));
                r.setDataNascimento(rs.getString("b.data_nascimento"));
                r.setEmail(rs.getString("b.email"));
                r.setTelefone1(rs.getString("b.telefone1"));
                r.setTelefone2(rs.getString("b.telefone2"));
                r.setComissao(rs.getFloat("b.comissao"));
                r.setIcAtivo(rs.getInt("b.ic_ativo"));
                r.setLogradouro(rs.getString("b.logradouro"));
                r.setNumero(rs.getString("b.numero"));
                r.setComplemento(rs.getString("b.complemento"));
                r.setBairro(rs.getString("b.bairro"));
                r.setMunicipio(rs.getString("b.municipio"));
                r.setUf(rs.getString("b.uf"));
                r.setCep(rs.getString("b.cep"));                
                
                m.setRevendedora(r);
                
                mostruarios.add(m);
            }
            
            return mostruarios;
            
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }        
    }
    
    public List<Produto> getListProdutos(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<Produto> produtos = new ArrayList<Produto>();
            
            while (rs.next()){
                Produto produto = new Produto();
                Fornecedor fornecedor = new Fornecedor();
                TipoProduto tipoProduto = new TipoProduto();
                
                produto.setCodigo(rs.getString("p.codigo"));
                produto.setId(rs.getInt("p.id"));
                produto.setNome(rs.getString("p.nome"));
                produto.setDescricao(rs.getString("p.descricao"));
                produto.setValorEntrada(rs.getFloat("p.valor_entrada"));
                produto.setValorSaida(rs.getFloat("m.vl_produto"));
                produto.setCodItemForn(rs.getString("p.codigo_origem_fornecedor"));
                produto.setIcAtivo(rs.getInt("p.ic_ativo"));
                produto.setQtdEstoque(rs.getInt("p.qtd_estoque"));
                produto.setQtdConsignado(rs.getInt("p.qtd_consig"));
                produto.setQtdTotal(rs.getInt("p.qtd_total"));
                
                produto.setIdProdutoMostruario(rs.getInt("m.id"));
                produto.setIcVendido(rs.getInt("m.ic_vendido"));
                produto.setData(rs.getString("dt_inclusao"));
                
                fornecedor.setId(rs.getInt("f.id"));
                fornecedor.setNomeFantasia(rs.getString("f.nome_fantasia"));
                fornecedor.setRazaoSocial(rs.getString("f.razao_social"));
                fornecedor.setCnpj(rs.getString("f.cnpj"));
                fornecedor.setTelefone1(rs.getString("f.telefone1"));
                fornecedor.setTelefone2(rs.getString("f.telefone2"));
                fornecedor.setSite(rs.getString("f.site"));
                fornecedor.setLogradouro(rs.getString("f.logradouro"));
                fornecedor.setNumero(rs.getString("f.numero"));
                fornecedor.setComplemento(rs.getString("f.complemento"));
                fornecedor.setBairro(rs.getString("f.bairro"));
                fornecedor.setMunicipio(rs.getString("f.municipio"));
                fornecedor.setUf(rs.getString("f.uf"));
                fornecedor.setCep(rs.getString("f.cep"));
                fornecedor.setIcAtivo(rs.getInt("f.ic_ativo"));
                fornecedor.setContatoFornecedor(null);

                tipoProduto.setCodigo(rs.getString("t.codigo"));
                tipoProduto.setDescricao(rs.getString("t.descricao"));
                
                produto.setFornecedor(fornecedor);
                produto.setTipoProduto(tipoProduto);
                
                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }    
    
    public List<Produto> listarProdutosMostruario(Integer idMostruario, Integer status) throws SQLException, ClassNotFoundException{
        String sql = "SELECT *, "
                        + "date_format(m.data_inclusao, '%d/%m/%Y') as dt_inclusao "
                        +"FROM HORUS_PRODUTO P "
                        +"INNER JOIN T_FORNECEDOR F ON P.ID_FORNECEDOR = F.ID "
                        +"INNER JOIN T_TIPO_PRODUTO T ON P.CODIGO_TIPO_PRODUTO = T.CODIGO "
                        +"INNER JOIN T_PRODUTO_MOSTRUARIO M ON P.ID = M.ID_PRODUTO "
                        +"WHERE M.ID_MOSTRUARIO = "+idMostruario
                        +" AND M.IC_VENDIDO = "+status
                        +" ORDER BY M.DATA_INCLUSAO, P.CODIGO ";
        
        return getListProdutos(sql);
    }
    
    public List<Produto> listarProdutosMostruario(Integer idMostruario) throws SQLException, ClassNotFoundException{
        String sql = "SELECT *, "
                        + "date_format(m.data_inclusao, '%d/%m/%Y') as dt_inclusao "
                        +"FROM HORUS_PRODUTO P "
                        +"INNER JOIN T_FORNECEDOR F ON P.ID_FORNECEDOR = F.ID "
                        +"INNER JOIN T_TIPO_PRODUTO T ON P.CODIGO_TIPO_PRODUTO = T.CODIGO "
                        +"INNER JOIN T_PRODUTO_MOSTRUARIO M ON P.ID = M.ID_PRODUTO "
                        +"WHERE M.ID_MOSTRUARIO = "+idMostruario
                        +" AND M.IC_VENDIDO IN (0,1) "
                + " ORDER BY M.DATA_INCLUSAO, P.CODIGO";
        
        return getListProdutos(sql);
    }    
    
    public Map preencherDisponibilidade(Integer idMostruario) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;        
        Map<String,Integer> disponibilidade = null;
        String sql = "select b.codigo, "
                        +"count(1) as qtd "
                    +"from t_produto_mostruario a "
                    +"inner join horus_produto b "
                    +"on a.id_produto = b.id "
                    +"where a.id_mostruario ="+idMostruario
                    +" group by b.codigo";
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            disponibilidade = new HashMap<String, Integer>();
            
            while (rs.next()) {
                disponibilidade.put(rs.getString("codigo"), rs.getInt("qtd"));
            }
            
            return disponibilidade;
            
        } catch (SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }         
    }
    
    public void fecharMostruario(Mostruario mostruario, 
                                List<Produto> produtosVendidos,
                                List<Produto> produtosDevolvidos) 
            throws SQLException, ClassNotFoundException{ 
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
                        
            
            st = conn.createStatement();
            st.execute("update t_mostruario "
                    + "set data_fechamento = now() "
                    + ", ic_status = 1 "
                    + ", qtd_venda = " + mostruario.getQtdItensFechamento()
                    + ", vl_total = " + mostruario.getValorFechamento()
                    + ", comissao = " + mostruario.getPercentualComissaoFechamento()
                    + " where id = "+mostruario.getId());

            st.execute("update t_produto a "
                        +"inner join (select b.id_produto, " 
                                    +"count(*) as qtd "
                                    +"from t_produto_mostruario b "
                                    +"where b.id_mostruario = "+mostruario.getId()
                                    +" group by b.id_produto) c "
                        +"on a.id = c.id_produto "
                        +"set a.qtd_estoque = (a.qtd_estoque + c.qtd), "
                        +"a.qtd_consig = (a.qtd_consig - c.qtd)");
            
            st.execute("delete from t_produto_mostruario where id_mostruario ="+mostruario.getId());
            
            for (Produto produto : produtosVendidos){
                String data;
                if (produto.getData()!=null){
                    data = " STR_TO_DATE('"+produto.getData()+"', '%d/%m/%Y') ";
                } else {
                    data = " NULL ";
                }                
                st.execute("insert into t_produto_mostruario (id_mostruario, id_produto, ic_vendido, vl_produto, data_inclusao) values ("+mostruario.getId()+", "+produto.getId()+", 1,"+produto.getValorSaida()+","+data+")");
                st.execute("update t_produto set qtd_estoque = (qtd_estoque - 1) where id = "+produto.getId());
            }
            
            for (Produto produto : produtosDevolvidos){
                String data;
                if (produto.getData()!=null){
                    data = " STR_TO_DATE('"+produto.getData()+"', '%d/%m/%Y') ";
                } else {
                    data = " NULL ";
                }                
                st.execute("insert into t_produto_mostruario (id_mostruario, id_produto, ic_vendido, vl_produto, data_inclusao) values ("+mostruario.getId()+", "+produto.getId()+", 0,"+produto.getValorSaida()+","+data+")");
            }   
            
            int idPgto = 1;
            for (PagamentoMostruario pgto : mostruario.getPagamento()){
                String dataRealizado;
                
                if (pgto.getDataRealizado()==null||pgto.getDataRealizado().isEmpty())
                    dataRealizado = "NULL";
                else
                    dataRealizado = "STR_TO_DATE('"+pgto.getDataRealizado()+"', '%d/%m/%Y')";
                                    
                st.execute("INSERT INTO `horus`.`t_pgto_mostruario` "
                            +"(`id`, "
                            +"`id_mostruario`, "
                            +"`data_prev_pgto`, "
                            +"`data_rlzd_pgto`, "
                            +"`vl_pgto`) "
                            +"VALUES "
                            +"( "
                            +idPgto +", "
                            +mostruario.getId() + ", "
                            +"STR_TO_DATE('"+pgto.getDataPrevisao()+"', '%d/%m/%Y'), "
                            +dataRealizado +", "
                            +pgto.getValorPgto()
                            +")");
                idPgto++;
            }
            
            List<Produto> atualizarLog = new ArrayList<Produto>();

            rs = st.executeQuery("select id_produto, count(*) as qtd from t_produto_mostruario where id_mostruario = "+mostruario.getId()+" and ic_vendido = 1 group by id_produto");
            
            while (rs.next()){
                Produto p = new Produto();
                p.setId(rs.getInt("id_produto"));
                p.setQtdEstoque(rs.getInt("qtd"));
                atualizarLog.add(p);
            }
            
            for (Produto produto : atualizarLog){
                st.execute("INSERT INTO `horus`.`t_log_estoque` "
                    + "(`fluxo`, "
                    + "`id_motivo`, "
                    + "`id_produto`, "
                    + "`dt_movimentacao`, "
                    + "`qtd_produto`) "
                    + "VALUES "
                    + "('S'"
                    + ", 2"
                    + ", "+produto.getId()
                    + ", now()"
                    + ", "+produto.getQtdEstoque()
                    + ")");
            }
            
            rs = st.executeQuery("select count(*) from t_pgto_mostruario where id_mostruario = "+mostruario.getId()+" and data_rlzd_pgto is null");
            if (rs.first()){
                if (rs.getInt(1)==0){
                    st.execute("update t_mostruario set ic_status = 2 where id = "+mostruario.getId());
                }
            }
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }        
    }
    
    public void atualizarpagamento(Mostruario mostruario) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            st = conn.createStatement();
            
            st.execute("delete from t_pgto_mostruario where id_mostruario = "+mostruario.getId());
            
            int idPgto = 1;
            
            for (PagamentoMostruario pgto : mostruario.getPagamento()){
                String dataRealizado;
                
                if (pgto.getDataRealizado()==null||pgto.getDataRealizado().isEmpty())
                    dataRealizado = "NULL";
                else
                    dataRealizado = "STR_TO_DATE('"+pgto.getDataRealizado()+"', '%d/%m/%Y')";
                
                st.execute("INSERT INTO `horus`.`t_pgto_mostruario` "
                            +"(`id`, "
                            +"`id_mostruario`, "
                            +"`data_prev_pgto`, "
                            +"`data_rlzd_pgto`, "
                            +"`vl_pgto`) "
                            +"VALUES "
                            +"( "
                            +idPgto +", "
                            +mostruario.getId() + ", "
                            +"STR_TO_DATE('"+pgto.getDataPrevisao()+"', '%d/%m/%Y'), "
                            +dataRealizado +", "
                            +pgto.getValorPgto()
                            +")");
                idPgto++;
            }
            
            rs = st.executeQuery("select count(*) from t_pgto_mostruario where id_mostruario = "+mostruario.getId()+" and data_rlzd_pgto is null");
            if (rs.first()){
                if (rs.getInt(1)==0){
                    st.execute("update t_mostruario set ic_status = 2 where id = "+mostruario.getId());
                }
            }            
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }        
    }
    
    
    public List<PagamentoMostruario> listarFormaPgto(Mostruario mostruario) throws SQLException, ClassNotFoundException, Exception{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "select id, "
            +"id_mostruario, "
            +"date_format(data_prev_pgto, '%d/%m/%Y') as data_prev_pgto, "
            +"date_format(data_rlzd_pgto, '%d/%m/%Y') as data_rlzd_pgto, "
            +"vl_pgto "
        +"from t_pgto_mostruario "
        +"where id_mostruario = "+mostruario.getId()
        +" order by id ";
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            List<PagamentoMostruario> listaPgto = new ArrayList<PagamentoMostruario>();
            
            while (rs.next()){
                PagamentoMostruario pgto = new PagamentoMostruario();
                pgto.setMostruario(mostruario);
                pgto.setId(rs.getInt("id"));
                pgto.setValorPgto(rs.getFloat("vl_pgto"));
                pgto.setDataPrevisao(rs.getString("data_prev_pgto"));
                pgto.setDataRealizado(rs.getString("data_rlzd_pgto"));
                if (pgto.getDataRealizado()==null||pgto.getDataRealizado().isEmpty())
                    pgto.setPago(false);
                else
                    pgto.setPago(true);
                
                listaPgto.add(pgto);
            }
            
            return listaPgto;
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }
    }
    
    
    
    
    
    
    
    public void editarMostruario(Mostruario mostruario, 
                                List<Produto> produtosVendidos,
                                List<Produto> produtosDevolvidos) 
            throws Exception, SQLException, ClassNotFoundException{     

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            conn.setAutoCommit(false);                       
                    
            st = conn.createStatement();
                        
            for (Produto produto : produtosVendidos){
                st.execute("update t_produto_mostruario set ic_vendido = 1 where id = " + produto.getIdProdutoMostruario());
                st.execute("update t_produto set qtd_estoque = qtd_estoque - 1 where id = " + produto.getId());
                st.execute("INSERT INTO `horus`.`t_log_estoque` "
                    + "(`fluxo`, "
                    + "`id_motivo`, "
                    + "`id_produto`, "
                    + "`dt_movimentacao`, "
                    + "`qtd_produto`) "
                    + "VALUES "
                    + "('S'"
                    + ", 2"
                    + ", "+produto.getId()
                    + ", now()"
                    + ", 1"
                    + ")");                
            }
            
            for (Produto produto : produtosDevolvidos){
                st.execute("update t_produto_mostruario set ic_vendido = 0 where id = " + produto.getIdProdutoMostruario());
                st.execute("update t_produto set qtd_estoque = qtd_estoque + 1 where id = " + produto.getId());
                st.execute("INSERT INTO `horus`.`t_log_estoque` "
                    + "(`fluxo`, "
                    + "`id_motivo`, "
                    + "`id_produto`, "
                    + "`dt_movimentacao`, "
                    + "`qtd_produto`) "
                    + "VALUES "
                    + "('E'"
                    + ", 1"
                    + ", "+produto.getId()
                    + ", now()"
                    + ", 1"
                    + ")");                                
            }   
            
            st.execute("update t_mostruario "
                    + "set ic_status = 1 "
                    + ", qtd_venda = (select count(*) from t_produto_mostruario where ic_vendido = 1 and id_mostruario = " + mostruario.getId() + ") "
                    + ", vl_total = (select sum(vl_produto) from t_produto_mostruario where id_mostruario = "+mostruario.getId()+" and ic_vendido = 1) "
                    + ", comissao = " + mostruario.getPercentualComissaoFechamento()
                    + " where id = "+mostruario.getId());            
                      
            rs = st.executeQuery("select count(*) from t_pgto_mostruario where id_mostruario = "+mostruario.getId()+" and data_rlzd_pgto is null");
            if (rs.first()){
                if (rs.getInt(1)==0){
                    st.execute("update t_mostruario set ic_status = 2 where id = "+mostruario.getId());
                }
            }
            
            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            closeResources(conn, st, rs);
        }                

    }    
    
}
