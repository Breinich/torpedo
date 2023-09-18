package torpedo.controller;

import torpedo.exceptions.FieldNotFoundException;
import torpedo.model.Player;
import torpedo.model.Ship;

import java.util.ArrayList;

/**
 * 'Butább' botot vezérlő osztály.
 */
public class DumBotConroller extends BotController{

    /**
     * Konstruktor.
     * @param s a hajók referenciája kollekcióban tárolva
     * @param n a tábla foka - (n x n) mezője van.
     * @param b a bothoz tartozo Player objektum referenciája, amit irányítani fog.
     */
    public DumBotConroller(ArrayList<Ship> s, int n, Player b){
        bot = b;
        k = n;
        ships = (ArrayList<Ship>) s.clone();

        initPlayer();
    }

    /**
     * Lead egy tippet az ellenfél hajóira.
     * Random választ koordinátát a tipptábláján, így ugyanarra a mezőre akár többször is tippelhet.
     */
    @Override
    public void setGuess() {
        try {
            bot.guess(ranCoord(), ranCoord(), GameController.pHuman);
        } catch (FieldNotFoundException e) {
            //do nothing, must not throw this exception
        }
    }
}
