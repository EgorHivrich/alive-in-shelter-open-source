/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class ParticleControllerInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<ParticleController> templates;
/*     */   ParallelArray.ObjectChannel<ParticleController> particleControllerChannel;
/*     */   
/*     */   public static class Single
/*     */     extends ParticleControllerInfluencer {
/*     */     public Single(ParticleController... templates) {
/*  25 */       super(templates);
/*     */     }
/*     */ 
/*     */     
/*     */     public Single() {}
/*     */ 
/*     */     
/*     */     public Single(Single particleControllerSingle) {
/*  33 */       super(particleControllerSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  38 */       ParticleController first = (ParticleController)this.templates.first();
/*  39 */       for (int i = 0, c = this.controller.particles.capacity; i < c; i++) {
/*  40 */         ParticleController copy = first.copy();
/*  41 */         copy.init();
/*  42 */         ((ParticleController[])this.particleControllerChannel.data)[i] = copy;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  48 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/*  49 */         ((ParticleController[])this.particleControllerChannel.data)[i].start();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void killParticles(int startIndex, int count) {
/*  55 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/*  56 */         ((ParticleController[])this.particleControllerChannel.data)[i].end();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/*  62 */       return new Single(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Random
/*     */     extends ParticleControllerInfluencer
/*     */   {
/*     */     ParticleControllerPool pool;
/*     */     
/*     */     private class ParticleControllerPool
/*     */       extends Pool<ParticleController> {
/*     */       public ParticleController newObject() {
/*  74 */         ParticleController controller = ((ParticleController)ParticleControllerInfluencer.Random.this.templates.random()).copy();
/*  75 */         controller.init();
/*  76 */         return controller;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {
/*  82 */         for (int i = 0, free = ParticleControllerInfluencer.Random.this.pool.getFree(); i < free; i++) {
/*  83 */           ((ParticleController)ParticleControllerInfluencer.Random.this.pool.obtain()).dispose();
/*     */         }
/*  85 */         super.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Random() {
/*  93 */       this.pool = new ParticleControllerPool();
/*     */     }
/*     */     public Random(ParticleController... templates) {
/*  96 */       super(templates);
/*  97 */       this.pool = new ParticleControllerPool();
/*     */     }
/*     */     
/*     */     public Random(Random particleControllerRandom) {
/* 101 */       super(particleControllerRandom);
/* 102 */       this.pool = new ParticleControllerPool();
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/* 107 */       this.pool.clear();
/*     */       
/* 109 */       for (int i = 0; i < this.controller.emitter.maxParticleCount; i++) {
/* 110 */         this.pool.free(this.pool.newObject());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 116 */       this.pool.clear();
/* 117 */       super.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/* 122 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/* 123 */         ParticleController controller = (ParticleController)this.pool.obtain();
/* 124 */         controller.start();
/* 125 */         ((ParticleController[])this.particleControllerChannel.data)[i] = controller;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void killParticles(int startIndex, int count) {
/* 131 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/* 132 */         ParticleController controller = ((ParticleController[])this.particleControllerChannel.data)[i];
/* 133 */         controller.end();
/* 134 */         this.pool.free(controller);
/* 135 */         ((ParticleController[])this.particleControllerChannel.data)[i] = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/* 141 */       return new Random(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParticleControllerInfluencer() {
/* 149 */     this.templates = new Array(true, 1, ParticleController.class);
/*     */   }
/*     */   
/*     */   public ParticleControllerInfluencer(ParticleController... templates) {
/* 153 */     this.templates = new Array((Object[])templates);
/*     */   }
/*     */   
/*     */   public ParticleControllerInfluencer(ParticleControllerInfluencer influencer) {
/* 157 */     this((ParticleController[])influencer.templates.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 162 */     this.particleControllerChannel = (ParallelArray.ObjectChannel<ParticleController>)this.controller.particles.addChannel(ParticleChannels.ParticleController);
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 167 */     for (int i = 0; i < this.controller.particles.size; i++) {
/* 168 */       ((ParticleController[])this.particleControllerChannel.data)[i].end();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 174 */     if (this.controller != null) {
/* 175 */       for (int i = 0; i < this.controller.particles.size; i++) {
/* 176 */         ParticleController controller = ((ParticleController[])this.particleControllerChannel.data)[i];
/* 177 */         if (controller != null) {
/* 178 */           controller.dispose();
/* 179 */           ((ParticleController[])this.particleControllerChannel.data)[i] = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 187 */     ResourceData.SaveData data = resources.createSaveData();
/* 188 */     Array<ParticleEffect> effects = manager.getAll(ParticleEffect.class, new Array());
/*     */     
/* 190 */     Array<ParticleController> controllers = new Array(this.templates);
/* 191 */     Array<IntArray> effectsIndices = new Array();
/*     */     
/* 193 */     for (int i = 0; i < effects.size && controllers.size > 0; i++) {
/* 194 */       ParticleEffect effect = (ParticleEffect)effects.get(i);
/* 195 */       Array<ParticleController> effectControllers = effect.getControllers();
/* 196 */       Iterator<ParticleController> iterator = controllers.iterator();
/* 197 */       IntArray indices = null;
/* 198 */       while (iterator.hasNext()) {
/* 199 */         ParticleController controller = iterator.next();
/* 200 */         int index = -1;
/* 201 */         if ((index = effectControllers.indexOf(controller, true)) > -1) {
/* 202 */           if (indices == null) {
/* 203 */             indices = new IntArray();
/*     */           }
/* 205 */           iterator.remove();
/* 206 */           indices.add(index);
/*     */         } 
/*     */       } 
/*     */       
/* 210 */       if (indices != null) {
/* 211 */         data.saveAsset(manager.getAssetFileName(effect), ParticleEffect.class);
/* 212 */         effectsIndices.add(indices);
/*     */       } 
/*     */     } 
/* 215 */     data.save("indices", effectsIndices);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 220 */     ResourceData.SaveData data = resources.getSaveData();
/* 221 */     Array<IntArray> effectsIndices = (Array<IntArray>)data.load("indices");
/*     */     
/* 223 */     Iterator<IntArray> iterator = effectsIndices.iterator(); AssetDescriptor descriptor;
/* 224 */     while ((descriptor = data.loadAsset()) != null) {
/* 225 */       ParticleEffect effect = (ParticleEffect)manager.get(descriptor);
/* 226 */       if (effect == null)
/* 227 */         throw new RuntimeException("Template is null"); 
/* 228 */       Array<ParticleController> effectControllers = effect.getControllers();
/* 229 */       IntArray effectIndices = iterator.next();
/*     */       
/* 231 */       for (int i = 0, n = effectIndices.size; i < n; i++)
/* 232 */         this.templates.add(effectControllers.get(effectIndices.get(i))); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\ParticleControllerInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */