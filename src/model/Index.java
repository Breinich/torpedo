package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A táblákban ezzel azonosíthatók a mezők,
 * ezzel könnyebben kezelhetők, mint egy tömbbel, vagy valamilyen kollekcióval.
 */
public class Index  implements Serializable {

    /**
     * A mezőt tartalmazó sor.
     */
    public final int sor;

    /**
     * A mezőt tartalamazó oszlop.
     */
    public final int oszlop;

    /**
     * Konstruktor, beállítja a sor és az oszlop értékét.
     * @param s sor száma (0-tól (k-1)-ig)
     * @param o oszlop száma (0-tól (k-1)-ig)
     */
    Index(int s, int o){
        sor = s;
        oszlop = o;
    }

    /**
     * Az equals füüggvény felüldefiniálása, hogy 2 index egyenlő lehessen,
     * ha a sor és oszlop számuk megegyezik.
     * @param o a másik Index objektum
     * @return egyenlőek-e?
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index)) return false;
        Index index = (Index) o;
        return sor == index.sor && oszlop == index.oszlop;
    }

    /**
     * @return objektum hash-kódja a változói alapján.
     */
    @Override
    public int hashCode() {
        return Objects.hash(sor, oszlop);
    }
}
