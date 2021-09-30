package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                if (args.length < 2) {
                    System.out.print("Please enter a file name for addition. \n");
                    System.exit(0);
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.out.print("Please enter a commit message. \n");
                    System.exit(0);
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if (args.length < 2) {
                    System.out.print("Please enter a file name for removal. \n");
                    System.exit(0);
                }
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.log();
            case "global-log":
                Repository.globalLog();
            case "find":
                if (args.length < 2) {
                    System.out.print("Please enter a commit message. \n");
                    System.exit(0);
                }
                Repository.find(args[1]);
            // TODO: FILL THE REST IN
        }
    }
}
