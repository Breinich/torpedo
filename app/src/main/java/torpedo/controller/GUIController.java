package torpedo.controller;

import torpedo.model.Ship;
import torpedo.view.GameView;
import torpedo.view.MenuView;
import torpedo.view.SettingsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import static torpedo.controller.Difficulty.EASY;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * A játék megjelenését vezérlő osztály.
 */
public class GUIController {

    /**
     * A játék háttérszíne.
     */
    public static final Color bgColor;

    /**
     * a játék előtérszíne.
     */
    public static final Color fgColor;

    /**
     * Cím betűtípus.
     */
    public static final Font titleFont;

    /**
     * Kiemelt betűtípus.
     */
    public static final Font headFont;

    /**
     * Nagyobb kiemelt betűtípus.
     */
    public static final Font lHeadFont;

    /**
     * Általános betűtípus.
     */
    public static final Font commonFont;

    /**
     * Menü nézet JPanel.
     */
    private static final MenuView menu;

    /**
     * Játék nézet JPanel.
     */
    static GameView game;

    /**
     * Beállítások nézet JPanel.
     */
    private static final SettingsView settings;

    /**
     * Főablak.
     */
    private static final JFrame mainFrame = new JFrame("Torpedó");

    /**
     * A programblakban megjelenő felső menüsáv.
     */
    private static JMenuBar jMenuBar;

    static {
        fgColor = new Color(0, 0, 102);
        bgColor = new Color(255, 255, 204);
        commonFont = new Font("Britannic Bold", Font.PLAIN, 24);
        lHeadFont = new Font("Britannic Bold", Font.PLAIN, 48);
        headFont = new Font("Britannic Bold", Font.PLAIN, 36);
        titleFont = new Font("Agency FB", Font.BOLD, 52);

        menu = new MenuView();
        settings = new SettingsView();
    }

    /**
     * Egyedi cellaszerkesztő belső osztály, hogy
     * a hajók számát a táblázatban JSpinner-rel lehessen állítani.
     */
    public static class MySpinnerEditor extends DefaultCellEditor
    {

        /**
         * A cella értékét változtató JSpinner.
         */
        final JSpinner sp;

        /**
         * A szerkesztő JSPinner DefaultEditor -ja.
         */
        final JSpinner.DefaultEditor defaultEditor;

        /**
         * A cellában lévő szövegbeviteli mező.
         */
        final JTextField text;

        /**
         *  Konstruktor,
         *  beállítja a cellaszerkesztőben a JSpinner-t és a JTextField-et.
         */
        public MySpinnerEditor() {
            super(new JTextField());
            sp = new JSpinner();
            sp.setModel(new SpinnerNumberModel(0,0,20,1));
            defaultEditor = ((JSpinner.DefaultEditor)sp.getEditor());
            text = defaultEditor.getTextField();
            text.setFont(commonFont);
            text.setForeground(fgColor);
            text.setBackground(bgColor);
        }

        /**
         * Adott cellában érvényes CellEditorComponent megadása
         * @param table tábla, aminek aza adatát szerkeszti az osztály
         * @param value érték, amit be szeretnénk állítani a CellEditor értékeként
         * @param isSelected ki van-e választva az adott cella
         * @param row cellát meghatározó sor indexe
         * @param column celáát meghatározó oszlop indexe
         * @return a TableCellEditor-ban lévő Component
         */
        public Component getTableCellEditorComponent(JTable table, Object
                value, boolean isSelected, int row, int column)
        {
            sp.setValue(value);
            return sp;
        }

        /**
         *
         * @return a CellEditor-ban szereplő értékkel
         */
        public Object getCellEditorValue() {
            return sp.getValue();
        }
    }

