package com.media.library.service;

import com.media.library.model.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;


@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public HashMap<String, String> storeFile(MultipartFile file) {
        HashMap<String, String> fileMetaData = new HashMap<>();
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            String fileExtension = "";
            try {
                fileExtension = fileName.substring(fileName.lastIndexOf("."));
            } catch(Exception e) {
                fileExtension = "";
                throw new RuntimeException("Sorry! Could not obtain the file extension" + e);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            fileMetaData.put("fileName", fileName);
            fileMetaData.put("fileFormat", fileExtension);
            fileMetaData.put("uploadDir", targetLocation.toString());
            fileMetaData.put("fileURL", targetLocation.toString());

            return fileMetaData;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    public HashMap<String, String> storeAndRenameFile(MultipartFile file, String title) {
        HashMap<String, String> fileMetaData = new HashMap<>();
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "";
        try {
            // Check if the file's name contains invalid characters
            if(originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }
            String fileExtension = "";
            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch(Exception e) {
                fileExtension = "";
            }
            fileName = title.toLowerCase().replaceAll(" ", "_") + fileExtension;
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            fileMetaData.put("fileName", fileName);
            fileMetaData.put("fileFormat", fileExtension);
            fileMetaData.put("uploadDir", targetLocation.toString());
            fileMetaData.put("fileURL", targetLocation.toString());


            return fileMetaData;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
}
