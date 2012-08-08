package tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Revendedora;
import util.Horus;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class RevendedoraTableModel extends AbstractTableModel {
    private List<Revendedora> linhas;
    private String[] colunas = new String[] {
                    "Nome", "Telefone", "Email", "Status"};
    
    public RevendedoraTableModel() {
            linhas = new ArrayList<Revendedora>();
    }

    public RevendedoraTableModel(List<Revendedora> listaDeRevendedoras) {
            linhas = new ArrayList<Revendedora>(listaDeRevendedoras);
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
                return String.class;
            case 3: 
                return Boolean.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            Revendedora revendedora = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: return revendedora.getNome();
            case 1: return Horus.formataTelefone(revendedora.getTelefone1());
            case 2: return revendedora.getEmail();
            case 3: return (revendedora.getIcAtivo()==0?false:true);
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {};

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }

    public Revendedora getRevendedora(int indiceLinha) {
            return linhas.get(indiceLinha);
    }

    public void addRevendedora(Revendedora revendedora) {
            linhas.add(revendedora);
            int ultimoIndice = getRowCount() - 1;
            fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void removeRevendedora(int indiceLinha) {
            linhas.remove(indiceLinha);
            fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void addListaDeRevendedoras(List<Revendedora> revendedoras) {
            int tamanhoAntigo = getRowCount();
            linhas.addAll(revendedoras);
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
