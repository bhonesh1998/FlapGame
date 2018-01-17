package com.example.upeshsahu.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Flappybird extends ApplicationAdapter {

	//sprite are simple image
	//sprite batch is something which manages the sprite
	SpriteBatch batch;
	//texture for background
	Texture background;
    Texture gameover;
	//for colllisio just checking the shape
    Circle birdcircle ;

    //use to colour the shapes
    //ShapeRenderer shapeRenderer;
    Rectangle[] toptuberec;
    Rectangle[] bottomtuberec;
	Texture[] tube;
    //texture for birds
	Texture[] birds;
    int score=0;
	int scoringtube=0;
    BitmapFont font;
    int flapState = 0;
    float velocity=0;
	float birdY=0;
	int gamestate=0;
    float tubegap=300;
    float maxtubeoffset;
    float[] offset=new float[4];
    float tubevelocity=4;
    float[] tubeX=new float[4];
    int  nooftube=4;
    float distance;
   Random random;

	//this method is called when app is created
	@Override
	public void create () {




		batch = new SpriteBatch();
		birdcircle =new Circle();
		//shapeRenderer=new ShapeRenderer();
        toptuberec=new Rectangle[4];
        bottomtuberec=new Rectangle[4];

          font=new BitmapFont();
          font.setColor(Color.WHITE);
          font.getData().setScale(4);

          gameover=new Texture("gameover.png");
		//initialisng the background with image
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		tube=new Texture[2];
		tube[0]=new Texture("toptube.png");
		tube[1]=new Texture("bottomtube1.png");
           random=new Random();

          maxtubeoffset=Gdx.graphics.getHeight()/2-tubegap/2-150;
         distance=Gdx.graphics.getWidth()/2+80;
		startgame();
	}
	public void startgame(){
		birdY= (Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2);

		for(int i=0;i<4;i++)
		{

			offset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/2)-tubegap+200;
//			tubeX[i]=Gdx.graphics.getWidth()/2-tube[0].getWidth()/2+ i*distance;
			tubeX[i]=Gdx.graphics.getWidth()+ i*distance;
			toptuberec[i]=new Rectangle();
			bottomtuberec[i]=new Rectangle();
		}

	}

	//this is the looping method which repeats itself
	@Override
	public void render () {
		batch.begin();
		//setting the image in background the left lower most corner and android width and height
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gamestate==1){

			if(tubeX[scoringtube]< Gdx.graphics.getWidth()/2)
			{
				score++;
			        if(scoringtube<3)
					{ scoringtube++;

					}
					else
					{
						scoringtube=0;
					}
					Gdx.app.log("score"," "+score);
			}

			if (Gdx.input.justTouched())
			{
				Gdx.app.log("touched","oops its hurting ");
				velocity=-20;
				//by setting this -ve actaully we are incresing the birdy as - - is +

			}
			for(int i=0;i<4;i++)
			{
               //its put the tube back on screen when disapper
				if(tubeX[i] < -tube[0].getWidth())
				{
					tubeX[i]+=distance*4;
					offset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/2)-tubegap+200;
				}
				else{
					//that will move the tube if screen is not tapped or if tapped loacte the new tube at center
					tubeX[i]-=tubevelocity;

				}

				//setting tubes
				batch.draw(tube[0],tubeX[i],Gdx.graphics.getHeight()/2+tubegap/2+offset[i]);
				batch.draw(tube[1],tubeX[i],Gdx.graphics.getHeight()/2-tubegap/2-tube[1].getHeight()+offset[i]);

			   toptuberec[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2+tubegap/2+offset[i],tube[0].getWidth(),tube[0].getHeight());
			   bottomtuberec[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2-tubegap/2-tube[1].getHeight()+offset[i],tube[1].getWidth(),tube[1].getHeight());

			}




			if(birdY>0)// so that bird never go below the bottom of screen
			{
		//we incresae the velocity of birds and vary the height with changing velocity
            velocity++;
		birdY-=velocity;
			Gdx.app.log("touched",birdY+"  ");}
			else {

				//velocity <0
				gamestate=2;
			}
		}
		else if(gamestate==0)
		{
			if (Gdx.input.justTouched())
			{
				Gdx.app.log("touched","oops its hurting ");
			   gamestate=1;
			}
		}
		else  if(gamestate==2)
		{
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			if (Gdx.input.justTouched())
			{
				Gdx.app.log("touched","playing game one more time ");
				gamestate=1;
			velocity=0;
			score=0;
			scoringtube=0;
				startgame();
			}

		}




		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		//its shiw the animation

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch,"score"+score,10,Gdx.graphics.getHeight()-10);
		batch.end();
		//setting the circle on birds
     //  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
       //shapeRenderer.setColor(Color.GOLD);
                      //         x coordinate                         y coordinate                       radiyus
       birdcircle.set(Gdx.graphics.getWidth()/2,birdY+birds[0].getHeight()/2,birds[0].getWidth()/2);
		//shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);

          for(int i=0;i<4;i++)
		  {
		  //	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2+tubegap/2+offset[i],tube[0].getWidth(),tube[0].getHeight());
		   // shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2-tubegap/2-tube[1].getHeight()+offset[i],tube[1].getWidth(),tube[1].getHeight());

		    if(Intersector.overlaps(birdcircle,toptuberec[i]) || Intersector.overlaps(birdcircle,bottomtuberec[i]))
			{
				gamestate=2;
			
				//Gdx.input.vibrate(200);
				Gdx.app.log("collision ","yes ");
			}
		  }

          //shapeRenderer.end();



	}
}
