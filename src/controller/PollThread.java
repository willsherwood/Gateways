package controller;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 * Created by s506571 on 2/10/2015.
 */
public class PollThread extends Thread {
    ControllerEnvironment environment;

    @Override
    public void run() {
        // basically just poll through AxisMap.controllers
        // and set the key controller.port|axis.ID to the value
        while (true) {
            for (Controller controller : environment.getControllers()) {
                for (Component component : controller.getComponents()) {
                    String id = controller.getPortNumber() + "|" + component.getIdentifier();
                    Axis axis = AxisMap.getAxis(id);
                    // do something
                }
            }
        }
    }
}
