package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 18.09.2017.
 */

public class Frag_g3_dogovor_class {
    String id;
    String number;
    String price;
    String address;
    String income;

    public Frag_g3_dogovor_class(String _id, String _number, String _address, String _price,  String _income){
        this.id= _id;
        this.number= _number;
        this.address = _address;
        this.price= _price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
