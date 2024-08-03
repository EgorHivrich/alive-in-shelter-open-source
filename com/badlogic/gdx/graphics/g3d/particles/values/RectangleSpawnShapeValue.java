/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ public final class RectangleSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public RectangleSpawnShapeValue(RectangleSpawnShapeValue value) {
/* 10 */     super(value);
/* 11 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public RectangleSpawnShapeValue() {}
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 18 */     float width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 19 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 20 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */     
/* 22 */     if (this.edges) {
/*    */       
/* 24 */       int a = MathUtils.random(-1, 1);
/* 25 */       float tx = 0.0F, ty = 0.0F, tz = 0.0F;
/* 26 */       if (a == -1) {
/* 27 */         tx = (MathUtils.random(1) == 0) ? (-width / 2.0F) : (width / 2.0F);
/* 28 */         if (tx == 0.0F) {
/* 29 */           ty = (MathUtils.random(1) == 0) ? (-height / 2.0F) : (height / 2.0F);
/* 30 */           tz = (MathUtils.random(1) == 0) ? (-depth / 2.0F) : (depth / 2.0F);
/*    */         } else {
/*    */           
/* 33 */           ty = MathUtils.random(height) - height / 2.0F;
/* 34 */           tz = MathUtils.random(depth) - depth / 2.0F;
/*    */         }
/*    */       
/* 37 */       } else if (a == 0) {
/*    */         
/* 39 */         tz = (MathUtils.random(1) == 0) ? (-depth / 2.0F) : (depth / 2.0F);
/* 40 */         if (tz == 0.0F) {
/* 41 */           ty = (MathUtils.random(1) == 0) ? (-height / 2.0F) : (height / 2.0F);
/* 42 */           tx = (MathUtils.random(1) == 0) ? (-width / 2.0F) : (width / 2.0F);
/*    */         } else {
/*    */           
/* 45 */           ty = MathUtils.random(height) - height / 2.0F;
/* 46 */           tx = MathUtils.random(width) - width / 2.0F;
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 51 */         ty = (MathUtils.random(1) == 0) ? (-height / 2.0F) : (height / 2.0F);
/* 52 */         if (ty == 0.0F) {
/* 53 */           tx = (MathUtils.random(1) == 0) ? (-width / 2.0F) : (width / 2.0F);
/* 54 */           tz = (MathUtils.random(1) == 0) ? (-depth / 2.0F) : (depth / 2.0F);
/*    */         } else {
/*    */           
/* 57 */           tx = MathUtils.random(width) - width / 2.0F;
/* 58 */           tz = MathUtils.random(depth) - depth / 2.0F;
/*    */         } 
/*    */       } 
/* 61 */       vector.x = tx; vector.y = ty; vector.z = tz;
/*    */     } else {
/*    */       
/* 64 */       vector.x = MathUtils.random(width) - width / 2.0F;
/* 65 */       vector.y = MathUtils.random(height) - height / 2.0F;
/* 66 */       vector.z = MathUtils.random(depth) - depth / 2.0F;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 72 */     return new RectangleSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\RectangleSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */