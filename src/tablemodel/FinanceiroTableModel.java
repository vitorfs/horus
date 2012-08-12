package tablemodel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.PagamentoMostruario;
import util.Horus;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class FinanceiroTableModel extends AbstractTableModel {
    private List<PagamentoMostruario> linhas;
    private String[] colunas = new String[] {
                "Código Mostruario", "Revendedora", "Data Pagamento", "Valor", "Status"};
    
    public FinanceiroTableModel() {
            linhas = new ArrayList<PagamentoMostruario>();
    }

    public FinanceiroTableModel(List<PagamentoMostruario> listaDePagamentos) {
            linhas = new ArrayList<PagamentoMostruario>(listaDePagamentos);
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
    public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
            case 0: return Integer.class;
            case 1: 
            case 2: 
            case 3:
            case 4: return String.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            PagamentoMostruario pgto = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: return pgto.getMostruario().getId();
            case 1: return pgto.getMostruario().getRevendedora().getNome();
            case 2: return pgto.getDataPrevisao();
            case 3: return NumberFormat.getCurrencyInstance().format(pgto.getValorPgto());
            case 4:
                if (Horus.isToday(pgto.getDataPrevisao()))
                    return "Hoje";                                
                else if (Horus.isBeforeToday(pgto.getDataPrevisao()))
                    return "Vencido";                
                else 
                    return "Aberto";                
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }
    
    public void addListaPagamentos(List<PagamentoMostruario> pgtos) {
            int tamanhoAntigo = getRowCount();
            linhas.addAll(pgtos);
            fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }

    public void limpar() {
            linhas.clear();
            fireTableDataChanged();
    }

    public boolean isEmpty() {
            return linhas.isEmpty();
    }
    
    public List<PagamentoMostruario> getPagamentos(){
        return linhas;
    }    
    
}

