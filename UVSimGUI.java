import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;

public class UVSimGUI extends JFrame {
    private UVCpu cpu;
    private String currentDirectoryPath = ".";
    private String currentFilePath = "";
    private JButton openButton;
    private JButton runButton;
    private JButton saveButton;
    private JTextArea programArea;
    private JTextArea outputArea;
    private JTextArea memoryArea;
    private JTextField inputField;
    private JButton inputButton;
    private JButton clearOutputButton;
    private JLabel statusLabel;
    private JLabel cpuLabel;
    private String pendingInput = null;
    private JButton restartButton;

    public UVSimGUI() {
        setupGUI();
        setupEvents();
    }

    private void setupGUI() {

        setTitle("UVSimGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        saveButton = new JButton("Save As");
        openButton = new JButton("Open Program File");
        runButton = new JButton("Run Program");
        runButton.setEnabled(false);
        clearOutputButton = new JButton("Clear Output");
        topPanel.add(clearOutputButton);
        restartButton = new JButton("Restart Program");
        topPanel.add(restartButton);
        statusLabel = new JLabel("No file loaded");

        topPanel.add(saveButton);
        topPanel.add(openButton);
        topPanel.add(runButton);
        topPanel.add(statusLabel);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));


        // Program panel
        JPanel programPanel = new JPanel(new BorderLayout());
        programPanel.add(new JLabel("Program:"), BorderLayout.NORTH);
        programArea = new JTextArea(10, 30);
        programArea.setEditable(true);
        programArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        programPanel.add(new JScrollPane(programArea), BorderLayout.CENTER);


        // Memory panel
        JPanel memoryPanel = new JPanel(new BorderLayout());
        memoryPanel.add(new JLabel("Memory:"), BorderLayout.NORTH);
        memoryArea = new JTextArea(10, 30);
        memoryArea.setEditable(false);
        memoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        memoryPanel.add(new JScrollPane(memoryArea), BorderLayout.CENTER);

        leftPanel.add(programPanel);
        leftPanel.add(memoryPanel);
        JPanel rightPanel = new JPanel(new BorderLayout());


        // Output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
        outputArea = new JTextArea(15, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);



        // Input section
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input:"), BorderLayout.NORTH);
        JPanel inputControls = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputButton = new JButton("Enter");
        inputButton.setEnabled(false);
        inputControls.add(inputField, BorderLayout.CENTER);
        inputControls.add(inputButton, BorderLayout.EAST);
        inputPanel.add(inputControls, BorderLayout.CENTER);


        rightPanel.add(outputPanel, BorderLayout.CENTER);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);



        JPanel bottomPanel = new JPanel();
        cpuLabel = new JLabel("CPU: Accumulator=0, Program Counter=0");
        cpuLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        bottomPanel.add(cpuLabel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void setupEvents() {
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runProgram();
            }
        });

        inputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleInput();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { saveFile();}
        });

        // allow enter key press
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleInput();
            }
        });
        clearOutputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                SwingUtilities.invokeLater(() -> {
                    runButton.setEnabled(true);
                    updateMemoryDisplay();
                    updateCPUStatus();
                });
            }
        });
        restartButton.addActionListener(e -> restart());
    }

    private void openFile() {

        JFileChooser chooser = new JFileChooser(currentDirectoryPath);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".txt");
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            currentFilePath = file.getAbsolutePath();
            currentDirectoryPath = file.getParent();
