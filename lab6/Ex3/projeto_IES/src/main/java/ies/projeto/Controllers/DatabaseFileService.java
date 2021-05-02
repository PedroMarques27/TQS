package ies.projeto.Controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ies.projeto.Entities.Images;
import ies.projeto.Repositories.ImageRepository;

@Service
public class DatabaseFileService {

    @Autowired
    private ImageRepository dbFileRepository;

    public Images storeFile(MultipartFile file, int userID) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Images dbFile = new Images(fileName, file.getContentType(), file.getBytes());
            dbFile.setUserID(userID);
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Images getFile(String fileId) {
        try{
            return dbFileRepository.findByName(fileId);
        }catch (Exception ex) {
            throw new FileNotFoundException("File not found with id " + fileId);
        }
       
    }
}