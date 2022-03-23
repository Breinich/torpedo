package model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * A Field osztályt tesztelő osztály.
 */
public class TestField {

    /**
     * Egy Field objektum, amin fogunk kíséreletezni.
     */
    private Field field;

    /**
     * A Field objektum létrehozása minden teszt előtt.
     */
    @Before
    public void setUp(){
        field = new Field();
    }

    /**
     * A hajókkal kapcsolatos műveletek tesztelése.
     */
    @Test
    public void testShip(){
        Assert.assertFalse(field.guessShip());

        Ship s = new Ship(4);
        if(field.addShip(s)) {
            Assert.assertEquals(s, field.getShip());
            Assert.assertEquals(State.HAJO, field.getState());
            Assert.assertTrue(field.guessShip());
            Assert.assertEquals(State.TALALT, field.getState());
            Assert.assertFalse(field.addShip(s));
            Assert.assertTrue(field.guessShip());
        }
    }

    /**
     * A mezők állapotainak, annak kezelésének a tesztelése.
     */
    @Test
    public void testStates(){
        Assert.assertEquals(State.SZABAD, field.getState());
        field.setFoglalt();
        Assert.assertEquals(State.FOGLALT, field.getState());
        field.setGuessState(true);
        Assert.assertEquals(State.TALALT, field.getState());
        field.setGuessState(false);
        Assert.assertEquals(State.NEMTALALT, field.getState());
    }
}
