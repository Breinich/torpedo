package torpedo.controller;

import torpedo.exceptions.FieldNotFoundException;
import torpedo.model.Direction;
import torpedo.model.Player;
import torpedo.model.Ship;

import java.io.Serializable;
import java.util.ArrayList;

import static torpedo.model.Direction.*;

/**
 * A játékos ellenfelét szimuláló botoknak az ősosztálya.
 */
public abstract class BotController  implements Serializable {

    /**
     * Egy Player referencia, amit irányít a kontroller.
     */
    protected Player bot;

    /**
     * A tábla foka.
     */
    protected int k;

    /**
     * A játékos hajói, amiket el kell helyeznie a kontrollernek.
     */
    protected ArrayList<Ship> ships;

    /**
     * Konstruktor, az adott bot-hoz tartozó Player objektumhoz hozzáadja a kapott hajókat random koordinátákra és random irányban.
     * Persze az összes hajót elhelyezi és csak legális helyekre.
     */
    protected void initPlayer(){
        for(Ship s : ships){

            while (true) {
                try {
                    Direction d = ranDir();
                    int sor = ranCoord();
                    int oszlop = ranCoord();

                    if(d == LEFT){
                        d = RIGHT;
                        oszlop -= s.getLength()-1;
                    }
                    else if(d == UP){
                        d = DOWN;
                        sor -= s.getLength()-1;
                    }

                    if (bot.placeShip(s, sor, oszlop, d))
                        break;
                } catch (FieldNotFoundException ignored) {}
            }
        }
    }

    /**
     * Random irányt generál.
     * @return random irány (DOWN RIGHT, UP, vagy LEFT).
     */
    protected Direction ranDir(){
        int i = (int) (Math.random()*4);
        return switch (i) {
            case 1 -> RIGHT;
            case 2 -> LEFT;
            case 3 -> UP;
            default -> DOWN;
        };
    }

    /**
     * Random koordinátát generál a tábla sikjában.
     * @return random koordináta a táblán elérhető intervallumban
     */
    protected int ranCoord(){
        return (int) (Math.random()*k);
    }

    /**
     * Tipp leadása az élő játékos hajóira.
     */
    public abstract void setGuess();

}
