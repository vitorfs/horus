package tablemodel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Produto;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class ProdutoDisponivelEstoqueTableModel extends AbstractTableModel {
    private List<Produto> linhas;
    private String[] colunas = new String[] {
                    "Código Produto", "Produto", "Valor Unitário", "Qtd. Estoque"};
    
    public ProdutoDisponivelEstoqueTableModel() {
            linhas = new ArrayList<Produto>();
    }

    public ProdutoDisponivelEstoqueTableModel(List<Produto> listaDeProdutos) {
            linhas = new ArrayList<Produto>(listaDeProdutos);
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
                return String.class;
            case 2: 
                return Integer.class;
            case 3: 
                return Float.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            Produto produto = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: 
                return produto.getCodigo();
            case 1: 
                return produto.getNome();
            case 2: 
                return NumberFormat.getCurrencyInstance().format(produto.getValorSaida());
            case 3: 
                return produto.getQtdEstoque();
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {};

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }

    public Produto getProduto(int indiceLinha) {
            return linhas.get(indiceLinha);
    }

    public void addProduto(Produto produto) {
            linhas.add(produto);
            int ultimoIndice = getRowCount() - 1;
            fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void removeProduto(int indiceLinha) {
            linhas.remove(indiceLinha);
            fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void addListaDeProdutos(List<Produto> produtos) {
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
    
}
