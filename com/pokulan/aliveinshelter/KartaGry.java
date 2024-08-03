/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ public class KartaGry {
/*    */   int waga;
/*    */   boolean kolor;
/*    */   int figura;
/*    */   Texture tekstura;
/*    */   boolean pusta;
/*    */   boolean zaznaczona;
/*    */   boolean zakryta;
/*    */   
/*    */   public KartaGry(int waga, boolean kolor, int figura, Texture tekstura, boolean pusta) {
/* 15 */     this.waga = waga;
/* 16 */     this.kolor = kolor;
/* 17 */     this.figura = figura;
/* 18 */     this.tekstura = tekstura;
/* 19 */     this.pusta = pusta;
/* 20 */     this.zaznaczona = false;
/* 21 */     this.zakryta = false;
/*    */   }
/*    */   
/*    */   public int getWaga() {
/* 25 */     return this.waga;
/*    */   }
/*    */   public boolean getKolor() {
/* 28 */     return this.kolor;
/*    */   }
/*    */   public int getFigura() {
/* 31 */     return this.figura;
/*    */   }
/*    */   public Texture getTekstura() {
/* 34 */     return this.tekstura;
/*    */   }
/*    */   public boolean getPusta() {
/* 37 */     return this.pusta;
/*    */   }
/*    */   public void setPusta(boolean x) {
/* 40 */     this.pusta = x;
/*    */   }
/*    */   public void disp() {
/* 43 */     this.tekstura.dispose();
/*    */   }
/*    */   public void setZakryta(boolean zakryta) {
/* 46 */     this.zakryta = zakryta;
/*    */   }
/*    */   public boolean getZakryta() {
/* 49 */     return this.zakryta;
/*    */   }
/*    */   public boolean getZaznaczona() {
/* 52 */     return this.zaznaczona;
/*    */   }
/*    */   
/*    */   public void setZaznaczona(boolean zaznaczona) {
/* 56 */     this.zaznaczona = zaznaczona;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\KartaGry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */