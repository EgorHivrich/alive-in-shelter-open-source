/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GeometryUtils
/*     */ {
/*  21 */   private static final Vector2 tmp1 = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector2 toBarycoord(Vector2 p, Vector2 a, Vector2 b, Vector2 c, Vector2 barycentricOut) {
/*  37 */     Vector2 v0 = tmp1.set(b).sub(a);
/*  38 */     Vector2 v1 = tmp2.set(c).sub(a);
/*  39 */     Vector2 v2 = tmp3.set(p).sub(a);
/*  40 */     float d00 = v0.dot(v0);
/*  41 */     float d01 = v0.dot(v1);
/*  42 */     float d11 = v1.dot(v1);
/*  43 */     float d20 = v2.dot(v0);
/*  44 */     float d21 = v2.dot(v1);
/*  45 */     float denom = d00 * d11 - d01 * d01;
/*  46 */     barycentricOut.x = (d11 * d20 - d01 * d21) / denom;
/*  47 */     barycentricOut.y = (d00 * d21 - d01 * d20) / denom;
/*  48 */     return barycentricOut;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean barycoordInsideTriangle(Vector2 barycentric) {
/*  53 */     return (barycentric.x >= 0.0F && barycentric.y >= 0.0F && barycentric.x + barycentric.y <= 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector2 fromBarycoord(Vector2 barycentric, Vector2 a, Vector2 b, Vector2 c, Vector2 interpolatedOut) {
/*  59 */     float u = 1.0F - barycentric.x - barycentric.y;
/*  60 */     interpolatedOut.x = u * a.x + barycentric.x * b.x + barycentric.y * c.x;
/*  61 */     interpolatedOut.y = u * a.y + barycentric.x * b.y + barycentric.y * c.y;
/*  62 */     return interpolatedOut;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float fromBarycoord(Vector2 barycentric, float a, float b, float c) {
/*  68 */     float u = 1.0F - barycentric.x - barycentric.y;
/*  69 */     return u * a + barycentric.x * b + barycentric.y * c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float lowestPositiveRoot(float a, float b, float c) {
/*  79 */     float det = b * b - 4.0F * a * c;
/*  80 */     if (det < 0.0F) return Float.NaN;
/*     */     
/*  82 */     float sqrtD = (float)Math.sqrt(det);
/*  83 */     float invA = 1.0F / 2.0F * a;
/*  84 */     float r1 = (-b - sqrtD) * invA;
/*  85 */     float r2 = (-b + sqrtD) * invA;
/*     */     
/*  87 */     if (r1 > r2) {
/*  88 */       float tmp = r2;
/*  89 */       r2 = r1;
/*  90 */       r1 = tmp;
/*     */     } 
/*     */     
/*  93 */     if (r1 > 0.0F) return r1; 
/*  94 */     if (r2 > 0.0F) return r2; 
/*  95 */     return Float.NaN;
/*     */   }
/*     */   
/*     */   public static boolean colinear(float x1, float y1, float x2, float y2, float x3, float y3) {
/*  99 */     float dx21 = x2 - x1, dy21 = y2 - y1;
/* 100 */     float dx32 = x3 - x2, dy32 = y3 - y2;
/* 101 */     float det = dx32 * dy21 - dx21 * dy32;
/* 102 */     return (Math.abs(det) < 1.0E-6F);
/*     */   }
/*     */   
/*     */   public static Vector2 triangleCentroid(float x1, float y1, float x2, float y2, float x3, float y3, Vector2 centroid) {
/* 106 */     centroid.x = (x1 + x2 + x3) / 3.0F;
/* 107 */     centroid.y = (y1 + y2 + y3) / 3.0F;
/* 108 */     return centroid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector2 triangleCircumcenter(float x1, float y1, float x2, float y2, float x3, float y3, Vector2 circumcenter) {
/* 113 */     float dx21 = x2 - x1, dy21 = y2 - y1;
/* 114 */     float dx32 = x3 - x2, dy32 = y3 - y2;
/* 115 */     float dx13 = x1 - x3, dy13 = y1 - y3;
/* 116 */     float det = dx32 * dy21 - dx21 * dy32;
/* 117 */     if (Math.abs(det) < 1.0E-6F)
/* 118 */       throw new IllegalArgumentException("Triangle points must not be colinear."); 
/* 119 */     det *= 2.0F;
/* 120 */     float sqr1 = x1 * x1 + y1 * y1, sqr2 = x2 * x2 + y2 * y2, sqr3 = x3 * x3 + y3 * y3;
/* 121 */     circumcenter.set((sqr1 * dy32 + sqr2 * dy13 + sqr3 * dy21) / det, -(sqr1 * dx32 + sqr2 * dx13 + sqr3 * dx21) / det);
/* 122 */     return circumcenter;
/*     */   }
/*     */   
/*     */   public static float triangleArea(float x1, float y1, float x2, float y2, float x3, float y3) {
/* 126 */     return Math.abs((x1 - x3) * (y2 - y1) - (x1 - x2) * (y3 - y1)) * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector2 quadrilateralCentroid(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Vector2 centroid) {
/* 131 */     float avgX1 = (x1 + x2 + x3) / 3.0F;
/* 132 */     float avgY1 = (y1 + y2 + y3) / 3.0F;
/* 133 */     float avgX2 = (x1 + x4 + x3) / 3.0F;
/* 134 */     float avgY2 = (y1 + y4 + y3) / 3.0F;
/* 135 */     centroid.x = avgX1 - (avgX1 - avgX2) / 2.0F;
/* 136 */     centroid.y = avgY1 - (avgY1 - avgY2) / 2.0F;
/* 137 */     return centroid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector2 polygonCentroid(float[] polygon, int offset, int count, Vector2 centroid) {
/* 142 */     if (count < 6) throw new IllegalArgumentException("A polygon must have 3 or more coordinate pairs."); 
/* 143 */     float x = 0.0F, y = 0.0F;
/*     */     
/* 145 */     float signedArea = 0.0F;
/* 146 */     int i = offset;
/* 147 */     for (int n = offset + count - 2; i < n; i += 2) {
/* 148 */       float f1 = polygon[i];
/* 149 */       float f2 = polygon[i + 1];
/* 150 */       float f3 = polygon[i + 2];
/* 151 */       float f4 = polygon[i + 3];
/* 152 */       float f5 = f1 * f4 - f3 * f2;
/* 153 */       signedArea += f5;
/* 154 */       x += (f1 + f3) * f5;
/* 155 */       y += (f2 + f4) * f5;
/*     */     } 
/*     */     
/* 158 */     float x0 = polygon[i];
/* 159 */     float y0 = polygon[i + 1];
/* 160 */     float x1 = polygon[offset];
/* 161 */     float y1 = polygon[offset + 1];
/* 162 */     float a = x0 * y1 - x1 * y0;
/* 163 */     signedArea += a;
/* 164 */     x += (x0 + x1) * a;
/* 165 */     y += (y0 + y1) * a;
/*     */     
/* 167 */     if (signedArea == 0.0F) {
/* 168 */       centroid.x = 0.0F;
/* 169 */       centroid.y = 0.0F;
/*     */     } else {
/* 171 */       signedArea *= 0.5F;
/* 172 */       centroid.x = x / 6.0F * signedArea;
/* 173 */       centroid.y = y / 6.0F * signedArea;
/*     */     } 
/* 175 */     return centroid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float polygonArea(float[] polygon, int offset, int count) {
/* 180 */     float area = 0.0F;
/* 181 */     for (int i = offset, n = offset + count; i < n; i += 2) {
/* 182 */       int x1 = i;
/* 183 */       int y1 = i + 1;
/* 184 */       int x2 = (i + 2) % n;
/* 185 */       if (x2 < offset)
/* 186 */         x2 += offset; 
/* 187 */       int y2 = (i + 3) % n;
/* 188 */       if (y2 < offset)
/* 189 */         y2 += offset; 
/* 190 */       area += polygon[x1] * polygon[y2];
/* 191 */       area -= polygon[x2] * polygon[y1];
/*     */     } 
/* 193 */     area *= 0.5F;
/* 194 */     return area;
/*     */   }
/*     */   
/*     */   public static void ensureCCW(float[] polygon) {
/* 198 */     if (!areVerticesClockwise(polygon, 0, polygon.length))
/* 199 */       return;  int lastX = polygon.length - 2;
/* 200 */     for (int i = 0, n = polygon.length / 2; i < n; i += 2) {
/* 201 */       int other = lastX - i;
/* 202 */       float x = polygon[i];
/* 203 */       float y = polygon[i + 1];
/* 204 */       polygon[i] = polygon[other];
/* 205 */       polygon[i + 1] = polygon[other + 1];
/* 206 */       polygon[other] = x;
/* 207 */       polygon[other + 1] = y;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areVerticesClockwise(float[] polygon, int offset, int count) {
/* 212 */     if (count <= 2) return false; 
/* 213 */     float area = 0.0F;
/* 214 */     for (int i = offset, n = offset + count - 3; i < n; i += 2) {
/* 215 */       float f1 = polygon[i];
/* 216 */       float f2 = polygon[i + 1];
/* 217 */       float f3 = polygon[i + 2];
/* 218 */       float f4 = polygon[i + 3];
/* 219 */       area += f1 * f4 - f3 * f2;
/*     */     } 
/* 221 */     float p1x = polygon[count - 2];
/* 222 */     float p1y = polygon[count - 1];
/* 223 */     float p2x = polygon[0];
/* 224 */     float p2y = polygon[1];
/* 225 */     return (area + p1x * p2y - p2x * p1y < 0.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\math\GeometryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */