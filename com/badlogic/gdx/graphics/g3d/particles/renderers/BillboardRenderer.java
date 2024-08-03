/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BillboardRenderer
/*    */   extends ParticleControllerRenderer<BillboardControllerRenderData, BillboardParticleBatch>
/*    */ {
/*    */   public BillboardRenderer() {
/* 17 */     super(new BillboardControllerRenderData());
/*    */   }
/*    */   
/*    */   public BillboardRenderer(BillboardParticleBatch batch) {
/* 21 */     this();
/* 22 */     setBatch((ParticleBatch<?>)batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 27 */     this.renderData.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/* 28 */     this.renderData.regionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.TextureRegion, (ParallelArray.ChannelInitializer)ParticleChannels.TextureRegionInitializer.get());
/* 29 */     this.renderData.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Color, (ParallelArray.ChannelInitializer)ParticleChannels.ColorInitializer.get());
/* 30 */     this.renderData.scaleChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Scale, (ParallelArray.ChannelInitializer)ParticleChannels.ScaleInitializer.get());
/* 31 */     this.renderData.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation2D, (ParallelArray.ChannelInitializer)ParticleChannels.Rotation2dInitializer.get());
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 36 */     return new BillboardRenderer(this.batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompatible(ParticleBatch<?> batch) {
/* 41 */     return batch instanceof BillboardParticleBatch;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\renderers\BillboardRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */