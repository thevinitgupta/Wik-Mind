package com.wikmind.service.source.service;

import com.wikmind.service.source.exceptions.SourceUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class SHA256ChecksumService {
    public String sha256(MultipartFile file) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());

            return HexFormat.of().formatHex(hash);
        } catch (IOException ex) {
            throw new SourceUploadException("Unable to calculate checksum.", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
