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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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
    private final int DEF_HEIGHT = 500;

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

    /* Fields declaration for Custom Progress Dialog */
    private JDialog fileHasherDialog = null;
    private JProgressBar progressBar;
    private JTextArea hashLogTxtArea;
    private JButton close;
    private JScrollPane logPane;
    private JPanel dialogPanel;

    /* Fields declaration for ResultPanel */
    private JComboBox<HashType> hashAlgosCombo;
    private JTextArea resultTxtArea;
    private JButton copyToClipboard;
    private JScrollPane scrollPaneResult;

    /* Fields declaration for HashText tab */
    private JLabel hashTxtHeader;
    private JScrollPane hashTxtPaneData;
    private JButton hashTxtPasteButton, txtComputeHash;
    private JTextArea hashTxtAreaData;

    /* Fields Description for HashFiles tab */
    private JLabel hashFilesHeader;
    private JScrollPane filesTablePane;
    private JTable filesTable;
    private DefaultTableModel tableModel;
    private JButton addFile, addFiles, addFolder, removeAll, exportToTsv,
            filesComputeHash;
    private JComboBox<HashType> hashFilesAlgosCombo;

    /* Fields Description for Group File hash tab */
    private JLabel grpFilesHeader;
    private JScrollPane grpFilesListScrollPane;
    private JList<File> grpFilesList;
    private JButton grpAddFile, grpAddFiles, grpAddFolder, grpRemoveSelec, grpRemoveAll,
            grpExportResults, grpComputeHash;
    private HashType hashAlgo;

    /* Field Decriptio for Group File hash tab */
    private JLabel verifyFilesHeader;
    private JScrollPane verifyFilesListScrollPane;
    private JList<File> verifyFilesList;
    private JButton loadSavedHash, verifyRemoveAll, verifyComputeHash;

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
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            //UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ex) {
            System.err.println(ex.getMessage());
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
            } else {
                JOptionPane.showMessageDialog(this, "Result Text area is empty!",
                        "Nothing to copy", JOptionPane.ERROR_MESSAGE);
            }
        });
        resultPanel.add(copyToClipboard);
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
        tabbedPane.addTab("About pH & Help", welcome);
        tabbedPane.addTab("Hash Text", hashText);
        tabbedPane.addTab("Hash Files", hashFiles);
        tabbedPane.addTab("Hash Files Group", hashFilesGroup);
        tabbedPane.addTab("Verify Hashes", verifyHashes);
        tabbedPane.addChangeListener((ChangeEvent e) -> {
            switch (tabbedPane.getSelectedIndex()) {
                case 0:
                    break;
                case 1:
                    txtComputeHash.setVisible(true);
                    grpComputeHash.setVisible(false);
                    verifyComputeHash.setVisible(false);
                    resultTxtArea.setText("");
                    hashText.add(resultPanel);
                    break;
                case 2:
                    break;
                case 3:
                    txtComputeHash.setVisible(false);
                    grpComputeHash.setVisible(true);
                    verifyComputeHash.setVisible(false);
                    resultTxtArea.setText("");
                    hashFilesGroup.add(resultPanel);
                    break;
                case 4:
                    txtComputeHash.setVisible(false);
                    grpComputeHash.setVisible(false);
                    verifyComputeHash.setVisible(true);
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
        welcomePane.setSize(welcome.getSize());
        welcomePane.setEditable(false);
        welcomePane.setBorder(null);
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
        hashTxtPaneData.setBounds(180, 50, 590, 200);
        hashText.add(hashTxtPaneData);

        txtComputeHash = new JButton("Compute Hash!");
        txtComputeHash.setBounds(490, 90, 200, 40);
        txtComputeHash.addActionListener((ActionEvent e) -> {
            String hashedData = HashingUtils.getHash(hashTxtAreaData.getText(),
                    (HashType) hashAlgosCombo.getSelectedItem());
            resultTxtArea.setText(hashedData);
        });
        resultPanel.add(txtComputeHash);

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
                    System.err.println(ex.getMessage());
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
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
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

        addFolder = new JButton("Add Folder");
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
        filesComputeHash.setBounds(610, 10, 160, 30);
        filesComputeHash.setFont(new Font("Cambria", Font.BOLD, 15));
        filesComputeHash.addActionListener((ActionEvent e) -> {
            createHashingProgressDialog();

            List<String> computedHashes = new LinkedList<>();
            progressBar.setValue(0);
            hashLogTxtArea.setText("");

            HashFileTask hashFileTask = new HashFileTask(filesTohash, hashLogTxtArea,
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
                                    System.err.println(ex.getMessage());
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

        });
        hashFiles.add(filesComputeHash);

    }

    private void createHashingProgressDialog() {
        setEnabled(false);
        fileHasherDialog = new JDialog(this);
        dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setSize(new Dimension(400, 300));
        //dialogPanel.setPreferredSize(new Dimension(400, 300));

        fileHasherDialog.setContentPane(dialogPanel);
        fileHasherDialog.setAlwaysOnTop(true);
        fileHasherDialog.setVisible(true);
        fileHasherDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        fileHasherDialog.setLocation(this.getLocation());
        fileHasherDialog.setResizable(false);
        fileHasherDialog.setTitle("Hashing Files in progress...");
        fileHasherDialog.setSize(new Dimension(500, 300));
        fileHasherDialog.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(true);
        dialogPanel.add(progressBar, BorderLayout.SOUTH);

        hashLogTxtArea = new JTextArea(10, 10);
        logPane = new JScrollPane(hashLogTxtArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logPane.setBorder(BorderFactory.createTitledBorder("Update Logs"));
        logPane.getInsets().set(20, 20, 20, 20);
        dialogPanel.add(logPane, BorderLayout.CENTER);

        close = new JButton("Close");
        close.setEnabled(false);
        close.addActionListener((ActionEvent e) -> {
            setEnabled(true);
            fileHasherDialog.dispose();
        });
        dialogPanel.add(close, BorderLayout.NORTH);
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

        grpFilesHeader = new JLabel("Compute Hash for group of files.");
        grpFilesHeader.setBounds(10, 10, 300, 30);
        grpFilesHeader.setFont(new Font("Cambria", Font.BOLD, 14));
        hashFilesGroup.add(grpFilesHeader);

        DefaultListModel<File> listModal = new DefaultListModel<>();

        grpFilesList = new JList<>(listModal);
        grpFilesList.setDragEnabled(false);
        grpFilesListScrollPane = new JScrollPane(grpFilesList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        grpFilesListScrollPane.setBounds(190, 50, 575, 200);
        grpFilesListScrollPane.setBorder(BorderFactory.createTitledBorder("List "
                + "of files for group hashing"));
        hashFilesGroup.add(grpFilesListScrollPane);

        //(DefaultListModel<File>) grpFilesList.getModel();
        grpAddFile = new JButton("Single File");
        grpAddFile.setBounds(20, 50, 150, 35);
        grpAddFile.addActionListener((ActionEvent e) -> {
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setMultiSelectionEnabled(false);

            int retVal = fc.showOpenDialog(this);

            if (retVal == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                listModal.addElement(f);
            }
            resultTxtArea.setText("");
            grpComputeHash.setEnabled(true);
        });
        hashFilesGroup.add(grpAddFile);

        grpAddFiles = new JButton("Add Multiple files");
        grpAddFiles.setBounds(20, 90, 150, 35);
        grpAddFiles.addActionListener((ActionEvent e) -> {
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setMultiSelectionEnabled(true);

            int retVal = fc.showOpenDialog(this);

            if (retVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                Arrays.asList(files).iterator().forEachRemaining(listModal::addElement);
            }
            resultTxtArea.setText("");
            grpComputeHash.setEnabled(true);
        });
        hashFilesGroup.add(grpAddFiles);

        grpAddFolder = new JButton("Add Folder");
        grpAddFolder.setToolTipText("Add all files in a folder");
        grpAddFolder.setBounds(20, 130, 150, 35);
        grpAddFolder.addActionListener((ActionEvent e) -> {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setMultiSelectionEnabled(false);

            int retVal = fc.showOpenDialog(this);

            if (retVal == JFileChooser.APPROVE_OPTION) {
                Path dir = fc.getSelectedFile().toPath();

                DirectoryStream.Filter<Path> filter = (Path entry)
                        -> Files.isRegularFile(entry, LinkOption.NOFOLLOW_LINKS);

                try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir, filter)) {
                    dirStream.iterator().forEachRemaining((item)
                            -> listModal.addElement(item.toFile()));
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            resultTxtArea.setText("");
            grpComputeHash.setEnabled(true);
        });
        hashFilesGroup.add(grpAddFolder);

        grpRemoveSelec = new JButton("Remove Selected");
        grpRemoveSelec.setToolTipText("Remove selected file from the list");
        grpRemoveSelec.setBounds(20, 170, 150, 35);
        grpRemoveSelec.addActionListener((ActionEvent e) -> {
            if (!listModal.isEmpty()) {
                int index = grpFilesList.getSelectedIndex();
                if (index == -1) {
                    JOptionPane.showMessageDialog(hashFilesGroup, "No file Selected.",
                            "Unable to Remove.", JOptionPane.ERROR_MESSAGE);
                } else {
                    listModal.remove(index);
                }
            } else {
                JOptionPane.showMessageDialog(hashFilesGroup, "No files added yet. "
                        + "The list is empty.", "Empty List", JOptionPane.ERROR_MESSAGE);
            }

            if (listModal.isEmpty()) {
                grpComputeHash.setEnabled(false);
            }
            resultTxtArea.setText("");
        });
        hashFilesGroup.add(grpRemoveSelec);

        grpRemoveAll = new JButton("Remove All");
        grpRemoveAll.setToolTipText("Remove all the files from the list");
        grpRemoveAll.setBounds(20, 210, 150, 35);
        grpRemoveAll.addActionListener((ActionEvent e) -> {
            if (!listModal.isEmpty()) {
                listModal.removeAllElements();
            } else {
                JOptionPane.showMessageDialog(hashFilesGroup, "No files added yet. "
                        + "The list is empty.", "Empty List", JOptionPane.ERROR_MESSAGE);
            }
            resultTxtArea.setText("");
            grpComputeHash.setEnabled(false);
        });
        hashFilesGroup.add(grpRemoveAll);

        grpExportResults = new JButton("Export Results");
        grpExportResults.setToolTipText("Export the table and save as TSV file in the"
                + " current directory");
        grpExportResults.setBounds(610, 10, 150, 35);
        grpExportResults.setFont(new Font("Cambria", Font.CENTER_BASELINE, 15));
        grpExportResults.addActionListener((ActionEvent e) -> {
            if (!resultTxtArea.getText().isEmpty()) {
                Path path = Paths.get("files_group_result.txt");
                try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                    writer.write("List of files group hashed:");
                    writer.newLine();
                    writer.newLine();

                    Enumeration<File> enumer = listModal.elements();

                    while (enumer.hasMoreElements()) {
                        writer.write(enumer.nextElement().getAbsolutePath());
                        writer.newLine();
                    }

                    writer.newLine();
                    writer.write("Digest Algorithm: " + hashAlgo.getValue());
                    writer.newLine();
                    writer.write("Message Digest/Hash: " + resultTxtArea.getText());
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

                JOptionPane.showMessageDialog(this, "List has been exported with "
                        + "final Hash result.", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hash Not computed yet. Compute "
                        + "hash and then try again.", "Unable to Export", JOptionPane.WARNING_MESSAGE);
            }
        });
        hashFilesGroup.add(grpExportResults);

        grpComputeHash = new JButton("Compute Hash!");
        grpComputeHash.setEnabled(false);
        grpComputeHash.setBounds(490, 90, 200, 40);
        grpComputeHash.setFont(new Font("Cambria", Font.BOLD, 15));
        grpComputeHash.addActionListener((ActionEvent e) -> {
            createHashingProgressDialog();

            progressBar.setValue(0);
            hashLogTxtArea.setText("");
            hashAlgo = (HashType) hashAlgosCombo.getSelectedItem();

            HashGroupFilesTask groupFilesTask = new HashGroupFilesTask(listModal.elements(),
                    listModal.getSize(), hashLogTxtArea, hashAlgo);

            groupFilesTask.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                switch (evt.getPropertyName()) {
                    case "progress":
                        progressBar.setIndeterminate(false);
                        progressBar.setValue((int) evt.getNewValue());
                        break;
                    case "state":
                        switch ((SwingWorker.StateValue) evt.getNewValue()) {
                            case DONE: {
                                try {
                                    resultTxtArea.setText(groupFilesTask.get());
                                    close.setEnabled(true);
                                } catch (InterruptedException | ExecutionException ex) {
                                    System.err.println(ex.getMessage());
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
            groupFilesTask.execute();

        });
        resultPanel.add(grpComputeHash);

    }

    private class HashGroupFilesTask extends SwingWorker<String, String> {

        private final Enumeration<File> filesToHash;
        private final JTextArea txtArea;
        private String grpHash;
        private final HashType hashType;
        private final int fileCount;

        /**
         * Initializes the variables to be used during the processing of worker
         * thread.
         *
         * @param filesToHash Enumeration of all the files to calculate hash.
         * @param txtArea JTextArea in which the hashing progress is logged.
         * @param hashType Type to hash algorithm to be used
         */
        public HashGroupFilesTask(final Enumeration<File> filesToHash, int fileCount,
                final JTextArea txtArea, final HashType hashType) {
            this.filesToHash = filesToHash;
            this.txtArea = txtArea;
            this.hashType = hashType;
            this.fileCount = fileCount;
            grpHash = null;
        }

        /**
         * In case of any interruption occurs then throw exception.
         *
         * @throws InterruptedException
         */
        private void failIfInterrupted() throws InterruptedException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Interrupted while hashing files");
            }
        }

        @Override
        protected String doInBackground() throws Exception {
            int count = 0;
            byte[] dataBytes = new byte[1024];
            byte[] mdbytes;
            boolean permErr = false;
            try {
                MessageDigest md = MessageDigest.getInstance(hashType.getValue());
                publish("File hashing Procedure started. Files to Hash:" + fileCount);

                while (filesToHash.hasMoreElements()) {
                    failIfInterrupted();
                    File f = filesToHash.nextElement();
                    count += 1;

                    publish("Working with file: " + f.getName());

                    if (!f.canRead()) {
                        permErr = true;
                        publish("Permission Denied. Unable to create hash for "
                                + "the said file group - " + f.getName());
                        break;
                    }

                    setProgress((count * 100) / fileCount);

                    try (FileInputStream fis = new FileInputStream(f)) {
                        int nread;
                        while ((nread = fis.read(dataBytes)) != -1) {
                            md.update(dataBytes, 0, nread);
                        }
                    } catch (FileNotFoundException ex) {
                        System.err.println(ex.getMessage());
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }

                if (!permErr) {
                    mdbytes = md.digest();
                    grpHash = HashingUtils.byteArrayToHex(mdbytes);
                }

            } catch (NoSuchAlgorithmException ex) {
                System.err.println(ex.getMessage());
            }

            return grpHash;
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

    private void verifyHashesTab() {
        verifyHashes.setLayout(null);

        verifyFilesHeader = new JLabel("Verify Saved Hashes");
        verifyFilesHeader.setBounds(10, 10, 200, 30);
        verifyFilesHeader.setFont(new Font("Cambria", Font.BOLD, 14));
        verifyHashes.add(verifyFilesHeader);

        verifyComputeHash = new JButton("Compute Hash!");
        verifyComputeHash.setEnabled(false);
        verifyComputeHash.setBounds(490, 90, 200, 40);
        verifyComputeHash.setFont(new Font("Cambria", Font.BOLD, 15));
        resultPanel.add(verifyComputeHash);
    }

    private void createStatusBar() {
        statusBar = new JToolBar(SwingConstants.HORIZONTAL);
        statusBar.setBorder(BorderFactory.createBevelBorder(
                BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(getWidth(), 30));
        statusBar.setFloatable(false);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PsychoHasherGui().setVisible(true);
        });
    }
}
