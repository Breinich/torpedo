package view;

import controller.GUIController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static controller.GUIController.*;

/**
 * A menüt tartalmazó képernyőnézet megvalósítása.
 */
public class MenuView extends JPanel {

    /**
     * A nézet címe.
     */
    private JPanel titlePanel;

    /**
     * Új gyors játékot létrehozó gomb.
     */
    private JButton newGameButton;

    /**
     * Új egyedi beállításokkal rendelkező játékot létrehozó gomb.
     */
    private JButton newSetUpButton;

    /**
     * Az utolsó mentett játékot betöltő gomb.
     */
    private JButton loadGameButton;

    /**
     * Konstruktor, meghívja a nézetet beállító függvényt.
     */
    public MenuView() {
        initComponents();
    }


    /**
     * Meghívja az egyes komponenseket létrehozó függvényeket, majd elrendezi őket a képernyőn.
     */
    private void initComponents() {

        JPanel centerPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel southPanel = new JPanel();

        setBackground(bgColor);
        setMinimumSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());

        initTitle();
        add(titlePanel, BorderLayout.NORTH);

        centerPanel.setBackground(bgColor);
        centerPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        centerPanel.setMinimumSize(new Dimension(740, 0));

        initNewGameButton();
        initNewSetUpButton();
        initLoadGameButton();

        GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(
                centerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(centerPanelLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(centerPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(newGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(newSetUpButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(loadGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, 0))
        );
        centerPanelLayout.setVerticalGroup(
                centerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(centerPanelLayout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(newGameButton)
                                .addGap(46, 46, 46)
                                .addComponent(newSetUpButton)
                                .addGap(46, 46, 46)
                                .addComponent(loadGameButton)
                                .addContainerGap(271, Short.MAX_VALUE))
        );

        add(centerPanel, BorderLayout.CENTER);

        westPanel.setBackground(bgColor);
        westPanel.setMinimumSize(new Dimension(370, 0));

        GroupLayout westPanelLayout = new GroupLayout(westPanel);
        westPanel.setLayout(westPanelLayout);
        westPanelLayout.setHorizontalGroup(
                westPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 370, Short.MAX_VALUE)
        );
        westPanelLayout.setVerticalGroup(
                westPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 636, Short.MAX_VALUE)
        );

        add(westPanel, BorderLayout.WEST);

        southPanel.setBackground(bgColor);
        southPanel.setMinimumSize(new Dimension(370, 0));

        GroupLayout southPanelLayout = new GroupLayout(southPanel);
        southPanel.setLayout(southPanelLayout);
        southPanelLayout.setHorizontalGroup(
                southPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 370, Short.MAX_VALUE)
        );
        southPanelLayout.setVerticalGroup(
                southPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 636, Short.MAX_VALUE)
        );

        add(southPanel, BorderLayout.EAST);
    }

    /**
     * Létrehozza és beállítja a nézet címét.
     */
    private void initTitle() {
        JLabel title = new JLabel();
        titlePanel = new JPanel();

        titlePanel.setBackground(bgColor);
        titlePanel.setForeground(fgColor);
        titlePanel.setLayout(new BorderLayout());

        title.setFont(new Font("Agency FB", Font.BOLD, 70));
        title.setForeground(fgColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setText("Battleship Game");
        title.setHorizontalTextPosition(SwingConstants.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);
    }

    /**
     * Új gyors játékot létrehozó gomb létrehozása és beállítása.
     */
    private void initNewGameButton() {
        newGameButton = new JButton();

        newGameButton.setFont(lHeadFont);
        newGameButton.setForeground(fgColor);
        newGameButton.setText("New Instant Game");
        newGameButton.setBorderPainted(false);
        newGameButton.setContentAreaFilled(false);
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newGameButton.setFocusPainted(false);
        newGameButton.setHideActionText(true);
        newGameButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newGameButton.setInheritsPopupMenu(true);
        newGameButton.addActionListener(this::newGameActionPerformed);
    }

    /**
     * Új egyedi beállításokkal rendelkező játékot létrehozó gomb létrehozása és beállítása.
     */
    private void initNewSetUpButton() {
        newSetUpButton = new JButton();

        newSetUpButton.setFont(lHeadFont);
        newSetUpButton.setForeground(fgColor);
        newSetUpButton.setText("New Custom Game");
        newSetUpButton.setBorderPainted(false);
        newSetUpButton.setContentAreaFilled(false);
        newSetUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newSetUpButton.setFocusPainted(false);
        newSetUpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newSetUpButton.setInheritsPopupMenu(true);
        newSetUpButton.addActionListener(this::setUpGameActionPerformed);
    }

    /**
     * Az utolsó mentett játékot betöltő gomb létrehozása és beállítása.
     */
    private void initLoadGameButton() {
        loadGameButton = new JButton();

        loadGameButton.setFont(lHeadFont);
        loadGameButton.setForeground(fgColor);
        loadGameButton.setText("Load a Saved Game");
        loadGameButton.setBorderPainted(false);
        loadGameButton.setContentAreaFilled(false);
        loadGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadGameButton.setFocusPainted(false);
        loadGameButton.setHorizontalTextPosition(SwingConstants.CENTER);
        loadGameButton.setInheritsPopupMenu(true);
        loadGameButton.addActionListener(GUIController::loadGameActionPerformed);
    }

    /**
     * Új gyors játék indítása
     *
     * @param evt New Instant Game megnyomása
     */
    private void newGameActionPerformed(ActionEvent evt) {
        GUIController.toNewGameView();
    }

    /**
     * Ugrás a játékbeállításokhoz
     *
     * @param evt New Custom Game megnyomása
     */
    private void setUpGameActionPerformed(ActionEvent evt) {
        GUIController.toSettingsView();
    }


}
