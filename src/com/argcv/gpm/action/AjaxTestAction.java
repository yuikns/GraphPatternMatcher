package com.argcv.gpm.action;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AjaxTestAction extends BaseAction {
    public class InfoValue {
        public String name;
        String msg;

        InfoValue(String name, String msg) {
            this.name = name;
            this.msg = msg;
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = -7934727957720882022L;

    private String name = null;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void loadData() throws Exception {
        // info = "hello !" + name;
        System.out.println("get : name :" + name);
        List<InfoValue> ivl = new ArrayList<InfoValue>();
        for (int i = 0; i < 20; i++) {
            ivl.add(new InfoValue(name + "@" + i, "hello times : " + i));
        }
        Gson g = new Gson();
        setStringToReturn(g.toJson(ivl));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
