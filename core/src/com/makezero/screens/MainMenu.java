package com.makezero.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.makezero.logic.LevelGenerator;
import com.makezero.logic.Player;
import com.makezero.screens.Play;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenu implements Screen {

	private Stage stage;
	private Stack base;
	private Skin skin;
	
	private Sound clickSound;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Table.drawDebug(stage);//TBR
		stage.act(delta);
		stage.draw();
		//tweenManager.update(delta);


	}

	@Override
	public void resize(int width, int height) {
		// http://www.badlogicgames.com/wordpress/?p=3322
		stage.getViewport().update(width, height, true);
		base.invalidateHierarchy();
	}

	@Override
	public void show() {
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		String skinFilename = "ui/skin_small.json";
		if(Gdx.graphics.getHeight() >= (1280+640)/2 && Gdx.graphics.getWidth() >= (720+360)/2){
			skinFilename = "ui/skin_large.json";
		}
		skin = new Skin(Gdx.files.internal(skinFilename), new TextureAtlas("ui/textureAtlas.pack"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("sound/choose_option.mp3"));

		Table table = new Table(skin);
		table.setFillParent(true); // Enables resizing the table when window is resized

		// Play button
		TextButton buttonPlay = new TextButton("PLAY", skin, "normal_green");
		buttonPlay.pad(5);
		buttonPlay.addListener(new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				clickSound.play();
				//((Game) Gdx.app.getApplicationListener()).setScreen(new Play());
				stage.addAction(sequence(moveTo(-stage.getWidth(), 0, .5f), run(new Runnable() {

					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new Play());
					}
				})));
			}
		});
		// Settings button
		TextButton buttonSetting = new TextButton("Setting", skin, "small_black");
		buttonSetting.pad(5);
		buttonSetting.addListener(new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
			}
		});
		// Creating heading
		Player player = new Player();
		LevelGenerator.getLevelInfo(0);
		TextButton level = new TextButton(String.valueOf(LevelGenerator.getTotalLevels() - player.getPlayerInfo().levelNum), skin, "normal_blue_plain");level.setDisabled(true);level.pad(10);//level.setColor(1, 1, 1, 1);
		
		Label line1 = new Label("make", skin, "large_white");
		//line1.setColor(1, 0, 0, 1);
		TextButton line3Z = new TextButton("Z", skin, "large_blue");line3Z.setTouchable(Touchable.disabled);line3Z.pad(5);
		TextButton line3E = new TextButton("E", skin, "large_red");line3E.setTouchable(Touchable.disabled);line3E.pad(5);
		TextButton line3R = new TextButton("R", skin, "large_orange");line3R.setTouchable(Touchable.disabled);line3R.pad(5);
		TextButton line3O = new TextButton("O", skin, "large_blue");line3O.setTouchable(Touchable.disabled);line3O.pad(5);
		Label line2 = new Label("it", skin, "normal_white");
		//Label line3 = new Label("zero", skin, "large");
		Label empty = new Label(" ", skin, "large");
		//level.addAction(fadeIn(5) );

		// Creating a 9 column structure
		//table.defaults().getMaxWidth();
		int numColumns = 9;
		for(int i=0;i<numColumns-1;i++){
			table.add(empty).expand().uniform();
		}
		table.add(empty).expand().uniform().row();
		table.add(empty).colspan(numColumns).expand().row();
		
		/*table.add(empty).colspan(6).expand();
		table.add(buttonSetting).colspan(2).expand().top().right().row();*/
		
		table.add(empty).colspan(numColumns).expand().row();
		
		table.add(level).colspan(numColumns).expand().row();
		
		table.add(empty).colspan(numColumns).expand().row();
		
		table.add(empty).colspan(3).expand();
		table.add(buttonPlay).colspan(3).fill().row();
		
		table.add(empty).colspan(numColumns).expand().row();
		
		table.add(line1).colspan(numColumns).expand().row();
		
		table.add(line2).colspan(numColumns).expand().row();
		//table.add(line3).colspan(numColumns).expand().row();
		//table.add(empty).colspan(1).expand();
		table.add(line3Z).colspan(2).right();table.add(line3E).colspan(2).right();table.add(line3R).colspan(2).right();
		table.add(line3O).colspan(3).row();

		table.add(empty).colspan(numColumns).expand().row();

		table.debug(); // TO DO Remove this debug
		
		//Image image = new Image(skin, "smoke_f5f5f5");
		//image.setScaling(Scaling.fill);
		
		base = new Stack();
		base.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//base.add(image);
		base.add(table);
		stage.addActor(base);		
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
		stage.dispose();
		//atlas.dispose(); //skin disposes atlas also
		skin.getAtlas().dispose();
		skin.dispose();
		clickSound.dispose();
	}

}
