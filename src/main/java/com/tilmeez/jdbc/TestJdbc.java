package com.tilmeez.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {

    public static void main(String[] args) {

        String jdbcUrl = "jdbc:postgresql://ec2-52-30-159-47.eu-west-1.compute.amazonaws.com:5432/d2j5mchnh8vql4";
        String user = "ouxptxqkhymyap";
        String pass = "b597e7d85a71150fb92ecdeeba9195877f2367dfd9ed04c0011db39400eabd4e";

        try {
            System.out.println("Connect to database: " + jdbcUrl);

            Connection myConn =
                    DriverManager.getConnection(jdbcUrl, user, pass);

            System.out.println("Connection successful!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
