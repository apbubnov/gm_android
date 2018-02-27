package ru.ejevikaapp.authorization.Class;

/**
 * Created by Дмитрий on 22.02.2018.
 */

public class Extra_class {
    String id;
    String title;
    String value;

    public Extra_class(String _id, String _title, String _value){
        this.id = _id;
        this.title = _title;
        this.value = _value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}