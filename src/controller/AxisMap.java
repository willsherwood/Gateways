package controller;

import net.java.games.input.Controller;
import sherwood.iohandlers.ConfigHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by s506571 on 2/10/2015.
 */
public class AxisMap {

    static Map<String, Axis> axisMap;

    static {
        axisMap = new TreeMap<>();
        for (Axis c : Axis.values())
            axisMap.put(ConfigHandler.load(c.toString()), c);
    }

    public static Axis getAxis(String identifier) {
        return axisMap.get(identifier);
    }

}
