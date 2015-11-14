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
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author psychocoder
 */
public class MainToolWindow extends JFrame {

    private final int DEF_WIDTH = 800;
    private final int DEF_HEIGHT = 600;

    public MainToolWindow() throws HeadlessException {
        setSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        initComponents();
    }

    public MainToolWindow(int WIDTH, int HEIGHT) throws HeadlessException {
        setSize(new Dimension(WIDTH, HEIGHT));
        initComponents();
    }

    private void initComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Unable to set system look and feel.");
        }

        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("PsychoHasher - All Purpose Hashing tool");
        setContentPane(new MainPanel());
        setLayout(new BorderLayout());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainToolWindow().setVisible(true);
        });
    }
}