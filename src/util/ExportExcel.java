package util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.JOptionPane;
import model.Mostruario;
import model.Produto;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class ExportExcel {
    
    public static void WriteFile(Mostruario m, List<Produto> lp){
        try{
          // Create file 
          FileWriter fstream = new FileWriter("romaneio.xls");
          BufferedWriter out = new BufferedWriter(fstream);
          out.write("<table>"
                  + "<tr>"
                  + "<td align=right colspan=2><b>Revendedora:</b></td><td>"+m.getRevendedora().getNome()+"</td>"
                  + "<td align=right><b>Telefone:</b></td><td>"+m.getRevendedora().getTelefone1()+"</td>"
                  + "</tr><tr>"
                  + "<td align=right colspan=2><b>Retirada:</b></td><td>"+m.getDataRetirada()+"</td>"
                  + "<td align=right><b>Acerto:</b></td><td>"+m.getDataAcerto()+"</td>"
                  + "</tr><tr>"
                  + "<td align=right colspan=2><b>Valor do Mostruário:</b></td><td>"+NumberFormat.getCurrencyInstance().format(m.getValorTotal())+"</td>"
                  + "<td align=right><b>Peças:</b></td><td>"+lp.size()+"</td>"
                  + "</tr></table>");
          
          out.write("<table border=1><tr>"
                  + "<th bgcolor=#a5a5a5>Nº</th>"
                  + "<th bgcolor=#a5a5a5>Código</th>"
                  + "<th colspan=2 bgcolor=#95b3d7>Descrição do Produto</th>"
                  + "<th bgcolor=#948b54>Preço</th></tr>");
          for (int i=0;i<lp.size();i++){
              out.write("<tr>"
                      + "<td>"+(i+1)+"</td>"
                      + "<td>"+lp.get(i).getCodigo() +"</td>"
                      + "<td colspan=2 bgcolor=#dbe5f1>"+lp.get(i).getNome() +"</td>"
                      + "<td bgcolor=#c5be97><b>"+NumberFormat.getCurrencyInstance().format(lp.get(i).getValorSaida()) +"</b></td>"
                      + "</tr>");
          }
          out.write("</table>");

          //Close the output stream
          out.close();
          } catch (FileNotFoundException e){
              JOptionPane.showMessageDialog(null, "Feche o arquivo romaneio.xls antes de prosseguir!");
          }
        catch (Exception e){//Catch exception if any
          System.err.println("Error: " + e.getMessage());
          }
      }        
    
     /*public static void main(String args[]){
         try {
          FileWriter fstream = new FileWriter("etiqueta.txt");
          BufferedWriter out = new BufferedWriter(fstream);         
          
          char stx = 2;
          char cr = 13;
          
          out.write(stx+"m"+cr);
          //out.write(stx+"M0180"+cr);
          out.write(stx+"L"+cr);
          out.write("D12"+cr);
          out.write("1A2104000050090BR2207"+cr);
          out.write("D11"+cr);
          out.write("191100000600240Anel Cravejado"+cr);
          out.write("191100000200265R$ 59,90"+cr);          
          out.write("Q0001"+cr);
          out.write("E"+cr);
          out.close();
          
          //Process proc = Runtime.getRuntime().exec("cmd /c start copy etiqueta.txt lpt1");
          Process proc = Runtime.getRuntime().exec("cmd /c start imprimirEtiqueta.exe");
         } catch (Exception e){
             System.out.println(e);
         }
     }    */
    }
    