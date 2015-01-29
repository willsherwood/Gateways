package screenStates.transitions;

import main.Main;
import sherwood.gameScreen.GameScreen;
import sherwood.screenStates.ScreenState;

import java.awt.*;
import java.util.BitSet;

public class FadeInState extends ScreenState {

    private ScreenState state;
    private int frames;
    private int frame;

    public FadeInState(ScreenState state, int frames) {
        this.state = state;
        this.frames = frames;
    }

    @Override
    public void draw(Graphics2D g) {
        state.draw(g);
        g.setColor(new Color(0, 0, 0, 255-(int) (255. / frames * frame)));
        g.fillRect(0, 0, GameScreen.WIDTH, GameScreen.HEIGHT);
        if (Main.DEBUG) {
            g.setColor(Color.WHITE);
            g.drawString("Fade State", 100, 100);
        }
    }

    @Override
    public void step(BitSet keys) {
        if (frame == frames) {
            GameScreen.get().requestScreenState(state);
            return;
        }
        state.step(keys);
        frame++;
    }

    @Override
    public void init() {
        super.init();
        state.init();
    }
}
