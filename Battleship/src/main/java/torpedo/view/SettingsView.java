package torpedo.view;

import torpedo.controller.Difficulty;
import torpedo.controller.GUIController;
import torpedo.controller.GameController;
import torpedo.model.Ship;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static torpedo.controller.Difficulty.EASY;
import static torpedo.controller.Difficulty.MEDIUM;
import static torpedo.controller.GUIController.*;

/**
 * Az egyedi játékbeállításokat tartalmazó képernyő nézet megvalósítása.
 */
public class SettingsView extends JPanel {

    /**
     * A beállítandó játékban az ellenfél bot nehézsége.
     */
    private Difficulty d = EASY;

    /**
     * A tábla méretét beállító tolóka.
     */
    private JSlider sizeSlider;

    /**
     * A játékos nevét beolvasó szövegdoboz.
     */
    private JTextField nameTextField;

    /**
     * A hajók mennyiségét beállító táblázat.
     */
    private JTable shipsTable;

    /**
     * A játékos neve felirat.
     */
    private JLabel playerNameLabel;

    /**
     * A bot nehézségét lehet ezzel a gombbal állítani.
     */
    private JRadioButton dumButton;

    /**
     * A bot nehézségét lehet ezzel a gombbal állítani.
     */
    private JRadioButton smartButton;

    /**
     * A táblaméret állítását jelző szöveg.
     */
    private JLabel tableSizeLabel;

    /**
     * A nézet címe.
     */
    private JLabel titleLabel;

    /**
     * A botok nehézségét állító szöveg.
     */
    private JLabel botStrengthLabel;

    /**
     * A táblázatot tartalmazó JScrollPane.
     */
    private JScrollPane tableScrollPane;

    /**
     * A játék indítása gomb.
     */
    private JButton submitButton;


    /**
     * Konstruktor, meghívja a komponenseket elrendező és beállító függvényt.
     */
    public SettingsView(){
        initComponents();
    }

