/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.Mesh;
/*    */ import com.badlogic.gdx.graphics.g3d.Model;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ 
/*    */ public abstract class MeshSpawnShapeValue
/*    */   extends SpawnShapeValue {
/*    */   protected Mesh mesh;
/*    */   protected Model model;
/*    */   
/*    */   public static class Triangle {
/*    */     float x1;
/*    */     float y1;
/*    */     float z1;
/*    */     float x2;
/*    */     
/*    */     public Triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
/* 24 */       this.x1 = x1; this.y1 = y1; this.z1 = z1;
/* 25 */       this.x2 = x2; this.y2 = y2; this.z2 = z2;
/* 26 */       this.x3 = x3; this.y3 = y3; this.z3 = z3;
/*    */     }
/*    */     float y2; float z2; float x3; float y3;
/*    */     float z3;
/*    */     
/*    */     public static Vector3 pick(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Vector3 vector) {
/* 32 */       float a = MathUtils.random(), b = MathUtils.random();
/* 33 */       return vector.set(x1 + a * (x2 - x1) + b * (x3 - x1), y1 + a * (y2 - y1) + b * (y3 - y1), z1 + a * (z2 - z1) + b * (z3 - z1));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Vector3 pick(Vector3 vector) {
/* 39 */       float a = MathUtils.random(), b = MathUtils.random();
/* 40 */       return vector.set(this.x1 + a * (this.x2 - this.x1) + b * (this.x3 - this.x1), this.y1 + a * (this.y2 - this.y1) + b * (this.y3 - this.y1), this.z1 + a * (this.z2 - this.z1) + b * (this.z3 - this.z1));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MeshSpawnShapeValue(MeshSpawnShapeValue value) {
/* 53 */     super(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public MeshSpawnShapeValue() {}
/*    */   
/*    */   public void load(ParticleValue value) {
/* 60 */     super.load(value);
/* 61 */     MeshSpawnShapeValue spawnShapeValue = (MeshSpawnShapeValue)value;
/* 62 */     setMesh(spawnShapeValue.mesh, spawnShapeValue.model);
/*    */   }
/*    */   
/*    */   public void setMesh(Mesh mesh, Model model) {
/* 66 */     if (mesh.getVertexAttribute(1) == null)
/* 67 */       throw new GdxRuntimeException("Mesh vertices must have Usage.Position"); 
/* 68 */     this.model = model;
/* 69 */     this.mesh = mesh;
/*    */   }
/*    */   
/*    */   public void setMesh(Mesh mesh) {
/* 73 */     setMesh(mesh, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void save(AssetManager manager, ResourceData data) {
/* 78 */     if (this.model != null) {
/* 79 */       ResourceData.SaveData saveData = data.createSaveData();
/* 80 */       saveData.saveAsset(manager.getAssetFileName(this.model), Model.class);
/* 81 */       saveData.save("index", Integer.valueOf(this.model.meshes.indexOf(this.mesh, true)));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(AssetManager manager, ResourceData data) {
/* 87 */     ResourceData.SaveData saveData = data.getSaveData();
/* 88 */     AssetDescriptor descriptor = saveData.loadAsset();
/* 89 */     if (descriptor != null) {
/* 90 */       Model model = (Model)manager.get(descriptor);
/* 91 */       setMesh((Mesh)model.meshes.get(((Integer)saveData.load("index")).intValue()), model);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\MeshSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */