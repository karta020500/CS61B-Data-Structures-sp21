# Gitlet

This project implementing a version-control system that mimics some of the basic features of the popular system Git. Ours is smaller and simpler, however, so we have named it Gitlet.

A version-control system is essentially a backup system for related collections of files. The main functionality that Gitlet supports is:

1. Saving the contents of entire directories of files. In Gitlet, this is called committing, and the saved contents themselves are called commits.

2. Restoring a version of one or more files or entire commits. In Gitlet, this is called checking out those files or that commit.

3. Viewing the history of your backups. In Gitlet, you view this history in something called the log.

4. Maintaining related sequences of commits, called branches.

5. Merging changes made in one branch into another.

# The Commands

## init  
   * Usage: java gitlet.Main init  
   * Description: Creates a new Gitlet version-control system in the current directory. 
## add 
   * Usage: java gitlet.Main add [file name]
   * Description: Adds a copy of the file as it currently exists to the staging area. 
## commit 
   * Usage: java gitlet.Main commit [message]
   * Description: Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time, creating a new commit.
## rm 
   * Usage: java gitlet.Main rm [file name]
   * Description: Unstage the file if it is currently staged for addition.
## log 
   * Usage: java gitlet.Main log
   * Description: Starting at the current head commit, display information about each commit backwards along the commit tree until the initial commit.
## global-log 
   * Usage: java gitlet.Main global-log
   * Description: Like log, except displays information about all commits ever made.
## find
   * Usage: java gitlet.Main find [commit message]
   * Description: Prints out the ids of all commits that have the given commit message.
## status
   * Usage: java gitlet.Main status
   * Description: Displays what branches currently exist, and marks the current branch with a *.
## checkout
   * Usage: 
   1. java gitlet.Main checkout -- [file name]
   2. java gitlet.Main checkout [commit id] -- [file name]
   3. java gitlet.Main checkout [branch name]
   * Description: 
   1. Takes the version of the file as it exists in the head commit and puts it in the working directory.
   2. Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory.
   3. Takes all files in the commit at the head of the given branch, and puts them in the working directory.
## branch
   * Usage: java gitlet.Main branch [branch name]
   * Description: Creates a new branch with the given name, and points it at the current head commit.
## rm-branch
   * Usage: java gitlet.Main rm-branch [branch name]
   * Description: Deletes the branch with the given name.
## reset
   * Usage: java gitlet.Main reset [commit id]
   * Description: Checks out all the files tracked by the given commit.
## merge
   * Usage: java gitlet.Main merge [branch name]
   * Description: Merges files from the given branch into the current branch.
