//WRITTEN BY ASHAR ADEEL
//17 DECEMBER 2024
//VERSION 2

//BASE N CONVERTER INTEFACE
//CONVERT ANY NUMBER (>2mill) TO ANY BASE (2<x<35)


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class baseNConverterUI2 {
    //GLOBALS
    public static void createAndShowGUI(data user){
        JFrame frame = new JFrame("BaseNConverter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLayout(new GridLayout(7, 1, 10, 10)); // 10px horizontal and vertical gap
        frame.setResizable(true); // Allow resizing
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true); // Make the frame visible

        // UI Components
        JLabel title = new JLabel("BaseNConverter");
        Font defaultFont = UIManager.getFont("Label.font");
        title.setFont(defaultFont.deriveFont(24f)); // Keep system default font, change size to 18


        JButton infoButton = new JButton("Info");
        infoButton.addActionListener(e -> {
            // Create a new JFrame for the info window
            JFrame infoFrame = new JFrame("Information");
            infoFrame.setSize(500, 150);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the main one
            infoFrame.setLocationRelativeTo(null); // Center the frame on the screen
            infoFrame.setVisible(true); // Make the frame visible

            // Create a JPanel for the content with FlowLayout to center the components
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // Vertical alignment
            infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center all components

            // Create a label panel to center the labels horizontally
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS)); // Vertical alignment
            labelPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the labels

            // Add the information text
            labelPanel.add(new JLabel("BaseNConverter"));
            labelPanel.add(new JLabel("This is version 2. Written by ashar"));
            labelPanel.add(new JLabel("BASE: Must be between 2-35, will be encoded from '0-9' and 'a-z'."));
            labelPanel.add(new JLabel("VALUES: Must be positive, between 0 - 2 million."));

            // Add the labelPanel to the infoPanel
            infoPanel.add(labelPanel);

            // Add the Close button to the info window
            JButton closeButton = new JButton("Close");
            closeButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
            closeButton.addActionListener(closeEvent -> infoFrame.dispose()); // Close the info window

            // Add the Close button to the infoPanel
            infoPanel.add(closeButton);

            // Add the panel to the info frame and make it visible
            infoFrame.add(infoPanel);
            infoFrame.setVisible(true);
        });



        JLabel baseLabel = new JLabel("Base (2-35):");
        JTextField baseField = new JTextField();

        JLabel valueLabel = new JLabel("Value (0-2000000):");
        JTextField valueField = new JTextField();

        JButton convertButton = new JButton("Convert");
        convertButton.setEnabled(true); // Initially disabled
        // Inside createAndShowGUI(data user) method
        JLabel resultLabel = new JLabel("Converted Value: ");
        // Action listener for the convertButton
        // Inside createAndShowGUI(data user) method

        // Action listener for the convertButton
        convertButton.addActionListener(e -> {
            // Get the base and value from the text fields
            String baseInput = baseField.getText();
            String valueInput = valueField.getText();

            // Validate base (must be between 2 and 35)
            if (!isInteger(baseInput) || Integer.parseInt(baseInput) < 2 || Integer.parseInt(baseInput) > 35) {
                JOptionPane.showMessageDialog(frame, "Invalid Base. Must be between 2 and 35.", "Invalid", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate value (must be between 0 and 2 million)
            if (!isInteger(valueInput) || Integer.parseInt(valueInput) < 0 || Integer.parseInt(valueInput) > 2000000) {
                JOptionPane.showMessageDialog(frame, "Invalid Value. Must be between 0 and 2 million.", "Invalid", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set the values in the user object
            user.base = Integer.parseInt(baseInput);
            user.inputValue = Integer.parseInt(valueInput);

            // Perform the conversion
            convertToBase(user);  // This will update the user.outputValue

            // Update the resultLabel with the converted value from the user object
            resultLabel.setText("Converted Value: " + getOutput(user));

            // Print the result to the console (optional)
            System.out.println("Converted Value: " + getOutput(user));

            // Save the user data
            try {
                saveUserData(user);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred while saving data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        resultLabel.setText("Converted Value: #" + getOutput(user));

        // Add components to the frame
        frame.add(title);
        frame.add(infoButton);
        frame.add(baseLabel);
        frame.add(baseField);
        frame.add(valueLabel);
        frame.add(valueField);
        frame.add(new JLabel()); // Empty placeholder
        frame.add(convertButton);
        frame.add(resultLabel);

        frame.setVisible(true);
    }
    public static void main(String[] p) throws IOException{
        data user = createUser(-1,null,-1,null);

        SwingUtilities.invokeLater(() -> createAndShowGUI(user));

        System.out.println("BaseNConverter.");
        System.out.println("BASE: Must be between 2-35, will be encoded from '0-9' and 'a-z' ");
        System.out.println("VALUEs: Must be positive, between 0 - 2 million.");
        System.out.println("Written by: ashar adeel");
        String askUse = input("Would you like to use BaseNConverter? (y/n)");
        while (!askUse.equalsIgnoreCase("y") && !askUse.equalsIgnoreCase("n")) { // If input is invalid
            askUse = input("Invalid input, choose between y/n.");
        }
        while (askUse.equalsIgnoreCase("y")) { // If yes, do this
            baseConverter(user);
            //SAVE CHECK
            String askSave = input("Would you like to save user data? (y/n)");{
                while(!askSave.equalsIgnoreCase("y")){
                    askSave = input("Invalid input, choose between y/n.");
                }
                if(askSave.equalsIgnoreCase("y")){
                    saveUserData(user);
                }
            }
            askUse = input("Would you like to convert another value? (y/n)");
        }
        if (askUse.equalsIgnoreCase("n")) { // If no, print this and close
            System.out.println("Thank you for using BaseNConverter");
        }

    }

    //SAVE SYSTEM
    public static void saveUserData(data user) throws IOException {
        final String filename = "bnconverter.dat";
        File file = new File(filename + ".csv");

        // If the file exists, we append new data below the previous data
        boolean fileExists = file.exists();

        // Create or open the file for writing
        try (PrintWriter fileWrite = new PrintWriter(new FileWriter(filename + ".csv", true))) {
            // If the file doesn't exist, write the header
            if (!fileExists) {
                fileWrite.println("BaseNConverter, written by ashar adeel");
                fileWrite.println("BASE, INPUT VALUE, CONVERTED VALUE, STATUS, TIME, DATE");
            }

            // Write the new user data below the previous results
            fileWrite.println(getBase(user) + "," + getInput(user) + "," + getOutput(user) + ",SAVED," + sysTime());
            System.out.println("User data saved successfully for " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while saving user data.");
            e.printStackTrace();
        }
    }

    //PARENT METHOD
    public static void baseConverter(data user){
        takeInputs(user);
        String value = assignedValue(getInput(user));
        System.out.println("VALUE = #" + convertToBase(user));
    }

    //CONVERSION CALCULATOR
    public static String convertToBase(data user){
        //DECLARE VARIABLES
        int base = getBase(user);
        int inputValue = getInput(user);
        int pointer = inputValue / base;
        String[] values = new String[pointer + 1];

        //CONVERSION
        while (inputValue != 0) {
            int remainder = inputValue % base;
            values[pointer] = assignedValue(remainder);
            inputValue = inputValue / base;
            pointer = pointer - 1;
        }

        //SET VALUES
        String output = buildOutput(values);
        setOutput(user,output);
        return output;
    }

    //BUILD FINAL RESULT
    public static String buildOutput(String[] values) {
        String result = "";
        for (String val : values) {
            if (val != null) {
                result += val; // Concatenate the non-null values
            }
        }
        return result;
    }

    public static String assignedValue(int value){
        String assVal = "";
        if(value>=0 && value <=9){
            assVal = String.valueOf(value);
        } else if(value == 10){
            assVal = "a";
        } else if(value == 11){
            assVal = "b";
        } else if(value == 12){
            assVal = "c";
        } else if(value == 13){
            assVal = "d";
        } else if(value == 14){
            assVal = "e";
        } else if(value == 15){
            assVal = "f";
        } else if (value == 16) {
            assVal = "g";
        } else if (value == 17) {
            assVal = "h";
        } else if (value == 18) {
            assVal = "i";
        } else if (value == 19) {
            assVal = "j";
        } else if (value == 20) {
            assVal = "k";
        } else if (value == 21) {
            assVal = "l";
        } else if (value == 22) {
            assVal = "m";
        } else if (value == 23) {
            assVal = "n";
        } else if (value == 24) {
            assVal = "o";
        } else if (value == 25) {
            assVal = "p";
        } else if (value == 26) {
            assVal = "q";
        } else if (value == 27) {
            assVal = "r";
        } else if (value == 28) {
            assVal = "s";
        } else if (value == 29) {
            assVal = "t";
        } else if (value == 30) {
            assVal = "u";
        } else if (value == 31) {
            assVal = "v";
        } else if (value == 32) {
            assVal = "w";
        } else if (value == 33) {
            assVal = "x";
        } else if (value == 34) {
            assVal = "y";
        } else if (value == 35) {
            assVal = "z";
        }
        return assVal;
    }

    //GATHER INPUTS
    public static void takeInputs(data user){
        //take base
        String userBase = input("Insert Base: "); //BASE GREATER THAN 2 AND LESS THAN 35
        while(!isInteger(userBase) || Integer.parseInt(userBase)<2 || Integer.parseInt(userBase)>35){
            userBase = input("Please enter a valid integer.");
        }
        //take value
        String userValue = input("Insert Value: "); //POSITIVE ONLY, must be less than 20000
        while(!isInteger(userValue) || Integer.parseInt(userValue)<0 || Integer.parseInt(userValue)>2000000){
            userValue = input("Please enter a valid integer.");
        }
        //set values
        setBase(user,Integer.parseInt(userBase));
        setInput(user,Integer.parseInt(userValue));
    }

    //CHECK IF INT
    public static boolean isInteger(String inputValue) {
        // Check if input is null or empty
        if (inputValue == null || inputValue.isEmpty())
        {
            return false;
        }

        // Check for the negative sign at the beginning
        int startIndex = 0;
        if (inputValue.charAt(0) == '-')
        {
            startIndex = 1;
        }

        // Check that every character after the negative sign (if present) is a digit
        for (int i = startIndex; i < inputValue.length(); i++) {
            if (!Character.isDigit(inputValue.charAt(i)))
            {
                return false;  // If any character is not a digit, return false
            }
        }

        return inputValue.length() > startIndex;  // Ensure that the string has at least one digit
    }

    //GATHER INPUTS
    public static String input(String text){
        System.out.println(text);
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        return answer;
    }

    //GET SYSTEM TIME FOR SAVE
    public static String sysTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, dd-MM-yyyy");
        return now.format(formatter);
    }

    //  //  ADT METHODS
    //CREATE USER
    public static data createUser(int base, String out, int in, String x){
        data user = new data();
        user.base = base;
        user.outputValue = out;
        user.inputValue = in;
        user.extra = x;
        return user;
    }
    //GETTERS
    public static int getBase(data d) {
        return d.base;
    }
    public static String getOutput(data d) {
        return d.outputValue;
    }
    public static int getInput(data d) {
        return d.inputValue;
    }
    public static String getExtra(data d) {
        return d.extra;
    }
    //SETTERS
    public static void setBase(data d, int base) {
        d.base = base;
    }
    public static void setOutput(data d, String outputValue) {
        d.outputValue = outputValue;
    }
    public static void setInput(data d, int inputValue) {
        d.inputValue = inputValue;
    }
    public static void setExtra(data d, String extra) {
        d.extra = extra;
    }

}