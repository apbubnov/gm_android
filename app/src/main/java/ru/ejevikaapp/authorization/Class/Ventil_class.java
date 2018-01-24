package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 13.09.2017.
 */

public class Ventil_class {
    String id;
    String calculation_id;
    String type;
    String razmer;
    String kol_vo;

    public Ventil_class(String _id, String _calculation_id,  String _kol_vo, String _type, String _razmer){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.kol_vo = _kol_vo;
        this.type = _type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRazmer() {
        return razmer;
    }

    public void setRazmer(String razmer) {
        this.razmer = razmer;
    }

    public String getKol_vo() {
        return kol_vo;
    }

    public void setKol_vo(String kol_vo) {
        this.kol_vo = kol_vo;
    }
}
