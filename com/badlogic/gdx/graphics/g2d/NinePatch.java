/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class NinePatch
/*     */ {
/*     */   public static final int TOP_LEFT = 0;
/*     */   public static final int TOP_CENTER = 1;
/*     */   public static final int TOP_RIGHT = 2;
/*     */   public static final int MIDDLE_LEFT = 3;
/*     */   public static final int MIDDLE_CENTER = 4;
/*     */   public static final int MIDDLE_RIGHT = 5;
/*     */   public static final int BOTTOM_LEFT = 6;
/*     */   public static final int BOTTOM_CENTER = 7;
/*     */   public static final int BOTTOM_RIGHT = 8;
/*  49 */   private static final Color tmpDrawColor = new Color();
/*     */   
/*     */   private Texture texture;
/*  52 */   private int bottomLeft = -1, bottomCenter = -1, bottomRight = -1;
/*  53 */   private int middleLeft = -1, middleCenter = -1, middleRight = -1;
/*  54 */   private int topLeft = -1; private int topCenter = -1; private int topRight = -1; private float leftWidth; private float rightWidth;
/*     */   private float middleWidth;
/*  56 */   private float[] vertices = new float[180]; private float middleHeight; private float topHeight; private float bottomHeight;
/*     */   private int idx;
/*  58 */   private final Color color = new Color(Color.WHITE);
/*  59 */   private float padLeft = -1.0F, padRight = -1.0F, padTop = -1.0F, padBottom = -1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NinePatch(Texture texture, int left, int right, int top, int bottom) {
/*  69 */     this(new TextureRegion(texture), left, right, top, bottom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NinePatch(TextureRegion region, int left, int right, int top, int bottom) {
/*  80 */     if (region == null) throw new IllegalArgumentException("region cannot be null."); 
/*  81 */     int middleWidth = region.getRegionWidth() - left - right;
/*  82 */     int middleHeight = region.getRegionHeight() - top - bottom;
/*     */     
/*  84 */     TextureRegion[] patches = new TextureRegion[9];
/*  85 */     if (top > 0) {
/*  86 */       if (left > 0) patches[0] = new TextureRegion(region, 0, 0, left, top); 
/*  87 */       if (middleWidth > 0) patches[1] = new TextureRegion(region, left, 0, middleWidth, top); 
/*  88 */       if (right > 0) patches[2] = new TextureRegion(region, left + middleWidth, 0, right, top); 
/*     */     } 
/*  90 */     if (middleHeight > 0) {
/*  91 */       if (left > 0) patches[3] = new TextureRegion(region, 0, top, left, middleHeight); 
/*  92 */       if (middleWidth > 0) patches[4] = new TextureRegion(region, left, top, middleWidth, middleHeight); 
/*  93 */       if (right > 0) patches[5] = new TextureRegion(region, left + middleWidth, top, right, middleHeight); 
/*     */     } 
/*  95 */     if (bottom > 0) {
/*  96 */       if (left > 0) patches[6] = new TextureRegion(region, 0, top + middleHeight, left, bottom); 
/*  97 */       if (middleWidth > 0) patches[7] = new TextureRegion(region, left, top + middleHeight, middleWidth, bottom); 
/*  98 */       if (right > 0) patches[8] = new TextureRegion(region, left + middleWidth, top + middleHeight, right, bottom);
/*     */     
/*     */     } 
/*     */     
/* 102 */     if (left == 0 && middleWidth == 0) {
/* 103 */       patches[1] = patches[2];
/* 104 */       patches[4] = patches[5];
/* 105 */       patches[7] = patches[8];
/* 106 */       patches[2] = null;
/* 107 */       patches[5] = null;
/* 108 */       patches[8] = null;
/*     */     } 
/*     */     
/* 111 */     if (top == 0 && middleHeight == 0) {
/* 112 */       patches[3] = patches[6];
/* 113 */       patches[4] = patches[7];
/* 114 */       patches[5] = patches[8];
/* 115 */       patches[6] = null;
/* 116 */       patches[7] = null;
/* 117 */       patches[8] = null;
/*     */     } 
/*     */     
/* 120 */     load(patches);
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(Texture texture, Color color) {
/* 125 */     this(texture);
/* 126 */     setColor(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(Texture texture) {
/* 131 */     this(new TextureRegion(texture));
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(TextureRegion region, Color color) {
/* 136 */     this(region);
/* 137 */     setColor(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(TextureRegion region) {
/* 142 */     load(new TextureRegion[] { null, null, null, null, region, null, null, null, null });
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
/*     */   public NinePatch(TextureRegion... patches) {
/* 154 */     if (patches == null || patches.length != 9) throw new IllegalArgumentException("NinePatch needs nine TextureRegions");
/*     */     
/* 156 */     load(patches);
/*     */     
/* 158 */     float leftWidth = getLeftWidth();
/* 159 */     if ((patches[0] != null && patches[0].getRegionWidth() != leftWidth) || (patches[3] != null && patches[3]
/* 160 */       .getRegionWidth() != leftWidth) || (patches[6] != null && patches[6]
/* 161 */       .getRegionWidth() != leftWidth)) {
/* 162 */       throw new GdxRuntimeException("Left side patches must have the same width");
/*     */     }
/*     */     
/* 165 */     float rightWidth = getRightWidth();
/* 166 */     if ((patches[2] != null && patches[2].getRegionWidth() != rightWidth) || (patches[5] != null && patches[5]
/* 167 */       .getRegionWidth() != rightWidth) || (patches[8] != null && patches[8]
/* 168 */       .getRegionWidth() != rightWidth)) {
/* 169 */       throw new GdxRuntimeException("Right side patches must have the same width");
/*     */     }
/*     */     
/* 172 */     float bottomHeight = getBottomHeight();
/* 173 */     if ((patches[6] != null && patches[6].getRegionHeight() != bottomHeight) || (patches[7] != null && patches[7]
/* 174 */       .getRegionHeight() != bottomHeight) || (patches[8] != null && patches[8]
/* 175 */       .getRegionHeight() != bottomHeight)) {
/* 176 */       throw new GdxRuntimeException("Bottom side patches must have the same height");
/*     */     }
/*     */     
/* 179 */     float topHeight = getTopHeight();
/* 180 */     if ((patches[0] != null && patches[0].getRegionHeight() != topHeight) || (patches[1] != null && patches[1]
/* 181 */       .getRegionHeight() != topHeight) || (patches[2] != null && patches[2]
/* 182 */       .getRegionHeight() != topHeight)) {
/* 183 */       throw new GdxRuntimeException("Top side patches must have the same height");
/*     */     }
/*     */   }
/*     */   
/*     */   public NinePatch(NinePatch ninePatch) {
/* 188 */     this(ninePatch, ninePatch.color);
/*     */   }
/*     */   
/*     */   public NinePatch(NinePatch ninePatch, Color color) {
/* 192 */     this.texture = ninePatch.texture;
/*     */     
/* 194 */     this.bottomLeft = ninePatch.bottomLeft;
/* 195 */     this.bottomCenter = ninePatch.bottomCenter;
/* 196 */     this.bottomRight = ninePatch.bottomRight;
/* 197 */     this.middleLeft = ninePatch.middleLeft;
/* 198 */     this.middleCenter = ninePatch.middleCenter;
/* 199 */     this.middleRight = ninePatch.middleRight;
/* 200 */     this.topLeft = ninePatch.topLeft;
/* 201 */     this.topCenter = ninePatch.topCenter;
/* 202 */     this.topRight = ninePatch.topRight;
/*     */     
/* 204 */     this.leftWidth = ninePatch.leftWidth;
/* 205 */     this.rightWidth = ninePatch.rightWidth;
/* 206 */     this.middleWidth = ninePatch.middleWidth;
/* 207 */     this.middleHeight = ninePatch.middleHeight;
/* 208 */     this.topHeight = ninePatch.topHeight;
/* 209 */     this.bottomHeight = ninePatch.bottomHeight;
/*     */     
/* 211 */     this.padLeft = ninePatch.padLeft;
/* 212 */     this.padTop = ninePatch.padTop;
/* 213 */     this.padBottom = ninePatch.padBottom;
/* 214 */     this.padRight = ninePatch.padRight;
/*     */     
/* 216 */     this.vertices = new float[ninePatch.vertices.length];
/* 217 */     System.arraycopy(ninePatch.vertices, 0, this.vertices, 0, ninePatch.vertices.length);
/* 218 */     this.idx = ninePatch.idx;
/* 219 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   private void load(TextureRegion[] patches) {
/* 223 */     float color = Color.WHITE.toFloatBits();
/*     */     
/* 225 */     if (patches[6] != null) {
/* 226 */       this.bottomLeft = add(patches[6], color, false, false);
/* 227 */       this.leftWidth = patches[6].getRegionWidth();
/* 228 */       this.bottomHeight = patches[6].getRegionHeight();
/*     */     } 
/* 230 */     if (patches[7] != null) {
/* 231 */       this.bottomCenter = add(patches[7], color, true, false);
/* 232 */       this.middleWidth = Math.max(this.middleWidth, patches[7].getRegionWidth());
/* 233 */       this.bottomHeight = Math.max(this.bottomHeight, patches[7].getRegionHeight());
/*     */     } 
/* 235 */     if (patches[8] != null) {
/* 236 */       this.bottomRight = add(patches[8], color, false, false);
/* 237 */       this.rightWidth = Math.max(this.rightWidth, patches[8].getRegionWidth());
/* 238 */       this.bottomHeight = Math.max(this.bottomHeight, patches[8].getRegionHeight());
/*     */     } 
/* 240 */     if (patches[3] != null) {
/* 241 */       this.middleLeft = add(patches[3], color, false, true);
/* 242 */       this.leftWidth = Math.max(this.leftWidth, patches[3].getRegionWidth());
/* 243 */       this.middleHeight = Math.max(this.middleHeight, patches[3].getRegionHeight());
/*     */     } 
/* 245 */     if (patches[4] != null) {
/* 246 */       this.middleCenter = add(patches[4], color, true, true);
/* 247 */       this.middleWidth = Math.max(this.middleWidth, patches[4].getRegionWidth());
/* 248 */       this.middleHeight = Math.max(this.middleHeight, patches[4].getRegionHeight());
/*     */     } 
/* 250 */     if (patches[5] != null) {
/* 251 */       this.middleRight = add(patches[5], color, false, true);
/* 252 */       this.rightWidth = Math.max(this.rightWidth, patches[5].getRegionWidth());
/* 253 */       this.middleHeight = Math.max(this.middleHeight, patches[5].getRegionHeight());
/*     */     } 
/* 255 */     if (patches[0] != null) {
/* 256 */       this.topLeft = add(patches[0], color, false, false);
/* 257 */       this.leftWidth = Math.max(this.leftWidth, patches[0].getRegionWidth());
/* 258 */       this.topHeight = Math.max(this.topHeight, patches[0].getRegionHeight());
/*     */     } 
/* 260 */     if (patches[1] != null) {
/* 261 */       this.topCenter = add(patches[1], color, true, false);
/* 262 */       this.middleWidth = Math.max(this.middleWidth, patches[1].getRegionWidth());
/* 263 */       this.topHeight = Math.max(this.topHeight, patches[1].getRegionHeight());
/*     */     } 
/* 265 */     if (patches[2] != null) {
/* 266 */       this.topRight = add(patches[2], color, false, false);
/* 267 */       this.rightWidth = Math.max(this.rightWidth, patches[2].getRegionWidth());
/* 268 */       this.topHeight = Math.max(this.topHeight, patches[2].getRegionHeight());
/*     */     } 
/* 270 */     if (this.idx < this.vertices.length) {
/* 271 */       float[] newVertices = new float[this.idx];
/* 272 */       System.arraycopy(this.vertices, 0, newVertices, 0, this.idx);
/* 273 */       this.vertices = newVertices;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int add(TextureRegion region, float color, boolean isStretchW, boolean isStretchH) {
/* 278 */     if (this.texture == null) {
/* 279 */       this.texture = region.getTexture();
/* 280 */     } else if (this.texture != region.getTexture()) {
/* 281 */       throw new IllegalArgumentException("All regions must be from the same texture.");
/*     */     } 
/* 283 */     float u = region.u;
/* 284 */     float v = region.v2;
/* 285 */     float u2 = region.u2;
/* 286 */     float v2 = region.v;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     if (isStretchW) {
/* 292 */       float halfTexelWidth = 0.5F / this.texture.getWidth();
/* 293 */       u += halfTexelWidth;
/* 294 */       u2 -= halfTexelWidth;
/*     */     } 
/* 296 */     if (isStretchH) {
/* 297 */       float halfTexelHeight = 0.5F / this.texture.getHeight();
/* 298 */       v -= halfTexelHeight;
/* 299 */       v2 += halfTexelHeight;
/*     */     } 
/*     */     
/* 302 */     float[] vertices = this.vertices;
/*     */     
/* 304 */     this.idx += 2;
/* 305 */     vertices[this.idx++] = color;
/* 306 */     vertices[this.idx++] = u;
/* 307 */     vertices[this.idx] = v;
/* 308 */     this.idx += 3;
/* 309 */     vertices[this.idx++] = color;
/* 310 */     vertices[this.idx++] = u;
/* 311 */     vertices[this.idx] = v2;
/* 312 */     this.idx += 3;
/* 313 */     vertices[this.idx++] = color;
/* 314 */     vertices[this.idx++] = u2;
/* 315 */     vertices[this.idx] = v2;
/* 316 */     this.idx += 3;
/* 317 */     vertices[this.idx++] = color;
/* 318 */     vertices[this.idx++] = u2;
/* 319 */     vertices[this.idx++] = v;
/*     */     
/* 321 */     return this.idx - 20;
/*     */   }
/*     */ 
/*     */   
/*     */   private void set(int idx, float x, float y, float width, float height, float color) {
/* 326 */     float fx2 = x + width;
/* 327 */     float fy2 = y + height;
/* 328 */     float[] vertices = this.vertices;
/* 329 */     vertices[idx++] = x;
/* 330 */     vertices[idx++] = y;
/* 331 */     vertices[idx] = color;
/* 332 */     idx += 3;
/* 333 */     vertices[idx++] = x;
/* 334 */     vertices[idx++] = fy2;
/* 335 */     vertices[idx] = color;
/* 336 */     idx += 3;
/* 337 */     vertices[idx++] = fx2;
/* 338 */     vertices[idx++] = fy2;
/* 339 */     vertices[idx] = color;
/* 340 */     idx += 3;
/* 341 */     vertices[idx++] = fx2;
/* 342 */     vertices[idx++] = y;
/* 343 */     vertices[idx] = color;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float x, float y, float width, float height) {
/* 347 */     float centerColumnX = x + this.leftWidth;
/* 348 */     float rightColumnX = x + width - this.rightWidth;
/* 349 */     float middleRowY = y + this.bottomHeight;
/* 350 */     float topRowY = y + height - this.topHeight;
/* 351 */     float c = tmpDrawColor.set(this.color).mul(batch.getColor()).toFloatBits();
/*     */     
/* 353 */     if (this.bottomLeft != -1) set(this.bottomLeft, x, y, centerColumnX - x, middleRowY - y, c); 
/* 354 */     if (this.bottomCenter != -1) set(this.bottomCenter, centerColumnX, y, rightColumnX - centerColumnX, middleRowY - y, c); 
/* 355 */     if (this.bottomRight != -1) set(this.bottomRight, rightColumnX, y, x + width - rightColumnX, middleRowY - y, c); 
/* 356 */     if (this.middleLeft != -1) set(this.middleLeft, x, middleRowY, centerColumnX - x, topRowY - middleRowY, c); 
/* 357 */     if (this.middleCenter != -1)
/* 358 */       set(this.middleCenter, centerColumnX, middleRowY, rightColumnX - centerColumnX, topRowY - middleRowY, c); 
/* 359 */     if (this.middleRight != -1) set(this.middleRight, rightColumnX, middleRowY, x + width - rightColumnX, topRowY - middleRowY, c); 
/* 360 */     if (this.topLeft != -1) set(this.topLeft, x, topRowY, centerColumnX - x, y + height - topRowY, c); 
/* 361 */     if (this.topCenter != -1) set(this.topCenter, centerColumnX, topRowY, rightColumnX - centerColumnX, y + height - topRowY, c); 
/* 362 */     if (this.topRight != -1) set(this.topRight, rightColumnX, topRowY, x + width - rightColumnX, y + height - topRowY, c);
/*     */     
/* 364 */     batch.draw(this.texture, this.vertices, 0, this.idx);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 370 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 374 */     return this.color;
/*     */   }
/*     */   
/*     */   public float getLeftWidth() {
/* 378 */     return this.leftWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLeftWidth(float leftWidth) {
/* 383 */     this.leftWidth = leftWidth;
/*     */   }
/*     */   
/*     */   public float getRightWidth() {
/* 387 */     return this.rightWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRightWidth(float rightWidth) {
/* 392 */     this.rightWidth = rightWidth;
/*     */   }
/*     */   
/*     */   public float getTopHeight() {
/* 396 */     return this.topHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTopHeight(float topHeight) {
/* 401 */     this.topHeight = topHeight;
/*     */   }
/*     */   
/*     */   public float getBottomHeight() {
/* 405 */     return this.bottomHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBottomHeight(float bottomHeight) {
/* 410 */     this.bottomHeight = bottomHeight;
/*     */   }
/*     */   
/*     */   public float getMiddleWidth() {
/* 414 */     return this.middleWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMiddleWidth(float middleWidth) {
/* 421 */     this.middleWidth = middleWidth;
/*     */   }
/*     */   
/*     */   public float getMiddleHeight() {
/* 425 */     return this.middleHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMiddleHeight(float middleHeight) {
/* 432 */     this.middleHeight = middleHeight;
/*     */   }
/*     */   
/*     */   public float getTotalWidth() {
/* 436 */     return this.leftWidth + this.middleWidth + this.rightWidth;
/*     */   }
/*     */   
/*     */   public float getTotalHeight() {
/* 440 */     return this.topHeight + this.middleHeight + this.bottomHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPadding(float left, float right, float top, float bottom) {
/* 446 */     this.padLeft = left;
/* 447 */     this.padRight = right;
/* 448 */     this.padTop = top;
/* 449 */     this.padBottom = bottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadLeft() {
/* 454 */     if (this.padLeft == -1.0F) return getLeftWidth(); 
/* 455 */     return this.padLeft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadLeft(float left) {
/* 460 */     this.padLeft = left;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadRight() {
/* 465 */     if (this.padRight == -1.0F) return getRightWidth(); 
/* 466 */     return this.padRight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadRight(float right) {
/* 471 */     this.padRight = right;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadTop() {
/* 476 */     if (this.padTop == -1.0F) return getTopHeight(); 
/* 477 */     return this.padTop;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadTop(float top) {
/* 482 */     this.padTop = top;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadBottom() {
/* 487 */     if (this.padBottom == -1.0F) return getBottomHeight(); 
/* 488 */     return this.padBottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadBottom(float bottom) {
/* 493 */     this.padBottom = bottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float scaleX, float scaleY) {
/* 498 */     this.leftWidth *= scaleX;
/* 499 */     this.rightWidth *= scaleX;
/* 500 */     this.topHeight *= scaleY;
/* 501 */     this.bottomHeight *= scaleY;
/* 502 */     this.middleWidth *= scaleX;
/* 503 */     this.middleHeight *= scaleY;
/* 504 */     if (this.padLeft != -1.0F) this.padLeft *= scaleX; 
/* 505 */     if (this.padRight != -1.0F) this.padRight *= scaleX; 
/* 506 */     if (this.padTop != -1.0F) this.padTop *= scaleY; 
/* 507 */     if (this.padBottom != -1.0F) this.padBottom *= scaleY; 
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 511 */     return this.texture;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g2d\NinePatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */