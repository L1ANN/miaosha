package com.lian.miaosha_7.domain;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午10:59 2018/10/17
 * @Modified By:
 */
public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
