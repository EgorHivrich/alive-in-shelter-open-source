/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Zegar
/*    */ {
/*    */   public Texture tekstura;
/*    */   public float speedM;
/*    */   public float speedH;
/*    */   public float speedM_2;
/*    */   public float speedH_2;
/*    */   public int speedTime;
/*    */   public boolean kupiony;
/*    */   public int cena;
/*    */   public boolean dlc;
/*    */   public int procent;
/*    */   public float style;
/*    */   
/*    */   public Zegar(Texture tekstura, int speedM, int cena, boolean dlc, float style) {
/* 22 */     this.speedM = speedM / 10.0F;
/* 23 */     this.speedH = this.speedM / 12.0F;
/* 24 */     this.speedM_2 = this.speedM;
/* 25 */     this.speedTime = speedM;
/* 26 */     this.speedH_2 = this.speedH;
/* 27 */     this.tekstura = tekstura;
/* 28 */     this.cena = cena;
/* 29 */     this.style = style;
/* 30 */     this.dlc = dlc;
/* 31 */     this.kupiony = false;
/* 32 */     this.procent = 100 - (int)(speedM * 100.0D / 20.0D);
/*    */   }
/*    */   
/*    */   public void setLow() {
/* 36 */     this.speedH = (float)(this.speedH / 3.0D);
/* 37 */     this.speedM = (float)(this.speedM / 3.0D);
/*    */   }
/*    */   
/*    */   public void setNormal() {
/* 41 */     this.speedM = this.speedM_2;
/* 42 */     this.speedH = this.speedH_2;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Zegar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */