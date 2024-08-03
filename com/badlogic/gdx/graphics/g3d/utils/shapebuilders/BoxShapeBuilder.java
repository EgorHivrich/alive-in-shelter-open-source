/*     */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
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
/*     */ public class BoxShapeBuilder
/*     */   extends BaseShapeBuilder
/*     */ {
/*     */   public static void build(MeshPartBuilder builder, BoundingBox box) {
/*  35 */     builder.box(box.getCorner000(tmpV0), box.getCorner010(tmpV1), box.getCorner100(tmpV2), box.getCorner110(tmpV3), box
/*  36 */         .getCorner001(tmpV4), box.getCorner011(tmpV5), box.getCorner101(tmpV6), box.getCorner111(tmpV7));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, MeshPartBuilder.VertexInfo corner000, MeshPartBuilder.VertexInfo corner010, MeshPartBuilder.VertexInfo corner100, MeshPartBuilder.VertexInfo corner110, MeshPartBuilder.VertexInfo corner001, MeshPartBuilder.VertexInfo corner011, MeshPartBuilder.VertexInfo corner101, MeshPartBuilder.VertexInfo corner111) {
/*  42 */     builder.ensureVertices(8);
/*  43 */     short i000 = builder.vertex(corner000);
/*  44 */     short i100 = builder.vertex(corner100);
/*  45 */     short i110 = builder.vertex(corner110);
/*  46 */     short i010 = builder.vertex(corner010);
/*  47 */     short i001 = builder.vertex(corner001);
/*  48 */     short i101 = builder.vertex(corner101);
/*  49 */     short i111 = builder.vertex(corner111);
/*  50 */     short i011 = builder.vertex(corner011);
/*     */     
/*  52 */     int primitiveType = builder.getPrimitiveType();
/*  53 */     if (primitiveType == 1) {
/*  54 */       builder.ensureIndices(24);
/*  55 */       builder.rect(i000, i100, i110, i010);
/*  56 */       builder.rect(i101, i001, i011, i111);
/*  57 */       builder.index(i000, i001, i010, i011, i110, i111, i100, i101);
/*  58 */     } else if (primitiveType == 0) {
/*  59 */       builder.ensureRectangleIndices(2);
/*  60 */       builder.rect(i000, i100, i110, i010);
/*  61 */       builder.rect(i101, i001, i011, i111);
/*     */     } else {
/*  63 */       builder.ensureRectangleIndices(6);
/*  64 */       builder.rect(i000, i100, i110, i010);
/*  65 */       builder.rect(i101, i001, i011, i111);
/*  66 */       builder.rect(i000, i010, i011, i001);
/*  67 */       builder.rect(i101, i111, i110, i100);
/*  68 */       builder.rect(i101, i100, i000, i001);
/*  69 */       builder.rect(i110, i111, i011, i010);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
/*  76 */     if ((builder.getAttributes().getMask() & 0x198L) == 0L) {
/*  77 */       build(builder, vertTmp1.set(corner000, null, null, null), vertTmp2.set(corner010, null, null, null), vertTmp3
/*  78 */           .set(corner100, null, null, null), vertTmp4.set(corner110, null, null, null), vertTmp5
/*  79 */           .set(corner001, null, null, null), vertTmp6.set(corner011, null, null, null), vertTmp7
/*  80 */           .set(corner101, null, null, null), vertTmp8.set(corner111, null, null, null));
/*     */     } else {
/*  82 */       builder.ensureVertices(24);
/*  83 */       builder.ensureRectangleIndices(6);
/*  84 */       Vector3 nor = tmpV1.set(corner000).lerp(corner110, 0.5F).sub(tmpV2.set(corner001).lerp(corner111, 0.5F)).nor();
/*  85 */       builder.rect(corner000, corner010, corner110, corner100, nor);
/*  86 */       builder.rect(corner011, corner001, corner101, corner111, nor.scl(-1.0F));
/*  87 */       nor = tmpV1.set(corner000).lerp(corner101, 0.5F).sub(tmpV2.set(corner010).lerp(corner111, 0.5F)).nor();
/*  88 */       builder.rect(corner001, corner000, corner100, corner101, nor);
/*  89 */       builder.rect(corner010, corner011, corner111, corner110, nor.scl(-1.0F));
/*  90 */       nor = tmpV1.set(corner000).lerp(corner011, 0.5F).sub(tmpV2.set(corner100).lerp(corner111, 0.5F)).nor();
/*  91 */       builder.rect(corner001, corner011, corner010, corner000, nor);
/*  92 */       builder.rect(corner100, corner110, corner111, corner101, nor.scl(-1.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, Matrix4 transform) {
/*  98 */     build(builder, obtainV3().set(-0.5F, -0.5F, -0.5F).mul(transform), obtainV3().set(-0.5F, 0.5F, -0.5F).mul(transform), 
/*  99 */         obtainV3().set(0.5F, -0.5F, -0.5F).mul(transform), obtainV3().set(0.5F, 0.5F, -0.5F).mul(transform), 
/* 100 */         obtainV3().set(-0.5F, -0.5F, 0.5F).mul(transform), obtainV3().set(-0.5F, 0.5F, 0.5F).mul(transform), 
/* 101 */         obtainV3().set(0.5F, -0.5F, 0.5F).mul(transform), obtainV3().set(0.5F, 0.5F, 0.5F).mul(transform));
/* 102 */     freeAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, float depth) {
/* 107 */     build(builder, 0.0F, 0.0F, 0.0F, width, height, depth);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float x, float y, float z, float width, float height, float depth) {
/* 112 */     float hw = width * 0.5F;
/* 113 */     float hh = height * 0.5F;
/* 114 */     float hd = depth * 0.5F;
/* 115 */     float x0 = x - hw, y0 = y - hh, z0 = z - hd, x1 = x + hw, y1 = y + hh, z1 = z + hd;
/* 116 */     build(builder, 
/* 117 */         obtainV3().set(x0, y0, z0), obtainV3().set(x0, y1, z0), obtainV3().set(x1, y0, z0), obtainV3().set(x1, y1, z0), 
/* 118 */         obtainV3().set(x0, y0, z1), obtainV3().set(x0, y1, z1), obtainV3().set(x1, y0, z1), obtainV3().set(x1, y1, z1));
/* 119 */     freeAll();
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\BoxShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */