package main;

import java.awt.*;
public class Block {

	private int x, y, width, height, remainingHits;
    private Color color;
    
    //Create a block
    public Block(int x, int y, int width, int height, int remainingHits, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.remainingHits = remainingHits;
        this.color = color;
    }
    //Method to draw the block
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(String.valueOf(this.remainingHits) , (this.x+this.width/2)-6, (this.y+this.height/2)+6);
    }
    
    // Method to get the bounds of the block
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isHit(Ball b) {
    	if (this.getBounds().intersects(b.getBounds())){
    		this.remainingHits--;
    		return true;
    	}
    	return false;
    }
    public boolean isDestroyed() {
    	return this.remainingHits == 0;
    }
    
    public int getY() {
    	return this.y;
    }
    
    public int getHeight() {
    	return this.height;
    }
}
