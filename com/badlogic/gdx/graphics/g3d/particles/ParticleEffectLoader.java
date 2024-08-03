/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleEffectLoader
/*     */   extends AsynchronousAssetLoader<ParticleEffect, ParticleEffectLoader.ParticleEffectLoadParameter>
/*     */ {
/*  33 */   protected Array<ObjectMap.Entry<String, ResourceData<ParticleEffect>>> items = new Array();
/*     */   
/*     */   public ParticleEffectLoader(FileHandleResolver resolver) {
/*  36 */     super(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, ParticleEffectLoadParameter parameter) {}
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ParticleEffectLoadParameter parameter) {
/*  45 */     Json json = new Json();
/*  46 */     ResourceData<ParticleEffect> data = (ResourceData<ParticleEffect>)json.fromJson(ResourceData.class, file);
/*  47 */     Array<ResourceData.AssetData> assets = null;
/*  48 */     synchronized (this.items) {
/*  49 */       ObjectMap.Entry<String, ResourceData<ParticleEffect>> entry = new ObjectMap.Entry();
/*  50 */       entry.key = fileName;
/*  51 */       entry.value = data;
/*  52 */       this.items.add(entry);
/*  53 */       assets = data.getAssets();
/*     */     } 
/*     */     
/*  56 */     Array<AssetDescriptor> descriptors = new Array();
/*  57 */     for (ResourceData.AssetData<?> assetData : assets) {
/*     */ 
/*     */       
/*  60 */       if (!resolve(assetData.filename).exists()) {
/*  61 */         assetData.filename = file.parent().child(Gdx.files.internal(assetData.filename).name()).path();
/*     */       }
/*     */       
/*  64 */       if (assetData.type == ParticleEffect.class) {
/*  65 */         descriptors.add(new AssetDescriptor(assetData.filename, assetData.type, parameter)); continue;
/*     */       } 
/*  67 */       descriptors.add(new AssetDescriptor(assetData.filename, assetData.type));
/*     */     } 
/*     */     
/*  70 */     return descriptors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(ParticleEffect effect, ParticleEffectSaveParameter parameter) throws IOException {
/*  76 */     ResourceData<ParticleEffect> data = new ResourceData<ParticleEffect>(effect);
/*     */ 
/*     */     
/*  79 */     effect.save(parameter.manager, data);
/*     */ 
/*     */     
/*  82 */     if (parameter.batches != null) {
/*  83 */       for (ParticleBatch<?> batch : parameter.batches) {
/*  84 */         boolean save = false;
/*  85 */         for (ParticleController controller : effect.getControllers()) {
/*  86 */           if (controller.renderer.isCompatible(batch)) {
/*  87 */             save = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*  92 */         if (save) batch.save(parameter.manager, data);
/*     */       
/*     */       } 
/*     */     }
/*     */     
/*  97 */     Json json = new Json();
/*  98 */     json.toJson(data, parameter.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleEffect loadSync(AssetManager manager, String fileName, FileHandle file, ParticleEffectLoadParameter parameter) {
/* 103 */     ResourceData<ParticleEffect> effectData = null;
/* 104 */     synchronized (this.items) {
/* 105 */       for (int i = 0; i < this.items.size; i++) {
/* 106 */         ObjectMap.Entry<String, ResourceData<ParticleEffect>> entry = (ObjectMap.Entry<String, ResourceData<ParticleEffect>>)this.items.get(i);
/* 107 */         if (((String)entry.key).equals(fileName)) {
/* 108 */           effectData = (ResourceData<ParticleEffect>)entry.value;
/* 109 */           this.items.removeIndex(i);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 115 */     ((ParticleEffect)effectData.resource).load(manager, effectData);
/* 116 */     if (parameter != null) {
/* 117 */       if (parameter.batches != null) {
/* 118 */         for (ParticleBatch<?> batch : parameter.batches) {
/* 119 */           batch.load(manager, effectData);
/*     */         }
/*     */       }
/* 122 */       ((ParticleEffect)effectData.resource).setBatch(parameter.batches);
/*     */     } 
/* 124 */     return (ParticleEffect)effectData.resource;
/*     */   }
/*     */   
/*     */   private <T> T find(Array<?> array, Class<T> type) {
/* 128 */     for (Object object : array) {
/* 129 */       if (ClassReflection.isAssignableFrom(type, object.getClass())) return (T)object; 
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   public static class ParticleEffectLoadParameter extends AssetLoaderParameters<ParticleEffect> {
/*     */     Array<ParticleBatch<?>> batches;
/*     */     
/*     */     public ParticleEffectLoadParameter(Array<ParticleBatch<?>> batches) {
/* 138 */       this.batches = batches;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ParticleEffectSaveParameter
/*     */     extends AssetLoaderParameters<ParticleEffect>
/*     */   {
/*     */     Array<ParticleBatch<?>> batches;
/*     */     FileHandle file;
/*     */     AssetManager manager;
/*     */     
/*     */     public ParticleEffectSaveParameter(FileHandle file, AssetManager manager, Array<ParticleBatch<?>> batches) {
/* 151 */       this.batches = batches;
/* 152 */       this.file = file;
/* 153 */       this.manager = manager;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParticleEffectLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */