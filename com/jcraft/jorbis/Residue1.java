/*    */ package com.jcraft.jorbis;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Residue1
/*    */   extends Residue0
/*    */ {
/*    */   int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
/* 32 */     int used = 0;
/* 33 */     for (int i = 0; i < ch; i++) {
/* 34 */       if (nonzero[i] != 0) {
/* 35 */         in[used++] = in[i];
/*    */       }
/*    */     } 
/* 38 */     if (used != 0) {
/* 39 */       return _01inverse(vb, vl, in, used, 1);
/*    */     }
/*    */     
/* 42 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\jcraft\jorbis\Residue1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */