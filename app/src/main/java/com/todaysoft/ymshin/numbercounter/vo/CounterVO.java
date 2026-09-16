package com.todaysoft.ymshin.numbercounter.vo;

public class CounterVO {

    private int seq;
    private String name;
    private String value;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CounterVO{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
