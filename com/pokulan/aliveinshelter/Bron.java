/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Bron
/*    */ {
/*    */   int dmg;
/*    */   int maxDmg;
/*    */   int procent;
/*    */   boolean zuzywalne;
/*    */   int stan;
/*    */   int stopien;
/*    */   boolean jest;
/*    */   String name;
/*    */   AliveInShelter aliveinshelter;
/*    */   
/*    */   Bron(int dmg, int maxDmg, int procent, boolean zuzywalne, int stopien, AliveInShelter aliveinshelter) {
/* 18 */     this.aliveinshelter = aliveinshelter;
/* 19 */     this.dmg = dmg;
/* 20 */     this.maxDmg = maxDmg;
/* 21 */     this.zuzywalne = zuzywalne;
/* 22 */     this.procent = procent;
/* 23 */     this.stan = 100;
/* 24 */     if (zuzywalne) {
/* 25 */       this.stopien = stopien;
/*    */     } else {
/* 27 */       stopien = 0;
/*    */     } 
/* 29 */     this.jest = false;
/* 30 */     this.name = "";
/*    */   }
/*    */   
/*    */   void fix() {
/* 34 */     this.stan = 100;
/*    */   }
/*    */   void save(int lp) {
/* 37 */     this.aliveinshelter.prefs.putInteger("BRONdmg" + lp, this.dmg);
/* 38 */     this.aliveinshelter.prefs.putInteger("BRONmaxDmg" + lp, this.maxDmg);
/* 39 */     this.aliveinshelter.prefs.putInteger("BRONprocent" + lp, this.procent);
/* 40 */     this.aliveinshelter.prefs.putInteger("BRONstan" + lp, this.stan);
/* 41 */     this.aliveinshelter.prefs.putInteger("BRONstopien" + lp, this.stopien);
/* 42 */     this.aliveinshelter.prefs.putBoolean("BRONjest" + lp, this.jest);
/* 43 */     this.aliveinshelter.prefs.putBoolean("BRONzuzywalne" + lp, this.zuzywalne);
/* 44 */     this.aliveinshelter.prefs.putString("BRONname" + lp, this.name);
/* 45 */     this.aliveinshelter.prefs.flush();
/*    */   }
/*    */   void load(int lp) {
/* 48 */     this.dmg = this.aliveinshelter.prefs.getInteger("BRONdmg" + lp, 0);
/* 49 */     this.maxDmg = this.aliveinshelter.prefs.getInteger("BRONmaxDmg" + lp, 0);
/* 50 */     this.procent = this.aliveinshelter.prefs.getInteger("BRONprocent" + lp, 0);
/* 51 */     this.stan = this.aliveinshelter.prefs.getInteger("BRONstan" + lp, 0);
/* 52 */     this.stopien = this.aliveinshelter.prefs.getInteger("BRONstopien" + lp, 0);
/* 53 */     this.jest = this.aliveinshelter.prefs.getBoolean("BRONjest" + lp, false);
/* 54 */     this.zuzywalne = this.aliveinshelter.prefs.getBoolean("BRONzuzywalne" + lp, false);
/* 55 */     this.name = this.aliveinshelter.prefs.getString("BRONname" + lp, "NULL");
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Bron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */