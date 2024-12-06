package com.springboot.Yazlab2;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@SpringBootApplication
public class Yazlab2Application {
 private static Connection conn=null;
 public static Connection getConnection() throws SQLException {
if(conn!=null){
	return conn;
}else{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url="jdbc:mysql://127.0.0.1:3306/spring_boot?useSSL=false";
	String user="root";
	String password="yigit123";
	try{
		Class.forName(driver);
		conn= DriverManager.getConnection(url,user,password);
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
}
return conn;
 }
	public static void main(String[] args) {
		SpringApplication.run(Yazlab2Application.class, args);
	}





}
