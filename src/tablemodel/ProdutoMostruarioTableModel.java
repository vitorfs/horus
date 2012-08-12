package tablemodel;

import java.text.NumberFormat;
import model.Produto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class ProdutoMostruarioTableModel extends AbstractTableModel {
    private List<Produto> linhas;
    private String[] colunas = new String[] {
                    "Nº", "Código", "Nome", "Data", "Preço"};
    
    public ProdutoMostruarioTableModel() {
            linhas = new ArrayList<Produto>();
    }

    public ProdutoMostruarioTableModel(List<Produto> listaDeProdutos) {
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
                return Integer.class;
            case 1: 
            case 2: 
            case 3: 
            case 4: 
                return String.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            Produto produto = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: return rowIndex+1;                
            case 1: return produto.getCodigo();
            case 2: return produto.getNome();
            case 3: return produto.getData();
            case 4: return NumberFormat.getCurrencyInstance().format(produto.getValorSaida());
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
    
    public int getProduto(String cod){
        int achou = -1;
        for (int i=0;i<linhas.size();i++){
            if (linhas.get(i).getCodigo().equals(cod)){
                achou = i;
                break;
            }
        }
        return achou;
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
    
    public List<Produto> getTodosProdutos(){
        return linhas;
    }
    
}
