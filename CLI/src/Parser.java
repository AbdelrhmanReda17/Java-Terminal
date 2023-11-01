public class Parser {
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public String[] addArg(String arg, String[] args, int size, int index) {
        String[] newArgs = new String[size + 1];

        for (int i = 0; i < size; i++) {
            newArgs[i] = args[i];
        }

        newArgs[size] = arg;
        return newArgs;
    }
    public boolean parse(String input) {
        StringTokenizer st = new StringTokenizer(input, " ");
        String command = st.nextToken();
        commandName = command;
        if (command.equals("echo") || command.equals("pwd") || command.equals("cd") || command.equals("ls") || command.equals("mkdir") || command.equals("rmdir") || command.equals("touch") || command.equals("cp") || command.equals("exit")) {
            commandName = command;
            int i = 0;
            while (st.hasMoreTokens()) {
                if (args != null) {
                    args = addArg(st.nextToken(), args, args.length, i);
                } else {
                    args = addArg(st.nextToken(), args, 0, i);
                }
                i++;
            }
            return true;
        } else {
            System.out.println("command not found : " + command);
            return false;
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}
