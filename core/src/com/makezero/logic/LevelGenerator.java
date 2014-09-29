package com.makezero.logic;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.makezero.models.LevelInfo;

public class LevelGenerator {
	static private Array<String> validOptions = new Array<String>(new String[]{"1","2","3","4","5","6","7","8","9","0","-","+","/","*","(",")","{","}","[","]"});
	static private Array<String> levelStrings;
	
	private LevelGenerator(){

	}	
	

	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	static public LevelInfo getLevelInfoFromString(Integer levelNum){
		LevelInfo levelInfo = new LevelInfo();
		levelInfo.question = new Array<String>();
		levelInfo.answer = new Array<String>();
		String levelString = levelStrings.get(levelNum);
		String[] splitString = levelString.split("");
		for(int i=0;i<splitString.length;i++){
			if(!splitString[i].equals("")){
				levelInfo.question.add(splitString[i]);
			}
			if(splitString[i].equals("?")){
				levelInfo.answer.add(splitString[i+1]);
				i++;
			}
		}
		//Gdx.app.log("question", "Level info "+levelInfo.question.toString());
		//Gdx.app.log("question", "Level String " +levelString);
		//Gdx.app.log("answer", levelInfo.answer.toString());
		return levelInfo;
	}
	
	static public LevelInfo getLevelInfo(Integer levelNum){
		if(levelStrings == null) createLevelInfoStrings();
		//if(levelInfoList == null) levelInfoList = createLevelInfoList();
		
		if(levelNum>=levelStrings.size) return null;
		Integer maxOptions = 14;
		LevelInfo levelInfo = getLevelInfoFromString(levelNum);//createLevelInfoList().get(levelNum);//
		levelInfo.options = new Array<String>(maxOptions);
		for(int i=0;i<maxOptions;i++){
			levelInfo.options.add("");				
		}
		for(int i=0;i<levelInfo.answer.size;i++){
			Integer pos = randInt(0,maxOptions-1);
			while(levelInfo.options.get(pos) != ""){
				pos = (pos+1)%maxOptions;
			}
			levelInfo.options.set(pos, levelInfo.answer.get(i));
		}
		//Gdx.app.log("Created Array options", levelInfo.options.toString());
		
		for(int i=0;i<maxOptions;i++){
			if(levelInfo.options.get(i)==""){
				levelInfo.options.set(i, validOptions.get(randInt(0, validOptions.size-1)));
			}
			
		}
		//Gdx.app.log("Created Array options", levelInfo.options.toString());
		
		return levelInfo;
	}
	
	static public int getTotalLevels(){
		return levelStrings.size;
	}
	
