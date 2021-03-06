package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.*;

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
    /** commit's hashCode */
    private String hashCode;
    /** commit's branch */
    private String branch;

    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.blobs = null;
        this.parents = null;
        this.hashCode = null;
        this.branch = "master";
    }

    public Commit(String message) {
        this.message = message;
        this.timestamp = new Date();
        this.blobs = null;
        this.parents = null;
        this.hashCode = null;
        this.branch = Repository.getBranchInfo();
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

    public String getBranch() {
        return branch;
    }

    public boolean versionDiff(String filename, String fileHash) {
        if (this.blobs == null) return true;
        if (!this.blobs.containsKey(filename) || !this.blobs.get(filename).equals(fileHash)) return true;
        return false;
    }

    public void setBlobs(Blob[] addBlobs) {
        if (this.blobs == null) this.blobs = new HashMap<>();
        for (Blob addBlob : addBlobs) {
            this.blobs.put(addBlob.getFileName(), addBlob.getHashCode());
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setParents(String id) {
        if (this.parents == null) this.parents = new ArrayList<>();
        this.parents.add(id);
    }

    public void setCurrTimestamp() {
        this.timestamp = new Date();
    }

    public void resetParents() {
        this.parents = null;
    }

    public void setParents(String id1, String id2) {
        if (this.parents == null) this.parents = new ArrayList<>();
        this.parents.add(id1);
        this.parents.add(id2);
    }


    public String getCommitSha1() {
        StringBuilder commitInfo = new StringBuilder();

        commitInfo.append(this.message);
        commitInfo.append(this.timestamp.toString());

        for (String key : this.blobs.keySet()) {
            commitInfo.append(this.blobs.get(key));
        }

        for (String s : this.parents) {
            commitInfo.append(s);
        }

        String hashCode = sha1(commitInfo.toString());

        return hashCode;
    }

    public boolean tracked(String filename) {
        return this.blobs.containsKey(filename);
    }

    public void removeBlobs(Blob[] rmBlobs) {

        for (Blob b : rmBlobs) {
            String rmKey = b.getFileName();
            if (this.blobs.containsKey(rmKey)) blobs.remove(rmKey);
        }
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return hashCode;
    }


    /* TODO: fill in the rest of this class. */
}
