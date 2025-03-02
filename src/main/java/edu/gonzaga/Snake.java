package edu.gonzaga;

import java.util.ArrayList;

public class Snake extends BoardElement {
    private int length = 4;
    private ArrayList<SnakeSegment> segments;
    private char facing = 'r'; // u, d, l, r
    private int nextRotation = 'l' + 'r';
    private int invincibleTimer = 10;

    public Snake() {
        super(3, 7);
        segments = new ArrayList<>();
        segments.add(new SnakeSegment(0, 7, 1, 'l' + 'r'));
        segments.add(new SnakeSegment(1, 7, 2, 'l' + 'r'));
        segments.add(new SnakeSegment(2, 7, 3, 'l' + 'r'));
    }

    //moves snake forward
    public void move() {
        segments.add(new SnakeSegment(x, y, length, nextRotation));
        switch (facing) {
            case 'u':
                y--;
                break;
            case 'd':
                y++;
                break;
            case 'l':
                x--;
                break;
            case 'r':
                x++;
                break;
        }
        for (SnakeSegment s : segments) {
            s.subtractLife(1);
        }
        if (segments.get(0).getLife() == 0) {
            segments.remove(0);
        }
        if (invincibleTimer > 0) {
            invincibleTimer--;
        }
        nextRotation = facing == 'u' || facing == 'd' ? 'u' + 'd' : 'l' + 'r';
    }

    public void grow() {
        for (SnakeSegment s : segments) {
            s.subtractLife(-1);
        }
        length++;
    }

    public void turn(char dir) {
        if (canTurn(dir)) {
            // opposite of prev dir, add new dir, set as next rotation
            nextRotation = (facing ^ (facing % 6 == 0 ? 30 : 17)) + dir;
            facing = dir;
        }
    }

    private boolean canTurn(char dir) {
        if (facing == dir) {
            return false;
        }
        if (facing + dir == 'u' + 'd' || facing + dir == 'l' + 'r') {
            return false;
        }
        return true;
    }

    public char getFacing() {
        return facing;
    }

    public ArrayList<SnakeSegment> getSegments() {
        return segments;
    }

    public int getLength() {
        return length;
    }

    public void setInvincible(int time) {
        invincibleTimer = time;
    }

    public int getInvincible() {
        return invincibleTimer;
    }

    public String toString() {
        String out = "h(" + x + "," + y + " " + (char) facing + " " + length + ")\n";
        for (SnakeSegment s : segments) {
            out += s + "\n";
        }
        return out;
    }

    public SnakeSegment getHead() {
        return segments.get(segments.size() - 1);
    }
}