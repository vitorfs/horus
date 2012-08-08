/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrmFecharPedido.java
 *
 * Created on Jan 7, 2012, 10:28:06 PM
 */
package view;

import controller.ManterMostruarioController;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import model.Mostruario;
import model.PagamentoMostruario;
import model.Produto;
import model.Revendedora;
import print.ImprimirRomaneio2;
import tablemodel.ProdutoMostruarioTableModel;
import util.Horus;

/**
 *
 * @author Vitor
 */
public class FrmFecharMostruario extends javax.swing.JDialog {
    private ProdutoMostruarioTableModel modelVendido;
    private ProdutoMostruarioTableModel modelDevolvido;
    private double percentVendido;
    private float valorTotalMostruario;
    private int status;
    //private boolean ctr = false;
    private List<JTextField> listValor;
    private List<JFormattedTextField> listDataPgto;
    private List<JFormattedTextField> listPgtoRlzd;
    private List<JCheckBox> listPago;
    
    private boolean locked = true;
    private List<PagamentoMostruario> listPgto;
            
    private static final ImageIcon ativo
        = new ImageIcon(FrmPanelFornecedor.class.getResource("/images/accept.png"));
    private static final ImageIcon inativo
        = new ImageIcon(FrmPanelFornecedor.class.getResource("/images/cross.png"));             
    
    private TableRowSorter<ProdutoMostruarioTableModel> sorter;
    
    private List<Produto> pVendidoLocked;
    private List<Produto> pDevolvidoLocked;
    
