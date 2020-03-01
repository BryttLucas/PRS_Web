package com.maxtrain.bootcamp.prs.lineItem;

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

import com.maxtrain.bootcamp.prs.request.Request;
import com.maxtrain.bootcamp.prs.request.RequestController;
import com.maxtrain.bootcamp.prs.request.RequestRepository;
import com.maxtrain.bootcamp.prs.util.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/line-items")
public class LineItemController {

	@Autowired
	private LineItemRepository lineRepo;
	@Autowired
	private RequestRepository requestRepo;

	private void recalcLines(int requestId) throws Exception {
		Optional<Request> req = requestRepo.findById(requestId);
		if (!req.isPresent()) {
			throw new Exception("Cannot find the Request with id." + requestId);
		}
		Iterable<LineItem> lines = lineRepo.getLineitemByRequestId(requestId);
		double total = 0;
		for (LineItem line : lines) {
			total += (line.getProduct().getPrice() * line.getQuantity());

		}
		Request request = req.get();
		request.setTotal(total);
		request.setStatus(RequestController.REQUEST_STATUS_EDIT);
		requestRepo.save(request);
	}

	@GetMapping("/lines-for-pr/{id}")
	public JsonResponse getByRequest(@PathVariable Integer id) {
		try {
			Iterable<LineItem> lines = lineRepo.getLineitemByRequestId(id);
			return JsonResponse.getInstance(lines);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@GetMapping("/")
	public JsonResponse getAll() {
		try {
			return JsonResponse.getInstance(lineRepo.findAll());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@GetMapping("/{id}")
	private JsonResponse get(@PathVariable Integer id) {
		if (id == null)
			return JsonResponse.getInstance("Paramerter id cannot be null.");
		try {
			Optional<LineItem> line = lineRepo.findById(id);
			if (!line.isPresent())
				return JsonResponse.getInstance("Lineitem not found.");
			return JsonResponse.getInstance(line.get());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	private JsonResponse save(LineItem line) {
		try {
			LineItem lineItem = lineRepo.saveAndFlush(line);
			return JsonResponse.getInstance(lineItem);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return JsonResponse.getInstance("Parameter lineitem cannot be null.");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PostMapping("/")
	private JsonResponse insert(@RequestBody LineItem line) {
		try {
			save(line);
			recalcLines(line.getRequest().getId());
			return JsonResponse.getInstance(line);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody LineItem line) {
		try {
			Optional<LineItem> l = lineRepo.findById(line.getId());
			if (!l.isPresent()) {
				return JsonResponse.getInstance("Parameter id doesn't match.");
			}
			save(line);
			recalcLines(line.getRequest().getId());
			return JsonResponse.getInstance(line);

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);

		}
	}

	@DeleteMapping("/{id}")
	public JsonResponse delete(@PathVariable Integer id) {
			if (id == null)
				return JsonResponse.getInstance("Parameter id cannot be null.");
			try {
			      Optional<LineItem> line = lineRepo.findById(id);
			      if (!line.isPresent()) 
			    	  return JsonResponse.getInstance("Line item doesn't exist.");
				lineRepo.deleteById(id);
				lineRepo.flush();
				recalcLines(line.get().getRequest().getId());
				return JsonResponse.getInstance(line.get());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}
	
}
