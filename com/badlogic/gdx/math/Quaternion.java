/*     */ package com.badlogic.gdx.math;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Quaternion
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7661875440774897168L;
/*  30 */   private static Quaternion tmp1 = new Quaternion(0.0F, 0.0F, 0.0F, 0.0F);
/*  31 */   private static Quaternion tmp2 = new Quaternion(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*     */   
/*     */   public float x;
/*     */   
/*     */   public float y;
/*     */   
/*     */   public float z;
/*     */   
/*     */   public float w;
/*     */ 
/*     */   
/*     */   public Quaternion(float x, float y, float z, float w) {
/*  44 */     set(x, y, z, w);
/*     */   }
/*     */   
/*     */   public Quaternion() {
/*  48 */     idt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion(Quaternion quaternion) {
/*  55 */     set(quaternion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion(Vector3 axis, float angle) {
/*  63 */     set(axis, angle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion set(float x, float y, float z, float w) {
/*  73 */     this.x = x;
/*  74 */     this.y = y;
/*  75 */     this.z = z;
/*  76 */     this.w = w;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion set(Quaternion quaternion) {
/*  84 */     return set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion set(Vector3 axis, float angle) {
/*  93 */     return setFromAxis(axis.x, axis.y, axis.z, angle);
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion cpy() {
/*  98 */     return new Quaternion(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float len(float x, float y, float z, float w) {
/* 103 */     return (float)Math.sqrt((x * x + y * y + z * z + w * w));
/*     */   }
/*     */ 
/*     */   
/*     */   public float len() {
/* 108 */     return (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return "[" + this.x + "|" + this.y + "|" + this.z + "|" + this.w + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setEulerAngles(float yaw, float pitch, float roll) {
/* 122 */     return setEulerAnglesRad(yaw * 0.017453292F, pitch * 0.017453292F, roll * 0.017453292F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setEulerAnglesRad(float yaw, float pitch, float roll) {
/* 132 */     float hr = roll * 0.5F;
/* 133 */     float shr = (float)Math.sin(hr);
/* 134 */     float chr = (float)Math.cos(hr);
/* 135 */     float hp = pitch * 0.5F;
/* 136 */     float shp = (float)Math.sin(hp);
/* 137 */     float chp = (float)Math.cos(hp);
/* 138 */     float hy = yaw * 0.5F;
/* 139 */     float shy = (float)Math.sin(hy);
/* 140 */     float chy = (float)Math.cos(hy);
/* 141 */     float chy_shp = chy * shp;
/* 142 */     float shy_chp = shy * chp;
/* 143 */     float chy_chp = chy * chp;
/* 144 */     float shy_shp = shy * shp;
/*     */     
/* 146 */     this.x = chy_shp * chr + shy_chp * shr;
/* 147 */     this.y = shy_chp * chr - chy_shp * shr;
/* 148 */     this.z = chy_chp * shr - shy_shp * chr;
/* 149 */     this.w = chy_chp * chr + shy_shp * shr;
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGimbalPole() {
/* 156 */     float t = this.y * this.x + this.z * this.w;
/* 157 */     return (t > 0.499F) ? 1 : ((t < -0.499F) ? -1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRollRad() {
/* 163 */     int pole = getGimbalPole();
/* 164 */     return (pole == 0) ? MathUtils.atan2(2.0F * (this.w * this.z + this.y * this.x), 1.0F - 2.0F * (this.x * this.x + this.z * this.z)) : (pole * 2.0F * MathUtils.atan2(this.y, this.w));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRoll() {
/* 170 */     return getRollRad() * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitchRad() {
/* 176 */     int pole = getGimbalPole();
/* 177 */     return (pole == 0) ? (float)Math.asin(MathUtils.clamp(2.0F * (this.w * this.x - this.z * this.y), -1.0F, 1.0F)) : (pole * 3.1415927F * 0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 183 */     return getPitchRad() * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYawRad() {
/* 189 */     return (getGimbalPole() == 0) ? MathUtils.atan2(2.0F * (this.y * this.w + this.x * this.z), 1.0F - 2.0F * (this.y * this.y + this.x * this.x)) : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 195 */     return getYawRad() * 57.295776F;
/*     */   }
/*     */   
/*     */   public static final float len2(float x, float y, float z, float w) {
/* 199 */     return x * x + y * y + z * z + w * w;
/*     */   }
/*     */ 
/*     */   
/*     */   public float len2() {
/* 204 */     return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion nor() {
/* 210 */     float len = len2();
/* 211 */     if (len != 0.0F && !MathUtils.isEqual(len, 1.0F)) {
/* 212 */       len = (float)Math.sqrt(len);
/* 213 */       this.w /= len;
/* 214 */       this.x /= len;
/* 215 */       this.y /= len;
/* 216 */       this.z /= len;
/*     */     } 
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion conjugate() {
/* 225 */     this.x = -this.x;
/* 226 */     this.y = -this.y;
/* 227 */     this.z = -this.z;
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 transform(Vector3 v) {
/* 236 */     tmp2.set(this);
/* 237 */     tmp2.conjugate();
/* 238 */     tmp2.mulLeft(tmp1.set(v.x, v.y, v.z, 0.0F)).mulLeft(this);
/*     */     
/* 240 */     v.x = tmp2.x;
/* 241 */     v.y = tmp2.y;
/* 242 */     v.z = tmp2.z;
/* 243 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mul(Quaternion other) {
/* 251 */     float newX = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
/* 252 */     float newY = this.w * other.y + this.y * other.w + this.z * other.x - this.x * other.z;
/* 253 */     float newZ = this.w * other.z + this.z * other.w + this.x * other.y - this.y * other.x;
/* 254 */     float newW = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
/* 255 */     this.x = newX;
/* 256 */     this.y = newY;
/* 257 */     this.z = newZ;
/* 258 */     this.w = newW;
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mul(float x, float y, float z, float w) {
/* 270 */     float newX = this.w * x + this.x * w + this.y * z - this.z * y;
/* 271 */     float newY = this.w * y + this.y * w + this.z * x - this.x * z;
/* 272 */     float newZ = this.w * z + this.z * w + this.x * y - this.y * x;
/* 273 */     float newW = this.w * w - this.x * x - this.y * y - this.z * z;
/* 274 */     this.x = newX;
/* 275 */     this.y = newY;
/* 276 */     this.z = newZ;
/* 277 */     this.w = newW;
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mulLeft(Quaternion other) {
/* 286 */     float newX = other.w * this.x + other.x * this.w + other.y * this.z - other.z * this.y;
/* 287 */     float newY = other.w * this.y + other.y * this.w + other.z * this.x - other.x * this.z;
/* 288 */     float newZ = other.w * this.z + other.z * this.w + other.x * this.y - other.y * this.x;
/* 289 */     float newW = other.w * this.w - other.x * this.x - other.y * this.y - other.z * this.z;
/* 290 */     this.x = newX;
/* 291 */     this.y = newY;
/* 292 */     this.z = newZ;
/* 293 */     this.w = newW;
/* 294 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mulLeft(float x, float y, float z, float w) {
/* 305 */     float newX = w * this.x + x * this.w + y * this.z - z * this.y;
/* 306 */     float newY = w * this.y + y * this.w + z * this.x - x * this.z;
/* 307 */     float newZ = w * this.z + z * this.w + x * this.y - y * this.x;
/* 308 */     float newW = w * this.w - x * this.x - y * this.y - z * this.z;
/* 309 */     this.x = newX;
/* 310 */     this.y = newY;
/* 311 */     this.z = newZ;
/* 312 */     this.w = newW;
/* 313 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion add(Quaternion quaternion) {
/* 318 */     this.x += quaternion.x;
/* 319 */     this.y += quaternion.y;
/* 320 */     this.z += quaternion.z;
/* 321 */     this.w += quaternion.w;
/* 322 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion add(float qx, float qy, float qz, float qw) {
/* 327 */     this.x += qx;
/* 328 */     this.y += qy;
/* 329 */     this.z += qz;
/* 330 */     this.w += qw;
/* 331 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toMatrix(float[] matrix) {
/* 340 */     float xx = this.x * this.x;
/* 341 */     float xy = this.x * this.y;
/* 342 */     float xz = this.x * this.z;
/* 343 */     float xw = this.x * this.w;
/* 344 */     float yy = this.y * this.y;
/* 345 */     float yz = this.y * this.z;
/* 346 */     float yw = this.y * this.w;
/* 347 */     float zz = this.z * this.z;
/* 348 */     float zw = this.z * this.w;
/*     */     
/* 350 */     matrix[0] = 1.0F - 2.0F * (yy + zz);
/* 351 */     matrix[4] = 2.0F * (xy - zw);
/* 352 */     matrix[8] = 2.0F * (xz + yw);
/* 353 */     matrix[12] = 0.0F;
/* 354 */     matrix[1] = 2.0F * (xy + zw);
/* 355 */     matrix[5] = 1.0F - 2.0F * (xx + zz);
/* 356 */     matrix[9] = 2.0F * (yz - xw);
/* 357 */     matrix[13] = 0.0F;
/* 358 */     matrix[2] = 2.0F * (xz - yw);
/* 359 */     matrix[6] = 2.0F * (yz + xw);
/* 360 */     matrix[10] = 1.0F - 2.0F * (xx + yy);
/* 361 */     matrix[14] = 0.0F;
/* 362 */     matrix[3] = 0.0F;
/* 363 */     matrix[7] = 0.0F;
/* 364 */     matrix[11] = 0.0F;
/* 365 */     matrix[15] = 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion idt() {
/* 371 */     return set(0.0F, 0.0F, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIdentity() {
/* 376 */     return (MathUtils.isZero(this.x) && MathUtils.isZero(this.y) && MathUtils.isZero(this.z) && MathUtils.isEqual(this.w, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIdentity(float tolerance) {
/* 381 */     return (MathUtils.isZero(this.x, tolerance) && MathUtils.isZero(this.y, tolerance) && MathUtils.isZero(this.z, tolerance) && 
/* 382 */       MathUtils.isEqual(this.w, 1.0F, tolerance));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxis(Vector3 axis, float degrees) {
/* 392 */     return setFromAxis(axis.x, axis.y, axis.z, degrees);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxisRad(Vector3 axis, float radians) {
/* 401 */     return setFromAxisRad(axis.x, axis.y, axis.z, radians);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxis(float x, float y, float z, float degrees) {
/* 411 */     return setFromAxisRad(x, y, z, degrees * 0.017453292F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxisRad(float x, float y, float z, float radians) {
/* 421 */     float d = Vector3.len(x, y, z);
/* 422 */     if (d == 0.0F) return idt(); 
/* 423 */     d = 1.0F / d;
/* 424 */     float l_ang = (radians < 0.0F) ? (6.2831855F - -radians % 6.2831855F) : (radians % 6.2831855F);
/* 425 */     float l_sin = (float)Math.sin((l_ang / 2.0F));
/* 426 */     float l_cos = (float)Math.cos((l_ang / 2.0F));
/* 427 */     return set(d * x * l_sin, d * y * l_sin, d * z * l_sin, l_cos).nor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(boolean normalizeAxes, Matrix4 matrix) {
/* 432 */     return setFromAxes(normalizeAxes, matrix.val[0], matrix.val[4], matrix.val[8], matrix.val[1], matrix.val[5], matrix.val[9], matrix.val[2], matrix.val[6], matrix.val[10]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(Matrix4 matrix) {
/* 439 */     return setFromMatrix(false, matrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(boolean normalizeAxes, Matrix3 matrix) {
/* 444 */     return setFromAxes(normalizeAxes, matrix.val[0], matrix.val[3], matrix.val[6], matrix.val[1], matrix.val[4], matrix.val[7], matrix.val[2], matrix.val[5], matrix.val[8]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(Matrix3 matrix) {
/* 451 */     return setFromMatrix(false, matrix);
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
/*     */   public Quaternion setFromAxes(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
/* 473 */     return setFromAxes(false, xx, xy, xz, yx, yy, yz, zx, zy, zz);
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
/*     */   public Quaternion setFromAxes(boolean normalizeAxes, float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
/* 497 */     if (normalizeAxes) {
/* 498 */       float lx = 1.0F / Vector3.len(xx, xy, xz);
/* 499 */       float ly = 1.0F / Vector3.len(yx, yy, yz);
/* 500 */       float lz = 1.0F / Vector3.len(zx, zy, zz);
/* 501 */       xx *= lx;
/* 502 */       xy *= lx;
/* 503 */       xz *= lx;
/* 504 */       yx *= ly;
/* 505 */       yy *= ly;
/* 506 */       yz *= ly;
/* 507 */       zx *= lz;
/* 508 */       zy *= lz;
/* 509 */       zz *= lz;
/*     */     } 
/*     */ 
/*     */     
/* 513 */     float t = xx + yy + zz;
/*     */ 
/*     */     
/* 516 */     if (t >= 0.0F) {
/* 517 */       float s = (float)Math.sqrt((t + 1.0F));
/* 518 */       this.w = 0.5F * s;
/* 519 */       s = 0.5F / s;
/* 520 */       this.x = (zy - yz) * s;
/* 521 */       this.y = (xz - zx) * s;
/* 522 */       this.z = (yx - xy) * s;
/* 523 */     } else if (xx > yy && xx > zz) {
/* 524 */       float s = (float)Math.sqrt(1.0D + xx - yy - zz);
/* 525 */       this.x = s * 0.5F;
/* 526 */       s = 0.5F / s;
/* 527 */       this.y = (yx + xy) * s;
/* 528 */       this.z = (xz + zx) * s;
/* 529 */       this.w = (zy - yz) * s;
/* 530 */     } else if (yy > zz) {
/* 531 */       float s = (float)Math.sqrt(1.0D + yy - xx - zz);
/* 532 */       this.y = s * 0.5F;
/* 533 */       s = 0.5F / s;
/* 534 */       this.x = (yx + xy) * s;
/* 535 */       this.z = (zy + yz) * s;
/* 536 */       this.w = (xz - zx) * s;
/*     */     } else {
/* 538 */       float s = (float)Math.sqrt(1.0D + zz - xx - yy);
/* 539 */       this.z = s * 0.5F;
/* 540 */       s = 0.5F / s;
/* 541 */       this.x = (xz + zx) * s;
/* 542 */       this.y = (zy + yz) * s;
/* 543 */       this.w = (yx - xy) * s;
/*     */     } 
/*     */     
/* 546 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromCross(Vector3 v1, Vector3 v2) {
/* 554 */     float dot = MathUtils.clamp(v1.dot(v2), -1.0F, 1.0F);
/* 555 */     float angle = (float)Math.acos(dot);
/* 556 */     return setFromAxisRad(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x, angle);
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
/*     */   public Quaternion setFromCross(float x1, float y1, float z1, float x2, float y2, float z2) {
/* 568 */     float dot = MathUtils.clamp(Vector3.dot(x1, y1, z1, x2, y2, z2), -1.0F, 1.0F);
/* 569 */     float angle = (float)Math.acos(dot);
/* 570 */     return setFromAxisRad(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2, angle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion slerp(Quaternion end, float alpha) {
/* 579 */     float d = this.x * end.x + this.y * end.y + this.z * end.z + this.w * end.w;
/* 580 */     float absDot = (d < 0.0F) ? -d : d;
/*     */ 
/*     */     
/* 583 */     float scale0 = 1.0F - alpha;
/* 584 */     float scale1 = alpha;
/*     */ 
/*     */ 
/*     */     
/* 588 */     if ((1.0F - absDot) > 0.1D) {
/*     */       
/* 590 */       float angle = (float)Math.acos(absDot);
/* 591 */       float invSinTheta = 1.0F / (float)Math.sin(angle);
/*     */ 
/*     */ 
/*     */       
/* 595 */       scale0 = (float)Math.sin(((1.0F - alpha) * angle)) * invSinTheta;
/* 596 */       scale1 = (float)Math.sin((alpha * angle)) * invSinTheta;
/*     */     } 
/*     */     
/* 599 */     if (d < 0.0F) scale1 = -scale1;
/*     */ 
/*     */ 
/*     */     
/* 603 */     this.x = scale0 * this.x + scale1 * end.x;
/* 604 */     this.y = scale0 * this.y + scale1 * end.y;
/* 605 */     this.z = scale0 * this.z + scale1 * end.z;
/* 606 */     this.w = scale0 * this.w + scale1 * end.w;
/*     */ 
/*     */     
/* 609 */     return this;
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
/*     */   public Quaternion slerp(Quaternion[] q) {
/* 621 */     float w = 1.0F / q.length;
/* 622 */     set(q[0]).exp(w);
/* 623 */     for (int i = 1; i < q.length; i++)
/* 624 */       mul(tmp1.set(q[i]).exp(w)); 
/* 625 */     nor();
/* 626 */     return this;
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
/*     */   public Quaternion slerp(Quaternion[] q, float[] w) {
/* 640 */     set(q[0]).exp(w[0]);
/* 641 */     for (int i = 1; i < q.length; i++)
/* 642 */       mul(tmp1.set(q[i]).exp(w[i])); 
/* 643 */     nor();
/* 644 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion exp(float alpha) {
/* 655 */     float norm = len();
/* 656 */     float normExp = (float)Math.pow(norm, alpha);
/*     */ 
/*     */     
/* 659 */     float theta = (float)Math.acos((this.w / norm));
/*     */ 
/*     */     
/* 662 */     float coeff = 0.0F;
/* 663 */     if (Math.abs(theta) < 0.001D) {
/* 664 */       coeff = normExp * alpha / norm;
/*     */     } else {
/* 666 */       coeff = (float)(normExp * Math.sin((alpha * theta)) / norm * Math.sin(theta));
/*     */     } 
/*     */     
/* 669 */     this.w = (float)(normExp * Math.cos((alpha * theta)));
/* 670 */     this.x *= coeff;
/* 671 */     this.y *= coeff;
/* 672 */     this.z *= coeff;
/*     */ 
/*     */     
/* 675 */     nor();
/*     */     
/* 677 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 682 */     int prime = 31;
/* 683 */     int result = 1;
/* 684 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.w);
/* 685 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.x);
/* 686 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.y);
/* 687 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.z);
/* 688 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 693 */     if (this == obj) {
/* 694 */       return true;
/*     */     }
/* 696 */     if (obj == null) {
/* 697 */       return false;
/*     */     }
/* 699 */     if (!(obj instanceof Quaternion)) {
/* 700 */       return false;
/*     */     }
/* 702 */     Quaternion other = (Quaternion)obj;
/* 703 */     return (NumberUtils.floatToRawIntBits(this.w) == NumberUtils.floatToRawIntBits(other.w) && 
/* 704 */       NumberUtils.floatToRawIntBits(this.x) == NumberUtils.floatToRawIntBits(other.x) && 
/* 705 */       NumberUtils.floatToRawIntBits(this.y) == NumberUtils.floatToRawIntBits(other.y) && 
/* 706 */       NumberUtils.floatToRawIntBits(this.z) == NumberUtils.floatToRawIntBits(other.z));
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
/*     */   public static final float dot(float x1, float y1, float z1, float w1, float x2, float y2, float z2, float w2) {
/* 721 */     return x1 * x2 + y1 * y2 + z1 * z2 + w1 * w2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dot(Quaternion other) {
/* 728 */     return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dot(float x, float y, float z, float w) {
/* 738 */     return this.x * x + this.y * y + this.z * z + this.w * w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mul(float scalar) {
/* 745 */     this.x *= scalar;
/* 746 */     this.y *= scalar;
/* 747 */     this.z *= scalar;
/* 748 */     this.w *= scalar;
/* 749 */     return this;
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
/*     */   public float getAxisAngle(Vector3 axis) {
/* 763 */     return getAxisAngleRad(axis) * 57.295776F;
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
/*     */   public float getAxisAngleRad(Vector3 axis) {
/* 777 */     if (this.w > 1.0F) nor(); 
/* 778 */     float angle = (float)(2.0D * Math.acos(this.w));
/* 779 */     double s = Math.sqrt((1.0F - this.w * this.w));
/* 780 */     if (s < 9.999999974752427E-7D) {
/*     */       
/* 782 */       axis.x = this.x;
/* 783 */       axis.y = this.y;
/* 784 */       axis.z = this.z;
/*     */     } else {
/* 786 */       axis.x = (float)(this.x / s);
/* 787 */       axis.y = (float)(this.y / s);
/* 788 */       axis.z = (float)(this.z / s);
/*     */     } 
/*     */     
/* 791 */     return angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleRad() {
/* 799 */     return (float)(2.0D * Math.acos((this.w > 1.0F) ? (this.w / len()) : this.w));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngle() {
/* 806 */     return getAngleRad() * 57.295776F;
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
/*     */   public void getSwingTwist(float axisX, float axisY, float axisZ, Quaternion swing, Quaternion twist) {
/* 823 */     float d = Vector3.dot(this.x, this.y, this.z, axisX, axisY, axisZ);
/* 824 */     twist.set(axisX * d, axisY * d, axisZ * d, this.w).nor();
/* 825 */     swing.set(twist).conjugate().mulLeft(this);
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
/*     */   public void getSwingTwist(Vector3 axis, Quaternion swing, Quaternion twist) {
/* 839 */     getSwingTwist(axis.x, axis.y, axis.z, swing, twist);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAroundRad(float axisX, float axisY, float axisZ) {
/* 848 */     float d = Vector3.dot(this.x, this.y, this.z, axisX, axisY, axisZ);
/* 849 */     float l2 = len2(axisX * d, axisY * d, axisZ * d, this.w);
/* 850 */     return MathUtils.isZero(l2) ? 0.0F : (float)(2.0D * Math.acos(MathUtils.clamp((float)(this.w / Math.sqrt(l2)), -1.0F, 1.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAroundRad(Vector3 axis) {
/* 857 */     return getAngleAroundRad(axis.x, axis.y, axis.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAround(float axisX, float axisY, float axisZ) {
/* 866 */     return getAngleAroundRad(axisX, axisY, axisZ) * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAround(Vector3 axis) {
/* 873 */     return getAngleAround(axis.x, axis.y, axis.z);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\math\Quaternion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */