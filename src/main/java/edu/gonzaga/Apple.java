package edu.gonzaga;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Apple extends Food {
    public Apple (int x, int y) {
        super (x,y);
        texturePath = "apple.png";
    }

    @Override
    public void applyAffect(Snake snake) {
        // regular apple does not apply any special effects
    }

    @Override
    public Image getImage() {
        return new ImageIcon(getClass().getResource("/resources/food/apple.png")).getImage();
    }
}