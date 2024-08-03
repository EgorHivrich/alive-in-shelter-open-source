/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Lozko
/*    */ {
/*    */   public Texture tekstura;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   public boolean xmas;
/*    */   int recover;
/*    */   
/*    */   public Lozko(Texture tekstura, int cena, boolean dlc, float style, int recover) {
/* 18 */     this.tekstura = tekstura;
/* 19 */     this.cena = cena;
/* 20 */     this.style = style;
/* 21 */     this.dlc = dlc;
/* 22 */     this.kupiony = false;
/* 23 */     this.xmas = false;
/* 24 */     this.recover = recover;
/*    */   }
/*    */   public int getRecover() {
/* 27 */     return this.recover;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Lozko.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */