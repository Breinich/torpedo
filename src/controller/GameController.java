package controller;

import model.Player;
import model.Ship;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A játék állapotát vezérlő osztály.
 */
public class GameController  implements Serializable {

    /**
     * Az élő játékos ellenfelét vezérlő objektum.
     */
    public static BotController bot;

    /**
     * A játékban szereplő 2 játékos.
     */
    public static Player pHuman, pBot;

    /**
     * A tábla foka.
     */
    private static int k;

    /**
     * Elkezdődött-e már a tippelés egymás hajóira?
     */
    private static boolean started;

    /**
     * Törlődjönek-e a játékbeállítások a következő lehetséges alkalommal?
     */
    private static boolean reset;

    /**
     * Mentett játékot indítunk-e?
     */
    private static boolean saved;

    /**
     * A játékban résztvevő hajók és méreteik.
     */
    static ArrayList<Ship> ships;

    static {
        saved = false;
        reset = false;
        started = false;
    }

    /**
     * Konstruktor a mentett játék beállításához
     * -a főbb attribútumok nem a konstruktoron keresztül, hanem külső függvényből állítódnak be-.
     * @param n a tábla foka.
     */
    public GameController(int n){
        k = n;
    }

    /**
     * Ez az alapértelmezett és a leggyakrabban használt konstruktora.
     * Beállítja a játékosokat szimbolizáló objektumokat, a számítógép játékosát vezérlő (Botcontroller) objektumot.
     * Hozzáadja a hajókat a játékhoz.
     * @param n a tábla foka.
     * @param s a hajókat tároló kollekció referenciája.
     * @param n1 Az élő játékos által választott felhasználónév.
     * @param d a számítógép játékosát vezérlő kontroller nehézsége.
     */
    public GameController(int n, ArrayList<Ship> s, String n1, Difficulty d){
        pHuman = new Player(n, n1);

        switch (d){
            case EASY -> {pBot = new Player(n, "DumBot"); bot = new DumBotConroller(s,n, pBot); }
            case MEDIUM -> {pBot = new Player(n, "SmartBot"); bot = new SmartBotController(s, n, pBot);}
        }
        ships = s;
        k = n;
    }

    /**
     * Megadja, hogy egy adott hosszú hajóból mennyi van alapból a játékban / játékos.
     * @param l keresett hajók hossza.
     * @return adott hosszú hajók száma.
     */
    public static int getShipAmount(int l){
        int cnt = 0;
        for(Ship s : ships){
            if(s.getLength() == l)
                cnt++;
        }

        return cnt;
    }

    /**
     *
     * @return a tábla foka.
     */
    public static int getK() {
        return k;
    }

    /**
     *
     * @return elkezdődött-e már a tippelés része a játéknak.
     */
    public static boolean isStarted() {
        return started;
    }

    /**
     * Be lehet állítani vele, hogy a játék tippelős, vagy még hajólerakós állapotban legyen
     * (true és false értékek segítségével, ebben a sorrendben).
     * @param s amire módosítsuk a started értékét.
     */
    public static void setStarted(boolean s) {
        started = s;
    }

    /**
     *
     * @return törlendő-e az adott játékbeállítás.
     */
    public static boolean isReset() {
        return reset;
    }

    /**
     *
     * @param reset törlendő legyen-e az adott játékbeállítás?
     */
    public static void setReset(boolean reset) {
        GameController.reset = reset;
    }

    /**
     * A játék vége esetén hívódik meg.
     * Kiírja egy felugró üzenetablakban, hogy ki nyerte a játékot.
     */
    public static void gameEnd(){
        JFrame parent = new JFrame();
        String winner;
        if(pHuman.gameWon())
            winner = pHuman.getName();
        else
            winner = pBot.getName();

        JOptionPane.showMessageDialog(parent, winner+" won the game!");
        GUIController.toMenuView();
    }

    /**
     * Mentett játék betöltésénél fontos.
     * @param b mentett állapotú legyen-e a játék?
     */
    public static void setSaved(boolean b){
        saved = b;
    }

    /**
     *
     * @return mentett állapotú-e a játék?
     */
    public static boolean isSaved() {
        return saved;
    }
}
