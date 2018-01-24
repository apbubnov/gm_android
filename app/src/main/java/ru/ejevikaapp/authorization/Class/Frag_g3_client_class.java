package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 18.09.2017.
 */

public class Frag_g3_client_class {
    String id;
    String fio;
    String number;
    String date;

    public Frag_g3_client_class(String _id, String _fio, String _number, String _date){

        this.id = _id;
        this.fio = _fio;
        this.number = _number;
        this.date = _date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
