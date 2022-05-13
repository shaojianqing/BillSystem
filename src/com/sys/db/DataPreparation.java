package com.sys.db;

import java.util.Random;
import java.util.UUID;

public class DataPreparation {
	
	static String sqlFormat = "INSERT INTO order_info(order_id, product_id, product_name, unit_price, currency, quantity, service_fee, consumer_id, consumer_name, merchant_id, merchant_name, is_valid, gmt_create, gmt_modified)"
			+ "VALUES('%s', '%s', '%s', '%f', '%s', '%d', '%f', '%s', '%s', '%s', '%s', '1', now(), now());";

	static String[] productNames = new String[]{"Sam Piano","Apple iphone","Dell Computer","Apple ipad","intel Central Processor", "HIK Camera", "ARM Chipset", "Microsoft Windows XP", "Apple Mac OS X", "MacBook Pro"};
	
	static String[] consumerNames = new String[]{"Lily Rome","Lucy Turing","Kuko Sam", "Tony Kruh", "Herry Mongo", "Oliver Twist", "Macky Jams", "Davids Hynbg"};
	
	static String[] merchantNames = new String[]{"Bank of America", "Google Inc", "Alibaba Inc", "CITI Bank", "China Energy Inc", "Zhejiang Province Inc", "China Unicom Inc", "Eran Energy Inc", "HSBC Bank"};
	
	static String[] currencyNames = new String[]{"CNY", "USD", "KRW", "EUR", "MYR", "SGD", "GBP"};
	
	public static void main(String[] args) {
		
		for (int i=0;i<productNames.length;++i) {
			String productName = productNames[i];
			for (int j=0;j<consumerNames.length;++j) {
				String consumerName = consumerNames[j];
				for (int k=0;k<merchantNames.length;++k) {
					String merchantName = merchantNames[k];
					for (int h=0;h<currencyNames.length;++h) {
						String currencyName = currencyNames[h];
						String sql = String.format(sqlFormat, generateUUID(), generateUUID(), productName, generateDouble(), currencyName, 
								generateInt(), generateDouble(), generateUUID(), consumerName, generateUUID(), merchantName);
						System.out.println(sql);
					}
				}
			}
		}
	}

	private static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	private static int generateInt() {
		return (int)(Math.random()*100);
	}
	
	private static double generateDouble() {
		return new Random().nextDouble()*1000;
	}
}
