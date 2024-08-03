/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Door
/*    */ {
/*    */   int extraOfense;
/*    */   int value;
/*    */   int stanMax;
/*    */   Texture closed;
/*    */   Texture opened;
/*    */   Texture outside;
/*    */   Texture preview;
/*    */   boolean kupiony;
/*    */   float style;
/*    */   boolean lockpick;
/*    */   
/*    */   Door(int extraOfense, Texture closed, Texture opened, Texture outside, Texture preview, int value, float styl, int stanMax, boolean lockpick) {
/* 21 */     this.extraOfense = extraOfense;
/* 22 */     this.closed = closed;
/* 23 */     this.opened = opened;
/* 24 */     this.outside = outside;
/* 25 */     this.value = value;
/* 26 */     this.preview = preview;
/* 27 */     this.lockpick = lockpick;
/* 28 */     this.kupiony = false;
/* 29 */     this.style = styl;
/* 30 */     this.stanMax = stanMax;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Door.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */