package com.maxtrain.bootcamp.prs.product;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.bootcamp.prs.util.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping(path = "/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepo;

	@GetMapping()
	public JsonResponse getAll() {
		Iterable<Product> product = productRepo.findAll();
		return JsonResponse.getInstance(product);
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable Integer id) {
		try {
			if (id == null)
				return JsonResponse.getInstance("Parameter id cannot be null.");
			Optional<Product> product = productRepo.findById(id);
			if (!product.isPresent()) {
				return JsonResponse.getInstance("Product not found.");
			}
			return JsonResponse.getInstance(product.get());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	private JsonResponse save(Product product) {
		try {
			Product prod = productRepo.save(product);
			return JsonResponse.getInstance(prod);
		} catch (IllegalArgumentException e) {
			return JsonResponse.getInstance("Parameter product cannot be null.");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PostMapping()
	public JsonResponse Insert(@RequestBody Product product) {
		try {
			return save(product);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PutMapping("/{id}")
	public JsonResponse update(@RequestBody Product product, @PathVariable Integer id) {
		try {
			if (id != product.getId())
				return JsonResponse.getInstance("Parameter id cannot be null.");
			return save(product);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@DeleteMapping("/{id}")
	public JsonResponse delete(@PathVariable Integer id) {
		try {
			if (id == null)
				return JsonResponse.getInstance("Parameter id cannot be null.");
			Optional<Product> product = productRepo.findById(id);
			if (!product.isPresent()) {
				return JsonResponse.getInstance("Product not found.");
			}
			productRepo.deleteById(product.get().getId());
			return JsonResponse.getInstance(product.get());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);

		}
	}
}
