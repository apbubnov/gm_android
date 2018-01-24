package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 20.09.2017.
 */

public class Frag_brigade_class {
    String id;
    String name;
    String plus;
    String fine_prize;
    String residue;

    public Frag_brigade_class(String _id, String _name, String _plus, String _fine_prize,  String _residue){
        this.id= _id;
        this.name= _name;
        this.plus = _plus;
        this.fine_prize= _fine_prize;
        this.residue= _residue;
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

    public String getPlus() {
        return plus;
    }

    public void setPlus(String plus) {
        this.plus = plus;
    }

    public String getFine_prize() {
        return fine_prize;
    }

    public void setFine_prize(String fine_prize) {
        this.fine_prize = fine_prize;
    }

    public String getResidue() {
        return residue;
    }

    public void setResidue(String residue) {
        this.residue = residue;
    }
}
