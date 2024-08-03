package com.pokulan.aliveinshelter;

import java.util.Vector;

public interface PlayServices {
  void signIn();
  
  void signOut();
  
  void rateGame();
  
  void unlockAchievement(int paramInt);
  
  void submitScore(int paramInt1, int paramInt2, boolean paramBoolean);
  
  void showAchievement();
  
  void showScore(int paramInt1, int paramInt2);
  
  boolean isSignedIn();
  
  boolean copyKey();
  
  String playerID();
  
  String getFromServer();
  
  int dodajCoinyPoReinstallacji();
  
  void startQuickGame();
  
  String getFromServerChangelog();
  
  String getFromServerDlc();
  
  void copyToClip(String paramString);
  
  void isNoCheater();
  
  void isExtraPlayer();
  
  long getMillis();
  
  void uprawnienia(String paramString, Integer paramInteger);
  
  void scanMedia(String paramString);
  
  boolean isUprawnienie(String paramString, Integer paramInteger);
  
  void setExternalDLC(boolean paramBoolean);
  
  String getChat(int paramInt);
  
  void putChat(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  String getNick();
  
  void zapiszOnline();
  
  void wczytajOnline();
  
  String getFriendNick(String paramString, int paramInt);
  
  void getOnlineActions();
  
  void putAction(String paramString1, String paramString2, int paramInt);
  
  void shareString(String paramString);
  
  boolean checkLuckyPatcher();
  
  void goHome();
  
  void startBackground();
  
  int systemLanguage();
  
  void survey(boolean paramBoolean);
  
  String read18DLC();
  
  void end18();
  
  String getDeviceName();
  
  boolean is18();
  
  boolean isMoon();
  
  String[] saveExists(String paramString);
  
  void saveMap(Object paramObject, String paramString1, String paramString2);
  
  Object loadMap(String paramString);
  
  String getDateSave();
  
  String wikiDate();
  
  Vector<String>[] getWikiPages(String paramString1, String paramString2);
}


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\PlayServices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */