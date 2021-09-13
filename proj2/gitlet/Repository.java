package gitlet;

import java.io.File;
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
            System.out.print("A Gitlet version-control system already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        Commit initCommit = new Commit();
        File initCommitFile = join(GITLET_DIR, "initCommit");
        writeObject(initCommitFile, initCommit);
    }

    public static void add(String file){
        //TODO check difference between CWD and Head commit.
        //TODO based on change writing log. ps. commit can based on log to write changed files into Blobs Dir or Commit Dir.
    }
}
