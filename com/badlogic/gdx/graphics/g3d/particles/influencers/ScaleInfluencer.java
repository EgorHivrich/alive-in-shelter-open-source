/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScaleInfluencer
/*    */   extends SimpleInfluencer
/*    */ {
/*    */   public ScaleInfluencer() {
/* 12 */     this.valueChannelDescriptor = ParticleChannels.Scale;
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateParticles(int startIndex, int count) {
/* 17 */     if (this.value.isRelative()) {
/* 18 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 19 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 20 */         float start = this.value.newLowValue() * this.controller.scale.x;
/* 21 */         float diff = this.value.newHighValue() * this.controller.scale.x;
/* 22 */         this.interpolationChannel.data[a + 0] = start;
/* 23 */         this.interpolationChannel.data[a + 1] = diff;
/* 24 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } else {
/*    */       
/* 28 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 29 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 30 */         float start = this.value.newLowValue() * this.controller.scale.x;
/* 31 */         float diff = this.value.newHighValue() * this.controller.scale.x - start;
/* 32 */         this.interpolationChannel.data[a + 0] = start;
/* 33 */         this.interpolationChannel.data[a + 1] = diff;
/* 34 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public ScaleInfluencer(ScaleInfluencer scaleInfluencer) {
/* 40 */     super(scaleInfluencer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 45 */     return new ScaleInfluencer(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\ScaleInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */