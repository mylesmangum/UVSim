import javax.swing.*;
        import java.awt.*;

public class FileTab {
    public UVCpu cpu;
    public JTextArea programArea;
    public JTextArea memoryArea;
    public String fileName;
    public String filePath;

    public FileTab(String fileName, String filePath, UVSimGUI gui) {
        this.fileName = fileName;
        this.filePath = filePath;

        // Create CPU with appropriate constructor
        if (filePath != null && !filePath.isEmpty()) {
            this.cpu = new UVCpu(filePath, gui);
        } else {
            this.cpu = new UVCpu();
        }

        // Create text areas
        this.programArea = new JTextArea(10, 30);
        this.programArea.setEditable(true);
        this.programArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        this.memoryArea = new JTextArea(10, 30);
        this.memoryArea.setEditable(false);
        this.memoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }
}