public class Parser {
    private String commandName;
    private String[] args;
    private String type; 
    private String operator; 
    private String fileName; 


    /**
     * Parses the input string and returns a boolean value - Abdelrhman  
     * 
     * @param  input  the input string to be parsed
     * @return  true if the input string is successfully parsed, false otherwise
     */
    public boolean parse(String input){
    // Split the input string by spaces and store the tokens in an array
    String[] tokens = input.split(" ");

    // Set the command name to the first token
    commandName = tokens[0];
    // Check if the tokens length is greater than 1 and if the command name is either "ls" or "cp"
    if(tokens.length > 1 && ( ("ls".equals(commandName)) || ("cp".equals(commandName)))){
        // Check if the second token starts with a hyphen
        if(tokens[1].startsWith("-")){
            // Set the type to the second token
            type = tokens[1];
            // Call the parseHandler method with specific arguments
            return parseHandler(tokens, tokens.length -1, tokens.length-2, true);
            // Return true to indicate successful parsing
        }
    }


    // Call the parseHandler method with specific arguments
    return parseHandler(tokens, tokens.length, tokens.length-1, false);

    // Return true to indicate successful parsing
}
    
    /**
     * Parses the handler based on the given tokens, length, argsLength, and isPlus - Abdelrhman
     *
     * @param tokens      the array of tokens
     * @param length      the length of the tokens array
     * @param argsLength  the length of the args array
     * @param isPlus      a boolean indicating whether isPlus is true or false
     */
    private boolean parseHandler(String[] tokens, int length, int argsLength, boolean isPlus) {
        // ls > output.txt
        // tempArgs = 2;
        // TempArgs = [null , null];    
        boolean isOperator = false;
        String[] tempArgs = new String[argsLength];

        // Iterate through the tokens array
        for (int i = 1; i < length; i++) {
            // Check if the current token is an operator
            if (">>".equals(tokens[i]) || ">".equals(tokens[i])) {
                isOperator = true;
                operator = tokens[i];
                try {
                    fileName = tokens[i + 1];
                } catch (Exception e) {
                    return false;
                }
                break;
            }
            // Check if isPlus is true
            if (isPlus) {
                tempArgs[i - 1] = tokens[i + 1];
            } else {
                tempArgs[i - 1] = tokens[i];
            }
        }
        // Check if an operator is present
        if (!isOperator) {
            args = tempArgs;
        } else {
            args = new String[argsLength - 2];
            
            // Copy the elements from tempArgs to args
            for (int i = 0; i < argsLength - 2; i++) {
                args[i] = tempArgs[i];
            }
        }
        return true;
    }

    public String getCommandName(){
        return commandName;
    }
    public String getType(){
        return type;
    }
    public void Reset(){
        commandName = null;
        args = null;
        type = null;
        operator = null;
        fileName = null;
    }
    public String[] getArgs(){
        return args;
    }
    public String getOperator(){
        return operator;
    }
    public String getFileName(){
        return fileName;
    }
}
