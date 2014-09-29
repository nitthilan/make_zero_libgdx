package com.makezero.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.makezero.models.PlayerInfo;

public class Player {
	static private PlayerInfo playerInfo;
	private static String LevelNum = "LevelNum";
	private static String NumGoldCoins = "NumGoldCoins";
	private PlayerInfo createPlayerInfo(){
		PlayerInfo playerInfo =  new PlayerInfo();
		playerInfo.prefs = Gdx.app.getPreferences("PlayerInfo");
		if(!playerInfo.prefs.contains(LevelNum)) {
			playerInfo.prefs.putInteger(LevelNum, 0);
		}
		if(!playerInfo.prefs.contains(NumGoldCoins)) {
			playerInfo.prefs.putInteger(NumGoldCoins, 0);
		}
		playerInfo.prefs.flush();
		return playerInfo;
	}
	
	public void resetPlayerInfo(){
		// Comment the below for making it permanent
		playerInfo.prefs.putInteger(LevelNum, 0);
		playerInfo.prefs.putInteger(NumGoldCoins, 0);
		playerInfo.prefs.flush();
	}
	
	public Player(){
		if(playerInfo == null)	playerInfo = createPlayerInfo();
		//.app.log("Application pointers", playerInfo.toString());
	}
	
	public PlayerInfo getPlayerInfo(){
		playerInfo.levelNum = playerInfo.prefs.getInteger(LevelNum);
		playerInfo.numGoldCoins = playerInfo.prefs.getInteger(NumGoldCoins);		
		return playerInfo;
	}
	public void levelUpdate(Integer numGoldCoins){
		playerInfo.numGoldCoins += numGoldCoins;
		playerInfo.levelNum++;
		playerInfo.prefs.putInteger(LevelNum, playerInfo.levelNum);
		playerInfo.prefs.putInteger(NumGoldCoins, playerInfo.numGoldCoins);
		playerInfo.prefs.flush();
	}


}
