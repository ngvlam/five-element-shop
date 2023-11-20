package com.project.DuAnTotNghiep.service.serviceImpl;

import com.project.DuAnTotNghiep.entity.Material;
import com.project.DuAnTotNghiep.repository.MaterialRepository;
import com.project.DuAnTotNghiep.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public Page<Material> getAllMaterial(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    @Override
    public Material save(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public void delete(Long id) {
        materialRepository.deleteById(id);
    }

    @Override
    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    @Override
    public List<Material> getAll() {
        return materialRepository.findAll();
    }
}
