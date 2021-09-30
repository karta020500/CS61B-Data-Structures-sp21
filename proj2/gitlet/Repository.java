package gitlet;

import java.io.*;
import java.util.List;

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
    /** The stage for removal directory*/
    public static final File REMOVAL_DIR = join(GITLET_DIR, "removal");
    /** The test directory*/
    public static final File TEST_DIR = join(CWD, "test");

    /* TODO: fill in the rest of this class. */
    public static void init(){
        // check is it initiating before.
        if (GITLET_DIR.exists()){
            System.out.print("A Gitlet version-control system already exists in the current directory. \n");
            System.exit(0);
        }

        // create initial commit and persist it.
        Repository.makeInitFileDir();
        Commit initCommit = new Commit();
        String hashCode = sha1(initCommit.getMessage(), initCommit.getTimestamp().toString());
        initCommit.setHashCode(hashCode);
        File initCommitFile = join(COMMITS_DIR, hashCode);
        writeObject(initCommitFile, initCommit);

        // documenting the repository information.
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

        // check file exists in CWD.
        if (!addFile.exists()){
            System.out.print("File does not exist. \n");
            System.exit(0);
        }

        // extract head data and compute adding file hashcode.
        String addContent = readContentsAsString(addFile);
        String addHashCode = sha1(addContent);
        String[] repoInfo = getRepoInfo();
        File headFile = join(COMMITS_DIR, repoInfo[1]);
        Commit headCommit = readObject(headFile, Commit.class);

        // compare the differences between head file version and adding file version based on hashcode.
        File addBlobFile = join(ADDITION_DIR, filename);
        if (headCommit.versionDiff(filename, addHashCode)) {
            Blob addBlob = new Blob(filename, addContent, addHashCode);
            writeObject(addBlobFile, addBlob);
        } else if (addBlobFile.exists()) {
            addBlobFile.delete();
        }
    }

    private static Commit getHeadCommit() {
        String[] repoInfo = getRepoInfo();
        return retrieveCommit(repoInfo[1]);
    }

    private static Commit retrieveCommit(String hashCode) {
        File headFile = join(COMMITS_DIR, hashCode);
        return readObject(headFile, Commit.class);
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

        Blob[] addBlobs = retrieveBlobs(ADDITION_DIR);
        Blob[] rmBlobs = retrieveBlobs(REMOVAL_DIR);
        // check staging area have file.
        if (addBlobs.length == 0 && rmBlobs.length == 0) {
            System.out.print("No changes added to the commit. \n");
            System.exit(0);
        }
        // read files from staging area and set data to current commit.
        curCommit.setBlobs(addBlobs);

        // read files from removal area and remove the files in current commit.
        curCommit.removeBlobs(rmBlobs);

        //set parents to current commit.
        String[] repoInfo = getRepoInfo();
        curCommit.setParents(repoInfo[1]);

        // data persistence and clear staging area.
        String hashCode = curCommit.getCommitSha1();
        curCommit.setHashCode(hashCode);
        File curCommitFile = join(COMMITS_DIR, hashCode);
        writeObject(curCommitFile, curCommit);
        for (Blob b : addBlobs) {
            join(ADDITION_DIR, b.getFileName()).delete();
            File BlobsFile = join(BLOBS_DIR, b.getHashCode());
            writeObject(BlobsFile, b.getHashCode());
        }
        for (Blob b : rmBlobs) {
            join(REMOVAL_DIR, b.getFileName()).delete();
        }

        // forwarding the head.
        setRepoInfo(repoInfo[0], hashCode);
    }


    private static Blob[] retrieveBlobs(File path) {
        File[] filesList = path.listFiles();

        Blob[] Blobs = new Blob[filesList.length];
        for (int i = 0; i < filesList.length; i++) {
            Blobs[i] = readObject(filesList[i], Blob.class);
        }
        return Blobs;
    }

    public static void remove(String filename) {
        //check whether it's staged for addition to decide remove from addition or not.
        if (searchFileFromPath(ADDITION_DIR, filename)) {
            join(ADDITION_DIR, filename).delete();
        }
        //check If the file is tracked in the current commit, stage it for removal and make sure file didn't in CWD.
        Commit headCommit = getHeadCommit();
        if (headCommit.tracked(filename)) {
            if (!REMOVAL_DIR.exists()) REMOVAL_DIR.mkdir();
            //stage it for removal.
            File removalBlobFile = join(REMOVAL_DIR, filename);
            String removalHashCode = headCommit.getBlobs().get(filename);
            Blob removalBlob = new Blob(filename, removalHashCode);
            writeObject(removalBlobFile, removalBlob);

            //remove the file from the working directory.
            File cwdFile = join(TEST_DIR, filename);
            if (cwdFile.exists()) cwdFile.delete();
        }
    }

    private static boolean searchFileFromPath(File path, String filename) {
        File[] filesList = path.listFiles();
        if (filesList == null) return false;
        for (File f : filesList) {
            if (f.getName().equals(filename)) return true;
        }
        return false;
    }

    public static void log() {
        printBranchCommitTree(getHeadCommit());
    }

    private static void printBranchCommitTree(Commit head) {
        if (head.getParents() == null || head.getParents().size() == 1){
            printCommitInfo(head);
        } else {
            printMergeInfo(head);
        }
        if (head.getParents() != null) {
            printBranchCommitTree(retrieveCommit(head.getParents().get(0)));
        }
    }

    private static void printCommitInfo(Commit com) {
        System.out.println(
                "=== \n" +
                "Commit " + com.getHashCode() + "\n" +
                "Date: " + com.getTimestamp() + "\n" +
                com.getMessage() + "\n");
    }

    private static void printMergeInfo(Commit com) {
        String parent1 = com.getParents().get(0).substring(0, 8);
        String parent2 = com.getParents().get(1).substring(0, 8);
        System.out.println(
                "=== \n" +
                        "Commit " + com.getHashCode() + "\n" +
                        "Merge: " + parent1 + " " + parent2 + "\n" +
                        "Date: " + com.getTimestamp() + "\n" +
                        com.getMessage()+ "\n");
    }


    public static void globalLog() {
        printCommitTree(getHeadCommit());
    }

    private static void printCommitTree(Commit head) {
        if (head.getParents() == null || head.getParents().size() == 1) {
            printCommitInfo(head);
            if (head.getParents() != null) {
                printCommitTree(retrieveCommit(head.getParents().get(0)));
            }
        } else {
            printMergeInfo(head);
            printCommitTree(retrieveCommit(head.getParents().get(0)));
            printCommitTree(retrieveCommit(head.getParents().get(1)));
        }
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
