/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParticleControllerFinalizerInfluencer
/*    */   extends Influencer
/*    */ {
/*    */   ParallelArray.FloatChannel positionChannel;
/*    */   ParallelArray.FloatChannel scaleChannel;
/*    */   ParallelArray.FloatChannel rotationChannel;
/*    */   ParallelArray.ObjectChannel<ParticleController> controllerChannel;
/*    */   boolean hasScale;
/*    */   boolean hasRotation;
/*    */   
/*    */   public void init() {
/* 22 */     this.controllerChannel = (ParallelArray.ObjectChannel<ParticleController>)this.controller.particles.getChannel(ParticleChannels.ParticleController);
/* 23 */     if (this.controllerChannel == null)
/* 24 */       throw new GdxRuntimeException("ParticleController channel not found, specify an influencer which will allocate it please."); 
/* 25 */     this.scaleChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Scale);
/* 26 */     this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Rotation3D);
/* 27 */     this.hasScale = (this.scaleChannel != null);
/* 28 */     this.hasRotation = (this.rotationChannel != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 33 */     this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 38 */     int i = 0, positionOffset = 0, c = this.controller.particles.size;
/* 39 */     for (; i < c; 
/* 40 */       i++, positionOffset += this.positionChannel.strideSize) {
/* 41 */       ParticleController particleController = ((ParticleController[])this.controllerChannel.data)[i];
/* 42 */       float scale = this.hasScale ? this.scaleChannel.data[i] : 1.0F;
/* 43 */       float qx = 0.0F, qy = 0.0F, qz = 0.0F, qw = 1.0F;
/* 44 */       if (this.hasRotation) {
/* 45 */         int rotationOffset = i * this.rotationChannel.strideSize;
/* 46 */         qx = this.rotationChannel.data[rotationOffset + 0];
/* 47 */         qy = this.rotationChannel.data[rotationOffset + 1];
/* 48 */         qz = this.rotationChannel.data[rotationOffset + 2];
/* 49 */         qw = this.rotationChannel.data[rotationOffset + 3];
/*    */       } 
/* 51 */       particleController.setTransform(this.positionChannel.data[positionOffset + 0], this.positionChannel.data[positionOffset + 1], this.positionChannel.data[positionOffset + 2], qx, qy, qz, qw, scale);
/*    */ 
/*    */ 
/*    */       
/* 55 */       particleController.update();
/*    */     } 
/*    */   }
/*    */   
/*    */   public ParticleControllerFinalizerInfluencer copy() {
/* 60 */     return new ParticleControllerFinalizerInfluencer();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\ParticleControllerFinalizerInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */