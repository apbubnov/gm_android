package ru.ejevikaapp.authorization.Class;

public class Truby_class {
    String id;
    String calculation_id;
    String kol_vo;
    String diametr;

    public Truby_class(String _id, String _calculation_id, String _kol_vo, String _diametr){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.kol_vo = _kol_vo;
        this.diametr = _diametr;
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

    public String getDiametr() {
        return diametr;
    }

    public void setDiametr(String diametr) {
        this.diametr = diametr;
    }
}
