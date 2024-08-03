/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Maska
/*    */ {
/*    */   int stan;
/*    */   int maxStan;
/*    */   int cena;
/*    */   Texture textureFull;
/*    */   Texture textureLow;
/*    */   Texture textureRoom;
/*    */   Texture textureOn;
/*    */   Texture blank;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   
/*    */   public Maska(Texture textureFull, Texture textureLow, Texture textureRoom, int maxStan, boolean dlc, int cena, Texture textureOn, Texture blank) {
/* 21 */     this.textureFull = textureFull;
/* 22 */     this.textureLow = textureLow;
/* 23 */     this.textureRoom = textureRoom;
/* 24 */     this.textureOn = textureOn;
/* 25 */     this.cena = cena;
/* 26 */     this.maxStan = maxStan;
/* 27 */     this.stan = maxStan;
/* 28 */     this.dlc = dlc;
/* 29 */     this.kupiony = false;
/* 30 */     this.blank = blank;
/*    */   }
/*    */   
/* 33 */   public Texture getTextureFull() { return this.textureFull; }
/* 34 */   public Texture getTextureLow() { return this.textureLow; } public Texture getTextureRoom() {
/* 35 */     return this.textureRoom;
/*    */   } public Texture getTextureOn() {
/* 37 */     if (this.stan > 0) return this.textureOn; 
/* 38 */     return this.blank;
/*    */   }
/* 40 */   public boolean isDlc() { return this.dlc; }
/* 41 */   public boolean isKupiony() { return this.kupiony; }
/* 42 */   public int getStan() { return this.stan; }
/* 43 */   public int getMaxStan() { return this.maxStan; }
/* 44 */   public int getCena() { return this.cena; }
/* 45 */   public void setKupiony(boolean kupiony) { this.kupiony = kupiony; } public void setStanMax() {
/* 46 */     this.stan = this.maxStan;
/*    */   } public void setStanNull() {
/* 48 */     this.stan = 0;
/*    */   }
/*    */   public void setStan(int ile) {
/* 51 */     this.stan = ile;
/* 52 */     if (this.stan > this.maxStan) { this.stan = this.maxStan; }
/* 53 */     else if (this.stan < 0) { this.stan = 0; }
/*    */   
/*    */   } public void addStan(int add) {
/* 56 */     this.stan += add;
/* 57 */     if (this.stan > this.maxStan) { this.stan = this.maxStan; }
/* 58 */     else if (this.stan < 0) { this.stan = 0; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Maska.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */