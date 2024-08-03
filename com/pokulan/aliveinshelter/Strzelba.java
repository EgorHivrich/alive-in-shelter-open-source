/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Strzelba
/*    */ {
/*    */   int maxDMG;
/*    */   int DMG;
/*    */   int cena;
/*    */   Texture texture;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   public float style;
/*    */   String credits;
/*    */   
/*    */   public Strzelba(Texture textureFull, int maxDMG, int DMG, boolean dlc, int cena, float style, String who) {
/* 19 */     this.texture = textureFull;
/* 20 */     this.cena = cena;
/* 21 */     this.maxDMG = maxDMG;
/* 22 */     this.DMG = DMG;
/* 23 */     this.style = style;
/*    */     
/* 25 */     this.dlc = dlc;
/* 26 */     this.kupiony = false;
/* 27 */     if (who.equals("")) { this.credits = ""; }
/* 28 */     else { this.credits = "By: " + who; }
/*    */   
/*    */   }
/* 31 */   public Texture getTextureFull() { return this.texture; }
/* 32 */   public boolean isDlc() { return this.dlc; }
/* 33 */   public boolean isKupiony() { return this.kupiony; }
/* 34 */   public int getMaxDMG() { return this.maxDMG; }
/* 35 */   public int getDMG() { return this.DMG; }
/* 36 */   public int getCena() { return this.cena; } public void setKupiony(boolean kupiony) {
/* 37 */     this.kupiony = kupiony;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Strzelba.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */