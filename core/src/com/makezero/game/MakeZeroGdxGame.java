package com.makezero.game;

import com.badlogic.gdx.Game;
import com.makezero.screens.Splash;

public class MakeZeroGdxGame extends Game {
	public static final String TITLE="Make It Zero", VERSION="0.0.1";
	@Override
	public void create () {
		setScreen(new Splash());
	}

	@Override
	public void render () {
		super.render();
	}
	@Override
	public void dispose() {
		super.dispose();
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	@Override
	public void pause() {
		super.pause();
	}
	@Override
	public void resume() {
		super.resume();
	}
}
