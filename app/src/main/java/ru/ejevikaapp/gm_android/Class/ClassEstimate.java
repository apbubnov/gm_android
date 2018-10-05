package ru.ejevikaapp.gm_android.Class;

/**
 * Created by Dmitry on 07.04.2018.
 */

public class ClassEstimate {
    String name;
    String count;
    String price;
    String totalPrice;

    public ClassEstimate(String _name, String _count, String _price, String _totalPrice){
        this.name = _name;
        this.count = _count;
        this.price = _price;
        this.totalPrice = _totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
