/*package com.example.space_invaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences; ///
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.RectF;
import java.util.Random;
import java.io.IOException;

public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;

    Random generator = new Random();

    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused at the start
    private boolean paused = true;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    // The players ship
    private PlayerShip playerShip;

    // The player's bullet
    private Bullet bullet;

    // The invaders bullets
    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    // Up to 10 invaders
    Invader[] invaders = new Invader[10];
    int numInvaders = 0;
    int numVisibleInvaders = 0;

    // The player's shelters are built from bricks
    private DefenseBrick[] bricks = new DefenseBrick[400];
    private int numBricks;

    // For sound FX
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    //notes:
    private int cNoteID=  -1;
    private int dNoteID = -1;
    private int eNoteID = -1;
    private int fNoteID = -1;
    private int gNoteID = -1;
    private int aNoteID = -1;
    private int hNoteID = -1;

    private boolean nextLevel=false;
    private int level = 1;

    // The score
    int score = 0;
    //int previousScore = 0;

    // Lives
    private int lives = 1;

    // CurrentNote
    private char currentNote = 'c'; // ToDo set the current note
    private char currentNote2;
    private boolean soundHasBeenPlayed = false;

    // How menacing should the sound be?
    private long menaceInterval = 1000;
    // Which menace sound should play next
    private boolean uhOrOh;
    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();
    private long lastMenaceTime2 = System.currentTimeMillis();

    private boolean playerDidShootTheWrongInvader = false;
    private int counter = 0;
    private int counter2 = 0;

    /*SharedPreferences prefs;
    prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
    final SharedPreferences.Editor editor = prefs.edit();*/


    // When the we initialize (call new()) on gameView
