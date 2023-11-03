import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Terminal {
    private File currentDirectory;
    private Parser parser; 
    private List<String> commandHistory;

    public Terminal() {
        currentDirectory = new File(System.getProperty("user.home"));
        parser = new Parser();
        commandHistory = new ArrayList<String>();
    }

    // pwd > output.txt

    public String pwd(){
        return(currentDirectory.getAbsolutePath());
    }
    public void cd(String[] args){
        if(args.length == 0){
            currentDirectory = new File(System.getProperty("user.home"));
        }
        else if(args[0].equals("..")){
            String[] path = currentDirectory.getAbsolutePath().split("\\\\");
            String newPath = "";
            for(int i = 0; i < path.length - 1; i++){
                newPath += path[i];
                newPath += File.separator;
            }
            currentDirectory = new File(newPath);
        }
        else{
            if(args[0].startsWith("C:")){
                currentDirectory = new File(args[0]);
            }
            else if(!args[0].startsWith(File.separator)){
                File[] dir = currentDirectory.listFiles();
                boolean found = false;
                for(File f : dir){
                    if(f.getName().equals(args[0])){
                        found = true;
                        break;
                    }
                }
                if(found){
                    currentDirectory = new File(currentDirectory.getAbsolutePath() + File.separator + args[0]);
                }
                else{
                    System.out.println("The system cannot find the path specified.");
                }
                
            }
            else{
                currentDirectory = new File(currentDirectory.getAbsolutePath().substring(0, 2),args[0]);
            }
        }
    }
    public void mkdir(String[] args){
        File f = new File(currentDirectory.getAbsolutePath());
        if(args.length == 1){
            if(args[0].charAt(1) == ':'){
                f = new File(args[0]);
            }
            else if(args[0].startsWith(File.separator)){
                f = new File(currentDirectory.getAbsolutePath().substring(0, 2),args[0]);
            }
            else{
                f = new File(currentDirectory.getAbsolutePath(),File.separator+args[0]);
            }
            if(!f.mkdir()){
                System.out.println("The system cannot find the path specified.");
            }
        }
        else{
            for(int i = 0; i < args.length - 1; i++){
                String[] newArgs = {args[args.length-1]+File.separator+args[i]};
                mkdir(newArgs);
            }
        }
        
        }
        

    public String echo(String[] args) {return null;}
    public List<File> ls(String[] args, String type) {return null;}
    public void rmdir(String[] args) {}
        /**
     * Creates a new file with the given target path - Yassin
     * @param args a String array containing the target path of the file
     * @throws IOException
     */
    public void touch(String[] args) {
        // Check if the number of arguments is not equal to 1
        if (args.length != 1) {
            System.out.println("Invalid arguments");
            return;
        }

        // Get the target path from the arguments
        String target = args[0];
        File newFile;

        // Check if the target path starts with a file separator
        if (target.startsWith(File.separator)) {
            // Get the current partition of the current directory
            String currentPartition = currentDirectory.getAbsolutePath().substring(0, 2);
            // Create a new file using the current partition and target path
            newFile = new File(currentPartition, target);
        } else {
            // Create a new file using the current directory and target path
            newFile = new File(currentDirectory, target);
        }

        try {
            // Try to create a new file
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getAbsolutePath());
            } else {
                System.out.println("File already exists or could not be created: " + newFile.getAbsolutePath());
            }
        } catch (IOException e) {
            // Catch any IO exception that occurred while creating the file
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
    }
    
    /**
     * Copies a file from a source location to a destination location - Yassin
     * @param  args      an array of two strings representing the source and destination paths
     * @param  type      a string representing the type of copy operation ("-r" for recursive, otherwise non-recursive)
     */
    public void cp(String[] args, String type) {
        // Check if the number of arguments is valid
        if (args.length != 2) {
            System.out.println("Invalid arguments");
            return;
        }

        String source = args[0];
        String destination = args[1];
        File sourceFile;
        File destinationFile;

        try {
            String currentPartition = currentDirectory.getAbsolutePath().substring(0, 2);

            // Create the source file based on the given source path
            if (source.startsWith(File.separator)) {
                sourceFile = new File(currentPartition, source);
            } else if (source.charAt(1) == ':') {
                sourceFile = new File(source);
            } else {
                sourceFile = new File(currentDirectory, source);
            }

            // Create the destination file based on the given destination path
            if (destination.startsWith(File.separator)) {
                destinationFile = new File(currentPartition, destination);
            } else if (destination.charAt(1) == ':') {
                destinationFile = new File(destination);
            } else {
                destinationFile = new File(currentDirectory, destination);
            }

            // Perform the copy operation based on the given type
            if ("-r".equals(type)) {
                Files.copy(destinationFile.toPath(), sourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while copying the files: " + e.getMessage());
        }
    }
    
   /**
     * Retrieves the history of commands executed. - Yassin
     * @author Yassin Tareek
     * @return  a string representing the command history
     */
    public String history() {
        // Initialize an empty string to store the command history
        String result = "";
        // Iterate over the command history list
        for (int i = 0; i < commandHistory.size(); i++) {
            // Append the command number, command, and a new line character to the result string
            result += (i + 1) + " " + commandHistory.get(i) + "\n";
        }
        // Return the command history as a string
        return result;
    }

    
    /**
     * Removes a file. - Abdelrhman
     *
     * @param args an array of arguments containing the path of the file to be deleted
     */
    public void rm(String[] args) {
        // Check if the number of arguments is valid
        if (args.length > 1) {
            System.out.println("Usage : rm <filename>");
            return;
        }

        // Create a file object for the file to be deleted
        File fileToDelete = new File(currentDirectory.getAbsolutePath() + File.separator + args[0]);

        // Check if the file exists
        if (!fileToDelete.exists()) {
            System.out.println("File doesn't exist!");
            return;
        }

        // Delete the file
        if (fileToDelete.delete()) {
            System.out.println("File has been deleted successfully!");
        } else {
            System.out.println("Failed to delete the file!");
        }
    }
        
    /**
     * Counts the number of lines, words, and characters in a file. - Abdelrhman
     *
     * @param args an array of command line arguments, where args[0] is the filename
     * @return a string containing the number of lines, words, and characters in the file, along with the filename
     *         or null if the filename is missing or an error occurs
     */
    public String wc(String[] args) {
        // Check if the filename is missing
        if (args.length != 1) {
            System.out.println("Usage: wc <filename>");
            return null;
        }

        // Create a File object for the specified file
        File file = new File(currentDirectory.getAbsolutePath() + File.separator + args[0]);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lines = 0;
            int words = 0;
            int characters = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                String[] wordsInLine = line.split("\\s+"); // Split the line into words
                words += wordsInLine.length;
                characters += line.length();
            }

            // Return the count of lines, words, and characters, along with the filename
            return lines + " " + words + " " + characters + " " + args[0];
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * This function takes an array of strings as input and performs certain operations based on the number of arguments passed. - Abdelrhman
     * If only one argument is passed, it reads the file with the name specified in the argument and returns the contents of the file as a string.
     * If two arguments are passed, it reads the contents of the files specified in the arguments, combines them, and returns the combined contents as a string.
     * If any other number of arguments are passed, it prints an error message and returns null.
     *
     * @param  args  an array of strings representing the arguments passed to the function
     * @return       a string representing the contents of the file(s) read, or null if an error occurs
     */
    public String cat(String[] args) {
    try {
        // Get the current directory path
        String currentDirString = new String(currentDirectory.getAbsolutePath() + File.separator);
        
        // Create a file object for the first file
        File firstFile = new File (currentDirString + args[0]);
        
        // Create an output file object
        File outputFile = new File(currentDirString + "Output.txt");
        
        // Create a file reader object
        Scanner fileReader;
        
        // If only one argument is passed
        if (args.length == 1) {
            // Read the contents of the first file
            fileReader = new Scanner(firstFile);
        }
        // If two arguments are passed
        else if (args.length == 2) {
            // Create a file object for the second file
            File secondFile = new File (currentDirString + args[1]);
            
            // Read the contents of the first file and second file as byte arrays
            byte[] firstFileBytes = Files.readAllBytes(firstFile.toPath());
            byte[] secondFileBytes = Files.readAllBytes(secondFile.toPath());

            // Write the contents of the first file to the output file
            Files.write(outputFile.toPath(), firstFileBytes, StandardOpenOption.CREATE);
            
            // Append the contents of the second file to the output file
            Files.write(outputFile.toPath(), secondFileBytes, StandardOpenOption.APPEND);
            
            // Read the contents of the output file
            fileReader = new Scanner(outputFile);
        }
        else {
            // Print an error message and return null
            System.out.println("Invalid number of args!");
            return null;
        }
        
        String result = "";
        
        // Read each line from the file and append it to the result string
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            result += data + "\n";
        }
        
        // Close the file reader
        fileReader.close();
        
        // Delete the output file if it exists
        if (outputFile.exists()) {
            outputFile.delete();
        }
        
        // Return the result string
        return result;
    } catch (FileNotFoundException e) {
        // Print an error message and return null if a file is not found
        System.out.println("An error occurred.");
        return null;
    } catch (IOException e) {
        // Print an error message and return null if an IO exception occurs
        System.out.println("An error occurred.");
        return null;
    }
}
    
    /**
     * Perform operation (<, <<) on a file.  Abdelrhman
     *
     * @param operator       The operator to perform. Can be either "<" or "<<".
     * @param fileName       The name of the output file.
     * @param stringToWrite  The string to write to the file.
     */
    public void performOperation(String operator, String fileName, String stringToWrite) {    
        // Create a file object with the specified file name in the current directory
        File fileToWrite = new File(currentDirectory.getAbsolutePath() + File.separator + fileName);
        
        // Check if the operator is "<"
        if ("<".equals(operator)) {
            try (FileWriter writer = new FileWriter(fileToWrite)) {
                // Write the string to the file
                writer.write(stringToWrite);
            } catch (IOException e) {
                // Print an error message if an exception occurs
                System.out.println("An error occurred: " + e.getMessage());
            }
        } 
        // Check if the operator is "<<"
        else if ("<<".equals(operator)) {
            // Check if the file exists
            if (fileToWrite.exists()) {
                try (FileWriter writer = new FileWriter(fileToWrite)) {
                    // Write the string to the file
                    writer.write(stringToWrite);
                } catch (IOException e) {
                    // Print an error message if an exception occurs
                    System.out.println("An error occurred: " + e.getMessage());
                }
            } else {
                // Print a message if the file does not exist
                System.out.println("File does not exist: " + fileToWrite.getAbsolutePath());
            }
        }
    }

    /**
 * Executes the chosen command action based on user input.
 */
    public void chooseCommandAction() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(currentDirectory + "> ");
                String userInput = scanner.nextLine();
                if (userInput.equals("exit")) {
                    break;
                }
                boolean isParsed = parser.parse(userInput);

                if (!isParsed) {
                    System.out.println("Invalid command");
                    continue;
                }
                boolean isExecuted = false;

                String functionResult = "";
                String commandName = parser.getCommandName();
                String[] args = parser.getArgs();
                String operator = parser.getOperator();
                String fileName = parser.getFileName();

                switch (commandName) {
                    case "cd":
                        cd(args);
                        isExecuted = true;
                        break;
                    case "pwd":
                        functionResult = pwd();
                        if (functionResult != null) {
                            FunctionExecution(operator, fileName, functionResult);
                        }
                        isExecuted = true;
                        break;
                    case "echo":
                        functionResult = echo(args);
                        if (functionResult != null) {
                            FunctionExecution(operator, fileName, functionResult);
                        }
                        isExecuted = true;
                        break;
                    case "ls":
                        List<File> fileList = ls(args, parser.getType());
                        if (fileList != null) {
                            for (File file : fileList) {
                                functionResult += (file.getName() + " ");
                            }
                            FunctionExecution(operator, fileName, functionResult);
                        }
                        isExecuted = true;
                        break;
                    case "mkdir":
                        mkdir(args);
                        isExecuted = true;
                        break;
                    case "rmdir":
                        rmdir(args);
                        isExecuted = true;
                        break;
                    case "touch":
                        touch(args);
                        isExecuted = true;
                        break;
                    case "cp":
                        cp(args, parser.getType());
                        isExecuted = true;
                        break;
                    case "rm":
                        rm(args);
                        isExecuted = true;
                        break;
                    case "cat":
                        functionResult = cat(args);
                        if (functionResult != null) {
                            FunctionExecution(operator, fileName, functionResult);
                        }
                        isExecuted = true;
                        break;
                    case "history":
                        functionResult = history();
                        if (functionResult != null) {
                            FunctionExecution(operator, fileName, functionResult);
                        }
                        isExecuted = true;
                        break;
                    case "wc":
                        functionResult = wc(args);
                        if (functionResult != null) {
                            FunctionExecution(operator, fileName, functionResult);
                        }
                        isExecuted = true;
                        break;
                    default:
                        System.out.println("Command not found");
                        break;
                }
                if (isExecuted) {
                    commandHistory.add(userInput);
                }
                parser.Reset();
            }
        }
    }
    
    /**
     * Executes a function with the given operator, file name, and result - Abdelrhman
     *
     * @param  operator   the operator to be used in the function
     * @param  fileName   the name of the file to be processed
     * @param  Result     the result of the function execution
     */
    private void FunctionExecution(String operator, String fileName, String Result) {
        if (operator != null && fileName != null) {
            performOperation(operator, fileName, Result);
        } else {
            System.out.println(Result);
        }
    }
}
