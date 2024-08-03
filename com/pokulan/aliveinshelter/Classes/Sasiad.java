/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ 
/*    */ public class Sasiad
/*    */ {
/*    */   String ID;
/*    */   String nick;
/*    */   boolean dlc;
/*  9 */   int[] statystyki = new int[27];
/*    */   
/*    */   boolean waiting;
/*    */   
/*    */   int zapraszaDoGry;
/*    */   
/*    */   public Sasiad() {
/* 16 */     for (int i = 0; i < 27; ) { this.statystyki[i] = 0; i++; }
/* 17 */      this.dlc = false;
/* 18 */     this.ID = "-";
/* 19 */     this.nick = "---";
/* 20 */     this.waiting = false;
/* 21 */     this.zapraszaDoGry = 0;
/*    */   }
/*    */   
/* 24 */   public String getID() { return this.ID; }
/* 25 */   public String getNick() { return this.nick; }
/* 26 */   public int getStat(int i) { return this.statystyki[i]; }
/* 27 */   public boolean getDLC() { return this.dlc; }
/* 28 */   public int getZaprasz() { return this.zapraszaDoGry; }
/* 29 */   public boolean getWaiting() { return this.waiting; }
/* 30 */   public void setID(String id) { this.ID = id; }
/* 31 */   public void setNick(String nc) { this.nick = nc; }
/* 32 */   public void setStat(int i, int co) { this.statystyki[i] = co; }
/* 33 */   public void setDLC(boolean nc) { this.dlc = nc; }
/* 34 */   public void setWaiting(boolean nc) { this.waiting = nc; }
/* 35 */   public void setZaprasza(int zapr) { this.zapraszaDoGry = zapr; } public boolean is() {
/* 36 */     return !this.nick.equals("---");
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Sasiad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */