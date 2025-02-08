package service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CryptService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public String encryptPassword(String password) {
        return encoder.encode(password);
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
