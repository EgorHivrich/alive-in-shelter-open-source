/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import com.badlogic.gdx.utils.reflect.ReflectionException;
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
/*     */ public class ResourceData<T>
/*     */   implements Json.Serializable
/*     */ {
/*     */   public static interface Configurable<T>
/*     */   {
/*     */     void save(AssetManager param1AssetManager, ResourceData<T> param1ResourceData);
/*     */     
/*     */     void load(AssetManager param1AssetManager, ResourceData<T> param1ResourceData);
/*     */   }
/*     */   
/*     */   public static class SaveData
/*     */     implements Json.Serializable
/*     */   {
/*     */     ObjectMap<String, Object> data;
/*     */     IntArray assets;
/*     */     private int loadIndex;
/*     */     protected ResourceData resources;
/*     */     
/*     */     public SaveData() {
/*  45 */       this.data = new ObjectMap();
/*  46 */       this.assets = new IntArray();
/*  47 */       this.loadIndex = 0;
/*     */     }
/*     */     
/*     */     public SaveData(ResourceData resources) {
/*  51 */       this.data = new ObjectMap();
/*  52 */       this.assets = new IntArray();
/*  53 */       this.loadIndex = 0;
/*  54 */       this.resources = resources;
/*     */     }
/*     */     
/*     */     public <K> void saveAsset(String filename, Class<K> type) {
/*  58 */       int i = this.resources.getAssetData(filename, type);
/*  59 */       if (i == -1) {
/*  60 */         this.resources.sharedAssets.add(new ResourceData.AssetData<K>(filename, type));
/*  61 */         i = this.resources.sharedAssets.size - 1;
/*     */       } 
/*  63 */       this.assets.add(i);
/*     */     }
/*     */     
/*     */     public void save(String key, Object value) {
/*  67 */       this.data.put(key, value);
/*     */     }
/*     */     
/*     */     public AssetDescriptor loadAsset() {
/*  71 */       if (this.loadIndex == this.assets.size) return null; 
/*  72 */       ResourceData.AssetData data = (ResourceData.AssetData)this.resources.sharedAssets.get(this.assets.get(this.loadIndex++));
/*  73 */       return new AssetDescriptor(data.filename, data.type);
/*     */     }
/*     */     
/*     */     public <K> K load(String key) {
/*  77 */       return (K)this.data.get(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/*  82 */       json.writeValue("data", this.data, ObjectMap.class);
/*  83 */       json.writeValue("indices", this.assets.toArray(), int[].class);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/*  88 */       this.data = (ObjectMap<String, Object>)json.readValue("data", ObjectMap.class, jsonData);
/*  89 */       this.assets.addAll((int[])json.readValue("indices", int[].class, jsonData));
/*     */     } }
/*     */   
/*     */   public static class AssetData<T> implements Json.Serializable {
/*     */     public String filename;
/*     */     public Class<T> type;
/*     */     
/*     */     public AssetData() {}
/*     */     
/*     */     public AssetData(String filename, Class<T> type) {
/*  99 */       this.filename = filename;
/* 100 */       this.type = type;
/*     */     }
/*     */     
/*     */     public void write(Json json) {
/* 104 */       json.writeValue("filename", this.filename);
/* 105 */       json.writeValue("type", this.type.getName());
/*     */     }
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 109 */       this.filename = (String)json.readValue("filename", String.class, jsonData);
/* 110 */       String className = (String)json.readValue("type", String.class, jsonData);
/*     */       try {
/* 112 */         this.type = ClassReflection.forName(className);
/* 113 */       } catch (ReflectionException e) {
/* 114 */         throw new GdxRuntimeException("Class not found: " + className, e);
/*     */       } 
/*     */     }
/*     */   }
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
/* 132 */   private ObjectMap<String, SaveData> uniqueData = new ObjectMap();
/* 133 */   private Array<SaveData> data = new Array(true, 3, SaveData.class);
/* 134 */   Array<AssetData> sharedAssets = new Array();
/* 135 */   private int currentLoadIndex = 0;
/*     */   public T resource;
/*     */   
/*     */   public ResourceData(T resource) {
/* 139 */     this();
/* 140 */     this.resource = resource;
/*     */   }
/*     */   public ResourceData() {}
/*     */   <K> int getAssetData(String filename, Class<K> type) {
/* 144 */     int i = 0;
/* 145 */     for (AssetData data : this.sharedAssets) {
/* 146 */       if (data.filename.equals(filename) && data.type.equals(type)) {
/* 147 */         return i;
/*     */       }
/* 149 */       i++;
/*     */     } 
/* 151 */     return -1;
/*     */   }
/*     */   
/*     */   public Array<AssetDescriptor> getAssetDescriptors() {
/* 155 */     Array<AssetDescriptor> descriptors = new Array();
/* 156 */     for (AssetData data : this.sharedAssets) {
/* 157 */       descriptors.add(new AssetDescriptor(data.filename, data.type));
/*     */     }
/* 159 */     return descriptors;
/*     */   }
/*     */   
/*     */   public Array<AssetData> getAssets() {
/* 163 */     return this.sharedAssets;
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData createSaveData() {
/* 168 */     SaveData saveData = new SaveData(this);
/* 169 */     this.data.add(saveData);
/* 170 */     return saveData;
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData createSaveData(String key) {
/* 175 */     SaveData saveData = new SaveData(this);
/* 176 */     if (this.uniqueData.containsKey(key))
/* 177 */       throw new RuntimeException("Key already used, data must be unique, use a different key"); 
/* 178 */     this.uniqueData.put(key, saveData);
/* 179 */     return saveData;
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData getSaveData() {
/* 184 */     return (SaveData)this.data.get(this.currentLoadIndex++);
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData getSaveData(String key) {
/* 189 */     return (SaveData)this.uniqueData.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 194 */     json.writeValue("unique", this.uniqueData, ObjectMap.class);
/* 195 */     json.writeValue("data", this.data, Array.class, SaveData.class);
/* 196 */     json.writeValue("assets", this.sharedAssets.toArray(AssetData.class), AssetData[].class);
/* 197 */     json.writeValue("resource", this.resource, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 202 */     this.uniqueData = (ObjectMap<String, SaveData>)json.readValue("unique", ObjectMap.class, jsonData);
/* 203 */     for (ObjectMap.Entries<ObjectMap.Entry<String, SaveData>> entries = this.uniqueData.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, SaveData> entry = entries.next();
/* 204 */       ((SaveData)entry.value).resources = this; }
/*     */ 
/*     */     
/* 207 */     this.data = (Array<SaveData>)json.readValue("data", Array.class, SaveData.class, jsonData);
/* 208 */     for (SaveData saveData : this.data) {
/* 209 */       saveData.resources = this;
/*     */     }
/*     */     
/* 212 */     this.sharedAssets.addAll((Array)json.readValue("assets", Array.class, AssetData.class, jsonData));
/* 213 */     this.resource = (T)json.readValue("resource", null, jsonData);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ResourceData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */