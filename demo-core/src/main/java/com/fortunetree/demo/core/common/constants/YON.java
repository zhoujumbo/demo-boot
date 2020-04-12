package com.fortunetree.demo.core.common.constants;

public enum YON {
    NO(0, "否"),
    YES(1, "是");

    final int value;
    final String message;

    private YON(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }

    public String label(){
        return this.toString();
    }

}
