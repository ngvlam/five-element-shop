package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>{

    Account findByEmail(String email);
}
