/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Material;
/*    */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*    */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*    */ import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ModelInstanceParticleBatch;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*    */ 
/*    */ public class ModelInstanceRenderer
/*    */   extends ParticleControllerRenderer<ModelInstanceControllerRenderData, ModelInstanceParticleBatch> {
/*    */   private boolean hasColor;
/*    */   
/*    */   public ModelInstanceRenderer() {
/* 18 */     super(new ModelInstanceControllerRenderData());
/*    */   }
/*    */   private boolean hasScale; private boolean hasRotation;
/*    */   public ModelInstanceRenderer(ModelInstanceParticleBatch batch) {
/* 22 */     this();
/* 23 */     setBatch((ParticleBatch<?>)batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 28 */     this.renderData.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 33 */     this.renderData.modelInstanceChannel = (ParallelArray.ObjectChannel<ModelInstance>)this.controller.particles.getChannel(ParticleChannels.ModelInstance);
/* 34 */     this.renderData.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Color);
/* 35 */     this.renderData.scaleChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Scale);
/* 36 */     this.renderData.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Rotation3D);
/* 37 */     this.hasColor = (this.renderData.colorChannel != null);
/* 38 */     this.hasScale = (this.renderData.scaleChannel != null);
/* 39 */     this.hasRotation = (this.renderData.rotationChannel != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 44 */     int i = 0, positionOffset = 0, c = this.controller.particles.size;
/* 45 */     for (; i < c; 
/* 46 */       i++, positionOffset += this.renderData.positionChannel.strideSize) {
/* 47 */       ModelInstance instance = ((ModelInstance[])this.renderData.modelInstanceChannel.data)[i];
/* 48 */       float scale = this.hasScale ? this.renderData.scaleChannel.data[i] : 1.0F;
/* 49 */       float qx = 0.0F, qy = 0.0F, qz = 0.0F, qw = 1.0F;
/* 50 */       if (this.hasRotation) {
/* 51 */         int rotationOffset = i * this.renderData.rotationChannel.strideSize;
/* 52 */         qx = this.renderData.rotationChannel.data[rotationOffset + 0];
/* 53 */         qy = this.renderData.rotationChannel.data[rotationOffset + 1];
/* 54 */         qz = this.renderData.rotationChannel.data[rotationOffset + 2];
/* 55 */         qw = this.renderData.rotationChannel.data[rotationOffset + 3];
/*    */       } 
/*    */       
/* 58 */       instance.transform.set(this.renderData.positionChannel.data[positionOffset + 0], this.renderData.positionChannel.data[positionOffset + 1], this.renderData.positionChannel.data[positionOffset + 2], qx, qy, qz, qw, scale, scale, scale);
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 63 */       if (this.hasColor) {
/* 64 */         int colorOffset = i * this.renderData.colorChannel.strideSize;
/* 65 */         ColorAttribute colorAttribute = (ColorAttribute)((Material)instance.materials.get(0)).get(ColorAttribute.Diffuse);
/* 66 */         BlendingAttribute blendingAttribute = (BlendingAttribute)((Material)instance.materials.get(0)).get(BlendingAttribute.Type);
/* 67 */         colorAttribute.color.r = this.renderData.colorChannel.data[colorOffset + 0];
/* 68 */         colorAttribute.color.g = this.renderData.colorChannel.data[colorOffset + 1];
/* 69 */         colorAttribute.color.b = this.renderData.colorChannel.data[colorOffset + 2];
/* 70 */         if (blendingAttribute != null)
/* 71 */           blendingAttribute.opacity = this.renderData.colorChannel.data[colorOffset + 3]; 
/*    */       } 
/*    */     } 
/* 74 */     super.update();
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 79 */     return new ModelInstanceRenderer(this.batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompatible(ParticleBatch<?> batch) {
/* 84 */     return batch instanceof ModelInstanceParticleBatch;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\renderers\ModelInstanceRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */