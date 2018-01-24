package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 20.09.2017.
 */

public class Frag_add_brigade_class {
    String id;
    String name;
    String fio;
    String phone;

    public Frag_add_brigade_class(String _id, String _name, String _fio, String _phone){
        this.id= _id;
        this.name= _name;
        this.fio = _fio;
        this.phone= _phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
