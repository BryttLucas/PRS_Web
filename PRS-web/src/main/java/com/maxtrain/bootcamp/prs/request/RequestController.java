package com.maxtrain.bootcamp.prs.request;

import java.sql.Date;
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
@RequestMapping(path = "/requests")
public class RequestController {
	public static final String REQUEST_STATUS_NEW = "NEW";
	public static final String REQUEST_STATUS_EDIT = "EDIT";
	public static final String REQUEST_STATUS_REVIEW = "REVIEW";
	public static final String REQUEST_STATUS_APPROVE = "APPROVE";
	public static final String REQUEST_STATUS_REJECTED = "REJECTED";

	@Autowired
	private RequestRepository requestRepo;

	@GetMapping()
	public JsonResponse getAll() {
		try {
			return JsonResponse.getInstance(requestRepo.findAll());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable Integer id) {
		if (id == null)
			return JsonResponse.getInstance("Path ID cann't be null.");
		try {
			Optional<Request> request = requestRepo.findById(id);
			if (!request.isPresent()) {
				return JsonResponse.getInstance("Request not found.");
			}
			return JsonResponse.getInstance(request.get());
		} catch (IllegalArgumentException e) {
			return JsonResponse.getInstance("Id must not be null.");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	private JsonResponse save(Request request) {
		try {
			return JsonResponse.getInstance(requestRepo.save(request));
		} catch (IllegalArgumentException e) {
			return JsonResponse.getInstance(e);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e.getMessage());
		}
	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody Request request) {
		try {
			request.setStatus(REQUEST_STATUS_NEW);
			request.setTotal(0);
			request.setSubmittedDate(new Date(System.currentTimeMillis()));
			return save(request);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody Request request) {
		try {
			Optional<Request> r = requestRepo.findById(request.getId());
			if(!r.isPresent()) return JsonResponse.getInstance("No Request matches ID entered.");
			return save(request);
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
			Optional<Request> request = requestRepo.findById(id);
			if (!request.isPresent())
				return JsonResponse.getInstance("Request not found.");
			requestRepo.deleteById(request.get().getId());
			return JsonResponse.getInstance(request.get());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	// No need for a mapping for this method (SNB 2/22)
	// @GetMapping("requests/list-review/{id}")
	// Method will change the request status and save it
	private JsonResponse setRequestStatus(Request request, String status) {
		try {
			request.setStatus(status);
			return save(request);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	// @PutMapping("/list-review/{id}")
	@PutMapping("/submit-review")
	public JsonResponse getRequestWithStatusOfReview(@RequestBody Request request) {
		try {
			Optional<Request> requests = requestRepo.findById(request.getId());
			if (!requests.isPresent())
				return JsonResponse.getInstance("No matching request found.");
			request.setSubmittedDate(new Date(System.currentTimeMillis()));
			if (request.getTotal() <= 50) {
				return setRequestStatus(request, REQUEST_STATUS_APPROVE);
			}
			return setRequestStatus(request, REQUEST_STATUS_REVIEW);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}

	@PutMapping("/approve")
	public JsonResponse approve(@RequestBody Request request) {
		try {
			Optional<Request> requests = requestRepo.findById(request.getId());
			if (!requests.isPresent())
				return JsonResponse.getInstance("No Request match found.");
			return setRequestStatus(request, REQUEST_STATUS_APPROVE);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}

	}

	@PutMapping("/reject")
	public JsonResponse reject(@RequestBody Request request) {
		try {
			Optional<Request> requests = requestRepo.findById(request.getId());
			if (!requests.isPresent())
				return JsonResponse.getInstance("No Request match found.");
			return setRequestStatus(request, REQUEST_STATUS_REJECTED);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}

	}

	// Requirement #6 - submit request for review: change status to approved (if <=
	// 50) or review, set submitted date to current date
	@GetMapping("/list-review/{id}")
	public JsonResponse getReviews(@PathVariable Integer id) {
		try {
			return JsonResponse.getInstance(requestRepo.findRequestByStatusAndUserIdNot(REQUEST_STATUS_REVIEW,id));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponse.getInstance(e);
		}
	}
}
