package edu.gonzaga;

import javax.swing.ImageIcon;
import java.awt.Image;

public class StarFruit extends Food {
    public StarFruit(int x, int y) {
        super(x,y);
        texturePath = "star.png";
    }

    @Override
    public void applyAffect(Snake snake) {
        // sets the snakes invicibility timer to 10 sec (we can change)
        snake.setInvincible(snake.getInvincible() + 12);
    }

    @Override
    public Image getImage() {
        return new ImageIcon(getClass().getResource("/resources/food/star.png")).getImage();
    }
}