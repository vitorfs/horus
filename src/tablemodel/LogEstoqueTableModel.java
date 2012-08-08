package tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.LogEstoque;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class LogEstoqueTableModel extends AbstractTableModel{
    private List<LogEstoque> linhas;
    private String[] colunas = new String[] {
                    "Produto", "Descrição", "Data", "Quantidade"};

    public LogEstoqueTableModel() {
        linhas = new ArrayList<LogEstoque>();
    }
    
    public LogEstoqueTableModel(List<LogEstoque> listaDeLogEstoque){
        linhas = new ArrayList<LogEstoque>(listaDeLogEstoque);
    }
    
    
    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
            return colunas[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            LogEstoque logEstoque = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: return logEstoque.getIdProduto();
            case 1: return logEstoque.getMotivo();
            case 2: return logEstoque.getDataMovimentacao();
            case 3: return logEstoque.getQtdProduto();
                
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
            case 0:
            case 1:
            case 2: 
                return String.class;
            case 3: 
                return Integer.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }    
    
}
