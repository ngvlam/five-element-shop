package com.project.DuAnTotNghiep.service;


import com.project.DuAnTotNghiep.entity.Account;

import java.security.NoSuchAlgorithmException;

public interface AccountService {
    Account findByEmail(String email);

    Account save(Account account);

    String setHashMD5(String password) throws NoSuchAlgorithmException;

    String getHashMD5(String password) throws NoSuchAlgorithmException;
}
