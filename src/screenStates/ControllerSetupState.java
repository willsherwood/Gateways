package screenStates;

import controller.AxisMap;
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
        g.drawString("Current controller: " + controllers[currentController].getName(), 50, GameScreen.HEIGHT - 10);
        int cx = 10;
        int cy = 10;
        for (Component component : controllers[currentController].getComponents()) {
            g.drawString(component.getName(), cx, cy);
            // TODO: Hat switch POV
            if (component.isAnalog()) {
                g.drawRect(cx, cy + 5, 100, 20);
                g.drawRect(cx + (int) (component.getPollData() * 50 + 50) - 15, cy, 30, 30);
            } else {
                if (component.getIdentifier().equals(Component.Identifier.Axis.POV)) {
                    float dir = component.getPollData() - 0.125f / 2; // fudge
                    int i = 0;
                    if (dir < Component.POV.CENTER) {
                        i = 1 + 1 * 3;
                    } else if (dir < Component.POV.UP_LEFT) {
                        i = 0 + 0 * 3;
                    } else if (dir < Component.POV.UP) {
                        i = 1 + 0 * 3;
                    } else if (dir < Component.POV.UP_RIGHT) {
                        i = 2 + 0 * 3;
                    } else if (dir < Component.POV.RIGHT) {
                        i = 2 + 1 * 3;
                    } else if (dir < Component.POV.DOWN_RIGHT) {
                        i = 2 + 2 * 3;
                    } else if (dir < Component.POV.DOWN) {
                        i = 1 + 2 * 3;
                    } else if (dir < Component.POV.DOWN_LEFT) {
                        i = 0 + 2 * 3;
                    } else if (dir < Component.POV.LEFT) {
                        i = 0 + 1 * 3;
                    }
                    for (int y = 0; y < 3; y++) {
                        for (int x = 0; x < 3; x++) {
                            if (i == x + y * 3)
                                g.fillRect(cx + 35 + x * 10, cy + y * 10, 10, 10);
                            g.drawRect(cx + 35 + x * 10, cy + y * 10, 10, 10);
                        }
                    }
                } else {
                    if (component.getPollData() < 0.5) {
                        g.drawOval(cx + 35, cy, 30, 30);
                    } else {
                        g.fillOval(cx + 35, cy, 30, 30);
                    }
                }
            }
            cy += 40;
            if (cy > 600) {
                cy = 10;
                cx += 120;
            }
        }
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
        AxisMap.controller = controllers[currentController];
    }
}
