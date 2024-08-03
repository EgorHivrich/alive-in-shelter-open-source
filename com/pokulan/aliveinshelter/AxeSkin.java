/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.Sprite;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AxeSkin
/*    */ {
/*    */   AliveInShelter aliveinshelter;
/*    */   Sprite skin;
/*    */   Texture skinShelter;
/*    */   Texture skinRoom;
/*    */   boolean dlaPremium;
/*    */   boolean statRack;
/*    */   int statRackHuj;
/*    */   boolean kupione;
/*    */   String By;
/*    */   float style;
/*    */   
/*    */   AxeSkin(Texture _skin, Texture _skinShelter, Texture _skinRoom, AliveInShelter aliveInShelter, float styl) {
/* 22 */     this.aliveinshelter = aliveInShelter;
/* 23 */     this.skinShelter = _skinShelter;
/* 24 */     this.skinRoom = _skinRoom;
/* 25 */     this.skin = new Sprite(_skin);
/* 26 */     this.skin.setOrigin(80.0F, 0.0F);
/* 27 */     this.skin.setScale(this.aliveinshelter.ResX, this.aliveinshelter.ResY);
/* 28 */     this.skin.setPosition(80.0F * this.aliveinshelter.ResX, -5.0F * this.aliveinshelter.ResY);
/* 29 */     if (_skin.getWidth() != this.aliveinshelter.gameScreenX) { this.dlaPremium = true; }
/* 30 */     else { this.dlaPremium = false; }
/* 31 */      if (_skin.getHeight() != this.aliveinshelter.gameScreenY) { this.statRack = true; }
/* 32 */     else { this.statRack = false; }
/* 33 */      this.statRackHuj = 0;
/* 34 */     this.kupione = false;
/* 35 */     this.By = "pokulan";
/* 36 */     this.style = styl;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\AxeSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */