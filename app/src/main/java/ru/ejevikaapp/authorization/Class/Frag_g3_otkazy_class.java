package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 19.09.2017.
 */

public class Frag_g3_otkazy_class {
    String id;
    String number;
    String fio;
    String phone;

    public Frag_g3_otkazy_class(String _id, String _number, String _fio, String _phone){
        this.id= _id;
        this.number= _number;
        this.fio = _fio;
        this.phone= _phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