	static private Array<LevelInfo> createLevelInfoList(){
		Array<LevelInfo> lil = new Array<LevelInfo>();
		
		LevelInfo li = new LevelInfo();
		li.question = new Array<String>(new String[] {"1","+","1","?","2"});
		li.answer = new Array<String>(new String[] {"-"});
		lil.add(li);
		
		li = new LevelInfo();
		li.question = new Array<String>(new String[] {"9","?","2","?","1","1"});
		li.answer = new Array<String>(new String[] {"-","+"});
		lil.add(li);
		
		li = new LevelInfo();
		li.question = new Array<String>(new String[] {"7","+","?","-","7"});
		li.answer = new Array<String>(new String[] {"0"});
		lil.add(li);
		
		li = new LevelInfo();
		li.question = new Array<String>(new String[] {"9","-","?","+","3"});
		li.answer = new Array<String>(new String[] {"-","2"});
		lil.add(li);
		
		li = new LevelInfo();
		li.question = new Array<String>(new String[] {"1","?","5","?","4"});
		li.answer = new Array<String>(new String[] {"-","+"});
		lil.add(li);
		
		li = new LevelInfo();
		li.question = new Array<String>(new String[] {"0","?","4","?","4"});
		li.answer = new Array<String>(new String[] {"+","-"});
		lil.add(li);
		return lil;
	}
	static private void createLevelInfoStrings(){
		levelStrings = new Array<String>();
		levelStrings.add("1+1?-2");
		levelStrings.add("9-11?+2");
		levelStrings.add("7?+0-7");
		levelStrings.add("9-?7+3-5");
		levelStrings.add("1?-5?+4");
		levelStrings.add("7?-8?+9?-8");
		levelStrings.add("9?-24?+6+9");
		levelStrings.add("9?+3+5?-7-10");
		levelStrings.add("7+6?-5?+4-9?-3");
		levelStrings.add("9-2?-3?-4");
		levelStrings.add("8-4+?1+2-7");
		levelStrings.add("6+7+8-3-9-?9");
		levelStrings.add("9+?7+2-10-8");
		levelStrings.add("?5+4-8-?1");
		levelStrings.add("4+?7-9-2");
		levelStrings.add("9-7?+5-7");
		levelStrings.add("9+8-?9-8");
		levelStrings.add("9+6-?0-15");
		levelStrings.add("6+1+?8+9-?2?4");
		levelStrings.add("2+?0+1+8-?1?1");
		levelStrings.add("9+6?+2+5-?2?2");
		levelStrings.add("3+2+?5+7-17");
		levelStrings.add("9+4+6+7-?1?0-16");
		levelStrings.add("4+8+3+4-9-?6-?4");
		levelStrings.add("7?+9+4+8?-2?8");
		levelStrings.add("5+9?+8+2+6-30");		
		levelStrings.add("1+2+9?+?1-7-6");
		levelStrings.add("7+?0+2+6+8-4-?0-2-?8-9");
		levelStrings.add("3+?7+1+6+3-?9-2-?4-5");
		levelStrings.add("6+7+?8+9+?6-1?-3-8-4?-2-18");
		levelStrings.add("3+1+6+?4+5-5-1-?6-5-2");
		levelStrings.add("9+1+2+?3+6-1-6-?2-8-4");
		levelStrings.add("1+3+?8+4+2-6?-9-3");
		levelStrings.add("?86+9-?95");
		levelStrings.add("?46+8-5?4");
		levelStrings.add("3?6+8-4?4");
		levelStrings.add("5?9+?1-?60");
		levelStrings.add("4?8+5-?53");
		levelStrings.add("?26+6-3?2");
		levelStrings.add("5?2+?41-9?3");
		levelStrings.add("3?6+13?-4?9");
		levelStrings.add("4?8+?41-8?9");
		levelStrings.add("44?+18?-?62");
		levelStrings.add("59?+33-?92");
		levelStrings.add("8?5+91?-1?76");
		levelStrings.add("?75+?86-?161");
		levelStrings.add("6?9+3?1-1?0?0");
		levelStrings.add("89+?97-1?86");
		levelStrings.add("86+2?5-1?11");
		levelStrings.add("8?7+46-1?33");
		levelStrings.add("9?1+9?9-1?90");
		levelStrings.add("55+3?1?-7?3-13");
		levelStrings.add("97?+86?-92?-91");
		levelStrings.add("83?+83-94?-72");
		levelStrings.add("6?3+12?-54-2?1");
		levelStrings.add("82?-36-28?-18");		
		levelStrings.add("92-44-48?+21+25?-10-11?-12-13");
		levelStrings.add("4?4-25-19?+15+9-?9-6-4-?5");
		/*levelStrings.add("5247+658-5905");
		levelStrings.add("5212-3659-1553");
		levelStrings.add("3543-1657-1886");
		levelStrings.add("4232+3798-8030");
		levelStrings.add("7825-2463-5362");
		levelStrings.add("7255+1929-9184");
		levelStrings.add("97841-12997-84844");
		levelStrings.add("27728+62436-90164");
		levelStrings.add("71862-14786-57076");
		levelStrings.add("12915+85263-98178");
		levelStrings.add("42441+30589-73030");*/
	}	

}
