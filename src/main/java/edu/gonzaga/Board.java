package edu.gonzaga;

import java.util.ArrayList;
import java.util.Map;

public class Board {
    private int width = 17;
    private int height = 15;
    private Snake snake;
    private ArrayList<Obstacle> obstacles;
    private Food food;
    private int score = 0;

    //easy = 0, medium = 2, hard = 5
    public Board(int numObstacles) {
        snake = new Snake();
        // randomize the initial food type
        int foodType = (int) (Math.random() * 3);
        switch (foodType) {
            case 0:
            food = new Apple(12, 7);
            break;
            case 1:
            food = new GoldenApple(12, 7);
            break;
            case 2:
            food = new StarFruit(12, 7);
            break;
        }
        obstacles = new ArrayList<>();
        for (int i = 0; i < numObstacles; i++) {
            obstacles.add(new Obstacle((int)(Math.random() * width), (int)(Math.random() * height)));
        }
    }

    //main game loop, called on a time interval
    //returns false if the snake dies
    public boolean tick() {
        snake.move();
        switch (detectCollision(snake)) {
            case "food":
            score++;
            snake.grow();
            food.applyAffect(snake);
            makeNewFood();
            break;
            case "edge":
            case "obstacle":
            case "snake":
            if (snake.getInvincible() == 0) {
                return false;
            } else {
                keepInBounds();
            }
        }
        System.out.println(this);
        return true;
    }
    
    //called from keyboard listener
    public void turnSnake(char dir) {
        snake.turn(dir);
    }

    public int getScore() {
        return score;
    } 

    public Snake getSnake() {
        return snake;
    }

    //takes in a board element and returns if it collides with anything, if not returns empty string
    //used for snake dying and setting new food pos
    public String detectCollision(BoardElement object) {
        if (object.getX() < 0 || object.getY() < 0 || object.getX() >= width || object.getY() >= height) {
            return "edge";
        }
        for (Obstacle o : obstacles) {
            if (object.getX() == o.getX() && object.getY() == o.getY()) {
                return "obstacle";
            }
        }
        for (SnakeSegment s : snake.getSegments()) {
            if (object.getX() == s.getX() && object.getY() == s.getY()) {
                return "snake";
            }
        }
        if (object.getX() == food.getX() && object.getY() == food.getY()) {
            return "food";
        }
        return "";
    }

    // randomizes the food type
    private void makeNewFood() {
        do {
            int foodType = (int) (Math.random() * 3);
            switch (foodType) {
                case 0:
                food = new Apple((int) (Math.random() * width), (int) (Math.random() * height));
                break;
                case 1:
                food = new GoldenApple((int) (Math.random() * width), (int) (Math.random() * height));
                break;
                case 2:
                food = new StarFruit((int) (Math.random() * width), (int) (Math.random() * height));
                break;
            }
        } while (detectCollision(food).equals("obstacle") || detectCollision(food).equals("snake"));
    }
    
    private void keepInBounds() {
        snake.setX((snake.getX() + width) % width);
        snake.setY((snake.getY() + height) % height);
    }

    //print board to console for testing
    public String toString() {
        StringBuilder gameStr = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gameStr.append("`");
            }
            gameStr.append("\n");
        }
        gameStr.setCharAt(food.getY() * (width + 1) + food.getX(), 'F');
        for (Obstacle o : obstacles) {
            int index = o.getY() * (width + 1) + o.getX();
            gameStr.setCharAt(index, 'O');
        }
        for (SnakeSegment s : snake.getSegments()) {
            int index = s.getY() * (width + 1) + s.getX();
            gameStr.setCharAt(index, s.getRotAsChr());
        }
        char headChr = Map.of('u','^','d','v','l','<','r','>').get(snake.getFacing());
        gameStr.setCharAt(snake.getY() * (width + 1) + snake.getX(), headChr);
        gameStr.append("Score: " + score + "\n");
        return gameStr.toString();
    }
    
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public Food getFood() {
        return food;
    }
}
