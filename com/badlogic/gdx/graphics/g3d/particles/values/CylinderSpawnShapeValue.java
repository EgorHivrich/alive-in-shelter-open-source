/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ 
/*    */ public final class CylinderSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public CylinderSpawnShapeValue(CylinderSpawnShapeValue cylinderSpawnShapeValue) {
/* 11 */     super(cylinderSpawnShapeValue);
/* 12 */     load(cylinderSpawnShapeValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public CylinderSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 20 */     float radiusX, radiusZ, width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 21 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 22 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */ 
/*    */     
/* 25 */     float hf = height / 2.0F;
/* 26 */     float ty = MathUtils.random(height) - hf;
/*    */ 
/*    */     
/* 29 */     if (this.edges) {
/* 30 */       radiusX = width / 2.0F;
/* 31 */       radiusZ = depth / 2.0F;
/*    */     } else {
/*    */       
/* 34 */       radiusX = MathUtils.random(width) / 2.0F;
/* 35 */       radiusZ = MathUtils.random(depth) / 2.0F;
/*    */     } 
/*    */     
/* 38 */     float spawnTheta = 0.0F;
/*    */ 
/*    */     
/* 41 */     boolean isRadiusXZero = (radiusX == 0.0F), isRadiusZZero = (radiusZ == 0.0F);
/* 42 */     if (!isRadiusXZero && !isRadiusZZero)
/* 43 */     { spawnTheta = MathUtils.random(360.0F); }
/*    */     
/* 45 */     else if (isRadiusXZero) { spawnTheta = (MathUtils.random(1) == 0) ? -90.0F : 90.0F; }
/* 46 */     else if (isRadiusZZero) { spawnTheta = (MathUtils.random(1) == 0) ? 0.0F : 180.0F; }
/*    */ 
/*    */     
/* 49 */     vector.set(radiusX * MathUtils.cosDeg(spawnTheta), ty, radiusZ * MathUtils.sinDeg(spawnTheta));
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 54 */     return new CylinderSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\CylinderSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */