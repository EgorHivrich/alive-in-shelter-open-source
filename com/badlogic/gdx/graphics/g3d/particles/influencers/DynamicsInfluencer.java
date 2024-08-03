/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ public class DynamicsInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<DynamicsModifier> velocities;
/*     */   private ParallelArray.FloatChannel accellerationChannel;
/*     */   private ParallelArray.FloatChannel positionChannel;
/*     */   private ParallelArray.FloatChannel previousPositionChannel;
/*     */   
/*     */   public DynamicsInfluencer() {
/*  23 */     this.velocities = new Array(true, 3, DynamicsModifier.class);
/*     */   }
/*     */   private ParallelArray.FloatChannel rotationChannel; private ParallelArray.FloatChannel angularVelocityChannel; boolean hasAcceleration; boolean has2dAngularVelocity; boolean has3dAngularVelocity;
/*     */   public DynamicsInfluencer(DynamicsModifier... velocities) {
/*  27 */     this.velocities = new Array(true, velocities.length, DynamicsModifier.class);
/*  28 */     for (DynamicsModifier value : velocities) {
/*  29 */       this.velocities.add(value.copy());
/*     */     }
/*     */   }
/*     */   
/*     */   public DynamicsInfluencer(DynamicsInfluencer velocityInfluencer) {
/*  34 */     this((DynamicsModifier[])velocityInfluencer.velocities.toArray(DynamicsModifier.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/*  39 */     for (int k = 0; k < this.velocities.size; k++) {
/*  40 */       ((DynamicsModifier[])this.velocities.items)[k].allocateChannels();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  45 */     this.accellerationChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Acceleration);
/*  46 */     this.hasAcceleration = (this.accellerationChannel != null);
/*  47 */     if (this.hasAcceleration) {
/*  48 */       this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*  49 */       this.previousPositionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.PreviousPosition);
/*     */     } 
/*     */ 
/*     */     
/*  53 */     this.angularVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.AngularVelocity2D);
/*  54 */     this.has2dAngularVelocity = (this.angularVelocityChannel != null);
/*  55 */     if (this.has2dAngularVelocity) {
/*  56 */       this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation2D);
/*  57 */       this.has3dAngularVelocity = false;
/*     */     } else {
/*     */       
/*  60 */       this.angularVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.AngularVelocity3D);
/*  61 */       this.has3dAngularVelocity = (this.angularVelocityChannel != null);
/*  62 */       if (this.has3dAngularVelocity) {
/*  63 */         this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation3D);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(ParticleController particleController) {
/*  69 */     super.set(particleController);
/*  70 */     for (int k = 0; k < this.velocities.size; k++) {
/*  71 */       ((DynamicsModifier[])this.velocities.items)[k].set(particleController);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  77 */     for (int k = 0; k < this.velocities.size; k++) {
/*  78 */       ((DynamicsModifier[])this.velocities.items)[k].init();
/*     */     }
/*     */   }
/*     */   
/*     */   public void activateParticles(int startIndex, int count) {
/*  83 */     if (this.hasAcceleration)
/*     */     {
/*     */       
/*  86 */       for (int i = startIndex * this.positionChannel.strideSize, c = i + count * this.positionChannel.strideSize; i < c; i += this.positionChannel.strideSize) {
/*  87 */         this.previousPositionChannel.data[i + 0] = this.positionChannel.data[i + 0];
/*  88 */         this.previousPositionChannel.data[i + 1] = this.positionChannel.data[i + 1];
/*  89 */         this.previousPositionChannel.data[i + 2] = this.positionChannel.data[i + 2];
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     if (this.has2dAngularVelocity) {
/*     */       
/* 101 */       for (int i = startIndex * this.rotationChannel.strideSize, c = i + count * this.rotationChannel.strideSize; i < c; i += this.rotationChannel.strideSize) {
/* 102 */         this.rotationChannel.data[i + 0] = 1.0F;
/* 103 */         this.rotationChannel.data[i + 1] = 0.0F;
/*     */       }
/*     */     
/* 106 */     } else if (this.has3dAngularVelocity) {
/*     */       
/* 108 */       for (int i = startIndex * this.rotationChannel.strideSize, c = i + count * this.rotationChannel.strideSize; i < c; i += this.rotationChannel.strideSize) {
/* 109 */         this.rotationChannel.data[i + 0] = 0.0F;
/* 110 */         this.rotationChannel.data[i + 1] = 0.0F;
/* 111 */         this.rotationChannel.data[i + 2] = 0.0F;
/* 112 */         this.rotationChannel.data[i + 3] = 1.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     for (int k = 0; k < this.velocities.size; k++) {
/* 117 */       ((DynamicsModifier[])this.velocities.items)[k].activateParticles(startIndex, count);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 123 */     if (this.hasAcceleration)
/* 124 */       Arrays.fill(this.accellerationChannel.data, 0, this.controller.particles.size * this.accellerationChannel.strideSize, 0.0F); 
/* 125 */     if (this.has2dAngularVelocity || this.has3dAngularVelocity) {
/* 126 */       Arrays.fill(this.angularVelocityChannel.data, 0, this.controller.particles.size * this.angularVelocityChannel.strideSize, 0.0F);
/*     */     }
/*     */     
/* 129 */     for (int k = 0; k < this.velocities.size; k++) {
/* 130 */       ((DynamicsModifier[])this.velocities.items)[k].update();
/*     */     }
/*     */ 
/*     */     
/* 134 */     if (this.hasAcceleration)
/*     */     {
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
/* 148 */       for (int i = 0, offset = 0; i < this.controller.particles.size; i++, offset += this.positionChannel.strideSize) {
/* 149 */         float x = this.positionChannel.data[offset + 0];
/* 150 */         float y = this.positionChannel.data[offset + 1];
/* 151 */         float z = this.positionChannel.data[offset + 2];
/* 152 */         this.positionChannel.data[offset + 0] = 2.0F * x - this.previousPositionChannel.data[offset + 0] + this.accellerationChannel.data[offset + 0] * this.controller.deltaTimeSqr;
/*     */         
/* 154 */         this.positionChannel.data[offset + 1] = 2.0F * y - this.previousPositionChannel.data[offset + 1] + this.accellerationChannel.data[offset + 1] * this.controller.deltaTimeSqr;
/*     */         
/* 156 */         this.positionChannel.data[offset + 2] = 2.0F * z - this.previousPositionChannel.data[offset + 2] + this.accellerationChannel.data[offset + 2] * this.controller.deltaTimeSqr;
/*     */         
/* 158 */         this.previousPositionChannel.data[offset + 0] = x;
/* 159 */         this.previousPositionChannel.data[offset + 1] = y;
/* 160 */         this.previousPositionChannel.data[offset + 2] = z;
/*     */       } 
/*     */     }
/*     */     
/* 164 */     if (this.has2dAngularVelocity) {
/* 165 */       for (int i = 0, offset = 0; i < this.controller.particles.size; i++, offset += this.rotationChannel.strideSize) {
/* 166 */         float rotation = this.angularVelocityChannel.data[i] * this.controller.deltaTime;
/* 167 */         if (rotation != 0.0F) {
/* 168 */           float cosBeta = MathUtils.cosDeg(rotation), sinBeta = MathUtils.sinDeg(rotation);
/* 169 */           float currentCosine = this.rotationChannel.data[offset + 0];
/* 170 */           float currentSine = this.rotationChannel.data[offset + 1];
/* 171 */           float newCosine = currentCosine * cosBeta - currentSine * sinBeta;
/* 172 */           float newSine = currentSine * cosBeta + currentCosine * sinBeta;
/* 173 */           this.rotationChannel.data[offset + 0] = newCosine;
/* 174 */           this.rotationChannel.data[offset + 1] = newSine;
/*     */         }
/*     */       
/*     */       } 
/* 178 */     } else if (this.has3dAngularVelocity) {
/* 179 */       for (int i = 0, offset = 0, angularOffset = 0; i < this.controller.particles.size; i++, 
/* 180 */         offset += this.rotationChannel.strideSize, angularOffset += this.angularVelocityChannel.strideSize) {
/*     */         
/* 182 */         float wx = this.angularVelocityChannel.data[angularOffset + 0];
/* 183 */         float wy = this.angularVelocityChannel.data[angularOffset + 1];
/* 184 */         float wz = this.angularVelocityChannel.data[angularOffset + 2];
/* 185 */         float qx = this.rotationChannel.data[offset + 0];
/* 186 */         float qy = this.rotationChannel.data[offset + 1];
/* 187 */         float qz = this.rotationChannel.data[offset + 2];
/* 188 */         float qw = this.rotationChannel.data[offset + 3];
/* 189 */         TMP_Q.set(wx, wy, wz, 0.0F).mul(qx, qy, qz, qw).mul(0.5F * this.controller.deltaTime).add(qx, qy, qz, qw).nor();
/* 190 */         this.rotationChannel.data[offset + 0] = TMP_Q.x;
/* 191 */         this.rotationChannel.data[offset + 1] = TMP_Q.y;
/* 192 */         this.rotationChannel.data[offset + 2] = TMP_Q.z;
/* 193 */         this.rotationChannel.data[offset + 3] = TMP_Q.w;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicsInfluencer copy() {
/* 200 */     return new DynamicsInfluencer(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 205 */     json.writeValue("velocities", this.velocities, Array.class, DynamicsModifier.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 210 */     this.velocities.addAll((Array)json.readValue("velocities", Array.class, DynamicsModifier.class, jsonData));
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\influencers\DynamicsInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */