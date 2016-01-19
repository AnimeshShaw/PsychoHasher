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
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
public class PsychoHasherGui extends JFrame {

    private final int DEF_WIDTH = 800;
    private final int DEF_HEIGHT = 500;

    private JMenuBar menuBar;
    private JMenu file, hashUtils, help;

    private JMenuItem fileOpen, fileSave, fileExit;
    private JMenuItem hashData, hashFile, hashDisk, verifyHash;

    private JSeparator fileSep1, huSep1;

    private JTabbedPane tabbedPane;

    private JPanel mainpanel, welcome, hashText, hashFiles, hashDisks, verifyHashes;

    public PsychoHasherGui() throws HeadlessException {
        mainpanel = new JPanel(new BorderLayout());
        setSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("PsychoHasher - All Purpose Hashing tool");
        setContentPane(mainpanel);
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

        createTopMenu();
        createMainTabbedPane();
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

        mainpanel.add(menuBar, BorderLayout.NORTH);
    }

    private void createMainTabbedPane() {
        tabbedPane = new JTabbedPane();
        //tabbedPane.setSize(getWidth(), getHeight());

        welcome = new JPanel(new FlowLayout());
        hashText = new JPanel();
        hashFiles = new JPanel();
        hashDisks = new JPanel();
        verifyHashes = new JPanel();

        tabbedPane.addTab("Welcome", welcome);
        tabbedPane.addTab("Hash Text", hashText);
        tabbedPane.addTab("Hash Files", hashFiles);
        tabbedPane.addTab("Hash Disks", hashDisks);
        tabbedPane.addTab("Verify Hashes", verifyHashes);

        mainpanel.add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PsychoHasherGui().setVisible(true);
        });
    }
}
