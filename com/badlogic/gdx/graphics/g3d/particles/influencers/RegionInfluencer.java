/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ 
/*     */ public abstract class RegionInfluencer extends Influencer {
/*     */   public Array<AspectTextureRegion> regions;
/*     */   ParallelArray.FloatChannel regionChannel;
/*     */   
/*     */   public static class Single extends RegionInfluencer {
/*     */     public Single() {}
/*     */     
/*     */     public Single(Single regionInfluencer) {
/*  20 */       super(regionInfluencer);
/*     */     }
/*     */     
/*     */     public Single(TextureRegion textureRegion) {
/*  24 */       super(new TextureRegion[] { textureRegion });
/*     */     }
/*     */     
/*     */     public Single(Texture texture) {
/*  28 */       super(texture);
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  33 */       RegionInfluencer.AspectTextureRegion region = ((RegionInfluencer.AspectTextureRegion[])this.regions.items)[0];
/*  34 */       int i = 0, c = this.controller.emitter.maxParticleCount * this.regionChannel.strideSize;
/*  35 */       for (; i < c; 
/*  36 */         i += this.regionChannel.strideSize) {
/*  37 */         this.regionChannel.data[i + 0] = region.u;
/*  38 */         this.regionChannel.data[i + 1] = region.v;
/*  39 */         this.regionChannel.data[i + 2] = region.u2;
/*  40 */         this.regionChannel.data[i + 3] = region.v2;
/*  41 */         this.regionChannel.data[i + 4] = 0.5F;
/*  42 */         this.regionChannel.data[i + 5] = region.halfInvAspectRatio;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/*  48 */       return new Single(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Random extends RegionInfluencer {
/*     */     public Random() {}
/*     */     
/*     */     public Random(Random regionInfluencer) {
/*  56 */       super(regionInfluencer);
/*     */     }
/*     */     public Random(TextureRegion textureRegion) {
/*  59 */       super(new TextureRegion[] { textureRegion });
/*     */     }
/*     */     
/*     */     public Random(Texture texture) {
/*  63 */       super(texture);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  68 */       int i = startIndex * this.regionChannel.strideSize, c = i + count * this.regionChannel.strideSize;
/*  69 */       for (; i < c; 
/*  70 */         i += this.regionChannel.strideSize) {
/*  71 */         RegionInfluencer.AspectTextureRegion region = (RegionInfluencer.AspectTextureRegion)this.regions.random();
/*  72 */         this.regionChannel.data[i + 0] = region.u;
/*  73 */         this.regionChannel.data[i + 1] = region.v;
/*  74 */         this.regionChannel.data[i + 2] = region.u2;
/*  75 */         this.regionChannel.data[i + 3] = region.v2;
/*  76 */         this.regionChannel.data[i + 4] = 0.5F;
/*  77 */         this.regionChannel.data[i + 5] = region.halfInvAspectRatio;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/*  83 */       return new Random(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Animated extends RegionInfluencer {
/*     */     ParallelArray.FloatChannel lifeChannel;
/*     */     
/*     */     public Animated() {}
/*     */     
/*     */     public Animated(Animated regionInfluencer) {
/*  93 */       super(regionInfluencer);
/*     */     }
/*     */     
/*     */     public Animated(TextureRegion textureRegion) {
/*  97 */       super(new TextureRegion[] { textureRegion });
/*     */     }
/*     */     
/*     */     public Animated(Texture texture) {
/* 101 */       super(texture);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 106 */       super.allocateChannels();
/* 107 */       this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 112 */       int i = 0, l = 2;
/* 113 */       int c = this.controller.particles.size * this.regionChannel.strideSize;
/* 114 */       for (; i < c; 
/* 115 */         i += this.regionChannel.strideSize, l += this.lifeChannel.strideSize) {
/* 116 */         RegionInfluencer.AspectTextureRegion region = (RegionInfluencer.AspectTextureRegion)this.regions.get((int)(this.lifeChannel.data[l] * (this.regions.size - 1)));
/* 117 */         this.regionChannel.data[i + 0] = region.u;
/* 118 */         this.regionChannel.data[i + 1] = region.v;
/* 119 */         this.regionChannel.data[i + 2] = region.u2;
/* 120 */         this.regionChannel.data[i + 3] = region.v2;
/* 121 */         this.regionChannel.data[i + 4] = 0.5F;
/* 122 */         this.regionChannel.data[i + 5] = region.halfInvAspectRatio;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Animated copy() {
/* 128 */       return new Animated(this);
/*     */     } }
/*     */   
/*     */   public static class AspectTextureRegion {
/*     */     public float u;
/*     */     public float v;
/*     */     public float u2;
/*     */     public float v2;
/*     */     public float halfInvAspectRatio;
/*     */     
/*     */     public AspectTextureRegion() {}
/*     */     
/*     */     public AspectTextureRegion(AspectTextureRegion aspectTextureRegion) {
/* 141 */       set(aspectTextureRegion);
/*     */     }
/*     */     
/*     */     public AspectTextureRegion(TextureRegion region) {
/* 145 */       set(region);
/*     */     }
/*     */     
/*     */     public void set(TextureRegion region) {
/* 149 */       this.u = region.getU();
/* 150 */       this.v = region.getV();
/* 151 */       this.u2 = region.getU2();
/* 152 */       this.v2 = region.getV2();
/* 153 */       this.halfInvAspectRatio = 0.5F * region.getRegionHeight() / region.getRegionWidth();
/*     */     }
/*     */     
/*     */     public void set(AspectTextureRegion aspectTextureRegion) {
/* 157 */       this.u = aspectTextureRegion.u;
/* 158 */       this.v = aspectTextureRegion.v;
/* 159 */       this.u2 = aspectTextureRegion.u2;
/* 160 */       this.v2 = aspectTextureRegion.v2;
/* 161 */       this.halfInvAspectRatio = aspectTextureRegion.halfInvAspectRatio;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegionInfluencer(int regionsCount) {
/* 169 */     this.regions = new Array(false, regionsCount, AspectTextureRegion.class);
/*     */   }
/*     */   
/*     */   public RegionInfluencer() {
/* 173 */     this(1);
/* 174 */     AspectTextureRegion aspectRegion = new AspectTextureRegion();
/* 175 */     aspectRegion.u = aspectRegion.v = 0.0F;
/* 176 */     aspectRegion.u2 = aspectRegion.v2 = 1.0F;
/* 177 */     aspectRegion.halfInvAspectRatio = 0.5F;
/* 178 */     this.regions.add(aspectRegion);
/*     */   }
/*     */ 
/*     */   
/*     */   public RegionInfluencer(TextureRegion... regions) {
/* 183 */     this.regions = new Array(false, regions.length, AspectTextureRegion.class);
/* 184 */     add(regions);
/*     */   }
/*     */   
/*     */   public RegionInfluencer(Texture texture) {
/* 188 */     this(new TextureRegion[] { new TextureRegion(texture) });
/*     */   }
/*     */   
/*     */   public RegionInfluencer(RegionInfluencer regionInfluencer) {
/* 192 */     this(regionInfluencer.regions.size);
/* 193 */     this.regions.ensureCapacity(regionInfluencer.regions.size);
/* 194 */     for (int i = 0; i < regionInfluencer.regions.size; i++) {
/* 195 */       this.regions.add(new AspectTextureRegion((AspectTextureRegion)regionInfluencer.regions.get(i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void add(TextureRegion... regions) {
/* 200 */     this.regions.ensureCapacity(regions.length);
/* 201 */     for (TextureRegion region : regions) {
/* 202 */       this.regions.add(new AspectTextureRegion(region));
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 207 */     this.regions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 212 */     this.regionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.TextureRegion);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 217 */     json.writeValue("regions", this.regions, Array.class, AspectTextureRegion.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 222 */     this.regions.clear();
/* 223 */     this.regions.addAll((Array)json.readValue("regions", Array.class, AspectTextureRegion.class, jsonData));
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\RegionInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */