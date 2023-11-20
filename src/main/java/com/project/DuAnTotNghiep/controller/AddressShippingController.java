package com.project.DuAnTotNghiep.controller;

import com.project.DuAnTotNghiep.dto.AddressShipping.AddressShippingDto;
import com.project.DuAnTotNghiep.service.AddressShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddressShippingController {

    private final AddressShippingService addressShippingService;

    public AddressShippingController(AddressShippingService addressShippingService) {
        this.addressShippingService = addressShippingService;
    }

    @ResponseBody
    @PostMapping("api/public/addressShipping")
    public ResponseEntity<AddressShippingDto> createAddressShipping(@RequestBody AddressShippingDto addressShippingDto){
        return ResponseEntity.ok(addressShippingService.saveAddressShippingUser(addressShippingDto));
    }
}
