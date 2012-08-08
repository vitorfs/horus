package tablemodel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.ProdutoConsignado;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class ProdutoConsignadoTableModel extends AbstractTableModel {
    private List<ProdutoConsignado> linhas;
    private String[] colunas = new String[] {
                    "Revendedora", "Código", "Nome", "Retirada", "Preço", "Qtd."};
    
    public ProdutoConsignadoTableModel() {
            linhas = new ArrayList<ProdutoConsignado>();
    }

    public ProdutoConsignadoTableModel(List<ProdutoConsignado> listaDeProdutos) {
            linhas = new ArrayList<ProdutoConsignado>(listaDeProdutos);
    }

    @Override
    public int getColumnCount() {
            return colunas.length;
    }

    @Override
    public int getRowCount() {
            return linhas.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
            return colunas[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return String.class;
            case 5: 
                return Integer.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            ProdutoConsignado produto = linhas.get(rowIndex);
            //"Revendedora", "Código", "Nome", "Preço", "Qtd", "Retirada"
            switch (columnIndex) {
            case 0: return produto.getRevendedora().getNome();
            case 1: return produto.getCodigo();
            case 2: return produto.getNome();
            case 3: return produto.getDataRetirada();                
            case 4: return NumberFormat.getCurrencyInstance().format(produto.getValorSaida());
            case 5: return produto.getQuantidade();
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {};

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }

    public ProdutoConsignado getProduto(int indiceLinha) {
            return linhas.get(indiceLinha);
    }

    public void addProduto(ProdutoConsignado produto) {
            linhas.add(produto);
            int ultimoIndice = getRowCount() - 1;
            fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void removeProduto(int indiceLinha) {
            linhas.remove(indiceLinha);
            fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void addListaDeProdutos(List<ProdutoConsignado> produtos) {
            int tamanhoAntigo = getRowCount();
            linhas.addAll(produtos);
            fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }

    public void limpar() {
            linhas.clear();
            fireTableDataChanged();
    }

    public boolean isEmpty() {
            return linhas.isEmpty();
    }
    
    public List<ProdutoConsignado> getTodosProdutos(){
        return linhas;
    }
    
}
