package edu.gonzaga;

import javax.swing.ImageIcon;
import java.awt.Image;

public class GoldenApple extends Food {
    public GoldenApple(int x, int y) {
        super(x,y);
        texturePath = "golden_apple.png";
    }

    @Override
    public void applyAffect(Snake snake) {
        // grows snake by 4 more segments than usual
        for (int i = 0; i < 4; i++) {
            snake.grow();
        }
    }

    @Override
    public Image getImage() {
        return new ImageIcon(getClass().getResource("/resources/food/golden.png")).getImage();
    }
}