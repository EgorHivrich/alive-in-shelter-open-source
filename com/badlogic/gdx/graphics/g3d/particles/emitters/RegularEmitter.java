/*     */ package com.badlogic.gdx.graphics.g3d.particles.emitters;public class RegularEmitter extends Emitter implements Json.Serializable { public RangedNumericValue delayValue; public RangedNumericValue durationValue; public ScaledNumericValue lifeOffsetValue; public ScaledNumericValue lifeValue;
/*     */   public ScaledNumericValue emissionValue;
/*     */   protected int emission;
/*     */   protected int emissionDiff;
/*     */   protected int emissionDelta;
/*     */   protected int lifeOffset;
/*     */   protected int lifeOffsetDiff;
/*     */   protected int life;
/*     */   protected int lifeDiff;
/*     */   protected float duration;
/*     */   protected float delay;
/*     */   protected float durationTimer;
/*     */   protected float delayTimer;
/*     */   private boolean continuous;
/*     */   private EmissionMode emissionMode;
/*     */   private ParallelArray.FloatChannel lifeChannel;
/*     */   
/*  18 */   public enum EmissionMode { Enabled,
/*     */ 
/*     */ 
/*     */     
/*  22 */     EnabledUntilCycleEnd,
/*     */     
/*  24 */     Disabled; }
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
/*     */   public RegularEmitter() {
/*  41 */     this.delayValue = new RangedNumericValue();
/*  42 */     this.durationValue = new RangedNumericValue();
/*  43 */     this.lifeOffsetValue = new ScaledNumericValue();
/*  44 */     this.lifeValue = new ScaledNumericValue();
/*  45 */     this.emissionValue = new ScaledNumericValue();
/*     */     
/*  47 */     this.durationValue.setActive(true);
/*  48 */     this.emissionValue.setActive(true);
/*  49 */     this.lifeValue.setActive(true);
/*  50 */     this.continuous = true;
/*  51 */     this.emissionMode = EmissionMode.Enabled;
/*     */   }
/*     */   
/*     */   public RegularEmitter(RegularEmitter regularEmitter) {
/*  55 */     this();
/*  56 */     set(regularEmitter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/*  61 */     this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  66 */     this.delay = this.delayValue.active ? this.delayValue.newLowValue() : 0.0F;
/*  67 */     this.delayTimer = 0.0F;
/*  68 */     this.durationTimer = 0.0F;
/*     */     
/*  70 */     this.duration = this.durationValue.newLowValue();
/*  71 */     this.percent = this.durationTimer / this.duration;
/*     */     
/*  73 */     this.emission = (int)this.emissionValue.newLowValue();
/*  74 */     this.emissionDiff = (int)this.emissionValue.newHighValue();
/*  75 */     if (!this.emissionValue.isRelative()) {
/*  76 */       this.emissionDiff -= this.emission;
/*     */     }
/*  78 */     this.life = (int)this.lifeValue.newLowValue();
/*  79 */     this.lifeDiff = (int)this.lifeValue.newHighValue();
/*  80 */     if (!this.lifeValue.isRelative()) {
/*  81 */       this.lifeDiff -= this.life;
/*     */     }
/*  83 */     this.lifeOffset = this.lifeOffsetValue.active ? (int)this.lifeOffsetValue.newLowValue() : 0;
/*  84 */     this.lifeOffsetDiff = (int)this.lifeOffsetValue.newHighValue();
/*  85 */     if (!this.lifeOffsetValue.isRelative())
/*  86 */       this.lifeOffsetDiff -= this.lifeOffset; 
/*     */   }
/*     */   
/*     */   public void init() {
/*  90 */     super.init();
/*  91 */     this.emissionDelta = 0;
/*  92 */     this.durationTimer = this.duration;
/*     */   }
/*     */   
/*     */   public void activateParticles(int startIndex, int count) {
/*  96 */     int currentTotaLife = this.life + (int)(this.lifeDiff * this.lifeValue.getScale(this.percent));
/*  97 */     int currentLife = currentTotaLife;
/*  98 */     int offsetTime = (int)(this.lifeOffset + this.lifeOffsetDiff * this.lifeOffsetValue.getScale(this.percent));
/*  99 */     if (offsetTime > 0) {
/* 100 */       if (offsetTime >= currentLife)
/* 101 */         offsetTime = currentLife - 1; 
/* 102 */       currentLife -= offsetTime;
/*     */     } 
/* 104 */     float lifePercent = 1.0F - currentLife / currentTotaLife;
/*     */     int i, c;
/* 106 */     for (i = startIndex * this.lifeChannel.strideSize, c = i + count * this.lifeChannel.strideSize; i < c; i += this.lifeChannel.strideSize) {
/* 107 */       this.lifeChannel.data[i + 0] = currentLife;
/* 108 */       this.lifeChannel.data[i + 1] = currentTotaLife;
/* 109 */       this.lifeChannel.data[i + 2] = lifePercent;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/* 114 */     int deltaMillis = (int)(this.controller.deltaTime * 1000.0F);
/*     */     
/* 116 */     if (this.delayTimer < this.delay) {
/* 117 */       this.delayTimer += deltaMillis;
/*     */     } else {
/* 119 */       boolean emit = (this.emissionMode != EmissionMode.Disabled);
/*     */       
/* 121 */       if (this.durationTimer < this.duration) {
/* 122 */         this.durationTimer += deltaMillis;
/* 123 */         this.percent = this.durationTimer / this.duration;
/*     */       
/*     */       }
/* 126 */       else if (this.continuous && emit && this.emissionMode == EmissionMode.Enabled) {
/* 127 */         this.controller.start();
/*     */       } else {
/* 129 */         emit = false;
/*     */       } 
/*     */       
/* 132 */       if (emit) {
/*     */         
/* 134 */         this.emissionDelta += deltaMillis;
/* 135 */         float emissionTime = this.emission + this.emissionDiff * this.emissionValue.getScale(this.percent);
/* 136 */         if (emissionTime > 0.0F) {
/* 137 */           emissionTime = 1000.0F / emissionTime;
/* 138 */           if (this.emissionDelta >= emissionTime) {
/* 139 */             int emitCount = (int)(this.emissionDelta / emissionTime);
/* 140 */             emitCount = Math.min(emitCount, this.maxParticleCount - this.controller.particles.size);
/* 141 */             this.emissionDelta = (int)(this.emissionDelta - emitCount * emissionTime);
/* 142 */             this.emissionDelta = (int)(this.emissionDelta % emissionTime);
/* 143 */             addParticles(emitCount);
/*     */           } 
/*     */         } 
/* 146 */         if (this.controller.particles.size < this.minParticleCount) {
/* 147 */           addParticles(this.minParticleCount - this.controller.particles.size);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     int activeParticles = this.controller.particles.size; int k;
/* 153 */     for (int i = 0; i < this.controller.particles.size; ) {
/* 154 */       this.lifeChannel.data[k + 0] = this.lifeChannel.data[k + 0] - deltaMillis; if (this.lifeChannel.data[k + 0] - deltaMillis <= 0.0F) {
/* 155 */         this.controller.particles.removeElement(i);
/*     */         
/*     */         continue;
/*     */       } 
/* 159 */       this.lifeChannel.data[k + 2] = 1.0F - this.lifeChannel.data[k + 0] / this.lifeChannel.data[k + 1];
/*     */       
/* 161 */       i++;
/* 162 */       k += this.lifeChannel.strideSize;
/*     */     } 
/*     */     
/* 165 */     if (this.controller.particles.size < activeParticles) {
/* 166 */       this.controller.killParticles(this.controller.particles.size, activeParticles - this.controller.particles.size);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addParticles(int count) {
/* 171 */     count = Math.min(count, this.maxParticleCount - this.controller.particles.size);
/* 172 */     if (count <= 0)
/* 173 */       return;  this.controller.activateParticles(this.controller.particles.size, count);
/* 174 */     this.controller.particles.size += count;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getLife() {
/* 178 */     return this.lifeValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getEmission() {
/* 182 */     return this.emissionValue;
/*     */   }
/*     */   
/*     */   public RangedNumericValue getDuration() {
/* 186 */     return this.durationValue;
/*     */   }
/*     */   
/*     */   public RangedNumericValue getDelay() {
/* 190 */     return this.delayValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getLifeOffset() {
/* 194 */     return this.lifeOffsetValue;
/*     */   }
/*     */   
/*     */   public boolean isContinuous() {
/* 198 */     return this.continuous;
/*     */   }
/*     */   
/*     */   public void setContinuous(boolean continuous) {
/* 202 */     this.continuous = continuous;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EmissionMode getEmissionMode() {
/* 208 */     return this.emissionMode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmissionMode(EmissionMode emissionMode) {
/* 214 */     this.emissionMode = emissionMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 219 */     if (this.delayTimer < this.delay) return false; 
/* 220 */     return (this.durationTimer >= this.duration && this.controller.particles.size == 0);
/*     */   }
/*     */   
/*     */   public float getPercentComplete() {
/* 224 */     if (this.delayTimer < this.delay) return 0.0F; 
/* 225 */     return Math.min(1.0F, this.durationTimer / this.duration);
/*     */   }
/*     */   
/*     */   public void set(RegularEmitter emitter) {
/* 229 */     set(emitter);
/* 230 */     this.delayValue.load(emitter.delayValue);
/* 231 */     this.durationValue.load(emitter.durationValue);
/* 232 */     this.lifeOffsetValue.load(emitter.lifeOffsetValue);
/* 233 */     this.lifeValue.load(emitter.lifeValue);
/* 234 */     this.emissionValue.load(emitter.emissionValue);
/* 235 */     this.emission = emitter.emission;
/* 236 */     this.emissionDiff = emitter.emissionDiff;
/* 237 */     this.emissionDelta = emitter.emissionDelta;
/* 238 */     this.lifeOffset = emitter.lifeOffset;
/* 239 */     this.lifeOffsetDiff = emitter.lifeOffsetDiff;
/* 240 */     this.life = emitter.life;
/* 241 */     this.lifeDiff = emitter.lifeDiff;
/* 242 */     this.duration = emitter.duration;
/* 243 */     this.delay = emitter.delay;
/* 244 */     this.durationTimer = emitter.durationTimer;
/* 245 */     this.delayTimer = emitter.delayTimer;
/* 246 */     this.continuous = emitter.continuous;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleControllerComponent copy() {
/* 251 */     return new RegularEmitter(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 256 */     super.write(json);
/* 257 */     json.writeValue("continous", Boolean.valueOf(this.continuous));
/* 258 */     json.writeValue("emission", this.emissionValue);
/* 259 */     json.writeValue("delay", this.delayValue);
/* 260 */     json.writeValue("duration", this.durationValue);
/* 261 */     json.writeValue("life", this.lifeValue);
/* 262 */     json.writeValue("lifeOffset", this.lifeOffsetValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 267 */     super.read(json, jsonData);
/* 268 */     this.continuous = ((Boolean)json.readValue("continous", boolean.class, jsonData)).booleanValue();
/* 269 */     this.emissionValue = (ScaledNumericValue)json.readValue("emission", ScaledNumericValue.class, jsonData);
/* 270 */     this.delayValue = (RangedNumericValue)json.readValue("delay", RangedNumericValue.class, jsonData);
/* 271 */     this.durationValue = (RangedNumericValue)json.readValue("duration", RangedNumericValue.class, jsonData);
/* 272 */     this.lifeValue = (ScaledNumericValue)json.readValue("life", ScaledNumericValue.class, jsonData);
/* 273 */     this.lifeOffsetValue = (ScaledNumericValue)json.readValue("lifeOffset", ScaledNumericValue.class, jsonData);
/*     */   } }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\emitters\RegularEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */