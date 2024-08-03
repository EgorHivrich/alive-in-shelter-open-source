/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ 
/*    */ public final class PointSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public PointSpawnShapeValue(PointSpawnShapeValue value) {
/* 10 */     super(value);
/* 11 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public PointSpawnShapeValue() {}
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 18 */     vector.x = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 19 */     vector.y = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 20 */     vector.z = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 25 */     return new PointSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\PointSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */