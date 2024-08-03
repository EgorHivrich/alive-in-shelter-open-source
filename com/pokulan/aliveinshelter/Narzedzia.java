/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ public class Narzedzia {
/*    */   int maxStan;
/*    */   int cena;
/*    */   Texture texture;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   
/*    */   public Narzedzia(Texture textureFull, int maxStan, boolean dlc, int cena) {
/* 13 */     this.texture = textureFull;
/* 14 */     this.cena = cena;
/* 15 */     this.maxStan = maxStan;
/* 16 */     this.dlc = dlc;
/* 17 */     this.kupiony = false;
/*    */   }
/*    */   
/* 20 */   public Texture getTexture() { return this.texture; }
/* 21 */   public boolean isDlc() { return this.dlc; }
/* 22 */   public boolean isKupiony() { return this.kupiony; }
/* 23 */   public int getMaxStan() { return this.maxStan; }
/* 24 */   public int getCena() { return this.cena; } public void setKupiony(boolean kupiony) {
/* 25 */     this.kupiony = kupiony;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Narzedzia.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */