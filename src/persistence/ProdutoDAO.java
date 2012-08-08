package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Fornecedor;
import model.Produto;
import model.TipoProduto;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class ProdutoDAO extends DAO {
    private static final ProdutoDAO INSTANCE = new ProdutoDAO();
    
    private ProdutoDAO(){
        
    }
    
    public static ProdutoDAO getInstance(){
        return INSTANCE;
    }
    
    public void insert(Produto produto) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("insert into t_produto (id_fornecedor, codigo_tipo_produto, nome, descricao, valor_entrada, valor_saida, codigo_origem_fornecedor, ic_ativo) values (?, ?, ?, ?, ?, ?, ?, ?)");
            
            setParam(st, 1, produto.getFornecedor().getId());
            setParam(st, 2, produto.getTipoProduto().getCodigo());
            setParam(st, 3, produto.getNome());
            setParam(st, 4, produto.getDescricao());
            setParam(st, 5, produto.getValorEntrada());
            setParam(st, 6, produto.getValorSaida());
            setParam(st, 7, produto.getCodItemForn());
            setParam(st, 8, produto.getIcAtivo());

            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st);
        }
    }    
    
    public void update(Produto produto) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("update t_produto set id_fornecedor = ?, codigo_tipo_produto = ?, nome = ?, descricao = ?, valor_entrada = ?, valor_saida = ?, codigo_origem_fornecedor = ?, ic_ativo = ? where id = ?");
            
            setParam(st, 1, produto.getFornecedor().getId());
            setParam(st, 2, produto.getTipoProduto().getCodigo());
            setParam(st, 3, produto.getNome());
            setParam(st, 4, produto.getDescricao());
            setParam(st, 5, produto.getValorEntrada());
            setParam(st, 6, produto.getValorSaida());
            setParam(st, 7, produto.getCodItemForn());
            setParam(st, 8, produto.getIcAtivo());
            setParam(st, 9, produto.getId());

            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st);
        }
    }
    
    public Produto getProduto(Integer id) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("SELECT * FROM HORUS_PRODUTO WHERE ID = ?");
            st.setInt(1, id);
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
    
    public Produto getProduto(String cod, Integer disp, Integer dispAlt) throws Exception, SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("SELECT * FROM HORUS_PRODUTO WHERE CODIGO = ? AND IC_ATIVO = 1");
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
                
                if ((produto.getQtdEstoque()+dispAlt)-disp>0){
                    return produto;
                } else {
                    throw new Exception("Produto indisponível no estoque.");
                }
                
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
                produto.setValorSaida(rs.getFloat("p.valor_saida"));
                produto.setCodItemForn(rs.getString("p.codigo_origem_fornecedor"));
                produto.setIcAtivo(rs.getInt("p.ic_ativo"));
                produto.setQtdEstoque(rs.getInt("p.qtd_estoque"));
                produto.setQtdConsignado(rs.getInt("p.qtd_consig"));
                produto.setQtdTotal(rs.getInt("p.qtd_total"));
                
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
    
    private String getFiltro(Produto filtro){
        String sql = "";
        
        if (filtro!=null){
            if (filtro.getFornecedor()!=null)
                sql += " AND P.ID_FORNECEDOR = "+filtro.getFornecedor().getId();
            if (filtro.getTipoProduto()!=null)
                sql += " AND P.CODIGO_TIPO_PRODUTO = '"+filtro.getTipoProduto().getCodigo()+"'";
            if (filtro.getIcAtivo()!=null)
                sql += " AND P.IC_ATIVO = "+filtro.getIcAtivo();            
        }
        
        return sql;
    }
    
    public List<Produto> getProdutos() throws SQLException, ClassNotFoundException{
        return getListProdutos("SELECT * FROM HORUS_PRODUTO P INNER JOIN T_FORNECEDOR F ON P.ID_FORNECEDOR = F.ID INNER JOIN T_TIPO_PRODUTO T ON P.CODIGO_TIPO_PRODUTO = T.CODIGO");
    }

    public List<Produto> getProdutos(Produto filtro, int limit, int offset) throws SQLException, ClassNotFoundException{
        String sql = "SELECT * FROM HORUS_PRODUTO P INNER JOIN T_FORNECEDOR F ON P.ID_FORNECEDOR = F.ID INNER JOIN T_TIPO_PRODUTO T ON P.CODIGO_TIPO_PRODUTO = T.CODIGO WHERE 1=1";           
        sql += getFiltro(filtro);
        sql += " LIMIT "+limit+" OFFSET "+offset;
        //System.out.println(sql);
        return getListProdutos(sql);
    }
       
    
    public Integer countProdutos(Produto filtro) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            sql = "SELECT COUNT(*) FROM HORUS_PRODUTO P WHERE 1=1";
            sql += getFiltro(filtro);
            
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            if (rs.first()){
                return rs.getInt(1);
            }
            else {
                return 0;
            }
            
        } catch (Exception e) {
            return 0;
        } finally {
            closeResources(conn, st, rs);
        }        
    }
    
    public Integer getNextId() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("select max(id)+1 as next_value from t_produto");
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
    
    
    public void delete(Produto produto) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = DatabaseLocator.getInstance().getConnection();
            st = conn.prepareStatement("delete from t_produto where id = "+produto.getId());
            st.execute();
        } catch(SQLException e) {
            throw e;
        } finally {
            closeResources(conn, st);
        }        
    }
}
