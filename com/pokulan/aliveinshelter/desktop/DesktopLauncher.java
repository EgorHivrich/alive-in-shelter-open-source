/*     */ package com.pokulan.aliveinshelter.desktop;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
/*     */ import com.pokulan.aliveinshelter.AdHandler;
/*     */ import com.pokulan.aliveinshelter.AdHandler2;
/*     */ import com.pokulan.aliveinshelter.AliveInShelter;
/*     */ import com.pokulan.aliveinshelter.IabInterface;
/*     */ import com.pokulan.aliveinshelter.PlayServices;
/*     */ import com.pokulan.aliveinshelter.addMoney;
/*     */ import com.pokulan.aliveinshelter.aktualka;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DesktopLauncher
/*     */ {
/*  43 */   static String IP = "188.252.42.80";
/*  44 */   static String[] languages = new String[] { "en", "pl", "ru", "pt", "es", "it", "de", "cs", "fr", "tr", "zh", "hu", "ko", "id" };
/*     */   
/*     */   public static void main(String[] arg) {
/*  47 */     AdHandler adHandler = new AdHandler()
/*     */       {
/*     */         public void showAds(boolean show) {}
/*     */       };
/*     */ 
/*     */     
/*  53 */     AdHandler2 adHandler2 = new AdHandler2()
/*     */       {
/*     */         public void showAdsFull(boolean show, int a) {}
/*     */       };
/*     */ 
/*     */     
/*  59 */     IabInterface iabInterface = new IabInterface()
/*     */       {
/*     */         public void removeAds(int a) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void processPurchases() {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean removed() {
/*  72 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isPremium() {
/*  77 */           return true;
/*     */         }
/*     */       };
/*  80 */     aktualka akt = new aktualka()
/*     */       {
/*     */         public String wersja() {
/*  83 */           return null;
/*     */         }
/*     */       };
/*  86 */     addMoney money = new addMoney()
/*     */       {
/*     */         public void showAds3(boolean show) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void showAds4(boolean show) {}
/*     */       };
/*  97 */     PlayServices playServices = new PlayServices()
/*     */       {
/*     */         public void signIn() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void signOut() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void rateGame() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void unlockAchievement(int i) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void submitScore(int s1, int s2, boolean hard) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void showAchievement() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void showScore(int s1, int hard) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean isSignedIn() {
/* 128 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean copyKey() {
/* 133 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public String playerID() {
/* 138 */           return "NOT_LOGGED_TO_GOOGLE";
/*     */         }
/*     */ 
/*     */         
/*     */         public String getFromServer() {
/*     */           try {
/* 144 */             URL url = new URL("http://" + DesktopLauncher.IP + "/keys.txt");
/* 145 */             URLConnection urlConn = url.openConnection();
/* 146 */             urlConn.setDoOutput(true);
/* 147 */             urlConn.setConnectTimeout(1500);
/*     */ 
/*     */             
/* 150 */             BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
/*     */             String str;
/* 152 */             if ((str = in.readLine()) != null)
/*     */             {
/* 154 */               return str;
/*     */             }
/* 156 */             in.close();
/* 157 */           } catch (IOException e) {
/* 158 */             return "x";
/*     */           } 
/*     */           
/* 161 */           return "x";
/*     */         }
/*     */ 
/*     */         
/*     */         public int dodajCoinyPoReinstallacji() {
/* 166 */           return 0;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void startQuickGame() {}
/*     */ 
/*     */         
/*     */         public String getFromServerChangelog() {
/*     */           try {
/* 176 */             URL url = new URL("http://" + DesktopLauncher.IP + "/AIS_changelog.txt");
/* 177 */             URLConnection urlConn = url.openConnection();
/* 178 */             urlConn.setDoOutput(true);
/* 179 */             urlConn.setConnectTimeout(1700);
/*     */ 
/*     */             
/* 182 */             BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
/*     */             String str;
/* 184 */             if ((str = in.readLine()) != null)
/*     */             {
/* 186 */               return str;
/*     */             }
/* 188 */             in.close();
/* 189 */           } catch (IOException e) {
/* 190 */             return "x";
/*     */           } 
/*     */           
/* 193 */           return "x";
/*     */         }
/*     */ 
/*     */         
/*     */         public String getFromServerDlc() {
/* 198 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void copyToClip(String txt) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void isNoCheater() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void isExtraPlayer() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public long getMillis() {
/* 217 */           return System.currentTimeMillis();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void uprawnienia(String permission, Integer requestCode) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void scanMedia(String path) {}
/*     */ 
/*     */         
/*     */         public boolean isUprawnienie(String permission, Integer requestCode) {
/* 230 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void setExternalDLC(boolean dlc) {}
/*     */ 
/*     */         
/*     */         public String getChat(int jaki) {
/*     */           try {
/* 240 */             URL url = new URL("http://" + DesktopLauncher.IP + "/php_AIS/chat" + jaki + ".txt");
/* 241 */             URLConnection urlConn = url.openConnection();
/* 242 */             urlConn.setDoOutput(true);
/* 243 */             urlConn.setConnectTimeout(1700);
/*     */ 
/*     */             
/* 246 */             BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
/*     */             String str;
/* 248 */             if ((str = in.readLine()) != null) {
/* 249 */               return str;
/*     */             }
/* 251 */             in.close();
/* 252 */           } catch (IOException e) {
/* 253 */             return "x";
/*     */           } 
/*     */           
/* 256 */           return "SERVER#@#No data#@#2#@#0";
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void putChat(String nick, String msg, int jaki, int kolor) {}
/*     */ 
/*     */         
/*     */         public String getNick() {
/* 265 */           return "NOT_LOGGED_TO_GOOGLE";
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void zapiszOnline() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void wczytajOnline() {}
/*     */ 
/*     */         
/*     */         public String getFriendNick(String ID, int jaki) {
/* 278 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void getOnlineActions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void putAction(String friendID, String co, int tryb) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void shareString(String string) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean checkLuckyPatcher() {
/* 298 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void goHome() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void startBackground() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public int systemLanguage() {
/* 313 */           String shortName = Locale.getDefault().getLanguage();
/* 314 */           for (int i = 0; i < 13; i++) {
/* 315 */             if (DesktopLauncher.languages[i].equals(shortName)) return i; 
/*     */           } 
/* 317 */           return 0;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void survey(boolean a) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public String read18DLC() {
/* 327 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void end18() {}
/*     */ 
/*     */ 
/*     */         
/*     */         public String getDeviceName() {
/* 337 */           return "";
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean is18() {
/* 342 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isMoon() {
/* 347 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public String[] saveExists(String file) {
/* 352 */           String main_path = System.getProperty("user.home") + "";
/*     */           
/* 354 */           String sciezka = main_path + File.separator + file + ".ais";
/* 355 */           File path = new File(sciezka);
/*     */           
/* 357 */           File directory = new File(main_path);
/* 358 */           File[] fList = directory.listFiles();
/*     */           
/* 360 */           String[] saves = { main_path, "---", "---", "---" };
/*     */           
/*     */           try {
/* 363 */             if (path.exists() && !path.isDirectory())
/* 364 */             { saves[1] = file + "-AUTOSAVE.ais"; }
/* 365 */             else { saves[1] = "---"; } 
/* 366 */           } catch (Exception e) {
/* 367 */             saves[1] = "---";
/*     */           } 
/*     */           
/* 370 */           int i = 0;
/* 371 */           if (fList != null) {
/* 372 */             for (File f : fList) {
/* 373 */               if (f.isFile() && 
/* 374 */                 f.getName().startsWith("AIS-SAV") && !f.getName().equals("AIS-SAVE.ais")) {
/* 375 */                 if (i == 0) i = 2; 
/* 376 */                 saves[i] = f.getName();
/* 377 */                 i++;
/* 378 */                 if (i >= 4) {
/*     */                   break;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */           
/* 385 */           return saves;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void saveMap(Object obj, String file2, String file_old) {
/* 399 */           File path = new File(System.getProperty("user.home") + "");
/*     */           
/* 401 */           if (!path.exists() && !path.isDirectory())
/*     */           {
/*     */             
/* 404 */             if (path.mkdirs());
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 416 */             File file = new File(path, file2);
/* 417 */             FileOutputStream stream = new FileOutputStream(file);
/* 418 */             ObjectOutputStream objectOut = new ObjectOutputStream(stream);
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 423 */               objectOut.writeObject(obj);
/*     */             } finally {
/* 425 */               stream.close();
/* 426 */               objectOut.close();
/* 427 */               if (!file_old.equals("x")) {
/* 428 */                 File fileO = new File(path, file_old);
/* 429 */                 fileO.delete();
/*     */               } 
/*     */             } 
/* 432 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public Object loadMap(String file2) {
/* 439 */           File path = new File(System.getProperty("user.home") + "");
/*     */           
/* 441 */           if (!path.exists() && !path.isDirectory())
/*     */           {
/*     */             
/* 444 */             if (path.mkdirs());
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 456 */             File file = new File(path, file2);
/*     */             
/* 458 */             FileInputStream fi = new FileInputStream(file);
/* 459 */             ObjectInputStream oi = new ObjectInputStream(fi);
/* 460 */             Map<String, ?> mapaDanych = new HashMap<String, Object>();
/*     */             
/* 462 */             mapaDanych = (HashMap)oi.readObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 476 */             try { return mapaDanych; }
/*     */             finally { Exception exception = null; oi.close(); fi.close(); }
/*     */           
/* 479 */           } catch (Exception e) {
/* 480 */             return "none";
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public String getDateSave() {
/* 486 */           Calendar c = Calendar.getInstance();
/* 487 */           int year = c.get(1);
/* 488 */           int month = c.get(2);
/* 489 */           int day = c.get(5);
/* 490 */           int h = c.get(10);
/* 491 */           int m = c.get(12);
/* 492 */           int s = c.get(13);
/* 493 */           String out = year + "-" + month + "-" + day + "-" + h + "-" + m + "-" + s;
/*     */           
/* 495 */           return out;
/*     */         }
/*     */ 
/*     */         
/*     */         public String wikiDate() {
/* 500 */           return null;
/*     */         }
/*     */ 
/*     */         
/*     */         public Vector<String>[] getWikiPages(String from, String end) {
/* 505 */           return (Vector<String>[])new Vector[0];
/*     */         }
/*     */       };
/* 508 */     int width = 0;
/* 509 */     int height = 0;
/* 510 */     System.out.println("Working Directory = " + System.getProperty("user.dir"));
/*     */     
/*     */     try {
/* 513 */       FileInputStream fstream = new FileInputStream("ais_conf.cfg");
/* 514 */       BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
/*     */       
/*     */       String strLine;
/*     */       
/* 518 */       while ((strLine = br.readLine()) != null) {
/* 519 */         String[] parts = strLine.split(":");
/* 520 */         if (parts[0].contains("w")) {
/* 521 */           width = (int)Double.parseDouble(parts[1]);
/*     */         }
/* 523 */         if (parts[0].contains("h")) {
/* 524 */           height = (int)Double.parseDouble(parts[1]);
/*     */         }
/* 526 */         System.out.println(strLine);
/*     */       } 
/*     */ 
/*     */       
/* 530 */       fstream.close();
/* 531 */     } catch (Exception e) {
/* 532 */       System.err.println("Error: " + e.getMessage());
/* 533 */       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/*     */ 
/*     */       
/*     */       try {
/* 537 */         FileWriter fstream = new FileWriter("ais_conf.cfg");
/* 538 */         BufferedWriter out = new BufferedWriter(fstream);
/* 539 */         out.write("h:" + screenSize.getHeight() + "\n");
/* 540 */         out.write("w:" + screenSize.getWidth() + "\n");
/* 541 */         width = (int)screenSize.getWidth();
/* 542 */         height = (int)screenSize.getHeight();
/*     */         
/* 544 */         out.close();
/*     */         
/* 546 */         Runtime.getRuntime().exec("java -jar desktop-1.0.jar");
/* 547 */         System.exit(0);
/* 548 */       } catch (Exception ee) {
/* 549 */         System.err.println("Error: " + ee.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 553 */     LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
/* 554 */     cfg.title = "Alive In Shelter";
/* 555 */     cfg.allowSoftwareMode = true;
/* 556 */     cfg.foregroundFPS = 60;
/* 557 */     cfg.resizable = false;
/* 558 */     cfg.vSyncEnabled = true;
/* 559 */     System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
/*     */ 
/*     */     
/* 562 */     cfg.height = height;
/* 563 */     cfg.width = width;
/* 564 */     cfg.fullscreen = true;
/* 565 */     System.out.println("WIDTH: " + width);
/* 566 */     System.out.println("HEIGH: " + height);
/*     */     
/* 568 */     cfg.addIcon("ic_launcher.png", Files.FileType.Internal);
/* 569 */     cfg.addIcon("ic_launcher_128.png", Files.FileType.Internal);
/* 570 */     cfg.addIcon("ic_launcher_16.png", Files.FileType.Internal);
/* 571 */     new LwjglApplication((ApplicationListener)new AliveInShelter(true, adHandler, adHandler2, playServices, iabInterface, akt, money), cfg);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\desktop\DesktopLauncher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */