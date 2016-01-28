/*
 * Copyright 2015 Psycho_Coder <Animesh Shaw>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.letshackit.psychohasher.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import net.letshackit.psychohasher.HashType;
import net.letshackit.psychohasher.HashingUtils;

/**
 *
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
public class PsychoHasherGui extends JFrame {

    private final int DEF_WIDTH = 800;
    private final int DEF_HEIGHT = 480;

    /* Global Components declaration */
    private JTabbedPane tabbedPane;

    private JEditorPane welcomePane;

    private final JPanel mainPanel;
    private JPanel resultPanel;
    private JPanel welcome, hashText, hashFiles, hashFilesGroup, verifyHashes;

    private JToolBar statusBar;

    private Clipboard clipBrd;

    private LinkedList<File> filesTohash;

    private JFileChooser fc;

    /* Fields declaration for ResultPanel */
    private JComboBox<HashType> hashAlgosCombo;
    private JTextArea resultTxtArea;
    private JButton copyToClipboard, computeHash;
    private JScrollPane scrollPaneResult;

    /* Fields declaration for HashText tab */
    private JLabel hashTxtHeader;
    private JScrollPane hashTxtPaneData;
    private JButton hashTxtPasteButton;
    private JTextArea hashTxtAreaData;

    /* Fields Description for HashFiles tab */
    private JLabel hashFilesHeader;
    private JScrollPane filesTablePane;
    private JTable filesTable;
    private DefaultTableModel tableModel;
    private JButton addFile, addFiles, addFolder, removeAll, exportToTsv,
            filesComputeHash;
    private JComboBox<HashType> hashFilesAlgosCombo;


    /* Fields Description for HashFiles tab */
    private JLabel grpFilesHeader;
    private JScrollPane grpFilesTablePane;
    private JList<File> grpFilesList;
    private JButton grpAddFile, grpAddFiles, grpAddFolder, grpRemoveAll, grpExportToTsv,
            grpComputeHash;

    /**
     * Builds the GUI and Initializes the components.
     *
     * @throws HeadlessException
     */
    public PsychoHasherGui() throws HeadlessException {
        mainPanel = new JPanel(new BorderLayout());

        setSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("PsychoHasher - All Purpose Hashing tool");
        setContentPane(mainPanel);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Unable to set system look and feel.");
        }

        initGlobalDec();
        createResultPanel();
        createMainTabbedPane();
        createStatusBar();
    }

    /**
     * Global declaration of Reusable components
     */
    private void initGlobalDec() {
        resultPanel = new JPanel();
        clipBrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        fc = new JFileChooser();
        filesTohash = new LinkedList<>();
    }

    /**
     * Reusable Result Panel to be used in different tabs for different ops.
     */
    private void createResultPanel() {
        resultPanel.setBorder(BorderFactory.createTitledBorder("Select Type of "
                + "Hash and Click 'Compute Hash'"));
        resultPanel.setLayout(null);
        resultPanel.setBounds(20, 260, getWidth() - 50, 145);

        resultTxtArea = new JTextArea();
        resultTxtArea.setEditable(false);
        resultTxtArea.setWrapStyleWord(false);
        scrollPaneResult = new JScrollPane(resultTxtArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneResult.setBounds(30, 30, 680, 50);
        resultPanel.add(scrollPaneResult);

        hashAlgosCombo = new JComboBox<>(HashType.values());
        hashAlgosCombo.setEditable(false);
        hashAlgosCombo.setLightWeightPopupEnabled(true);
        hashAlgosCombo.setBounds(50, 90, 200, 40);
        resultPanel.add(hashAlgosCombo);

        copyToClipboard = new JButton("Copy to Clipboard");
        copyToClipboard.setBounds(270, 90, 200, 40);
        copyToClipboard.addActionListener((ActionEvent e) -> {
            if (!resultTxtArea.getText().isEmpty()) {
                StringSelection strSelec = new StringSelection(resultTxtArea.
                        getText());
                clipBrd.setContents(strSelec, null);
            }
        });
        resultPanel.add(copyToClipboard);

        computeHash = new JButton("Compute Hash!");
        computeHash.setBounds(490, 90, 200, 40);
        computeHash.addActionListener((ActionEvent e) -> {
            String hashedData = HashingUtils.getHash(hashTxtAreaData.getText(),
                    (HashType) hashAlgosCombo.getSelectedItem());
            resultTxtArea.setText(hashedData);
        });
        resultPanel.add(computeHash);

    }

    /**
     * Create Tabbed Pane
     */
    private void createMainTabbedPane() {

        /**
         * Initialize the panels to be used in the tabbed pane.
         */
        welcome = new JPanel(new FlowLayout());
        hashText = new JPanel();
        hashFiles = new JPanel();
        hashFilesGroup = new JPanel();
        verifyHashes = new JPanel();

        /**
         * Initialize tabbed pane and create tabs
         */
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Welcome", welcome);
        tabbedPane.addTab("Hash Text", hashText);
        tabbedPane.addTab("Hash Files", hashFiles);
        tabbedPane.addTab("Hash Files Group", hashFilesGroup);
        tabbedPane.addTab("Verify Hashes", verifyHashes);
        tabbedPane.addChangeListener((ChangeEvent e) -> {
            switch (tabbedPane.getSelectedIndex()) {
                case 0:
                    break;
                case 1:
                    resultTxtArea.setText("");
                    hashText.add(resultPanel);
                    break;
                case 2:
                    break;
                case 3:
                    resultTxtArea.setText("");
                    hashFilesGroup.add(resultPanel);
                    break;
                case 4:
                    resultTxtArea.setText("");
                    verifyHashes.add(resultPanel);
                    break;
            }
        });

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        /* ---Welcome Tab Starts--- */
        createWelcomeTab();
        /* ---Welcome Tab ends--- */

        /* ---HashText tab Starts--- */
        createHashTextTab();
        /* ---HashText tab ends--- */

        /* ---HashFiles tab Starts--- */
        createHashFilesTab();
        /* ---HashFiles tab ends--- */

        /* ---HashDisks tab Starts--- */
        createHashFilesGroupTab();
        /* ---HashDisks tab ends--- */

        /* ---Verify Hashes tab Starts--- */
        verifyHashesTab();
        /* ---Verify Hash tab ends--- */
    }

    private void createWelcomeTab() {
        welcomePane = new JEditorPane();
        welcomePane.setEditable(false);
        try {
            welcomePane.setPage(this.getClass().getClassLoader()
                    .getResource("welcome.html"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        welcome.add(welcomePane);
    }

    private void createHashTextTab() {
        hashText.setLayout(null);

        hashTxtHeader = new JLabel("Get Hash for Text/String!");
        hashTxtHeader.setBounds(10, 10, 200, 30);
        hashTxtHeader.setFont(new Font("Cambria", Font.BOLD, 14));
        hashText.add(hashTxtHeader);

        hashTxtPasteButton = new JButton("Paste Text");
        hashTxtPasteButton.setToolTipText("Pastes text into the text area "
                + "from System Clipboard");
        hashTxtPasteButton.setBounds(20, 50, 150, 50);
        hashTxtPasteButton.addActionListener((ActionEvent e) -> {
            Transferable t = clipBrd.getContents(PsychoHasherGui.this);

            if (t != null) {
                try {
                    hashTxtAreaData.setText((String) t.
                            getTransferData(DataFlavor.stringFlavor));
                } catch (UnsupportedFlavorException | IOException ex) {
                    System.err.
                            println("Error while pasting data from clipboard.");
                }
            }

        });
        hashText.add(hashTxtPasteButton);

        hashTxtAreaData = new JTextArea();
        hashTxtPaneData = new JScrollPane(hashTxtAreaData,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        hashTxtPaneData.setBounds(190, 50, 575, 180);
        hashText.add(hashTxtPaneData);
    }

    private void createHashFilesTab() {
        hashFiles.setLayout(null);

        Vector<String> colNames = new Vector<>();
        colNames.add("Sl No.");
        colNames.add("Filename");
        colNames.add("Size (in KiB)");
        colNames.add("Location");

        tableModel = new DefaultTableModel(colNames, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        filesTable = new JTable(tableModel);
        filesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        filesTablePane = new JScrollPane(filesTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filesTablePane.setBounds(20, 50, 750, 310);
        hashFiles.add(filesTablePane);

        hashFilesHeader = new JLabel("Get Hash for Single/Multiple Files");
        hashFilesHeader.setBounds(10, 10, 300, 30);
        hashFilesHeader.setFont(new Font("Cambria", Font.BOLD, 14));
        hashFiles.add(hashFilesHeader);

        exportToTsv = new JButton("Export as TSV");
        exportToTsv.setToolTipText("Export the table and save as TSV file in the current directory");
        exportToTsv.setBounds(620, 370, 150, 35);
        exportToTsv.setFont(new Font("Cambria", Font.CENTER_BASELINE, 15));
        exportToTsv.addActionListener((ActionEvent e) -> {
            if (tableModel.getRowCount() > 0) {
                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("hashes.tsv"),
                        StandardCharsets.UTF_8)) {
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        writer.write(tableModel.getColumnName(i) + "\t");
                    }

                    writer.newLine();

                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            writer.write(tableModel.getValueAt(i, j).toString() + "\t");
                        }
                        writer.newLine();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PsychoHasherGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(this, "Table exported and saved as "
                        + "hashes.tsv", "Data export complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No data to export", "Data export failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        hashFiles.add(exportToTsv);

        hashFilesAlgosCombo = new JComboBox<>(HashType.values());
        hashFilesAlgosCombo.setEditable(false);
        hashFilesAlgosCombo.setLightWeightPopupEnabled(true);
        hashFilesAlgosCombo.setBounds(400, 10, 200, 30);
        hashFiles.add(hashFilesAlgosCombo);

        addFile = new JButton("Add Single File");
        addFile.setToolTipText("Add a single file to hash.");
        addFile.setBounds(20, 370, 140, 35);
        addFile.addActionListener((ActionEvent e) -> {
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setMultiSelectionEnabled(false);

            int rowCount = tableModel.getRowCount();
            int retVal = fc.showOpenDialog(this);

            if (retVal == JFileChooser.APPROVE_OPTION) {

                File f = fc.getSelectedFile();

                if (!filesTohash.contains(f)) {
                    filesTohash.add(f);
                    double size = f.length() / 1024;
                    Vector<String> rowData = new Vector<>();
                    rowData.add(Integer.toString(rowCount + 1));
                    rowData.add(f.getName());
                    rowData.add(Double.toString(size));
                    rowData.add(f.getAbsolutePath());
                    tableModel.addRow(rowData);
                    filesComputeHash.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "File has been added already.",
                            "Error! Duplicate file found", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        hashFiles.add(addFile);

        addFiles = new JButton("Add Multiple Files");
        addFiles.setToolTipText("Add multiple files for hashing.");
        addFiles.setBounds(170, 370, 140, 35);
        addFiles.addActionListener((ActionEvent e) -> {
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setMultiSelectionEnabled(true);

            int rowCount = tableModel.getRowCount();
            int retVal = fc.showOpenDialog(this);

            if (retVal == JFileChooser.APPROVE_OPTION) {

                int skippedCount = 0;
                File[] files = fc.getSelectedFiles();

                for (File f : files) {
                    if (!filesTohash.contains(f)) {
                        filesTohash.add(f);
                        double size = f.length() / 1024;
                        Vector<String> rowData = new Vector<>();
                        rowCount += 1;
                        rowData.add(Integer.toString(rowCount));
                        rowData.add(f.getName());
                        rowData.add(Double.toString(size));
                        rowData.add(f.getAbsolutePath());
                        tableModel.addRow(rowData);
                        filesComputeHash.setEnabled(true);
                    } else {
                        skippedCount++;
                    }
                }

                if (skippedCount > 0) {
                    JOptionPane.showMessageDialog(this, "Few files were already "
                            + "present and hence skipped. Files Skipped: "
                            + skippedCount, "Error! Duplicate files found",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        hashFiles.add(addFiles);

        addFolder = new JButton("Add Files in Folder");
        addFolder.setToolTipText("Add all the files in a directory for "
                + "hashing.");
        addFolder.setBounds(320, 370, 140, 35);
        addFolder.addActionListener((ActionEvent e) -> {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setMultiSelectionEnabled(false);

            int rowCount = tableModel.getRowCount();
            int retVal = fc.showOpenDialog(this);
            int skippedCount = 0;

            if (retVal == JFileChooser.APPROVE_OPTION) {

                Path dir = fc.getSelectedFile().toPath();

                DirectoryStream.Filter<Path> filter = (Path entry)
                        -> Files.isRegularFile(entry, LinkOption.NOFOLLOW_LINKS);

                try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir, filter)) {
                    for (Path path : dirStream) {
                        File f = path.toFile();
                        if (!filesTohash.contains(f)) {
                            filesTohash.add(f);
                            double size = f.length() / 1024;
                            Vector<String> rowData = new Vector<>();
                            rowCount += 1;
                            rowData.add(Integer.toString(rowCount));
                            rowData.add(f.getName());
                            rowData.add(Double.toString(size));
                            rowData.add(f.getAbsolutePath());
                            tableModel.addRow(rowData);
                            filesComputeHash.setEnabled(true);
                        } else {
                            skippedCount++;
                        }
                    }

                    if (skippedCount > 0) {
                        JOptionPane.showMessageDialog(this, "Few files were already "
                                + "present and hence skipped. Files Skipped: "
                                + skippedCount, "Error! Duplicate files found",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        hashFiles.add(addFolder);

        removeAll = new JButton("Remove All");
        removeAll.setToolTipText("Remove all files from the table.");
        removeAll.setBounds(470, 370, 140, 35);
        removeAll.addActionListener((ActionEvent e) -> {
            tableModel.setRowCount(0);
            filesTohash.clear();
            filesComputeHash.setEnabled(false);
        });
        hashFiles.add(removeAll);

        filesComputeHash = new JButton("Compute Hash!");
        filesComputeHash.setEnabled(false);
        filesComputeHash.setToolTipText("Compute hash for all the files in the table");
        filesComputeHash.setBounds(610, 10, 150, 30);
        filesComputeHash.setFont(new Font("Cambria", Font.BOLD, 15));
        filesComputeHash.addActionListener((ActionEvent e) -> {
            createFileHashingProgressDialog();
        });
        hashFiles.add(filesComputeHash);

    }

    private void createFileHashingProgressDialog() {
        //setEnabled(false);
        JDialog fileHasherDialog = new JDialog(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 300));

        fileHasherDialog.setContentPane(panel);
        fileHasherDialog.setAlwaysOnTop(true);
        fileHasherDialog.setVisible(true);
        fileHasherDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        fileHasherDialog.setLocation(this.getLocation());
        fileHasherDialog.setResizable(false);
        fileHasherDialog.setTitle("Hashing Files in progress...");
        fileHasherDialog.setSize(new Dimension(500, 300));
        fileHasherDialog.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(true);
        panel.add(progressBar, BorderLayout.SOUTH);

        JTextArea txtArea = new JTextArea(10, 10);
        JScrollPane pane = new JScrollPane(txtArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setBorder(BorderFactory.createTitledBorder("Update Logs"));
        pane.getInsets().set(20, 20, 20, 20);
        panel.add(pane, BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.setEnabled(false);
        close.addActionListener((ActionEvent e) -> {
            fileHasherDialog.dispose();
        });
        panel.add(close, BorderLayout.NORTH);

        List<String> computedHashes = new LinkedList<>();

        HashFileTask hashFileTask = new HashFileTask(filesTohash, txtArea,
                (HashType) hashFilesAlgosCombo.getSelectedItem());
        hashFileTask.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            switch (evt.getPropertyName()) {
                case "progress":
                    progressBar.setIndeterminate(false);
                    progressBar.setValue((int) evt.getNewValue());
                    break;
                case "state":
                    switch ((SwingWorker.StateValue) evt.getNewValue()) {
                        case DONE: {
                            try {
                                close.setEnabled(true);
                                computedHashes.addAll(hashFileTask.get());

                                String selectedOption = ((HashType) hashFilesAlgosCombo
                                        .getSelectedItem()).getValue();
                                int index = 4;

                                if (tableModel.getColumnCount() < 5) {
                                    tableModel.addColumn(selectedOption);
                                } else {
                                    boolean colFound = false;
                                    for (index = 4; index < tableModel.getColumnCount(); index++) {
                                        if (tableModel.getColumnName(index).equals(selectedOption)) {
                                            colFound = true;
                                            break;
                                        }
                                    }

                                    if (!colFound) {
                                        tableModel.addColumn(selectedOption);
                                    }
                                }

                                for (int i = 0; i < computedHashes.size(); i++) {
                                    tableModel.setValueAt(computedHashes.get(i), i, index);
                                }
                            } catch (InterruptedException | ExecutionException ex) {
                                Logger.getLogger(PsychoHasherGui.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;
                        case STARTED:
                        case PENDING:
                            progressBar.setVisible(true);
                            progressBar.setIndeterminate(true);
                    }
                    break;
            }
        });
        hashFileTask.execute();

    }

    /**
     * Class to hash files array in a different thread and set the progress.
     */
    private class HashFileTask extends SwingWorker<LinkedList<String>, String> {

        private final LinkedList<File> filesToHash;
        private final JTextArea txtArea;
        private final LinkedList<String> hashes;
        private final HashType hashType;

        /**
         *
         * @param filesToHash
         * @param txtArea
         * @param hashType
         */
        public HashFileTask(final LinkedList<File> filesToHash,
                final JTextArea txtArea, final HashType hashType) {
            this.filesToHash = filesToHash;
            this.txtArea = txtArea;
            this.hashType = hashType;
            hashes = new LinkedList<>();
        }

        private void failIfInterrupted() throws InterruptedException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Interrupted while hashing files");
            }
        }

        @Override
        protected LinkedList<String> doInBackground() throws Exception {
            int filesCount = filesToHash.size();
            int count = 0;
            Iterator<File> itr = filesToHash.iterator();
            publish("File hashing Procedure started. Files to Hash:" + filesCount);

            while (itr.hasNext()) {
                failIfInterrupted();
                File f = itr.next();
                count += 1;
                setProgress((count * 100) / filesCount);
                if (f.canRead()) {
                    publish("Hashing file - " + f.getName());
                    hashes.add(HashingUtils.getFileHash(f, hashType));
                } else {
                    publish("Permission Denied. Unable to Hash file - " + f.getName());
                    hashes.add("N/A");
                }
            }

            return hashes;
        }

        @Override
        protected void process(List<String> chunks) {
            chunks.stream().map((str) -> {
                txtArea.append(str);
                return str;
            }).forEach((_item) -> {
                txtArea.append("\n");
            });
        }

        @Override
        protected void done() {
            txtArea.append("Hash Computation Complete");
        }

    }

    private void createHashFilesGroupTab() {
        hashFilesGroup.setLayout(null);

        grpFilesHeader = new JLabel("Get Hash for Single/Multiple Files");
        grpFilesHeader.setBounds(10, 10, 300, 30);
        grpFilesHeader.setFont(new Font("Cambria", Font.BOLD, 14));
        hashFilesGroup.add(grpFilesHeader);
    }

    private void verifyHashesTab() {
        verifyHashes.setLayout(null);
    }

    private void createStatusBar() {
        statusBar = new JToolBar(SwingConstants.HORIZONTAL);
        statusBar.setBorder(BorderFactory.createBevelBorder(
                BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(getWidth(), 30));
        mainPanel.add(statusBar, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PsychoHasherGui().setVisible(true);
        });
    }
}
