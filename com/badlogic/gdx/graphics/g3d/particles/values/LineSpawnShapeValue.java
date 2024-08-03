/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ 
/*    */ public final class LineSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public LineSpawnShapeValue(LineSpawnShapeValue value) {
/* 11 */     super(value);
/* 12 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public LineSpawnShapeValue() {}
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 19 */     float width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 20 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 21 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */     
/* 23 */     float a = MathUtils.random();
/* 24 */     vector.x = a * width;
/* 25 */     vector.y = a * height;
/* 26 */     vector.z = a * depth;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 31 */     return new LineSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\LineSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */