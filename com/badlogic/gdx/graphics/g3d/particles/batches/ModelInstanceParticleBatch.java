/*    */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceControllerRenderData;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.Pool;
/*    */ 
/*    */ 
/*    */ public class ModelInstanceParticleBatch
/*    */   implements ParticleBatch<ModelInstanceControllerRenderData>
/*    */ {
/* 16 */   Array<ModelInstanceControllerRenderData> controllersRenderData = new Array(false, 5);
/*    */   
/*    */   int bufferedParticlesCount;
/*    */   
/*    */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 21 */     for (ModelInstanceControllerRenderData data : this.controllersRenderData) {
/* 22 */       for (int i = 0, count = data.controller.particles.size; i < count; i++) {
/* 23 */         ((ModelInstance[])data.modelInstanceChannel.data)[i].getRenderables(renderables, pool);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getBufferedCount() {
/* 29 */     return this.bufferedParticlesCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 34 */     this.controllersRenderData.clear();
/* 35 */     this.bufferedParticlesCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {}
/*    */ 
/*    */   
/*    */   public void draw(ModelInstanceControllerRenderData data) {
/* 43 */     this.controllersRenderData.add(data);
/* 44 */     this.bufferedParticlesCount += data.controller.particles.size;
/*    */   }
/*    */   
/*    */   public void save(AssetManager manager, ResourceData assetDependencyData) {}
/*    */   
/*    */   public void load(AssetManager manager, ResourceData assetDependencyData) {}
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\batches\ModelInstanceParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */