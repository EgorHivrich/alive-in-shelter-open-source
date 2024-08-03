/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ 
/*    */ public abstract class SimpleInfluencer
/*    */   extends Influencer
/*    */ {
/*    */   public ScaledNumericValue value;
/*    */   ParallelArray.FloatChannel valueChannel;
/*    */   ParallelArray.FloatChannel interpolationChannel;
/*    */   ParallelArray.FloatChannel lifeChannel;
/*    */   ParallelArray.ChannelDescriptor valueChannelDescriptor;
/*    */   
/*    */   public SimpleInfluencer() {
/* 20 */     this.value = new ScaledNumericValue();
/* 21 */     this.value.setHigh(1.0F);
/*    */   }
/*    */   
/*    */   public SimpleInfluencer(SimpleInfluencer billboardScaleinfluencer) {
/* 25 */     this();
/* 26 */     set(billboardScaleinfluencer);
/*    */   }
/*    */   
/*    */   private void set(SimpleInfluencer scaleInfluencer) {
/* 30 */     this.value.load(scaleInfluencer.value);
/* 31 */     this.valueChannelDescriptor = scaleInfluencer.valueChannelDescriptor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 36 */     this.valueChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(this.valueChannelDescriptor);
/* 37 */     ParticleChannels.Interpolation.id = this.controller.particleChannels.newId();
/* 38 */     this.interpolationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation);
/* 39 */     this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateParticles(int startIndex, int count) {
/* 44 */     if (!this.value.isRelative()) {
/* 45 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 46 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 47 */         float start = this.value.newLowValue();
/* 48 */         float diff = this.value.newHighValue() - start;
/* 49 */         this.interpolationChannel.data[a + 0] = start;
/* 50 */         this.interpolationChannel.data[a + 1] = diff;
/* 51 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } else {
/*    */       
/* 55 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 56 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 57 */         float start = this.value.newLowValue();
/* 58 */         float diff = this.value.newHighValue();
/* 59 */         this.interpolationChannel.data[a + 0] = start;
/* 60 */         this.interpolationChannel.data[a + 1] = diff;
/* 61 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 68 */     int i = 0, a = 0, l = 2;
/* 69 */     int c = i + this.controller.particles.size * this.valueChannel.strideSize;
/* 70 */     for (; i < c; 
/* 71 */       i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize, l += this.lifeChannel.strideSize)
/*    */     {
/* 73 */       this.valueChannel.data[i] = this.interpolationChannel.data[a + 0] + this.interpolationChannel.data[a + 1] * this.value
/* 74 */         .getScale(this.lifeChannel.data[l]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 80 */     json.writeValue("value", this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 85 */     this.value = (ScaledNumericValue)json.readValue("value", ScaledNumericValue.class, jsonData);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\SimpleInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */