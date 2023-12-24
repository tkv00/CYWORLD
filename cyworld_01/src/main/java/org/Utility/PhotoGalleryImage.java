package org.Utility;

import javax.swing.*;

public class PhotoGalleryImage extends ImageDetails {
    private ImageIcon image;
    private String title;
    private String uploadTime;

    public PhotoGalleryImage(ImageIcon image, String title, String uploadTime) {
        super();
        this.image = image;
        this.title = title;
        this.uploadTime = uploadTime;
    }

    // 각 필드에 대한 getter 메서드
    public ImageIcon getImage() { return image; }
    public String getTitle() { return title; }
    public String getUploadTime() { return uploadTime; }
}
