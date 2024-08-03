/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Mesh;
/*    */ import com.badlogic.gdx.graphics.g3d.Model;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ public final class UnweightedMeshSpawnShapeValue extends MeshSpawnShapeValue {
/*    */   private float[] vertices;
/*    */   private short[] indices;
/*    */   private int positionOffset;
/*    */   private int vertexSize;
/*    */   private int vertexCount;
/*    */   private int triangleCount;
/*    */   
/*    */   public UnweightedMeshSpawnShapeValue(UnweightedMeshSpawnShapeValue value) {
/* 17 */     super(value);
/* 18 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnweightedMeshSpawnShapeValue() {}
/*    */   
/*    */   public void setMesh(Mesh mesh, Model model) {
/* 25 */     super.setMesh(mesh, model);
/* 26 */     this.vertexSize = mesh.getVertexSize() / 4;
/* 27 */     this.positionOffset = (mesh.getVertexAttribute(1)).offset / 4;
/* 28 */     int indicesCount = mesh.getNumIndices();
/* 29 */     if (indicesCount > 0) {
/* 30 */       this.indices = new short[indicesCount];
/* 31 */       mesh.getIndices(this.indices);
/* 32 */       this.triangleCount = this.indices.length / 3;
/*    */     } else {
/* 34 */       this.indices = null;
/* 35 */     }  this.vertexCount = mesh.getNumVertices();
/* 36 */     this.vertices = new float[this.vertexCount * this.vertexSize];
/* 37 */     mesh.getVertices(this.vertices);
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 42 */     if (this.indices == null) {
/*    */       
/* 44 */       int triangleIndex = MathUtils.random(this.vertexCount - 3) * this.vertexSize;
/* 45 */       int p1Offset = triangleIndex + this.positionOffset;
/* 46 */       int p2Offset = p1Offset + this.vertexSize;
/* 47 */       int p3Offset = p2Offset + this.vertexSize;
/* 48 */       float x1 = this.vertices[p1Offset], y1 = this.vertices[p1Offset + 1], z1 = this.vertices[p1Offset + 2];
/* 49 */       float x2 = this.vertices[p2Offset], y2 = this.vertices[p2Offset + 1], z2 = this.vertices[p2Offset + 2];
/* 50 */       float x3 = this.vertices[p3Offset], y3 = this.vertices[p3Offset + 1], z3 = this.vertices[p3Offset + 2];
/* 51 */       MeshSpawnShapeValue.Triangle.pick(x1, y1, z1, x2, y2, z2, x3, y3, z3, vector);
/*    */     }
/*    */     else {
/*    */       
/* 55 */       int triangleIndex = MathUtils.random(this.triangleCount - 1) * 3;
/* 56 */       int p1Offset = this.indices[triangleIndex] * this.vertexSize + this.positionOffset;
/* 57 */       int p2Offset = this.indices[triangleIndex + 1] * this.vertexSize + this.positionOffset;
/* 58 */       int p3Offset = this.indices[triangleIndex + 2] * this.vertexSize + this.positionOffset;
/* 59 */       float x1 = this.vertices[p1Offset], y1 = this.vertices[p1Offset + 1], z1 = this.vertices[p1Offset + 2];
/* 60 */       float x2 = this.vertices[p2Offset], y2 = this.vertices[p2Offset + 1], z2 = this.vertices[p2Offset + 2];
/* 61 */       float x3 = this.vertices[p3Offset], y3 = this.vertices[p3Offset + 1], z3 = this.vertices[p3Offset + 2];
/* 62 */       MeshSpawnShapeValue.Triangle.pick(x1, y1, z1, x2, y2, z2, x3, y3, z3, vector);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 68 */     return new UnweightedMeshSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\UnweightedMeshSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */