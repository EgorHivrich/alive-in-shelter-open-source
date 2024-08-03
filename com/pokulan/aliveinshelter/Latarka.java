/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.Sprite;
/*    */ 
/*    */ public class Latarka {
/*    */   int cena;
/*    */   int szansa;
/*    */   Texture testureShelter;
/*    */   Texture textureOn;
/*    */   Texture textureRoom;
/*    */   Sprite textureSignal;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   float style;
/*    */   
/*    */   public Latarka(Texture testureShelter, Texture textureOn, Texture textureRoom, Texture textureSignal, int szansa, boolean dlc, int cena, float ResX, float ResY, float style) {
/* 18 */     this.testureShelter = testureShelter;
/* 19 */     this.textureRoom = textureRoom;
/* 20 */     this.textureOn = textureOn;
/* 21 */     this.cena = cena;
/* 22 */     this.szansa = szansa;
/* 23 */     this.dlc = dlc;
/* 24 */     this.kupiony = false;
/* 25 */     this.style = style;
/*    */     
/* 27 */     this.textureSignal = new Sprite(textureSignal);
/* 28 */     this.textureSignal.setOrigin(80.0F, 0.0F);
/* 29 */     this.textureSignal.setScale(ResX, ResY);
/* 30 */     this.textureSignal.setPosition(80.0F * ResX, 0.0F * ResY);
/*    */   }
/*    */   
/* 33 */   public Texture getTextureShelter() { return this.testureShelter; }
/* 34 */   public Sprite getSprite() { return this.textureSignal; } public Texture getTextureRoom() {
/* 35 */     return this.textureRoom;
/*    */   } public Texture getTextureOn() {
/* 37 */     return this.textureOn;
/*    */   }
/* 39 */   public boolean isDlc() { return this.dlc; }
/* 40 */   public boolean isKupiony() { return this.kupiony; }
/* 41 */   public double getSzansa() { return this.szansa; }
/* 42 */   public int getCena() { return this.cena; } public void setKupiony(boolean kupiony) {
/* 43 */     this.kupiony = kupiony;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Latarka.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */