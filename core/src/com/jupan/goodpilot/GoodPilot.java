package com.jupan.goodpilot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class GoodPilot extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	int score = 0;
	int scoringMisel = 0;

	BitmapFont font;

	Texture[] planes;
	int flapState = 0;

	float planeY = 0;
	float velocity = 0;
	Circle planeCircle;

	int gameState = 0;
	float gravity = 2;

	Texture topMisel;
	Texture bottomMisel;
	Texture gameover;

	float gap = 400;


	Random randomGenerator;
	float maxMiselOffset;
	float miselVelocity = 4;
	int numberOfMisels = 4;
	float[] miselX = new float[numberOfMisels];
	float[] miselOffset = new float[numberOfMisels];
	float distanceBetweenMisels;

	Rectangle[] topMiselRectangle;
	Rectangle[] bottomMiselRectangle;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.jpeg");
		gameover = new Texture("gameover.png");

		//shapeRenderer = new ShapeRenderer();
		planeCircle = new Circle();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


		planes = new Texture[2];
		planes[0] = new Texture("plane1.png");
		planes[1] = new Texture("plane2.png");



		topMisel = new Texture("topmisel.png");
		bottomMisel = new Texture("bottommisel.png");
		maxMiselOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();

		distanceBetweenMisels = Gdx.graphics.getWidth() * 3 / 4;

		topMiselRectangle = new Rectangle[numberOfMisels];
		bottomMiselRectangle = new Rectangle[numberOfMisels];

		startGame();


	}

	public void startGame() {

		planeY = Gdx.graphics.getHeight() / 2 - planes[0].getHeight() / 2;

		for(int i = 0; i < numberOfMisels; i++) {

			miselOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200); // Create random number between 0 and 1
			miselX[i] = Gdx.graphics.getWidth() / 2 - topMisel.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenMisels;

			topMiselRectangle[i] = new Rectangle();
			bottomMiselRectangle[i] = new Rectangle();

		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (miselX[scoringMisel] < Gdx.graphics.getWidth() / 2) {

				score++;

				Gdx.app.log("Score", String.valueOf(score));

				if (scoringMisel < numberOfMisels - 1) {

					scoringMisel++;

				} else {

					scoringMisel = 0;

				}

			}

			if (Gdx.input.justTouched()) {

				velocity = -30;
				Gdx.app.log("GraphicHeight", String.valueOf(Gdx.graphics.getHeight()));
				Gdx.app.log("CurrentHeight", String.valueOf(planeY));

			}

			for(int i = 0; i < numberOfMisels; i++) {

				if (miselX[i] < -topMisel.getWidth()) {

					miselX[i] += numberOfMisels * distanceBetweenMisels;
					miselOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {

					miselX[i] -= miselVelocity;

				}

				batch.draw(topMisel, miselX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + miselOffset[i]);
				batch.draw(bottomMisel, miselX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomMisel.getHeight() + miselOffset[i]);

				topMiselRectangle[i] = new Rectangle(miselX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + miselOffset[i], topMisel.getWidth(), topMisel.getHeight());
				bottomMiselRectangle[i] = new Rectangle(miselX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomMisel.getHeight() + miselOffset[i], bottomMisel.getWidth(), bottomMisel.getHeight());
			}

			if (planeY > 0 && planeY < Gdx.graphics.getHeight()) {

				velocity += gravity;
				planeY -= velocity;

			} else if(planeY <= 0 || planeY >= Gdx.graphics.getHeight()){

				gameState = 2;


			}

		} else if (gameState == 0) {

			if (Gdx.input.justTouched()) {

				gameState = 1;

			}

		} else if (gameState == 2) {

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

			if (Gdx.input.justTouched()) {

				gameState = 1;
				startGame();

				score = 0;
				scoringMisel = 0;
				velocity = 0;

			}

		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.draw(planes[flapState], Gdx.graphics.getWidth() / 2 - planes[flapState].getWidth() / 2, planeY);

		font.draw(batch, String.valueOf(score), 100, 200);

		planeCircle.set(Gdx.graphics.getWidth() / 2, planeY + planes[flapState].getHeight() / 2, planes[flapState].getWidth() / 2);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(planeCircle.x, planeCircle.y, planeCircle.radius);*/

		for(int i = 0; i < numberOfMisels; i++) {

			/*shapeRenderer.rect(miselX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + miselOffset[i], topMisel.getWidth(), topMisel.getHeight());
			shapeRenderer.rect(miselX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomMisel.getHeight() + miselOffset[i], bottomMisel.getWidth(), bottomMisel.getHeight());*/

			if (Intersector.overlaps(planeCircle, topMiselRectangle[i]) || Intersector.overlaps(planeCircle, bottomMiselRectangle[i])) {

				gameState = 2;

			}

		}

		batch.end();


		//shapeRenderer.end();
	}

}
