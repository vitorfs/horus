package print;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import controller.ManterMostruarioController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import javax.imageio.ImageIO;
import model.Mostruario;
import tablemodel.ProdutoMostruarioTableModel;

/**
 * Demo of the basic features of {@code JTable} printing.
 * Allows the user to configure a couple of options and print
 * a table of student grades.
 * <p>
 * Requires the following other files:
 * <ul>
 *     <li>images/passed.png
 *     <li>images/failed.png
 * </ul>
 *
 * @author Shannon Hickey
 */
public class ImprimirRomaneio2 extends JDialog {

    /* UI Components */
    private JPanel contentPane;
    private JLabel lbRomaneio;
    private JTable tblRomaneio;
    private JScrollPane scroll;
    private JCheckBox showPrintDialogBox;
    private JCheckBox interactiveBox;
    private JCheckBox fitWidthBox;
    private JButton printButton;
    private ProdutoMostruarioTableModel model;

    /* Protected so that they can be modified/disabled by subclasses */
    protected JCheckBox headerBox;
    protected JCheckBox footerBox;
    protected JTextField headerField;
    protected JTextField footerField;

    /**
     * Constructs an instance of the demo.
     */
    public ImprimirRomaneio2(java.awt.Frame parent, boolean modal, Mostruario mostruario) {
        super(parent, modal);
        this.setTitle("Imprimir Produtos");
        //super("Imprimir Romaneio");

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResource("/images/printer.png"));
        } catch (IOException e) {
            
        }
        this.setIconImage(image);
        
        lbRomaneio = new JLabel("Revendedora: "+mostruario.getRevendedora().getNome());
        lbRomaneio.setFont(new Font("Dialog", Font.BOLD, 16));

        model = new ProdutoMostruarioTableModel();
        try {
            model.addListaDeProdutos(mostruario.getProdutos());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao carregar lista de produtos.");
        }
        
        tblRomaneio = createTable(model);
        tblRomaneio.setFillsViewportHeight(true);
        tblRomaneio.setRowHeight(24);

        scroll = new JScrollPane(tblRomaneio);

        String tooltipText;

        tooltipText = "Incluir cabeçalho";
        headerBox = new JCheckBox("Cabeçalho:", true);
        headerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                headerField.setEnabled(headerBox.isSelected());
            }
        });
        headerBox.setToolTipText(tooltipText);
        tooltipText = "Cabeçalho da Página (Utilize {0} para incluir o número da página)";
        headerField = new JTextField("Revendedora: "+mostruario.getRevendedora().getNome());
        headerField.setToolTipText(tooltipText);

        tooltipText = "Incluir rodapé";
        footerBox = new JCheckBox("Rodapé:", true);
        footerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                footerField.setEnabled(footerBox.isSelected());
            }
        });
        footerBox.setToolTipText(tooltipText);
        tooltipText = "Rodapé da Página (Utilize {0} para incluir o número da página)";
        footerField = new JTextField("Página {0}");
        footerField.setToolTipText(tooltipText);

        tooltipText = "Show the Print Dialog Before Printing";
        showPrintDialogBox = new JCheckBox("Show print dialog", true);
        showPrintDialogBox.setToolTipText(tooltipText);
        showPrintDialogBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!showPrintDialogBox.isSelected()) {
                    JOptionPane.showMessageDialog(
                        ImprimirRomaneio2.this,
                        "If the Print Dialog is not shown,"
                            + " the default printer is used.",
                        "Printing Message",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        
        tooltipText = "Keep the GUI Responsive and Show a Status Dialog During Printing";
        interactiveBox = new JCheckBox("Interactive (Show status dialog)", true);
        interactiveBox.setToolTipText(tooltipText);
        interactiveBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!interactiveBox.isSelected()) {
                    JOptionPane.showMessageDialog(
                        ImprimirRomaneio2.this,
                        "If non-interactive, the GUI is fully blocked"
                            + " during printing.",
                        "Printing Message",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        tooltipText = "Shrink the Table to Fit the Entire Width on a Page";
        fitWidthBox = new JCheckBox("Fit width to printed page", true);
        fitWidthBox.setToolTipText(tooltipText);

        tooltipText = "Imprimir Mostruário";
        printButton = new JButton("Imprimir");
        printButton.setToolTipText(tooltipText);
        
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                printGradesTable();
            }
        });

        contentPane = new JPanel();
        addComponentsToContentPane();
        setContentPane(contentPane);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
    }

    private void addComponentsToContentPane() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Opções de Impressão"));

        GroupLayout bottomPanelLayout = new GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(headerBox)
                    .addComponent(footerBox))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(footerField)
                    .addComponent(headerField, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(fitWidthBox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printButton))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(showPrintDialogBox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(interactiveBox)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBox)
                    .addComponent(headerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(interactiveBox)
                    .addComponent(showPrintDialogBox))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(footerBox)
                    .addComponent(footerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(fitWidthBox)
                    .addComponent(printButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(lbRomaneio)
                    .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbRomaneio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }

    protected JTable createTable(TableModel model) {
        return new JTable(model);
    }

    private void printGradesTable() {
        /* Fetch printing properties from the GUI components */

        MessageFormat header = null;
        
        /* if we should print a header */
        if (headerBox.isSelected()) {
            /* create a MessageFormat around the header text */
            header = new MessageFormat(headerField.getText());
            /*header = new MessageFormat[3];
            header[0] = new MessageFormat("Revendedora: Paula        Telefone: 1234-5678");
            header[1] = new MessageFormat("Retirada: 01/01/2012      Acerto: 30/01/2012");
            header[2] = new MessageFormat("Valor total: R$ 4000,00   Peças: 134");*/
        }

        MessageFormat footer = null;
        
        /* if we should print a footer */
        if (footerBox.isSelected()) {
            /* create a MessageFormat around the footer text */
            footer = new MessageFormat(footerField.getText());
        }

        boolean fitWidth = fitWidthBox.isSelected();
        boolean showPrintDialog = showPrintDialogBox.isSelected();
        boolean interactive = interactiveBox.isSelected();

        /* determine the print mode */
        JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
                                         : JTable.PrintMode.NORMAL;

        try {
            /* print the table */
            boolean complete = tblRomaneio.print(mode, header, footer,
                                                 showPrintDialog, null,
                                                 interactive, null);

            /* if printing completes */
            if (complete) {
                /* show a success message */
                JOptionPane.showMessageDialog(this,
                                              "Impressão Completa",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            } else {
                /* show a message indicating that printing was cancelled */
                JOptionPane.showMessageDialog(this,
                                              "Impressão Cancelada",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            /* Printing failed, report to the user */
            JOptionPane.showMessageDialog(this,
                                          "Impressão Falhou: " + pe.getMessage(),
                                          "Printing Result",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(final String[] args) {
        /* Schedule for the GUI to be created and shown on the EDT */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                /* Don't want bold fonts if we end up using metal */
                UIManager.put("swing.boldMetal", false);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                }
                new ImprimirRomaneio2(null, true, new Mostruario()).setVisible(true);
            }
        });
    }
}
