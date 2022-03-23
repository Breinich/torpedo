package controller;

import exceptions.FieldNotFoundException;
import model.Player;
import model.Ship;
import model.State;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Okosabbik Botot vezérlő osztály.
 */
public class SmartBotController extends BotController{

    /**
     * Konstruktor, beállítja az ősosztály tagváltozóit.
     * @param s a hajókat tartalmazó kollekció
     * @param n a tábla fokszáma
     * @param b a botot szimbolizáló játékos referenciája
     */
    public SmartBotController(ArrayList<Ship> s, int n, Player b){
        bot = b;
        k = n;
        ships = (ArrayList<Ship>) s.clone();

        initPlayer();
    }

    /**
     * Tippelés leadása 'okosabb' módon.
     * Ha van már egy el nem süllyesztett találata korábbról, akkor annak a szomszédságába fog igyekezni tippelni.
     * Eyébként olyan mezőre fog random tippelni, amelyikre korábban még nem tippelt
     */
    @Override
    public void setGuess() {

        for(int i = 0; i < k; i++){
            for(int j = 0; j < k; j++){
                try {
                    if(bot.getGuessState(i, j) == State.TALALT){
                        int [] coord = searchNextFree(i, j);
                        if(coord[0] != -1){
                            bot.guess(coord[0], coord[1], GameController.pHuman);
                            return;
                        }
                    }
                } catch (FieldNotFoundException e) {
                    //do nothing
                }
            }
        }
        int i, j;
        while(true){
            i = ranCoord();
            j = ranCoord();
            try {
                if (bot.getGuessState(i, j) == State.SZABAD)
                    break;
            } catch (FieldNotFoundException e) {
                //do nothing
            }
        }
        try {
            bot.guess(i, j, GameController.pHuman);
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * A tippeléshez segédfüggvény.
     * Megkeresi egy adott koordinátájú találathoz, hogy van-e még nem tippelt, az egybefüggő szomszédos találatokkkal szomszédos mező, ami potenciálisan találat lehet.
     * @param sor a korábban már eltalált hajó egyik mezejének a sora
     * @param oszlop a korábban már eltalált hajó egyik mezejének az oszlopa
     * @return egy számpárost, ha sikerült potenciális mezőt találnai, akkor annak a koordinátái, ha nem, akkor {-1, -1}-et
     */
    private int[] searchNextFree(int sor, int oszlop){
        int s = 1;
        int o = 1;
        int l = 1;
        int[] tmp = null;

        /*
        függőlegesen keres, először le, aztán fel
        ha eléri a tábla szélét vagy egy nemtalált mezőt, akkor a másik irányba kezd el haladni
         */
        int i;
        if(sor == k-1){
            i = sor-1;
            s = -1;
        }
        else
            i = sor + 1;
        for(; i > -1; i += s){
            State state = null;
            try {
                state = bot.getGuessState(i, oszlop);
                boolean br = false;
                switch(state){
                    case NEMTALALT -> {if(s == 1){ s= -1; i = sor;}else br = true;}
                    case TALALT -> l++;
                    case SZABAD -> {tmp = new int[] {i, oszlop}; if(l>1) return tmp; else if(s == 1){ s= -1; i = sor;} else br = true;}
                }
                if(br)
                    break;
            } catch (FieldNotFoundException e) {
                //do nothing
            }
            if(i == k-1){
                i = sor -1;
                s = -1;
            }
        }

        /*
        Vízszintes keresés, először jobbra, aztán balra
        Ha eléri a tábla szélét, vagy egy nemtalált mezőt, akkor megfordul a másik irányba
         */
        l= 1;
        int j;
        if(oszlop == k-1){
            j = oszlop -1;
            o = -1;
        }
        else
            j = oszlop +1;
        for(;j > -1; j += o){
            State state = null;
            try {
                state = bot.getGuessState(sor, j);
                boolean br = false;
                switch(state){
                    case NEMTALALT -> {if(o == 1){ o= -1; j = oszlop;}else br = true;}
                    case TALALT -> l++;
                    case SZABAD -> {tmp = new int[] {sor, j}; if(l>1) return tmp; else if(o == 1){o= -1; j = oszlop;} else br = true;}
                }
                if(br)
                    break;
            } catch (FieldNotFoundException e) {
                //do nothing
            }
            if(j == k-1){
                j = sor -1;
                o = -1;
            }
        }

        return Objects.requireNonNullElseGet(tmp, () -> new int[]{-1, -1});
    }
}
