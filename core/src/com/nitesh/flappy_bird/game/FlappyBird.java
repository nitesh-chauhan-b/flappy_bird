package com.nitesh.flappy_bird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    int flapState = 0;
    int pause = 0;
    float birdY = 0;


    //Bird Speed
    float velocity = 0;
    float gravity = 0.9f;

    //Game State
    int gameState = 0;

    //Setting tubes
    Texture top_tube, bottom_tube;
    float gap = 500;
    float maxtubeOffset;

    Random randomGenerator;
    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;

    //Setting collision detection
    Circle bird_circle;
    ShapeRenderer shapeRenderer;
    Rectangle[] top_tube_rectangle;
    Rectangle[] bottom_tube_rectangle;

    //Score
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

    //Game Over things
    Texture gameOver;
    float tempOffset;
    int gameOverPause =0;
    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");


        top_tube = new Texture("toptube.png");
        bottom_tube = new Texture("bottomtube.png");

        maxtubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 305;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;


        shapeRenderer = new ShapeRenderer();
        bird_circle = new Circle();
        top_tube_rectangle = new Rectangle[numberOfTubes];
        bottom_tube_rectangle = new Rectangle[numberOfTubes];


        startGame();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        gameOver = new Texture("gameover.png");

    }

    public void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        //Creating loop of tubes
        for (int i = 0; i < numberOfTubes; i++) {

            tempOffset = (randomGenerator.nextFloat() - 0.4f) * (Gdx.graphics.getHeight() - gap - 200);
            if (tempOffset < 410 && tempOffset > -410) {
                tubeOffset[i] = tempOffset;
                Gdx.app.log("OFFSET", String.valueOf(maxtubeOffset));
                Gdx.app.log("CURRENT_OFFSET", String.valueOf(tempOffset));
            }

            tubeX[i] = Gdx.graphics.getWidth() / 2 - top_tube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            top_tube_rectangle[i] = new Rectangle();
            bottom_tube_rectangle[i] = new Rectangle();

        }
    }

    @Override
    public void render() {

        //Check Whether the screen is touched or not
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;

                Gdx.app.log("SCORE", String.valueOf(score));

                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -20;
            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -top_tube.getWidth()) {

                    tubeX[i] = numberOfTubes * distanceBetweenTubes;
                    tempOffset = (randomGenerator.nextFloat() - 0.4f) * (Gdx.graphics.getHeight() - gap - 200);

                    if (tempOffset < 410 && tempOffset > -410) {
                        tubeOffset[i] = tempOffset;
                        Gdx.app.log("CURRENT_OFFSET", String.valueOf(tempOffset));
                        Gdx.app.log("OFFSET", String.valueOf(maxtubeOffset));
                    }


                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }

                batch.draw(top_tube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottom_tube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottom_tube.getHeight() + tubeOffset[i]);

                top_tube_rectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], top_tube.getWidth(), top_tube.getHeight());
                bottom_tube_rectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottom_tube.getHeight() + tubeOffset[i], bottom_tube.getWidth(), bottom_tube.getHeight());

//                top_tube_rectangle.set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], top_tube.getWidth(), top_tube.getHeight());
//
//                bottom_tube_rectangle.set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottom_tube.getHeight() + tubeOffset[i], bottom_tube.getWidth(), bottom_tube.getHeight());
//
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                shapeRenderer.setColor(Color.RED);
//
//                shapeRenderer.rect(top_tube_rectangle.x, top_tube_rectangle.y, top_tube_rectangle.width, top_tube_rectangle.height);
//
//                shapeRenderer.rect(bottom_tube_rectangle.x, bottom_tube_rectangle.y, bottom_tube_rectangle.width, bottom_tube_rectangle.height);
//
//                shapeRenderer.end();
            }


            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY = birdY - velocity;
            } else {
                gameState=2;
            }


            if (pause < 5) {
                pause++;
            } else {
                pause = 0;
                if (flapState == 0) {
                    flapState = 1;
                } else {
                    flapState = 0;
                }
            }

        }

        else if (gameState == 0) {
            //After GameOver Start again
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }

        else if (gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - 500/ 2, Gdx.graphics.getHeight() / 2 - 250 / 2,500,250);

            //After GameOver Start again
            if (Gdx.input.justTouched()) {


                if(gameOverPause<1)
                {
                    gameOverPause++;
                }
                else{
                    gameOverPause=0;
                    gameState = 1;
                    startGame();
                    score = 0;
                    scoringTube = 0;
                    velocity = 0;
                }


            }

        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch, String.valueOf(score), 50, 180);
        batch.end();


        bird_circle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(bird_circle.x, bird_circle.y, bird_circle.radius);

        for (int i = 0; i < numberOfTubes; i++) {
//            shapeRenderer.rect(top_tube_rectangle[i].x, top_tube_rectangle[i].y, top_tube_rectangle[i].width, top_tube_rectangle[i].height);
//            shapeRenderer.rect(bottom_tube_rectangle[i].x, bottom_tube_rectangle[i].y, bottom_tube_rectangle[i].width, bottom_tube_rectangle[i].height);
            if (Intersector.overlaps(bird_circle, top_tube_rectangle[i]) || Intersector.overlaps(bird_circle, bottom_tube_rectangle[i])) {
                gameState=2;
            }
        }

//        shapeRenderer.end();

    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
