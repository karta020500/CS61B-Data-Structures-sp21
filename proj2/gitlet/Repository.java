package gitlet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Scanner;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory*/
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The blobs directory*/
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** The stage for addition directory*/
    public static final File ADDITION_DIR = join(GITLET_DIR, "addition");
    /** The test directory*/
    public static final File TEST_DIR = join(CWD, "test");

    /* TODO: fill in the rest of this class. */
    public static void init(){
        if (GITLET_DIR.exists()){
            System.out.print("A Gitlet version-control system already exists in the current directory. \n");
            System.exit(0);
        }
        Repository.makeInitFileDir();
        Commit initCommit = new Commit();
        String hashCode = sha1(initCommit.getMessage(), initCommit.getTimestamp().toString());
        File initCommitFile = join(COMMITS_DIR, hashCode);
        writeObject(initCommitFile, initCommit);
        setRepoInfo("master", hashCode);
    }

    private static void makeInitFileDir() {
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
    }

    public static void add(String filename) {
        Repository.makeAddFileDir();
        File addFile = join(TEST_DIR, filename);
        if (!addFile.exists()){
            System.out.print("File does not exist. \n");
            System.exit(0);
        }

        String addContent = readContentsAsString(addFile);
        String addHashCode = sha1(addContent);
        String[] repoInfo = getRepoInfo();
        File headFile = join(COMMITS_DIR, repoInfo[1]);
        Commit headCommit = readObject(headFile, Commit.class);

        File addBlobFile = join(ADDITION_DIR, filename);
        if (headCommit.versionDiff(filename, addHashCode)) {
            Blob addBlob = new Blob(filename, addContent, addHashCode);
            writeObject(addBlobFile, addBlob);
        } else if (addBlobFile.exists()) {
            addBlobFile.delete();
        }

    }

    private static String[] getRepoInfo() {
        String repoContent = readContentsAsString(join(GITLET_DIR, "repo_info.txt"));
        String[] repoInfo = repoContent.split(" ");
        return repoInfo;
    }

    private static void setRepoInfo(String branch, String hashCode) {
        File f = new File(GITLET_DIR, "repo_info.txt");
        Utils.writeContents(f, branch + " " + hashCode);
    }

    private static void makeAddFileDir() {
        ADDITION_DIR.mkdir();
        TEST_DIR.mkdir();
    }

    public static void commit(String message) {
        Commit curCommit = new Commit(message);
        File[] filesList = ADDITION_DIR.listFiles();
        if (filesList.length == 0) {
            System.out.print("No changes added to the commit. \n");
            System.exit(0);
        }

        Blob[] addBlobs = new Blob[filesList.length];
        for (int i = 0; i < filesList.length; i++) {
            addBlobs[i] = readObject(filesList[i], Blob.class);
        }
        curCommit.setBlobs(addBlobs);
        String[] repoInfo = getRepoInfo();
        curCommit.setParents(repoInfo[1]);

        String hashCode = curCommit.getCommitSha1();
        File curCommitFile = join(COMMITS_DIR, hashCode);
        writeObject(curCommitFile, curCommit);
        for (Blob b : addBlobs) {
            join(ADDITION_DIR, b.getFileName()).delete();
            File BlobsFile = join(BLOBS_DIR, b.getHashCode());
            writeObject(BlobsFile, b.getHashCode());
        }
        setRepoInfo(repoInfo[0], hashCode);
    }

    public static void remove(String file) {
        //TODO check whether it's staged for addition to decide remove from addition or not.
        //TODO check If the file is tracked in the current commit, stage it for removal and make sure file didn't in CWD.
    }

    public static void log() {
        //TODO display information about each commit backwards along the commit tree until the initial commit to console.
        //Note: may include merge.
    }

    public static void globalLog() {
        //TODO displays information about all commits ever made.
        //Note: order is not important.
    }

    public static void find(String message) {
        //TODO search for matched commit based on input commit message.
    }

    public static void status() {
        //TODO show ropo status including branches, staged file, removed file.
        //Note: should maintain one text file to tracking those info.
    }

    public static void checkout(String file, String commitId, String branchName) {
        //TODO 1. takes version of the file to CWD from head of branch.
        //TODO 2. takes version of the file to CWD from specific commit.
        //TODO 3. takes all the file to CWD from the specific branch head of commit.
    }

    public static void branch(String branchName) {
        //TODO 1. use a file to record each branch with a corresponding commit.
    }

    public static void removeBranch(String branchName) {
        //TODO 1. delete data related to specified branch name in the branch file.
    }

    public static void reset(String commitId) {
        //TODO 1.  moves the current branch's head to that commit node.
        //TODO 2. clear the staged area.
    }

    public static void merge(String branchName) {
    }

}
