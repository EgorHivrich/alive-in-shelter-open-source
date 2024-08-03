/*    */ package com.pokulan.aliveinshelter;
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
/*    */ public class Poletko
/*    */ {
/* 16 */   int stan = -1;
/* 17 */   int co = 0;
/*    */   boolean podlanie = false;
/*    */   boolean nawoz = false;
/*    */   boolean bowlStan;
/*    */   
/*    */   String name() {
/* 23 */     if (this.co == 1) return Zdania.TEXT[90]; 
/* 24 */     if (this.co == 2) return Zdania.TEXT[162]; 
/* 25 */     if (this.co == 3) return Zdania.TEXT[223]; 
/* 26 */     if (this.co == 4) return Zdania.TEXT[293]; 
/* 27 */     if (this.co == 5) return Zdania.TEXT[398]; 
/* 28 */     if (this.co == 6) return Zdania.TEXT[433]; 
/* 29 */     if (this.co == 7) return Zdania.TEXT[447]; 
/* 30 */     if (this.co == 8) return Zdania.TEXT[481]; 
/* 31 */     return "---";
/*    */   }
/*    */   
/*    */   int stanik() {
/* 35 */     if (this.stan > -1) return this.stan; 
/* 36 */     return 0;
/*    */   }
/*    */   
/*    */   void clean() {
/* 40 */     this.co = 0;
/* 41 */     this.stan = -1;
/* 42 */     this.podlanie = false;
/* 43 */     this.nawoz = false;
/*    */   }
/*    */   
/*    */   void posadz(int co) {
/* 47 */     this.co = co;
/* 48 */     this.stan = 1;
/*    */   }
/*    */   
/*    */   void rosnij() {
/* 52 */     if (this.stan < 10 && this.co > 0) this.stan++; 
/* 53 */     if (this.podlanie) {
/* 54 */       if (this.stan < 10 && this.co > 0) this.stan++; 
/* 55 */       this.podlanie = false;
/*    */     } 
/* 57 */     if (this.nawoz) {
/* 58 */       if (this.stan < 10 && this.co > 0) this.stan++; 
/* 59 */       this.nawoz = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   void podlewanie() {
/* 64 */     this.podlanie = true;
/*    */   }
/*    */   
/*    */   void nawozenie() {
/* 68 */     this.nawoz = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Poletko.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */