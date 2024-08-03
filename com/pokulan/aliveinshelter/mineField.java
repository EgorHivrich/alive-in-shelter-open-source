/*    */ package com.pokulan.aliveinshelter;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class mineField {
/*    */   int id;
/*    */   boolean wykopane;
/*    */   
/*    */   mineField() {
/*  9 */     rand(1);
/*    */   }
/*    */   
/* 12 */   int getId() { return this.id; }
/* 13 */   void setId(int i) { this.id = i; }
/* 14 */   void setWykopane(boolean i) { this.wykopane = i; } boolean getWykopane() {
/* 15 */     return this.wykopane;
/*    */   } void rand(int level) {
/* 17 */     Random x = new Random();
/* 18 */     int losowa = x.nextInt(85);
/* 19 */     if (losowa >= 5 && losowa <= 7 && level >= 15) { this.id = 4; }
/* 20 */     else if (losowa >= 8 && losowa <= 10) { this.id = 3; }
/* 21 */     else if (losowa >= 12 && losowa <= 14) { this.id = 2; }
/* 22 */     else if (losowa >= 17 && losowa <= 20) { this.id = 1; }
/* 23 */     else if (losowa >= 30 && losowa <= 33) { this.id = 5; }
/* 24 */     else if (losowa >= 40 && losowa <= 45) { this.id = 6; }
/* 25 */     else if (losowa >= 50 && losowa <= 53) { this.id = 7; }
/* 26 */     else if (losowa >= 60 && losowa <= 62) { this.id = 8; }
/* 27 */     else if (losowa >= 70 && losowa <= 71) { this.id = 9; }
/* 28 */     else if (losowa == 81) { this.id = 10; }
/* 29 */     else { this.id = 0; }
/* 30 */      this.wykopane = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\mineField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */