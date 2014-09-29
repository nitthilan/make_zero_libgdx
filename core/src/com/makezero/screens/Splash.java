package com.makezero.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.makezero.screens.MainMenu;
import com.makezero.tweens.SpriteAccessor;

public class Splash implements Screen {
	private SpriteBatch batch;
	private Sprite splash;
	//private Texture texture;
	private TweenManager tweenManager;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		splash.draw(batch);
		batch.end();
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		splash.setSize(width, height);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		Texture texture = new Texture(Gdx.files.internal("img/blue_sky_kanidan.png"));
		splash = new Sprite(texture);
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 0.5f).target(1).repeatYoyo(1, 0.5f).setCallback(new TweenCallback(){

			@Override
			public void onEvent(int type, BaseTween<?> source) {
				 ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				
			}
			
		}).start(tweenManager);
		tweenManager.update(Float.MIN_VALUE);// ideally should use tweenManager.update(Gdx.graphics.getDeltaTime()); but since for the first call the delta value would be zero we are passing a small value

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		splash.getTexture().dispose();
	}

}
