/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
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
/*    */ public class RotateByAction
/*    */   extends RelativeTemporalAction
/*    */ {
/*    */   private float amount;
/*    */   
/*    */   protected void updateRelative(float percentDelta) {
/* 25 */     this.target.rotateBy(this.amount * percentDelta);
/*    */   }
/*    */   
/*    */   public float getAmount() {
/* 29 */     return this.amount;
/*    */   }
/*    */   
/*    */   public void setAmount(float rotationAmount) {
/* 33 */     this.amount = rotationAmount;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2d\actions\RotateByAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */