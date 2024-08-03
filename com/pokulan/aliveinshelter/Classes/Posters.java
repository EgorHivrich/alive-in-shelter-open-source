/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Posters
/*    */ {
/*    */   public Texture tekstura;
/*    */   public Texture tekstura2;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   public boolean xmas;
/*    */   
/*    */   public Posters(Texture tekstura, Texture tekstura2, int cena, boolean dlc, float style) {
/* 18 */     this.tekstura = tekstura;
/* 19 */     this.tekstura2 = tekstura2;
/* 20 */     this.cena = cena;
/* 21 */     this.style = style;
/* 22 */     this.dlc = dlc;
/* 23 */     this.kupiony = false;
/* 24 */     this.xmas = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Posters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */