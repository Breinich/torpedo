package view;

import controller.GUIController;
import controller.GameController;
import exceptions.FieldNotFoundException;
import model.Direction;
import model.Ship;
import model.State;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;

import static controller.GUIController.*;
import static model.Direction.DOWN;
import static model.Direction.RIGHT;

/**
 * A játékot megejelenítő nézetet létrehozó és kezelő osztály
 */
public class GameView extends JPanel implements Serializable {

    /**
     * Nézeten megjelenő cím.
     */
    private JLabel titleLabel;

    /**
     * A táblázatot tartalmazó JScrollPane.
     */
    private JScrollPane tableScrollPane;

    /**
     * Az egyik táblát tartalmazó JPanel.
     */
    private JPanel jPanel1;

    /**
     * A másik táblát tartalmazó JPanel.
     */
    private JPanel jPanel2;

    /**
     * A nézet alján megjelenő gomb
     */
    private JButton button;

    /**
     * Az üzeneteket tartalmazó szövegdoboz tároló JScrollPane-je.
     */
    private JScrollPane messageScrollPane;

    /**
     * Az üzeneteket tartalmazó szövegdoboz.
     */
    private JTextArea messageArea;

    /**
     * Kezdetben az elhelyezendő, később az ellenfél hajóinak a számát mutató táblázat.
     */
    private JTable ships;

    /**
     * A játékos hajóit tartalmazó táblát alkotó gombok kollekciója.
     */
    private final ArrayList<FieldButton> fieldButtons;

    /**
     * A játékos tippjeit tartalmazó táblát alkotó gombok kollekciója.
     */
    private final ArrayList<FieldButton> guessButtons;

    /**
     * A játékos hajóit tartalmazó táblán az üres mezők színe - 'tenger' színe.
     */
    private final Color seaColor = new Color(21,66,255);

    /**
     * A hajók színe.
     */
    private final Color shipColor = new Color(77, 30, 0);

    /**
     * Az eltalált hajók színe.
     */
    private final Color damagedShipColor = new Color(122, 122, 122);

    /**
     * Az eltalált üres mezők / 'tenger' színe.
     */
    private final Color notHitColor = new Color(129, 129, 204);

    /**
     * Az eltalált hajók színe a tipptáblán.
     */
    private final Color hitColor = new Color(197, 0, 0);

    /**
     * Azoknak az üres mezőknek / 'tengernek' a színe, amik szomszédosak legalább egy hajót tartalmazó mezővel,
     * így oda már nem lehet másik hajót helyezni.
     */
    private final Color neighbourColor = new Color(62, 62, 204, 255);

    /**
     * A táblák fokszáma.
     */
    private final int k;

    /**
     * Számolja, hogy melyik hosszúságú hajóból mennyii van még hátra a hajók elhelyezésekor.
     */
    private final int[] placeShipRemains = new int[5];

    /**
     * Kezdeti hajómennyiségeket tároja.
     */
    private final int[] startShipRemains = new int[5];

    /**
     * Ideiglenesen tárolja a felhasználó által utolsó 2 alkalommal megnyomott mező koordinátáit,
     * a hajók elhelyezésénél van szerepe.
     */
    private final int[][] placeCoord = new int[][] {{-1,-2},{-3, -4}};

    /**
     * Azt jelenti, hogy az éppen megnyomott mező -a hajók elhelyezése során-
     * a második, vagy az első koordinátáját mutatja-e a hajónak.
     */
    private boolean secondCoord = false;

    /**
     * Vége van a hajók elhelyezésének?
     */
    private boolean placeturnEnd = false;

    /**
     * A hajók elhelyezéséről szolgál a felhasználónak segítséggel.
     */
    private final String placeTxt = "At first, you need to place all of your\nships, then will the party go on.\nChoose 2 game fields, than press the\nSubmit Place button.";

    /**
     * Egy mezőt jelképező egyedi gomb, ami tárolja a saját koordinátáit.
     */
    private static class FieldButton extends JButton{

        /**
         * A gombot tartalmazó sor száma a táblán.
         */
        int row;

        /**
         * A gombot tartalmazó oszlop száma a táblán.
         */
        int col;
        /**
         * Konstruktor, beállítja a mező sorát és oszlopát.
         * @param r sor
         * @param c oszlop
         */
        FieldButton(int r, int c){
            row = r;
            col = c;

        }
    }

    /**
     * A nézet konstruktora,
     * létrehozza a táblákat, beállítja a hajószámokat és meghívja a nézet komponenseit beállító függvényt.
     */
    public GameView(){
        k = GameController.getK();
        for(int i = 0; i < 5; i++)
        {
            placeShipRemains[i] = GameController.getShipAmount(i+1);
            startShipRemains[i] = GameController.getShipAmount(i+1);
        }
        fieldButtons = new ArrayList<>();
        guessButtons = new ArrayList<>();
        initComponents();

        if(GameController.isSaved()){
            loadGame();
        }
    }

    /**
     * Meghívja az egyes komponenseket létrehozó / beállító függvényeket,
     * majd elrendezi őket.
     */
    private void initComponents() {

        initTitle();
        initButton();
        initPanels();
        initMessageArea();
        initShips();

        setBackground(bgColor);
        setForeground(fgColor);
        setMinimumSize(new Dimension(1280, 720));


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addComponent(titleLabel)
                                                .addComponent(tableScrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(button, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(messageScrollPane, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titleLabel)
                                .addPreferredGap(ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(messageScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(80, 80, 80)
                                                .addComponent(tableScrollPane, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
                                                .addGap(50, 50, 50)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(button, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(23, Short.MAX_VALUE))
        );
    }

    /**
     * Beállítja és létrehozza a hajókat tartalmazó táblázatot, kínézetét.
     */
    private void initShips() {
        tableScrollPane = new JScrollPane();
        ships = new JTable();

        tableScrollPane.setBackground(bgColor);
        tableScrollPane.setForeground(fgColor);

        ships.setBackground(bgColor);
        ships.setFont(commonFont);
        ships.setForeground(fgColor);
        ships.setModel(new DefaultTableModel(
                new Object [][] {
                        {1, 0},
                        {2, 0},
                        {3, 0},
                        {4, 0},
                        {5, 0}
                },
                new String [] {
                        "Size", "Amount"
                }
        ) {
            final Class[] types = new Class [] {
                    Integer.class, Integer.class
            };
            final boolean[] canEdit = new boolean [] {
                    false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        for(int i = 0; i < 5; i++){
            ships.setValueAt(GameController.getShipAmount(i+1), i, 1);
        }
        ships.setFocusable(false);
        ships.setGridColor(fgColor);
        ships.setRowHeight(40);
        ships.getTableHeader().setBackground(bgColor);
        ships.getTableHeader().setForeground(fgColor);
        ships.getTableHeader().setFont(commonFont);
        ships.setFont(headFont);
        tableScrollPane.setViewportView(ships);
        tableScrollPane.getViewport().setBackground(bgColor);

    }

    /**
     * Beállítja és létrehozza az üzeneteket megjelenítő szövegdobozt.
     */
    private void initMessageArea() {

        messageScrollPane = new JScrollPane();
        messageArea = new JTextArea();

        messageArea.setBackground(bgColor);
        messageArea.setColumns(20);
        messageArea.setFont(new Font("Britannic Bold", Font.PLAIN, 18));
        messageArea.setForeground(fgColor);
        messageArea.setRows(5);
        messageArea.setTabSize(4);
        messageArea.setText("Hello "+GameController.pHuman.getName()+"!\n"+placeTxt);
        messageArea.setCaretColor(fgColor);
        messageArea.setFocusable(false);
        messageScrollPane.setViewportView(messageArea);

    }

    /**
     * Beállítja és létrehozza a táblákat tartalmazó JPaneleket és feltölti a mezőkkel őket.
     */
    private void initPanels() {
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();

        jPanel1.setPreferredSize(new Dimension(450, 450));
        GridLayout gridLayout1 = new GridLayout(k,k, 0, 0);
        jPanel1.setLayout(gridLayout1);


        jPanel2.setPreferredSize(new Dimension(450, 450));
        jPanel2.setOpaque(true);
        jPanel2.setBackground(new Color(255, 255, 255));
        GridLayout gridLayout2 = new GridLayout(k,k, 0, 0);
        jPanel2.setLayout(gridLayout2);

        for(int row = 0; row < k; row++){
            for(int col = 0; col < k; col++) {
                FieldButton fieldButton = new FieldButton(row, col);
                FieldButton guessButton = new FieldButton(row, col);


                fieldButton.setEnabled(true);
                fieldButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
                fieldButton.setBackground(seaColor);
                fieldButton.addActionListener(this::fieldButtonActionPerformed);

                guessButton.setEnabled(false);
                guessButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
                guessButton.setBackground(Color.BLACK);
                guessButton.addActionListener(this::guessButtonActionPerformed);

                jPanel1.add(fieldButton);
                jPanel2.add(guessButton);
                fieldButtons.add(fieldButton);
                guessButtons.add(guessButton);
            }
        }

    }

    /**
     * Beállítja és létrehozza az alsó multifunkciós gombot.
     */
    private void initButton() {
        button = new JButton();

        button.setBackground(bgColor);
        button.setFont(headFont);
        button.setForeground(fgColor);
        button.setText("Submit Place");
        button.setBorder(new LineBorder(fgColor, 1, true));
        button.setFocusPainted(false);
        button.addActionListener(this::buttonActionPerformed);
    }

    /**
     * Létrehozza és beállítja a nézet címét.
     */
    private void initTitle() {
        titleLabel = new JLabel();

        titleLabel.setBackground(bgColor);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(fgColor);
        titleLabel.setText("Battleship Game");
    }

    /**
     * Üzenet megjellenítése.
     * @param msg az üzenet.
     */
    private synchronized void shortMessage(String msg){
        messageArea.setText(msg);
    }

    /**
     * Ha a játékos feladta a játékot.
     */
    private void surrender(){
        shortMessage(GameController.pHuman.getName()+" surrendered.");
        GameController.pHuman.setLost(true);
        GameController.pHuman.setWon(false);
        GameController.pBot.setWon(true);
        GameController.pBot.setLost(false);
        GameController.setStarted(false);
        GameController.setReset(true);
        GameController.gameEnd();
    }

    /**
     * A játék - tippelések - elindításakor hívódik meg, beállítja a nézetnek a megfelelő viselkedést.
     */
    private void startGame(){
        updateFieldView();

        for(FieldButton f : fieldButtons){
            f.setEnabled(false);
        }

        enableGuess();
        shortMessage("Choose one tile on the guessboard \nto make a guess.");

        GameController.setStarted(true);
        for(int i = 0; i < 5; i++) {
            ships.setValueAt(startShipRemains[i], i, 1);
        }
        button.setText("Give Up");
    }

    /**
     * Egy hajó elhelyezése a játékos táblájában.
     */
    private void placeShip(){
        shortMessage(placeTxt);
        Direction dir = null;
        int l = 0;
        int startRow = -1;
        int startColumn = -1;
        boolean valid = false;


        if(placeCoord[0][0] == placeCoord[1][0]){
            dir = RIGHT;
            valid = true;
            startRow = placeCoord[0][0];
            l = Math.abs(placeCoord[1][1] - placeCoord[0][1]) + 1;
            startColumn = Math.min(placeCoord[0][1], placeCoord[1][1]);
        }
        else if(placeCoord[0][1] == placeCoord[1][1]){
            dir = DOWN;
            valid = true;
            l = Math.abs(placeCoord[1][0] - placeCoord[0][0]) + 1;
            startColumn = placeCoord[0][1];
            startRow = Math.min(placeCoord[0][0], placeCoord[1][0]);
        }
        else{
            shortMessage("INVALID SHIP COORDINATES\nTry choosing another 2 (or 1 twice)!");
        }

        if(valid && l < 6 && l > 0) {
            if ((int) ships.getValueAt(l - 1, 1) > 0) {

                Ship ship = new Ship(l);

                try {
                    if (GameController.pHuman.placeShip(ship, startRow, startColumn, dir)) {
                        int temp = (int) ships.getValueAt(l - 1, 1);
                        placeShipRemains[l - 1] -= 1;
                        ships.setValueAt(temp - 1, l - 1, 1);
                        ships.revalidate();
                    }
                } catch (FieldNotFoundException e) {
                    shortMessage("Rossz sor- és oszlopindexek,\nvalami nem jo a levesben.");
                }
            }
            else{
                shortMessage("Insufficient ship amount.");
            }
            for (int i : placeShipRemains) {
                if (i != 0) {
                    placeturnEnd = false;
                    break;
                } else {
                    placeturnEnd = true;
                }
            }
        }
        updateFieldView();
        if(placeturnEnd) {
            button.setText("Start Game");
            button.revalidate();
        }
    }

    /**
     * A multifunkciós gomb megnyomása esetén a játékállapotnak megfelelő függvényt hívja meg.
     * @param evt a középső-alsó nagyobb gomb megnyomása.
     */
    private void buttonActionPerformed(ActionEvent evt) {

        if(GameController.isReset()){
            GUIController.toMenuView();
        }
        else if(GameController.isStarted()){
            surrender();
        }
        else if(placeturnEnd) {
            startGame();
        }
        else {
            placeShip();
        }
    }

    /**
     * Beállítja az ideiglenes hajóelhelyezés kezdő-, vagy végpont koordinátáit.
     * @param evt a hajókat tartalmazó táblán egy mező megnyomása.
     */
    private void fieldButtonActionPerformed(ActionEvent evt){
        shortMessage(placeTxt);
        FieldButton src = (FieldButton) evt.getSource();
        if(!secondCoord){
            placeCoord[0][0] = src.row;
            placeCoord[0][1] = src.col;
            secondCoord = true;
        }
        else{
            placeCoord[1][0] = src.row;
            placeCoord[1][1] = src.col;
            secondCoord = false;
        }
    }

    /**
     * Egy tipp leadása.
     * @param evt a tipptáblán egy gomb megnyomása.
     */
    private void guessButtonActionPerformed(ActionEvent evt){
        FieldButton src = (FieldButton)evt.getSource();

        try {
            GameController.pHuman.guess(src.row, src.col, GameController.pBot);
            int l = GameController.pBot.getLatestSankSize();
            if(l > 0){
                int n = (int)ships.getValueAt(l-1, 1);
                ships.setValueAt(n-1, l-1, 1);
            }
        }
        catch (FieldNotFoundException e) {
            throw new RuntimeException();
        }
        updateGuessView();

        if(GameController.pHuman.gameWon()){
            GameController.setReset(true);
            GameController.gameEnd();
        }


        GameController.bot.setGuess();
        updateFieldView();

        if(GameController.pBot.gameWon()){
            GameController.setReset(true);
            GameController.gameEnd();
        }

    }

    /**
     * A hajókat tartalmazó tábla kinézetének a frissítése.
     */
    private void updateFieldView(){
        for(FieldButton f : fieldButtons){
            try{
                State s = GameController.pHuman.getRealState(f.row, f.col);
                switch(s){
                    case HAJO -> {f.setBackground(shipColor); f.revalidate();}
                    case FOGLALT -> {f.setBackground(neighbourColor); f.revalidate();}
                    case TALALT -> {f.setBackground(damagedShipColor); f.revalidate();}
                    case NEMTALALT -> {f.setBackground(notHitColor); f.revalidate();}
                }
            }
            catch(FieldNotFoundException e){
                //do nothing
            }
        }
    }

    /**
     * A tippeket tartalmazó tábla kinézetének a frissítése.
     */
    private void updateGuessView(){
        for(FieldButton f : guessButtons){
            try{
                State s = GameController.pHuman.getGuessState(f.row, f.col);
                switch (s){
                    case TALALT -> {f.setBackground(hitColor); f.revalidate();}
                    case NEMTALALT -> {f.setBackground(new Color(255, 255, 255)); f.revalidate();}
                }
            }
            catch(FieldNotFoundException e){
                //do nothing
            }
        }
    }

    /**
     * A tipptáblának a mezőinek (gombjainak) engedélyezése. (tipikusan az összes hajó elhelyezése után)
     */
    private void enableGuess(){
        for(FieldButton f : guessButtons){
            f.setEnabled(true);
        }
    }

    /**
     * Mentett játék betöltése.
     */
    private void loadGame(){
        startGame();
        updateGuessView();

        for(int i = 0; i < 5; i++) {
            ships.setValueAt(GameController.pBot.getShipCount(i+1), i, 1);
        }
    }
}
