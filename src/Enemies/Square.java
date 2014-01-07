package Enemies;
import GameObjects.Bullet;
import java.awt.*;

public class Square extends Enemy {

    public Square(double cx, double cy) {
        double potentialX, potentialY;
        while (true) {
            potentialX = Math.random()*xBoundary + xOffset;
            potentialY = Math.random()*yBoundary + yOffset;
            if (potentialX > cx + enemySpawnPadding || potentialX < cx - enemySpawnPadding || potentialY > cy + enemySpawnPadding || potentialY < cy - enemySpawnPadding) {
                break;
            }
        }
        this.x = potentialX;
        this.y = potentialY;
        this.shape = new Polygon();
    }

    protected void drawShape() {
        this.dodgeZone = new Rectangle ((int)(this.x - dodgeZoneSize / 2), (int)(this.y - dodgeZoneSize / 2), dodgeZoneSize, dodgeZoneSize);
        this.shape.reset();
        this.shape.addPoint((int) (this.x - size / 2), (int) (this.y - size / 2));
        this.shape.addPoint((int) (this.x - size / 2), (int) (this.y - size / 2) + size);
        this.shape.addPoint((int) (this.x - size / 2) + size, (int) (this.y - size / 2) + size);
        this.shape.addPoint((int) (this.x - size / 2) + size, (int) (this.y - size / 2));
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.cyan);
        graphics.drawPolygon(this.shape);
    }

    public void move(double targetX, double targetY) {
        if (this.isDodging && this.incomingBullet != null) {
            this.dodgeManeuver(this.incomingBullet);
        } else {
            double rad = Math.atan2((targetY - this.y),(targetX - this.x));
            this.x += Math.cos(rad)*velocity;
            this.y += Math.sin(rad)*velocity;
        }
        this.drawShape();
    }

    private void dodgeManeuver(Bullet bullet) {
        Polygon path = new Polygon();
        double d = 25;
        double bullAngle = bullet.getRadian();
        double realAngle = Math.atan2(this.y-bullet.getY(), this.x-bullet.getX());
        if (bullAngle<0){
            bullAngle+=Math.PI*2;
        }
        if (realAngle<0){
            realAngle+=Math.PI*2;
        }

        path.addPoint((int)(bullet.getX()+Math.cos(bullAngle+Math.PI/2)*d), (int)(bullet.getY()+Math.sin(bullAngle+Math.PI/2)*d));
        path.addPoint((int)(bullet.getX()+Math.cos(bullAngle-Math.PI/2)*d), (int)(bullet.getY()+Math.sin(bullAngle-Math.PI/2)*d));
        path.addPoint((int)((bullet.getX()+Math.cos(bullAngle-Math.PI/2)*d)+Math.cos(bullAngle)*150), (int)((bullet.getY()+Math.sin(bullAngle-Math.PI/2)*d)+Math.sin(bullAngle)*150));
        path.addPoint((int)((bullet.getX()+Math.cos(bullAngle+Math.PI/2)*d)+Math.cos(bullAngle)*150), (int)((bullet.getY()+Math.sin(bullAngle+Math.PI/2)*d)+Math.sin(bullAngle)*150));

        if (path.contains(this.x, this.y)){
            if (bullAngle<realAngle){
                this.x+=Math.cos(bullAngle+Math.PI/2)*this.velocity;
                this.y+=Math.sin(bullAngle+Math.PI/2)*this.velocity;
            }
            else{
                this.x+=Math.cos(bullAngle-Math.PI/2)*this.velocity;
                this.y+=Math.sin(bullAngle-Math.PI/2)*this.velocity;
            }
        }
    }
}
