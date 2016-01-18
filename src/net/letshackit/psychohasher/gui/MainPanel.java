/*
 * Copyright 2015 psychocoder.
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 *
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
class MainPanel extends JPanel {

    private final int DEF_WIDTH = 600;
    private final int DEF_HEIGHT = 400;

    private JMenuBar menuBar;
    private JMenu file, hashUtils, help;

    private JMenuItem fileOpen, fileSave, fileExit;
    private JMenuItem hashData, hashFile, hashFileGroup, verifyHash;

    private JSeparator fileSep1, huSep1;

    public MainPanel() {
        setSize(DEF_WIDTH, DEF_HEIGHT);
        initComponents();
    }

    public MainPanel(int WIDTH, int HEIGHT) {
        setSize(WIDTH, HEIGHT);
        initComponents();
    }

    private void initComponents() {
        //setBackground(Color.CYAN);
        setLayout(new BorderLayout());

        createTopMenu();        

    }

    private void createTopMenu() {
        menuBar = new JMenuBar();
        menuBar.setSize(this.getWidth(), 30);
        //menuBar.setBackground(Color.BLACK);

        file = new JMenu("File");
        //file.setForeground(Color.WHITE);
        menuBar.add(file);

        hashUtils = new JMenu("Hash Utils");
        //hashUtils.setForeground(Color.WHITE);
        menuBar.add(hashUtils);

        help = new JMenu("Help");
        //help.setForeground(Color.WHITE);
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
        file.add(fileExit);

        hashData = new JMenuItem("Hash Text");
        hashData.setPreferredSize(new Dimension(140, 25));
        hashUtils.add(hashData);

        hashFile = new JMenuItem("Hash Single File");
        hashFile.setPreferredSize(new Dimension(140, 25));
        hashUtils.add(hashFile);

        hashFileGroup = new JMenuItem("Hash Group of File");
        hashFileGroup.setPreferredSize(new Dimension(140, 25));
        hashUtils.add(hashFileGroup);

        huSep1 = new JSeparator(SwingConstants.HORIZONTAL);
        huSep1.setPreferredSize(new Dimension(140, 5));
        hashUtils.add(huSep1);

        verifyHash = new JMenuItem("Verify Hash");
        verifyHash.setPreferredSize(new Dimension(140, 25));
        hashUtils.add(verifyHash);

        add(menuBar, BorderLayout.NORTH);
    }
}
