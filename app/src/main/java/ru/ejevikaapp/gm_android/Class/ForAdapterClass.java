package ru.ejevikaapp.gm_android.Class;

public class ForAdapterClass {
    String id;
    String calculation_id;
    String count;
    String ftType;
    String sdType;
    String tdType;

    public ForAdapterClass(String _id, String _calculation_id, String _count, String _ftType, String _sdType,
                           String _tdType){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.count = _count;
        this.ftType = _ftType;
        this.sdType = _sdType;
        this.tdType = _tdType;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFtType() {
        return ftType;
    }

    public void setFtType(String ftType) {
        this.ftType = ftType;
    }

    public String getSdType() {
        return sdType;
    }

    public void setSdType(String sdType) {
        this.sdType = sdType;
    }

    public String getTdType() {
        return tdType;
    }

    public void setTdType(String tdType) {
        this.tdType = tdType;
    }
}
