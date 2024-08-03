/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ public final class EllipseSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/* 11 */   PrimitiveSpawnShapeValue.SpawnSide side = PrimitiveSpawnShapeValue.SpawnSide.both;
/*    */   
/*    */   public EllipseSpawnShapeValue(EllipseSpawnShapeValue value) {
/* 14 */     super(value);
/* 15 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public EllipseSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 23 */     float radiusX, radiusY, radiusZ, width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 24 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 25 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */ 
/*    */ 
/*    */     
/* 29 */     float minT = 0.0F, maxT = 6.2831855F;
/* 30 */     if (this.side == PrimitiveSpawnShapeValue.SpawnSide.top) {
/* 31 */       maxT = 3.1415927F;
/*    */     }
/* 33 */     else if (this.side == PrimitiveSpawnShapeValue.SpawnSide.bottom) {
/* 34 */       maxT = -3.1415927F;
/*    */     } 
/* 36 */     float t = MathUtils.random(minT, maxT);
/*    */ 
/*    */     
/* 39 */     if (this.edges) {
/* 40 */       if (width == 0.0F) {
/* 41 */         vector.set(0.0F, height / 2.0F * MathUtils.sin(t), depth / 2.0F * MathUtils.cos(t));
/*    */         return;
/*    */       } 
/* 44 */       if (height == 0.0F) {
/* 45 */         vector.set(width / 2.0F * MathUtils.cos(t), 0.0F, depth / 2.0F * MathUtils.sin(t));
/*    */         return;
/*    */       } 
/* 48 */       if (depth == 0.0F) {
/* 49 */         vector.set(width / 2.0F * MathUtils.cos(t), height / 2.0F * MathUtils.sin(t), 0.0F);
/*    */         
/*    */         return;
/*    */       } 
/* 53 */       radiusX = width / 2.0F;
/* 54 */       radiusY = height / 2.0F;
/* 55 */       radiusZ = depth / 2.0F;
/*    */     } else {
/*    */       
/* 58 */       radiusX = MathUtils.random(width / 2.0F);
/* 59 */       radiusY = MathUtils.random(height / 2.0F);
/* 60 */       radiusZ = MathUtils.random(depth / 2.0F);
/*    */     } 
/*    */     
/* 63 */     float z = MathUtils.random(-1.0F, 1.0F);
/* 64 */     float r = (float)Math.sqrt((1.0F - z * z));
/* 65 */     vector.set(radiusX * r * MathUtils.cos(t), radiusY * r * MathUtils.sin(t), radiusZ * z);
/*    */   }
/*    */   
/*    */   public PrimitiveSpawnShapeValue.SpawnSide getSide() {
/* 69 */     return this.side;
/*    */   }
/*    */   
/*    */   public void setSide(PrimitiveSpawnShapeValue.SpawnSide side) {
/* 73 */     this.side = side;
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(ParticleValue value) {
/* 78 */     super.load(value);
/* 79 */     EllipseSpawnShapeValue shape = (EllipseSpawnShapeValue)value;
/* 80 */     this.side = shape.side;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 85 */     return new EllipseSpawnShapeValue(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 90 */     super.write(json);
/* 91 */     json.writeValue("side", this.side);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 96 */     super.read(json, jsonData);
/* 97 */     this.side = (PrimitiveSpawnShapeValue.SpawnSide)json.readValue("side", PrimitiveSpawnShapeValue.SpawnSide.class, jsonData);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\EllipseSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */