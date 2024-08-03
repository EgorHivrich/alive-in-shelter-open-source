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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Interpolation
/*     */ {
/*     */   public abstract float apply(float paramFloat);
/*     */   
/*     */   public float apply(float start, float end, float a) {
/*  27 */     return start + (end - start) * apply(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static final Interpolation linear = new Interpolation() {
/*     */       public float apply(float a) {
/*  34 */         return a;
/*     */       }
/*     */     };
/*     */   
/*  38 */   public static final Interpolation fade = new Interpolation() {
/*     */       public float apply(float a) {
/*  40 */         return MathUtils.clamp(a * a * a * (a * (a * 6.0F - 15.0F) + 10.0F), 0.0F, 1.0F);
/*     */       }
/*     */     };
/*     */   
/*  44 */   public static final Pow pow2 = new Pow(2);
/*     */   
/*  46 */   public static final PowIn pow2In = new PowIn(2);
/*     */   
/*  48 */   public static final PowOut pow2Out = new PowOut(2);
/*     */   
/*  50 */   public static final Pow pow3 = new Pow(3);
/*  51 */   public static final PowIn pow3In = new PowIn(3);
/*  52 */   public static final PowOut pow3Out = new PowOut(3);
/*     */   
/*  54 */   public static final Pow pow4 = new Pow(4);
/*  55 */   public static final PowIn pow4In = new PowIn(4);
/*  56 */   public static final PowOut pow4Out = new PowOut(4);
/*     */   
/*  58 */   public static final Pow pow5 = new Pow(5);
/*  59 */   public static final PowIn pow5In = new PowIn(5);
/*  60 */   public static final PowOut pow5Out = new PowOut(5);
/*     */   
/*  62 */   public static final Interpolation sine = new Interpolation() {
/*     */       public float apply(float a) {
/*  64 */         return (1.0F - MathUtils.cos(a * 3.1415927F)) / 2.0F;
/*     */       }
/*     */     };
/*     */   
/*  68 */   public static final Interpolation sineIn = new Interpolation() {
/*     */       public float apply(float a) {
/*  70 */         return 1.0F - MathUtils.cos(a * 3.1415927F / 2.0F);
/*     */       }
/*     */     };
/*     */   
/*  74 */   public static final Interpolation sineOut = new Interpolation() {
/*     */       public float apply(float a) {
/*  76 */         return MathUtils.sin(a * 3.1415927F / 2.0F);
/*     */       }
/*     */     };
/*     */   
/*  80 */   public static final Exp exp10 = new Exp(2.0F, 10.0F);
/*  81 */   public static final ExpIn exp10In = new ExpIn(2.0F, 10.0F);
/*  82 */   public static final ExpOut exp10Out = new ExpOut(2.0F, 10.0F);
/*     */   
/*  84 */   public static final Exp exp5 = new Exp(2.0F, 5.0F);
/*  85 */   public static final ExpIn exp5In = new ExpIn(2.0F, 5.0F);
/*  86 */   public static final ExpOut exp5Out = new ExpOut(2.0F, 5.0F);
/*     */   
/*  88 */   public static final Interpolation circle = new Interpolation() {
/*     */       public float apply(float a) {
/*  90 */         if (a <= 0.5F) {
/*  91 */           a *= 2.0F;
/*  92 */           return (1.0F - (float)Math.sqrt((1.0F - a * a))) / 2.0F;
/*     */         } 
/*  94 */         a--;
/*  95 */         a *= 2.0F;
/*  96 */         return ((float)Math.sqrt((1.0F - a * a)) + 1.0F) / 2.0F;
/*     */       }
/*     */     };
/*     */   
/* 100 */   public static final Interpolation circleIn = new Interpolation() {
/*     */       public float apply(float a) {
/* 102 */         return 1.0F - (float)Math.sqrt((1.0F - a * a));
/*     */       }
/*     */     };
/*     */   
/* 106 */   public static final Interpolation circleOut = new Interpolation() {
/*     */       public float apply(float a) {
/* 108 */         a--;
/* 109 */         return (float)Math.sqrt((1.0F - a * a));
/*     */       }
/*     */     };
/*     */   
/* 113 */   public static final Elastic elastic = new Elastic(2.0F, 10.0F, 7, 1.0F);
/* 114 */   public static final ElasticIn elasticIn = new ElasticIn(2.0F, 10.0F, 6, 1.0F);
/* 115 */   public static final ElasticOut elasticOut = new ElasticOut(2.0F, 10.0F, 7, 1.0F);
/*     */   
/* 117 */   public static final Swing swing = new Swing(1.5F);
/* 118 */   public static final SwingIn swingIn = new SwingIn(2.0F);
/* 119 */   public static final SwingOut swingOut = new SwingOut(2.0F);
/*     */   
/* 121 */   public static final Bounce bounce = new Bounce(4);
/* 122 */   public static final BounceIn bounceIn = new BounceIn(4);
/* 123 */   public static final BounceOut bounceOut = new BounceOut(4);
/*     */   
/*     */   public static class Pow
/*     */     extends Interpolation
/*     */   {
/*     */     final int power;
/*     */     
/*     */     public Pow(int power) {
/* 131 */       this.power = power;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 135 */       if (a <= 0.5F) return (float)Math.pow((a * 2.0F), this.power) / 2.0F; 
/* 136 */       return (float)Math.pow(((a - 1.0F) * 2.0F), this.power) / ((this.power % 2 == 0) ? -2 : 2) + 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PowIn extends Pow {
/*     */     public PowIn(int power) {
/* 142 */       super(power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 146 */       return (float)Math.pow(a, this.power);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PowOut extends Pow {
/*     */     public PowOut(int power) {
/* 152 */       super(power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 156 */       return (float)Math.pow((a - 1.0F), this.power) * ((this.power % 2 == 0) ? -1 : true) + 1.0F;
/*     */     } }
/*     */   
/*     */   public static class Exp extends Interpolation {
/*     */     final float value;
/*     */     final float power;
/*     */     final float min;
/*     */     final float scale;
/*     */     
/*     */     public Exp(float value, float power) {
/* 166 */       this.value = value;
/* 167 */       this.power = power;
/* 168 */       this.min = (float)Math.pow(value, -power);
/* 169 */       this.scale = 1.0F / (1.0F - this.min);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 173 */       if (a <= 0.5F) return ((float)Math.pow(this.value, (this.power * (a * 2.0F - 1.0F))) - this.min) * this.scale / 2.0F; 
/* 174 */       return (2.0F - ((float)Math.pow(this.value, (-this.power * (a * 2.0F - 1.0F))) - this.min) * this.scale) / 2.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ExpIn extends Exp {
/*     */     public ExpIn(float value, float power) {
/* 180 */       super(value, power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 184 */       return ((float)Math.pow(this.value, (this.power * (a - 1.0F))) - this.min) * this.scale;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ExpOut extends Exp {
/*     */     public ExpOut(float value, float power) {
/* 190 */       super(value, power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 194 */       return 1.0F - ((float)Math.pow(this.value, (-this.power * a)) - this.min) * this.scale;
/*     */     } }
/*     */   
/*     */   public static class Elastic extends Interpolation {
/*     */     final float value;
/*     */     final float power;
/*     */     final float scale;
/*     */     final float bounces;
/*     */     
/*     */     public Elastic(float value, float power, int bounces, float scale) {
/* 204 */       this.value = value;
/* 205 */       this.power = power;
/* 206 */       this.scale = scale;
/* 207 */       this.bounces = bounces * 3.1415927F * ((bounces % 2 == 0) ? true : -1);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 211 */       if (a <= 0.5F) {
/* 212 */         a *= 2.0F;
/* 213 */         return (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale / 2.0F;
/*     */       } 
/* 215 */       a = 1.0F - a;
/* 216 */       a *= 2.0F;
/* 217 */       return 1.0F - (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale / 2.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ElasticIn extends Elastic {
/*     */     public ElasticIn(float value, float power, int bounces, float scale) {
/* 223 */       super(value, power, bounces, scale);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 227 */       if (a >= 0.99D) return 1.0F; 
/* 228 */       return (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ElasticOut extends Elastic {
/*     */     public ElasticOut(float value, float power, int bounces, float scale) {
/* 234 */       super(value, power, bounces, scale);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 238 */       a = 1.0F - a;
/* 239 */       return 1.0F - (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Bounce
/*     */     extends BounceOut
/*     */   {
/*     */     public Bounce(float[] widths, float[] heights) {
/* 247 */       super(widths, heights);
/*     */     }
/*     */     
/*     */     public Bounce(int bounces) {
/* 251 */       super(bounces);
/*     */     }
/*     */     
/*     */     private float out(float a) {
/* 255 */       float test = a + this.widths[0] / 2.0F;
/* 256 */       if (test < this.widths[0]) return test / this.widths[0] / 2.0F - 1.0F; 
/* 257 */       return super.apply(a);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 261 */       if (a <= 0.5F) return (1.0F - out(1.0F - a * 2.0F)) / 2.0F; 
/* 262 */       return out(a * 2.0F - 1.0F) / 2.0F + 0.5F;
/*     */     } }
/*     */   
/*     */   public static class BounceOut extends Interpolation {
/*     */     final float[] widths;
/*     */     final float[] heights;
/*     */     
/*     */     public BounceOut(float[] widths, float[] heights) {
/* 270 */       if (widths.length != heights.length)
/* 271 */         throw new IllegalArgumentException("Must be the same number of widths and heights."); 
/* 272 */       this.widths = widths;
/* 273 */       this.heights = heights;
/*     */     }
/*     */     
/*     */     public BounceOut(int bounces) {
/* 277 */       if (bounces < 2 || bounces > 5) throw new IllegalArgumentException("bounces cannot be < 2 or > 5: " + bounces); 
/* 278 */       this.widths = new float[bounces];
/* 279 */       this.heights = new float[bounces];
/* 280 */       this.heights[0] = 1.0F;
/* 281 */       switch (bounces) {
/*     */         case 2:
/* 283 */           this.widths[0] = 0.6F;
/* 284 */           this.widths[1] = 0.4F;
/* 285 */           this.heights[1] = 0.33F;
/*     */           break;
/*     */         case 3:
/* 288 */           this.widths[0] = 0.4F;
/* 289 */           this.widths[1] = 0.4F;
/* 290 */           this.widths[2] = 0.2F;
/* 291 */           this.heights[1] = 0.33F;
/* 292 */           this.heights[2] = 0.1F;
/*     */           break;
/*     */         case 4:
/* 295 */           this.widths[0] = 0.34F;
/* 296 */           this.widths[1] = 0.34F;
/* 297 */           this.widths[2] = 0.2F;
/* 298 */           this.widths[3] = 0.15F;
/* 299 */           this.heights[1] = 0.26F;
/* 300 */           this.heights[2] = 0.11F;
/* 301 */           this.heights[3] = 0.03F;
/*     */           break;
/*     */         case 5:
/* 304 */           this.widths[0] = 0.3F;
/* 305 */           this.widths[1] = 0.3F;
/* 306 */           this.widths[2] = 0.2F;
/* 307 */           this.widths[3] = 0.1F;
/* 308 */           this.widths[4] = 0.1F;
/* 309 */           this.heights[1] = 0.45F;
/* 310 */           this.heights[2] = 0.3F;
/* 311 */           this.heights[3] = 0.15F;
/* 312 */           this.heights[4] = 0.06F;
/*     */           break;
/*     */       } 
/* 315 */       this.widths[0] = this.widths[0] * 2.0F;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 319 */       a += this.widths[0] / 2.0F;
/* 320 */       float width = 0.0F, height = 0.0F;
/* 321 */       for (int i = 0, n = this.widths.length; i < n; i++) {
/* 322 */         width = this.widths[i];
/* 323 */         if (a <= width) {
/* 324 */           height = this.heights[i];
/*     */           break;
/*     */         } 
/* 327 */         a -= width;
/*     */       } 
/* 329 */       a /= width;
/* 330 */       float z = 4.0F / width * height * a;
/* 331 */       return 1.0F - (z - z * a) * width;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BounceIn extends BounceOut {
/*     */     public BounceIn(float[] widths, float[] heights) {
/* 337 */       super(widths, heights);
/*     */     }
/*     */     
/*     */     public BounceIn(int bounces) {
/* 341 */       super(bounces);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 345 */       return 1.0F - super.apply(1.0F - a);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Swing
/*     */     extends Interpolation
/*     */   {
/*     */     private final float scale;
/*     */     
/*     */     public Swing(float scale) {
/* 355 */       this.scale = scale * 2.0F;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 359 */       if (a <= 0.5F) {
/* 360 */         a *= 2.0F;
/* 361 */         return a * a * ((this.scale + 1.0F) * a - this.scale) / 2.0F;
/*     */       } 
/* 363 */       a--;
/* 364 */       a *= 2.0F;
/* 365 */       return a * a * ((this.scale + 1.0F) * a + this.scale) / 2.0F + 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SwingOut extends Interpolation {
/*     */     private final float scale;
/*     */     
/*     */     public SwingOut(float scale) {
/* 373 */       this.scale = scale;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 377 */       a--;
/* 378 */       return a * a * ((this.scale + 1.0F) * a + this.scale) + 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SwingIn extends Interpolation {
/*     */     private final float scale;
/*     */     
/*     */     public SwingIn(float scale) {
/* 386 */       this.scale = scale;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 390 */       return a * a * ((this.scale + 1.0F) * a - this.scale);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\math\Interpolation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */