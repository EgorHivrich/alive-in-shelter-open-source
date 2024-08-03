/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
/*     */ public abstract class PrimitiveSpawnShapeValue extends SpawnShapeValue {
/*     */   public ScaledNumericValue spawnWidthValue;
/*     */   public ScaledNumericValue spawnHeightValue;
/*     */   public ScaledNumericValue spawnDepthValue;
/*     */   protected float spawnWidth;
/*     */   protected float spawnWidthDiff;
/*     */   protected float spawnHeight;
/*     */   protected float spawnHeightDiff;
/*  11 */   protected static final Vector3 TMP_V1 = new Vector3(); protected float spawnDepth; protected float spawnDepthDiff;
/*     */   
/*  13 */   public enum SpawnSide { both, top, bottom; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean edges = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrimitiveSpawnShapeValue() {
/*  24 */     this.spawnWidthValue = new ScaledNumericValue();
/*  25 */     this.spawnHeightValue = new ScaledNumericValue();
/*  26 */     this.spawnDepthValue = new ScaledNumericValue();
/*     */   }
/*     */   
/*     */   public PrimitiveSpawnShapeValue(PrimitiveSpawnShapeValue value) {
/*  30 */     super(value);
/*  31 */     this.spawnWidthValue = new ScaledNumericValue();
/*  32 */     this.spawnHeightValue = new ScaledNumericValue();
/*  33 */     this.spawnDepthValue = new ScaledNumericValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActive(boolean active) {
/*  38 */     super.setActive(active);
/*  39 */     this.spawnWidthValue.setActive(true);
/*  40 */     this.spawnHeightValue.setActive(true);
/*  41 */     this.spawnDepthValue.setActive(true);
/*     */   }
/*     */   
/*     */   public boolean isEdges() {
/*  45 */     return this.edges;
/*     */   }
/*     */   
/*     */   public void setEdges(boolean edges) {
/*  49 */     this.edges = edges;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getSpawnWidth() {
/*  53 */     return this.spawnWidthValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getSpawnHeight() {
/*  57 */     return this.spawnHeightValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getSpawnDepth() {
/*  61 */     return this.spawnDepthValue;
/*     */   }
/*     */   
/*     */   public void setDimensions(float width, float height, float depth) {
/*  65 */     this.spawnWidthValue.setHigh(width);
/*  66 */     this.spawnHeightValue.setHigh(height);
/*  67 */     this.spawnDepthValue.setHigh(depth);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  72 */     this.spawnWidth = this.spawnWidthValue.newLowValue();
/*  73 */     this.spawnWidthDiff = this.spawnWidthValue.newHighValue();
/*  74 */     if (!this.spawnWidthValue.isRelative()) this.spawnWidthDiff -= this.spawnWidth;
/*     */     
/*  76 */     this.spawnHeight = this.spawnHeightValue.newLowValue();
/*  77 */     this.spawnHeightDiff = this.spawnHeightValue.newHighValue();
/*  78 */     if (!this.spawnHeightValue.isRelative()) this.spawnHeightDiff -= this.spawnHeight;
/*     */     
/*  80 */     this.spawnDepth = this.spawnDepthValue.newLowValue();
/*  81 */     this.spawnDepthDiff = this.spawnDepthValue.newHighValue();
/*  82 */     if (!this.spawnDepthValue.isRelative()) this.spawnDepthDiff -= this.spawnDepth;
/*     */   
/*     */   }
/*     */   
/*     */   public void load(ParticleValue value) {
/*  87 */     super.load(value);
/*  88 */     PrimitiveSpawnShapeValue shape = (PrimitiveSpawnShapeValue)value;
/*  89 */     this.edges = shape.edges;
/*  90 */     this.spawnWidthValue.load(shape.spawnWidthValue);
/*  91 */     this.spawnHeightValue.load(shape.spawnHeightValue);
/*  92 */     this.spawnDepthValue.load(shape.spawnDepthValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/*  97 */     super.write(json);
/*  98 */     json.writeValue("spawnWidthValue", this.spawnWidthValue);
/*  99 */     json.writeValue("spawnHeightValue", this.spawnHeightValue);
/* 100 */     json.writeValue("spawnDepthValue", this.spawnDepthValue);
/* 101 */     json.writeValue("edges", Boolean.valueOf(this.edges));
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 106 */     super.read(json, jsonData);
/* 107 */     this.spawnWidthValue = (ScaledNumericValue)json.readValue("spawnWidthValue", ScaledNumericValue.class, jsonData);
/* 108 */     this.spawnHeightValue = (ScaledNumericValue)json.readValue("spawnHeightValue", ScaledNumericValue.class, jsonData);
/* 109 */     this.spawnDepthValue = (ScaledNumericValue)json.readValue("spawnDepthValue", ScaledNumericValue.class, jsonData);
/* 110 */     this.edges = ((Boolean)json.readValue("edges", boolean.class, jsonData)).booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\PrimitiveSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */