package org.Utility;

public class ImageDetails {
    private byte[] imageData;
    private String imageName;
    private String uploadTime;
    private String userId;
    public ImageDetails(byte[] imageData, String imageName, String uploadTime, String userId) {
        this.imageData = imageData;
        this.imageName = imageName;
        this.uploadTime = uploadTime;
        this.userId = userId;
    }

    public ImageDetails() {

    }

    // Getter 및 Setter 메소드 추가 (필요에 따라)
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}