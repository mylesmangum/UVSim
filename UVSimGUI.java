import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.prefs.*;
import java.util.Scanner;

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
    private JButton customizeButton;
    private JPanel colorPanel;
    private JButton primaryColor;
    private JButton secondaryColor;
    private JButton convertButton;

    private JTabbedPane tabbedPane;
    private ArrayList<FileTab> fileTabs;
    private ArrayList<JPanel> primaryPanels;
    private ArrayList<JPanel> secondaryPanels;
    public static final String PRIMARY = "primary";
    public static final String SECONDARY = "secondary";

    public UVSimGUI() {
        fileTabs = new ArrayList<>();
        setupGUI();
        setupEvents();
    }

    private void setupGUI() {
        Preferences prefs = Preferences.userNodeForPackage(UVSimGUI.class);
        primaryPanels = new ArrayList<>();
        secondaryPanels = new ArrayList<>();
        Color primary;
        Color secondary;
        if(prefs.get(PRIMARY,"default") == "default"){
            primary = new Color(36,93,56);
        }
        else{
            primary = getColor(prefs.get(PRIMARY, "default"));
        }
        if(prefs.get(SECONDARY, "default") == "default"){
            secondary = new Color(142,140,137);
        }
        else{
            secondary = getColor(prefs.get(SECONDARY, "default"));
        }


        setTitle("UVSimGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        saveButton = new JButton("Save As");
        openButton = new JButton("Open Program File");
        runButton = new JButton("Run Program");
        runButton.setEnabled(false);
        clearOutputButton = new JButton("Clear Output");
        convertButton = new JButton("Convert 4→6");
        convertButton.setBackground(secondary);
        convertButton.setForeground(Color.black);
        statusLabel = new JLabel("No file loaded");
        customizeButton = new JButton("Customize");
        primaryPanels.add(topPanel);
        topPanel.setBackground(primary);



        topPanel.add(openButton);
        topPanel.add(runButton);
        topPanel.add(saveButton);
        topPanel.add(convertButton);
        topPanel.add(clearOutputButton);
        topPanel.add(statusLabel);
        topPanel.add(customizeButton);
        //color the buttons
        for(Component c : topPanel.getComponents()){
            c.setBackground(secondary);
            c.setForeground(Color.black);
        }
        secondaryPanels.add(topPanel);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        centerPanel.setBackground(primary);
        primaryPanels.add(centerPanel);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(primary);
        tabbedPane.addChangeListener(e -> updateUIForCurrentTab());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(primary);
        primaryPanels.add(rightPanel);

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

        centerPanel.add(tabbedPane);
        centerPanel.add(rightPanel);



        JPanel bottomPanel = new JPanel();
        cpuLabel = new JLabel("CPU: Accumulator=0, Program Counter=0");
        cpuLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        bottomPanel.add(cpuLabel);
        bottomPanel.setBackground(primary);
        primaryPanels.add(bottomPanel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);


        colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(5,5));
        colorPanel.setVisible(false);
        primaryColor = new JButton("Primary Color");
        secondaryColor = new JButton("Secondary Color");
        colorPanel.add(primaryColor);
        colorPanel.add(secondaryColor);
        add(colorPanel, BorderLayout.EAST);
        colorPanel.setBackground(primary);
        primaryPanels.add(colorPanel);
        for(Component c : colorPanel.getComponents()){
            c.setForeground(Color.black);
            c.setBackground(secondary);
        }
        secondaryPanels.add(colorPanel);
    }


    //action listeners
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
        convertButton.addActionListener(e -> convert4DigitFile());
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

        customizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorPanel.setVisible(!colorPanel.isVisible());
            }
        });
        primaryColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color p;
                p = JColorChooser.showDialog(colorPanel, "Select Color", Color.black);
                Preferences prefs = Preferences.userNodeForPackage(UVSimGUI.class);
                prefs.put(PRIMARY, p.toString());
                for (JPanel panel : primaryPanels) {
                    panel.setBackground(p);
                }
            }
        });
        secondaryColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color s;
                s = JColorChooser.showDialog(colorPanel, "Select Color", Color.black);
                Preferences prefs = Preferences.userNodeForPackage(UVSimGUI.class);
                prefs.put(SECONDARY, s.toString());
                for (JPanel panel : secondaryPanels) {
                    for (Component c : panel.getComponents()) {
                        c.setBackground(s);
                    }
                }
            }
        });
    }

    private FileTab getCurrentFileTab() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < fileTabs.size()) {
            return fileTabs.get(selectedIndex);
        }
        return null;
    }

    private void updateUIForCurrentTab() {
        FileTab currentTab = getCurrentFileTab();
        if (currentTab != null) {
            // Update references for compatibility with existing code
            programArea = currentTab.programArea;
            memoryArea = currentTab.memoryArea;
            cpu = currentTab.cpu;
            currentFilePath = currentTab.filePath;

            statusLabel.setText("Loaded: " + currentTab.fileName);
            runButton.setEnabled(true);
            updateMemoryDisplay();
            updateCPUStatus();
        }
    }

    //called functions
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

                FileTab fileTab = new FileTab(file.getName(), currentFilePath, this);
                fileTab.programArea.setText(content);
                fileTabs.add(fileTab);

                JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
                //leftPanel.setBackground(new Color(36,93,56)); // Use primary color
                primaryPanels.add(leftPanel);
                JPanel programPanel = new JPanel(new BorderLayout());
                programPanel.add(new JLabel("Program:"), BorderLayout.NORTH);
                programPanel.add(new JScrollPane(fileTab.programArea), BorderLayout.CENTER);

                JPanel memoryPanel = new JPanel(new BorderLayout());
                memoryPanel.add(new JLabel("Memory:"), BorderLayout.NORTH);
                memoryPanel.add(new JScrollPane(fileTab.memoryArea), BorderLayout.CENTER);

                leftPanel.add(programPanel);
                leftPanel.add(memoryPanel);

                tabbedPane.addTab(file.getName(), leftPanel);
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

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
        FileTab currentTab = getCurrentFileTab();
        if (currentTab == null) return;

        String savedContent = currentTab.programArea.getText();
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
                JOptionPane.showMessageDialog(this, "Please have each line be one word each when saving.  Each word must be 6 digits long and have a plus or minus before the word.");
            }
        }
    }

    private void runProgram() {
        FileTab currentTab = getCurrentFileTab();
        if (currentTab == null) return;

        if (currentTab.cpu == null) {
            // user can type program without loading file
            currentTab.cpu = new UVCpu();
        }

        if (!validateInstructionCount()) {
            return; // Don't run if there are too many instructions
        }

        outputArea.append("=== Running Program ===\n");
        runButton.setEnabled(false);

        // run program in another thread
        new Thread(() -> {
            try {
                loadProgramFromTextArea();
                currentTab.cpu.run();
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
        FileTab currentTab = getCurrentFileTab();
        if (currentTab == null) return;

        String content = currentTab.programArea.getText();
        String[] lines = content.split("\n");

        // Reset CPU
        currentTab.cpu.pc = 0;
        currentTab.cpu.acc = 0;
        currentTab.cpu.halted = false;

        // Clear memory -- changed 100's to 250
        for (int i = 0; i < 250; i++) {
            currentTab.cpu.mem.write(i, 0);
        }

        // Load instructions from text area
        int memoryIndex = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && memoryIndex < 250) {
                try {
                    if (Memory.isWord(line)) {
                        int instruction = Integer.parseInt(line);
                        currentTab.cpu.mem.write(memoryIndex, instruction);
                        memoryIndex++;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                }
            }
        }
    }

    private boolean validateInstructionCount() {
        FileTab currentTab = getCurrentFileTab();
        if (currentTab == null) return false;

        String content = currentTab.programArea.getText();
        String[] lines = content.split("\n");

        int validInstructionCount = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                try {
                    if (Memory.isWord(line)) {
                        validInstructionCount++;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                }
            }
        }
        //changed 100 to 250
        if (validInstructionCount > 250) {
            JOptionPane.showMessageDialog(this,
                    "Error: Program contains " + validInstructionCount + " instructions.\n" +
                            "Maximum allowed is 250 instructions.",
                    "Too Many Instructions",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    //changed 9999 to 999999
    private void handleInput() {
        String input = inputField.getText().trim();

        if (!input.isEmpty()) {
            try {

                int value = Integer.parseInt(input);
                if (value >= -999999 && value <= 999999) {
                    pendingInput = input;
                    inputField.setText("");
                    inputButton.setEnabled(false);
                    outputArea.append("Input: " + input + "\n");


                    synchronized (this) {
                        notify();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a number between -999999 and 999999");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Please enter a number.");
        }
    }
    //changed 100's to 250
    private void updateMemoryDisplay() {
        FileTab currentTab = getCurrentFileTab();
        if (currentTab == null || currentTab.cpu == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Addr  Value\n");
        sb.append("----  -----\n");

        for (int i = 0; i < 250; i++) {
            int value = currentTab.cpu.mem.read(i).getValue();
            if (value != 0) {
                sb.append(String.format("%02d    %+05d\n", i, value));
            }
        }

        currentTab.memoryArea.setText(sb.toString());
    }

    private void updateCPUStatus() {
        FileTab currentTab = getCurrentFileTab();
        if (currentTab != null && currentTab.cpu != null) {
            cpuLabel.setText("CPU: Accumulator=" + currentTab.cpu.acc + ", Program Counter=" + currentTab.cpu.pc);
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
        FileTab currentTab = getCurrentFileTab();
        if (currentTab == null || currentTab.filePath == null || currentTab.filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No file loaded to restart.");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(currentTab.filePath)));
            currentTab.programArea.setText(content);

            currentTab.cpu = new UVCpu(currentTab.filePath, this);

            outputArea.setText("Program restarted.\n");
            inputField.setText("");
            inputButton.setEnabled(false);

            updateCPUStatus();
            updateMemoryDisplay();

            runButton.setEnabled(true);

            statusLabel.setText("Restarted: " + Paths.get(currentTab.filePath).getFileName());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error restarting: " + ex.getMessage());
        }
    }
    //changed word length constant to 7 instead of 5
    private boolean validateFileFormatting(String text) {
        String[] words = text.split("\\r?\\n");
        for(String word : words) {
            if (word.length() != 7){
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
                "Skipped malformed line: %s\nOnly signed 6-digit numbers are allowed.\n", line);

        displayOutput(message);
    }

    public int userInputInt() {

        displayOutput("Enter a signed six digit word: ");
        String input = getInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputMismatchException("Input was not an integer");
        }

    }
    private Color getColor(String toCol){
        if (toCol == null || !toCol.startsWith("java.awt.Color[r=")) {
            throw new IllegalArgumentException("Invalid Color string format.");
        }
        String components = toCol.substring(toCol.indexOf("[") + 1, toCol.indexOf("]"));

        // Split by comma
        String[] parts = components.split(",");

        int r = 0, g = 0, b = 0, a = 255; // Default alpha to 255 (opaque)

        for (String part : parts) {
            if (part.startsWith("r=")) {
                r = Integer.parseInt(part.substring(2));
            } else if (part.startsWith("g=")) {
                g = Integer.parseInt(part.substring(2));
            } else if (part.startsWith("b=")) {
                b = Integer.parseInt(part.substring(2));
            } else if (part.startsWith("a=")) {
                a = Integer.parseInt(part.substring(2));
            }
        }
        return new Color(r, g, b, a);
    }

    private void convert4DigitFile() { //This method converts a 4 digit signed int into 6 digits
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

            ArrayList<String> convertedLines = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.matches("[-+][0-9]{4}")) {
                        String sign = line.substring(0, 1);
                        String opcode = line.substring(1, 3);
                        String address = line.substring(3);
                        String converted = sign + "0" + opcode + "0" + address;
                        convertedLines.add(converted);
                    } else if (line.matches("[-+][0-9]{6}")) {
                        convertedLines.add(line);
                    } else if (!line.isEmpty()) {
                        displayOutput("Skipping malformed line: " + line);
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
                return;
            }

            // Prompt user to save new converted file
            JFileChooser saveChooser = new JFileChooser(currentDirectoryPath);
            if (saveChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = saveChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(saveFile)) {
                    for (String line : convertedLines) {
                        writer.write(line + "\n");
                    }
                    writer.flush();
                    JOptionPane.showMessageDialog(this, "File converted and saved successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving converted file: " + ex.getMessage());
                }
            }
        }
    }



}