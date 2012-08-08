package tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.PagamentoMostruario;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class PagamentoMostruarioTableModel extends AbstractTableModel {
    private List<PagamentoMostruario> linhas;
    private String[] colunas = new String[] {
                    "Nº", "Valor", "Data Pagamento", "Pago", "Status"};
    
    public PagamentoMostruarioTableModel() {
            linhas = new ArrayList<PagamentoMostruario>();
    }

    public PagamentoMostruarioTableModel(List<PagamentoMostruario> listaDePagamentos) {
            linhas = new ArrayList<PagamentoMostruario>(listaDePagamentos);
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
                return Float.class;
            case 2:
            case 3:
                return String.class;
            case 4: 
                return Boolean.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            PagamentoMostruario pgto = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return pgto.getValorPgto();
            case 2: return pgto.getDataPrevisao();
            case 3: return pgto.getDataRealizado();
            case 4: return pgto.isPago();
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        PagamentoMostruario pgto = linhas.get(rowIndex);
        switch (columnIndex) {
            case 0: 
                break;
            case 1: 
                pgto.setValorPgto(aValue);
                break;
            case 2: 
                pgto.setDataPrevisao(aValue);
                break;
            case 3: 
                pgto.setDataRealizado(aValue);
                break;
            case 4:
                pgto.setPago(aValue);
                break;
        }
    };

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return false;
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }

    public PagamentoMostruario getPagamentoMostruario(int indiceLinha) {
            return linhas.get(indiceLinha);
    }

    public void addPagamentoMostruario(PagamentoMostruario pagamentoMostruario) {
            linhas.add(pagamentoMostruario);
            int ultimoIndice = getRowCount() - 1;
            fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void removeProduto(int indiceLinha) {
            linhas.remove(indiceLinha);
            fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void addListaDePagamentoMostruario(List<PagamentoMostruario> pagamentos) {
            int tamanhoAntigo = getRowCount();
            linhas.addAll(pagamentos);
            fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }

    public void limpar() {
            linhas.clear();
            fireTableDataChanged();
    }

    public boolean isEmpty() {
            return linhas.isEmpty();
    }
    
    public List<PagamentoMostruario> getTodosPagamentos(){
        return linhas;
    }
    
}
