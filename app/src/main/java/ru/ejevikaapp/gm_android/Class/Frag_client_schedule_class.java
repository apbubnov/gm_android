package ru.ejevikaapp.gm_android.Class;

/**
 * Created by Дмитрий on 20.09.2017.
 */

public class Frag_client_schedule_class {
    String id;
    String fio;
    String address;
    String id_client;
    String status;
    String create;

    public Frag_client_schedule_class(String _id, String _fio, String _address, String _id_client,
                                      String _status, String _create){
        this.id= _id;
        this.fio= _fio;
        this.address= _address;
        this.id_client= _id_client;
        this.status= _status;
        this.create = _create;
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

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }
}
