package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 13.09.2017.
 */

public class Kupit_Svetlin_class {
    String id;
    String calculation_id;
    String kol_vo;
    String color;
    String lampa;

    public Kupit_Svetlin_class(String _id, String _calculation_id, String _kol_vo, String _color, String _lampa){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.kol_vo = _kol_vo;
        this.color = _color;
        this.lampa = _lampa;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLampa() {
        return lampa;
    }

    public void setLampa(String lampa) {
        this.lampa = lampa;
    }
}
