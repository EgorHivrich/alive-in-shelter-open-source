/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.VertexAttributes;
/*    */ import com.badlogic.gdx.math.CumulativeDistribution;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WeightMeshSpawnShapeValue
/*    */   extends MeshSpawnShapeValue
/*    */ {
/*    */   private CumulativeDistribution<MeshSpawnShapeValue.Triangle> distribution;
/*    */   
/*    */   public WeightMeshSpawnShapeValue(WeightMeshSpawnShapeValue value) {
/* 17 */     super(value);
/* 18 */     this.distribution = new CumulativeDistribution();
/* 19 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public WeightMeshSpawnShapeValue() {
/* 24 */     this.distribution = new CumulativeDistribution();
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 29 */     calculateWeights();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void calculateWeights() {
/* 36 */     this.distribution.clear();
/* 37 */     VertexAttributes attributes = this.mesh.getVertexAttributes();
/* 38 */     int indicesCount = this.mesh.getNumIndices();
/* 39 */     int vertexCount = this.mesh.getNumVertices();
/* 40 */     int vertexSize = (short)(attributes.vertexSize / 4);
/* 41 */     int positionOffset = (short)((attributes.findByUsage(1)).offset / 4);
/* 42 */     float[] vertices = new float[vertexCount * vertexSize];
/* 43 */     this.mesh.getVertices(vertices);
/* 44 */     if (indicesCount > 0) {
/* 45 */       short[] indices = new short[indicesCount];
/* 46 */       this.mesh.getIndices(indices);
/*    */ 
/*    */       
/* 49 */       for (int i = 0; i < indicesCount; i += 3) {
/* 50 */         int p1Offset = indices[i] * vertexSize + positionOffset;
/* 51 */         int p2Offset = indices[i + 1] * vertexSize + positionOffset;
/* 52 */         int p3Offset = indices[i + 2] * vertexSize + positionOffset;
/* 53 */         float x1 = vertices[p1Offset], y1 = vertices[p1Offset + 1], z1 = vertices[p1Offset + 2];
/* 54 */         float x2 = vertices[p2Offset], y2 = vertices[p2Offset + 1], z2 = vertices[p2Offset + 2];
/* 55 */         float x3 = vertices[p3Offset], y3 = vertices[p3Offset + 1], z3 = vertices[p3Offset + 2];
/* 56 */         float area = Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0F);
/* 57 */         this.distribution.add(new MeshSpawnShapeValue.Triangle(x1, y1, z1, x2, y2, z2, x3, y3, z3), area);
/*    */       } 
/*    */     } else {
/*    */       int i;
/*    */       
/* 62 */       for (i = 0; i < vertexCount; i += vertexSize) {
/* 63 */         int p1Offset = i + positionOffset;
/* 64 */         int p2Offset = p1Offset + vertexSize;
/* 65 */         int p3Offset = p2Offset + vertexSize;
/* 66 */         float x1 = vertices[p1Offset], y1 = vertices[p1Offset + 1], z1 = vertices[p1Offset + 2];
/* 67 */         float x2 = vertices[p2Offset], y2 = vertices[p2Offset + 1], z2 = vertices[p2Offset + 2];
/* 68 */         float x3 = vertices[p3Offset], y3 = vertices[p3Offset + 1], z3 = vertices[p3Offset + 2];
/* 69 */         float area = Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0F);
/* 70 */         this.distribution.add(new MeshSpawnShapeValue.Triangle(x1, y1, z1, x2, y2, z2, x3, y3, z3), area);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 75 */     this.distribution.generateNormalized();
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 80 */     MeshSpawnShapeValue.Triangle t = (MeshSpawnShapeValue.Triangle)this.distribution.value();
/* 81 */     float a = MathUtils.random(), b = MathUtils.random();
/* 82 */     vector.set(t.x1 + a * (t.x2 - t.x1) + b * (t.x3 - t.x1), t.y1 + a * (t.y2 - t.y1) + b * (t.y3 - t.y1), t.z1 + a * (t.z2 - t.z1) + b * (t.z3 - t.z1));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 89 */     return new WeightMeshSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\WeightMeshSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */