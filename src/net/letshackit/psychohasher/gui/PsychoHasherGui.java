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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.letshackit.psychohasher.HashType;

/**
 *
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
public class PsychoHasherGui extends JFrame {

    private final int DEF_WIDTH = 800;
    private final int DEF_HEIGHT = 400;

    private JMenuBar menuBar;
    private JMenu file, hashUtils, help;

    private JMenuItem fileOpen, fileSave, fileExit;
    private JMenuItem hashData, hashFile, hashDisk, verifyHash;

    private JSeparator fileSep1, huSep1;

    private JTabbedPane tabbedPane;

    private JEditorPane welcomePane;

    private final JPanel mainPanel, resultPanel;
    private JPanel welcome, hashText, hashFiles, hashDisks, verifyHashes;

    /* Field declaration for ResultPanel */
    private JComboBox<String> hashAlgosCombo;
    private JTextArea resultTxtArea;
    private JButton copyToClipboard, computeHash;
    private JScrollPane scrollPaneResult;

    /* Field declaration for HashText tab */
    private JLabel hashTxtPaste;
    private JScrollPane hashTxtPaneData;
    private JButton hashTxtPasteButton;
    private JTextArea hashTxtAreaData;

    public PsychoHasherGui() throws HeadlessException {
        mainPanel = new JPanel(new BorderLayout());
        resultPanel = new JPanel();

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

        createResultPanel();
        createTopMenu();
        createMainTabbedPane();
    }

    private void createResultPanel() {
        //resultPanel.setSize(getWidth(), 150);
        resultPanel.setBorder(BorderFactory.createTitledBorder("Select Type of "
                + "Hash and Click 'Compute Hash'"));
        resultPanel.setLayout(null);
        resultPanel.setBounds(20, 180, getWidth() - 50, 140);
        
        hashAlgosCombo = new JComboBox<>(HashType.getSupportedHashes());
        hashAlgosCombo.setEditable(false);
        hashAlgosCombo.setLightWeightPopupEnabled(true);
        hashAlgosCombo.setBounds(30, 90, 200, 40);
        resultPanel.add(hashAlgosCombo);
             
        copyToClipboard = new JButton("Copy to Clipboard");
        copyToClipboard.setBounds(250, 90, 200, 40);
        resultPanel.add(copyToClipboard);
        
        computeHash = new JButton("Compute Hash!");
        computeHash.setBounds(470, 90, 200, 40);
        resultPanel.add(computeHash);
        
        resultTxtArea = new JTextArea();       
        scrollPaneResult = new JScrollPane(resultTxtArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneResult.setBounds(30, 30, 570, 50);        
        resultPanel.add(scrollPaneResult);
    }

    private void createTopMenu() {
        menuBar = new JMenuBar();
        menuBar.setSize(new Dimension(DEF_WIDTH, 30));

        file = new JMenu("File");
        file.setPreferredSize(new Dimension(45, 30));
        menuBar.add(file);

        hashUtils = new JMenu("Hash Utils");
        hashUtils.setPreferredSize(new Dimension(80, 30));
        menuBar.add(hashUtils);

        help = new JMenu("Help");
        help.setPreferredSize(new Dimension(45, 30));
        menuBar.add(help);

        fileOpen = new JMenuItem("Open");
        fileOpen.setPreferredSize(new Dimension(140, 25));
        file.add(fileOpen);

        fileSave = new JMenuItem("Save");
        fileSave.setPreferredSize(new Dimension(140, 25));
        file.add(fileSave);

        fileSep1 = new JSeparator(SwingConstants.HORIZONTAL);
        fileSep1.setPreferredSize(new Dimension(140, 5));
        file.add(fileSep1);

        fileExit = new JMenuItem("Exit");
        fileExit.setPreferredSize(new Dimension(140, 25));
        fileExit.addActionListener((ActionEvent e) -> {
            this.dispose();
        });
        file.add(fileExit);

        hashData = new JMenuItem("Hash Text");
        hashData.setPreferredSize(new Dimension(140, 25));
        hashData.addActionListener((ActionEvent e) -> {
            tabbedPane.setSelectedIndex(1);
        });
        hashUtils.add(hashData);

        hashFile = new JMenuItem("Hash Files");
        hashFile.setPreferredSize(new Dimension(140, 25));
        hashFile.addActionListener((ActionEvent e) -> {
            tabbedPane.setSelectedIndex(2);
        });
        hashUtils.add(hashFile);

        hashDisk = new JMenuItem("Hash Drives");
        hashDisk.setPreferredSize(new Dimension(140, 25));
        hashDisk.addActionListener((ActionEvent e) -> {
            tabbedPane.setSelectedIndex(3);
        });
        hashUtils.add(hashDisk);

        huSep1 = new JSeparator(SwingConstants.HORIZONTAL);
        huSep1.setPreferredSize(new Dimension(140, 5));
        hashUtils.add(huSep1);

        verifyHash = new JMenuItem("Verify Hash");
        verifyHash.setPreferredSize(new Dimension(140, 25));
        verifyHash.addActionListener((ActionEvent e) -> {
            tabbedPane.setSelectedIndex(4);
        });
        hashUtils.add(verifyHash);

        mainPanel.add(menuBar, BorderLayout.NORTH);
    }

    private void createMainTabbedPane() {

        /**
         * Initialize the panels to be used in the tabbed pane.
         */
        welcome = new JPanel(new FlowLayout());
        hashText = new JPanel();
        hashFiles = new JPanel();
        hashDisks = new JPanel();
        verifyHashes = new JPanel();

        /**
         * Initialize tabbed pane and create tabs
         */
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Welcome", welcome);
        tabbedPane.addTab("Hash Text", hashText);
        tabbedPane.addTab("Hash Files", hashFiles);
        tabbedPane.addTab("Hash Disks", hashDisks);
        tabbedPane.addTab("Verify Hashes", verifyHashes);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        /* ---Welcome Tab Starts--- */
        welcomePane = new JEditorPane();
        welcomePane.setEditable(false);
        try {
            welcomePane.setPage(this.getClass().
                    getResource("resources/welcome.html"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        welcome.add(welcomePane);
        /* ---Welcome Tab ends--- */

        /* ---HashText tab Starts--- */
        hashText.setLayout(null);
        hashTxtPasteButton = new JButton("Paste Text");
        hashTxtPasteButton.setBounds(20, 20, 150, 30);
        hashText.add(hashTxtPasteButton);

        hashTxtAreaData = new JTextArea();
        hashTxtPaneData = new JScrollPane(hashTxtAreaData,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        hashTxtPaneData.setBounds(190, 20, 575, 140);
        hashText.add(hashTxtPaneData);
        
        hashText.add(resultPanel);

        /* ---HashText tab ends--- */
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PsychoHasherGui().setVisible(true);
        });
    }
}
