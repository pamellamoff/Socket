/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;
import javax.swing.*;
import java.io.File;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final File[] fileToSend = new File[1];
        
        // Set the frame to house everything
        JFrame jFrame = new JFrame("Client Code");
        // Defined the size of the width and height of the frame.
        jFrame.setSize(450, 450);
        // Makes the layout a box layout that places your children on top of each other.
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        // When the frame is closed, the program will terminate successfully.
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Title setting above the panel.
        JLabel jlTitle = new JLabel("File Sender");
        // Change the font family, size, and style.
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        // Add a border around the label for spacing.
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        // Make it so the title is centered horizontally.
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Label that has the file name.
        JLabel jlFileName = new JLabel("Choose a file to send.");
        // Change the font.
        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
        // Make a border for spacing.
        jlFileName.setBorder(new EmptyBorder(50, 0, 0, 0));
        // Center the label on the x axis (horizontally).
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel that contains the buttons.
        JPanel jpButton = new JPanel();
        // Border for panel that houses buttons.
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));
        
        // Create send file button.
        JButton jbSendFile = new JButton("Send File");
        // Size definition for layout containers.
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        // Change the font style, type, and size for the button.
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Second button to choose a file.
        JButton jbChooseFile = new JButton("Choose File");
        // Size definition for layout containers
        jbChooseFile.setPreferredSize(new Dimension(150, 75));
        // Change the font style, type, and size for the button.
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Add the buttons to the panel.
        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);
        
        // Button action for choosing the file.
        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creating a file selector to open the box to choose a file.
                JFileChooser jFileChooser = new JFileChooser();
                // Box title.
                jFileChooser.setDialogTitle("Choose a file to send.");
                // Show the box and, if a file is chosen, execute the instructions.
                if (jFileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file.
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    // Change the text of the java swing label to have the file name.
                    jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());
                }
            }
        });
        
        // Sends the file when the button is clicked.
        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If a file has not yet been selected then display this message.
                if (fileToSend[0] == null) {
                    jlFileName.setText("Please choose a file to send first!");
                    // If a file has been selected then do the following.
                } else {
                    try {
                        // Creating the input stream in the file you want to upload.
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                       // Creation of socket connection to connect to server.
                        Socket socket = new Socket("localhost", 1997);
                        
                        // Creation of the output stream to write and write to the server through the socket connection.
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        
                        // Take the name of the file you want to upload and store it in file name.
                        String fileName = fileToSend[0].getName();
                        // Convert the filename into a byte array to be sent to the server.
                        byte[] fileNameBytes = fileName.getBytes();
                        
                        // Creation of the byte array on file size.
                        byte[] fileBytes = new byte[(int)fileToSend[0].length()];
                        // The contents of the file are placed in the byte array so that these bytes can be sent to the server.
                        fileInputStream.read(fileBytes);
                        
                        // The length of the filename is sent so the server knows when to stop reading.
                        dataOutputStream.writeInt(fileNameBytes.length);
                        // Send the file name.
                        dataOutputStream.write(fileNameBytes);
                        
                        // The length of the byte array is sent so the server knows when to stop reading.
                        dataOutputStream.writeInt(fileBytes.length);
                        // Send the actual file.
                        dataOutputStream.write(fileBytes);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
            }
        });
        
        // Everything is added to the frame and made visible.
        jFrame.add(jlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);
        
    }
}
