package main;

import java.sql.SQLException;
import java.sql.Timestamp;

import database.Mysql;

public class Test {
	static Timestamp now2;
	public static void main(String[] args){
		
		try {
			new Mysql().getData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
