/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.NumberUtils;
/*     */ import java.io.Serializable;
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
/*     */ public class Rectangle
/*     */   implements Serializable, Shape2D
/*     */ {
/*  26 */   public static final Rectangle tmp = new Rectangle();
/*     */ 
/*     */   
/*  29 */   public static final Rectangle tmp2 = new Rectangle();
/*     */   
/*     */   private static final long serialVersionUID = 5733252015138115702L;
/*     */   
/*     */   public float x;
/*     */   
/*     */   public float y;
/*     */   
/*     */   public float width;
/*     */   
/*     */   public float height;
/*     */ 
/*     */   
/*     */   public Rectangle() {}
/*     */ 
/*     */   
/*     */   public Rectangle(float x, float y, float width, float height) {
/*  46 */     this.x = x;
/*  47 */     this.y = y;
/*  48 */     this.width = width;
/*  49 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle(Rectangle rect) {
/*  55 */     this.x = rect.x;
/*  56 */     this.y = rect.y;
/*  57 */     this.width = rect.width;
/*  58 */     this.height = rect.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle set(float x, float y, float width, float height) {
/*  67 */     this.x = x;
/*  68 */     this.y = y;
/*  69 */     this.width = width;
/*  70 */     this.height = height;
/*     */     
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/*  77 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setX(float x) {
/*  84 */     this.x = x;
/*     */     
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/*  91 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setY(float y) {
/*  98 */     this.y = y;
/*     */     
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 105 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setWidth(float width) {
/* 112 */     this.width = width;
/*     */     
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 119 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setHeight(float height) {
/* 126 */     this.height = height;
/*     */     
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getPosition(Vector2 position) {
/* 134 */     return position.set(this.x, this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setPosition(Vector2 position) {
/* 141 */     this.x = position.x;
/* 142 */     this.y = position.y;
/*     */     
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setPosition(float x, float y) {
/* 152 */     this.x = x;
/* 153 */     this.y = y;
/*     */     
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setSize(float width, float height) {
/* 163 */     this.width = width;
/* 164 */     this.height = height;
/*     */     
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setSize(float sizeXY) {
/* 173 */     this.width = sizeXY;
/* 174 */     this.height = sizeXY;
/*     */     
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getSize(Vector2 size) {
/* 182 */     return size.set(this.width, this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(float x, float y) {
/* 189 */     return (this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Vector2 point) {
/* 195 */     return contains(point.x, point.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Rectangle rectangle) {
/* 201 */     float xmin = rectangle.x;
/* 202 */     float xmax = xmin + rectangle.width;
/*     */     
/* 204 */     float ymin = rectangle.y;
/* 205 */     float ymax = ymin + rectangle.height;
/*     */     
/* 207 */     return (xmin > this.x && xmin < this.x + this.width && xmax > this.x && xmax < this.x + this.width && ymin > this.y && ymin < this.y + this.height && ymax > this.y && ymax < this.y + this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overlaps(Rectangle r) {
/* 214 */     return (this.x < r.x + r.width && this.x + this.width > r.x && this.y < r.y + r.height && this.y + this.height > r.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle set(Rectangle rect) {
/* 221 */     this.x = rect.x;
/* 222 */     this.y = rect.y;
/* 223 */     this.width = rect.width;
/* 224 */     this.height = rect.height;
/*     */     
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(Rectangle rect) {
/* 233 */     float minX = Math.min(this.x, rect.x);
/* 234 */     float maxX = Math.max(this.x + this.width, rect.x + rect.width);
/* 235 */     this.x = minX;
/* 236 */     this.width = maxX - minX;
/*     */     
/* 238 */     float minY = Math.min(this.y, rect.y);
/* 239 */     float maxY = Math.max(this.y + this.height, rect.y + rect.height);
/* 240 */     this.y = minY;
/* 241 */     this.height = maxY - minY;
/*     */     
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(float x, float y) {
/* 251 */     float minX = Math.min(this.x, x);
/* 252 */     float maxX = Math.max(this.x + this.width, x);
/* 253 */     this.x = minX;
/* 254 */     this.width = maxX - minX;
/*     */     
/* 256 */     float minY = Math.min(this.y, y);
/* 257 */     float maxY = Math.max(this.y + this.height, y);
/* 258 */     this.y = minY;
/* 259 */     this.height = maxY - minY;
/*     */     
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(Vector2 vec) {
/* 268 */     return merge(vec.x, vec.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(Vector2[] vecs) {
/* 275 */     float minX = this.x;
/* 276 */     float maxX = this.x + this.width;
/* 277 */     float minY = this.y;
/* 278 */     float maxY = this.y + this.height;
/* 279 */     for (int i = 0; i < vecs.length; i++) {
/* 280 */       Vector2 v = vecs[i];
/* 281 */       minX = Math.min(minX, v.x);
/* 282 */       maxX = Math.max(maxX, v.x);
/* 283 */       minY = Math.min(minY, v.y);
/* 284 */       maxY = Math.max(maxY, v.y);
/*     */     } 
/* 286 */     this.x = minX;
/* 287 */     this.width = maxX - minX;
/* 288 */     this.y = minY;
/* 289 */     this.height = maxY - minY;
/* 290 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAspectRatio() {
/* 296 */     return (this.height == 0.0F) ? Float.NaN : (this.width / this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getCenter(Vector2 vector) {
/* 303 */     vector.x = this.x + this.width / 2.0F;
/* 304 */     vector.y = this.y + this.height / 2.0F;
/* 305 */     return vector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setCenter(float x, float y) {
/* 313 */     setPosition(x - this.width / 2.0F, y - this.height / 2.0F);
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setCenter(Vector2 position) {
/* 321 */     setPosition(position.x - this.width / 2.0F, position.y - this.height / 2.0F);
/* 322 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle fitOutside(Rectangle rect) {
/* 331 */     float ratio = getAspectRatio();
/*     */     
/* 333 */     if (ratio > rect.getAspectRatio()) {
/*     */       
/* 335 */       setSize(rect.height * ratio, rect.height);
/*     */     } else {
/*     */       
/* 338 */       setSize(rect.width, rect.width / ratio);
/*     */     } 
/*     */     
/* 341 */     setPosition(rect.x + rect.width / 2.0F - this.width / 2.0F, rect.y + rect.height / 2.0F - this.height / 2.0F);
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle fitInside(Rectangle rect) {
/* 351 */     float ratio = getAspectRatio();
/*     */     
/* 353 */     if (ratio < rect.getAspectRatio()) {
/*     */       
/* 355 */       setSize(rect.height * ratio, rect.height);
/*     */     } else {
/*     */       
/* 358 */       setSize(rect.width, rect.width / ratio);
/*     */     } 
/*     */     
/* 361 */     setPosition(rect.x + rect.width / 2.0F - this.width / 2.0F, rect.y + rect.height / 2.0F - this.height / 2.0F);
/* 362 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 368 */     return "[" + this.x + "," + this.y + "," + this.width + "," + this.height + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle fromString(String v) {
/* 376 */     int s0 = v.indexOf(',', 1);
/* 377 */     int s1 = v.indexOf(',', s0 + 1);
/* 378 */     int s2 = v.indexOf(',', s1 + 1);
/* 379 */     if (s0 != -1 && s1 != -1 && s2 != -1 && v.charAt(0) == '[' && v.charAt(v.length() - 1) == ']') {
/*     */       try {
/* 381 */         float x = Float.parseFloat(v.substring(1, s0));
/* 382 */         float y = Float.parseFloat(v.substring(s0 + 1, s1));
/* 383 */         float width = Float.parseFloat(v.substring(s1 + 1, s2));
/* 384 */         float height = Float.parseFloat(v.substring(s2 + 1, v.length() - 1));
/* 385 */         return set(x, y, width, height);
/* 386 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */     
/* 390 */     throw new GdxRuntimeException("Malformed Rectangle: " + v);
/*     */   }
/*     */   
/*     */   public float area() {
/* 394 */     return this.width * this.height;
/*     */   }
/*     */   
/*     */   public float perimeter() {
/* 398 */     return 2.0F * (this.width + this.height);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 402 */     int prime = 31;
/* 403 */     int result = 1;
/* 404 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.height);
/* 405 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.width);
/* 406 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.x);
/* 407 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.y);
/* 408 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 412 */     if (this == obj) return true; 
/* 413 */     if (obj == null) return false; 
/* 414 */     if (getClass() != obj.getClass()) return false; 
/* 415 */     Rectangle other = (Rectangle)obj;
/* 416 */     if (NumberUtils.floatToRawIntBits(this.height) != NumberUtils.floatToRawIntBits(other.height)) return false; 
/* 417 */     if (NumberUtils.floatToRawIntBits(this.width) != NumberUtils.floatToRawIntBits(other.width)) return false; 
/* 418 */     if (NumberUtils.floatToRawIntBits(this.x) != NumberUtils.floatToRawIntBits(other.x)) return false; 
/* 419 */     if (NumberUtils.floatToRawIntBits(this.y) != NumberUtils.floatToRawIntBits(other.y)) return false; 
/* 420 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\math\Rectangle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */