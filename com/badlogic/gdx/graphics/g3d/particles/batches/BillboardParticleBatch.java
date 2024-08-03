/*     */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.Shader;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardControllerRenderData;
/*     */ import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Matrix3;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BillboardParticleBatch
/*     */   extends BufferedParticleBatch<BillboardControllerRenderData>
/*     */ {
/*  35 */   protected static final Vector3 TMP_V1 = new Vector3();
/*  36 */   protected static final Vector3 TMP_V2 = new Vector3();
/*  37 */   protected static final Vector3 TMP_V3 = new Vector3();
/*  38 */   protected static final Vector3 TMP_V4 = new Vector3();
/*  39 */   protected static final Vector3 TMP_V5 = new Vector3();
/*  40 */   protected static final Vector3 TMP_V6 = new Vector3();
/*  41 */   protected static final Matrix3 TMP_M3 = new Matrix3();
/*     */   
/*     */   protected static final int sizeAndRotationUsage = 512;
/*     */   protected static final int directionUsage = 1024;
/*  45 */   private static final VertexAttributes GPU_ATTRIBUTES = new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(16, 2, "a_texCoord0"), new VertexAttribute(2, 4, "a_color"), new VertexAttribute(512, 4, "a_sizeAndRotation") });
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
/*  56 */   private static final VertexAttributes CPU_ATTRIBUTES = new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(16, 2, "a_texCoord0"), new VertexAttribute(2, 4, "a_color") });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final int GPU_POSITION_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(1)).offset / 4);
/*  62 */   private static final int GPU_UV_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(16)).offset / 4);
/*  63 */   private static final int GPU_SIZE_ROTATION_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(512)).offset / 4);
/*  64 */   private static final int GPU_COLOR_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(2)).offset / 4);
/*  65 */   private static final int GPU_VERTEX_SIZE = GPU_ATTRIBUTES.vertexSize / 4;
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
/*  78 */   private static final int CPU_POSITION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(1)).offset / 4);
/*  79 */   private static final int CPU_UV_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(16)).offset / 4);
/*  80 */   private static final int CPU_COLOR_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(2)).offset / 4); private static final int MAX_PARTICLES_PER_MESH = 8191; private static final int MAX_VERTICES_PER_MESH = 32764; private RenderablePool renderablePool;
/*  81 */   private static final int CPU_VERTEX_SIZE = CPU_ATTRIBUTES.vertexSize / 4;
/*     */   private Array<Renderable> renderables;
/*     */   private float[] vertices;
/*     */   private short[] indices;
/*     */   
/*     */   private class RenderablePool
/*     */     extends Pool<Renderable>
/*     */   {
/*     */     public Renderable newObject() {
/*  90 */       return BillboardParticleBatch.this.allocRenderable();
/*     */     } }
/*     */   
/*     */   public static class Config {
/*     */     boolean useGPU;
/*     */     
/*     */     public Config(boolean useGPU, ParticleShader.AlignMode mode) {
/*  97 */       this.useGPU = useGPU;
/*  98 */       this.mode = mode;
/*     */     }
/*     */ 
/*     */     
/*     */     ParticleShader.AlignMode mode;
/*     */ 
/*     */     
/*     */     public Config() {}
/*     */   }
/*     */   
/* 108 */   private int currentVertexSize = 0;
/*     */   private VertexAttributes currentAttributes;
/*     */   protected boolean useGPU = false;
/* 111 */   protected ParticleShader.AlignMode mode = ParticleShader.AlignMode.Screen;
/*     */ 
/*     */   
/*     */   protected Texture texture;
/*     */ 
/*     */   
/*     */   protected BlendingAttribute blendingAttribute;
/*     */ 
/*     */   
/*     */   protected DepthTestAttribute depthTestAttribute;
/*     */ 
/*     */   
/*     */   Shader shader;
/*     */ 
/*     */ 
/*     */   
/*     */   public BillboardParticleBatch(ParticleShader.AlignMode mode, boolean useGPU, int capacity, BlendingAttribute blendingAttribute, DepthTestAttribute depthTestAttribute) {
/* 128 */     super(BillboardControllerRenderData.class);
/* 129 */     this.renderables = new Array();
/* 130 */     this.renderablePool = new RenderablePool();
/* 131 */     this.blendingAttribute = blendingAttribute;
/* 132 */     this.depthTestAttribute = depthTestAttribute;
/*     */     
/* 134 */     if (this.blendingAttribute == null)
/* 135 */       this.blendingAttribute = new BlendingAttribute(1, 771, 1.0F); 
/* 136 */     if (this.depthTestAttribute == null) {
/* 137 */       this.depthTestAttribute = new DepthTestAttribute(515, false);
/*     */     }
/* 139 */     allocIndices();
/* 140 */     initRenderData();
/* 141 */     ensureCapacity(capacity);
/* 142 */     setUseGpu(useGPU);
/* 143 */     setAlignMode(mode);
/*     */   }
/*     */   
/*     */   public BillboardParticleBatch(ParticleShader.AlignMode mode, boolean useGPU, int capacity) {
/* 147 */     this(mode, useGPU, capacity, (BlendingAttribute)null, (DepthTestAttribute)null);
/*     */   }
/*     */   
/*     */   public BillboardParticleBatch() {
/* 151 */     this(ParticleShader.AlignMode.Screen, false, 100);
/*     */   }
/*     */   
/*     */   public BillboardParticleBatch(int capacity) {
/* 155 */     this(ParticleShader.AlignMode.Screen, false, capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocParticlesData(int capacity) {
/* 160 */     this.vertices = new float[this.currentVertexSize * 4 * capacity];
/* 161 */     allocRenderables(capacity);
/*     */   }
/*     */   
/*     */   protected Renderable allocRenderable() {
/* 165 */     Renderable renderable = new Renderable();
/* 166 */     renderable.meshPart.primitiveType = 4;
/* 167 */     renderable.meshPart.offset = 0;
/* 168 */     renderable
/* 169 */       .material = new Material(new Attribute[] { (Attribute)this.blendingAttribute, (Attribute)this.depthTestAttribute, (Attribute)TextureAttribute.createDiffuse(this.texture) });
/* 170 */     renderable.meshPart.mesh = new Mesh(false, 32764, 49146, this.currentAttributes);
/* 171 */     renderable.meshPart.mesh.setIndices(this.indices);
/* 172 */     renderable.shader = this.shader;
/* 173 */     return renderable;
/*     */   }
/*     */   
/*     */   private void allocIndices() {
/* 177 */     int indicesCount = 49146;
/* 178 */     this.indices = new short[indicesCount];
/* 179 */     for (int i = 0, vertex = 0; i < indicesCount; i += 6, vertex += 4) {
/* 180 */       this.indices[i] = (short)vertex;
/* 181 */       this.indices[i + 1] = (short)(vertex + 1);
/* 182 */       this.indices[i + 2] = (short)(vertex + 2);
/* 183 */       this.indices[i + 3] = (short)(vertex + 2);
/* 184 */       this.indices[i + 4] = (short)(vertex + 3);
/* 185 */       this.indices[i + 5] = (short)vertex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void allocRenderables(int capacity) {
/* 191 */     int meshCount = MathUtils.ceil((capacity / 8191));
/* 192 */     int free = this.renderablePool.getFree();
/* 193 */     if (free < meshCount)
/* 194 */       for (int i = 0, left = meshCount - free; i < left; i++) {
/* 195 */         this.renderablePool.free(this.renderablePool.newObject());
/*     */       } 
/*     */   }
/*     */   
/*     */   private Shader getShader(Renderable renderable) {
/* 200 */     Shader shader = this.useGPU ? (Shader)new ParticleShader(renderable, new ParticleShader.Config(this.mode)) : (Shader)new DefaultShader(renderable);
/*     */     
/* 202 */     shader.init();
/* 203 */     return shader;
/*     */   }
/*     */   
/*     */   private void allocShader() {
/* 207 */     Renderable newRenderable = allocRenderable();
/* 208 */     this.shader = newRenderable.shader = getShader(newRenderable);
/* 209 */     this.renderablePool.free(newRenderable);
/*     */   }
/*     */   
/*     */   private void clearRenderablesPool() {
/* 213 */     this.renderablePool.freeAll(this.renderables);
/* 214 */     for (int i = 0, free = this.renderablePool.getFree(); i < free; i++) {
/* 215 */       Renderable renderable = (Renderable)this.renderablePool.obtain();
/* 216 */       renderable.meshPart.mesh.dispose();
/*     */     } 
/* 218 */     this.renderables.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertexData() {
/* 223 */     if (this.useGPU) {
/* 224 */       this.currentAttributes = GPU_ATTRIBUTES;
/* 225 */       this.currentVertexSize = GPU_VERTEX_SIZE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 238 */       this.currentAttributes = CPU_ATTRIBUTES;
/* 239 */       this.currentVertexSize = CPU_VERTEX_SIZE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initRenderData() {
/* 246 */     setVertexData();
/* 247 */     clearRenderablesPool();
/* 248 */     allocShader();
/* 249 */     resetCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignMode(ParticleShader.AlignMode mode) {
/* 255 */     if (mode != this.mode) {
/* 256 */       this.mode = mode;
/* 257 */       if (this.useGPU) {
/* 258 */         initRenderData();
/* 259 */         allocRenderables(this.bufferedParticlesCount);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ParticleShader.AlignMode getAlignMode() {
/* 265 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseGpu(boolean useGPU) {
/* 271 */     if (this.useGPU != useGPU) {
/* 272 */       this.useGPU = useGPU;
/* 273 */       initRenderData();
/* 274 */       allocRenderables(this.bufferedParticlesCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isUseGPU() {
/* 279 */     return this.useGPU;
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/* 283 */     this.renderablePool.freeAll(this.renderables);
/* 284 */     this.renderables.clear();
/* 285 */     for (int i = 0, free = this.renderablePool.getFree(); i < free; i++) {
/* 286 */       Renderable renderable = (Renderable)this.renderablePool.obtain();
/* 287 */       TextureAttribute attribute = (TextureAttribute)renderable.material.get(TextureAttribute.Diffuse);
/* 288 */       attribute.textureDescription.texture = (GLTexture)texture;
/*     */     } 
/* 290 */     this.texture = texture;
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 294 */     return this.texture;
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 299 */     super.begin();
/* 300 */     this.renderablePool.freeAll(this.renderables);
/* 301 */     this.renderables.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void putVertex(float[] vertices, int offset, float x, float y, float z, float u, float v, float scaleX, float scaleY, float cosRotation, float sinRotation, float r, float g, float b, float a) {
/* 308 */     vertices[offset + GPU_POSITION_OFFSET] = x;
/* 309 */     vertices[offset + GPU_POSITION_OFFSET + 1] = y;
/* 310 */     vertices[offset + GPU_POSITION_OFFSET + 2] = z;
/*     */     
/* 312 */     vertices[offset + GPU_UV_OFFSET] = u;
/* 313 */     vertices[offset + GPU_UV_OFFSET + 1] = v;
/*     */     
/* 315 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET] = scaleX;
/* 316 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET + 1] = scaleY;
/* 317 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET + 2] = cosRotation;
/* 318 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET + 3] = sinRotation;
/*     */     
/* 320 */     vertices[offset + GPU_COLOR_OFFSET] = r;
/* 321 */     vertices[offset + GPU_COLOR_OFFSET + 1] = g;
/* 322 */     vertices[offset + GPU_COLOR_OFFSET + 2] = b;
/* 323 */     vertices[offset + GPU_COLOR_OFFSET + 3] = a;
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
/*     */   private static void putVertex(float[] vertices, int offset, Vector3 p, float u, float v, float r, float g, float b, float a) {
/* 358 */     vertices[offset + CPU_POSITION_OFFSET] = p.x;
/* 359 */     vertices[offset + CPU_POSITION_OFFSET + 1] = p.y;
/* 360 */     vertices[offset + CPU_POSITION_OFFSET + 2] = p.z;
/*     */     
/* 362 */     vertices[offset + CPU_UV_OFFSET] = u;
/* 363 */     vertices[offset + CPU_UV_OFFSET + 1] = v;
/*     */     
/* 365 */     vertices[offset + CPU_COLOR_OFFSET] = r;
/* 366 */     vertices[offset + CPU_COLOR_OFFSET + 1] = g;
/* 367 */     vertices[offset + CPU_COLOR_OFFSET + 2] = b;
/* 368 */     vertices[offset + CPU_COLOR_OFFSET + 3] = a;
/*     */   }
/*     */   
/*     */   private void fillVerticesGPU(int[] particlesOffset) {
/* 372 */     int tp = 0;
/* 373 */     for (BillboardControllerRenderData data : this.renderData) {
/* 374 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 375 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 376 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 377 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 378 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/* 379 */       for (int p = 0, c = data.controller.particles.size; p < c; p++, tp++) {
/* 380 */         int baseOffset = particlesOffset[tp] * this.currentVertexSize * 4;
/* 381 */         float scale = scaleChannel.data[p * scaleChannel.strideSize];
/* 382 */         int regionOffset = p * regionChannel.strideSize;
/* 383 */         int positionOffset = p * positionChannel.strideSize;
/* 384 */         int colorOffset = p * colorChannel.strideSize;
/* 385 */         int rotationOffset = p * rotationChannel.strideSize;
/* 386 */         float px = positionChannel.data[positionOffset + 0];
/* 387 */         float py = positionChannel.data[positionOffset + 1];
/* 388 */         float pz = positionChannel.data[positionOffset + 2];
/* 389 */         float u = regionChannel.data[regionOffset + 0];
/* 390 */         float v = regionChannel.data[regionOffset + 1];
/* 391 */         float u2 = regionChannel.data[regionOffset + 2];
/* 392 */         float v2 = regionChannel.data[regionOffset + 3];
/* 393 */         float sx = regionChannel.data[regionOffset + 4] * scale;
/* 394 */         float sy = regionChannel.data[regionOffset + 5] * scale;
/* 395 */         float r = colorChannel.data[colorOffset + 0];
/* 396 */         float g = colorChannel.data[colorOffset + 1];
/* 397 */         float b = colorChannel.data[colorOffset + 2];
/* 398 */         float a = colorChannel.data[colorOffset + 3];
/* 399 */         float cosRotation = rotationChannel.data[rotationOffset + 0];
/* 400 */         float sinRotation = rotationChannel.data[rotationOffset + 1];
/*     */ 
/*     */         
/* 403 */         putVertex(this.vertices, baseOffset, px, py, pz, u, v2, -sx, -sy, cosRotation, sinRotation, r, g, b, a);
/* 404 */         baseOffset += this.currentVertexSize;
/* 405 */         putVertex(this.vertices, baseOffset, px, py, pz, u2, v2, sx, -sy, cosRotation, sinRotation, r, g, b, a);
/* 406 */         baseOffset += this.currentVertexSize;
/* 407 */         putVertex(this.vertices, baseOffset, px, py, pz, u2, v, sx, sy, cosRotation, sinRotation, r, g, b, a);
/* 408 */         baseOffset += this.currentVertexSize;
/* 409 */         putVertex(this.vertices, baseOffset, px, py, pz, u, v, -sx, sy, cosRotation, sinRotation, r, g, b, a);
/* 410 */         baseOffset += this.currentVertexSize;
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
/*     */   private void fillVerticesToViewPointCPU(int[] particlesOffset) {
/* 533 */     int tp = 0;
/* 534 */     for (BillboardControllerRenderData data : this.renderData) {
/* 535 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 536 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 537 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 538 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 539 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/*     */       
/* 541 */       for (int p = 0, c = data.controller.particles.size; p < c; p++, tp++) {
/* 542 */         int baseOffset = particlesOffset[tp] * this.currentVertexSize * 4;
/* 543 */         float scale = scaleChannel.data[p * scaleChannel.strideSize];
/* 544 */         int regionOffset = p * regionChannel.strideSize;
/* 545 */         int positionOffset = p * positionChannel.strideSize;
/* 546 */         int colorOffset = p * colorChannel.strideSize;
/* 547 */         int rotationOffset = p * rotationChannel.strideSize;
/* 548 */         float px = positionChannel.data[positionOffset + 0];
/* 549 */         float py = positionChannel.data[positionOffset + 1];
/* 550 */         float pz = positionChannel.data[positionOffset + 2];
/* 551 */         float u = regionChannel.data[regionOffset + 0];
/* 552 */         float v = regionChannel.data[regionOffset + 1];
/* 553 */         float u2 = regionChannel.data[regionOffset + 2];
/* 554 */         float v2 = regionChannel.data[regionOffset + 3];
/* 555 */         float sx = regionChannel.data[regionOffset + 4] * scale;
/* 556 */         float sy = regionChannel.data[regionOffset + 5] * scale;
/* 557 */         float r = colorChannel.data[colorOffset + 0];
/* 558 */         float g = colorChannel.data[colorOffset + 1];
/* 559 */         float b = colorChannel.data[colorOffset + 2];
/* 560 */         float a = colorChannel.data[colorOffset + 3];
/* 561 */         float cosRotation = rotationChannel.data[rotationOffset + 0];
/* 562 */         float sinRotation = rotationChannel.data[rotationOffset + 1];
/* 563 */         Vector3 look = TMP_V3.set(this.camera.position).sub(px, py, pz).nor();
/* 564 */         Vector3 right = TMP_V1.set(this.camera.up).crs(look).nor();
/* 565 */         Vector3 up = TMP_V2.set(look).crs(right);
/* 566 */         right.scl(sx);
/* 567 */         up.scl(sy);
/*     */         
/* 569 */         if (cosRotation != 1.0F) {
/* 570 */           TMP_M3.setToRotation(look, cosRotation, sinRotation);
/* 571 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x - TMP_V2.x, -TMP_V1.y - TMP_V2.y, -TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v2, r, g, b, a);
/* 572 */           baseOffset += this.currentVertexSize;
/* 573 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x - TMP_V2.x, TMP_V1.y - TMP_V2.y, TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v2, r, g, b, a);
/* 574 */           baseOffset += this.currentVertexSize;
/* 575 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x + TMP_V2.x, TMP_V1.y + TMP_V2.y, TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v, r, g, b, a);
/* 576 */           baseOffset += this.currentVertexSize;
/* 577 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x + TMP_V2.x, -TMP_V1.y + TMP_V2.y, -TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v, r, g, b, a);
/*     */         } else {
/*     */           
/* 580 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x - TMP_V2.x + px, -TMP_V1.y - TMP_V2.y + py, -TMP_V1.z - TMP_V2.z + pz), u, v2, r, g, b, a);
/* 581 */           baseOffset += this.currentVertexSize;
/* 582 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x - TMP_V2.x + px, TMP_V1.y - TMP_V2.y + py, TMP_V1.z - TMP_V2.z + pz), u2, v2, r, g, b, a);
/* 583 */           baseOffset += this.currentVertexSize;
/* 584 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x + TMP_V2.x + px, TMP_V1.y + TMP_V2.y + py, TMP_V1.z + TMP_V2.z + pz), u2, v, r, g, b, a);
/* 585 */           baseOffset += this.currentVertexSize;
/* 586 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x + TMP_V2.x + px, -TMP_V1.y + TMP_V2.y + py, -TMP_V1.z + TMP_V2.z + pz), u, v, r, g, b, a);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fillVerticesToScreenCPU(int[] particlesOffset) {
/* 593 */     Vector3 look = TMP_V3.set(this.camera.direction).scl(-1.0F);
/* 594 */     Vector3 right = TMP_V4.set(this.camera.up).crs(look).nor();
/* 595 */     Vector3 up = this.camera.up;
/*     */     
/* 597 */     int tp = 0;
/* 598 */     for (BillboardControllerRenderData data : this.renderData) {
/* 599 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 600 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 601 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 602 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 603 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/*     */       
/* 605 */       for (int p = 0, c = data.controller.particles.size; p < c; p++, tp++) {
/* 606 */         int baseOffset = particlesOffset[tp] * this.currentVertexSize * 4;
/* 607 */         float scale = scaleChannel.data[p * scaleChannel.strideSize];
/* 608 */         int regionOffset = p * regionChannel.strideSize;
/* 609 */         int positionOffset = p * positionChannel.strideSize;
/* 610 */         int colorOffset = p * colorChannel.strideSize;
/* 611 */         int rotationOffset = p * rotationChannel.strideSize;
/* 612 */         float px = positionChannel.data[positionOffset + 0];
/* 613 */         float py = positionChannel.data[positionOffset + 1];
/* 614 */         float pz = positionChannel.data[positionOffset + 2];
/* 615 */         float u = regionChannel.data[regionOffset + 0];
/* 616 */         float v = regionChannel.data[regionOffset + 1];
/* 617 */         float u2 = regionChannel.data[regionOffset + 2];
/* 618 */         float v2 = regionChannel.data[regionOffset + 3];
/* 619 */         float sx = regionChannel.data[regionOffset + 4] * scale;
/* 620 */         float sy = regionChannel.data[regionOffset + 5] * scale;
/* 621 */         float r = colorChannel.data[colorOffset + 0];
/* 622 */         float g = colorChannel.data[colorOffset + 1];
/* 623 */         float b = colorChannel.data[colorOffset + 2];
/* 624 */         float a = colorChannel.data[colorOffset + 3];
/* 625 */         float cosRotation = rotationChannel.data[rotationOffset + 0];
/* 626 */         float sinRotation = rotationChannel.data[rotationOffset + 1];
/* 627 */         TMP_V1.set(right).scl(sx);
/* 628 */         TMP_V2.set(up).scl(sy);
/*     */         
/* 630 */         if (cosRotation != 1.0F) {
/* 631 */           TMP_M3.setToRotation(look, cosRotation, sinRotation);
/* 632 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x - TMP_V2.x, -TMP_V1.y - TMP_V2.y, -TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v2, r, g, b, a);
/* 633 */           baseOffset += this.currentVertexSize;
/* 634 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x - TMP_V2.x, TMP_V1.y - TMP_V2.y, TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v2, r, g, b, a);
/* 635 */           baseOffset += this.currentVertexSize;
/* 636 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x + TMP_V2.x, TMP_V1.y + TMP_V2.y, TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v, r, g, b, a);
/* 637 */           baseOffset += this.currentVertexSize;
/* 638 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x + TMP_V2.x, -TMP_V1.y + TMP_V2.y, -TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v, r, g, b, a);
/*     */         } else {
/*     */           
/* 641 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x - TMP_V2.x + px, -TMP_V1.y - TMP_V2.y + py, -TMP_V1.z - TMP_V2.z + pz), u, v2, r, g, b, a);
/* 642 */           baseOffset += this.currentVertexSize;
/* 643 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x - TMP_V2.x + px, TMP_V1.y - TMP_V2.y + py, TMP_V1.z - TMP_V2.z + pz), u2, v2, r, g, b, a);
/* 644 */           baseOffset += this.currentVertexSize;
/* 645 */           putVertex(this.vertices, baseOffset, TMP_V6.set(TMP_V1.x + TMP_V2.x + px, TMP_V1.y + TMP_V2.y + py, TMP_V1.z + TMP_V2.z + pz), u2, v, r, g, b, a);
/* 646 */           baseOffset += this.currentVertexSize;
/* 647 */           putVertex(this.vertices, baseOffset, TMP_V6.set(-TMP_V1.x + TMP_V2.x + px, -TMP_V1.y + TMP_V2.y + py, -TMP_V1.z + TMP_V2.z + pz), u, v, r, g, b, a);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flush(int[] offsets) {
/* 657 */     if (this.useGPU) {
/*     */       
/* 659 */       fillVerticesGPU(offsets);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 664 */     else if (this.mode == ParticleShader.AlignMode.Screen) {
/* 665 */       fillVerticesToScreenCPU(offsets);
/* 666 */     } else if (this.mode == ParticleShader.AlignMode.ViewPoint) {
/* 667 */       fillVerticesToViewPointCPU(offsets);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 673 */     int addedVertexCount = 0;
/* 674 */     int vCount = this.bufferedParticlesCount * 4; int v;
/* 675 */     for (v = 0; v < vCount; v += addedVertexCount) {
/* 676 */       addedVertexCount = Math.min(vCount - v, 32764);
/* 677 */       Renderable renderable = (Renderable)this.renderablePool.obtain();
/* 678 */       renderable.meshPart.size = addedVertexCount / 4 * 6;
/* 679 */       renderable.meshPart.mesh.setVertices(this.vertices, this.currentVertexSize * v, this.currentVertexSize * addedVertexCount);
/* 680 */       renderable.meshPart.update();
/* 681 */       this.renderables.add(renderable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 687 */     for (Renderable renderable : this.renderables) {
/* 688 */       renderables.add(((Renderable)pool.obtain()).set(renderable));
/*     */     }
/*     */   }
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 693 */     ResourceData.SaveData data = resources.createSaveData("billboardBatch");
/* 694 */     data.save("cfg", new Config(this.useGPU, this.mode));
/* 695 */     data.saveAsset(manager.getAssetFileName(this.texture), Texture.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 700 */     ResourceData.SaveData data = resources.getSaveData("billboardBatch");
/* 701 */     if (data != null) {
/* 702 */       setTexture((Texture)manager.get(data.loadAsset()));
/* 703 */       Config cfg = (Config)data.load("cfg");
/* 704 */       setUseGpu(cfg.useGPU);
/* 705 */       setAlignMode(cfg.mode);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\batches\BillboardParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */