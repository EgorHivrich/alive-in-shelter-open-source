/*     */ package com.badlogic.gdx.assets;
/*     */ 
/*     */ import com.badlogic.gdx.assets.loaders.AssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
/*     */ import com.badlogic.gdx.assets.loaders.MusicLoader;
/*     */ import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
/*     */ import com.badlogic.gdx.assets.loaders.PixmapLoader;
/*     */ import com.badlogic.gdx.assets.loaders.SkinLoader;
/*     */ import com.badlogic.gdx.assets.loaders.SoundLoader;
/*     */ import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
/*     */ import com.badlogic.gdx.assets.loaders.TextureLoader;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*     */ import com.badlogic.gdx.audio.Music;
/*     */ import com.badlogic.gdx.audio.Sound;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g2d.PolygonRegion;
/*     */ import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
/*     */ import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
/*     */ import com.badlogic.gdx.scenes.scene2d.ui.Skin;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.BaseJsonReader;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.I18NBundle;
/*     */ import com.badlogic.gdx.utils.JsonReader;
/*     */ import com.badlogic.gdx.utils.Logger;
/*     */ import com.badlogic.gdx.utils.ObjectIntMap;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
/*     */ import com.badlogic.gdx.utils.UBJsonReader;
/*     */ import com.badlogic.gdx.utils.async.AsyncExecutor;
/*     */ import com.badlogic.gdx.utils.async.ThreadUtils;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import java.util.Stack;
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
/*     */ 
/*     */ 
/*     */ public class AssetManager
/*     */   implements Disposable
/*     */ {
/*  65 */   final ObjectMap<Class, ObjectMap<String, RefCountedContainer>> assets = new ObjectMap();
/*  66 */   final ObjectMap<String, Class> assetTypes = new ObjectMap();
/*  67 */   final ObjectMap<String, Array<String>> assetDependencies = new ObjectMap();
/*  68 */   final ObjectSet<String> injected = new ObjectSet();
/*     */   
/*  70 */   final ObjectMap<Class, ObjectMap<String, AssetLoader>> loaders = new ObjectMap();
/*  71 */   final Array<AssetDescriptor> loadQueue = new Array();
/*     */   
/*     */   final AsyncExecutor executor;
/*  74 */   final Stack<AssetLoadingTask> tasks = new Stack<AssetLoadingTask>();
/*  75 */   AssetErrorListener listener = null;
/*  76 */   int loaded = 0;
/*  77 */   int toLoad = 0;
/*     */   
/*     */   final FileHandleResolver resolver;
/*     */   
/*  81 */   Logger log = new Logger("AssetManager", 0);
/*     */ 
/*     */   
/*     */   public AssetManager() {
/*  85 */     this((FileHandleResolver)new InternalFileHandleResolver());
/*     */   }
/*     */ 
/*     */   
/*     */   public AssetManager(FileHandleResolver resolver) {
/*  90 */     this(resolver, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AssetManager(FileHandleResolver resolver, boolean defaultLoaders) {
/*  97 */     this.resolver = resolver;
/*  98 */     if (defaultLoaders) {
/*  99 */       setLoader(BitmapFont.class, (AssetLoader<BitmapFont, AssetLoaderParameters<BitmapFont>>)new BitmapFontLoader(resolver));
/* 100 */       setLoader(Music.class, (AssetLoader<Music, AssetLoaderParameters<Music>>)new MusicLoader(resolver));
/* 101 */       setLoader(Pixmap.class, (AssetLoader<Pixmap, AssetLoaderParameters<Pixmap>>)new PixmapLoader(resolver));
/* 102 */       setLoader(Sound.class, (AssetLoader<Sound, AssetLoaderParameters<Sound>>)new SoundLoader(resolver));
/* 103 */       setLoader(TextureAtlas.class, (AssetLoader<TextureAtlas, AssetLoaderParameters<TextureAtlas>>)new TextureAtlasLoader(resolver));
/* 104 */       setLoader(Texture.class, (AssetLoader<Texture, AssetLoaderParameters<Texture>>)new TextureLoader(resolver));
/* 105 */       setLoader(Skin.class, (AssetLoader<Skin, AssetLoaderParameters<Skin>>)new SkinLoader(resolver));
/* 106 */       setLoader(ParticleEffect.class, (AssetLoader<ParticleEffect, AssetLoaderParameters<ParticleEffect>>)new ParticleEffectLoader(resolver));
/* 107 */       setLoader(ParticleEffect.class, (AssetLoader<ParticleEffect, AssetLoaderParameters<ParticleEffect>>)new ParticleEffectLoader(resolver));
/*     */       
/* 109 */       setLoader(PolygonRegion.class, (AssetLoader<PolygonRegion, AssetLoaderParameters<PolygonRegion>>)new PolygonRegionLoader(resolver));
/* 110 */       setLoader(I18NBundle.class, (AssetLoader<I18NBundle, AssetLoaderParameters<I18NBundle>>)new I18NBundleLoader(resolver));
/* 111 */       setLoader(Model.class, ".g3dj", (AssetLoader<Model, AssetLoaderParameters<Model>>)new G3dModelLoader((BaseJsonReader)new JsonReader(), resolver));
/* 112 */       setLoader(Model.class, ".g3db", (AssetLoader<Model, AssetLoaderParameters<Model>>)new G3dModelLoader((BaseJsonReader)new UBJsonReader(), resolver));
/* 113 */       setLoader(Model.class, ".obj", (AssetLoader<Model, AssetLoaderParameters<Model>>)new ObjLoader(resolver));
/*     */     } 
/* 115 */     this.executor = new AsyncExecutor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandleResolver getFileHandleResolver() {
/* 122 */     return this.resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> T get(String fileName) {
/* 128 */     Class<T> type = (Class<T>)this.assetTypes.get(fileName);
/* 129 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 130 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 131 */     if (assetsByType == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 132 */     RefCountedContainer assetContainer = (RefCountedContainer)assetsByType.get(fileName);
/* 133 */     if (assetContainer == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 134 */     T asset = assetContainer.getObject(type);
/* 135 */     if (asset == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 136 */     return asset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> T get(String fileName, Class<T> type) {
/* 143 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 144 */     if (assetsByType == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 145 */     RefCountedContainer assetContainer = (RefCountedContainer)assetsByType.get(fileName);
/* 146 */     if (assetContainer == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 147 */     T asset = assetContainer.getObject(type);
/* 148 */     if (asset == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 149 */     return asset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> Array<T> getAll(Class<T> type, Array<T> out) {
/* 155 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 156 */     if (assetsByType != null) {
/* 157 */       for (ObjectMap.Entries<ObjectMap.Entry<String, RefCountedContainer>> entries = assetsByType.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, RefCountedContainer> asset = entries.next();
/* 158 */         out.add(((RefCountedContainer)asset.value).getObject(type)); }
/*     */     
/*     */     }
/* 161 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> T get(AssetDescriptor<T> assetDescriptor) {
/* 167 */     return get(assetDescriptor.fileName, assetDescriptor.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unload(String fileName) {
/* 175 */     if (this.tasks.size() > 0) {
/* 176 */       AssetLoadingTask currAsset = this.tasks.firstElement();
/* 177 */       if (currAsset.assetDesc.fileName.equals(fileName)) {
/* 178 */         currAsset.cancel = true;
/* 179 */         this.log.debug("Unload (from tasks): " + fileName);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 185 */     int foundIndex = -1;
/* 186 */     for (int i = 0; i < this.loadQueue.size; i++) {
/* 187 */       if (((AssetDescriptor)this.loadQueue.get(i)).fileName.equals(fileName)) {
/* 188 */         foundIndex = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 192 */     if (foundIndex != -1) {
/* 193 */       this.toLoad--;
/* 194 */       this.loadQueue.removeIndex(foundIndex);
/* 195 */       this.log.debug("Unload (from queue): " + fileName);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 200 */     Class type = (Class)this.assetTypes.get(fileName);
/* 201 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName);
/*     */     
/* 203 */     RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName);
/*     */ 
/*     */     
/* 206 */     assetRef.decRefCount();
/* 207 */     if (assetRef.getRefCount() <= 0) {
/* 208 */       this.log.debug("Unload (dispose): " + fileName);
/*     */ 
/*     */       
/* 211 */       if (assetRef.getObject((Class)Object.class) instanceof Disposable) ((Disposable)assetRef.<Object>getObject(Object.class)).dispose();
/*     */ 
/*     */       
/* 214 */       this.assetTypes.remove(fileName);
/* 215 */       ((ObjectMap)this.assets.get(type)).remove(fileName);
/*     */     } else {
/* 217 */       this.log.debug("Unload (decrement): " + fileName);
/*     */     } 
/*     */ 
/*     */     
/* 221 */     Array<String> dependencies = (Array<String>)this.assetDependencies.get(fileName);
/* 222 */     if (dependencies != null) {
/* 223 */       for (String dependency : dependencies) {
/* 224 */         if (isLoaded(dependency)) unload(dependency);
/*     */       
/*     */       } 
/*     */     }
/* 228 */     if (assetRef.getRefCount() <= 0) {
/* 229 */       this.assetDependencies.remove(fileName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> boolean containsAsset(T asset) {
/* 236 */     ObjectMap<String, RefCountedContainer> typedAssets = (ObjectMap<String, RefCountedContainer>)this.assets.get(asset.getClass());
/* 237 */     if (typedAssets == null) return false; 
/* 238 */     for (ObjectMap.Keys<String> keys = typedAssets.keys().iterator(); keys.hasNext(); ) { String fileName = keys.next();
/* 239 */       T otherAsset = ((RefCountedContainer)typedAssets.get(fileName)).getObject((Class)Object.class);
/* 240 */       if (otherAsset == asset || asset.equals(otherAsset)) return true;  }
/*     */     
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> String getAssetFileName(T asset) {
/* 248 */     for (ObjectMap.Keys<Class<?>> keys = this.assets.keys().iterator(); keys.hasNext(); ) { Class assetType = keys.next();
/* 249 */       ObjectMap<String, RefCountedContainer> typedAssets = (ObjectMap<String, RefCountedContainer>)this.assets.get(assetType);
/* 250 */       for (ObjectMap.Keys<String> keys1 = typedAssets.keys().iterator(); keys1.hasNext(); ) { String fileName = keys1.next();
/* 251 */         T otherAsset = ((RefCountedContainer)typedAssets.get(fileName)).getObject((Class)Object.class);
/* 252 */         if (otherAsset == asset || asset.equals(otherAsset)) return fileName;  }
/*     */        }
/*     */     
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isLoaded(String fileName) {
/* 261 */     if (fileName == null) return false; 
/* 262 */     return this.assetTypes.containsKey(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isLoaded(String fileName, Class<?> type) {
/* 268 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 269 */     if (assetsByType == null) return false; 
/* 270 */     RefCountedContainer assetContainer = (RefCountedContainer)assetsByType.get(fileName);
/* 271 */     if (assetContainer == null) return false; 
/* 272 */     return (assetContainer.getObject(type) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> AssetLoader getLoader(Class<T> type) {
/* 279 */     return getLoader(type, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> AssetLoader getLoader(Class<T> type, String fileName) {
/* 288 */     ObjectMap<String, AssetLoader> loaders = (ObjectMap<String, AssetLoader>)this.loaders.get(type);
/* 289 */     if (loaders == null || loaders.size < 1) return null; 
/* 290 */     if (fileName == null) return (AssetLoader)loaders.get(""); 
/* 291 */     AssetLoader result = null;
/* 292 */     int l = -1;
/* 293 */     for (ObjectMap.Entries<ObjectMap.Entry<String, AssetLoader>> entries = loaders.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, AssetLoader> entry = entries.next();
/* 294 */       if (((String)entry.key).length() > l && fileName.endsWith((String)entry.key)) {
/* 295 */         result = (AssetLoader)entry.value;
/* 296 */         l = ((String)entry.key).length();
/*     */       }  }
/*     */     
/* 299 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> void load(String fileName, Class<T> type) {
/* 306 */     load(fileName, type, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
/* 314 */     AssetLoader loader = getLoader(type, fileName);
/* 315 */     if (loader == null) throw new GdxRuntimeException("No loader for type: " + ClassReflection.getSimpleName(type));
/*     */ 
/*     */     
/* 318 */     if (this.loadQueue.size == 0) {
/* 319 */       this.loaded = 0;
/* 320 */       this.toLoad = 0;
/*     */     } 
/*     */ 
/*     */     
/*     */     int i;
/*     */     
/* 326 */     for (i = 0; i < this.loadQueue.size; i++) {
/* 327 */       AssetDescriptor desc = (AssetDescriptor)this.loadQueue.get(i);
/* 328 */       if (desc.fileName.equals(fileName) && !desc.type.equals(type)) {
/* 329 */         throw new GdxRuntimeException("Asset with name '" + fileName + "' already in preload queue, but has different type (expected: " + 
/* 330 */             ClassReflection.getSimpleName(type) + ", found: " + 
/* 331 */             ClassReflection.getSimpleName(desc.type) + ")");
/*     */       }
/*     */     } 
/*     */     
/* 335 */     for (i = 0; i < this.tasks.size(); i++) {
/* 336 */       AssetDescriptor desc = ((AssetLoadingTask)this.tasks.get(i)).assetDesc;
/* 337 */       if (desc.fileName.equals(fileName) && !desc.type.equals(type)) {
/* 338 */         throw new GdxRuntimeException("Asset with name '" + fileName + "' already in task list, but has different type (expected: " + 
/* 339 */             ClassReflection.getSimpleName(type) + ", found: " + 
/* 340 */             ClassReflection.getSimpleName(desc.type) + ")");
/*     */       }
/*     */     } 
/*     */     
/* 344 */     Class otherType = (Class)this.assetTypes.get(fileName);
/* 345 */     if (otherType != null && !otherType.equals(type)) {
/* 346 */       throw new GdxRuntimeException("Asset with name '" + fileName + "' already loaded, but has different type (expected: " + 
/* 347 */           ClassReflection.getSimpleName(type) + ", found: " + ClassReflection.getSimpleName(otherType) + ")");
/*     */     }
/* 349 */     this.toLoad++;
/* 350 */     AssetDescriptor<T> assetDesc = new AssetDescriptor<T>(fileName, type, parameter);
/* 351 */     this.loadQueue.add(assetDesc);
/* 352 */     this.log.debug("Queued: " + assetDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void load(AssetDescriptor desc) {
/* 358 */     load(desc.fileName, desc.type, desc.params);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean update() {
/*     */     try {
/* 365 */       if (this.tasks.size() == 0) {
/*     */         
/* 367 */         while (this.loadQueue.size != 0 && this.tasks.size() == 0) {
/* 368 */           nextTask();
/*     */         }
/*     */         
/* 371 */         if (this.tasks.size() == 0) return true; 
/*     */       } 
/* 373 */       return (updateTask() && this.loadQueue.size == 0 && this.tasks.size() == 0);
/* 374 */     } catch (Throwable t) {
/* 375 */       handleTaskError(t);
/* 376 */       return (this.loadQueue.size == 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean update(int millis) {
/* 385 */     long endTime = TimeUtils.millis() + millis;
/*     */     while (true) {
/* 387 */       boolean done = update();
/* 388 */       if (done || TimeUtils.millis() > endTime) return done; 
/* 389 */       ThreadUtils.yield();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void finishLoading() {
/* 395 */     this.log.debug("Waiting for loading to complete...");
/* 396 */     while (!update())
/* 397 */       ThreadUtils.yield(); 
/* 398 */     this.log.debug("Loading complete.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finishLoadingAsset(String fileName) {
/* 404 */     this.log.debug("Waiting for asset to be loaded: " + fileName);
/* 405 */     while (!isLoaded(fileName)) {
/* 406 */       update();
/* 407 */       ThreadUtils.yield();
/*     */     } 
/* 409 */     this.log.debug("Asset loaded: " + fileName);
/*     */   }
/*     */   
/*     */   synchronized void injectDependencies(String parentAssetFilename, Array<AssetDescriptor> dependendAssetDescs) {
/* 413 */     ObjectSet<String> injected = this.injected;
/* 414 */     for (AssetDescriptor desc : dependendAssetDescs) {
/* 415 */       if (injected.contains(desc.fileName))
/* 416 */         continue;  injected.add(desc.fileName);
/* 417 */       injectDependency(parentAssetFilename, desc);
/*     */     } 
/* 419 */     injected.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void injectDependency(String parentAssetFilename, AssetDescriptor dependendAssetDesc) {
/* 424 */     Array<String> dependencies = (Array<String>)this.assetDependencies.get(parentAssetFilename);
/* 425 */     if (dependencies == null) {
/* 426 */       dependencies = new Array();
/* 427 */       this.assetDependencies.put(parentAssetFilename, dependencies);
/*     */     } 
/* 429 */     dependencies.add(dependendAssetDesc.fileName);
/*     */ 
/*     */     
/* 432 */     if (isLoaded(dependendAssetDesc.fileName)) {
/* 433 */       this.log.debug("Dependency already loaded: " + dependendAssetDesc);
/* 434 */       Class type = (Class)this.assetTypes.get(dependendAssetDesc.fileName);
/* 435 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(dependendAssetDesc.fileName);
/* 436 */       assetRef.incRefCount();
/* 437 */       incrementRefCountedDependencies(dependendAssetDesc.fileName);
/*     */     }
/*     */     else {
/*     */       
/* 441 */       this.log.info("Loading dependency: " + dependendAssetDesc);
/* 442 */       addTask(dependendAssetDesc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextTask() {
/* 449 */     AssetDescriptor assetDesc = (AssetDescriptor)this.loadQueue.removeIndex(0);
/*     */ 
/*     */     
/* 452 */     if (isLoaded(assetDesc.fileName)) {
/* 453 */       this.log.debug("Already loaded: " + assetDesc);
/* 454 */       Class type = (Class)this.assetTypes.get(assetDesc.fileName);
/* 455 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(assetDesc.fileName);
/* 456 */       assetRef.incRefCount();
/* 457 */       incrementRefCountedDependencies(assetDesc.fileName);
/* 458 */       if (assetDesc.params != null && assetDesc.params.loadedCallback != null) {
/* 459 */         assetDesc.params.loadedCallback.finishedLoading(this, assetDesc.fileName, assetDesc.type);
/*     */       }
/* 461 */       this.loaded++;
/*     */     } else {
/*     */       
/* 464 */       this.log.info("Loading: " + assetDesc);
/* 465 */       addTask(assetDesc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addTask(AssetDescriptor assetDesc) {
/* 472 */     AssetLoader loader = getLoader(assetDesc.type, assetDesc.fileName);
/* 473 */     if (loader == null) throw new GdxRuntimeException("No loader for type: " + ClassReflection.getSimpleName(assetDesc.type)); 
/* 474 */     this.tasks.push(new AssetLoadingTask(this, assetDesc, loader, this.executor));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> void addAsset(String fileName, Class<T> type, T asset) {
/* 480 */     this.assetTypes.put(fileName, type);
/*     */ 
/*     */     
/* 483 */     ObjectMap<String, RefCountedContainer> typeToAssets = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 484 */     if (typeToAssets == null) {
/* 485 */       typeToAssets = new ObjectMap();
/* 486 */       this.assets.put(type, typeToAssets);
/*     */     } 
/* 488 */     typeToAssets.put(fileName, new RefCountedContainer(asset));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean updateTask() {
/* 494 */     AssetLoadingTask task = this.tasks.peek();
/*     */     
/* 496 */     boolean complete = true;
/*     */     try {
/* 498 */       complete = (task.cancel || task.update());
/* 499 */     } catch (RuntimeException ex) {
/* 500 */       task.cancel = true;
/* 501 */       taskFailed(task.assetDesc, ex);
/*     */     } 
/*     */ 
/*     */     
/* 505 */     if (complete) {
/*     */       
/* 507 */       if (this.tasks.size() == 1) this.loaded++; 
/* 508 */       this.tasks.pop();
/*     */       
/* 510 */       if (task.cancel) return true;
/*     */       
/* 512 */       addAsset(task.assetDesc.fileName, task.assetDesc.type, task.getAsset());
/*     */ 
/*     */       
/* 515 */       if (task.assetDesc.params != null && task.assetDesc.params.loadedCallback != null) {
/* 516 */         task.assetDesc.params.loadedCallback.finishedLoading(this, task.assetDesc.fileName, task.assetDesc.type);
/*     */       }
/*     */       
/* 519 */       long endTime = TimeUtils.nanoTime();
/* 520 */       this.log.debug("Loaded: " + ((float)(endTime - task.startTime) / 1000000.0F) + "ms " + task.assetDesc);
/*     */       
/* 522 */       return true;
/*     */     } 
/* 524 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void taskFailed(AssetDescriptor assetDesc, RuntimeException ex) {
/* 530 */     throw ex;
/*     */   }
/*     */   
/*     */   private void incrementRefCountedDependencies(String parent) {
/* 534 */     Array<String> dependencies = (Array<String>)this.assetDependencies.get(parent);
/* 535 */     if (dependencies == null)
/*     */       return; 
/* 537 */     for (String dependency : dependencies) {
/* 538 */       Class type = (Class)this.assetTypes.get(dependency);
/* 539 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(dependency);
/* 540 */       assetRef.incRefCount();
/* 541 */       incrementRefCountedDependencies(dependency);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleTaskError(Throwable t) {
/* 548 */     this.log.error("Error loading asset.", t);
/*     */     
/* 550 */     if (this.tasks.isEmpty()) throw new GdxRuntimeException(t);
/*     */ 
/*     */     
/* 553 */     AssetLoadingTask task = this.tasks.pop();
/* 554 */     AssetDescriptor assetDesc = task.assetDesc;
/*     */ 
/*     */     
/* 557 */     if (task.dependenciesLoaded && task.dependencies != null) {
/* 558 */       for (AssetDescriptor desc : task.dependencies) {
/* 559 */         unload(desc.fileName);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 564 */     this.tasks.clear();
/*     */ 
/*     */     
/* 567 */     if (this.listener != null) {
/* 568 */       this.listener.error(assetDesc, t);
/*     */     } else {
/* 570 */       throw new GdxRuntimeException(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, AssetLoader<T, P> loader) {
/* 578 */     setLoader(type, null, loader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, String suffix, AssetLoader<T, P> loader) {
/* 587 */     if (type == null) throw new IllegalArgumentException("type cannot be null."); 
/* 588 */     if (loader == null) throw new IllegalArgumentException("loader cannot be null."); 
/* 589 */     this.log.debug("Loader set: " + ClassReflection.getSimpleName(type) + " -> " + ClassReflection.getSimpleName(loader.getClass()));
/* 590 */     ObjectMap<String, AssetLoader> loaders = (ObjectMap<String, AssetLoader>)this.loaders.get(type);
/* 591 */     if (loaders == null) this.loaders.put(type, loaders = new ObjectMap()); 
/* 592 */     loaders.put((suffix == null) ? "" : suffix, loader);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getLoadedAssets() {
/* 597 */     return this.assetTypes.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getQueuedAssets() {
/* 602 */     return this.loadQueue.size + this.tasks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized float getProgress() {
/* 607 */     if (this.toLoad == 0) return 1.0F; 
/* 608 */     return Math.min(1.0F, this.loaded / this.toLoad);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setErrorListener(AssetErrorListener listener) {
/* 614 */     this.listener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void dispose() {
/* 620 */     this.log.debug("Disposing.");
/* 621 */     clear();
/* 622 */     this.executor.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 627 */     this.loadQueue.clear();
/* 628 */     while (!update());
/*     */ 
/*     */     
/* 631 */     ObjectIntMap<String> dependencyCount = new ObjectIntMap();
/* 632 */     while (this.assetTypes.size > 0) {
/*     */       
/* 634 */       dependencyCount.clear();
/* 635 */       Array<String> assets = this.assetTypes.keys().toArray();
/* 636 */       for (String asset : assets) {
/* 637 */         dependencyCount.put(asset, 0);
/*     */       }
/*     */       
/* 640 */       for (String asset : assets) {
/* 641 */         Array<String> dependencies = (Array<String>)this.assetDependencies.get(asset);
/* 642 */         if (dependencies == null)
/* 643 */           continue;  for (String dependency : dependencies) {
/* 644 */           int count = dependencyCount.get(dependency, 0);
/* 645 */           count++;
/* 646 */           dependencyCount.put(dependency, count);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 651 */       for (String asset : assets) {
/* 652 */         if (dependencyCount.get(asset, 0) == 0) {
/* 653 */           unload(asset);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 658 */     this.assets.clear();
/* 659 */     this.assetTypes.clear();
/* 660 */     this.assetDependencies.clear();
/* 661 */     this.loaded = 0;
/* 662 */     this.toLoad = 0;
/* 663 */     this.loadQueue.clear();
/* 664 */     this.tasks.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Logger getLogger() {
/* 669 */     return this.log;
/*     */   }
/*     */   
/*     */   public void setLogger(Logger logger) {
/* 673 */     this.log = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getReferenceCount(String fileName) {
/* 679 */     Class type = (Class)this.assetTypes.get(fileName);
/* 680 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 681 */     return ((RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName)).getRefCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setReferenceCount(String fileName, int refCount) {
/* 687 */     Class type = (Class)this.assetTypes.get(fileName);
/* 688 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 689 */     ((RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName)).setRefCount(refCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getDiagnostics() {
/* 694 */     StringBuffer buffer = new StringBuffer();
/* 695 */     for (ObjectMap.Keys<String> keys = this.assetTypes.keys().iterator(); keys.hasNext(); ) { String fileName = keys.next();
/* 696 */       buffer.append(fileName);
/* 697 */       buffer.append(", ");
/*     */       
/* 699 */       Class type = (Class)this.assetTypes.get(fileName);
/* 700 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName);
/* 701 */       Array<String> dependencies = (Array<String>)this.assetDependencies.get(fileName);
/*     */       
/* 703 */       buffer.append(ClassReflection.getSimpleName(type));
/*     */       
/* 705 */       buffer.append(", refs: ");
/* 706 */       buffer.append(assetRef.getRefCount());
/*     */       
/* 708 */       if (dependencies != null) {
/* 709 */         buffer.append(", deps: [");
/* 710 */         for (String dep : dependencies) {
/* 711 */           buffer.append(dep);
/* 712 */           buffer.append(",");
/*     */         } 
/* 714 */         buffer.append("]");
/*     */       } 
/* 716 */       buffer.append("\n"); }
/*     */     
/* 718 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Array<String> getAssetNames() {
/* 723 */     return this.assetTypes.keys().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Array<String> getDependencies(String fileName) {
/* 728 */     return (Array<String>)this.assetDependencies.get(fileName);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Class getAssetType(String fileName) {
/* 733 */     return (Class)this.assetTypes.get(fileName);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\assets\AssetManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */