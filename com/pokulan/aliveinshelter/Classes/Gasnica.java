/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Gasnica
/*    */ {
/*    */   int stan;
/*    */   int maxStan;
/*    */   int cena;
/*    */   Texture textureFull;
/*    */   boolean dlc;
/*    */   boolean danie;
/*    */   boolean kupiony;
/*    */   
/*    */   public Gasnica(Texture textureFull, int maxStan, boolean dlc, int cena) {
/* 18 */     this.textureFull = textureFull;
/* 19 */     this.cena = cena;
/* 20 */     this.maxStan = maxStan;
/* 21 */     this.stan = maxStan;
/* 22 */     this.dlc = dlc;
/* 23 */     this.kupiony = false;
/* 24 */     this.danie = false;
/*    */   }
/*    */   
/* 27 */   public Texture getTextureFull() { return this.textureFull; }
/* 28 */   public boolean isDlc() { return this.dlc; }
/* 29 */   public boolean isKupiony() { return this.kupiony; }
/* 30 */   public int getStan() { return this.stan; }
/* 31 */   public int getMaxStan() { return this.maxStan; }
/* 32 */   public int getCena() { return this.cena; }
/* 33 */   public void setKupiony(boolean kupiony) { this.kupiony = kupiony; }
/* 34 */   public void setStanMax() { this.stan = this.maxStan; }
/* 35 */   public void setStanNull() { this.stan = 0; }
/* 36 */   public void setDanie(boolean danie) { this.danie = danie; } public boolean getDanie() {
/* 37 */     return this.danie;
/*    */   } public void setStan(int ile) {
/* 39 */     this.stan = ile;
/* 40 */     if (this.stan > this.maxStan) { this.stan = this.maxStan; }
/* 41 */     else if (this.stan < 0) { this.stan = 0; }
/*    */   
/*    */   } public void addStan(int add) {
/* 44 */     this.stan += add;
/* 45 */     if (this.stan > this.maxStan) { this.stan = this.maxStan; }
/* 46 */     else if (this.stan < 0) { this.stan = 0; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Gasnica.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */