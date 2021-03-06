/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Jan Darge
 */

package ucf.assignments;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class FileHandler {

    public static LinkedList<TaskToDoObj> tasks() {
        // fills up a linked list with data from the previously selected list (selected.txt)
        // if there is a next line
        // create a new task object w/ the next 4 lines (name,date,description,completed status)
        // returns the linked list of task objects

        LinkedList<TaskToDoObj> tasks = new LinkedList<>();

        try {
            File selected = new File(CastedUtilityGeneral.tempDirec() + "\\selected.txt");
            Scanner scanner = new Scanner(selected);

            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                String date = scanner.nextLine();
                String description = scanner.nextLine();
                String completed = scanner.nextLine();

                tasks.add(new TaskToDoObj(name, date, description, Boolean.parseBoolean(completed)));
            }

        } catch (Exception e) {
            System.out.println("Failed to read file.");
        }

        return tasks;
    }

    public static void renewFileAfterDelete(TaskToDoObj item) throws IOException {
        // after a task is deleted
        // read the selected.txt file and return the string of the content inside of it
        // remove deleted item from the string if its exists
        // rewrite file data with the corrected string data

        String fileContent = readSelectedFile();

        fileContent = removeTaskFileData(fileContent, item);

        fileSelectedGenerator(fileContent);
    }

    public static void writeHeader() throws IOException {
        // simply add the to-do lists name
        // ignore all other data
        // used for clear inside of the task window

        StringBuilder output = new StringBuilder();
        File document = new File(System.getProperty("user.dir") + "\\.temp\\selected.txt");
        Scanner scanner = new Scanner(document);

        output.append(scanner.nextLine());
        scanner.close();

        fileSelectedGenerator(output.toString());
    }

    public static String saveTaskData(LinkedList<TaskToDoObj> list, String selected_path) {
        // takes in a list of items
        // try to compile all the contents with in the list of items linked list into a single string
        // replace back to back newlines with a single newline for as long as there is a back to back new line
        // remove additional new line from the file
        // return compounded string

        try {
            StringBuilder content = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(selected_path));
            String list_title = br.readLine();

            content.append(list_title).append("\n");

            for (TaskToDoObj list_item : list) {
                content.append(list_item.getName()).append("\n").append(list_item.getDate()).append("\n").append(list_item.getDescription()).append("\n").append(list_item.isComplete()).append("\n");
            }

            String output = content.toString();

            while (output.contains("\n\n")) {
                output = output.replace("\n\n", "\n");
            }

            output = output.substring(0, output.length() - 1);

            return output;
        } catch (IOException e) {
            System.out.println("Failed to add additional tasks to the save file.");
        }

        return "";
    }

    public static void removeExtraNewLine(File file, String path) throws IOException {
        // takes a file and a file path
        // scans each line and compounds it into a string
        // replaces each back to back new line with a single newline
        // removes last line from the last character from the file
        // writes new content into in given file using the file path

        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }

        String output = content.toString();

        while (output.contains("\n\n")) {
            output = output.replace("\n\n", "\n");
        }

        output = output.substring(0, output.length() - 1);

        File updatedFile = new File(path);
        FileWriter fw = new FileWriter(updatedFile);

        fw.write(output);
        fw.close();
    }

    private static String readSelectedFile() throws FileNotFoundException {
        // reads the data provided inside of the selected.txt
        // returns all data as a string

        StringBuilder output = new StringBuilder();
        File document = new File(System.getProperty("user.dir") + "\\.temp\\selected.txt");
        Scanner scanner = new Scanner(document);

        while (scanner.hasNextLine()) {
            output.append(scanner.nextLine()).append("\n");
        }
        scanner.close();

        return output.toString();
    }

    private static void fileSelectedGenerator(String fileString) throws IOException {
        // takes the name of the file originally selected (list_#.txt)
        // copys all the data within that file into selected.txt

        String output = System.getProperty("user.dir") + "\\.temp\\selected.txt";

        FileWriter export = new FileWriter(output);
        PrintWriter writer = new PrintWriter(export);

        writer.println(fileString);

        writer.close();
    }

    private static String removeTaskFileData(String fileContent, TaskToDoObj item) {
        // look for the task name you want to delete from within the file content string
        // remove all data connected to it by replacing the data with a blank

        if (fileContent.contains(item.getName())) {
            String remove = item.getName() + "\n" + item.getDate() + "\n" + item.getDescription() + "\n" + item.isComplete() + "\n";

            return fileContent.replace(remove, "");
        }

        return fileContent;
    }

}
