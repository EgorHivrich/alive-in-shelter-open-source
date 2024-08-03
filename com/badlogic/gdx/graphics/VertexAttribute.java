/*     */ package com.badlogic.gdx.graphics;
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
/*     */ public final class VertexAttribute
/*     */ {
/*     */   public final int usage;
/*     */   public final int numComponents;
/*     */   public final boolean normalized;
/*     */   public final int type;
/*     */   public int offset;
/*     */   public String alias;
/*     */   public int unit;
/*     */   private final int usageIndex;
/*     */   
/*     */   public VertexAttribute(int usage, int numComponents, String alias) {
/*  52 */     this(usage, numComponents, alias, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexAttribute(int usage, int numComponents, String alias, int index) {
/*  63 */     this(usage, numComponents, (usage == 4) ? 5121 : 5126, (usage == 4), alias, index);
/*     */   }
/*     */ 
/*     */   
/*     */   private VertexAttribute(int usage, int numComponents, int type, boolean normalized, String alias) {
/*  68 */     this(usage, numComponents, type, normalized, alias, 0);
/*     */   }
/*     */   
/*     */   private VertexAttribute(int usage, int numComponents, int type, boolean normalized, String alias, int index) {
/*  72 */     this.usage = usage;
/*  73 */     this.numComponents = numComponents;
/*  74 */     this.type = type;
/*  75 */     this.normalized = normalized;
/*  76 */     this.alias = alias;
/*  77 */     this.unit = index;
/*  78 */     this.usageIndex = Integer.numberOfTrailingZeros(usage);
/*     */   }
/*     */   
/*     */   public static VertexAttribute Position() {
/*  82 */     return new VertexAttribute(1, 3, "a_position");
/*     */   }
/*     */   
/*     */   public static VertexAttribute TexCoords(int unit) {
/*  86 */     return new VertexAttribute(16, 2, "a_texCoord" + unit, unit);
/*     */   }
/*     */   
/*     */   public static VertexAttribute Normal() {
/*  90 */     return new VertexAttribute(8, 3, "a_normal");
/*     */   }
/*     */   
/*     */   public static VertexAttribute ColorPacked() {
/*  94 */     return new VertexAttribute(4, 4, 5121, true, "a_color");
/*     */   }
/*     */   
/*     */   public static VertexAttribute ColorUnpacked() {
/*  98 */     return new VertexAttribute(2, 4, 5126, false, "a_color");
/*     */   }
/*     */   
/*     */   public static VertexAttribute Tangent() {
/* 102 */     return new VertexAttribute(128, 3, "a_tangent");
/*     */   }
/*     */   
/*     */   public static VertexAttribute Binormal() {
/* 106 */     return new VertexAttribute(256, 3, "a_binormal");
/*     */   }
/*     */   
/*     */   public static VertexAttribute BoneWeight(int unit) {
/* 110 */     return new VertexAttribute(64, 2, "a_boneWeight" + unit, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 116 */     if (!(obj instanceof VertexAttribute)) {
/* 117 */       return false;
/*     */     }
/* 119 */     return equals((VertexAttribute)obj);
/*     */   }
/*     */   
/*     */   public boolean equals(VertexAttribute other) {
/* 123 */     return (other != null && this.usage == other.usage && this.numComponents == other.numComponents && this.alias.equals(other.alias) && this.unit == other.unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKey() {
/* 129 */     return (this.usageIndex << 8) + (this.unit & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 134 */     int result = getKey();
/* 135 */     result = 541 * result + this.numComponents;
/* 136 */     result = 541 * result + this.alias.hashCode();
/* 137 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\VertexAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */