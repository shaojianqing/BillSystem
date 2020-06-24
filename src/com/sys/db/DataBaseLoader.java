package com.sys.db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DataBaseLoader {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		try {
			FileInputStream input = new FileInputStream("/Users/mashu/Downloads/database.txt");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();	
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			
			byte[] data = buffer.toString().getBytes("UTF-8");
			String sql = new String("");
			
			DBConnection.getInstance().executeUpdate(sql);
			
			System.out.print("DataBase Load successfully~");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
