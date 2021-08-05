package com.stigentech.spares365.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "addproduct")
public class Product {
	
	@Column(name = "id")
	private int id;
	@Column(name = "productname")
	private String name;
	@Column(name = "price")
	private double price;
	@Column(name = "quantity")
	private int quantity;
	@Lob
	@Column(name = "description")
	private String description;
//	@Column(name="image", length=1000)
//	private byte[] productimage;
	@Column(name="modelnumber")
	private String modelnumber;
	@Column(name="productcategory")
	private String category;
	@Column(name="yearofmanufacture")
	private int year;
	@Lob
	@Column(name="image")
	private String image;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="orderId")
    private int orderId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getorderId() {
	return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product(int id, String name, double price, int quantity, String description,String modelnumber,String category,int year,int orderId, String image) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.description = description;
//		this.productimage=productimage;
		this.modelnumber=modelnumber;
		this.category=category;
		this.year=year;
		this.orderId=orderId;
		this.image = image;
	}

	public Product() {

	}

	public String getModelnumber() {
		return modelnumber;
	}

	public void setModelnumber(String modelnumber) {
		this.modelnumber = modelnumber;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public String getImage() {
	    return image;
	  }

	public void setImage(String image) {
		this.image = image;
	}

}
