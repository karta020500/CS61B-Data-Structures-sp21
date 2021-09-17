package gitlet;

import java.io.*;
import java.nio.file.Files;

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

    /* TODO: fill in the rest of this class. */
    public static void init(){
        if (GITLET_DIR.exists()){
            System.out.print("A Gitlet version-control system already exists in the current directory. \n");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        Commit initCommit = new Commit();
        String hashCode = sha1(initCommit.getMessage(), initCommit.getTimestamp().toString());
        File initCommitFile = join(GITLET_DIR, hashCode);
        writeObject(initCommitFile, hashCode);
        File f = new File(GITLET_DIR, "repo_info.txt");
        Utils.writeContents(f, "master:"+ hashCode);
        //TODO store repo_info as json using gson.
    }

    public static void add(String file) {
        //TODO check difference between CWD and Head commit.
        //TODO based on change writing log. ps. commit can based on log to write changed files into Blobs Dir or Commit Dir.
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
        //TODO 1.  moves the current branch’s head to that commit node.
        //TODO 2. clear the staged area.
    }

    public static void merge(String branchName) {
    }

}
