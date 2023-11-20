package com.project.DuAnTotNghiep.dto;

import com.project.DuAnTotNghiep.dto.CustomerDto.CustomerDto;
import com.project.DuAnTotNghiep.dto.Product.ProductDetailDto;
import com.project.DuAnTotNghiep.entity.Customer;
import com.project.DuAnTotNghiep.entity.enumClass.BillStatus;
import com.project.DuAnTotNghiep.entity.enumClass.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String billId;
    private CustomerDto customer;
    private InvoiceType invoiceType;
    private BillStatus billStatus;
    private Long paymentMethodId;
    private String billingAddress;
    private double promotionPrice;
    private Long voucherId;
    private List<OrderDetailDto> orderDetailDtos;
}
