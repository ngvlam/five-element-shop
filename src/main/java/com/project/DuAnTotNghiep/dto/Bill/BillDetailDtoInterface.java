package com.project.DuAnTotNghiep.dto.Bill;

import com.project.DuAnTotNghiep.entity.enumClass.BillStatus;
import com.project.DuAnTotNghiep.entity.enumClass.InvoiceType;

public interface BillDetailDtoInterface {
    String getMaDonHang();

    String getMaDinhDanh();
    String getDiaChi();

    Double getTongTien();

    Double getTienSauKhuyenMai();

    String getTenKhachHang();

    String getSoDienThoai();

    String getEmail();

    BillStatus getTrangThaiDonHang();

    String getPhuongThucThanhToan();

    InvoiceType getLoaiHoaDon();


}
