package tablemodel;

import model.Fornecedor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import util.Horus;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class FornecedorTableModel extends AbstractTableModel {
    private List<Fornecedor> linhas;
    private String[] colunas = new String[] {
                    "Código", "Nome", "Telefone", "Cidade", "Status"};
    
    public FornecedorTableModel() {
            linhas = new ArrayList<Fornecedor>();
    }

    public FornecedorTableModel(List<Fornecedor> listaDeFornecedores) {
            linhas = new ArrayList<Fornecedor>(listaDeFornecedores);
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
                return String.class;
            case 4: 
                return Boolean.class;
            default:throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            Fornecedor fornecedor = linhas.get(rowIndex);
            switch (columnIndex) {
            case 0: 
                return Horus.zeroFill(fornecedor.getId().toString(), 2);
            case 1: 
                return fornecedor.getNomeFantasia();
            case 2: 
                return Horus.formataTelefone(fornecedor.getTelefone1());
            case 3: 
                return fornecedor.getMunicipio();
            case 4: 
                return (fornecedor.getIcAtivo()==0?false:true);
            default: throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {};

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
    }

    public Fornecedor getFornecedor(int indiceLinha) {
            return linhas.get(indiceLinha);
    }

    public void addFornecedor(Fornecedor fornecedor) {
            linhas.add(fornecedor);
            int ultimoIndice = getRowCount() - 1;
            fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void removeFornecedor(int indiceLinha) {
            linhas.remove(indiceLinha);
            fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void addListaDeFornecedores(List<Fornecedor> fornecedores) {
            int tamanhoAntigo = getRowCount();
            linhas.addAll(fornecedores);
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
