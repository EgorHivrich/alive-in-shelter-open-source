/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Meter
/*    */ {
/*    */   int stan;
/*    */   int maxStan;
/*    */   int cena;
/*    */   Texture textureOK;
/*    */   Texture textureBroken;
/*    */   Texture textureRoom;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   
/*    */   public Meter(Texture textureFull, Texture textureLow, Texture textureRoom, int maxStan, boolean dlc, int cena) {
/* 19 */     this.textureOK = textureFull;
/* 20 */     this.textureBroken = textureLow;
/* 21 */     this.textureRoom = textureRoom;
/* 22 */     this.cena = cena;
/* 23 */     this.maxStan = maxStan;
/* 24 */     this.stan = maxStan;
/* 25 */     this.dlc = dlc;
/* 26 */     this.kupiony = false;
/*    */   }
/*    */   
/* 29 */   public Texture getTextureFull() { return this.textureOK; }
/* 30 */   public Texture getTextureLow() { return this.textureBroken; }
/* 31 */   public Texture getTextureRoom() { return this.textureRoom; }
/* 32 */   public boolean isDlc() { return this.dlc; }
/* 33 */   public boolean isKupiony() { return this.kupiony; }
/* 34 */   public int getStan() { return this.stan; }
/* 35 */   public int getMaxStan() { return this.maxStan; }
/* 36 */   public int getCena() { return this.cena; }
/* 37 */   public void setKupiony(boolean kupiony) { this.kupiony = kupiony; }
/* 38 */   public void setStanMax(int losowa) { this.stan = this.maxStan - losowa; } public void setStanNull() {
/* 39 */     this.stan = 0;
/*    */   } public void setStan(int ile) {
/* 41 */     this.stan = ile;
/* 42 */     if (this.stan > this.maxStan) { this.stan = this.maxStan; }
/* 43 */     else if (this.stan < 0) { this.stan = 0; }
/*    */   
/*    */   } public void addStan(int add) {
/* 46 */     this.stan += add;
/* 47 */     if (this.stan > this.maxStan) { this.stan = this.maxStan; }
/* 48 */     else if (this.stan < 0) { this.stan = 0; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Meter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */