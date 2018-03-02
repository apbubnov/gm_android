package ru.ejevikaapp.gm_android.Class;

/**
 * Created by Дмитрий on 23.10.2017.
 */

public class Other_comp_and_work_class {
    String id;
    String name;
    String price;
    String calculation_id;

    public Other_comp_and_work_class(String _id, String _calculation_id, String _name, String _price){
        this.id = _id;
        this.calculation_id = _calculation_id;
        this.name = _name;
        this.price = _price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCalculation_id() {
        return calculation_id;
    }

    public void setCalculation_id(String calculation_id) {
        this.calculation_id = calculation_id;
    }
}
