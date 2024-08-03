/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Zawor
/*    */ {
/*    */   public Texture tekstura;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   public int cieknie;
/*    */   public int cieknie2;
/*    */   
/*    */   public Zawor(Texture tekstura, int cena, boolean dlc, float style, int cieknie) {
/* 18 */     this.tekstura = tekstura;
/* 19 */     this.cena = cena;
/* 20 */     this.style = style;
/* 21 */     this.dlc = dlc;
/* 22 */     this.cieknie = cieknie;
/* 23 */     this.kupiony = false;
/* 24 */     this.cieknie2 = cieknie + 6;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Zawor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */