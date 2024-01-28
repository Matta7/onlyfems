package fr.univrouen.onlyfems.entities;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

@Entity
@Table(name = "images")
public class Image implements MultipartFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name = "";

    private String description = "";

    // true : public | false : private
    private Boolean privacy = true;

    private String contentType = "";

    @Transient
    private byte[] base64 = new byte[]{};

    @Transient
    private String base64String = "";

    public Image(String name, String description, boolean privacy, String contentType, String base64) {
        this.name = name;
        this.description = description;
        this.privacy = privacy;
        this.contentType = contentType;

        this.base64String = base64;
        this.base64 = Base64.getDecoder().decode(base64);
    }

    public Image() {}


    // GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals("")) {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && !description.equals("")) {
            this.description = description;
        }
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        if (privacy != null) {
            this.privacy = privacy;
        }
    }

    public void setContentType(String contentType) {
        if (contentType != null && !contentType.equals("")) {
            this.contentType = contentType;
        }
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64(String base64) {
        if (base64 != null && !base64.equals("")) {
            this.base64String = base64;
            this.base64 = Base64.getDecoder().decode(base64);
        }
    }

    public void setBase64(byte[] base64) {
        if (base64 != null && base64.length > 0) {
            this.base64 = base64;
            this.base64String = Base64.getEncoder().encodeToString(base64);
        }
    }

    // MultipartFile Methods

    @Override
    public String getOriginalFilename() {
        return this.name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return (base64 == null || base64.length == 0);
    }

    @Override
    public long getSize() {
        return base64.length;
    }

    @Override
    public byte[] getBytes() {
        return base64;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(base64);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(base64);
    }
}
