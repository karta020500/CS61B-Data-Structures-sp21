package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.List;
import java.util.Map;

import static gitlet.Utils.sha1;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private Date timestamp;
    /** The List for tracking files of this Commit. */
    private Map<String, String> blobs;
    /** commit's parents */
    private List<String> parents;

    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.blobs = null;
        this.parents = null;
    }

    public Commit(String message) {
        this.message = message;
        this.timestamp = new Date();
        this.blobs = null;
        this.parents = null;
    }

    public String getMessage() {
        return this.message;
    }
    public Date getTimestamp() {
        return this.timestamp;
    }

    public Map<String, String> getBlobs() {
        return this.blobs;
    }

    public List<String> getParents() {
        return this.parents;
    }

    public boolean versionDiff(String filename, String fileHash) {
        if (this.blobs == null) return true;
        if (this.blobs.get(filename) != fileHash) return true;
        return false;
    }

    public void setBlobs(Blob[] blobs) {
        for (int i = 0; i < blobs.length; i++) {
            this.blobs.put(blobs[i].getFileName(), blobs[i].getHashCode());
        }
    }

    public void setParents(String commit) {
        this.parents.add(commit);
    }


    public String getCommitSha1() {
        String[] commitInfo = new String[this.blobs.size() + this.parents.size() + 2];
        int index = 0;
        commitInfo[index] = this.message;
        index++;
        commitInfo[index] =  this.timestamp.toString();
        index++;
        for (String key : blobs.keySet()) {
            commitInfo[index] = blobs.get(key);
            index++;
        }
        for (String s : parents) {
            commitInfo[index] = s;
            index++;
        }

        String hashCode = sha1(commitInfo);
        return hashCode;
    }

    /* TODO: fill in the rest of this class. */
}