    /**
     * Konstruktor, beállítja a főablakot és a komponenseit.
     */
    public GUIController() {
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(new java.awt.Dimension(1280, 720));
        menuInit();
        mainFrame.setJMenuBar(jMenuBar);
        mainFrame.add(menu);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Beállítja a felső menüsáv tartalmát.
     */
    private void menuInit(){
        jMenuBar = new JMenuBar();
        JMenu gameMenu = new JMenu();
        JMenuItem newGameMenu = new JMenuItem();
        JMenuItem newCustomGameMenu = new JMenuItem();
        JMenuItem saveGameMenu = new JMenuItem();
        JMenuItem loadGameMenu = new JMenuItem();


        jMenuBar.setBackground(bgColor);

        gameMenu.setBackground(bgColor);
        gameMenu.setText("Game");
        gameMenu.setFont(new Font("Felix Titling", Font.BOLD, 18));
        gameMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        newGameMenu.setText("New Game");
        newGameMenu.addActionListener(this::newGameActionPerformed);
        newGameMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameMenu.add(newGameMenu);

        newCustomGameMenu.setText("New Custom Game");
        newCustomGameMenu.addActionListener(this::newCustomGameActionPerformed);
        newCustomGameMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameMenu.add(newCustomGameMenu);

        saveGameMenu.setText("Save Game and Exit");
        saveGameMenu.addActionListener(this::saveGameActionPerformed);
        saveGameMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameMenu.add(saveGameMenu);

        loadGameMenu.setText("Load Game");
        loadGameMenu.addActionListener(GUIController::loadGameActionPerformed);
        loadGameMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameMenu.add(loadGameMenu);

        jMenuBar.add(gameMenu);
    }

    /**
     * Meghívja az új alapjátékot létrehozó függvényt.
     * @param evt "New Game" című gomb megnyomása.
     */
    private void newGameActionPerformed(ActionEvent evt) {
        toNewGameView();
    }

    /**
     * Meghívja a beállításokat megjelenítő függvényt.
     * @param evt "New Custom Game" gomb megnyomása.
     */
    private void newCustomGameActionPerformed(ActionEvent evt) {
        toSettingsView();
    }

    /**
     * A játék mentése -ha már elkezdődött-, felugró üzenetabalakon a sikeresség megjelenítése, majd kilépés a főmenübe.
     * @param evt "Save Game and Exit" menü kiválasztása.
     */
    private void saveGameActionPerformed(ActionEvent evt) {
        if(GameController.isStarted()) {
            try {
                IOController.save();
                JFrame parent = new JFrame();
                JOptionPane.showMessageDialog(parent, "The game has been saved.");
                GUIController.toMenuView();
            }
            catch (IOException e){
                e.printStackTrace();
                JFrame parent = new JFrame();
                JOptionPane.showMessageDialog(parent, "The game couldn't have been saved.");
            }
        }


    }

    /**
     * Betölti az utoljára mentett játékot és ennek sikerességéről felugró üzenetablakban értesít.
     * @param evt "Load Game" gomb megnyomása, vagy a "Load Game" menü kiválasztása.
     */
    public static void loadGameActionPerformed(ActionEvent evt) {
        try{
            IOController.load();
            JFrame parent = new JFrame();
            JOptionPane.showMessageDialog(parent, "The game has been loaded.");
            GUIController.toGameView();
        }
        catch(IOException | ClassNotFoundException e){
            JFrame parent = new JFrame();
            JOptionPane.showMessageDialog(parent, "The game couldn't have been loaded.");
        }
    }

    /**
     * Új játék létrehozása paraméterek nélkül.
     * Létrehoz pár hajót és meghívja a paraméterezett változatát ezekkel a hajókkal, 10-es tábla fokszámmal.
     */
    public static void toNewGameView() {
        ArrayList<Ship> ships = new ArrayList<>();
        ships.add(new Ship(4));
        for(int i = 0; i< 2; i++){
            ships.add(new Ship(3));
            ships.add(new Ship(2));
            ships.add(new Ship(1));
        }
        ships.add(new Ship(1));

        toNewGameView(10, ships);
    }

    /**
     * Új játék létrehozása, meghívja a nála több paramétert váró azonos függvényt
     * alapértelmezett játékosnévvel és nehézséggel.
     * @param k a tábla fokszáma
     * @param ships a hajókat tároló kollekció
     */
    public static void toNewGameView(int k, ArrayList<Ship> ships) {
        toNewGameView(k, ships, "Player", EASY );
    }

    /**
     * Új játék létrehozása és megjelenítése a megadott paraméterek alapján.
     * @param k a tábla fokszáma
     * @param ships a hajókat tartalmazó kollekció
     * @param n1 a játékos felhasználóneve
     * @param d a játéék nehézsége
     */
    public static void toNewGameView(int k, ArrayList<Ship> ships, String n1, Difficulty d) {
        Container c = mainFrame.getContentPane();
        c.removeAll();
        new GameController(k, ships, n1, d);
        game = new GameView();
        mainFrame.setJMenuBar(jMenuBar);
        c.add(game);
        c.revalidate();
    }

    /**
     * A beállítások nézet megjelenítése.
     */
    public static void toSettingsView() {
        Container c = mainFrame.getContentPane();
        c.removeAll();
        mainFrame.setJMenuBar(jMenuBar);
        c.add(settings);
        c.revalidate();
        c.repaint();
    }

    /**
     * A főmenü nézet megjelenítése.
     */
    public static void toMenuView(){
        Container c = mainFrame.getContentPane();
        c.removeAll();
        mainFrame.setJMenuBar(jMenuBar);
        c.add(menu);
        c.revalidate();
        c.repaint();
    }

    /**
     * A már korábban beállított játék nézet megjelenítése.
     */
    public static void toGameView(){
        Container c = mainFrame.getContentPane();
        c.removeAll();
        mainFrame.setJMenuBar(jMenuBar);
        c.add(game);
        c.revalidate();
    }

}




