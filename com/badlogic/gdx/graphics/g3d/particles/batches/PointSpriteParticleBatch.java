/*     */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
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
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.PointSpriteControllerRenderData;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PointSpriteParticleBatch
/*     */   extends BufferedParticleBatch<PointSpriteControllerRenderData>
/*     */ {
/*     */   private static boolean pointSpritesEnabled = false;
/*  33 */   protected static final Vector3 TMP_V1 = new Vector3();
/*     */   protected static final int sizeAndRotationUsage = 512;
/*  35 */   protected static final VertexAttributes CPU_ATTRIBUTES = new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(2, 4, "a_color"), new VertexAttribute(16, 4, "a_region"), new VertexAttribute(512, 3, "a_sizeAndRotation") });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected static final int CPU_VERTEX_SIZE = (short)(CPU_ATTRIBUTES.vertexSize / 4);
/*  41 */   protected static final int CPU_POSITION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(1)).offset / 4);
/*  42 */   protected static final int CPU_COLOR_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(2)).offset / 4);
/*  43 */   protected static final int CPU_REGION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(16)).offset / 4);
/*  44 */   protected static final int CPU_SIZE_AND_ROTATION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(512)).offset / 4);
/*     */   
/*     */   private static void enablePointSprites() {
/*  47 */     Gdx.gl.glEnable(34370);
/*  48 */     if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
/*  49 */       Gdx.gl.glEnable(34913);
/*     */     }
/*  51 */     pointSpritesEnabled = true;
/*     */   }
/*     */   
/*     */   private float[] vertices;
/*     */   Renderable renderable;
/*     */   
/*     */   public PointSpriteParticleBatch() {
/*  58 */     this(1000);
/*     */   }
/*     */   
/*     */   public PointSpriteParticleBatch(int capacity) {
/*  62 */     super(PointSpriteControllerRenderData.class);
/*     */     
/*  64 */     if (!pointSpritesEnabled) {
/*  65 */       enablePointSprites();
/*     */     }
/*  67 */     allocRenderable();
/*  68 */     ensureCapacity(capacity);
/*  69 */     this.renderable.shader = (Shader)new ParticleShader(this.renderable, new ParticleShader.Config(ParticleShader.ParticleType.Point));
/*  70 */     this.renderable.shader.init();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void allocParticlesData(int capacity) {
/*  75 */     this.vertices = new float[capacity * CPU_VERTEX_SIZE];
/*  76 */     if (this.renderable.meshPart.mesh != null)
/*  77 */       this.renderable.meshPart.mesh.dispose(); 
/*  78 */     this.renderable.meshPart.mesh = new Mesh(false, capacity, 0, CPU_ATTRIBUTES);
/*     */   }
/*     */   
/*     */   protected void allocRenderable() {
/*  82 */     this.renderable = new Renderable();
/*  83 */     this.renderable.meshPart.primitiveType = 0;
/*  84 */     this.renderable.meshPart.offset = 0;
/*  85 */     this.renderable
/*     */       
/*  87 */       .material = new Material(new Attribute[] { (Attribute)new BlendingAttribute(1, 771, 1.0F), (Attribute)new DepthTestAttribute(515, false), (Attribute)TextureAttribute.createDiffuse((Texture)null) });
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/*  91 */     TextureAttribute attribute = (TextureAttribute)this.renderable.material.get(TextureAttribute.Diffuse);
/*  92 */     attribute.textureDescription.texture = (GLTexture)texture;
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/*  96 */     TextureAttribute attribute = (TextureAttribute)this.renderable.material.get(TextureAttribute.Diffuse);
/*  97 */     return (Texture)attribute.textureDescription.texture;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flush(int[] offsets) {
/* 102 */     int tp = 0;
/* 103 */     for (PointSpriteControllerRenderData data : this.renderData) {
/* 104 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 105 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 106 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 107 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 108 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/*     */       
/* 110 */       for (int p = 0; p < data.controller.particles.size; p++, tp++) {
/* 111 */         int offset = offsets[tp] * CPU_VERTEX_SIZE;
/* 112 */         int regionOffset = p * regionChannel.strideSize;
/* 113 */         int positionOffset = p * positionChannel.strideSize;
/* 114 */         int colorOffset = p * colorChannel.strideSize;
/* 115 */         int rotationOffset = p * rotationChannel.strideSize;
/*     */         
/* 117 */         this.vertices[offset + CPU_POSITION_OFFSET] = positionChannel.data[positionOffset + 0];
/* 118 */         this.vertices[offset + CPU_POSITION_OFFSET + 1] = positionChannel.data[positionOffset + 1];
/* 119 */         this.vertices[offset + CPU_POSITION_OFFSET + 2] = positionChannel.data[positionOffset + 2];
/* 120 */         this.vertices[offset + CPU_COLOR_OFFSET] = colorChannel.data[colorOffset + 0];
/* 121 */         this.vertices[offset + CPU_COLOR_OFFSET + 1] = colorChannel.data[colorOffset + 1];
/* 122 */         this.vertices[offset + CPU_COLOR_OFFSET + 2] = colorChannel.data[colorOffset + 2];
/* 123 */         this.vertices[offset + CPU_COLOR_OFFSET + 3] = colorChannel.data[colorOffset + 3];
/* 124 */         this.vertices[offset + CPU_SIZE_AND_ROTATION_OFFSET] = scaleChannel.data[p * scaleChannel.strideSize];
/* 125 */         this.vertices[offset + CPU_SIZE_AND_ROTATION_OFFSET + 1] = rotationChannel.data[rotationOffset + 0];
/* 126 */         this.vertices[offset + CPU_SIZE_AND_ROTATION_OFFSET + 2] = rotationChannel.data[rotationOffset + 1];
/* 127 */         this.vertices[offset + CPU_REGION_OFFSET] = regionChannel.data[regionOffset + 0];
/* 128 */         this.vertices[offset + CPU_REGION_OFFSET + 1] = regionChannel.data[regionOffset + 1];
/* 129 */         this.vertices[offset + CPU_REGION_OFFSET + 2] = regionChannel.data[regionOffset + 2];
/* 130 */         this.vertices[offset + CPU_REGION_OFFSET + 3] = regionChannel.data[regionOffset + 3];
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     this.renderable.meshPart.size = this.bufferedParticlesCount;
/* 135 */     this.renderable.meshPart.mesh.setVertices(this.vertices, 0, this.bufferedParticlesCount * CPU_VERTEX_SIZE);
/* 136 */     this.renderable.meshPart.update();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 141 */     if (this.bufferedParticlesCount > 0) {
/* 142 */       renderables.add(((Renderable)pool.obtain()).set(this.renderable));
/*     */     }
/*     */   }
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 147 */     ResourceData.SaveData data = resources.createSaveData("pointSpriteBatch");
/* 148 */     data.saveAsset(manager.getAssetFileName(getTexture()), Texture.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 153 */     ResourceData.SaveData data = resources.getSaveData("pointSpriteBatch");
/* 154 */     if (data != null)
/* 155 */       setTexture((Texture)manager.get(data.loadAsset())); 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\batches\PointSpriteParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */