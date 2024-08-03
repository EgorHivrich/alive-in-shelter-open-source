/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleController
/*     */   implements Json.Serializable, ResourceData.Configurable
/*     */ {
/*     */   protected static final float DEFAULT_TIME_STEP = 0.016666668F;
/*     */   public String name;
/*     */   public Emitter emitter;
/*     */   public Array<Influencer> influencers;
/*     */   public ParticleControllerRenderer<?, ?> renderer;
/*     */   public ParallelArray particles;
/*     */   public ParticleChannels particleChannels;
/*     */   public Matrix4 transform;
/*     */   public Vector3 scale;
/*     */   protected BoundingBox boundingBox;
/*     */   public float deltaTime;
/*     */   public float deltaTimeSqr;
/*     */   
/*     */   public ParticleController() {
/*  55 */     this.transform = new Matrix4();
/*  56 */     this.scale = new Vector3(1.0F, 1.0F, 1.0F);
/*  57 */     this.influencers = new Array(true, 3, Influencer.class);
/*  58 */     setTimeStep(0.016666668F);
/*     */   }
/*     */   
/*     */   public ParticleController(String name, Emitter emitter, ParticleControllerRenderer<?, ?> renderer, Influencer... influencers) {
/*  62 */     this();
/*  63 */     this.name = name;
/*  64 */     this.emitter = emitter;
/*  65 */     this.renderer = renderer;
/*  66 */     this.particleChannels = new ParticleChannels();
/*  67 */     this.influencers = new Array((Object[])influencers);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setTimeStep(float timeStep) {
/*  72 */     this.deltaTime = timeStep;
/*  73 */     this.deltaTimeSqr = this.deltaTime * this.deltaTime;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransform(Matrix4 transform) {
/*  79 */     this.transform.set(transform);
/*  80 */     transform.getScale(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransform(float x, float y, float z, float qx, float qy, float qz, float qw, float scale) {
/*  85 */     this.transform.set(x, y, z, qx, qy, qz, qw, scale, scale, scale);
/*  86 */     this.scale.set(scale, scale, scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(Quaternion rotation) {
/*  91 */     this.transform.rotate(rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Vector3 axis, float angle) {
/*  98 */     this.transform.rotate(axis, angle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(Vector3 translation) {
/* 103 */     this.transform.translate(translation);
/*     */   }
/*     */   
/*     */   public void setTranslation(Vector3 translation) {
/* 107 */     this.transform.setTranslation(translation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float scaleX, float scaleY, float scaleZ) {
/* 112 */     this.transform.scale(scaleX, scaleY, scaleZ);
/* 113 */     this.transform.getScale(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(Vector3 scale) {
/* 118 */     scale(scale.x, scale.y, scale.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mul(Matrix4 transform) {
/* 123 */     this.transform.mul(transform);
/* 124 */     this.transform.getScale(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getTransform(Matrix4 transform) {
/* 129 */     transform.set(this.transform);
/*     */   }
/*     */   
/*     */   public boolean isComplete() {
/* 133 */     return this.emitter.isComplete();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 140 */     bind();
/* 141 */     if (this.particles != null) {
/* 142 */       end();
/* 143 */       this.particleChannels.resetIds();
/*     */     } 
/* 145 */     allocateChannels(this.emitter.maxParticleCount);
/*     */     
/* 147 */     this.emitter.init();
/* 148 */     for (Influencer influencer : this.influencers)
/* 149 */       influencer.init(); 
/* 150 */     this.renderer.init();
/*     */   }
/*     */   
/*     */   protected void allocateChannels(int maxParticleCount) {
/* 154 */     this.particles = new ParallelArray(maxParticleCount);
/*     */     
/* 156 */     this.emitter.allocateChannels();
/* 157 */     for (Influencer influencer : this.influencers)
/* 158 */       influencer.allocateChannels(); 
/* 159 */     this.renderer.allocateChannels();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bind() {
/* 165 */     this.emitter.set(this);
/* 166 */     for (Influencer influencer : this.influencers)
/* 167 */       influencer.set(this); 
/* 168 */     this.renderer.set(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 173 */     this.emitter.start();
/* 174 */     for (Influencer influencer : this.influencers) {
/* 175 */       influencer.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/* 180 */     end();
/* 181 */     start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 186 */     for (Influencer influencer : this.influencers)
/* 187 */       influencer.end(); 
/* 188 */     this.emitter.end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateParticles(int startIndex, int count) {
/* 195 */     this.emitter.activateParticles(startIndex, count);
/* 196 */     for (Influencer influencer : this.influencers) {
/* 197 */       influencer.activateParticles(startIndex, count);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void killParticles(int startIndex, int count) {
/* 204 */     this.emitter.killParticles(startIndex, count);
/* 205 */     for (Influencer influencer : this.influencers) {
/* 206 */       influencer.killParticles(startIndex, count);
/*     */     }
/*     */   }
/*     */   
/*     */   public void update() {
/* 211 */     this.emitter.update();
/* 212 */     for (Influencer influencer : this.influencers) {
/* 213 */       influencer.update();
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw() {
/* 218 */     if (this.particles.size > 0) {
/* 219 */       this.renderer.update();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleController copy() {
/* 225 */     Emitter emitter = (Emitter)this.emitter.copy();
/* 226 */     Influencer[] influencers = new Influencer[this.influencers.size];
/* 227 */     int i = 0;
/* 228 */     for (Influencer influencer : this.influencers) {
/* 229 */       influencers[i++] = (Influencer)influencer.copy();
/*     */     }
/* 231 */     return new ParticleController(new String(this.name), emitter, (ParticleControllerRenderer<?, ?>)this.renderer.copy(), influencers);
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 235 */     this.emitter.dispose();
/* 236 */     for (Influencer influencer : this.influencers) {
/* 237 */       influencer.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 242 */     if (this.boundingBox == null) this.boundingBox = new BoundingBox(); 
/* 243 */     calculateBoundingBox();
/* 244 */     return this.boundingBox;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void calculateBoundingBox() {
/* 249 */     this.boundingBox.clr();
/* 250 */     ParallelArray.FloatChannel positionChannel = this.particles.<ParallelArray.FloatChannel>getChannel(ParticleChannels.Position);
/* 251 */     for (int pos = 0, c = positionChannel.strideSize * this.particles.size; pos < c; pos += positionChannel.strideSize) {
/* 252 */       this.boundingBox.ext(positionChannel.data[pos + 0], positionChannel.data[pos + 1], positionChannel.data[pos + 2]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <K extends Influencer> int findIndex(Class<K> type) {
/* 260 */     for (int i = 0; i < this.influencers.size; i++) {
/* 261 */       Influencer influencer = (Influencer)this.influencers.get(i);
/* 262 */       if (ClassReflection.isAssignableFrom(type, influencer.getClass())) {
/* 263 */         return i;
/*     */       }
/*     */     } 
/* 266 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public <K extends Influencer> K findInfluencer(Class<K> influencerClass) {
/* 271 */     int index = findIndex(influencerClass);
/* 272 */     return (index > -1) ? (K)this.influencers.get(index) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public <K extends Influencer> void removeInfluencer(Class<K> type) {
/* 277 */     int index = findIndex(type);
/* 278 */     if (index > -1) {
/* 279 */       this.influencers.removeIndex(index);
/*     */     }
/*     */   }
/*     */   
/*     */   public <K extends Influencer> boolean replaceInfluencer(Class<K> type, K newInfluencer) {
/* 284 */     int index = findIndex(type);
/* 285 */     if (index > -1) {
/* 286 */       this.influencers.insert(index, newInfluencer);
/* 287 */       this.influencers.removeIndex(index + 1);
/* 288 */       return true;
/*     */     } 
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 295 */     json.writeValue("name", this.name);
/* 296 */     json.writeValue("emitter", this.emitter, Emitter.class);
/* 297 */     json.writeValue("influencers", this.influencers, Array.class, Influencer.class);
/* 298 */     json.writeValue("renderer", this.renderer, ParticleControllerRenderer.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonMap) {
/* 303 */     this.name = (String)json.readValue("name", String.class, jsonMap);
/* 304 */     this.emitter = (Emitter)json.readValue("emitter", Emitter.class, jsonMap);
/* 305 */     this.influencers.addAll((Array)json.readValue("influencers", Array.class, Influencer.class, jsonMap));
/* 306 */     this.renderer = (ParticleControllerRenderer<?, ?>)json.readValue("renderer", ParticleControllerRenderer.class, jsonMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData data) {
/* 311 */     this.emitter.save(manager, data);
/* 312 */     for (Influencer influencer : this.influencers)
/* 313 */       influencer.save(manager, data); 
/* 314 */     this.renderer.save(manager, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData data) {
/* 319 */     this.emitter.load(manager, data);
/* 320 */     for (Influencer influencer : this.influencers)
/* 321 */       influencer.load(manager, data); 
/* 322 */     this.renderer.load(manager, data);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParticleController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */