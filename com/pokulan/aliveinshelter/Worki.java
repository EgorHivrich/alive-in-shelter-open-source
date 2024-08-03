/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Worki
/*    */ {
/*    */   int pelny;
/*    */   int potwor;
/*    */   int choroba;
/*    */   int cofanie;
/*    */   int cena;
/*    */   Texture textureFull;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   public float style;
/*    */   
/*    */   public Worki(Texture textureFull, int pelny, int potwor, int choroba, int cofanie, boolean dlc, int cena, float style) {
/* 20 */     this.textureFull = textureFull;
/* 21 */     this.cena = cena;
/* 22 */     this.pelny = pelny;
/* 23 */     this.potwor = potwor;
/* 24 */     this.cofanie = cofanie;
/* 25 */     this.potwor = potwor;
/* 26 */     this.choroba = choroba;
/* 27 */     this.dlc = dlc;
/* 28 */     this.style = style;
/* 29 */     this.kupiony = false;
/*    */   }
/*    */   
/* 32 */   public Texture getTextureFull() { return this.textureFull; }
/* 33 */   public boolean isDlc() { return this.dlc; }
/* 34 */   public boolean isKupiony() { return this.kupiony; }
/* 35 */   public int getPelny() { return this.pelny; }
/* 36 */   public int getChoroba() { return this.choroba; }
/* 37 */   public int getCofanie() { return this.cofanie; }
/* 38 */   public int getPotwor() { return this.potwor; }
/* 39 */   public int getCena() { return this.cena; } public void setKupiony(boolean kupiony) {
/* 40 */     this.kupiony = kupiony;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Worki.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */