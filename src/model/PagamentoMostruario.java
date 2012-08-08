package model;

import util.Horus;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public class PagamentoMostruario {
    private Integer id;
    private Mostruario mostruario;
    private String dataPrevisao;
    private String dataRealizado;
    private Float valorPgto;
    private boolean pago;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the mostruario
     */
    public Mostruario getMostruario() {
        return mostruario;
    }

    /**
     * @param mostruario the mostruario to set
     */
    public void setMostruario(Mostruario mostruario) {
        this.mostruario = mostruario;
    }

    /**
     * @return the dataPrevisao
     */
    public String getDataPrevisao() {
        return dataPrevisao;
    }

    /**
     * @param dataPrevisao the dataPrevisao to set
     */
    public void setDataPrevisao(String dataPrevisao) {
        if (Horus.isValidDate(dataPrevisao))
            this.dataPrevisao = dataPrevisao;
        else
            this.dataPrevisao = "";
    }
    
    public void setDataPrevisao(Object value){
        setDataPrevisao(value.toString());
    }

    /**
     * @return the dataRealizado
     */
    public String getDataRealizado() {
        return dataRealizado;
    }

    /**
     * @param dataRealizado the dataRealizado to set
     */
    public void setDataRealizado(String dataRealizado) {
        if (Horus.isValidDate(dataRealizado))
            this.dataRealizado = dataRealizado;
        else
            this.dataRealizado = "";
    }
    
    public void setDataRealizado(Object value) {
        setDataRealizado(value.toString());
    }

    /**
     * @return the valorPgto
     */
    public Float getValorPgto() {
        return valorPgto;
    }

    /**
     * @param valorPgto the valorPgto to set
     */
    public void setValorPgto(Float valorPgto) {
        this.valorPgto = valorPgto;
    }
    
    public void setValorPgto(Object value) {
        try {
            this.valorPgto = Float.parseFloat(value.toString());
        } catch (Exception e) {
            this.valorPgto = 0f;
        }
    }

    /**
     * @return the pago
     */
    public boolean isPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(boolean pago) {
        this.pago = pago;
    }
    
    public void setPago(Object pago){
        try {
            //System.out.println(pago);
            boolean aux;
            aux = (Boolean)pago;
            this.pago = aux;
        } catch (Exception e){
            this.pago = false;
            System.out.println(e);
        }
        
    }
}
