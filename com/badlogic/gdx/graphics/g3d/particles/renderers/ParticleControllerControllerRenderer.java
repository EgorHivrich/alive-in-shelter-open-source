/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParticleControllerControllerRenderer
/*    */   extends ParticleControllerRenderer
/*    */ {
/*    */   ParallelArray.ObjectChannel<ParticleController> controllerChannel;
/*    */   
/*    */   public void init() {
/* 20 */     this.controllerChannel = (ParallelArray.ObjectChannel<ParticleController>)this.controller.particles.getChannel(ParticleChannels.ParticleController);
/* 21 */     if (this.controllerChannel == null) {
/* 22 */       throw new GdxRuntimeException("ParticleController channel not found, specify an influencer which will allocate it please.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void update() {
/* 27 */     for (int i = 0, c = this.controller.particles.size; i < c; i++) {
/* 28 */       ((ParticleController[])this.controllerChannel.data)[i].draw();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 34 */     return new ParticleControllerControllerRenderer();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompatible(ParticleBatch batch) {
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\renderers\ParticleControllerControllerRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */