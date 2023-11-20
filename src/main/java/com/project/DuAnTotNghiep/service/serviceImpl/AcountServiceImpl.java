package com.project.DuAnTotNghiep.service.serviceImpl;

import com.project.DuAnTotNghiep.entity.Account;
import com.project.DuAnTotNghiep.repository.AccountRepository;
import com.project.DuAnTotNghiep.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AcountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public String setHashMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    @Override
    public String getHashMD5(String password) throws NoSuchAlgorithmException {
        byte[] bytes = Base64.getDecoder().decode(setHashMD5(password));
        return new String(bytes);
    }
}
