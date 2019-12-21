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
		Request request = req.get();
		Iterable<LineItem> lines = lineRepo.getLineitemByRequestId(request.getId());
		double total = 0;
		for (LineItem line : lines) {
			double subtotal = (line.getProduct().getPrice() * line.getQuantity());
			total = subtotal;
		}
		request.setTotal(total);
		request.setStatus(RequestController.REQUEST_STATUS_EDIT);
		requestRepo.save(request);
	}

	@GetMapping("/request/{id}")
	public JsonResponse getByRequest(@PathVariable Integer id) {
		try {
			Iterable<LineItem> lines = lineRepo.getLineitemByRequestId(id);
			return JsonResponse.getInstance(lines);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@GetMapping()
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
		try {
			if (id == null)
				return JsonResponse.getInstance("Paramerter id cannot be null.");
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
			LineItem line1 = lineRepo.saveAndFlush(line);
			return JsonResponse.getInstance(line1);
		} catch (IllegalArgumentException e) {
			return JsonResponse.getInstance("Parameter lineitem cannot be null.");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PostMapping()
	private JsonResponse insert(@RequestBody LineItem line) {
		try {
			JsonResponse jr = save(line);
			recalcLines(line.getRequest().getId());
			return jr;
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PutMapping("/{id}")
	public JsonResponse update(@RequestBody LineItem line, @PathVariable Integer id) {
		try {
			if (id != line.getId()) {
				return JsonResponse.getInstance("Parameter id doesn't match.");
			}
			return save(line);
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
			Optional<LineItem> line = lineRepo.findById(id);
			if (line.isPresent()) {
				lineRepo.deleteById(id);
				lineRepo.flush();
				recalcLines(line.get().getRequest().getId());
				return JsonResponse.getInstance(line.get());
			}
			return JsonResponse.getInstance("Line item does not exist.");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}
}
