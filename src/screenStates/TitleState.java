package screenStates;

import sherwood.gameScreen.GameScreen;
import sherwood.inputs.keyboard.control.Control;
import sherwood.inputs.keyboard.control.discrete.DiscreteControlKeyboardInput;
import sherwood.screenStates.NullState;
import sherwood.screenStates.ScreenState;
import util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TitleState extends ScreenState {

    private static final String TITLE = "Gateways";
    private int selected;
    private List<Pair<String, ScreenState>> selections;
    private int titleWidth = -1;

    private static Font[] fonts = {
            new Font("Garamond", Font.BOLD, 80),
            new Font("Garamond", Font.PLAIN, 40)
    };

    @SuppressWarnings("unchecked")
    public TitleState () {
        super();
        selections = new ArrayList<>();
        selections.add(new Pair<>("Start", new NullState()));
        selections.add(new Pair<>("Controls", new NullState()));
        selections.add(new Pair<>("Options", new NullState()));
    }

    @Override
    public void draw (Graphics2D g) {
        drawTitle(g);
        drawOptions(g);
    }

    private void drawOptions (Graphics2D g) {
        Font f = g.getFont();
        g.setFont(fonts[1]);
        for (int i = 0; i < selections.size(); i++) {
            if (selected == i)
                g.setColor(Color.YELLOW);
            else
                g.setColor(Color.LIGHT_GRAY);
            g.drawString(selections.get(i).getKey(), GameScreen.WIDTH / 2 - g.getFontMetrics(fonts[1]).stringWidth(selections.get(i).getKey()) / 2, GameScreen.HEIGHT - 20 - 60 * (selections.size() - i));
        }
        g.setFont(f);
    }

    private void drawTitle (Graphics2D g) {
        if (titleWidth == -1)
            titleWidth = g.getFontMetrics(fonts[0]).stringWidth(TITLE);
        Font previous = g.getFont();
        g.setFont(fonts[0]);
        g.drawString(TITLE, GameScreen.WIDTH / 2 - titleWidth / 2, 100);
        g.setFont(previous);
    }

    @Override
    public void step (BitSet bitSet) {
        if (bitSet.get(Control.getCondensed(Control.DOWN)))
            selected = (selected + 1) % selections.size();
        else if (bitSet.get(Control.getCondensed(Control.UP)))
            selected = (selected + selections.size() - 1) % selections.size();
        else if (bitSet.get(Control.getCondensed(Control.START)) || bitSet.get(Control.getCondensed(Control.A))) {
            GameScreen.get().requestScreenStateAndInit(selections.get(selected).getValue());
        }

    }

    @Override
    public void init () {
        super.init();
        GameScreen.get().requestKeyInputMechanism(new DiscreteControlKeyboardInput());
    }
}
