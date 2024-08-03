/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ 
/*     */ 
/*     */ public abstract class ModelInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<Model> models;
/*     */   ParallelArray.ObjectChannel<ModelInstance> modelChannel;
/*     */   
/*     */   public static class Single
/*     */     extends ModelInfluencer
/*     */   {
/*     */     public Single() {}
/*     */     
/*     */     public Single(Single influencer) {
/*  27 */       super(influencer);
/*     */     }
/*     */     
/*     */     public Single(Model... models) {
/*  31 */       super(models);
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  36 */       Model first = (Model)this.models.first();
/*  37 */       for (int i = 0, c = this.controller.emitter.maxParticleCount; i < c; i++) {
/*  38 */         ((ModelInstance[])this.modelChannel.data)[i] = new ModelInstance(first);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/*  44 */       return new Single(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Random
/*     */     extends ModelInfluencer {
/*     */     ModelInstancePool pool;
/*     */     
/*     */     private class ModelInstancePool
/*     */       extends Pool<ModelInstance> {
/*     */       public ModelInstance newObject() {
/*  55 */         return new ModelInstance((Model)ModelInfluencer.Random.this.models.random());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Random() {
/*  62 */       this.pool = new ModelInstancePool();
/*     */     }
/*     */     
/*     */     public Random(Random influencer) {
/*  66 */       super(influencer);
/*  67 */       this.pool = new ModelInstancePool();
/*     */     }
/*     */     
/*     */     public Random(Model... models) {
/*  71 */       super(models);
/*  72 */       this.pool = new ModelInstancePool();
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  77 */       this.pool.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  82 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/*  83 */         ((ModelInstance[])this.modelChannel.data)[i] = (ModelInstance)this.pool.obtain();
/*     */       }
/*     */     }
/*     */     
/*     */     public void killParticles(int startIndex, int count) {
/*  88 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/*  89 */         this.pool.free(((ModelInstance[])this.modelChannel.data)[i]);
/*  90 */         ((ModelInstance[])this.modelChannel.data)[i] = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/*  96 */       return new Random(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInfluencer() {
/* 104 */     this.models = new Array(true, 1, Model.class);
/*     */   }
/*     */   
/*     */   public ModelInfluencer(Model... models) {
/* 108 */     this.models = new Array((Object[])models);
/*     */   }
/*     */   
/*     */   public ModelInfluencer(ModelInfluencer influencer) {
/* 112 */     this((Model[])influencer.models.toArray(Model.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 117 */     this.modelChannel = (ParallelArray.ObjectChannel<ModelInstance>)this.controller.particles.addChannel(ParticleChannels.ModelInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 122 */     ResourceData.SaveData data = resources.createSaveData();
/* 123 */     for (Model model : this.models) {
/* 124 */       data.saveAsset(manager.getAssetFileName(model), Model.class);
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 129 */     ResourceData.SaveData data = resources.getSaveData();
/*     */     AssetDescriptor descriptor;
/* 131 */     while ((descriptor = data.loadAsset()) != null) {
/* 132 */       Model model = (Model)manager.get(descriptor);
/* 133 */       if (model == null)
/* 134 */         throw new RuntimeException("Model is null"); 
/* 135 */       this.models.add(model);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\ModelInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */