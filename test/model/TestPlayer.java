package model;

import exceptions.FieldNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * A Player osztályt tesztelő osztály.
 */
@RunWith(Parameterized.class)
public class TestPlayer {

    /**
     * Egy Player objektum, amin majd futtatjuk a teszteseteket.
     */
    private Player p1;

    /**
     * Sor paraméter a paraméteres teszteléséhez a táblák kezelésének.
     */
    private final int sor;

    /**
     * Oszlop paraméter a paraméteres teszteléséhez a táblák kezelésének.
     */
    private final int oszlop;

    /**
     * Konstruktor, ami beállítja a paramétereket a tesztekhez.
     * @param s sor
     * @param o oszlop
     */
    public TestPlayer(int s, int o){
        sor = s;
        oszlop = o;
    }

    /**
     * 2 sarok koordináta és egy jobb szél menti.
     * A program megalkotása közben ezekkel a célzott tesztekkel javítottam ki pár bug-ot pár algoritmusban.
     * @return a kipróbálandó paraméterek listája.
     */
    @Parameters
    public static List<Object[]> parameters(){
        List<Object[]> params = new ArrayList<>();
        params.add(new Object[] {0, 0});
        params.add(new Object[] {9, 9});
        params.add(new Object[] {6, 9});

        return  params;
    }

    /**
     * Minden teszt előtt inicializálja a Player objektumot.
     */
    @Before
    public void setUp(){
        p1 = new Player(10, "Test Elek");
    }

    /**
     * A hajók elhelyezését tesztelő függvények gyűjteménye.
     * @throws FieldNotFoundException ha olyan helyre akarnánk hajót rakni, ahonnan kilógna a tábláról, akkor dobódik ez a kivétel.
     */
    @Test
    public void testPlaceShip() throws FieldNotFoundException {
        Ship s1 = new Ship(1);
        Ship s2 = new Ship(2);
        Ship s3 = new Ship(3);
        Ship s4 = new Ship(4);

        Assert.assertTrue(p1.placeShip(s1, 1, 1, Direction.DOWN));
        Assert.assertFalse(p1.placeShip(s2, 0, 2, Direction.DOWN));
        Assert.assertTrue(p1.placeShip(s2, 7, 8, Direction.RIGHT));
        Assert.assertFalse(p1.placeShip(s4, 7, 5, Direction.RIGHT));
        Assert.assertTrue(p1.placeShip(s3, 4, 4, Direction.DOWN));
        Assert.assertTrue(p1.placeShip(s4, 8, 2, Direction.RIGHT));

        Assert.assertEquals(1, p1.getShipCount(3));
    }

    /**
     * Hajóra tippeléseket és a találatok detektálását ellenőrző függvények gyűjteménye.
     * @throws FieldNotFoundException ha az egyik koordináta kívül esne a táblán.
     */
    @Test
    public void testGuessAndAttacked() throws FieldNotFoundException {
        Ship s1 = new Ship(2);
        Ship s2 = new Ship(1);

        p1.placeShip(s1, 3, 3, Direction.RIGHT);
        Assert.assertTrue(p1.guess(3,3, p1));
        Assert.assertEquals(State.TALALT, p1.getGuessState(3,3));
        Assert.assertFalse(p1.guess(4,3, p1));
        Assert.assertEquals(State.NEMTALALT, p1.getGuessState(4,3));
        Assert.assertTrue(p1.guess(3, 4, p1));
        Assert.assertTrue(p1.isLatestSank());
        Assert.assertEquals(2, p1.getLatestSankSize());
        Assert.assertEquals(State.NEMTALALT, p1.getGuessState(2, 2));
        Assert.assertFalse(p1.guess(2,3, p1));
        Assert.assertEquals(State.NEMTALALT, p1.getGuessState(2, 3));
        Assert.assertFalse(p1.guess(4,3, p1));
        Assert.assertEquals(State.NEMTALALT, p1.getGuessState(4, 3));
        Assert.assertFalse(p1.guess(4,4, p1));
        Assert.assertEquals(State.NEMTALALT, p1.getGuessState(4, 4));
        Assert.assertFalse(p1.guess(7,7, p1));
        Assert.assertEquals(State.NEMTALALT, p1.getGuessState(7, 7));

        Assert.assertTrue(p1.placeShip(s2, sor, oszlop, Direction.RIGHT));

        int k = 10;
        if(oszlop + 1 < k)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor,oszlop+1));
        if(oszlop - 1 > -1)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor,oszlop-1));
        if(sor + 1 < k && oszlop + 1 < k)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor+1,oszlop+1));
        if(sor + 1 < k)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor+1,oszlop));
        if(sor + 1 < k && oszlop - 1 > -1)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor+1,oszlop-1));
        if(sor - 1 > -1)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor-1,oszlop));
        if(sor - 1 > -1 && oszlop + 1 < k)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor-1,oszlop+1));
        if(sor - 1 > -1 && oszlop - 1 > -1)
            Assert.assertEquals(State.FOGLALT, p1.getRealState(sor-1,oszlop-1));

        Assert.assertTrue(p1.guess(sor, oszlop, p1));

        if(oszlop + 1 < k)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor,oszlop+1));
        if(oszlop - 1 > -1)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor,oszlop-1));
        if(sor + 1 < k && oszlop + 1 < k)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor+1,oszlop+1));
        if(sor + 1 < k)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor+1,oszlop));
        if(sor + 1 < k && oszlop - 1 > -1)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor+1,oszlop-1));
        if(sor - 1 > -1)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor-1,oszlop));
        if(sor - 1 > -1 && oszlop + 1 < k)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor-1,oszlop+1));
        if(sor - 1 > -1 && oszlop - 1 > -1)
            Assert.assertEquals(State.NEMTALALT, p1.getGuessState(sor-1,oszlop-1));

        Assert.assertTrue(p1.gameLost());
        Assert.assertTrue(p1.gameWon());

        Assert.assertEquals("Test Elek", p1.getName());
    }

    /**
     * Egyéb tesztek, hogy elérhessük a Player osztályban is a 100%-os függvénylefedettséget
     */
    @Test
    public void testOther(){

        Assert.assertEquals(10, p1.getK());
        p1.setLost(true);
        p1.setWon(false);
        Assert.assertTrue(p1.gameLost());
        Assert.assertFalse(p1.gameWon());

    }
}
