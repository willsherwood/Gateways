package screenStates;

import net.java.games.input.Component;
import sherwood.gameScreen.GameScreen;
import sherwood.inputs.keyboard.control.Control;
import sherwood.inputs.keyboard.control.discrete.DiscreteControlKeyboardInput;
import sherwood.screenStates.ScreenState;

import java.awt.*;
import java.util.BitSet;
import java.util.TreeMap;

import net.java.games.input.*;

public class ControllerSetupState extends ScreenState {
    ControllerEnvironment environment;
    Controller[] controllers;
    int currentController;
    float xAxis, yAxis;
    TreeMap<String, Boolean> buttons;

    @Override
    public void init() {
        environment = ControllerEnvironment.getDefaultEnvironment();
        super.init();
        GameScreen.get().requestKeyInputMechanism(new DiscreteControlKeyboardInput());
        environment.addControllerListener(new ControllerListener() {
            @Override
            public void controllerRemoved(ControllerEvent controllerEvent) {
                getControllers();
            }

            @Override
            public void controllerAdded(ControllerEvent controllerEvent) {
                getControllers();
            }
        });
        getControllers();
    }

    private void getControllers() {
        controllers = environment.getControllers();
        currentController = 0;
    }

    // TODO: add buttons
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawOval(100, 100, 300, 300);
        g.setColor(Color.RED);
        g.drawOval((int)(200 + 75 * xAxis), (int)(200 + 75 * yAxis), 100, 100);
        g.setColor(Color.WHITE);
        g.drawString("Current controller: " + controllers[currentController].getName(), 100, 50);
    }

    @Override
    public void step(BitSet keys) {
        if (keys.get(Control.START.ordinal())) {
            getControllers();
        }
        if (keys.get(Control.UP.ordinal())) {
            currentController--;
            if (currentController < 0)
                currentController = controllers.length - 1;
        }
        if (keys.get(Control.DOWN.ordinal())) {
            currentController++;
            if (currentController >= controllers.length)
                currentController = 0;
        }
        controllers[currentController].poll();
        for (Component component : controllers[currentController].getComponents()) {
            if (component.getIdentifier().equals(Component.Identifier.Axis.X)) {
                xAxis = component.getPollData();
            } else if (component.getIdentifier().equals(Component.Identifier.Axis.Y)) {
                yAxis = component.getPollData();
            }
        }
    }
}
