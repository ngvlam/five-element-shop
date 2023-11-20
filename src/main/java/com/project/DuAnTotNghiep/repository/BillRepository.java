package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.dto.Bill.BillDetailDtoInterface;
import com.project.DuAnTotNghiep.dto.Bill.BillDetailProduct;
import com.project.DuAnTotNghiep.dto.Bill.BillDtoInterface;
import com.project.DuAnTotNghiep.entity.Account;
import com.project.DuAnTotNghiep.entity.Bill;
import com.project.DuAnTotNghiep.entity.Customer;
import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.entity.enumClass.BillStatus;
import com.project.DuAnTotNghiep.entity.enumClass.InvoiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT DISTINCT b.id AS maHoaDon,b.code AS maDinhDanh, a.name AS hoVaTen, a.phoneNumber " +
            "AS soDienThoai,b.createDate AS ngayTao, b.amount AS tongTien, b.status AS trangThai, b.invoiceType " +
            "AS loaiDon, pm.name AS hinhThucThanhToan " +
            "FROM Bill b " +
            "LEFT JOIN Customer a ON b.customer.id = a.id " +
            "LEFT JOIN BillDetail bd ON b.id = bd.bill.id " +
            "LEFT JOIN PaymentMethod pm ON b.paymentMethod.id = pm.id")
    Page<BillDtoInterface> listBill(Pageable pageable);

    @Query(value = "SELECT DISTINCT b.id AS maHoaDon,b.code AS maDinhDanh, a.name AS hoVaTen, a.phoneNumber " +
            "AS soDienThoai, b.createDate AS ngayTao, b.amount AS tongTien, b.status AS trangThai, b.invoiceType " +
            "AS loaiDon, pm.name AS hinhThucThanhToan " +
            "FROM Bill b " +
            "LEFT JOIN Customer a ON b.customer.id = a.id " +
            "LEFT JOIN BillDetail bd ON b.id = bd.bill.id " +
            "LEFT JOIN PaymentMethod pm ON b.paymentMethod.id = pm.id " +
            "WHERE (:maDinhDanh IS NULL OR b.code LIKE CONCAT('%', :maDinhDanh, '%')) " +
            "AND (:ngayTaoStart IS NULL OR :ngayTaoEnd IS NULL OR (b.createDate BETWEEN :ngayTaoStart AND :ngayTaoEnd)) " +
            "AND (:trangThai IS NULL OR b.status = :trangThai) " +
            "AND (:loaiDon IS NULL OR b.invoiceType= :loaiDon) "+
            "AND (:soDienThoai IS NULL OR a.phoneNumber IS NULL OR a.phoneNumber LIKE CONCAT('%', :soDienThoai, '%')) "+
            "AND (:hoVaTen IS NULL OR a.name IS NULL OR a.name LIKE CONCAT('%', :hoVaTen, '%'))")
    Page<BillDtoInterface> listSearchBill(
            @Param("maDinhDanh") String maDinhDanh,
            @Param("ngayTaoStart") LocalDateTime ngayTaoStart,
            @Param("ngayTaoEnd") LocalDateTime ngayTaoEnd,
            @Param("trangThai") BillStatus trangThai,
            @Param("loaiDon") InvoiceType loaiDon,
            @Param("soDienThoai") String soDienThoai,
            @Param("hoVaTen") String hoVaTen,
            Pageable pageable);

    @Modifying
    @Query(value = "UPDATE bill SET status=:status WHERE id=:id", nativeQuery = true)
    @Transactional
    int updateStatus(@Param("status") String status,@Param("id") Long id);


    @Query(value = "select distinct b.id as maDonHang,b.code as maDinhDanh,b.billing_address as diaChi," +
            " b.amount as tongTien,b.promotion_price as tienSauKhuyenMai,a.name as tenKhachHang," +
            "a.phone_number as soDienThoai,a.email as email, b.status as trangThaiDonHang ," +
            "pm.name as phuongThucThanhToan,b.invoice_type as loaiHoaDon " +
            "from bill b full join customer a on b.customer_id=a.id" +
            " full join bill_detail bd on b.id=bd.bill_id left join payment_method pm on b.payment_method_id=pm.id where b.id=:maHoaDon",nativeQuery = true)
    BillDetailDtoInterface getBillDetail(@Param("maHoaDon") Long maHoaDon);

    @Query(value = "select p.name as tenSanPham,c.name as tenMau, s.name as kichCo, pd.price as giaTien,bd.quantity as soLuong,pd.price*bd.quantity as tongTien " +
            "from bill b join bill_detail bd on b.id=bd.bill_id join" +
            " product_detail pd on bd.product_detail_id =pd.id join" +
            " product p on pd.product_id=p.id join color c on pd.color_id=c.id join size s on pd.size_id = s.id where b.id=:maHoaDon",nativeQuery = true)
    List<BillDetailProduct> getBillDetailProduct(@Param("maHoaDon") Long maHoaDon);

    Bill findTopByOrderByIdDesc();

    Page<Bill> findAllByStatusAndCustomer_Account_Id(BillStatus status, Long id, Pageable pageable);

    Page<Bill> findByCustomer_Account_Id(Long id, Pageable pageable);
}