package com.project.DuAnTotNghiep.controller.admin;


import com.lowagie.text.DocumentException;
import com.project.DuAnTotNghiep.dto.Bill.BillDetailDtoInterface;
import com.project.DuAnTotNghiep.dto.Bill.BillDetailProduct;
import com.project.DuAnTotNghiep.dto.Bill.BillDtoInterface;
import com.project.DuAnTotNghiep.entity.enumClass.BillStatus;
import com.project.DuAnTotNghiep.entity.enumClass.InvoiceType;
import com.project.DuAnTotNghiep.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BillController {

    @Autowired
    private BillService billService;


    @GetMapping("/bill-list")
    public String getBill(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "createDate,desc") String sortField,
            @RequestParam(name = "maDinhDanh", required = false) String maDinhDanh,
            @RequestParam(name = "ngayTaoStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayTaoStart,
            @RequestParam(name  = "ngayTaoEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayTaoEnd,
            @RequestParam(name = "trangThai", required = false) String trangThai,
            @RequestParam(name = "loaiDon", required = false) String loaiDon,
            @RequestParam(name = "soDienThoai", required = false) String soDienThoai,
            @RequestParam(name = "hoVaTen", required = false) String hoVaTen
    ) {
        int pageSize = 5;
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<BillDtoInterface> Bill;
        if (ngayTaoStart != null || ngayTaoEnd != null || maDinhDanh != null || trangThai != null || loaiDon != null || hoVaTen != null || soDienThoai != null) {
            // Convert Date to LocalDateTime
            LocalDateTime convertedNgayTaoStart = null;
            LocalDateTime convertedNgayTaoEnd = null;
            if(ngayTaoStart != null) {
                convertedNgayTaoStart = ngayTaoStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            }
            if(ngayTaoEnd != null) {
                convertedNgayTaoEnd = ngayTaoEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            Bill = billService.searchListBill(maDinhDanh, convertedNgayTaoStart, convertedNgayTaoEnd, trangThai, loaiDon, soDienThoai, hoVaTen, pageable);
        } else {
            Bill = billService.findAll(pageable);
        }

        model.addAttribute("sortField", sortFieldName);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("items", Bill);

        model.addAttribute("maHoaDon", maDinhDanh);
        model.addAttribute("ngayTaoStart", ngayTaoStart);
        model.addAttribute("ngayTaoEnd", ngayTaoEnd);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("loaiDon", loaiDon);
        model.addAttribute("soDienThoai", soDienThoai);
        model.addAttribute("hoVaTen", hoVaTen);
        model.addAttribute("sortField", sortField);
        model.addAttribute("billStatus", BillStatus.values());
        model.addAttribute("invoiceType", InvoiceType.values());
        return "admin/bill";
    }


    @GetMapping("/update-bill-status/{billId}")
    public String updateBillStatus(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "sort", defaultValue = "createDate,desc") String sortField, @PathVariable Long billId,
                                   @RequestParam String trangThaiDonHang, RedirectAttributes redirectAttributes) {
        int pageSize = 5;
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<BillDtoInterface> Bill = billService.findAll(pageable);
        try {
            billService.updateStatus(trangThaiDonHang, billId);
            redirectAttributes.addFlashAttribute("message", "Hóa đơn " + billId + " cập nhật trạng thái thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error updating status");
        }
        model.addAttribute("sortField", sortFieldName);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("items", Bill);

        return "redirect:/admin/bill-list";
    }


    @GetMapping("/getbill-detail/{maHoaDon}")
    public String getBillDetail(Model model, @PathVariable("maHoaDon") Long maHoaDon) {

        BillDetailDtoInterface billDetailDtoInterface = billService.getBillDetail(maHoaDon);
        List<BillDetailProduct> billDetailProducts = billService.getBillDetailProduct(maHoaDon);
        Double total = Double.valueOf("0");
            for (BillDetailProduct billDetailProduct:
                 billDetailProducts) {
                int q = billDetailProduct.getSoLuong();
                total += billDetailProduct.getGiaTien() * q;
            }
        model.addAttribute("billDetailProduct", billDetailProducts);
        model.addAttribute("billdetail", billDetailDtoInterface);
        model.addAttribute("total", total);
        return "admin/bill-detail";
    }


    @GetMapping("/export-bill")
    public void exportBill(
            HttpServletResponse response,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sortField,
            @RequestParam(name = "ngayTaoStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime ngayTaoStart,
            @RequestParam(name = "ngayTaoEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime ngayTaoEnd,
            UriComponentsBuilder uriBuilder
    ) throws IOException {
        int pageSize = 5;
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<BillDtoInterface> bills;

        if (ngayTaoStart != null && ngayTaoEnd != null) {
            bills = billService.searchListBill(null, ngayTaoStart, ngayTaoEnd, null, null, null, null, pageable);
        } else {
            bills = billService.findAll(pageable);
        }

        String exportUrl = uriBuilder.path("/export-bill")
                .queryParam("page", page)
                .queryParam("sort", sortField)
                .queryParam("ngayTaoStart", ngayTaoStart)
                .queryParam("ngayTaoEnd", ngayTaoEnd)
                .toUriString();

        billService.exportToExcel(response, bills, exportUrl);
    }

    @GetMapping("/export-pdf/{maHoaDon}")
    public String exportPdf(HttpServletResponse response, @PathVariable("maHoaDon") Long maHoaDon) throws DocumentException, IOException {
        return billService.exportPdf(response, maHoaDon);
    }

    @GetMapping("/generate-pdf/{maHoaDon}")
    public ResponseEntity<String> generatePDF(@PathVariable Long maHoaDon) {
        // Your HTML content as a string
        String htmlContent = billService.getHtmlContent(maHoaDon);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);
    }

}
