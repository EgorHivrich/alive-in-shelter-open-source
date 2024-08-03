/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Kalendarz
/*    */ {
/*    */   public Texture tekstura;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   
/*    */   public Kalendarz(Texture tekstura, int cena, boolean dlc, float style) {
/* 16 */     this.tekstura = tekstura;
/* 17 */     this.cena = cena;
/* 18 */     this.style = style;
/* 19 */     this.dlc = dlc;
/* 20 */     this.kupiony = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Kalendarz.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */