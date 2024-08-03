/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*    */ 
/*    */ 
/*    */ public abstract class ParticleControllerRenderer<D extends ParticleControllerRenderData, T extends ParticleBatch<D>>
/*    */   extends ParticleControllerComponent
/*    */ {
/*    */   protected T batch;
/*    */   protected D renderData;
/*    */   
/*    */   protected ParticleControllerRenderer() {}
/*    */   
/*    */   protected ParticleControllerRenderer(D renderData) {
/* 17 */     this.renderData = renderData;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 22 */     this.batch.draw((ParticleControllerRenderData)this.renderData);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean setBatch(ParticleBatch<?> batch) {
/* 27 */     if (isCompatible(batch)) {
/* 28 */       this.batch = (T)batch;
/* 29 */       return true;
/*    */     } 
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract boolean isCompatible(ParticleBatch<?> paramParticleBatch);
/*    */   
/*    */   public void set(ParticleController particleController) {
/* 38 */     super.set(particleController);
/* 39 */     if (this.renderData != null)
/* 40 */       ((ParticleControllerRenderData)this.renderData).controller = this.controller; 
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\renderers\ParticleControllerRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */