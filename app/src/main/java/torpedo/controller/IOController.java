package torpedo.controller;

import torpedo.model.Player;
import torpedo.model.Ship;
import torpedo.view.GameView;

import java.io.*;
import java.util.ArrayList;

/**
 * A szerializálással való játékállás mentést és betöltést segítő oszály.
 */
public class IOController {

    /**
     * Az élő játékos Player objektumát tároló fájl neve.
     */
    private static final String pathHum = "human.ser";

    /**
     * Az észámítógép által vezérelt Player objektumot tároló fájl neve.
     */
    private static final String pathBot = "bot.ser";

    /**
     * A számítógép játékosát irányító objektumot tároló fájl neve.
     */
    private static final String pathBotController = "botCtrl.ser";

    /**
     * A játék hajóit tároló kollekciót tároló fájl neve.
     */
    private static final String pathShips = "ships.ser";

    /**
     * Elmenti az adott játék 2 játékos objektumát, az eredeti hajókat tartalmazó kollekciót és a botot vezérlő kontrollert.
     * @throws IOException ha nem sikerült a kiírás valamelyik fázisa.
     */
    public static void save() throws IOException {

        FileOutputStream foH = new FileOutputStream(pathHum);
        FileOutputStream foB = new FileOutputStream(pathBot);
        FileOutputStream foBC = new FileOutputStream(pathBotController);
        FileOutputStream foS = new FileOutputStream(pathShips);

        ObjectOutputStream outH = new ObjectOutputStream(foH);
        ObjectOutputStream outB = new ObjectOutputStream(foB);
        ObjectOutputStream outBC = new ObjectOutputStream(foBC);
        ObjectOutputStream outS = new ObjectOutputStream(foS);

        outH.writeObject(GameController.pHuman);
        outB.writeObject(GameController.pBot);
        outBC.writeObject(GameController.bot);
        outS.writeObject(GameController.ships);

        outH.close();
        outB.close();
        outBC.close();
        outS.close();

        foH.close();
        foB.close();
        foBC.close();
        foS.close();

    }

    /**
     * Beolvassa korábban szerializáltan elmentett játék adatait és visszaállítja azt, létrehozza a megfelelő objektumokat.
     * @throws IOException ha a beolvasás műveleteinek egyik fázisa nem sikerült
     * @throws ClassNotFoundException ha a beolvasás során nem sikerült cast-olnia a beolvasott Object objektumot a megfelelő osztályúra
     */
    public static void load() throws IOException, ClassNotFoundException {

        FileInputStream fiH = new FileInputStream(pathHum);
        FileInputStream fiB = new FileInputStream(pathBot);
        FileInputStream fiBC = new FileInputStream(pathBotController);
        FileInputStream fiS = new FileInputStream(pathShips);

        ObjectInputStream inH = new ObjectInputStream(fiH);
        ObjectInputStream inB = new ObjectInputStream(fiB);
        ObjectInputStream inBC = new ObjectInputStream(fiBC);
        ObjectInputStream inS = new ObjectInputStream(fiS);

        Player human = (Player) inH.readObject();
        Player bot = (Player) inB.readObject();
        BotController bc = (BotController) inBC.readObject();
        ArrayList<Ship> s= (ArrayList<Ship>) inS.readObject();

        inH.close();
        inB.close();
        inBC.close();
        inS.close();

        fiH.close();
        fiB.close();
        fiBC.close();
        fiS.close();

        new GameController(human.getK());
        GameController.pBot = bot;
        GameController.pHuman = human;
        GameController.bot = bc;
        GameController.ships = s;
        GameController.setStarted(true);
        GameController.setReset(false);
        GameController.setSaved(true);
        GUIController.game = new GameView();

    }
}