    /**
     * Meghívja az egyes komponensek létrehozó és beállító függvényeit, majd elrendezi őket a képernyőn.
     */
    private void initComponents() {

        initLabels();
        initButtons();
        initTable();
        initSlider();
        initTextField();


        setBackground(bgColor);
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(216, 216, 216)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(botStrengthLabel)
                                                .addGap(56, 56, 56)
                                                .addComponent(dumButton)
                                                .addGap(54, 54, 54)
                                                .addComponent(smartButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tableScrollPane, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(playerNameLabel)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(123, 123, 123)
                                                        .addComponent(submitButton, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE))))
                                                .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(tableSizeLabel)
                                                        .addGap(37, 37, 37)
                                                        .addComponent(sizeSlider, GroupLayout.PREFERRED_SIZE, 657, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(566, 566, 566)
                                .addComponent(titleLabel)
                                .addGap(566, 566, 566)
                        ));

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(titleLabel)
                                .addGap(70, 70, 70)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(tableSizeLabel)
                                        .addComponent(sizeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(botStrengthLabel)
                                        .addComponent(dumButton)
                                        .addComponent(smartButton))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(playerNameLabel)
                                                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addGap(85, 85, 85)
                                            .addComponent(submitButton))
                                        .addComponent(tableScrollPane, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(197, Short.MAX_VALUE)
        ));
    }

    /**
     * Létrehozza és beállítja a nézet informális szövegeit -címek a bevitel mezők körül-.
     */
    private void initLabels(){
        playerNameLabel = new JLabel();
        tableSizeLabel = new JLabel();
        titleLabel = new JLabel();
        botStrengthLabel = new JLabel();

        playerNameLabel.setFont(headFont);
        playerNameLabel.setForeground(fgColor);
        playerNameLabel.setText("Player name");

        tableSizeLabel.setFont(headFont);
        tableSizeLabel.setForeground(fgColor);
        tableSizeLabel.setText("Table size");

        titleLabel.setFont(titleFont);
        titleLabel.setForeground(fgColor);
        titleLabel.setText("Settings");

        botStrengthLabel.setFont(headFont);
        botStrengthLabel.setForeground(fgColor);
        botStrengthLabel.setText("Bot strength");

    }

    /**
     * Létrehozza és beállítja a gombokat, továbbá azt is, hogy egyszerre csak az egyik lehessen kiválasztható.
     */
    private void initButtons() {
        dumButton = new JRadioButton();
        smartButton = new JRadioButton();
        submitButton = new JButton();

        dumButton.setBackground(bgColor);
        dumButton.setFont(commonFont);
        dumButton.setForeground(fgColor);
        dumButton.setText("DumBot");
        dumButton.addActionListener(this::dumButtonActionPerformed);
        dumButton.setFocusPainted(false);
        dumButton.setSelected(true);

        smartButton.setBackground(bgColor);
        smartButton.setFont(commonFont);
        smartButton.setForeground(fgColor);
        smartButton.setText("SmartBot");
        smartButton.addActionListener(this::smartButtonActionPerformed);
        smartButton.setFocusPainted(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(dumButton);
        buttonGroup.add(smartButton);

        submitButton.setBackground(bgColor);
        submitButton.setFont(headFont);
        submitButton.setForeground(fgColor);
        submitButton.setText("Create Game");
        submitButton.setBorder(new LineBorder(fgColor, 2, true));
        submitButton.setDefaultCapable(false);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(this::submitButtonActionPerformed);

    }

    /**
     * Létrehozza és beállítja a hajók számainak változtatását elősegítő táblát.
     * Az értékeket JSpinner-rel is lehet módosítani, 0 az alsó határ.
     */
    private void initTable(){
        shipsTable = new JTable();

        shipsTable.setBackground(bgColor);
        shipsTable.setFont(commonFont);
        shipsTable.setForeground(fgColor);
        shipsTable.setModel(new DefaultTableModel(
                new Object [][] {
                        {1, 2},
                        {2, 3},
                        {3, 2},
                        {4, 1},
                        {5, 0}
                },
                new String [] {
                        "Ship length", "Amount"
                }
        ) {
            final Class[] types = new Class [] {
                    Integer.class, Integer.class
            };
            final boolean[] canEdit = new boolean [] {
                    false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        shipsTable.getColumnModel().getColumn(1).setCellEditor(new GUIController.MySpinnerEditor());
        shipsTable.setGridColor(fgColor);
        shipsTable.setRowHeight(40);
        shipsTable.setShowGrid(true);
        shipsTable.setShowHorizontalLines(false);
        shipsTable.setRowSelectionAllowed(false);
        shipsTable.getTableHeader().setResizingAllowed(false);
        shipsTable.getTableHeader().setReorderingAllowed(false);
        shipsTable.getTableHeader().setFont(headFont);
        shipsTable.getTableHeader().setForeground(fgColor);
        shipsTable.getTableHeader().setBackground(bgColor);
        shipsTable.getTableHeader().setBorder(BorderFactory.createLineBorder(fgColor));

        tableScrollPane = new JScrollPane(shipsTable);
        tableScrollPane.setForeground(fgColor);
        tableScrollPane.setBackground(bgColor);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(fgColor));

    }

    /**
     * Beállítja a táblák méretetit megadó csúszkát, hogyan jelenjen meg, milyen osztásokkal, tulajdonságokkal.
     */
    private void initSlider(){
        sizeSlider = new JSlider();

        sizeSlider.setBackground(bgColor);
        sizeSlider.setFont(new Font("Britannic Bold", Font.PLAIN, 18));
        sizeSlider.setForeground(fgColor);
        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.setMaximum(20);
        sizeSlider.setMinimum(4);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setValue(10);

    }

    /**
     * Beállítja a név beviteli mezőjét.
     */
    private void initTextField(){
        nameTextField = new JTextField();

        nameTextField.setBackground(bgColor);
        nameTextField.setFont(headFont);
        nameTextField.setForeground(new Color(137,137,205));
        nameTextField.setText("Human");
        nameTextField.setMinimumSize(new Dimension(7, 100));
    }

    /**
     * Beállítja a játék nehézségét könnyűre.
     * @param evt a DumBot RadioButton kiválasztása.
     */
    private void dumButtonActionPerformed(ActionEvent evt) {
        d = EASY;
    }

    /**
     * Beállítja a játék nehézségét nehezebbre.
     * @param evt a SmartBot RadioButton kiválasztása
     */
    private void smartButtonActionPerformed(ActionEvent evt) {
        d = MEDIUM;
    }

    /**
     * Elindít egy új játékot a képernyőn bevitt értékekkel, paraméterekkel.
     * @param evt a Create Game gomb megnyomása.
     */
    private void submitButtonActionPerformed(ActionEvent evt) {

        boolean valid = true;
        String name = nameTextField.getText();
        int k = sizeSlider.getValue();
        ArrayList<Ship> ships = new ArrayList<>();
        int sumPLace = 0;
        int maxL = 1;

        for(int i = 0; i < 5; i++){
            if((int)shipsTable.getValueAt(i, 1) > 0){
                for(int j = 0; j< (int)shipsTable.getValueAt(i, 1); j++) {
                    if(k == 4 && i == 4){
                        valid = false;
                    }
                    else {
                        ships.add(new Ship((int) shipsTable.getValueAt(i, 0)));
                        sumPLace += (i+1+2)*3;
                        maxL = i+ 1;
                    }

                }
            }
        }
        if(k*k-sumPLace < 0)
            if(k*k - sumPLace < -(maxL)*2)
                valid = false;


        if(valid) {
            GUIController.toNewGameView(k, ships, name, d);
            GameController.setStarted(false);
            GameController.setReset(false);
        }
    }

}
