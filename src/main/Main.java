package main;

import screenStates.TitleState;
import screenStates.transitions.FadeInState;
import sherwood.gameScreen.GameScreen;

import javax.swing.*;

public class Main {

    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->GameScreen.start(new FadeInState(new TitleState(), 30), "Gateways"));
    }
}
