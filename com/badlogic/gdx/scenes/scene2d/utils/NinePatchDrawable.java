/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
/*    */ import com.badlogic.gdx.graphics.g2d.Batch;
/*    */ import com.badlogic.gdx.graphics.g2d.NinePatch;
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
/*    */ public class NinePatchDrawable
/*    */   extends BaseDrawable
/*    */ {
/*    */   private NinePatch patch;
/*    */   
/*    */   public NinePatchDrawable() {}
/*    */   
/*    */   public NinePatchDrawable(NinePatch patch) {
/* 40 */     setPatch(patch);
/*    */   }
/*    */   
/*    */   public NinePatchDrawable(NinePatchDrawable drawable) {
/* 44 */     super(drawable);
/* 45 */     setPatch(drawable.patch);
/*    */   }
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float width, float height) {
/* 49 */     this.patch.draw(batch, x, y, width, height);
/*    */   }
/*    */   
/*    */   public void setPatch(NinePatch patch) {
/* 53 */     this.patch = patch;
/* 54 */     setMinWidth(patch.getTotalWidth());
/* 55 */     setMinHeight(patch.getTotalHeight());
/* 56 */     setTopHeight(patch.getPadTop());
/* 57 */     setRightWidth(patch.getPadRight());
/* 58 */     setBottomHeight(patch.getPadBottom());
/* 59 */     setLeftWidth(patch.getPadLeft());
/*    */   }
/*    */   
/*    */   public NinePatch getPatch() {
/* 63 */     return this.patch;
/*    */   }
/*    */ 
/*    */   
/*    */   public NinePatchDrawable tint(Color tint) {
/* 68 */     NinePatchDrawable drawable = new NinePatchDrawable(this);
/* 69 */     drawable.setPatch(new NinePatch(drawable.getPatch(), tint));
/* 70 */     return drawable;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\utils\NinePatchDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */