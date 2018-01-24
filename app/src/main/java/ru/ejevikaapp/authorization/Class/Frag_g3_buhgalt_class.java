package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 18.09.2017.
 */

public class Frag_g3_buhgalt_class {
    String id;
    String number;
    String fio;
    String income;

    public Frag_g3_buhgalt_class(String _id, String _number, String _fio, String _income){
        this.id= _id;
        this.number= _number;
        this.fio= _fio;
        this.income= _income;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
