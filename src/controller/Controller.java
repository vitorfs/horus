package controller;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public final class Controller {
    
    private Controller(){
        
    }
    
    public static String ajustaAtributo(String atributo){
        if (atributo!=null){
            atributo = atributo.replace("'","").trim();
            if (atributo.length()==0)
                return null;
            else
                return atributo;
        }
        return null;
    }

    public static String ajustaAtributo(String atributo, String regex){
        if (atributo!=null){
            atributo = atributo.replaceAll(regex,"").trim();
            if (atributo.length()==0)
                return null;
            else
                return atributo;
        }
        return null;
    }
    
}
