package gitlet;

import java.io.*;
import java.util.*;

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
        File f = new File(GITLET_DIR, "repo_info.txt");
        Utils.writeContents(f, "master" + " " + hashCode + " ");
        setBranchInfo("master");
    }

    private static void makeInitFileDir() {
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
    }

    public static void add(String filename) {
        Repository.makeAddFileDir();
        File addFile = join(CWD, filename);

        // check file exists in CWD.
        if (!addFile.exists()){
            System.out.print("File does not exist. \n");
            System.exit(0);
        }

        // extract head data and compute adding file hashcode.
        String addContent = readContentsAsString(addFile);
        String addHashCode = sha1(addContent);
        Commit headCommit = getHeadCommit();

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
        return retrieveCommit(getBranchHeadId(getBranchInfo()));
    }

    private static Commit retrieveCommit(String hashCode) {
        File headFile = join(COMMITS_DIR, hashCode);
        return readObject(headFile, Commit.class);
    }

    private static Blob retrieveBlob(String hashCode) {
        File headFile = join(BLOBS_DIR, hashCode);
        return readObject(headFile, Blob.class);
    }

    private static String[] getRepoInfo() {
        String repoContent = readContentsAsString(join(GITLET_DIR, "repo_info.txt"));
        String[] repoInfo = repoContent.split(" ");
        return repoInfo;
    }

    public static String getBranchInfo() {
        return readContentsAsString(join(GITLET_DIR, "branch_info.txt"));
    }

    private static void setRepoInfo(String branch, String hashCode) {
        File f = new File(GITLET_DIR, "repo_info.txt");
        String[] repoInfo = getRepoInfo();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repoInfo.length; i+=2) {
            sb.append(repoInfo[i]);
            sb.append(" ");
            if (repoInfo[i].equals(branch)) {
                sb.append(hashCode);
            } else {
                sb.append(repoInfo[i+1]);
            }
            sb.append(" ");
        }
        Utils.writeContents(f, sb.toString());
    }

    private static void addRepoInfo(String branch) {
        File f = new File(GITLET_DIR, "repo_info.txt");
        String[] repoInfo = getRepoInfo();
        StringBuilder sb = new StringBuilder();
        String currBranch = getBranchInfo();
        String currBranchHead = "";
        for (int i = 0; i < repoInfo.length; i+=2) {
            if (repoInfo[i].equals(currBranch)) currBranchHead = repoInfo[i+1];
            sb.append(repoInfo[i]);
            sb.append(" ");
            sb.append(repoInfo[i+1]);
            sb.append(" ");
        }
        sb.append(branch);
        sb.append(" ");
        sb.append(currBranchHead);
        sb.append(" ");
        Utils.writeContents(f, sb.toString());
    }

    private static void deleteRepoInfo(String branch) {
        File f = new File(GITLET_DIR, "repo_info.txt");
        String[] repoInfo = getRepoInfo();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repoInfo.length; i+=2) {
            if (repoInfo[i].equals(branch)) continue;
            sb.append(repoInfo[i]);
            sb.append(" ");
            sb.append(repoInfo[i+1]);
            sb.append(" ");
        }
        Utils.writeContents(f, sb.toString());
    }

    private static void setBranchInfo(String branch) {
        File f = new File(GITLET_DIR, "branch_info.txt");
        Utils.writeContents(f, branch);
    }

    private static void makeAddFileDir() {
        ADDITION_DIR.mkdir();
    }

    public static void commit(String message) {
        Commit newCommit = getHeadCommit();

        Blob[] addBlobs = retrieveBlobs(ADDITION_DIR);
        Blob[] rmBlobs = retrieveBlobs(REMOVAL_DIR);
        // check staging area have file.
        if (addBlobs == null && rmBlobs == null) {
            System.out.print("No changes added to the commit. \n");
            System.exit(0);
        }
        // read files from staging area and set data to current commit.
        if (addBlobs != null) {
            newCommit.setBlobs(addBlobs);
        }
        // read files from removal area and remove the files in current commit.
        if (rmBlobs != null) {
            newCommit.removeBlobs(rmBlobs);
        }
        //set parents to current branch head commit.
        newCommit.setParents(newCommit.getHashCode());

        //set message.
        newCommit.setMessage(message);

        // data persistence and clear staging area.
        String hashCode = newCommit.getCommitSha1();
        newCommit.setHashCode(hashCode);
        File curCommitFile = join(COMMITS_DIR, hashCode);
        writeObject(curCommitFile, newCommit);
        if (addBlobs != null) {
            for (Blob b : addBlobs) {
                join(ADDITION_DIR, b.getFileName()).delete();
                File BlobsFile = join(BLOBS_DIR, b.getHashCode());
                writeObject(BlobsFile, b);
            }
        }
        if (rmBlobs != null) {
            for (Blob b : rmBlobs) {
                join(REMOVAL_DIR, b.getFileName()).delete();
            }
        }
        // forwarding the head.
        setRepoInfo(getBranchInfo(), hashCode);
    }


    private static Blob[] retrieveBlobs(File path) {
        File[] fileList = path.listFiles();
        if (fileList == null) return null;
        Blob[] Blobs = new Blob[fileList.length];
        for (int i = 0; i < fileList.length; i++) {
            Blobs[i] = readObject(fileList[i], Blob.class);
        }
        return Blobs;
    }

    private static boolean untrackFile() {
        File[] fileList = CWD.listFiles();
        Commit head = getHeadCommit();
        if (fileList == null && head.getBlobs() != null || fileList != null && head.getBlobs() == null) {
            return true;
        } else if (fileList == null) {
            return false;
        }

        if (head.getBlobs().size() != fileList.length) return true;

        for (File f : fileList) {
            Blob fileBlob = fileToBlob(f);
            if (!head.getBlobs().get(fileBlob.getFileName()).equals(fileBlob.getHashCode())) return true;
        }

        return false;
    }

    private static Blob fileToBlob(File file) {
        String content = readContentsAsString(file);
        return new Blob(file.getName(), content, sha1(content));
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
            Blob removalBlob = retrieveBlob(removalHashCode);
            writeObject(removalBlobFile, removalBlob);

            //remove the file from the working directory.
            File cwdFile = join(CWD, filename);
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
        Commit[] commits = retrieveCommits();
        List<String> matchedId = new ArrayList<>();
        for (Commit commit : commits) {
            if (commit.getMessage().indexOf(message) > 0) matchedId.add(commit.getCommitSha1());
        }
        if (matchedId.size() == 0) {
            System.out.print("Found no commit with that message. \n");
            System.exit(0);
        }
        for (String s : matchedId) {
            System.out.println(s + "\n");
        }
    }

    private static Commit[] retrieveCommits() {
        File[] filesList = COMMITS_DIR.listFiles();

        Commit[] commits = new Commit[filesList.length];
        for (int i = 0; i < filesList.length; i++) {
            commits[i] = readObject(filesList[i], Commit.class);
        }
        return commits;
    }

    public static void status() {
        //TODO show ropo status including branches, staged file, removed file.
        //Note: should maintain one text file to tracking those info.

        System.out.println("=== Branches === \n");

        String currBranch = getBranchInfo();
        String[] repoInfo = getRepoInfo();

        for (int i = 0; i < repoInfo.length; i+=2) {
            if (repoInfo[i].equals(currBranch)) {
                System.out.println("*"+repoInfo[i]+"\n");
            } else {
                System.out.println(repoInfo[i]+"\n");
            }
        }

        System.out.println("=== Staged Files === \n");
        Blob[] blobs = retrieveBlobs(ADDITION_DIR);
        if (blobs != null) {
            for (Blob b : blobs) {
                System.out.println(b.getFileName()+"\n");
            }
        }

        System.out.println("=== Removed Files === \n");
        blobs = retrieveBlobs(REMOVAL_DIR);
        if (blobs != null) {
            for (Blob b : blobs) {
                System.out.println(b.getFileName()+"\n");
            }
        }
    }

    public static void checkoutFile(String filename) {
        //TODO 1. takes version of the file to CWD from head of branch.
        checkout(getHeadCommit(), filename);
    }

    public static void checkoutFileFromId(String commitId, String filename) {
        //TODO 2. takes version of the file to CWD from specific commit.
        try {
            checkout(retrieveCommit(commitId), filename);
        } catch (IllegalArgumentException e) {
            System.out.print("No commit with that id exists. \n");
            System.exit(0);
        }
    }

    public static void checkoutBranch(String branchName) {
        //TODO 3. takes all the file to CWD from the specific branch head of commit.
        if (branchName.equals(getBranchInfo())) {
            System.out.print("No need to checkout the current branch. \n");
            System.exit(0);
        }
        String[] repoInfo = getRepoInfo();
        String branchHeadHash = "";
        for (int i = 0; i < repoInfo.length; i++) {
            if (repoInfo[i].equals(branchName)) branchHeadHash = repoInfo[i+1];
        }
        if (branchHeadHash.equals("")) {
            System.out.print("No such branch exists. \n");
            System.exit(0);
        }

        Commit version = retrieveCommit(branchHeadHash);

        for (String s : version.getBlobs().keySet()) {
            checkout(version, s);
        }
        setBranchInfo(branchName);
    }
    private static void checkout(Commit version, String filename) {
        if (untrackFile()) {
            System.out.print("There is an untracked file in the way; delete it, or add and commit it first. \n");
            System.exit(0);
        }
        String targetBlobHash = "";
        for (String s : version.getBlobs().keySet()) {
            if (s.equals(filename)) targetBlobHash = version.getBlobs().get(s);
        }
        if (targetBlobHash.equals("")) {
            System.out.print("File does not exist in that commit. \n");
            System.exit(0);
        }

        Blob targetBlob = retrieveBlob(targetBlobHash);
        File cwdFile = join(CWD, targetBlob.getFileName());
        writeContents(cwdFile, targetBlob.getContent());
    }

    public static void branch(String branchName) {
        //TODO 1. use a file to record each branch with a corresponding commit.
        if (checkBranchExist(branchName)) {
            System.out.print("A branch with that name already exists. \n");
            System.exit(0);
        }
        addRepoInfo(branchName);
    }

    public static void removeBranch(String branchName) {
        //TODO 1. delete data related to specified branch name in the branch file.
        if (!checkBranchExist(branchName)) {
            System.out.print("A branch with that name does not exist. \n");
            System.exit(0);
        }
        if (branchName.equals(getBranchInfo())) {
            System.out.print("Cannot remove the current branch. \n");
            System.exit(0);
        }
        deleteRepoInfo(branchName);
    }

    private static boolean checkBranchExist(String branchName) {
        String[] repoInfo = getRepoInfo();
        for (int i = 0; i < repoInfo.length; i+=2) {
            if (branchName.equals(repoInfo[i])) {
                return true;
            }
        }
        return false;
    }

    public static void reset(String commitId) {
        //TODO 1.  moves the current branch's head to that commit node.
        //TODO 2. clear the staged area.
        try {
            Commit version = retrieveCommit(commitId);
            if (!version.getBranch().equals(getBranchInfo())) {
                System.out.print("No commit with that id exists. \n");
                System.exit(0);
            }
            Set<String> set = new HashSet<>();
            for (String s : version.getBlobs().keySet()) {
                set.add(s);
                checkout(version, s);
            }
            File[] filesList = CWD.listFiles();
            assert filesList != null;
            for (File f : filesList) {
                if (!set.contains(f.getName())) f.delete();
            }
            setRepoInfo(getBranchInfo(), commitId);
        } catch (NullPointerException e) {
            System.out.print("No commit with that id exists. \n");
            System.exit(0);
        }
    }


    public static void merge(String branchName) {
        //TODO 1. if LCA is the given branch we do noting just print message.
        //TODO 2. if LCA is the current branch we fast-forward.

        String headId = getBranchHeadId(getBranchInfo());
        Commit head = retrieveCommit(headId);

        String otherId = getBranchHeadId(branchName);
        Commit other = retrieveCommit(otherId);
        Commit splitPoint = getLatestCommonAncestor(head, other);

        if (splitPoint.getHashCode().equals(other.getHashCode())) {
            System.out.print("Given branch is an ancestor of the current branch. \n");
            System.exit(0);
        }

        if (splitPoint.getHashCode().equals(head.getHashCode())) {
            checkoutBranch(branchName);
            System.out.print("Current branch fast-forwarded. \n");
            System.exit(0);
        }

        Map<String, String> headDiff = compareBlobs(splitPoint.getBlobs(), head.getBlobs());
        Map<String, String> otherDiff = compareBlobs(splitPoint.getBlobs(), other.getBlobs());

        Set<String> commitFileName = new HashSet<>();
        commitFileName.addAll(headDiff.keySet());
        commitFileName.addAll(otherDiff.keySet());

        //main logic :
        //TODO rule 1. modified in other but not head : Other.
        //TODO rule 2. modified in head but not other : Head.
        //TODO rule 3. modified in other and ead : 1. in same way : DNM 2. in diff way : conflict.
        //TODO rule 4. not in split and other but in head : Head.
        //TODO rule 5. not in split and head but in other : Other.
        //TODO rule 6. unmodified in head but not present in other : Remove.
        //TODO rule 7. unmodified in other but not present in head : Remain or Remove.
        //Note : Result in Head should consider merge commit is base on head commit.

        boolean conflictFlag = false;
        for (String s : commitFileName) {
            if (headDiff.containsKey(s) && otherDiff.containsKey(s)) {
                if (!headDiff.get(s).equals(otherDiff.get(s))) {
                    //conflict
                    conflictFlag = true;
                    Blob headBlob = retrieveBlob(headDiff.get(s));
                    Blob otherBlob = retrieveBlob(otherDiff.get(s));

                    String conflictContent = "<<<<<<< HEAD \n"
                            + headBlob.getContent() + "======= \n"
                            + otherBlob.getContent() + ">>>>>>>";

                    stageFile(s, conflictContent);
                }
            } else if (otherDiff.containsKey(s)){
                if (otherDiff.get(s).equals("")) {
                    // stage for removal.
                    remove(s);
                } else {
                    //stage for addiction.
                    Blob otherBlob = retrieveBlob(otherDiff.get(s));
                    stageFile(s, otherBlob.getContent());
                }
            }
        }
        String mergeMessage = "Merged [" + branchName + "] into [" + getBranchInfo() + "].";
        mergeCommit(mergeMessage, headId, otherId);
        if (conflictFlag) System.out.print("Encountered a merge conflict. \n");
    }

    private static void mergeCommit(String message, String headId, String otherId) {
        Commit newCommit = getHeadCommit();

        Blob[] addBlobs = retrieveBlobs(ADDITION_DIR);
        Blob[] rmBlobs = retrieveBlobs(REMOVAL_DIR);
        if (addBlobs != null) {
            newCommit.setBlobs(addBlobs);
        }
        if (rmBlobs != null) {
            newCommit.removeBlobs(rmBlobs);
        }
        newCommit.setParents(headId, otherId);
        newCommit.setMessage(message);

        String hashCode = newCommit.getCommitSha1();
        newCommit.setHashCode(hashCode);
        File curCommitFile = join(COMMITS_DIR, hashCode);
        writeObject(curCommitFile, newCommit);
        if (addBlobs != null) {
            for (Blob b : addBlobs) {
                join(ADDITION_DIR, b.getFileName()).delete();
                File BlobsFile = join(BLOBS_DIR, b.getHashCode());
                writeObject(BlobsFile, b.getHashCode());
            }
        }
        if (rmBlobs != null) {
            for (Blob b : rmBlobs) {
                join(REMOVAL_DIR, b.getFileName()).delete();
            }
        }
        setRepoInfo(getBranchInfo(), hashCode);
    }

    private static void stageFile(String filename, String fileContent) {
        File addBlobFile = join(ADDITION_DIR, filename);
        Blob addBlob = new Blob(filename, fileContent, sha1(fileContent));
        writeObject(addBlobFile, addBlob);
    }

    private static String getBranchHeadId(String branchName) {
        String headId = "";
        String[] repoInfo = getRepoInfo();
        for (int i = 0; i < repoInfo.length; i+=2) {
            if (repoInfo[i].equals(branchName)) headId = repoInfo[i+1];
        }

        return headId;
    }

    private static Commit getLatestCommonAncestor(Commit c1, Commit c2) {
        int depth1 = getCommitDepth(c1);
        int depth2 = getCommitDepth(c2);

        if (depth1 > depth2) {
            int offset = depth1 - depth2, i = 0;
            while (i < offset) {
                c1 = retrieveCommit(c1.getParents().get(0));
            }
        } else if ((depth2 > depth1)) {
            int offset = depth2 - depth1, i = 0;
            while (i < offset) {
                c2 = retrieveCommit(c2.getParents().get(0));
            }
        }

        Set<String> set = new HashSet<>();
        String lcaId = "";
        while (c1 != null || c2 != null) {
            for (String s : c1.getParents()) {
                if (set.contains(s)) {
                    lcaId = s;
                    break;
                } else {
                    set.add(s);
                }
            }

            for (String s : c2.getParents()) {
                if (set.contains(s)) {
                    lcaId = s;
                    break;
                } else {
                    set.add(s);
                }
            }

            c1 = retrieveCommit(c1.getParents().get(0));
            c2 = retrieveCommit(c2.getParents().get(0));
        }


        return retrieveCommit(lcaId);
    }

    private static int getCommitDepth(Commit head) {
        int depth = 0;
        Commit curr = head;
        while (curr != null) {
            depth++;
            String ancestorId = curr.getParents().get(0);
            if (ancestorId == null) {
                curr = null;
            } else {
                curr = retrieveCommit(ancestorId);
            }
        }

        return depth;
    }

    private static Map<String, String> compareBlobs(Map<String, String> base, Map<String, String> other) {
        Set<String> set = new HashSet<>();
        Map<String, String> res = new HashMap<>();

        for (String s : base.keySet()) {
            set.add(s);
        }
        for (String s : other.keySet()) {
            set.add(s);
        }

        for (String s : set) {
            if (other.containsKey(s)) {
                if (!base.containsKey(s) || !other.get(s).equals(base.get(s))) { // modified in other or added in other
                    res.put(s, other.get(s));
                }
            } else {                                    // removed in other
                res.put(s, "");
            }
        }

        return res;
    }

}
