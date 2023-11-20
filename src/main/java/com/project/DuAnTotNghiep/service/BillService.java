package com.project.DuAnTotNghiep.service;

import com.lowagie.text.DocumentException;
import com.project.DuAnTotNghiep.dto.Bill.BillDetailDtoInterface;
import com.project.DuAnTotNghiep.dto.Bill.BillDetailProduct;
import com.project.DuAnTotNghiep.dto.Bill.BillDtoInterface;
import com.project.DuAnTotNghiep.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BillService {

    Page<BillDtoInterface> findAll(Pageable pageable);

//    public Page<BillDtoInterface> searchListBill(String maDinhDanh,
//                                                 Date ngayTaoStart,
//                                                 Date ngayTaoEnd,
//                                                 Integer trangThai,
//                                                 Integer loaiDon,
//                                                 String soDienThoai,
//                                                 String hoVaTen,
//                                                 Pageable pageable);

    Page<BillDtoInterface> searchListBill(String maDinhDanh,
                                          LocalDateTime ngayTaoStart,
                                          LocalDateTime ngayTaoEnd,
                                          String trangThai,
                                          String loaiDon,
                                          String soDienThoai,
                                          String hoVaTen,
                                          Pageable pageable);

    int updateStatus(String status, Long id);

    BillDetailDtoInterface getBillDetail(Long maHoaDon);

    Page<Bill> getBillByStatus(String status, Pageable pageable);

    List<BillDetailProduct> getBillDetailProduct(Long maHoaDon);

    void exportToExcel(HttpServletResponse response, Page<BillDtoInterface> bills, String exportUrl) throws IOException;

    String exportPdf(HttpServletResponse response,Long maHoaDon) throws DocumentException, IOException;

    String getHtmlContent(Long maHoaDon);


    Page<Bill> getBillByAccount(Pageable pageable);

    void deleteById(Long id);
}
