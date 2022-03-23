package model;

import exceptions.FieldNotFoundException;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A játéktáblát tároló, kezelő osztály.
 */
public class Table  implements Serializable {

    /**
     * A játéktáblát tárolja, benne a mezők 2 szám alapján indexelődnek.
     * Az első szám a sor indexe a táblán (0-tól kezdve (k-1)-ig), a második pedig az oszlop indexe a táblán (0-tól kezdve (k-1)-ig).
     */
    private final HashMap<Index, Field> map;

    /**
     * Kontsruktor, ami létrehoz egy adott fokú táblát.
     * @param k a tábla foka, (k x k) nagyságú a tábla.
     */
    public Table(int k){
        map = new HashMap<>();

        for(int i = 0; i < k; i++){

            for (int j = 0; j < k; j++){
                Index index = new Index(i, j);

                map.put(index, new Field());
            }
        }
    }

    /**
     * Egy hajó elhelyezése egy adott mezőn, a játék kezdetén fontos.
     * @param ship hajó referenciája.
     * @param i a mező indexe a táblában.
     * @return sikerült-e lerakni a hajót?
     */
    public boolean placeShip(Ship ship, Index i){

        return map.get(i).addShip(ship);
    }

    /**
     * Egy lövés leadása / tippelés erre a mezőre.
     * @param i a mező indexe a táblában.
     * @return talált, vagy sem.
     */
    public boolean guessShip(Index i){

        return map.get(i).guessShip();
    }

    /**
     * A céltáblán a lövés eredményének beállítása.
     * @param i a mező indexe.
     * @param talalt talált-e a lövés.
     */
    public void setGuess(Index i, boolean talalt){

        map.get(i).setGuessState(talalt);
    }

    /**
     * Index konvertáló sor - oszlop kombinációból.
     * @param sor a táblán lévő sor száma.
     * @param oszlop a táblán lévő oszlop száma.
     * @return egy kulcs a tábla hashmapjéhez.
     * @throws FieldNotFoundException ha a kulcshoz nem tartozik mező, tehát ha rossz értékeket kapott paraméterül a függvény / a táblán kívülre mutatnak a koordináták.
     */
    public Index indexConv(int sor, int oszlop) throws FieldNotFoundException{
        Index i = new Index(sor, oszlop);
        if(!map.containsKey(i)) {
            throw new FieldNotFoundException();
        }
        return i;
    }

    /**
     * Egy adott mezőről az ott lehetségesen tartózkodó hajó referenciájának elkérése.
     * @param i a mező indexe.
     * @return hajó referenciája.
     */
    Ship getShip(Index i){
        return map.get(i).getShip();
    }

    /**
     * Beállítja egy adott mező státuszát FOGLALT-ra -hogy ne lehessen oda hajót rakni-.
     * @param i mező indexe.
     */
    void setFoglalt(Index i){
        map.get(i).setFoglalt();
    }

    /**
     * Megadja egy adott mező állapotát.
     * @param i a mező indexe.
     * @return a mező állapota.
     */
    public State getState(Index i){
        return map.get(i).getState();
    }
}
