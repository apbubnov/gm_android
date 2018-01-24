package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 15.09.2017.
 */

public class Frag_g2_price_montazh_class {
    String id;
    String name;
    String price1;
    String price2;

    public Frag_g2_price_montazh_class(String _id, String _name, String _price1, String _price2){
        this.id= _id;
        this.name= _name;
        this.price1= _price1;
        this.price2= _price2;
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

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }
}
