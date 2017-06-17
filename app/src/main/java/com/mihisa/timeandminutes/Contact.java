package com.mihisa.timeandminutes;

import java.util.HashMap;

/**
 * Created by insight on 21.05.17.
 */

public class Contact extends HashMap<String, String> {

    public static final String TIME = "timeStamp";
    public static final String MINUTES = "minutes";

    // Конструктор с параметрами
    public Contact(String timeStamp, String minutes) {
        super();
        super.put(TIME, timeStamp);
        super.put(MINUTES, minutes);
    }
}
