package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    private String fileName;
    private String content;
    private String hashCode;

    public Blob(String fileName, String content, String hashCode) {
        this.fileName = fileName;
        this.content = content;
        this.hashCode = hashCode;
    }
    public Blob(String fileName, String hashCode) {
        this.fileName = fileName;
        this.content = null;
        this.hashCode = hashCode;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    public String getHashCode() {
        return hashCode;
    }
}
