package com.meghaagarwal.foodorder;

public class Orders
{

    String username, itemname;
    public Orders(){}

    public Orders(String username, String itemname)
    {
        this.username = username;
        this.itemname= itemname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
