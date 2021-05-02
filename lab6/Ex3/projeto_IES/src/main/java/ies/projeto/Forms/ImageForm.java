package ies.projeto.Forms;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {
    private MultipartFile file;
    private int userID;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    
}
