package com.learn.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncoderTests {

    static final String PASSWORD = "password";

    @Test
    void hashingExample() {
        String generate1 = DigestUtils.md5DigestAsHex(PASSWORD.getBytes());
        String generate2 = DigestUtils.md5DigestAsHex(PASSWORD.getBytes());
        Assertions.assertEquals(generate1, generate2);
        // Generate Password with Salt
        String generate3 = PASSWORD + "ThisIsMySaltValue";
        Assertions.assertNotEquals(generate3, DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
    }

    @Test
    void noOpExample() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        Assertions.assertEquals("password", noOp.encode(PASSWORD));
    }

    @Test
    void ldapExample() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        Assertions.assertNotEquals(ldap.encode(PASSWORD), ldap.encode(PASSWORD));

        String generate = ldap.encode(PASSWORD);
        Assertions.assertTrue(ldap.matches(PASSWORD, generate));
    }

    @Test
    void sha256Example() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode(PASSWORD));
        Assertions.assertNotEquals(sha256.encode(PASSWORD), sha256.encode(PASSWORD));
    }

    @Test
    void BcryptExample() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        System.out.println(bcrypt.encode(PASSWORD));
        Assertions.assertNotEquals(bcrypt.encode(PASSWORD), bcrypt.encode(PASSWORD));
    }
}
