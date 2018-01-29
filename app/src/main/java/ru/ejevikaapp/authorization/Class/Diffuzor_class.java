package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 13.09.2017.
 */
public class Diffuzor_class {
    String id;
    String calculation_id;
    String kol_vo;
    String razmer;

    public Diffuzor_class(String _id, String _calculation_id, String _kol_vo, String _razmer){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.kol_vo = _kol_vo;
        this.razmer = _razmer;
    }

    public String getCalculation_id() {
        return calculation_id;
    }

    public void setCalculation_id(String calculation_id) {
        this.calculation_id = calculation_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKol_vo() {
        return kol_vo;
    }

    public void setKol_vo(String kol_vo) {
        this.kol_vo = kol_vo;
    }

    public String getRazmer() {
        return razmer;
    }

    public void setRazmer(String razmer) {
        this.razmer = razmer;
    }

}
