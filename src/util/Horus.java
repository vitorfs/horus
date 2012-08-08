package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class Horus {
    
    private Horus(){
        
    }
    
    public static String zeroFill(String value, int range){
        String zerofill="";
        for (int i=value.length();i<range;i++,zerofill+="0");
        return zerofill+value;        
    }       
    
    public static String formataTelefone(String telefone){
        if (telefone==null||telefone.length()!=10)
            return null;
        else
            return ("("+telefone.substring(0, 2)+") "+telefone.substring(2, 6)+"-"+telefone.substring(6));
    }
    
    public static void atualizarComboBox(javax.swing.JComboBox combo, List<?> elementos){
        combo.removeAllItems();
        combo.addItem(null);
        if (elementos != null){
            for (int i=0;i<elementos.size();i++){
                combo.addItem(elementos.get(i));
            }
        }
    }
    
    public static Integer retornaNumRegs(Object o){
        if (o instanceof String) {  
            try {  
                return Integer.parseInt(o.toString());  
            } catch (NumberFormatException e) {  
                return Integer.MAX_VALUE;  
            }  
        } else if (o instanceof Number) {  
            return ((Number) o).intValue();  
        }  
        return 0;  
    }
    
    public static Integer defineNumeroPaginas(int count, int regs){
        if (regs==0||count==0) 
            return 1;

        double paginas = count/regs;

        if (count%regs==0)
            return (int) Math.floor(paginas);
        else
            return (int) (Math.floor(paginas)+1);
    }
    
    public static boolean isValidDate(String inDate){
        if (inDate == null)
            return false;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        if (inDate.trim().length()!=dateFormat.toPattern().length())
            return false;
        
        dateFormat.setLenient(false);
        
        try{
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe){
            return false;
        }
        return true;
    }
    
    public static boolean isBeforeToday(String inDate){
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
            Date data = (Date)formatter.parse(inDate);                   

            if (data.before(new Date())){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }        
    }
    
}
