import screenStates.TitleState;
import sherwood.gameScreen.GameScreen;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->GameScreen.start(new TitleState(), "Gateways"));
    }
}
