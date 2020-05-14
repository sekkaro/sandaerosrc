package com.sangdaero.walab.payment.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sangdaero.walab.payment.service.FundraisingService;

@RestController
public class AjaxController {

	FundraisingService mFundraisingService;

	// constructor
	public AjaxController(FundraisingService mFundraisingService) {
		this.mFundraisingService = mFundraisingService;
	}

	@CrossOrigin(origins = "http://localhost:8080") //@PostMapping("/ajax/request")
	@RequestMapping(value="/ajax/request", method=RequestMethod.POST)
	@ResponseBody
	public void tableList(@Valid @RequestBody String totalTableData) throws Exception {
		
		System.out.println("ajax controller entered : " + totalTableData.toString());
		
		// now all the data are in here, do the save query with the table data
		//for (String data: totalTableData.get(0)) {
		//	System.out.println("Data => " + data);
		//}
	}
	
}
