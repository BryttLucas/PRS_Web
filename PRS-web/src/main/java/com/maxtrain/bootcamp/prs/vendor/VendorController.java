package com.maxtrain.bootcamp.prs.vendor;

import java.util.Optional;

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
@RequestMapping(path = "/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vendorRepo;

	@GetMapping()
	public JsonResponse getAll() {
		try {
		     return JsonResponse.getInstance(vendorRepo.findAll());
		}catch(Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable Integer id) {
			if (id == null)
				return JsonResponse.getInstance("Paramenter id connot be null.");
			try {
			    Optional<Vendor> vendor = vendorRepo.findById(id);
				if (!vendor.isPresent()) {
				     return JsonResponse.getInstance("Vendor not Found.");
			}
			         return JsonResponse.getInstance(vendor.get());
		    } catch (Exception e) {
		     	e.printStackTrace();
			        return JsonResponse.getInstance(e);
		}
	}

	private JsonResponse save(Vendor vendor) {
		try {
			return JsonResponse.getInstance(vendorRepo.save(vendor));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody Vendor vendor) {
		try {
			return save(vendor);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody Vendor vendor) {
		try {
			Optional<Vendor> vend = vendorRepo.findById(vendor.getId());
			if (!vend.isPresent())
				return JsonResponse.getInstance("Vendor ID doesn't match existing vendor.");
			return save(vendor);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);

		}
	}

	@DeleteMapping("/{id}")
	public JsonResponse delete(@PathVariable Integer id) {
		try {
			if (id == null)
				return JsonResponse.getInstance("Paameter id connot be null");
			Optional<Vendor> vendor = vendorRepo.findById(id);
			if (!vendor.isPresent()) {
				return JsonResponse.getInstance("Vendor not found");
			}
			vendorRepo.deleteById(id);
			return JsonResponse.getInstance(vendor.get());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e.getMessage());
		}

	}
}
