package torpedo.model;

/**
 * Egy mező lehetséges állapotai.
 *
 */
public enum State {

    /**
     * A mező üres.
     */
    SZABAD,

    /**
     * A mezőn hajó található
     */
    HAJO,

    /**
     * A mező foglalt, nem lehet rá hajót helyezni, mivel a szomszédságában már van hajó.
     */
    FOGLALT,

    /**
     * A mezőn lévő hajót eltalálta már az ellenfél.
     */
    TALALT,

    /**
     * Erről az üres mezőről tudni lehet, hogy nincs rajta hajó.
     */
    NEMTALALT
}
