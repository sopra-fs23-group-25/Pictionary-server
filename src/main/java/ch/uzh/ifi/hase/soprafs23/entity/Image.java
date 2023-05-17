package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Image implements Serializable {

    @Id
    @GeneratedValue
    private long imageId;
    private String imageData;

    public String getImageData() {return imageData;}
    public void setImageData(String imageData) {this.imageData = imageData;}
}
