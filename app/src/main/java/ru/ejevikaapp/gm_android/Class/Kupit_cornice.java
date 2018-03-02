package ru.ejevikaapp.gm_android.Class;

/**
 * Created by Дмитрий on 12.10.2017.
 */

public class Kupit_cornice {
    String id;
    String calculation_id;
    String kol_vo;
    String type;
    String length;

    public Kupit_cornice(String _id, String _calculation_id, String _kol_vo, String _type, String _length){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.kol_vo = _kol_vo;
        this.type = _type;
        this.length = _length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalculation_id() {
        return calculation_id;
    }

    public void setCalculation_id(String calculation_id) {
        this.calculation_id = calculation_id;
    }

    public String getKol_vo() {
        return kol_vo;
    }

    public void setKol_vo(String kol_vo) {
        this.kol_vo = kol_vo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
