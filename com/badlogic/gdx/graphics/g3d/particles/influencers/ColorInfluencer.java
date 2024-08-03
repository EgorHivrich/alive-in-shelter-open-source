/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.GradientColorValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ 
/*     */ public abstract class ColorInfluencer
/*     */   extends Influencer {
/*     */   ParallelArray.FloatChannel colorChannel;
/*     */   
/*     */   public static class Random
/*     */     extends ColorInfluencer {
/*     */     ParallelArray.FloatChannel colorChannel;
/*     */     
/*     */     public void allocateChannels() {
/*  21 */       this.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Color);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  26 */       int i = startIndex * this.colorChannel.strideSize, c = i + count * this.colorChannel.strideSize;
/*  27 */       for (; i < c; 
/*  28 */         i += this.colorChannel.strideSize) {
/*  29 */         this.colorChannel.data[i + 0] = MathUtils.random();
/*  30 */         this.colorChannel.data[i + 1] = MathUtils.random();
/*  31 */         this.colorChannel.data[i + 2] = MathUtils.random();
/*  32 */         this.colorChannel.data[i + 3] = MathUtils.random();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/*  38 */       return new Random();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Single
/*     */     extends ColorInfluencer {
/*     */     ParallelArray.FloatChannel alphaInterpolationChannel;
/*     */     ParallelArray.FloatChannel lifeChannel;
/*     */     public ScaledNumericValue alphaValue;
/*     */     public GradientColorValue colorValue;
/*     */     
/*     */     public Single() {
/*  50 */       this.colorValue = new GradientColorValue();
/*  51 */       this.alphaValue = new ScaledNumericValue();
/*  52 */       this.alphaValue.setHigh(1.0F);
/*     */     }
/*     */     
/*     */     public Single(Single billboardColorInfluencer) {
/*  56 */       this();
/*  57 */       set(billboardColorInfluencer);
/*     */     }
/*     */     
/*     */     public void set(Single colorInfluencer) {
/*  61 */       this.colorValue.load(colorInfluencer.colorValue);
/*  62 */       this.alphaValue.load(colorInfluencer.alphaValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/*  67 */       super.allocateChannels();
/*     */       
/*  69 */       ParticleChannels.Interpolation.id = this.controller.particleChannels.newId();
/*  70 */       this.alphaInterpolationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation);
/*  71 */       this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  76 */       int i = startIndex * this.colorChannel.strideSize;
/*  77 */       int a = startIndex * this.alphaInterpolationChannel.strideSize;
/*  78 */       int l = startIndex * this.lifeChannel.strideSize + 2;
/*  79 */       int c = i + count * this.colorChannel.strideSize;
/*  80 */       for (; i < c; 
/*  81 */         i += this.colorChannel.strideSize, 
/*  82 */         a += this.alphaInterpolationChannel.strideSize, 
/*  83 */         l += this.lifeChannel.strideSize) {
/*  84 */         float alphaStart = this.alphaValue.newLowValue();
/*  85 */         float alphaDiff = this.alphaValue.newHighValue() - alphaStart;
/*  86 */         this.colorValue.getColor(0.0F, this.colorChannel.data, i);
/*  87 */         this.colorChannel.data[i + 3] = alphaStart + alphaDiff * this.alphaValue.getScale(this.lifeChannel.data[l]);
/*  88 */         this.alphaInterpolationChannel.data[a + 0] = alphaStart;
/*  89 */         this.alphaInterpolationChannel.data[a + 1] = alphaDiff;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/*  95 */       int i = 0, a = 0, l = 2;
/*  96 */       int c = i + this.controller.particles.size * this.colorChannel.strideSize;
/*  97 */       for (; i < c; 
/*  98 */         i += this.colorChannel.strideSize, a += this.alphaInterpolationChannel.strideSize, l += this.lifeChannel.strideSize) {
/*     */         
/* 100 */         float lifePercent = this.lifeChannel.data[l];
/* 101 */         this.colorValue.getColor(lifePercent, this.colorChannel.data, i);
/* 102 */         this.colorChannel.data[i + 3] = this.alphaInterpolationChannel.data[a + 0] + this.alphaInterpolationChannel.data[a + 1] * this.alphaValue
/* 103 */           .getScale(lifePercent);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/* 109 */       return new Single(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 114 */       json.writeValue("alpha", this.alphaValue);
/* 115 */       json.writeValue("color", this.colorValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 120 */       this.alphaValue = (ScaledNumericValue)json.readValue("alpha", ScaledNumericValue.class, jsonData);
/* 121 */       this.colorValue = (GradientColorValue)json.readValue("color", GradientColorValue.class, jsonData);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 129 */     this.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Color);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\ColorInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */