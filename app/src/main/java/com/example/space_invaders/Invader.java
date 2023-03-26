package com.example.space_invaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Invader {

    RectF rect;

    Random generator = new Random();

    // The player ship will be represented by a Bitmap
    private Bitmap bitmap1;
    private Bitmap bitmap2;

    // How long and high our invader will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our invader
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the invader will move
    private float shipSpeed;

    private char note;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the ship moving and in which direction
    private int shipMoving = RIGHT;

    boolean isVisible;

    public Invader(Context context, int row, int column, int screenX, int screenY, char storedNote) {

        // Initialize a blank RectF
        rect = new RectF();

        length = screenX / 7;
        height = screenY / 7;

        isVisible = true;

        int padding = screenX / 35;

        x = column * (length + padding);
        y = row * (length + padding/4);

        note = storedNote;

        // ToDo Initialize the bitmap
        switch(note){
            case 'c': bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderc1);
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderc2);
                break;
            case 'd': bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderd1);
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderd2);
                break;
            case 'e': bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invadere1);
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invadere2);
                break;
            case 'f': bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderf1);
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderf2);
                break;
            case 'g': bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderg1);
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderg2);
                break;
            case 'a': bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invadera1);
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invadera2);
                break;
            default: bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderh1);
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invaderh2);
            break;
        }

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);

        // How fast is the invader in pixels per second
        shipSpeed = 40;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getStatus() { return isVisible; }

    public boolean getVisibility(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap1;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
    }

    public char getStoredNote() { return note; }

    public int getDirection() { return shipMoving; }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getBottom() {return rect.bottom; }

    public float getLength(){
        return length;
    }

    public float getHeight() {return height; }

    public void update(long fps){
        if(shipMoving == LEFT){
            x = x - shipSpeed / fps;
        }

        if(shipMoving == RIGHT){
            x = x + shipSpeed / fps;
        }

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom= y + height;
        rect.left = x;
        rect.right = x + length;
    }

    public void dropDownAndReverse(){
        if(shipMoving == LEFT){
            shipMoving = RIGHT;
        }else{
            shipMoving = LEFT;
        }
        y = y + height;
        shipSpeed*=1.18f;
    }

    public boolean takeAim(float playerShipX, float playerShipY, float playerShipLength){
        // This method is kind of like invader's artifficial intelligence

        //The first block of code in this method detects if the Invader object is approximately horizontally aligned
        // with the player. If it is a random number is generated to produce a 1 in 150 chance of returning true
        // to the calling code. As we will see shortly in the calling code,
        // when true is returned an attempt to launch a bullet is made with a call to shoot.

        int randomNumber = -1;

        // If near the player
        if((playerShipX + playerShipLength > x &&
                playerShipX + playerShipLength < x + length) || (playerShipX > x && playerShipX < x + length)){
            // A one in 150 chance to shoot
            randomNumber = generator.nextInt(15);
            if(randomNumber == 0){
                return true;
            }
        }

        // If firing randomly (not near the player) a 1 in 500 chance
        randomNumber = generator.nextInt(150);
        if(randomNumber == 0){
            return true;
        } return false;
    }


}
