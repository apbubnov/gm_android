package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 20.09.2017.
 */

public class Frag_schedule_class {
    String id;
    String number;
    String date;
    String phone;
    String client;

    public Frag_schedule_class(String _id, String _number, String _date, String _phone,  String _client){
        this.id= _id;
        this.number= _number;
        this.date = _date;
        this.phone= _phone;
        this.client= _client;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
