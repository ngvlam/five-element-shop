package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.dto.ProductSearchDto;
import com.project.DuAnTotNghiep.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByCode(String code);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> getAllByName(String name, Pageable pageable);

    boolean existsByCode(String code);

    Page<Product> findAllByStatus(int status, Pageable pageable);
    Page<Product> findAllByStatusAndDeleteFlag(int status, boolean deleteFlag, Pageable pageable);
    @Query(value = "SELECT p.id as id, " +
            "p.code as code, " +
            "p.name as name, " +
            "br.name as brandName, " +
            "mt.name as materialName, " +
            "ct.name as categoryName " +
//            "p.price as price " +
            "from Product p " +
            "inner join Brand br on br.id = p.brand.id " +
            "inner join Material mt on mt.id = p.material.id " +
            "inner join Category ct on ct.id = p.category.id " +
            "WHERE p.name LIKE %:productName% and p.deleteFlag=false ")
    Page<Product> searchProductName(String productName, Pageable pageable);

    @Query(value = "SELECT p.id as idSanPham,p.code as maSanPham,p.name as tenSanPham,p.brand.name as nhanHang,p.material.name as chatLieu,p.category.name as theLoai,p.status as trangThai FROM Product p where (:maSanPham is null or p.code like CONCAT('%', :maSanPham, '%')) and " +
            "(:tenSanPham is null or p.name like CONCAT('%', :tenSanPham, '%')) and (:nhanHang is null or p.brand.id=:nhanHang) and " +
            "(:chatLieu is null or p.material.id=:chatLieu) and (:theLoai is null or p.category.id=:theLoai) and (:trangThai is null or p.status=:trangThai) and(p.deleteFlag = false)")
    Page<ProductSearchDto> listSearchProduct(String maSanPham,String tenSanPham,Long nhanHang,Long chatLieu,Long theLoai,Integer trangThai,Pageable pageable);

    @Query(value = "SELECT p.id as idSanPham,p.code as maSanPham,p.name as tenSanPham,p.brand.name as nhanHang,p.material.name as chatLieu,p.category.name as theLoai,p.status as trangThai FROM Product p where p.deleteFlag = false")
    Page<ProductSearchDto> getAll(Pageable pageable);

    Page<Product> findAllByDeleteFlagFalse(Pageable pageable);

    Product findTopByOrderByIdDesc();

}