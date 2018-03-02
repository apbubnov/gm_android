package ru.ejevikaapp.gm_android.Class;

/**
 * Created by Дмитрий on 21.12.2017.
 */

public class Select_work {
    String id;
    String time;
    String addres;
    String name;
    String n5;

    public Select_work(String _id, String _time, String _addres, String _name, String _n5){
        this.id = _id;
        this.time = _time;
        this.addres = _addres;
        this.name = _name;
        this.n5 = _n5;

    }

    public String getN5() {
        return n5;
    }

    public void setN5(String n5) {
        this.n5 = n5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
