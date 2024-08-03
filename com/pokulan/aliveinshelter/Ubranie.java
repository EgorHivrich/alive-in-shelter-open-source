/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Ubranie
/*    */ {
/* 23 */   AliveInShelter aliveinshelter = new AliveInShelter(); Ubranie(Texture _tekstura, Texture teksturaZgon, Texture teksturaSzkielet, boolean tak, float styl) {
/* 24 */     if (tak) {
/* 25 */       this.cena = 70 + (_tekstura.getHeight() - this.aliveinshelter.gameScreenY) * 10;
/* 26 */       if (_tekstura.getWidth() != this.aliveinshelter.gameScreenX) { this.dlaPremium = true; }
/* 27 */       else { this.dlaPremium = false; }
/* 28 */        this.kupione = false;
/*    */     } else {
/* 30 */       this.cena = 0;
/* 31 */       this.dlaPremium = false;
/* 32 */       this.kupione = true;
/*    */     } 
/* 34 */     this.tekstura = _tekstura;
/* 35 */     this.teksturaZgon = teksturaZgon;
/* 36 */     this.teksturaSzkielet = teksturaSzkielet;
/* 37 */     this.By = "pokulan";
/* 38 */     this.hell = false;
/* 39 */     this.xmas = false;
/* 40 */     this.style = styl;
/* 41 */     this.adult = false;
/*    */   }
/*    */   
/*    */   boolean dlaPremium;
/*    */   int cena;
/*    */   Texture tekstura;
/*    */   Texture teksturaZgon;
/*    */   Texture teksturaSzkielet;
/*    */   boolean kupione;
/*    */   boolean hell;
/*    */   boolean xmas;
/*    */   String By;
/*    */   float style;
/*    */   boolean adult;
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Ubranie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */