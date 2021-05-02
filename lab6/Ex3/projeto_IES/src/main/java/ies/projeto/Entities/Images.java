package ies.projeto.Entities;


import javax.persistence.*;

@Entity

@Table(name="Images")
public class Images {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    private int userID;
    
    private String fileName;

    private String fileType;
    

    @Lob
    private byte[] data;

    public Images() {

    }

    public Images(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
