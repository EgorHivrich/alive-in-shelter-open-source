/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KartaTlo
/*    */ {
/*    */   Texture tlo;
/*    */   int cena;
/*    */   boolean dlc;
/*    */   boolean kupiony;
/*    */   float style;
/*    */   
/*    */   KartaTlo(Texture tlo, int cena, boolean dlc, boolean kupiony, float style) {
/* 16 */     this.cena = cena;
/* 17 */     this.dlc = dlc;
/* 18 */     this.kupiony = kupiony;
/* 19 */     this.tlo = tlo;
/* 20 */     this.style = style;
/*    */   }
/*    */   
/* 23 */   int getCena() { return this.cena; }
/* 24 */   boolean getDLC() { return this.dlc; }
/* 25 */   boolean getKupiony() { return this.kupiony; }
/* 26 */   Texture getTlo() { return this.tlo; } void setKupiony(boolean i) {
/* 27 */     this.kupiony = i;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\KartaTlo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */