/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ public class Phone
/*    */ {
/*    */   public Texture tekstura;
/*    */   public Texture messages;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   public String credits;
/*    */   
/*    */   public Phone(Texture tekstura, Texture messages, int cena, boolean dlc, float style, String cr) {
/* 17 */     this.tekstura = tekstura;
/* 18 */     this.messages = messages;
/* 19 */     this.cena = cena;
/* 20 */     this.style = style;
/* 21 */     this.dlc = dlc;
/* 22 */     this.kupiony = false;
/* 23 */     if (cr.equals("")) { this.credits = ""; }
/* 24 */     else { this.credits = "By: " + cr; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Phone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */