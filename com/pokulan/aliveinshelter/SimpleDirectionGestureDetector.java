/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.input.GestureDetector;
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
/*    */ public class SimpleDirectionGestureDetector
/*    */   extends GestureDetector
/*    */ {
/*    */   public SimpleDirectionGestureDetector(DirectionListener directionListener) {
/* 20 */     super((GestureDetector.GestureListener)new DirectionGestureListener(directionListener));
/*    */   }
/*    */   
/*    */   private static class DirectionGestureListener extends GestureDetector.GestureAdapter {
/*    */     SimpleDirectionGestureDetector.DirectionListener directionListener;
/*    */     
/*    */     public DirectionGestureListener(SimpleDirectionGestureDetector.DirectionListener directionListener) {
/* 27 */       this.directionListener = directionListener;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean fling(float velocityX, float velocityY, int button) {
/* 32 */       if (Math.abs(velocityX) > Math.abs(velocityY)) {
/* 33 */         if (velocityX > 0.0F) {
/* 34 */           this.directionListener.onRight();
/*    */         } else {
/* 36 */           this.directionListener.onLeft();
/*    */         }
/*    */       
/* 39 */       } else if (velocityY > 0.0F) {
/* 40 */         this.directionListener.onDown();
/*    */       } else {
/* 42 */         this.directionListener.onUp();
/*    */       } 
/*    */       
/* 45 */       return super.fling(velocityX, velocityY, button);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface DirectionListener {
/*    */     void onLeft();
/*    */     
/*    */     void onRight();
/*    */     
/*    */     void onUp();
/*    */     
/*    */     void onDown();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\SimpleDirectionGestureDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */