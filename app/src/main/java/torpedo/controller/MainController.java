package torpedo.controller;

/**
 * A játék belépési pontját tartalmazó osztály,
 * létrehoz egy GUIController-t.
 */
public class MainController {

    /**
     * A játék kinézetét vezérli.
     */
    static GUIController guiController;

    /**
     * A program belépési pontja, létrehoz egy GUIControllert.
     * @param args parancssori programargumentumok.
     */
    public static void main(String[] args){
        System.out.println("Hello World!");
        guiController = new GUIController();
    }

}
