/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ public class Sciana
/*    */ {
/*    */   public Texture sciana0;
/*    */   public Texture sciana1;
/*    */   public Texture sciana2;
/*    */   public Texture sciana3;
/*    */   public Texture mokro0;
/*    */   public Texture mokro1;
/*    */   public Texture mokro2;
/*    */   public Texture mokro3;
/*    */   public Texture zimno0;
/*    */   public Texture zimno1;
/*    */   
/*    */   public Sciana(Texture sucho0, Texture sucho1, Texture sucho2, Texture sucho3, Texture zimno0, Texture zimno1, Texture zimno2, Texture zimno3, Texture mokro0, Texture mokro1, Texture mokro2, Texture mokro3, int cena, boolean dlc, float style) {
/* 20 */     this.sucho0 = sucho0;
/* 21 */     this.sucho1 = sucho1;
/* 22 */     this.sucho2 = sucho2;
/* 23 */     this.sucho3 = sucho3;
/* 24 */     this.zimno0 = zimno0;
/* 25 */     this.zimno1 = zimno1;
/* 26 */     this.zimno2 = zimno2;
/* 27 */     this.zimno3 = zimno3;
/* 28 */     this.mokro0 = mokro0;
/* 29 */     this.mokro1 = mokro1;
/* 30 */     this.mokro2 = mokro2;
/* 31 */     this.mokro3 = mokro3;
/* 32 */     this.cena = cena;
/* 33 */     this.dlc = dlc;
/* 34 */     this.style = style;
/* 35 */     this.xmas = false;
/* 36 */     this.kupiony = false;
/*    */   }
/*    */   public Texture zimno2; public Texture zimno3; public Texture sucho0; public Texture sucho1; public Texture sucho2; public Texture sucho3; public boolean kupiony; public int cena; public boolean dlc; public float style; public boolean xmas;
/*    */   public void sucho(boolean napadnieci) {
/* 40 */     if (!napadnieci) this.sciana0 = this.sucho0; 
/* 41 */     this.sciana1 = this.sucho1;
/* 42 */     this.sciana2 = this.sucho2;
/* 43 */     this.sciana3 = this.sucho3;
/*    */   }
/*    */   public void mokro(boolean napadnieci) {
/* 46 */     if (!napadnieci) this.sciana0 = this.mokro0; 
/* 47 */     this.sciana1 = this.mokro1;
/* 48 */     this.sciana2 = this.mokro2;
/* 49 */     this.sciana3 = this.mokro3;
/*    */   }
/*    */   public void zimno(boolean napadnieci) {
/* 52 */     if (!napadnieci) this.sciana0 = this.zimno0; 
/* 53 */     this.sciana1 = this.zimno1;
/* 54 */     this.sciana2 = this.zimno2;
/* 55 */     this.sciana3 = this.zimno3;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Sciana.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */