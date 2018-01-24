package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 15.09.2017.
 */

public class Frag_g2_price_komplect_class {
    String id;
    String name;
    String ed_izm;
    String price;

    public Frag_g2_price_komplect_class(String _id, String _name, String _ed_izm, String _price){
        this.id= _id;
        this.name= _name;
        this.ed_izm= _ed_izm;
        this.price= _price;
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

    public String getEd_izm() {
        return ed_izm;
    }

    public void setEd_izm(String ed_izm) {
        this.ed_izm = ed_izm;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
