package com.fooddelivery.app.Foodbox.service;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fooddelivery.app.Foodbox.config.FileStorageProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;



@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                                      .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFileWithOriginalName(MultipartFile file, String username) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        Path userDir = this.fileStorageLocation.resolve(username);
        
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }
        
        // Handle filename and extension
        int lastDot = originalFilename.lastIndexOf('.');
        String baseName = (lastDot > 0) ? originalFilename.substring(0, lastDot) : originalFilename;
        String extension = (lastDot > 0) ? originalFilename.substring(lastDot) : "";
        
        // Handle duplicate files
        Path targetLocation;
        int counter = 0;
        String newFilename;
        
        do {
            newFilename = (counter == 0) 
                ? originalFilename 
                : baseName + "_" + counter + extension;
            targetLocation = userDir.resolve(newFilename);
            counter++;
        } while (Files.exists(targetLocation));
        
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return newFilename;
    }
}