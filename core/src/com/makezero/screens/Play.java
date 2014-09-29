package com.makezero.screens;

import org.matheclipse.parser.client.eval.DoubleEvaluator;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.makezero.logic.LevelGenerator;
import com.makezero.logic.Player;
import com.makezero.models.LevelInfo;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class Play implements Screen {

	private Stage stage;
	private Stack layerStack;

	private Actor layer2;
	private Label awesomeLabel;
	private Button continueButton;
	private Image blackImage;
	//private Table layer3;
	//private Table table;
	private Skin skin;

	private LevelInfo levelInfo;
	
	private Player player;
	
	private Array<TextButton> questionButtonArray;
	private Array<TextButton> optionsButtonArray;
	
	private Array<Integer> questionIndexArray;
	private Array<Integer> optionIndexArray;
	
	private Sound chooseOption;
	private Sound revertOption;
	private Sound wrongAnswer;
	private Sound rightAnswer;
	private Sound continueGame;
	
	
	private void fillTopMenu(int numColumns, Table table){
		// Top Line menu [Back Level Gold coin]
		TextButton back = new TextButton("back", skin, "small_black");
		back.pad(5);
		back.addListener(new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});
		
		Label empty = new Label(" ", skin, "large");
		
		table.add(empty).colspan(1).expand();
		table.add(back).colspan(2).top().left();
		table.add(new TextButton(String.valueOf(LevelGenerator.getTotalLevels() - player.getPlayerInfo().levelNum), skin, "small_red").pad(5)).colspan(3).top();
		table.add(new TextButton(String.valueOf(player.getPlayerInfo().numGoldCoins), skin, "small_orange").pad(5)).colspan(2).top().right();
		table.debug();
	}
	
	private Array<TextButton> createQuestionButtons(Array<String> question){
		Array<TextButton> questionArray = new Array<TextButton>();
		for(int i=0;i<question.size;i++){
			String skin_option = "normal_blue";
			if(question.get(i).equals("?"))	{
				skin_option = "normal_red";
			}
			//Gdx.app.error("Question", i+" "+question.get(i));
			questionArray.add(new TextButton(question.get(i), skin, skin_option));
			//questionArray.get(i).setDisabled(true);
			questionArray.get(i).setTouchable(Touchable.disabled);
			questionArray.get(i).setName(String.valueOf(i));
			//questionArray.get(i).pad(2);
		}
		return questionArray;
	}
	
	private void createQuestionTable(int numColumns, Table table, Array<TextButton> questionButtonArray){
		Label empty = new Label(" ", skin, "large");
		float height = Gdx.graphics.getHeight()/12;
		Integer tableSize = 7;
		int j = 0;
		for(j=0;j<questionButtonArray.size/tableSize;j++){
			table.add(empty).colspan(1).expand();
			for(int i=0;i<tableSize;i++){
				table.add(questionButtonArray.get(j*tableSize+i).pad(0)).colspan(1).height(height).expand().fill();
			}
			table.row();
		}
		int padColumns = 1+((j+1)*tableSize - questionButtonArray.size)/2;
		table.add(empty).colspan(padColumns).expand();
		for(int i=j*tableSize;i<questionButtonArray.size;i++){
			table.add(questionButtonArray.get(i).pad(0)).colspan(1).expand().height(height).fill();
		}
		for(int i=j;i<3;i++){
			table.add(empty).colspan(numColumns).expand().row();
		}
	}
	private Array<TextButton> createOptionsButton(Array<String> options){
		Array<TextButton> optionsArray = new Array<TextButton>();
		for(int i=0;i<options.size;i++){
			optionsArray.add(new TextButton(options.get(i), skin, "normal_green"));
			optionsArray.get(i).setName(String.valueOf(i));
		}
		return optionsArray;		
	}
	private void createOptionsTable(int numColumns, Table table, Array<TextButton> optionsTable){
		assert(optionsTable.size != 14);
		Label empty = new Label(" ", skin, "large");
		
		float height = Gdx.graphics.getHeight()/12;

		Integer tableSize = 7;
		table.add(empty).colspan(1).expand();
		for(int i=0;i<tableSize;i++){
			table.add(optionsTable.get(i).pad(0)).colspan(1).bottom().expand().height(height).fill();
		}
		table.row();
		table.add(empty).colspan(1).expand();
		for(int i=0;i<tableSize;i++){
			table.add(optionsTable.get(tableSize+i).pad(0)).colspan(1).top().expand().height(height).fill();
		}
		table.row();
	}
	
	private void initIndexes(Array<String> questions){
		questionIndexArray = new Array<Integer>();
		optionIndexArray = new Array<Integer>();
		for(int i=0;i<questions.size;i++){
			if(questions.get(i).equals("?")) {
				questionIndexArray.add(i);
				optionIndexArray.add(-1);				
			}
		}
	}
	
	private boolean isAllEmptyIdxFull(Array<Integer> optionIndexArray){
		for(int i=0;i<optionIndexArray.size;i++){
			if(optionIndexArray.get(i) == -1) return false;
		}
		return true;
	}
	
	private boolean isExpressionZero(String expression){
		try {
			DoubleEvaluator engine = new DoubleEvaluator();
			double d = engine.evaluate(expression);
			//Gdx.app.log("Evaluated expression", Double.toString(d)+ " expression "+ expression);
			if( Double.toString(d).compareTo("0.0")== 0 ) return true;
			else return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	private Integer findEmptyIdx(Array<Integer> optionIndexArray){
		for(int i=0;i<optionIndexArray.size;i++){
			if(optionIndexArray.get(i) == -1) return i;
		}
		return -1;
	}
	
	private boolean isAnswerCorrect(){
		String answer = "";
		int j = 0;
		for(int i=0;i<levelInfo.question.size;i++){
			if(!levelInfo.question.get(i).equals("?")){
				answer += levelInfo.question.get(i);
			}
			else{
				String optionString = levelInfo.options.get(optionIndexArray.get(j));
				j++;
				answer += optionString;
			}
		}
		//Gdx.app.log("evaluated string", answer);
		if(isExpressionZero(answer)) return true;
		else return false;
	}
	
	private void setOptionClickEvent(){
		for(int i=0;i<optionsButtonArray.size;i++){
			optionsButtonArray.get(i).addListener(new ClickListener(){
				@Override
				public void clicked (InputEvent event, float x, float y) {
					chooseOption.play();
					Integer optionButtonIdx = Integer.valueOf(event.getListenerActor().getName());
					// Find empty index slot
					Integer optionEmptyIdx = findEmptyIdx(optionIndexArray);
					if(optionEmptyIdx == -1) return;
					// Fill it with the option index
					optionIndexArray.set(optionEmptyIdx, optionButtonIdx);
					
					//Do the necessary modifications for the question button
					TextButton questionButton = questionButtonArray.get(questionIndexArray.get(optionEmptyIdx));
					questionButton.setText(levelInfo.options.get(optionButtonIdx));
					questionButton.setTouchable(Touchable.enabled);
					//Do the necessary changes for the option button
					TextButton optionButton = optionsButtonArray.get(optionButtonIdx);
					optionButton.setVisible(false);
					
					if(isAllEmptyIdxFull(optionIndexArray)){
						if(isAnswerCorrect()){
							rightAnswer.play();
							//layer2.setScale(0);
							//continueButton.setSize(0, 0);
							Color c = awesomeLabel.getColor();
							awesomeLabel.setColor(c.r, c.g, c.b, 0);
							c = continueButton.getColor();
							continueButton.setColor(c.r, c.g, c.b, 0);
							blackImage.setScale(1,.1f);
							//blackImage.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
							layer2.setVisible(true);
							//awesomeTable.addAction(sizeTo(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 2));
							blackImage.addAction(scaleTo(1, 1, .5f));
							awesomeLabel.addAction(sequence(delay(.5f),alpha(1, .5f)));
							continueButton.addAction(sequence(delay(.5f),alpha(1, .5f)));
						}
						else{
							wrongAnswer.play();
						}
					}
				}
			});
		}
	}
	private Integer findFilledIdx(Array<Integer> questionIndexArray, Integer idx){
		for(int i=0;i<questionIndexArray.size;i++){
			if(questionIndexArray.get(i) == idx) return i;
		}
		return -1;
	}
	
	private void setQuestionClickEvent(){
		for(int i=0;i<questionButtonArray.size;i++){
			questionButtonArray.get(i).addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					revertOption.play();
					Integer questionButtonIdx = Integer.valueOf(event.getListenerActor().getName());
					// Find the pressed question button idx
					Integer questionFilledIdx = findFilledIdx(questionIndexArray, questionButtonIdx);
					Integer optionButtonIdx = optionIndexArray.get(questionFilledIdx);
					optionIndexArray.set(questionFilledIdx, -1);
					//Do the necessary changes for question button
					TextButton questionButton = questionButtonArray.get(questionButtonIdx);
					questionButton.setText("?");
					questionButton.setTouchable(Touchable.disabled);
					//Do the necessary changes for option button
					optionsButtonArray.get(optionButtonIdx).setVisible(true);
				}
			});
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Table.drawDebug(stage);//TBR
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// http://www.badlogicgames.com/wordpress/?p=3322
		stage.getViewport().update(width, height, true);
		layerStack.invalidateHierarchy();
	}
	
	private Actor createLayer1(){
		Image image = new Image(skin, "smoke_f5f5f5");
		image.setScaling(Scaling.fill);

		Table table = new Table();
		table.setFillParent(true); // Enables resizing the table when window is resized
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Initialise the indexes
		questionButtonArray = createQuestionButtons(levelInfo.question);
		optionsButtonArray = createOptionsButton(levelInfo.options);
		initIndexes(levelInfo.question);
		setOptionClickEvent();
		setQuestionClickEvent();
		
		int numColumns = 9;
		Label empty = new Label(" ", skin, "large");
		for(int i=0;i<numColumns-1;i++){
			table.add(empty).expand().uniform();
		}
		table.add(empty).expand().uniform().row();
		
		//table.setBackground(skin., true);	
		fillTopMenu(numColumns, table);
		table.row();
		table.add(empty).colspan(numColumns).expand().row();
		table.add(empty).colspan(numColumns).expand().row();
		table.add(empty).colspan(numColumns).expand().row();
		createQuestionTable(numColumns, table, questionButtonArray);
		table.row();
		table.add(empty).colspan(numColumns).expand().row();
		table.add(empty).colspan(numColumns).expand().row();
		table.add(empty).colspan(numColumns).expand().row();
		createOptionsTable(numColumns, table, optionsButtonArray);
		table.debug(); // TO DO Remove this debug
		
		/*Stack stack = new Stack();
		stack.add(image);
		stack.add(table);*/
		return table;
	}
	
	private Actor createLayer2(){
		blackImage = new Image(skin, "black_transparent");
		blackImage.setScaling(Scaling.fill);

		Table awesomeTable = new Table();
		awesomeLabel= new Label("AWESOME", skin, "large_white");
		awesomeLabel.setColor((float) (110.0/255.0), (float) (164.0/255.0), (float) (36.0/255.0), 1);
		awesomeLabel.setFontScale(0.75f);
		continueButton = new TextButton("CONTINUE", skin, "normal_orange");
		continueButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				layer2.setVisible(false);
				//layer3.setVisible(false);
				player.levelUpdate(4);
				if(LevelGenerator.getLevelInfo(player.getPlayerInfo().levelNum) == null){
					((Game) Gdx.app.getApplicationListener()).setScreen(new Congratulation());
				}
				else{
					dispose();
					show();
					continueGame.play();
				}				
			}
		});
		
		/*Image starImage1 = new Image(skin, "gold_star");
		starImage1.setScale(.2f);

		Image starImage = new Image(skin, "gold_star");		
		starImage.setPosition(-1*Gdx.graphics.getWidth()/2, -1*Gdx.graphics.getHeight()/2);
		starImage.setCenterPosition(-1*Gdx.graphics.getWidth()/2, -1*Gdx.graphics.getHeight()/2);
		starImage.setX(Gdx.graphics.getWidth()/2);
		starImage.setY(Gdx.graphics.getHeight()/2);
		//starImage.set
		starImage.setScale(.2f);
		System.out.println("starImage "+starImage.getCenterX() + " "+starImage.getCenterY()+" "+starImage.getHeight()+ " "+starImage.getWidth()+
				" "+starImage.getImageX()+" "+starImage.getImageY());*/
		
		
		awesomeTable.add(awesomeLabel).expand();
		awesomeTable.row();
		awesomeTable.add(continueButton.pad(5)).expand();
		awesomeTable.setFillParent(true);
		awesomeTable.debug();
		
		

		
		Stack stack = new Stack();
		stack.add(blackImage);
		stack.add(awesomeTable);
		//stack.add(starImage);
		stack.setVisible(false);
		stack.setFillParent(true);
		return stack;		
	}
	
	@Override
	public void show() {
		stage = new Stage();
		chooseOption = Gdx.audio.newSound(Gdx.files.internal("sound/choose_option.mp3"));
		revertOption = Gdx.audio.newSound(Gdx.files.internal("sound/revert_option.mp3"));
		wrongAnswer = Gdx.audio.newSound(Gdx.files.internal("sound/242503__gabrielaraujo__failure-wrong-action.wav"));
		rightAnswer = Gdx.audio.newSound(Gdx.files.internal("sound/109662__grunz__success.wav"));
		continueGame = Gdx.audio.newSound(Gdx.files.internal("sound/215773__otisjames__win.mp3"));
		player = new Player();
		//Gdx.app.log("Application pointers", levelGenerator.toString() + player.getPlayerInfo().toString());
		levelInfo = LevelGenerator.getLevelInfo(player.getPlayerInfo().levelNum);
		
		Gdx.input.setInputProcessor(stage);
		
		String skinFilename = "ui/skin_small.json";
		if(Gdx.graphics.getHeight() >= (1280+640)/2 && Gdx.graphics.getWidth() >= (720+360)/2){
			skinFilename = "ui/skin_large.json";
		}
		skin = new Skin(Gdx.files.internal(skinFilename), new TextureAtlas("ui/textureAtlas.pack"));

		if(levelInfo == null){
			((Game) Gdx.app.getApplicationListener()).setScreen(new Congratulation());
			return;
		}
		
		layerStack = new Stack();
		layerStack.setFillParent(true); // Enables resizing the table when window is resized
		layerStack.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		layerStack.add(createLayer1());
		layer2 = createLayer2();
		layerStack.add(layer2);

		stage.addActor(layerStack);
		//evaluateMathExpression();
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
		// atlas.dispose(); skin disposes atlas also
		skin.dispose();
		chooseOption.dispose();
		revertOption.dispose();
		wrongAnswer.dispose();
		rightAnswer.dispose();
		continueGame.dispose();
	}


}
