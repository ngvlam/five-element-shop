package com.project.DuAnTotNghiep.service.serviceImpl;

import com.project.DuAnTotNghiep.entity.Color;
import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.entity.Size;
import com.project.DuAnTotNghiep.exception.NotFoundException;
import com.project.DuAnTotNghiep.repository.ColorRepository;
import com.project.DuAnTotNghiep.repository.ProductRepository;
import com.project.DuAnTotNghiep.repository.SizeRepository;
import com.project.DuAnTotNghiep.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Size> getAllSize(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }

    @Override
    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public void delete(Long id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public Optional<Size> findById(Long id) {
        return sizeRepository.findById(id);
    }

    @Override
    public List<Size> getAll() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> getSizesByProductId(Long productId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        return sizeRepository.findSizesByProduct(product);
    }

    @Override
    public List<Size> getSizesByProductIdAndColorId(Long productId, Long colorId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        Color color = colorRepository.findById(colorId).orElseThrow(() -> new NotFoundException("Color not found"));;

        return sizeRepository.findSizesByProductAndColor(product, color);
    }
}
