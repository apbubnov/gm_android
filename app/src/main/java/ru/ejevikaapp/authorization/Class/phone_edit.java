package ru.ejevikaapp.authorization.Class;

import android.util.Log;

/**
 * Created by Дмитрий on 30.01.2018.
 */

public class phone_edit {

    public String edit(String phone){

        String str1 = phone.substring(0,2);
        String str2 = phone;
        if (str1.equals("7")){

        } else  if (str1.equals("+8") || str1.equals("+7")){
            str2 = phone.substring(2, phone.length());
            str2 = "7" + str2;
        } else {
            str2 = phone.substring(1, phone.length());
            str2 = "7" + str2;
        }

            return str2;
    }

}
