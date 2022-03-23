package model;

import java.io.Serializable;

/**
 * Hajó osztály
 */
public class Ship  implements Serializable {

    /**
     * A hajó életeit tárolja.
     */
    private int life;

    /**
     * A hajó hosszát tárolja.
     */
    private final int length;

    /**
     * Azt tárolja, hogy a hajó el van-e süllyedve.
     */
    private boolean sank = false;

    /**
     * Konstruktor, ami beállítja a hajó hosszát.
     * @param l a hajó hossza.
     */
    public Ship(int l)
    {
        life = l;
        length = l;
    }

    /**
     * Egy találat érkezése a hajóra csökkenti eggyel a hajó élettartartamát.
     * Ha elsüllyedt, akkor beállítja az enneg megfelelő flag-jét.
     */
    void talalat(){
        life -= 1;
        if(life == 0)
            sank = true;
    }

    /**
     * @return elsüllyedt-e a hajó?
     */
    public boolean isSank(){
        return sank;
    }


    /**
     * @return a hajó hossza.
     */
    public int getLength() {
        return length;
    }
}