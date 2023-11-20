package com.project.DuAnTotNghiep.dto.Bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private Long maHoaDon;
    private String hoVaTen;
    private String soDienThoai;
    private Double tongTien;
    private int trangThai;
    private int loaiDon;
    private String hinhThucThanhToan;
}
