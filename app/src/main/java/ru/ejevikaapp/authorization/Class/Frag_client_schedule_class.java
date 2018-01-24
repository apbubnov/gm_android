package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 20.09.2017.
 */

public class Frag_client_schedule_class {
    String id;
    String fio;
    String address;
    String id_client;
    String phone;

    public Frag_client_schedule_class(String _id, String _fio, String _address, String _id_client, String _phone){
        this.id= _id;
        this.fio= _fio;
        this.address= _address;
        this.id_client= _id_client;
        this.phone= _phone;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
