package com.maxtrain.bootcamp.prs.lineItem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.maxtrain.bootcamp.prs.product.Product;
import com.maxtrain.bootcamp.prs.request.Request;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UIDX_request_product", columnNames = { "requestId", "productId" }))
public class LineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int quantity;
	@ManyToOne(optional = false)
	@JoinColumn(name = "RequestID")
	private Request request;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ProductID")
	private Product product;

	public LineItem() {
	}

	public LineItem(int id, Request request, Product product, int quantity) {
		super();

		this.id = id;
		this.request = request;
		this.product = product;
		this.quantity = quantity;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "LineItem [id=" + id + ", quantity=" + quantity + ", request=" + request + ", product=" + product + "]";
	}

}
