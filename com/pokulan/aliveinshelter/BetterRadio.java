/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ public class BetterRadio
/*    */ {
/*    */   int maxStan;
/*    */   int cena;
/*    */   Texture texture;
/*    */   Texture broken;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   
/*    */   public BetterRadio(Texture textureFull, Texture broken, int maxStan, boolean dlc, int cena) {
/* 16 */     this.texture = textureFull;
/* 17 */     this.broken = broken;
/* 18 */     this.cena = cena;
/* 19 */     this.maxStan = maxStan;
/*    */     
/* 21 */     this.dlc = dlc;
/* 22 */     this.kupiony = false;
/*    */   }
/*    */   
/* 25 */   public Texture getTextureFull() { return this.texture; }
/* 26 */   public Texture getTextureBroken() { return this.broken; }
/* 27 */   public boolean isDlc() { return this.dlc; }
/* 28 */   public boolean isKupiony() { return this.kupiony; }
/* 29 */   public int getMaxStan() { return this.maxStan; }
/* 30 */   public int getCena() { return this.cena; } public void setKupiony(boolean kupiony) {
/* 31 */     this.kupiony = kupiony;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\BetterRadio.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */