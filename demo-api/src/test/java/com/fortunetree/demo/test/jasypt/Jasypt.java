package com.fortunetree.demo.test.jasypt;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @ClassName Jasypt
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/7/17
 * @Version 1.0
 */
public class Jasypt {

    /**
     *
     * ENC(6eaMh/RX5oXUVca9ignvtg==)
     * @param args
     */
    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("DkJLKFJIjilIUI887F9FF8f87F89f8");
        //要加密的数据（数据库的用户名或密码）
//        String username = textEncryptor.encrypt("root");
//        String password = textEncryptor.encrypt("root123");
//        System.out.println("username:"+username);
//        System.out.println("password:"+password);


    }

}