    /** Creates new form FrmFecharPedido */
    public FrmFecharMostruario(java.awt.Frame parent, boolean modal, Mostruario mostruario, String titulo) {
        super(parent, modal);
        initComponents();

        sorter = new TableRowSorter<ProdutoMostruarioTableModel>(modelDevolvido);
        //tblProdutosDevolvidos.setRowSorter(sorter);        
        //sorter.setRowFilter(null);
        
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResource("/images/money_dollar.png"));
        } catch (IOException e) {
            
        }
        this.setIconImage(image);       
        this.setTitle(titulo);
        lbTitulo.setText(titulo);
        
        listValor = new ArrayList<JTextField>();
        listValor.add(txtValor1);
        listValor.add(txtValor2);
        listValor.add(txtValor3);
        listValor.add(txtValor4);
        listValor.add(txtValor5);
        listValor.add(txtValor6);
        
        listDataPgto = new ArrayList<JFormattedTextField>();
        listDataPgto.add(txtDataPgto1);
        listDataPgto.add(txtDataPgto2);
        listDataPgto.add(txtDataPgto3);
        listDataPgto.add(txtDataPgto4);
        listDataPgto.add(txtDataPgto5);
        listDataPgto.add(txtDataPgto6);
        
        listPgtoRlzd = new ArrayList<JFormattedTextField>();
        listPgtoRlzd.add(txtDataRlzd1);
        listPgtoRlzd.add(txtDataRlzd2);
        listPgtoRlzd.add(txtDataRlzd3);
        listPgtoRlzd.add(txtDataRlzd4);
        listPgtoRlzd.add(txtDataRlzd5);
        listPgtoRlzd.add(txtDataRlzd6);
        
        listPago = new ArrayList<JCheckBox>();
        listPago.add(ckPago1);
        listPago.add(ckPago2);
        listPago.add(ckPago3);
        listPago.add(ckPago4);
        listPago.add(ckPago5);
        listPago.add(ckPago6);
                
        if (mostruario!=null){
            try {
                lbCodigo.setText(mostruario.getId().toString());
                lbRevendedora.setText(mostruario.getRevendedora().getNome());
                lbDataAcerto.setText(mostruario.getDataAcerto());
                lbDataRetirada.setText(mostruario.getDataRetirada());
                lbValorTotal.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorTotal()));                
                status = mostruario.getStatus();
                
                if (mostruario.getStatus()==0) {
                    locked = false;
                    
                    mostruario.setProdutos(ManterMostruarioController.getInstance().listarProdutosMostruario(mostruario.getId()));
                    addProdutos(mostruario.getProdutos(), modelVendido, tblProdutosVendidos); 
                    
                    lbNumPecas.setText(String.valueOf(mostruario.getProdutos().size()));
                    txtPcsVendidas.setText(String.valueOf(mostruario.getProdutos().size()));
                    txtVendaTotal.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorTotal()));
                    txtPercentualComissao.setText(mostruario.getRevendedora().getComissao().toString());
                    txtComissao.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorTotal() * (mostruario.getRevendedora().getComissao()/100)));
                    txtTotalAcerto.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorTotal() - (mostruario.getValorTotal() * (mostruario.getRevendedora().getComissao()/100))));
                    percentVendido = mostruario.getProdutos().size();
                    valorTotalMostruario = mostruario.getValorTotal();
                    
                    
                    //definirValoresParcelas();
                }
                else {
                    
                    lbFechado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock.png"))); // NOI18N
                    lbFechado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));                                            
                    
                    txtPcsVendidas.setText(mostruario.getQtdItensFechamento().toString());
                    txtVendaTotal.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorFechamento()));
                    txtPercentualComissao.setText(mostruario.getPercentualComissaoFechamento().toString());
                    txtComissao.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorFechamento() * (mostruario.getPercentualComissaoFechamento()/100)));
                    txtTotalAcerto.setText(NumberFormat.getCurrencyInstance().format(mostruario.getValorFechamento() - (mostruario.getValorFechamento() * (mostruario.getPercentualComissaoFechamento()/100))));
                                               
                    addProdutos(ManterMostruarioController.getInstance().listarProdutosMostruario(mostruario.getId(), 0), modelDevolvido, tblProdutosDevolvidos); 
                    addProdutos(ManterMostruarioController.getInstance().listarProdutosMostruario(mostruario.getId(), 1), modelVendido, tblProdutosVendidos); 
                    
                    //addPagamentos(ManterMostruarioController.getInstance().listarFormaPgto(mostruario));
                    listPgto = new ArrayList<PagamentoMostruario>();
                    listPgto = ManterMostruarioController.getInstance().listarFormaPgto(mostruario);
                    
                    for (int i=0;i<listPgto.size();i++){
                        listValor.get(i).setText(listPgto.get(i).getValorPgto().toString());
                        listDataPgto.get(i).setText(listPgto.get(i).getDataPrevisao());
                        listPgtoRlzd.get(i).setText(listPgto.get(i).getDataRealizado());
                        listPago.get(i).setSelected(listPgto.get(i).isPago());
                    }
                    
                    cbParcelas.setSelectedIndex(listPgto.isEmpty()?2:listPgto.size()-1);
                        
                    lbNumPecas.setText(String.valueOf(tblProdutosDevolvidos.getRowCount()+tblProdutosVendidos.getRowCount()));
                    
                    txtCodProduto.setEnabled(false);
                    btnAdicionar.setEnabled(false);
                    btnRemover.setEnabled(false);
                    //btnSalvar.setEnabled(false);
                    txtPercentualComissao.setEditable(false);
                    
                    double percVendido = tblProdutosVendidos.getRowCount()/Double.parseDouble(lbNumPecas.getText());
                    double percDevol = tblProdutosDevolvidos.getRowCount()/Double.parseDouble(lbNumPecas.getText());

                    percentVendido = tblProdutosVendidos.getRowCount() + tblProdutosDevolvidos.getRowCount();
                            
                    lbPercentualVendido.setText("("+NumberFormat.getPercentInstance().format(percVendido)+")");
                    lbPercentualDevolvido.setText("("+NumberFormat.getPercentInstance().format(percDevol)+")");
                    
                    valorTotalMostruario = mostruario.getValorFechamento();
                    
                    /*if (tblPgto.getRowCount()==0){
                        ctr = true;
                        definirValoresParcelas();
                    } else {
                        cbParcelas.setSelectedIndex(tblPgto.getRowCount()-1);
                    }  */         
                    
                    
                    if (mostruario.getStatus()==2){
                        cbParcelas.setEnabled(false);
                        btnSalvar.setEnabled(false);
                        for (int i=0;i<6;i++){
                            listValor.get(i).setEditable(false);
                            listDataPgto.get(i).setEditable(false);
                            listPgtoRlzd.get(i).setEditable(false);
                            listPago.get(i).setEnabled(false);
                        }
                    }
                    
                    if (mostruario.getStatus()==1){
                        jTabbedPane1.setSelectedIndex(1);
                    }
                    
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage());
                this.dispose();
            }
        }
        
        else {
            this.dispose();
        }
        
        txtCodProduto.requestFocus();
        lbSaldoDevedor.setText(calcularSaldoDevedor());
        //tblPgto.getColumn("Status").setCellRenderer(createStatusColumnRenderer());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        bgImprimir = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbTitulo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbCodigo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbRevendedora = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbDataRetirada = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbDataAcerto = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbValorTotal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbNumPecas = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProdutosVendidos = new javax.swing.JTable();
        btnAdicionar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProdutosDevolvidos = new javax.swing.JTable();
        btnRemover = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCodProduto = new javax.swing.JTextField();
        lbPercentualVendido = new javax.swing.JLabel();
        lbPercentualDevolvido = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtPcsVendidas = new javax.swing.JTextField();
        txtVendaTotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtComissao = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtPercentualComissao = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtTotalAcerto = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtValor1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDataPgto1 = new javax.swing.JFormattedTextField();
        txtDataRlzd1 = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        ckPago1 = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        txtValor2 = new javax.swing.JTextField();
        txtDataPgto2 = new javax.swing.JFormattedTextField();
        txtDataRlzd2 = new javax.swing.JFormattedTextField();
        ckPago2 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        txtValor3 = new javax.swing.JTextField();
        txtDataPgto3 = new javax.swing.JFormattedTextField();
        txtDataRlzd3 = new javax.swing.JFormattedTextField();
        ckPago3 = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        ckPago4 = new javax.swing.JCheckBox();
        txtDataPgto4 = new javax.swing.JFormattedTextField();
        txtDataRlzd4 = new javax.swing.JFormattedTextField();
        txtValor4 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtDataPgto5 = new javax.swing.JFormattedTextField();
        txtValor5 = new javax.swing.JTextField();
        ckPago5 = new javax.swing.JCheckBox();
        txtDataRlzd5 = new javax.swing.JFormattedTextField();
        ckPago6 = new javax.swing.JCheckBox();
        txtDataPgto6 = new javax.swing.JFormattedTextField();
        txtDataRlzd6 = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        txtValor6 = new javax.swing.JTextField();
        cbParcelas = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        lbTituloSaldoDevedor = new javax.swing.JLabel();
        lbSaldoDevedor = new javax.swing.JLabel();
        lbFechado = new javax.swing.JLabel();
        rbVendidos = new javax.swing.JRadioButton();
        rbDevolvidos = new javax.swing.JRadioButton();
        btnImprimir = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fechar Mostruário");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTitulo.setFont(new java.awt.Font("Tahoma", 1, 24));
        lbTitulo.setForeground(new java.awt.Color(51, 51, 51));
        lbTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dollar_currency_sign.png"))); // NOI18N
        lbTitulo.setText("Fechar Mostruário");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Mostruário", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Código:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("Revendedora:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText("Data Retirada:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel10.setText("Data Acerto:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setText("Valor Total:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setText("Número de Peças:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCodigo)
                    .addComponent(lbDataRetirada)
                    .addComponent(lbValorTotal))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbRevendedora)
                    .addComponent(lbDataAcerto)
                    .addComponent(lbNumPecas))
                .addContainerGap(460, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lbCodigo)
                    .addComponent(jLabel6)
                    .addComponent(lbRevendedora))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lbDataRetirada)
                    .addComponent(jLabel10)
                    .addComponent(lbDataAcerto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lbValorTotal)
                    .addComponent(jLabel14)
                    .addComponent(lbNumPecas))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/disk.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        tblProdutosVendidos.setModel(new ProdutoMostruarioTableModel());
        tblProdutosVendidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblProdutosVendidosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblProdutosVendidos);

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrow_right.png"))); // NOI18N
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        tblProdutosDevolvidos.setModel(new ProdutoMostruarioTableModel());
        tblProdutosDevolvidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblProdutosDevolvidosKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblProdutosDevolvidos);

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrow_left.png"))); // NOI18N
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Vendidos:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Devolvidos:");

        txtCodProduto.setFont(new java.awt.Font("Tahoma", 0, 14));
        txtCodProduto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCodProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodProdutoKeyPressed(evt);
            }
        });

        lbPercentualVendido.setText("(100%)");

        lbPercentualDevolvido.setText("(0%)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdicionar)
                            .addComponent(btnRemover)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPercentualVendido)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPercentualDevolvido)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(txtCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(btnAdicionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemover))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(lbPercentualDevolvido)
                            .addComponent(jLabel2)
                            .addComponent(lbPercentualVendido))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Produtos", jPanel3);

        jLabel5.setText("Peças Vendidas:");

        txtPcsVendidas.setEditable(false);
        txtPcsVendidas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtVendaTotal.setEditable(false);
        txtVendaTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setText("Vendas Efetuadas:");

        jLabel9.setText("% Comissão:");

        txtComissao.setEditable(false);
        txtComissao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel11.setText("Total Comissão:");

        txtPercentualComissao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPercentualComissao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPercentualComissaoKeyReleased(evt);
            }
        });

        jLabel13.setText("Total Acerto:");

        txtTotalAcerto.setEditable(false);
        txtTotalAcerto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel1.setText("Parc.");

        jLabel15.setText("1.");

        jLabel17.setText("Valor");

        txtValor1.setPreferredSize(new java.awt.Dimension(60, 20));
        txtValor1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValor1CaretUpdate(evt);
            }
        });

        jLabel18.setText("Data Pagamento");

        try {
            txtDataPgto1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtDataRlzd1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel19.setText("Data Realizado");

        ckPago1.setText("Pago");
        ckPago1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPago1ActionPerformed(evt);
            }
        });

        jLabel20.setText("2.");

        txtValor2.setPreferredSize(new java.awt.Dimension(60, 20));
        txtValor2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValor2CaretUpdate(evt);
            }
        });

        try {
            txtDataPgto2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtDataRlzd2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        ckPago2.setText("Pago");
        ckPago2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPago2ActionPerformed(evt);
            }
        });

        jLabel21.setText("3.");

        txtValor3.setPreferredSize(new java.awt.Dimension(60, 20));
        txtValor3.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValor3CaretUpdate(evt);
            }
        });

        try {
            txtDataPgto3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtDataRlzd3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        ckPago3.setText("Pago");
        ckPago3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPago3ActionPerformed(evt);
            }
        });

        jLabel22.setText("4.");

        ckPago4.setText("Pago");
        ckPago4.setEnabled(false);
        ckPago4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPago4ActionPerformed(evt);
            }
        });

        try {
            txtDataPgto4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataPgto4.setEnabled(false);

        try {
            txtDataRlzd4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataRlzd4.setEnabled(false);

        txtValor4.setEnabled(false);
        txtValor4.setPreferredSize(new java.awt.Dimension(60, 20));
        txtValor4.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValor4CaretUpdate(evt);
            }
        });

        jLabel23.setText("5.");

        try {
            txtDataPgto5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataPgto5.setEnabled(false);

        txtValor5.setEnabled(false);
        txtValor5.setPreferredSize(new java.awt.Dimension(60, 20));
        txtValor5.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValor5CaretUpdate(evt);
            }
        });

        ckPago5.setText("Pago");
        ckPago5.setEnabled(false);
        ckPago5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPago5ActionPerformed(evt);
            }
        });

        try {
            txtDataRlzd5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataRlzd5.setEnabled(false);

        ckPago6.setText("Pago");
        ckPago6.setEnabled(false);
        ckPago6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPago6ActionPerformed(evt);
            }
        });

        try {
            txtDataPgto6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataPgto6.setEnabled(false);

        try {
            txtDataRlzd6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataRlzd6.setEnabled(false);

        jLabel24.setText("6.");

        txtValor6.setEnabled(false);
        txtValor6.setPreferredSize(new java.awt.Dimension(60, 20));
        txtValor6.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValor6CaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtValor1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtValor2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDataPgto1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtDataPgto2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataRlzd1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckPago1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataRlzd2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckPago2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataRlzd3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckPago3))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataRlzd4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckPago4))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataRlzd5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckPago5))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataRlzd6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckPago6)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtValor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataRlzd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckPago1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtValor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataRlzd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckPago2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtValor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataRlzd3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckPago3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtValor4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataRlzd4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckPago4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtValor5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataRlzd5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckPago5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtValor6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataPgto6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataRlzd6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckPago6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbParcelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1x", "2x", "3x", "4x", "5x", "6x" }));
        cbParcelas.setSelectedIndex(2);
        cbParcelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbParcelasActionPerformed(evt);
            }
        });

        jLabel16.setText("Parcelas:");

        lbTituloSaldoDevedor.setText("Saldo devedor:");

        lbSaldoDevedor.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPercentualComissao)
                                    .addComponent(txtPcsVendidas, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel11)))
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtVendaTotal)
                            .addComponent(txtTotalAcerto)
                            .addComponent(txtComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbTituloSaldoDevedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbSaldoDevedor)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPcsVendidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtPercentualComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtVendaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtTotalAcerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTituloSaldoDevedor)
                    .addComponent(lbSaldoDevedor))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Acerto", jPanel4);

        lbFechado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock_unlock.png"))); // NOI18N
        lbFechado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbFechado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbFechadoMouseClicked(evt);
            }
        });

        bgImprimir.add(rbVendidos);
        rbVendidos.setSelected(true);
        rbVendidos.setText("Vendidos");

        bgImprimir.add(rbDevolvidos);
        rbDevolvidos.setText("Devolvidos");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer.png"))); // NOI18N
        btnImprimir.setText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 394, Short.MAX_VALUE)
                        .addComponent(lbFechado))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbVendidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbDevolvidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 269, Short.MAX_VALUE)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbTitulo)
                    .addComponent(lbFechado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalvar)
                        .addComponent(rbVendidos)
                        .addComponent(rbDevolvidos)
                        .addComponent(btnImprimir))
                    .addComponent(btnCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private String calcularSaldoDevedor(){
        
        List<PagamentoMostruario> pagamento = recuperarPagamento(null);
        float pago = 0f;
        
        for (PagamentoMostruario p : pagamento){
            if (!p.isPago()){
                pago += p.getValorPgto();
            }
        }
        return NumberFormat.getCurrencyInstance().format(pago);
    }
    
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fechar esta janela?")==JOptionPane.YES_OPTION){
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        if (tblProdutosVendidos.getSelectedColumn()!=-1&&tblProdutosVendidos.getSelectedRow()!=-1){           
            try {
                Produto produto = new Produto();
                produto = (getModel(modelVendido, tblProdutosVendidos).getProduto(tblProdutosVendidos.getSelectedRow()));                            
                addProduto(produto, modelDevolvido, tblProdutosDevolvidos);
                getModel(modelVendido, tblProdutosVendidos).removeProduto(tblProdutosVendidos.getSelectedRow());
                
                if (!locked&&status>0){
                    if (produto.getIcVendido()==1)
                        pDevolvidoLocked.add(produto);
                    pVendidoLocked.remove(produto);
                }
                
                valorTotalMostruario -= produto.getValorSaida();
                atualizarDadosTela();
            } catch (Exception e){
                
            }
        }
        
        txtCodProduto.requestFocus();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        if (tblProdutosDevolvidos.getSelectedColumn()!=-1&&tblProdutosDevolvidos.getSelectedRow()!=-1){           
            try {
                Produto produto = new Produto();
                produto = (getModel(modelDevolvido, tblProdutosDevolvidos).getProduto(tblProdutosDevolvidos.getSelectedRow()));                            
                addProduto(produto, modelVendido, tblProdutosVendidos);
                getModel(modelDevolvido, tblProdutosDevolvidos).removeProduto(tblProdutosDevolvidos.getSelectedRow());
                
                if (!locked&&status>0){
                    if (produto.getIcVendido()==0)
                        pVendidoLocked.add(produto);
                    pDevolvidoLocked.remove(produto);
                }
                
                valorTotalMostruario += produto.getValorSaida();
                atualizarDadosTela();
            } catch (Exception e){
                
            }
        }
        
        txtCodProduto.requestFocus();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void txtCodProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProdutoKeyPressed
        if (evt.getKeyCode()==10){
            try {
                String cod;
                cod = txtCodProduto.getText().toUpperCase();
                
                Produto produto = new Produto();
                int index =
                        ((ProdutoMostruarioTableModel)
                        tblProdutosVendidos.getModel()).getProduto(cod);
                
                if (index!=-1){
                    produto = (getModel(modelVendido, tblProdutosVendidos).getProduto(index));
                    if (produto!=null){
                        addProduto(produto, modelDevolvido, tblProdutosDevolvidos);
                        getModel(modelVendido, tblProdutosVendidos).removeProduto(index);
                        
                        if (!locked&&status>0){
                            if (produto.getIcVendido()==1)
                                pDevolvidoLocked.add(produto);
                            pVendidoLocked.remove(produto);
                        }
                
                        valorTotalMostruario -= produto.getValorSaida();
                        atualizarDadosTela();
                    }
                }
                
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e);
            } finally {
                txtCodProduto.setText("");
            }
        }
    }//GEN-LAST:event_txtCodProdutoKeyPressed

    private void txtPercentualComissaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPercentualComissaoKeyReleased
        try {
            String str = txtPercentualComissao.getText();
            if (str.isEmpty()) str = "0";
            txtComissao.setText(NumberFormat.getCurrencyInstance().format(valorTotalMostruario * (Float.parseFloat(str.replace(",", "."))/100)));
            txtTotalAcerto.setText(NumberFormat.getCurrencyInstance().format(valorTotalMostruario - (valorTotalMostruario * (Float.parseFloat(str.replace(",", "."))/100))));
            
            //definirValoresParcelas();
        } catch (Exception e){
            txtPercentualComissao.setText("");
        }
    }//GEN-LAST:event_txtPercentualComissaoKeyReleased

    private List<PagamentoMostruario> recuperarPagamento(Mostruario mostruario){
        List<PagamentoMostruario> lpm = new ArrayList<PagamentoMostruario>();
        for (int i=0;i<=cbParcelas.getSelectedIndex();i++){
            PagamentoMostruario pm = new PagamentoMostruario();
            pm.setMostruario(mostruario);
            pm.setDataPrevisao(listDataPgto.get(i).getText());
            pm.setDataRealizado(Horus.isValidDate(listPgtoRlzd.get(i).getText())&&listPago.get(i).isSelected()?listPgtoRlzd.get(i).getText():null);
            pm.setValorPgto(listValor.get(i).getText().isEmpty()?0:Float.parseFloat(listValor.get(i).getText().replaceAll(",", ".")));
            pm.setPago(listPago.get(i).isSelected());
            lpm.add(pm);
        }
        return lpm;
    }
    
    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            //tblPgto.removeEditor();
            String msg;
            if (status==0)
                msg = "Confirmar fechamento do mostruário?";
            else
                msg = "Salvar alterações?";
            
            if (JOptionPane.showConfirmDialog(this, msg)==JOptionPane.YES_OPTION){
            List<Produto> produtosVendidos;
            List<Produto> produtosDevolvidos;
            List<PagamentoMostruario> pagamento;

            produtosVendidos = getModel(modelVendido, tblProdutosVendidos).getTodosProdutos();
            produtosDevolvidos = getModel(modelDevolvido, tblProdutosDevolvidos).getTodosProdutos();

            Mostruario mostruario = new Mostruario();

            mostruario.setId(Integer.parseInt(lbCodigo.getText()));
            mostruario.setQtdItensFechamento(Integer.parseInt(txtPcsVendidas.getText()));
            mostruario.setValorFechamento(valorTotalMostruario);
            mostruario.setPercentualComissaoFechamento(Float.parseFloat(txtPercentualComissao.getText()));
            
            pagamento = recuperarPagamento(mostruario);
            
            mostruario.setPagamento(pagamento);

            if (status==0)
                ManterMostruarioController.getInstance().fecharMostruario(mostruario, produtosVendidos, produtosDevolvidos);
            else {
                ManterMostruarioController.getInstance().atualizarpagamento(mostruario);
                if (!locked&&status>0)
                    ManterMostruarioController.getInstance().editarMostruario(mostruario, pVendidoLocked, pDevolvidoLocked);
            }
            
            this.dispose();
            if (status==0)
                JOptionPane.showMessageDialog(null, "O mostruário foi fechado com sucesso!");
            else
                JOptionPane.showMessageDialog(null, "O mostruário foi salvo com sucesso!");
            }

        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao fechar mostruário.");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void definirValoresParcelas(){
        int parcelas = cbParcelas.getSelectedIndex()+1;
        float valorParcela;
        valorParcela = (valorTotalMostruario - (valorTotalMostruario * (Float.parseFloat(txtPercentualComissao.getText().replace(",", "."))/100))) / parcelas;

        List<PagamentoMostruario> parcelasPgto = new ArrayList<PagamentoMostruario>();
        for (int i=0;i<parcelas;i++){
            PagamentoMostruario pgto = new PagamentoMostruario();
            pgto.setValorPgto(Math.round(valorParcela*100.0)/100.0);
            //pgto.setDataPrevisao(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
            pgto.setPago(false);
            parcelasPgto.add(pgto);
        }
        /*getPgtoModel().limpar();
        getPgtoModel().addListaDePagamentoMostruario(parcelasPgto);*/
    }
    
    private void cbParcelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbParcelasActionPerformed
        /*if (ctr)
            definirValoresParcelas();
        else
            ctr = true;
         * 
         */
        int parcelas = cbParcelas.getSelectedIndex();
        boolean icAtivo = true;
        for (int i=0;i<6;i++){
            //listValor.get(i).setText(null);
            listValor.get(i).setEnabled(icAtivo);
            
            //listDataPgto.get(i).setText(null);
            listDataPgto.get(i).setEnabled(icAtivo);
            
            //listPgtoRlzd.get(i).setText(null);
            listPgtoRlzd.get(i).setEnabled(icAtivo);
            
            //listPago.get(i).setSelected(false);
            listPago.get(i).setEnabled(icAtivo);
            if (i==parcelas) icAtivo = false;            
        }
    }//GEN-LAST:event_cbParcelasActionPerformed

    private void preencherDataFechamento(JFormattedTextField jftf, JCheckBox jck){
        lbSaldoDevedor.setText(calcularSaldoDevedor());
                
        if (!jck.isSelected())
            jftf.setText("");        
        else if (!Horus.isValidDate(jftf.getText()))
            jftf.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        
        
            
    }
    
    private void ckPago1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPago1ActionPerformed
        preencherDataFechamento(txtDataRlzd1, ckPago1);
    }//GEN-LAST:event_ckPago1ActionPerformed

    private void ckPago2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPago2ActionPerformed
        preencherDataFechamento(txtDataRlzd2, ckPago2);
    }//GEN-LAST:event_ckPago2ActionPerformed

    private void ckPago3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPago3ActionPerformed
        preencherDataFechamento(txtDataRlzd3, ckPago3);
    }//GEN-LAST:event_ckPago3ActionPerformed

    private void ckPago4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPago4ActionPerformed
        preencherDataFechamento(txtDataRlzd4, ckPago4);
    }//GEN-LAST:event_ckPago4ActionPerformed

    private void ckPago5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPago5ActionPerformed
        preencherDataFechamento(txtDataRlzd5, ckPago5);
    }//GEN-LAST:event_ckPago5ActionPerformed

    private void ckPago6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPago6ActionPerformed
        preencherDataFechamento(txtDataRlzd6, ckPago6);
    }//GEN-LAST:event_ckPago6ActionPerformed

    private void tblProdutosVendidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProdutosVendidosKeyReleased
        if (evt.getKeyCode()==123){
            FrmBuscaLista frmBusca = new FrmBuscaLista(null, true, tblProdutosVendidos.getModel());
            frmBusca.setVisible(true);
            
            if (frmBusca.getModelRow()!=-1){
                //tblProdutosVendidos.setRowSelectionInterval(frmBusca.getModelRow(), frmBusca.getModelRow());
                tblProdutosVendidos.changeSelection(frmBusca.getModelRow(), 0, false, false);
            }
        }
    }//GEN-LAST:event_tblProdutosVendidosKeyReleased

    private void tblProdutosDevolvidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProdutosDevolvidosKeyReleased
        if (evt.getKeyCode()==123){
            FrmBuscaLista frmBusca = new FrmBuscaLista(null, true, tblProdutosDevolvidos.getModel());
            frmBusca.setVisible(true);
            
            if (frmBusca.getModelRow()!=-1){
                //tblProdutosDevolvidos.setRowSelectionInterval(frmBusca.getModelRow(), frmBusca.getModelRow());
                tblProdutosDevolvidos.changeSelection(frmBusca.getModelRow(), 0, false, false);
            }
        }
    }//GEN-LAST:event_tblProdutosDevolvidosKeyReleased

    private void lbFechadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbFechadoMouseClicked
        if (locked) {
            locked = false;
            
            pVendidoLocked = new ArrayList<Produto>();
            pDevolvidoLocked = new ArrayList<Produto>();
            
            txtCodProduto.setEnabled(true);
            btnAdicionar.setEnabled(true);
            btnRemover.setEnabled(true);
            btnSalvar.setEnabled(true);
            txtPercentualComissao.setEditable(true);
            cbParcelas.setEnabled(true);

            for (int i=0;i<6;i++){
                listValor.get(i).setEditable(true);
                listDataPgto.get(i).setEditable(true);
                listPgtoRlzd.get(i).setEditable(true);
            }
            for (int i=0;i<listPgto.size();i++){
                listValor.get(i).setEnabled(true);
                listDataPgto.get(i).setEnabled(true);
                listPgtoRlzd.get(i).setEnabled(true);
                listPago.get(i).setEnabled(true);
            }
                        
            lbFechado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock_unlock.png"))); // NOI18N
            lbFechado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));                                                    

        }
    }//GEN-LAST:event_lbFechadoMouseClicked

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        Mostruario mostruario = new Mostruario();
        mostruario.setId(Integer.parseInt(lbCodigo.getText()));
        Revendedora revendedora = new Revendedora();
        revendedora.setNome(lbRevendedora.getText());
        mostruario.setRevendedora(revendedora);
        
        if (rbVendidos.isSelected())
            mostruario.setProdutos(getModel(modelVendido, tblProdutosVendidos).getTodosProdutos());
        else
            mostruario.setProdutos(getModel(modelDevolvido, tblProdutosDevolvidos).getTodosProdutos());
            
        ImprimirRomaneio2 ir = new ImprimirRomaneio2(null, true, mostruario);
        ir.setVisible(true);
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtValor1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValor1CaretUpdate
        lbSaldoDevedor.setText(calcularSaldoDevedor());
    }//GEN-LAST:event_txtValor1CaretUpdate

    private void txtValor2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValor2CaretUpdate
        lbSaldoDevedor.setText(calcularSaldoDevedor());
    }//GEN-LAST:event_txtValor2CaretUpdate

    private void txtValor3CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValor3CaretUpdate
        lbSaldoDevedor.setText(calcularSaldoDevedor());
    }//GEN-LAST:event_txtValor3CaretUpdate

    private void txtValor4CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValor4CaretUpdate
        lbSaldoDevedor.setText(calcularSaldoDevedor());
    }//GEN-LAST:event_txtValor4CaretUpdate

    private void txtValor5CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValor5CaretUpdate
        lbSaldoDevedor.setText(calcularSaldoDevedor());
    }//GEN-LAST:event_txtValor5CaretUpdate

    private void txtValor6CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValor6CaretUpdate
        lbSaldoDevedor.setText(calcularSaldoDevedor());
    }//GEN-LAST:event_txtValor6CaretUpdate

    private void addProduto(Produto produto, ProdutoMostruarioTableModel model, JTable tbl) throws Exception{
        getModel(model, tbl).addProduto(produto);
        tbl.changeSelection(getModel(model, tbl).getRowCount()-1,0,false,false);
    }
    
    private void addProdutos(List<Produto> produtos, ProdutoMostruarioTableModel model, JTable tbl){
        try{
            getModel(model, tbl).limpar();
            getModel(model, tbl).addListaDeProdutos(produtos);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }    
    
    private JTable getTblProduto(JTable tbl) {
            if (tbl == null) {
                    tbl = new JTable();
                    tbl.setModel(new ProdutoMostruarioTableModel());
            }
            return tbl;
    }
    
    private ProdutoMostruarioTableModel getModel(ProdutoMostruarioTableModel model, JTable tbl) {
            if (model == null) {
                    model = (ProdutoMostruarioTableModel) getTblProduto(tbl).getModel();
            }
            return model;
    }
    
    private void atualizarDadosTela(){
        try {
            double percVendido = tblProdutosVendidos.getRowCount()/percentVendido;
            double percDevol = tblProdutosDevolvidos.getRowCount()/percentVendido;

            lbPercentualVendido.setText("("+NumberFormat.getPercentInstance().format(percVendido)+")");
            lbPercentualDevolvido.setText("("+NumberFormat.getPercentInstance().format(percDevol)+")");
            txtPcsVendidas.setText(String.valueOf(tblProdutosVendidos.getRowCount()));
            txtVendaTotal.setText(NumberFormat.getCurrencyInstance().format(valorTotalMostruario));
            txtComissao.setText(NumberFormat.getCurrencyInstance().format(valorTotalMostruario * (Float.parseFloat(txtPercentualComissao.getText().replace(",", "."))/100)));
            txtTotalAcerto.setText(NumberFormat.getCurrencyInstance().format(valorTotalMostruario - (valorTotalMostruario * (Float.parseFloat(txtPercentualComissao.getText().replace(",", "."))/100))));
            
            //definirValoresParcelas();
            
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
    
    
    /*
    private void addPagamento(PagamentoMostruario pagamento) throws Exception{
        getPgtoModel().addPagamentoMostruario(pagamento);
    }
    
    private void addPagamentos(List<PagamentoMostruario> listaPgtos){
        try{
            getPgtoModel().limpar();
            getPgtoModel().addListaDePagamentoMostruario(listaPgtos);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private JTable getTblPagamento() {
            if (tblPgto == null) {
                    tblPgto = new JTable();
                    tblPgto.setModel(new PagamentoMostruarioTableModel());
            }
            return tblPgto;
    }

    private PagamentoMostruarioTableModel getPgtoModel() {
            if (modelPagamento == null) {
                    modelPagamento = (PagamentoMostruarioTableModel) getTblPagamento().getModel();
            }
            return modelPagamento;
    }    
    */
    
    protected TableCellRenderer createStatusColumnRenderer() {
        return new StatusColumnRenderer();
    }
    
    protected static class StatusColumnRenderer extends DefaultTableCellRenderer {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {

            super.getTableCellRendererComponent(table, value, isSelected,
                                                hasFocus, row, column);

            setText("");
            setHorizontalAlignment(SwingConstants.CENTER);

            /* set the icon based on the passed status */
            boolean status = (Boolean)value;
            setIcon(status ? ativo : inativo);

            return this;
        }
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmFecharMostruario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmFecharMostruario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmFecharMostruario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmFecharMostruario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                FrmFecharMostruario dialog = new FrmFecharMostruario(new javax.swing.JFrame(), true, null, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgImprimir;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cbParcelas;
    private javax.swing.JCheckBox ckPago1;
    private javax.swing.JCheckBox ckPago2;
    private javax.swing.JCheckBox ckPago3;
    private javax.swing.JCheckBox ckPago4;
    private javax.swing.JCheckBox ckPago5;
    private javax.swing.JCheckBox ckPago6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbCodigo;
    private javax.swing.JLabel lbDataAcerto;
    private javax.swing.JLabel lbDataRetirada;
    private javax.swing.JLabel lbFechado;
    private javax.swing.JLabel lbNumPecas;
    private javax.swing.JLabel lbPercentualDevolvido;
    private javax.swing.JLabel lbPercentualVendido;
    private javax.swing.JLabel lbRevendedora;
    private javax.swing.JLabel lbSaldoDevedor;
    private javax.swing.JLabel lbTitulo;
    private javax.swing.JLabel lbTituloSaldoDevedor;
    private javax.swing.JLabel lbValorTotal;
    private javax.swing.JRadioButton rbDevolvidos;
    private javax.swing.JRadioButton rbVendidos;
    private javax.swing.JTable tblProdutosDevolvidos;
    private javax.swing.JTable tblProdutosVendidos;
    private javax.swing.JTextField txtCodProduto;
    private javax.swing.JTextField txtComissao;
    private javax.swing.JFormattedTextField txtDataPgto1;
    private javax.swing.JFormattedTextField txtDataPgto2;
    private javax.swing.JFormattedTextField txtDataPgto3;
    private javax.swing.JFormattedTextField txtDataPgto4;
    private javax.swing.JFormattedTextField txtDataPgto5;
    private javax.swing.JFormattedTextField txtDataPgto6;
    private javax.swing.JFormattedTextField txtDataRlzd1;
    private javax.swing.JFormattedTextField txtDataRlzd2;
    private javax.swing.JFormattedTextField txtDataRlzd3;
    private javax.swing.JFormattedTextField txtDataRlzd4;
    private javax.swing.JFormattedTextField txtDataRlzd5;
    private javax.swing.JFormattedTextField txtDataRlzd6;
    private javax.swing.JTextField txtPcsVendidas;
    private javax.swing.JTextField txtPercentualComissao;
    private javax.swing.JTextField txtTotalAcerto;
    private javax.swing.JTextField txtValor1;
    private javax.swing.JTextField txtValor2;
    private javax.swing.JTextField txtValor3;
    private javax.swing.JTextField txtValor4;
    private javax.swing.JTextField txtValor5;
    private javax.swing.JTextField txtValor6;
    private javax.swing.JTextField txtVendaTotal;
    // End of variables declaration//GEN-END:variables
}
