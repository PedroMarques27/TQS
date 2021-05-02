package ies.projeto.Controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ies.projeto.Entities.Images;
import ies.projeto.Forms.ImageForm;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
public class FileUploadController {

    @Autowired
    private DatabaseFileService fileStorageService;

    @PostMapping("/uploadFile")
    public void uploadFile(@ModelAttribute(name = "imageForm") ImageForm lf, String back_to,
            HttpServletResponse response, HttpSession session)
            throws IOException {
        
        MultipartFile file = lf.getFile();
        int userID = (int)session.getAttribute("userID");
        Images fileName = fileStorageService.storeFile(file, userID);
        
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName.getFileName())
            .toUriString();

        Response data =  new Response(fileName.getFileName(), fileDownloadUri,
            file.getContentType(), file.getSize());
        if (back_to.equals("PROFILE"))
            response.sendRedirect("edit_profile");
        else{
            response.sendRedirect("home");
        }
            
            
        
    }
   



    

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity < Resource > downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Images databaseFile = fileStorageService.getFile(fileName);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
            .body(new ByteArrayResource(databaseFile.getData()));
    }
}