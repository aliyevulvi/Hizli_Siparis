package com.hizlisiparis.product;

import java.util.*;
import java.time.*;
import com.hizlisiparis.database.DB;

public class Product {
	private String productName;
	private int productPrice;
	private String productCategory;
	private transient int productAmount;
	private transient String productExtraInfo;
	private static ArrayList<Product> products = new ArrayList<>();
	private static String lastModification;

	public Product(String name, int price, String category) {
		this.productName = name;
		this.productPrice = price;
		this.productCategory = category;
	}
	
	public String getProductName() {
	    return this.productName;
	}
	
	public int getProductPrice() {
	    return this.productPrice;
	}
	
	public String getProductCategory() {
	    return this.productCategory;
	}
	
	public int getProductAmount() {
	    return this.productAmount;
	}
	
	public String getExtra() {
	    return this.productExtraInfo;
	}


	public static void main(String[] args) {
		DB.addProduct(new Product("Çay", 50, "İçecek"));
		DB.addProduct(new Product("Americano", 150, "İçecek"));
		DB.addProduct(new Product("Latte", 170, "İçecek"));
		DB.addProduct(new Product("Bitki Çayı", 150, "İçecek"));
		DB.addProduct(new Product("Sezar Salata", 250, "Salata"));
		DB.addProduct(new Product("Izgara Köfte", 450, "Ana Yemek"));
		DB.addProduct(new Product("Izgara Pirzola", 430, "Ana Yemek"));
		DB.addProduct(new Product("San Sebastian", 300, "Tatlı"));
		DB.addProduct(new Product("Magnolia", 280, "Tatlı"));
		DB.addProduct(new Product("Brownie", 300, "Tatlı"));
	}
}