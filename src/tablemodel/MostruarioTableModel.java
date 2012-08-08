package tablemodel;

import java.text.NumberFormat;
import model.Mostruario;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import util.Horus;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class MostruarioTableModel extends AbstractTableModel {
    private List<Mostruario> linhas;
    private String[] colunas = new String[] {
                    "Código", "Revendedora", "Data Retirada", "Data Acerto", "Qtd. Peças", "Valor Total", "Data Fechamento", "Valor Aberto", "Status"};
    
    public MostruarioTableModel() {
            linhas = new ArrayList<Mostruario>();
    }

    public MostruarioTableModel(List<Mostruario> listaDeMostruarios) {
            linhas = new ArrayList<Mostruario>(listaDeMostruarios);
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
            case 0: return Integer.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return String.class;
            case 4: return Integer.class;
            case 5: return String.class;
            case 6: return String.class;
            case 7: return String.class;
            case 8: return String.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            Mostruario mostruario = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: return mostruario.getId();
            case 1: return mostruario.getRevendedora().getNome();
            case 2: return mostruario.getDataRetirada();
            case 3: return mostruario.getDataAcerto();
            case 4: return mostruario.getQtdItens();
            case 5: return NumberFormat.getCurrencyInstance().format(mostruario.getValorTotal());
            case 6: return mostruario.getDataFechamento();
            case 7: return NumberFormat.getCurrencyInstance().format(mostruario.getValorEmAberto());
            case 8: 
                if (mostruario.getStatus()==0 && Horus.isBeforeToday(mostruario.getDataAcerto()))
                    return "Atrasado";                
                else if (mostruario.getStatus()==0)
                    return "Aberto";
                else if (mostruario.getStatus()==1)
                    return "Pendente";
                else
                    return "Concluido";
                
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {};

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }

    public Mostruario getMostruario(int indiceLinha) {
            return linhas.get(indiceLinha);
    }

    public void addMostruario(Mostruario mostruario) {
            linhas.add(mostruario);
            int ultimoIndice = getRowCount() - 1;
            fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void removeMostruario(int indiceLinha) {
            linhas.remove(indiceLinha);
            fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void addListaDeMostruarios(List<Mostruario> mostruarios) {
            int tamanhoAntigo = getRowCount();
            linhas.addAll(mostruarios);
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
