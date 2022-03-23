package model;

import exceptions.FieldNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;

import static model.Direction.DOWN;
import static model.Direction.RIGHT;

/**
 * Játékos osztály
 */
public class Player implements Serializable {

    /**
     * A játékos 2 táblája, egyiken a a saját hajói vannak, a másikon pedig a lövéseit / tipjeit tárolja.
     */
    private final Table real, guess;

    /**
     * A táblák fokszáma.
     */
    private final int k;

    /**
     * A hajókat tartallmazó kollekció.
     */
    private final ArrayList<Ship> armada;

    /**
     * Elsüllyedt-e az utolsó erre a játékosra leadott lövés által esetlegesen eltalált hajó?
     */
    private boolean latestSank = false;

    /**
     * Az utolsó erre a játékosra leadott lövés által esetlegesen eltalált hajó hossza.
     */
    private int latestSankSize = 0;

    /**
     * Nyert-e?
     */
    private boolean lost = false;

    /**
     * Vesztett-e?
     */
    private boolean won = false;

    /**
     * A játékos neve.
     */
    private final String name;

    /**
     * Konstruktor, beállítja a játékos tábláit és hajóit.
     * @param k a tábla mérete
     * @param n a játékos neve
     */
    public Player(int k, String n){
        real = new Table(k);
        guess = new Table(k);
        armada = new ArrayList<>();
        name = n;
        this.k = k;
    }

    /**
     * Egy lövés leadása eg másik játékos táblájára és a saját guess táblában az eredmény beállítása.
     * @param sor a célzott sor indexe
     * @param oszlop a célzott oszlop indexe
     * @param other a másik játékos referenciája
     * @return van-e találat?
     * @throws FieldNotFoundException ha a célzott mezőt nem tartalmazza a pálya
     */
    public boolean guess(int sor, int oszlop, Player other) throws FieldNotFoundException {
        boolean siker = other.getAttacked(sor, oszlop);

        guess.setGuess(guess.indexConv(sor, oszlop), siker);
        if(other.latestSank){
            guessAroundWreck(sor, oszlop);
        }

        if(other.lost && siker)
            won = true;

        return siker;
    }

    /**
     * Az összes elsüllyedt hajó melletti mező megjelölése nem találtként
     * a hajóból egy mezőt kap paraméterül.
     * @param sor a hajót egyik tartalmazó mező sora.
     * @param oszlop a hajót egyik tartalmazó mező oszlopa.
     */
    public void guessAroundWreck(int sor, int oszlop){
        Direction d;
        int length = 1;

        /*
         * Függőlegesen és ha kell, vízszintesen is megszámoljuk, hogy mettől eddig tart a hajó
         * i - maximum index
         * j - minimum index
         */

        int i;
        int s = 1;
        int o = 1;
        int fmax = -1, fmin = -1, vmax = -1, vmin = -1;
        d = DOWN;
        if(sor == k-1) {
            i = sor - 1;
            s = -1;
            fmax = sor;
        }
        else{
            i = sor + 1;
        }
        for(; i > -1; i += s){
            try {
                State state = getGuessState(i, oszlop);

                if (state == State.TALALT) {
                    length++;
                } else {
                    if(s == 1){
                        s= -1;
                        fmax = i -1;
                        i = sor;
                    }
                    else {
                        fmin = i +1;
                        break;
                    }
                }
                if(i == k-1){
                    s= -1;
                    fmax = i -1;
                    i = sor;
                }
            } catch (FieldNotFoundException ignored) {}
        }

        if(length == 1) {
            d = RIGHT;
            if(oszlop == k-1){
                i = oszlop -1;
                o = -1;
                vmax = oszlop;
            }
            else{
                i = oszlop+ 1;
            }
            for (; i > -1; i += o) {
                try {
                    State state = getGuessState(sor, i);
                    if (state == State.TALALT) {
                        length++;
                    } else {

                        if (o == 1) {
                            o = -1;
                            vmax = i -1;
                            i = oszlop;
                        } else{
                            vmin = i + 1;
                            break;
                        }
                    }
                    if(i == k-1){
                        o = -1;
                        vmax = i -1;
                        i = oszlop;
                    }
                } catch (FieldNotFoundException e) {
                    //do nothing
                }
            }
        }

        switch(d){
            case RIGHT -> {

                    for (int m = -1; m < 2; m++) {
                        try {
                            guess.setGuess(guess.indexConv(sor + m, vmin - 1), false);
                        }
                        catch(FieldNotFoundException ignored){}
                        try {
                            guess.setGuess(guess.indexConv(sor + m, vmax + 1), false);
                        }
                        catch(FieldNotFoundException ignored){}

                    }
                    for(int m = vmin;m< vmax+1;m++){
                        try {
                            guess.setGuess(guess.indexConv(sor - 1, m), false);
                        }
                        catch(FieldNotFoundException ignored){}
                        try {
                            guess.setGuess(guess.indexConv(sor + 1, m), false);
                        }
                        catch(FieldNotFoundException ignored){}
                    }

            }
            case DOWN -> {

                for (int m = -1; m < 2; m++) {
                    try {
                        guess.setGuess(guess.indexConv(fmin - 1, oszlop + m), false);
                    }
                    catch(FieldNotFoundException ignored){}
                    try{
                        guess.setGuess(guess.indexConv(fmax + 1, oszlop + m), false);
                    }
                    catch(FieldNotFoundException ignored){}
                }
                for(int m = fmin;m<fmax+1;m++){
                    try {
                        guess.setGuess(guess.indexConv(m, oszlop - 1), false);
                    }
                    catch(FieldNotFoundException ignored){}
                    try {
                        guess.setGuess(guess.indexConv(m, oszlop + 1), false);
                    }
                    catch(FieldNotFoundException ignored){}
                }

            }

        }
    }

    /**
     * Egy hajó elhelyezése a játékos saját tábláján.
     * @param ship az elhelyezendő hajó referenciája.
     * @param sor a táblán a hajó kezdetének a sora.
     * @param oszlop a táblán a hajó kezdetének a oszlopa.
     * @param d a hajó elhelyezésének iránya.
     * @return sikeres volt-e az elhelyezés?
     * @throws FieldNotFoundException ha a hajó nem fér bele a táblába.
     */
    public boolean placeShip(Ship ship, int sor, int oszlop, Direction d) throws FieldNotFoundException {
        if(!armada.contains(ship)) {
            boolean siker = true;
            switch (d) {

                case DOWN -> {

                    for (int i = sor; i < sor + ship.getLength(); i++) {
                        Index index = real.indexConv(i, oszlop);
                        if (!real.getState(index).equals(State.SZABAD)) {
                            siker = false;
                            break;
                        }
                    }
                    if (siker) {

                        for (int i = sor; i < sor + ship.getLength(); i++) {
                            Index index = real.indexConv(i, oszlop);
                            if (!real.placeShip(ship, index))
                                throw new RuntimeException();
                        }

                        for (int i = -1; i < 2; i++) {
                            try {
                                Index index = real.indexConv(sor - 1, oszlop + i);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                            try {
                                Index index = real.indexConv(sor + ship.getLength(), oszlop + i);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                        }
                        for (int i = 0; i < ship.getLength(); i++) {

                            try {
                                Index index = real.indexConv(sor + i, oszlop + 1);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                            try {
                                Index index = real.indexConv(sor + i, oszlop - 1);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                        }

                        armada.add(ship);
                    }

                    return siker;
                }

                case RIGHT -> {

                    for (int i = 0; i < ship.getLength(); i++) {
                        Index index = real.indexConv(sor, oszlop + i);
                        if (!real.getState(index).equals(State.SZABAD)) {
                            siker = false;
                            break;
                        }
                    }
                    if (siker) {

                        for (int i = 0; i < ship.getLength(); i++) {
                            Index index = real.indexConv(sor, oszlop + i);
                            if (!real.placeShip(ship, index))
                                throw new RuntimeException();
                        }

                        for (int i = -1; i < 2; i++) {
                            try {
                                Index index = real.indexConv((char) (sor + i), oszlop - 1);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                            try {
                                Index index = real.indexConv((char) (sor + i), oszlop + ship.getLength());
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                        }
                        for (int i = 0; i < ship.getLength(); i++) {

                            try {
                                Index index = real.indexConv((char) (sor + 1), oszlop + i);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                            try {
                                Index index = real.indexConv((char) (sor - 1), oszlop + i);
                                real.setFoglalt(index);
                            } catch (FieldNotFoundException f) {
                                //do nothing
                            }
                        }

                        armada.add(ship);
                    }

                    return siker;
                }

                default -> {
                    return false;
                }
            }
        }
        else
            return false;
    }

    /**
     * Amikor a másik játékos lő egyet ennek a játékosnak a hajóira, akkor hívódik meg ez a függvény.
     * @param sor a célpont sor száma.
     * @param oszlop a célpont oszlop száma.
     * @return ért-e találatot a lövés?
     * @throws FieldNotFoundException ha a megadott sor-oszlop pár a játéktáblán kívül található.
     */
    private boolean getAttacked(int sor, int oszlop) throws FieldNotFoundException {
        Index i = real.indexConv(sor, oszlop);
        if(real.getState(i) == State.TALALT){
            latestSank = false;
            latestSankSize = 0;
            return true;
        }
        else if(real.guessShip(i)){
            real.setGuess(i, true);
            Ship s = real.getShip(i);
            if(s.isSank()) {
                latestSank = true;
                latestSankSize = s.getLength();
                armada.remove(s);
                if(armada.isEmpty())
                    lost = true;
            }
            else{
                latestSank = false;
                latestSankSize = 0;
            }

            return true;
        }
        else{
            real.setGuess(i, false);
            latestSank = false;
            latestSankSize = 0;
            return false;
        }
    }

    /**
     * Megmondja, hogy a játékosnak adott hosszúságú hjóból hány darabja van még hátra.
     * @param l a hajó hosszúsága.
     * @return a hajók száma.
     */
    public int getShipCount(int l){
        int cnt = 0;
        for(Ship s : armada){
            if(s.getLength() == l)
                cnt++;
        }
        return cnt;
    }

    /**
     * Megadja, hogy adott koordinátán milyen állapotú mező szerepel a játékos hajóit tartalmazó táblájában.
     * @param sor a kérdéses mező sorának a száma.
     * @param oszlop a kérdéses mező oszlopának a száma.
     * @return a mező állapota.
     * @throws FieldNotFoundException ha a koordináták a táblán kívülre mutatnak.
     */
    public State getRealState(int sor, int oszlop) throws FieldNotFoundException {
        return real.getState(real.indexConv(sor, oszlop));
    }

    /**
     * Megadja, hogy adott koordinátán milyen állapotú mező szerepel a játékos tippjeit tartalmazó táblájában.
     * @param sor a kérdéses mező sorának a száma.
     * @param oszlop a kérdéses mező oszlopának a száma.
     * @return a mező állapota.
     * @throws FieldNotFoundException  ha a koordináták a táblán kívülre mutatnak.
     */
    public State getGuessState(int sor, int oszlop) throws FieldNotFoundException {
        return guess.getState(guess.indexConv(sor, oszlop));
    }

    /**
     * Beállítja, hogy a játékos vesztett-e.
     * @param b beállítandó érték.
     */
    public void setLost(boolean b) {
        lost = b;
    }

    /**
     * Beállítja, hogy a játékos nyert-e.
     * @param b beállítandó érték.
     */
    public void setWon(boolean b) {
        won = b;
    }

    /**
     * @return vesztett-e a játékos?
     */
    public boolean gameLost(){
        return lost;
    }

    /**
     * @return nyert-e a játékos?
     */
    public boolean gameWon(){
        return won;
    }

    /**
     * @return a tábla fokszáma
     */
    public int getK(){
        return k;
    }

    /**
     * @return a játékos neve.
     */
    public String getName() {
        return name;
    }

    /**
     * @return elsüllyedt-e az utolsó erre a játékosra leadott lövés által esetlegesen eltalált hajó?
     */
    public boolean isLatestSank(){
        return latestSank;
    }

    /**
     * @return az utolsó erre a játékosra leadott lövés által esetlegesen eltalált hajó hossza.
     */
    public int getLatestSankSize(){
        return latestSankSize;
    }

}