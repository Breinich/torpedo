package model;

import java.io.Serializable;

import static model.State.*;

/**
 * A torpedó játéktáblájában 1 darab mezőt jelképező osztály.
 */
public class Field implements Serializable {

    /**
     * Megmondja a mező éppeni állapotát.
     */
    private State state = SZABAD;

    /**
     * A mező által tárolt hajó, ha nem tárol, akkor null.
     */
    private Ship ship = null;

    /**
     * Hozzáad egy hajót a mezőhöz, ha lehet -a játék kezdetén-.
     * @param s a hajó amit ide szeretnénk helyezni
     * @return siekrült-e, vagy sem?
     */
    boolean addShip(Ship s){
        if(state.equals(SZABAD)) {
            ship = s;
            state = HAJO;
            return true;
        }
        else
            return false;
    }

    /**
     * Egy lövés leadása / tippelés erre a mezőre.
     * @return talált, vagy sem?
     */
    boolean guessShip(){
        switch (state){
            case HAJO -> {
                ship.talalat();
                state = TALALT;
                return true;
            }

            case TALALT -> {
                return true;
            }

            default -> {
                return false;
            }
        }
    }

    /**
     * A céltáblán a lövés eredményének beállítása.
     * @param talalt talált-e a lövés
     */
    public void setGuessState(boolean talalt){
        if(talalt)
            state = TALALT;
        else
            state = NEMTALALT;
    }

    /**
     *
     * @return tárolt hajó, vagy null
     */
    Ship getShip(){
        return ship;
    }

    /**
     * Állapot beállítása FOGLALT-ra, mert a szomszédjában van hajó, ezzel kiküszübölvén a hajók szomszédos elhelyezését.
     */
    void setFoglalt(){
        state = FOGLALT;
    }

    /**
     *
     * @return a mező állapotával.
     */
    public State getState() {
        return state;
    }
}
