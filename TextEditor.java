import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;

class TextEditor extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JFrame frame;

    public TextEditor() {
        frame = new JFrame("Text Editor");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {
            System.out.println("Failed to apply theme");
        }

        textArea = new JTextArea();
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem printFile = new JMenuItem("Print");
        JMenuItem exportPdf = new JMenuItem("Export as PDF");

        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        printFile.addActionListener(this);
        exportPdf.addActionListener(this);

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(printFile);
        fileMenu.add(exportPdf);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutText = new JMenuItem("Cut");
        JMenuItem copyText = new JMenuItem("Copy");
        JMenuItem pasteText = new JMenuItem("Paste");

        cutText.addActionListener(this);
        copyText.addActionListener(this);
        pasteText.addActionListener(this);

        editMenu.add(cutText);
        editMenu.add(copyText);
        editMenu.add(pasteText);

        JMenuItem closeApp = new JMenuItem("Close");
        closeApp.addActionListener(this);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(closeApp);

        frame.setJMenuBar(menuBar);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Cut":
                textArea.cut();
                break;

            case "Copy":
                textArea.copy();
                break;

            case "Paste":
                textArea.paste();
                break;

            case "Save":
                saveFile();
                break;

            case "Print":
                printFile();
                break;

            case "Export as PDF":
                exportAsPdf();
                break;

            case "Open":
                openFile();
                break;

            case "New":
                textArea.setText("");
                break;

            case "Close":
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to close?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
                break;
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser("f:");
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(frame, "File saved successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
            }
        }
    }

    private void printFile() {
        try {
            textArea.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error printing file: " + ex.getMessage());
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser("f:");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error opening file: " + ex.getMessage());
            }
        }
    }

    private void exportAsPdf() {
        JFileChooser fileChooser = new JFileChooser("f:");
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".pdf");
            try {
                Document pdfDoc = new Document();
                PdfWriter.getInstance(pdfDoc, new FileOutputStream(file));
                pdfDoc.open();
                pdfDoc.add(new Paragraph(textArea.getText()));
                pdfDoc.close();
                JOptionPane.showMessageDialog(frame, "PDF exported successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error exporting to PDF: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}
