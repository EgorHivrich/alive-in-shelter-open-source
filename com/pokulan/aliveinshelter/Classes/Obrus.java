/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Obrus
/*    */ {
/*    */   public Texture tekstura;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   public boolean xmas;
/*    */   
/*    */   public Obrus(Texture tekstura, int cena, boolean dlc, float style) {
/* 17 */     this.tekstura = tekstura;
/* 18 */     this.cena = cena;
/* 19 */     this.style = style;
/* 20 */     this.dlc = dlc;
/* 21 */     this.kupiony = false;
/* 22 */     this.xmas = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Obrus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */