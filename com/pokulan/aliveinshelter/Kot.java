/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.Animation;
/*    */ 
/*    */ 
/*    */ public class Kot
/*    */ {
/*    */   public Texture textura;
/*    */   public Animation animacja;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public float style;
/*    */   public String credits;
/*    */   public int in;
/*    */   public int out;
/*    */   public int zycie;
/*    */   
/*    */   public Kot(Texture tekstura, Animation animacja, int cena, boolean dlc, float style, String cr, int in, int out) {
/* 21 */     this.textura = tekstura;
/* 22 */     this.animacja = animacja;
/* 23 */     this.cena = cena;
/* 24 */     this.style = style;
/* 25 */     this.dlc = dlc;
/* 26 */     this.in = in;
/* 27 */     this.out = out;
/* 28 */     this.zycie = Math.abs(in) / 2;
/* 29 */     this.kupiony = false;
/* 30 */     if (cr.equals("")) { this.credits = ""; }
/* 31 */     else { this.credits = "By: " + cr; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Kot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */