package com.gwd.entity;


/**
* @Description:
* @Param:
* @return:
* @Author: ChenYu
* @Date: 2019/3/16
 */
public class User {

    private String name;
    private int weight;
    private String award;

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }


    @Override
    public String toString() {
        return name + ":" + award + ",";
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name, int weight) {
        this.name = name;
        this.weight = weight / 10;
    }

}
