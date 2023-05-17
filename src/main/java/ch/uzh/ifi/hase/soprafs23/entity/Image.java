package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Entity
public class Image implements Serializable {

    @Id
    @GeneratedValue
    private long imageId;
    @Column(length = 30000)
    private String imageData;

    public String getImageData() {return imageData;}
    public void setImageData(String imageData) {this.imageData = imageData;}
}
