package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 15.09.2017.
 */

public class Frag_g2_price_poloten_class {
    String id;
    String name;
    String textur;
    String price;

    public Frag_g2_price_poloten_class(String _id, String _name, String _textur, String _price){
        this.id= _id;
        this.name= _name;
        this.textur= _textur;
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

    public String getTextur() {
        return textur;
    }

    public void setTextur(String textur) {
        this.textur = textur;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