//            System.out.println("getParent: " + file.getParent());

            try {

                String content = new String(Files.readAllBytes(Paths.get(currentFilePath)));
                programArea.setText(content);


                cpu = new UVCpu(currentFilePath, this);


                statusLabel.setText("Loaded: " + file.getName());
                runButton.setEnabled(true);

                updateMemoryDisplay();
                outputArea.append("Program loaded successfully.\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
                statusLabel.setText("Error loading file");
            }
        }
    }

    private void saveFile() {
        String savedContent = programArea.getText();
        JFileChooser chooser = new JFileChooser(currentDirectoryPath);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".txt");
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        int savable = chooser.showSaveDialog(this);
        if (savable == JFileChooser.APPROVE_OPTION) {
            boolean validFormat = validateFileFormatting(savedContent);
            if (validFormat) {
                try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                    writer.write(savedContent.toString());
                    writer.flush();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
                }
            } else {
            JOptionPane.showMessageDialog(this, "Please have each line be one word each when saving.  Each word must be 4 digits long and have a plus or minus before the word.");
            }
        }
    }

    private void runProgram() {

        if (cpu == null) {
            // user can type program without loading file
            cpu = new UVCpu();
            UVConsole.setGUI(this);
        }


        outputArea.append("=== Running Program ===\n");
        runButton.setEnabled(false);

        // run program in another thread
        new Thread(() -> {
            try {
                loadProgramFromTextArea();

                cpu.run();
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("=== Program Finished ===\n");
                    runButton.setEnabled(true);
                    updateMemoryDisplay();
                    updateCPUStatus();
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("ERROR: " + e.getMessage() + "\n");
                    runButton.setEnabled(true);
                });
            }
        }).start();
    }

    private void loadProgramFromTextArea() {
        String content = programArea.getText();
        String[] lines = content.split("\n");

        // Reset CPU
        cpu.pc = 0;
        cpu.acc = 0;
        cpu.halted = false;

        // Clear memory
        for (int i = 0; i < 100; i++) {
            cpu.mem.write(i, 0);
        }

        // Load instructions from text area
        int memoryIndex = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && memoryIndex < 100) {
                try {
                    if (Memory.isWord(line)) {
                        int instruction = Integer.parseInt(line);
                        cpu.mem.write(memoryIndex, instruction);
                        memoryIndex++;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                }
            }
        }
    }


    private void handleInput() {
        String input = inputField.getText().trim();

        if (!input.isEmpty()) {
            try {

                int value = Integer.parseInt(input);
                if (value >= -9999 && value <= 9999) {
                    pendingInput = input;
                    inputField.setText("");
                    inputButton.setEnabled(false);
                    outputArea.append("Input: " + input + "\n");


                    synchronized (this) {
                        notify();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a number between -9999 and 9999");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Please enter a number.");
        }
    }

    private void updateMemoryDisplay() {
        if (cpu == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Addr  Value\n");
        sb.append("----  -----\n");

        for (int i = 0; i < 100; i++) {
            int value = cpu.mem.read(i);
            if (value != 0) {
                sb.append(String.format("%02d    %+05d\n", i, value));
            }
        }

        memoryArea.setText(sb.toString());
    }

    private void updateCPUStatus() {
        if (cpu != null) {
            cpuLabel.setText("CPU: Accumulator=" + cpu.acc + ", Program Counter=" + cpu.pc);
        }
    }

    // functions for UVConsole interaction
    public void displayOutput(String output) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(output + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
            updateCPUStatus();
            updateMemoryDisplay();
        });

    }

    public String getInput() {
        SwingUtilities.invokeLater(() -> {
            inputButton.setEnabled(true);
            inputField.requestFocus();
        });

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return pendingInput;
    }


    public void restart() {
        if (currentFilePath == null || currentFilePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No file loaded to restart.");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(currentFilePath)));
            programArea.setText(content);

            cpu = new UVCpu(currentFilePath, this);

            outputArea.setText("Program restarted.\n");
            inputField.setText("");
            inputButton.setEnabled(false);

            updateCPUStatus();
            updateMemoryDisplay();

            runButton.setEnabled(true);


            statusLabel.setText("Restarted: " + Paths.get(currentFilePath).getFileName());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error restarting: " + ex.getMessage());
        }
    }
    private boolean validateFileFormatting(String text) {
        String[] words = text.split("\\r?\\n");
        for(String word : words) {
            if (word.length() != 5){
                return false;
            }
            try {
                int value = Integer.parseInt(word);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UVSimGUI().setVisible(true);
        });
    }

    public void displayMalformedLine(String line) {
        String message = String.format(
                "Skipped malformed line: %s\nOnly signed 4-digit numbers are allowed.\n", line);

        displayOutput(message);
    }

    public int userInputInt() {

        displayOutput("Enter a signed four digit word: ");
        String input = getInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputMismatchException("Input was not an integer");
        }

    }
}