// This special constructor method runs
    /*public SpaceInvadersView(Context context, int x, int y) {

        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        // This SoundPool is deprecated but don't worry
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

            //notes:
            descriptor = assetManager.openFd("Notes/c.mp3");
            cNoteID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("Notes/d.mp3");
            dNoteID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("Notes/e.mp3");
            eNoteID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("Notes/f.mp3");
            fNoteID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("Notes/g.mp3");
            gNoteID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("Notes/a.mp3");
            aNoteID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("Notes/h.mp3");
            hNoteID = soundPool.load(descriptor, 0);



        } catch (IOException e) {
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }

    private void prepareLevel() {

        // Here we will initialize all the game objects
        // Reset the menace level
        menaceInterval = 1000;

        // Prepare the players bullet
        bullet = new Bullet(screenY);

        // Make a new player space ship
        playerShip = new PlayerShip(context, screenX, screenY);

        // Prepare the players bullet
        bullet = new Bullet(screenY);

        // Initialize the invadersBullets array
        for(int i = 0; i < invadersBullets.length; i++){
            invadersBullets[i] = new Bullet(screenY);
        }

        int randomNumber = -1;

        // Build an army of invaders depended on current level
        numInvaders = 0;
        numVisibleInvaders = 0;
        if(level<4) {
            for (int column = 0; column < level; column++) {
                for (int row = 0; row < 1; row++) {
                    randomNumber = generator.nextInt(7) + 97;
                    // ToDo random note stored by the invader
                    invaders[numInvaders] = new Invader(context, row, column, screenX, screenY, (char) randomNumber);

                    numInvaders++;
                    numVisibleInvaders++;
                }
            }// 4
        }else if(level==4){
            for (int column = 0; column < 2; column++) {
                for (int row = 0; row < 2; row++) {
                    randomNumber = generator.nextInt(7) + 97;

                    invaders[numInvaders] = new Invader(context, row, column, screenX, screenY, (char) randomNumber);

                    numInvaders++;
                    numVisibleInvaders++;
                }
            }// 5
        }else if(level==5){
            for (int column = 0; column < level; column++) {
                for (int row = 0; row < 1; row++) {
                    randomNumber = generator.nextInt(7) + 97;

                    invaders[numInvaders] = new Invader(context, row, column, screenX, screenY, (char) randomNumber);

                    numInvaders++;
                    numVisibleInvaders++;
                }
            }// 6
        }else if(level==6){
            for (int column = 0; column < 3; column++) {
                for (int row = 0; row < 2; row++) {
                    randomNumber = generator.nextInt(7) + 97;

                    invaders[numInvaders] = new Invader(context, row, column, screenX, screenY, (char) randomNumber);

                    numInvaders++;
                    numVisibleInvaders++;
                }
            }// 7 (8 invaders)
        }else if(level==7){
            for (int column = 0; column < 4; column++) {
                for (int row = 0; row < 2; row++) {
                    randomNumber = generator.nextInt(7) + 97;

                    invaders[numInvaders] = new Invader(context, row, column, screenX, screenY, (char) randomNumber);

                    numInvaders++;
                    numVisibleInvaders++;
                }
            }
        }

        // Build the shelters
        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new DefenseBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }

    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // Play a sound based on the menace level
            if(!paused) {
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // Reset the last menace time
                    lastMenaceTime = System.currentTimeMillis();
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
                // ToDo change the currentNote based on menace level
                int randomNumber2 = -1;
                if((startFrameTime - lastMenaceTime2) > 7*menaceInterval){ // ToDo How often the currentNote should change?
                    randomNumber2 = generator.nextInt(7) + 97;
                    currentNote = (char)randomNumber2;
                    soundHasBeenPlayed=false;
                    lastMenaceTime2 = System.currentTimeMillis();
                }
            }

        }



    }

    private void update () {

        // Did an invader bump into the side of the screen
        boolean bumped = false;

        // Has the player lost?
        boolean lost = false;

        // Move the player's ship
        playerShip.update(fps);

        // Update the invaders if visible
        for(int i = 0; i < numInvaders; i++){

            if(invaders[i].getVisibility()) {
                // Move the next invader
                invaders[i].update(fps);

                // Does he want to take a shot?
                if(invaders[i].takeAim(playerShip.getX(), playerShip.getY(),
                        playerShip.getLength())){

                    // If so try and spawn a bullet
                    if(invadersBullets[nextBullet].shoot(invaders[i].getX()
                                    + invaders[i].getLength() / 2,
                            invaders[i].getY(), bullet.DOWN)) {

                        // Shot fired
                        // Prepare for the next shot
                        nextBullet++;

                        // Loop back to the first one if we have reached the last
                        if (nextBullet == maxInvaderBullets) {
                            // This stops the firing of another bullet until one completes its journey
                            // Because if bullet 0 is still active shoot returns false.
                            nextBullet = 0;
                        }
                    }
                }

                // If that move caused them to bump the screen change bumped to true
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0){

                    bumped = true;

                }
            }
        }

        // Update all the invaders bullets if active
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                canvas.drawRect(invadersBullets[i].getRect(), paint);
            }
        }

        // Did an invader bump into the edge of the screen
        if(bumped){

            // Move all the invaders down and change direction
            for(int i = 0; i < numInvaders; i++){
                invaders[i].dropDownAndReverse();

            }
                    // ToDo Increase the menace level
            // By making the sounds more frequent
            menaceInterval = menaceInterval - 80;
        }
        for(int i = 0; i < numInvaders; i++) {
            // Have an invader collapsed with a playership?
            if (invaders[i].getStatus() && invaders[i].getY() + invaders[i].getHeight() > screenY
                    && invaders[i].getX()!=0 && invaders[i].getX() + invaders[i].getLength() != screenX
                    && ((invaders[i].getDirection()==1 && invaders[i].getX() + invaders[i].getLength()/2 <= playerShip.getX() + playerShip.getLength()/2)
                    || (invaders[i].getDirection()==2 && invaders[i].getX() + invaders[i].getLength()/2 >= playerShip.getX() + playerShip.getLength()/2))
            )
            {
                lost = true;
                Intent intent = new Intent(context, LostScreenActivity.class);
                context.startActivity(intent);
                //pause();
            }
        }

        // Update the players bullet
        if(bullet.getStatus()){
            bullet.update(fps);
        }

        // Has the player's bullet hit the top of the screen
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }

        // Has an invaders bullet hit the bottom of the screen
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getImpactPointY() > screenY){
                invadersBullets[i].setInactive();
            }
        }

        // Has the player's bullet hit an invader
        if(bullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), invaders[i].getRect())) { // ToDo has the player bullet hit the right invader?
                        invaders[i].setInvisible();
                        numVisibleInvaders--;
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();

                        if (invaders[i].getStoredNote() == currentNote) { // if player did hit the right invader
                            score = score + 10;
                        } else {
                            lives--;
                            // ToDo play sound informing the player that he has shoot the wrong invader
                            playerDidShootTheWrongInvader = true;

                        }
                        //has the player lost?
                        if (lives == 0) lost = true;
                        if (lost) { // ToDo react on player's defeaft
                            paused = true;

                            Intent intent = new Intent(context, LostScreenActivity.class);
                            context.startActivity(intent);
                        }

                        // Has the player comleted the current level?
                        if (numVisibleInvaders == 0 && lives > 0) {
                            // Has the player won the whole game?
                            if(level==7){

                                SharedPreferences prefs = SpaceInvadersActivity.this.getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("level 7 completed", true);
                                editor.commit();

                                paused = true;

                                Intent intent2 = new Intent(context, WinningScreenActivity.class);
                                context.startActivity(intent2);
                            }else{ nextLevel = true; }

                        }
                    }
                }
            }
        }

        // Has an alien bullet hit a shelter brick
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                for(int j = 0; j < numBricks; j++){
                    if(bricks[j].getVisibility()){
                        if(RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())){
                            // A collision has occurred
                            invadersBullets[i].setInactive();
                            bricks[j].setInvisible();
                            soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                        }
                    }
                }
            }

        }

        // Has a player bullet hit a shelter brick
        if(bullet.getStatus()){
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()){
                    if(RectF.intersects(bullet.getRect(), bricks[i].getRect())){
                        // A collision has occurred
                        bullet.setInactive();
                        bricks[i].setInvisible();
                        soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }

        // Has an invader bullet hit the player ship
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                if(RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())){
                    invadersBullets[i].setInactive();
                    lives --;
                    soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);
                }
            }
        }

    }

    private void draw () {
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));

            Paint myPaint = new Paint();
            myPaint.setColor(Color.rgb(255, 255, 128 ));


            // Draw the player spaceship
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - 50, paint);

            // Draw the invaders
            for(int i = 0; i < numInvaders; i++){
                if(invaders[i].getVisibility()) {
                    if(uhOrOh) {
                        canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                    }else{
                        canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                    }
                }
            }

            // Draw the bricks if visible
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), myPaint);
                }
            }

            // Draw the players bullet if active
            if(bullet.getStatus()){
                canvas.drawRect(bullet.getRect(), paint);
            }

            // Draw the invaders bullets if active

            // Draw the score and remaining lives
            // Change the brush color
            paint.setColor(Color.argb(255,  255, 255, 128));
            paint.setTextSize(40);
            canvas.drawText("Score: " + (score) + "   Lives: " + lives + " Level: " + level, 10,50, paint);
            // Change the brush color
            /*paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);*/
            /*paint.setTextSize(60);
            currentNote2 = (char)((int)currentNote - 32);
            canvas.drawText("Note: " + currentNote2, 10,150, paint); // ToDo draw the current note

            //if player has completed the level- inform him about it
            if(nextLevel==true){
                paint.setColor(Color.BLUE);
                if(level==6){
                    paint.setTextSize(70);
                    canvas.drawText("Gratulacje! ukończyłeś level " + level + "! ", 10, 560, paint);
                    paint.setTextSize(100);
                    paint.setColor(Color.YELLOW);
                    canvas.drawText("Skup się, to już ostatni poziom! ", 10, 760, paint);
                }else {
                    paint.setTextSize(100);
                    canvas.drawText("Gratulacje! ukończyłeś level " + level + "!", 10, 560, paint);
                }
                paused=true;

                if(counter2>150){
                nextLevel=false;
                level++;
                if(level==2) { lives = 1; }
                else if(level==3 || level==4) { lives=2; }
                else { lives=3; }
                counter2=0;
                prepareLevel();
                }else {
                    counter2++;
                }
            }

            if(soundHasBeenPlayed == false){
            switch(currentNote){ // ToDo play the current note

                case 'a': soundPool.play(aNoteID, 3, 3, 0, 0, 1);
                          break;
                case 'b': soundPool.play(hNoteID, 3, 3, 0, 0, 1);
                          break;
                case 'c': soundPool.play(cNoteID, 3, 3, 0, 0, 1);
                          break;
                case 'd': soundPool.play(dNoteID, 3, 3, 0, 0, 1);
                          break;
                case 'e': soundPool.play(eNoteID, 3, 3, 0, 0, 1);
                          break;
                case 'f': soundPool.play(fNoteID, 3, 3, 0, 0, 1);
                          break;
                case 'g': soundPool.play(gNoteID, 3, 3, 0, 0, 1);
                          break;
            }soundHasBeenPlayed=true;
            }

            if(playerDidShootTheWrongInvader == true){
                // ToDo draw the communicat that the player shoot the wrong invader
                paint.setColor(Color.RED);
                paint.setTextSize(30);
                canvas.drawText("Ups! To nie ta nutka- Zestrzeliłeś złego kosmitę! ", 10, 250, paint);
                counter++; // ToDo how much time lasted from the moment when the player shoot the wrong invader?
                if(counter>300) {
                    playerDidShootTheWrongInvader = false;
                    counter = 0;
                }
            }

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // If SpaceInvadersActivity is paused/stopped
    // shutdown our thread.
    public void pause () {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    public void resume () {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                paused = false;

                if(motionEvent.getY() > screenY - screenY / 8) {
                    if (motionEvent.getX() > screenX / 2) {
                            playerShip.setMovementState(playerShip.RIGHT);
                    } else {
                         playerShip.setMovementState(playerShip.LEFT);
                    }

                }

                if(motionEvent.getY() < screenY - screenY / 8) {
                    // Shots fired
                    if(bullet.shoot(playerShip.getX()+
                            playerShip.getLength()/2,screenY,bullet.UP)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }
                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                if(motionEvent.getY() > screenY - screenY / 10) {
                    playerShip.setMovementState(playerShip.STOPPED);
                }

                break;

        }

        return true;
    }

}*/