package com.airbook.airbook.modules.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dateformat1 {
   private String changedate;

    public String getChangedate() {
        return changedate;
    }

    public void setChangedate(String changedate) {
        this.changedate = changedate;
    }

    public void datetostring(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        changedate=sdf.format(date);
        setChangedate(changedate);
/*        System.out.println(sdf.format(date));*/
/*        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));
        sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        System.out.println(sdf.format(date));*/
    }
}
