/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DynamicsModifier
/*     */   extends Influencer
/*     */ {
/*  19 */   protected static final Vector3 TMP_V1 = new Vector3();
/*  20 */   protected static final Vector3 TMP_V2 = new Vector3();
/*  21 */   protected static final Vector3 TMP_V3 = new Vector3();
/*  22 */   protected static final Quaternion TMP_Q = new Quaternion();
/*     */   
/*     */   public static class FaceDirection
/*     */     extends DynamicsModifier
/*     */   {
/*     */     ParallelArray.FloatChannel rotationChannel;
/*     */     
/*     */     public FaceDirection(FaceDirection rotation) {
/*  30 */       super(rotation);
/*     */     } ParallelArray.FloatChannel accellerationChannel;
/*     */     public FaceDirection() {}
/*     */     public void allocateChannels() {
/*  34 */       this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation3D);
/*  35 */       this.accellerationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/*  40 */       int i = 0, accelOffset = 0, c = i + this.controller.particles.size * this.rotationChannel.strideSize;
/*  41 */       for (; i < c; 
/*  42 */         i += this.rotationChannel.strideSize, accelOffset += this.accellerationChannel.strideSize) {
/*     */ 
/*     */ 
/*     */         
/*  46 */         Vector3 axisZ = TMP_V1.set(this.accellerationChannel.data[accelOffset + 0], this.accellerationChannel.data[accelOffset + 1], this.accellerationChannel.data[accelOffset + 2]).nor();
/*  47 */         Vector3 axisY = TMP_V2.set(TMP_V1).crs(Vector3.Y).nor().crs(TMP_V1).nor();
/*  48 */         Vector3 axisX = TMP_V3.set(axisY).crs(axisZ).nor();
/*  49 */         TMP_Q.setFromAxes(false, axisX.x, axisY.x, axisZ.x, axisX.y, axisY.y, axisZ.y, axisX.z, axisY.z, axisZ.z);
/*     */ 
/*     */         
/*  52 */         this.rotationChannel.data[i + 0] = TMP_Q.x;
/*  53 */         this.rotationChannel.data[i + 1] = TMP_Q.y;
/*  54 */         this.rotationChannel.data[i + 2] = TMP_Q.z;
/*  55 */         this.rotationChannel.data[i + 3] = TMP_Q.w;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ParticleControllerComponent copy() {
/*  61 */       return new FaceDirection(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Strength extends DynamicsModifier {
/*     */     protected ParallelArray.FloatChannel strengthChannel;
/*     */     public ScaledNumericValue strengthValue;
/*     */     
/*     */     public Strength() {
/*  70 */       this.strengthValue = new ScaledNumericValue();
/*     */     }
/*     */     
/*     */     public Strength(Strength rotation) {
/*  74 */       super(rotation);
/*  75 */       this.strengthValue = new ScaledNumericValue();
/*  76 */       this.strengthValue.load(rotation.strengthValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/*  81 */       super.allocateChannels();
/*  82 */       ParticleChannels.Interpolation.id = this.controller.particleChannels.newId();
/*  83 */       this.strengthChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  89 */       int i = startIndex * this.strengthChannel.strideSize, c = i + count * this.strengthChannel.strideSize;
/*  90 */       for (; i < c; 
/*  91 */         i += this.strengthChannel.strideSize) {
/*  92 */         float start = this.strengthValue.newLowValue();
/*  93 */         float diff = this.strengthValue.newHighValue();
/*  94 */         if (!this.strengthValue.isRelative())
/*  95 */           diff -= start; 
/*  96 */         this.strengthChannel.data[i + 0] = start;
/*  97 */         this.strengthChannel.data[i + 1] = diff;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 103 */       super.write(json);
/* 104 */       json.writeValue("strengthValue", this.strengthValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 109 */       super.read(json, jsonData);
/* 110 */       this.strengthValue = (ScaledNumericValue)json.readValue("strengthValue", ScaledNumericValue.class, jsonData);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Angular
/*     */     extends Strength
/*     */   {
/*     */     protected ParallelArray.FloatChannel angularChannel;
/*     */     public ScaledNumericValue thetaValue;
/*     */     public ScaledNumericValue phiValue;
/*     */     
/*     */     public Angular() {
/* 122 */       this.thetaValue = new ScaledNumericValue();
/* 123 */       this.phiValue = new ScaledNumericValue();
/*     */     }
/*     */     
/*     */     public Angular(Angular value) {
/* 127 */       super(value);
/* 128 */       this.thetaValue = new ScaledNumericValue();
/* 129 */       this.phiValue = new ScaledNumericValue();
/* 130 */       this.thetaValue.load(value.thetaValue);
/* 131 */       this.phiValue.load(value.phiValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 136 */       super.allocateChannels();
/* 137 */       ParticleChannels.Interpolation4.id = this.controller.particleChannels.newId();
/* 138 */       this.angularChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation4);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/* 143 */       super.activateParticles(startIndex, count);
/*     */       
/* 145 */       int i = startIndex * this.angularChannel.strideSize, c = i + count * this.angularChannel.strideSize;
/* 146 */       for (; i < c; 
/* 147 */         i += this.angularChannel.strideSize) {
/*     */ 
/*     */         
/* 150 */         float start = this.thetaValue.newLowValue();
/* 151 */         float diff = this.thetaValue.newHighValue();
/* 152 */         if (!this.thetaValue.isRelative())
/* 153 */           diff -= start; 
/* 154 */         this.angularChannel.data[i + 0] = start;
/* 155 */         this.angularChannel.data[i + 1] = diff;
/*     */ 
/*     */         
/* 158 */         start = this.phiValue.newLowValue();
/* 159 */         diff = this.phiValue.newHighValue();
/* 160 */         if (!this.phiValue.isRelative())
/* 161 */           diff -= start; 
/* 162 */         this.angularChannel.data[i + 2] = start;
/* 163 */         this.angularChannel.data[i + 3] = diff;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 169 */       super.write(json);
/* 170 */       json.writeValue("thetaValue", this.thetaValue);
/* 171 */       json.writeValue("phiValue", this.phiValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 176 */       super.read(json, jsonData);
/* 177 */       this.thetaValue = (ScaledNumericValue)json.readValue("thetaValue", ScaledNumericValue.class, jsonData);
/* 178 */       this.phiValue = (ScaledNumericValue)json.readValue("phiValue", ScaledNumericValue.class, jsonData);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rotational2D
/*     */     extends Strength {
/*     */     ParallelArray.FloatChannel rotationalVelocity2dChannel;
/*     */     
/*     */     public Rotational2D() {}
/*     */     
/*     */     public Rotational2D(Rotational2D rotation) {
/* 189 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 194 */       super.allocateChannels();
/* 195 */       this.rotationalVelocity2dChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.AngularVelocity2D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 200 */       int i = 0, l = 2, s = 0;
/* 201 */       int c = i + this.controller.particles.size * this.rotationalVelocity2dChannel.strideSize;
/* 202 */       for (; i < c; 
/* 203 */         s += this.strengthChannel.strideSize, i += this.rotationalVelocity2dChannel.strideSize, l += this.lifeChannel.strideSize) {
/* 204 */         this.rotationalVelocity2dChannel.data[i] = this.rotationalVelocity2dChannel.data[i] + this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue
/* 205 */           .getScale(this.lifeChannel.data[l]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Rotational2D copy() {
/* 211 */       return new Rotational2D(this);
/*     */     } }
/*     */   
/*     */   public static class Rotational3D extends Angular {
/*     */     ParallelArray.FloatChannel rotationChannel;
/*     */     ParallelArray.FloatChannel rotationalForceChannel;
/*     */     
/*     */     public Rotational3D() {}
/*     */     
/*     */     public Rotational3D(Rotational3D rotation) {
/* 221 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 226 */       super.allocateChannels();
/* 227 */       this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation3D);
/* 228 */       this.rotationalForceChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.AngularVelocity3D);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void update() {
/* 258 */       int i = 0, l = 2, s = 0, a = 0;
/* 259 */       int c = this.controller.particles.size * this.rotationalForceChannel.strideSize;
/* 260 */       for (; i < c; 
/* 261 */         s += this.strengthChannel.strideSize, i += this.rotationalForceChannel.strideSize, 
/* 262 */         a += this.angularChannel.strideSize, l += this.lifeChannel.strideSize) {
/*     */         
/* 264 */         float lifePercent = this.lifeChannel.data[l];
/*     */         
/* 266 */         float strength = this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue.getScale(lifePercent);
/*     */         
/* 268 */         float phi = this.angularChannel.data[a + 2] + this.angularChannel.data[a + 3] * this.phiValue.getScale(lifePercent);
/*     */         
/* 270 */         float theta = this.angularChannel.data[a + 0] + this.angularChannel.data[a + 1] * this.thetaValue.getScale(lifePercent);
/*     */         
/* 272 */         float cosTheta = MathUtils.cosDeg(theta), sinTheta = MathUtils.sinDeg(theta);
/* 273 */         float cosPhi = MathUtils.cosDeg(phi), sinPhi = MathUtils.sinDeg(phi);
/*     */         
/* 275 */         TMP_V3.set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi);
/* 276 */         TMP_V3.scl(strength * 0.017453292F);
/*     */         
/* 278 */         this.rotationalForceChannel.data[i + 0] = this.rotationalForceChannel.data[i + 0] + TMP_V3.x;
/* 279 */         this.rotationalForceChannel.data[i + 1] = this.rotationalForceChannel.data[i + 1] + TMP_V3.y;
/* 280 */         this.rotationalForceChannel.data[i + 2] = this.rotationalForceChannel.data[i + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Rotational3D copy() {
/* 286 */       return new Rotational3D(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CentripetalAcceleration extends Strength {
/*     */     ParallelArray.FloatChannel accelerationChannel;
/*     */     ParallelArray.FloatChannel positionChannel;
/*     */     
/*     */     public CentripetalAcceleration() {}
/*     */     
/*     */     public CentripetalAcceleration(CentripetalAcceleration rotation) {
/* 297 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 302 */       super.allocateChannels();
/* 303 */       this.accelerationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/* 304 */       this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 309 */       float cx = 0.0F, cy = 0.0F, cz = 0.0F;
/* 310 */       if (!this.isGlobal) {
/* 311 */         float[] val = this.controller.transform.val;
/* 312 */         cx = val[12];
/* 313 */         cy = val[13];
/* 314 */         cz = val[14];
/*     */       } 
/*     */       
/* 317 */       int lifeOffset = 2, strengthOffset = 0, positionOffset = 0, forceOffset = 0;
/* 318 */       for (int i = 0, c = this.controller.particles.size; i < c; i++, 
/* 319 */         positionOffset += this.positionChannel.strideSize, 
/* 320 */         strengthOffset += this.strengthChannel.strideSize, 
/* 321 */         forceOffset += this.accelerationChannel.strideSize, 
/* 322 */         lifeOffset += this.lifeChannel.strideSize) {
/*     */ 
/*     */         
/* 325 */         float strength = this.strengthChannel.data[strengthOffset + 0] + this.strengthChannel.data[strengthOffset + 1] * this.strengthValue.getScale(this.lifeChannel.data[lifeOffset]);
/* 326 */         TMP_V3.set(this.positionChannel.data[positionOffset + 0] - cx, this.positionChannel.data[positionOffset + 1] - cy, this.positionChannel.data[positionOffset + 2] - cz)
/*     */ 
/*     */           
/* 329 */           .nor().scl(strength);
/* 330 */         this.accelerationChannel.data[forceOffset + 0] = this.accelerationChannel.data[forceOffset + 0] + TMP_V3.x;
/* 331 */         this.accelerationChannel.data[forceOffset + 1] = this.accelerationChannel.data[forceOffset + 1] + TMP_V3.y;
/* 332 */         this.accelerationChannel.data[forceOffset + 2] = this.accelerationChannel.data[forceOffset + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public CentripetalAcceleration copy() {
/* 338 */       return new CentripetalAcceleration(this);
/*     */     } }
/*     */   
/*     */   public static class PolarAcceleration extends Angular {
/*     */     ParallelArray.FloatChannel directionalVelocityChannel;
/*     */     
/*     */     public PolarAcceleration() {}
/*     */     
/*     */     public PolarAcceleration(PolarAcceleration rotation) {
/* 347 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 352 */       super.allocateChannels();
/* 353 */       this.directionalVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 358 */       int i = 0, l = 2, s = 0, a = 0;
/* 359 */       int c = i + this.controller.particles.size * this.directionalVelocityChannel.strideSize;
/* 360 */       for (; i < c; 
/* 361 */         s += this.strengthChannel.strideSize, i += this.directionalVelocityChannel.strideSize, 
/* 362 */         a += this.angularChannel.strideSize, l += this.lifeChannel.strideSize) {
/*     */         
/* 364 */         float lifePercent = this.lifeChannel.data[l];
/*     */         
/* 366 */         float strength = this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue.getScale(lifePercent);
/*     */         
/* 368 */         float phi = this.angularChannel.data[a + 2] + this.angularChannel.data[a + 3] * this.phiValue.getScale(lifePercent);
/*     */         
/* 370 */         float theta = this.angularChannel.data[a + 0] + this.angularChannel.data[a + 1] * this.thetaValue.getScale(lifePercent);
/*     */         
/* 372 */         float cosTheta = MathUtils.cosDeg(theta), sinTheta = MathUtils.sinDeg(theta);
/* 373 */         float cosPhi = MathUtils.cosDeg(phi), sinPhi = MathUtils.sinDeg(phi);
/* 374 */         TMP_V3.set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi).nor().scl(strength);
/* 375 */         this.directionalVelocityChannel.data[i + 0] = this.directionalVelocityChannel.data[i + 0] + TMP_V3.x;
/* 376 */         this.directionalVelocityChannel.data[i + 1] = this.directionalVelocityChannel.data[i + 1] + TMP_V3.y;
/* 377 */         this.directionalVelocityChannel.data[i + 2] = this.directionalVelocityChannel.data[i + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public PolarAcceleration copy() {
/* 383 */       return new PolarAcceleration(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TangentialAcceleration extends Angular { ParallelArray.FloatChannel directionalVelocityChannel;
/*     */     ParallelArray.FloatChannel positionChannel;
/*     */     
/*     */     public TangentialAcceleration() {}
/*     */     
/*     */     public TangentialAcceleration(TangentialAcceleration rotation) {
/* 393 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 398 */       super.allocateChannels();
/* 399 */       this.directionalVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/* 400 */       this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 405 */       int i = 0, l = 2, s = 0, a = 0, positionOffset = 0;
/* 406 */       int c = i + this.controller.particles.size * this.directionalVelocityChannel.strideSize;
/* 407 */       for (; i < c; 
/* 408 */         s += this.strengthChannel.strideSize, i += this.directionalVelocityChannel.strideSize, 
/* 409 */         a += this.angularChannel.strideSize, l += this.lifeChannel.strideSize, positionOffset += this.positionChannel.strideSize) {
/*     */         
/* 411 */         float lifePercent = this.lifeChannel.data[l];
/*     */         
/* 413 */         float strength = this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue.getScale(lifePercent);
/*     */         
/* 415 */         float phi = this.angularChannel.data[a + 2] + this.angularChannel.data[a + 3] * this.phiValue.getScale(lifePercent);
/*     */         
/* 417 */         float theta = this.angularChannel.data[a + 0] + this.angularChannel.data[a + 1] * this.thetaValue.getScale(lifePercent);
/*     */         
/* 419 */         float cosTheta = MathUtils.cosDeg(theta), sinTheta = MathUtils.sinDeg(theta);
/* 420 */         float cosPhi = MathUtils.cosDeg(phi), sinPhi = MathUtils.sinDeg(phi);
/* 421 */         TMP_V3.set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi)
/* 422 */           .crs(this.positionChannel.data[positionOffset + 0], this.positionChannel.data[positionOffset + 1], this.positionChannel.data[positionOffset + 2])
/*     */ 
/*     */           
/* 425 */           .nor().scl(strength);
/* 426 */         this.directionalVelocityChannel.data[i + 0] = this.directionalVelocityChannel.data[i + 0] + TMP_V3.x;
/* 427 */         this.directionalVelocityChannel.data[i + 1] = this.directionalVelocityChannel.data[i + 1] + TMP_V3.y;
/* 428 */         this.directionalVelocityChannel.data[i + 2] = this.directionalVelocityChannel.data[i + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public TangentialAcceleration copy() {
/* 434 */       return new TangentialAcceleration(this);
/*     */     } }
/*     */   
/*     */   public static class BrownianAcceleration extends Strength {
/*     */     ParallelArray.FloatChannel accelerationChannel;
/*     */     
/*     */     public BrownianAcceleration() {}
/*     */     
/*     */     public BrownianAcceleration(BrownianAcceleration rotation) {
/* 443 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 448 */       super.allocateChannels();
/* 449 */       this.accelerationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 454 */       int lifeOffset = 2, strengthOffset = 0, forceOffset = 0;
/* 455 */       for (int i = 0, c = this.controller.particles.size; i < c; i++, 
/* 456 */         strengthOffset += this.strengthChannel.strideSize, 
/* 457 */         forceOffset += this.accelerationChannel.strideSize, 
/* 458 */         lifeOffset += this.lifeChannel.strideSize) {
/*     */ 
/*     */         
/* 461 */         float strength = this.strengthChannel.data[strengthOffset + 0] + this.strengthChannel.data[strengthOffset + 1] * this.strengthValue.getScale(this.lifeChannel.data[lifeOffset]);
/* 462 */         TMP_V3.set(MathUtils.random(-1.0F, 1.0F), MathUtils.random(-1.0F, 1.0F), MathUtils.random(-1.0F, 1.0F)).nor().scl(strength);
/* 463 */         this.accelerationChannel.data[forceOffset + 0] = this.accelerationChannel.data[forceOffset + 0] + TMP_V3.x;
/* 464 */         this.accelerationChannel.data[forceOffset + 1] = this.accelerationChannel.data[forceOffset + 1] + TMP_V3.y;
/* 465 */         this.accelerationChannel.data[forceOffset + 2] = this.accelerationChannel.data[forceOffset + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public BrownianAcceleration copy() {
/* 471 */       return new BrownianAcceleration(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isGlobal = false;
/*     */   
/*     */   protected ParallelArray.FloatChannel lifeChannel;
/*     */   
/*     */   public DynamicsModifier() {}
/*     */   
/*     */   public DynamicsModifier(DynamicsModifier modifier) {
/* 482 */     this.isGlobal = modifier.isGlobal;
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 487 */     this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 492 */     super.write(json);
/* 493 */     json.writeValue("isGlobal", Boolean.valueOf(this.isGlobal));
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 498 */     super.read(json, jsonData);
/* 499 */     this.isGlobal = ((Boolean)json.readValue("isGlobal", boolean.class, jsonData)).booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\DynamicsModifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */