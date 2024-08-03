/*      */ package com.badlogic.gdx.graphics.glutils;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.math.Vector3;
/*      */ import com.badlogic.gdx.utils.Disposable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ShapeRenderer
/*      */   implements Disposable
/*      */ {
/*      */   private final ImmediateModeRenderer renderer;
/*      */   
/*      */   public enum ShapeType
/*      */   {
/*   82 */     Point(0), Line(1), Filled(4);
/*      */     
/*      */     private final int glType;
/*      */     
/*      */     ShapeType(int glType) {
/*   87 */       this.glType = glType;
/*      */     }
/*      */     
/*      */     public int getGlType() {
/*   91 */       return this.glType;
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean matrixDirty = false;
/*      */   
/*   97 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   98 */   private final Matrix4 transformMatrix = new Matrix4();
/*   99 */   private final Matrix4 combinedMatrix = new Matrix4();
/*  100 */   private final Vector2 tmp = new Vector2();
/*  101 */   private final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   private ShapeType shapeType;
/*      */   private boolean autoShapeType;
/*  104 */   private float defaultRectLineWidth = 0.75F;
/*      */   
/*      */   public ShapeRenderer() {
/*  107 */     this(5000);
/*      */   }
/*      */   
/*      */   public ShapeRenderer(int maxVertices) {
/*  111 */     this(maxVertices, null);
/*      */   }
/*      */   
/*      */   public ShapeRenderer(int maxVertices, ShaderProgram defaultShader) {
/*  115 */     if (defaultShader == null) {
/*  116 */       this.renderer = new ImmediateModeRenderer20(maxVertices, false, true, 0);
/*      */     } else {
/*  118 */       this.renderer = new ImmediateModeRenderer20(maxVertices, false, true, 0, defaultShader);
/*      */     } 
/*  120 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*  121 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(Color color) {
/*  126 */     this.color.set(color);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  131 */     this.color.set(r, g, b, a);
/*      */   }
/*      */   
/*      */   public Color getColor() {
/*  135 */     return this.color;
/*      */   }
/*      */   
/*      */   public void updateMatrices() {
/*  139 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 matrix) {
/*  145 */     this.projectionMatrix.set(matrix);
/*  146 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/*  151 */     return this.projectionMatrix;
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 matrix) {
/*  155 */     this.transformMatrix.set(matrix);
/*  156 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/*  161 */     return this.transformMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public void identity() {
/*  166 */     this.transformMatrix.idt();
/*  167 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void translate(float x, float y, float z) {
/*  172 */     this.transformMatrix.translate(x, y, z);
/*  173 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void rotate(float axisX, float axisY, float axisZ, float degrees) {
/*  178 */     this.transformMatrix.rotate(axisX, axisY, axisZ, degrees);
/*  179 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void scale(float scaleX, float scaleY, float scaleZ) {
/*  184 */     this.transformMatrix.scale(scaleX, scaleY, scaleZ);
/*  185 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoShapeType(boolean autoShapeType) {
/*  192 */     this.autoShapeType = autoShapeType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin() {
/*  198 */     if (!this.autoShapeType) throw new IllegalStateException("autoShapeType must be true to use this method."); 
/*  199 */     begin(ShapeType.Line);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin(ShapeType type) {
/*  206 */     if (this.shapeType != null) throw new IllegalStateException("Call end() before beginning a new shape batch."); 
/*  207 */     this.shapeType = type;
/*  208 */     if (this.matrixDirty) {
/*  209 */       this.combinedMatrix.set(this.projectionMatrix);
/*  210 */       Matrix4.mul(this.combinedMatrix.val, this.transformMatrix.val);
/*  211 */       this.matrixDirty = false;
/*      */     } 
/*  213 */     this.renderer.begin(this.combinedMatrix, this.shapeType.getGlType());
/*      */   }
/*      */   
/*      */   public void set(ShapeType type) {
/*  217 */     if (this.shapeType == type)
/*  218 */       return;  if (this.shapeType == null) throw new IllegalStateException("begin must be called first."); 
/*  219 */     if (!this.autoShapeType) throw new IllegalStateException("autoShapeType must be enabled."); 
/*  220 */     end();
/*  221 */     begin(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public void point(float x, float y, float z) {
/*  226 */     if (this.shapeType == ShapeType.Line) {
/*  227 */       float size = this.defaultRectLineWidth * 0.5F;
/*  228 */       line(x - size, y - size, z, x + size, y + size, z); return;
/*      */     } 
/*  230 */     if (this.shapeType == ShapeType.Filled) {
/*  231 */       float size = this.defaultRectLineWidth * 0.5F;
/*  232 */       box(x - size, y - size, z - size, this.defaultRectLineWidth, this.defaultRectLineWidth, this.defaultRectLineWidth);
/*      */       return;
/*      */     } 
/*  235 */     check(ShapeType.Point, null, 1);
/*  236 */     this.renderer.color(this.color);
/*  237 */     this.renderer.vertex(x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(float x, float y, float z, float x2, float y2, float z2) {
/*  242 */     line(x, y, z, x2, y2, z2, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(Vector3 v0, Vector3 v1) {
/*  247 */     line(v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(float x, float y, float x2, float y2) {
/*  252 */     line(x, y, 0.0F, x2, y2, 0.0F, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(Vector2 v0, Vector2 v1) {
/*  257 */     line(v0.x, v0.y, 0.0F, v1.x, v1.y, 0.0F, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(float x, float y, float x2, float y2, Color c1, Color c2) {
/*  262 */     line(x, y, 0.0F, x2, y2, 0.0F, c1, c2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void line(float x, float y, float z, float x2, float y2, float z2, Color c1, Color c2) {
/*  268 */     if (this.shapeType == ShapeType.Filled) {
/*  269 */       rectLine(x, y, x2, y2, this.defaultRectLineWidth);
/*      */       return;
/*      */     } 
/*  272 */     check(ShapeType.Line, null, 2);
/*  273 */     this.renderer.color(c1.r, c1.g, c1.b, c1.a);
/*  274 */     this.renderer.vertex(x, y, z);
/*  275 */     this.renderer.color(c2.r, c2.g, c2.b, c2.a);
/*  276 */     this.renderer.vertex(x2, y2, z2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void curve(float x1, float y1, float cx1, float cy1, float cx2, float cy2, float x2, float y2, int segments) {
/*  281 */     check(ShapeType.Line, null, segments * 2 + 2);
/*  282 */     float colorBits = this.color.toFloatBits();
/*      */ 
/*      */     
/*  285 */     float subdiv_step = 1.0F / segments;
/*  286 */     float subdiv_step2 = subdiv_step * subdiv_step;
/*  287 */     float subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;
/*      */     
/*  289 */     float pre1 = 3.0F * subdiv_step;
/*  290 */     float pre2 = 3.0F * subdiv_step2;
/*  291 */     float pre4 = 6.0F * subdiv_step2;
/*  292 */     float pre5 = 6.0F * subdiv_step3;
/*      */     
/*  294 */     float tmp1x = x1 - cx1 * 2.0F + cx2;
/*  295 */     float tmp1y = y1 - cy1 * 2.0F + cy2;
/*      */     
/*  297 */     float tmp2x = (cx1 - cx2) * 3.0F - x1 + x2;
/*  298 */     float tmp2y = (cy1 - cy2) * 3.0F - y1 + y2;
/*      */     
/*  300 */     float fx = x1;
/*  301 */     float fy = y1;
/*      */     
/*  303 */     float dfx = (cx1 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
/*  304 */     float dfy = (cy1 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;
/*      */     
/*  306 */     float ddfx = tmp1x * pre4 + tmp2x * pre5;
/*  307 */     float ddfy = tmp1y * pre4 + tmp2y * pre5;
/*      */     
/*  309 */     float dddfx = tmp2x * pre5;
/*  310 */     float dddfy = tmp2y * pre5;
/*      */     
/*  312 */     while (segments-- > 0) {
/*  313 */       this.renderer.color(colorBits);
/*  314 */       this.renderer.vertex(fx, fy, 0.0F);
/*  315 */       fx += dfx;
/*  316 */       fy += dfy;
/*  317 */       dfx += ddfx;
/*  318 */       dfy += ddfy;
/*  319 */       ddfx += dddfx;
/*  320 */       ddfy += dddfy;
/*  321 */       this.renderer.color(colorBits);
/*  322 */       this.renderer.vertex(fx, fy, 0.0F);
/*      */     } 
/*  324 */     this.renderer.color(colorBits);
/*  325 */     this.renderer.vertex(fx, fy, 0.0F);
/*  326 */     this.renderer.color(colorBits);
/*  327 */     this.renderer.vertex(x2, y2, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
/*  332 */     check(ShapeType.Line, ShapeType.Filled, 6);
/*  333 */     float colorBits = this.color.toFloatBits();
/*  334 */     if (this.shapeType == ShapeType.Line) {
/*  335 */       this.renderer.color(colorBits);
/*  336 */       this.renderer.vertex(x1, y1, 0.0F);
/*  337 */       this.renderer.color(colorBits);
/*  338 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */       
/*  340 */       this.renderer.color(colorBits);
/*  341 */       this.renderer.vertex(x2, y2, 0.0F);
/*  342 */       this.renderer.color(colorBits);
/*  343 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  345 */       this.renderer.color(colorBits);
/*  346 */       this.renderer.vertex(x3, y3, 0.0F);
/*  347 */       this.renderer.color(colorBits);
/*  348 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } else {
/*  350 */       this.renderer.color(colorBits);
/*  351 */       this.renderer.vertex(x1, y1, 0.0F);
/*  352 */       this.renderer.color(colorBits);
/*  353 */       this.renderer.vertex(x2, y2, 0.0F);
/*  354 */       this.renderer.color(colorBits);
/*  355 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, Color col1, Color col2, Color col3) {
/*  361 */     check(ShapeType.Line, ShapeType.Filled, 6);
/*  362 */     if (this.shapeType == ShapeType.Line) {
/*  363 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  364 */       this.renderer.vertex(x1, y1, 0.0F);
/*  365 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  366 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */       
/*  368 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  369 */       this.renderer.vertex(x2, y2, 0.0F);
/*  370 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  371 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  373 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  374 */       this.renderer.vertex(x3, y3, 0.0F);
/*  375 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  376 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } else {
/*  378 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  379 */       this.renderer.vertex(x1, y1, 0.0F);
/*  380 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  381 */       this.renderer.vertex(x2, y2, 0.0F);
/*  382 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  383 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float width, float height) {
/*  389 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*  390 */     float colorBits = this.color.toFloatBits();
/*  391 */     if (this.shapeType == ShapeType.Line) {
/*  392 */       this.renderer.color(colorBits);
/*  393 */       this.renderer.vertex(x, y, 0.0F);
/*  394 */       this.renderer.color(colorBits);
/*  395 */       this.renderer.vertex(x + width, y, 0.0F);
/*      */       
/*  397 */       this.renderer.color(colorBits);
/*  398 */       this.renderer.vertex(x + width, y, 0.0F);
/*  399 */       this.renderer.color(colorBits);
/*  400 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  402 */       this.renderer.color(colorBits);
/*  403 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  404 */       this.renderer.color(colorBits);
/*  405 */       this.renderer.vertex(x, y + height, 0.0F);
/*      */       
/*  407 */       this.renderer.color(colorBits);
/*  408 */       this.renderer.vertex(x, y + height, 0.0F);
/*  409 */       this.renderer.color(colorBits);
/*  410 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } else {
/*  412 */       this.renderer.color(colorBits);
/*  413 */       this.renderer.vertex(x, y, 0.0F);
/*  414 */       this.renderer.color(colorBits);
/*  415 */       this.renderer.vertex(x + width, y, 0.0F);
/*  416 */       this.renderer.color(colorBits);
/*  417 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  419 */       this.renderer.color(colorBits);
/*  420 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  421 */       this.renderer.color(colorBits);
/*  422 */       this.renderer.vertex(x, y + height, 0.0F);
/*  423 */       this.renderer.color(colorBits);
/*  424 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float width, float height, Color col1, Color col2, Color col3, Color col4) {
/*  435 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*      */     
/*  437 */     if (this.shapeType == ShapeType.Line) {
/*  438 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  439 */       this.renderer.vertex(x, y, 0.0F);
/*  440 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  441 */       this.renderer.vertex(x + width, y, 0.0F);
/*      */       
/*  443 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  444 */       this.renderer.vertex(x + width, y, 0.0F);
/*  445 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  446 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  448 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  449 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  450 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  451 */       this.renderer.vertex(x, y + height, 0.0F);
/*      */       
/*  453 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  454 */       this.renderer.vertex(x, y + height, 0.0F);
/*  455 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  456 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } else {
/*  458 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  459 */       this.renderer.vertex(x, y, 0.0F);
/*  460 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  461 */       this.renderer.vertex(x + width, y, 0.0F);
/*  462 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  463 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  465 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  466 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  467 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  468 */       this.renderer.vertex(x, y + height, 0.0F);
/*  469 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  470 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float degrees) {
/*  478 */     rect(x, y, originX, originY, width, height, scaleX, scaleY, degrees, this.color, this.color, this.color, this.color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float degrees, Color col1, Color col2, Color col3, Color col4) {
/*  489 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*      */     
/*  491 */     float cos = MathUtils.cosDeg(degrees);
/*  492 */     float sin = MathUtils.sinDeg(degrees);
/*  493 */     float fx = -originX;
/*  494 */     float fy = -originY;
/*  495 */     float fx2 = width - originX;
/*  496 */     float fy2 = height - originY;
/*      */     
/*  498 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  499 */       fx *= scaleX;
/*  500 */       fy *= scaleY;
/*  501 */       fx2 *= scaleX;
/*  502 */       fy2 *= scaleY;
/*      */     } 
/*      */     
/*  505 */     float worldOriginX = x + originX;
/*  506 */     float worldOriginY = y + originY;
/*      */     
/*  508 */     float x1 = cos * fx - sin * fy + worldOriginX;
/*  509 */     float y1 = sin * fx + cos * fy + worldOriginY;
/*      */     
/*  511 */     float x2 = cos * fx2 - sin * fy + worldOriginX;
/*  512 */     float y2 = sin * fx2 + cos * fy + worldOriginY;
/*      */     
/*  514 */     float x3 = cos * fx2 - sin * fy2 + worldOriginX;
/*  515 */     float y3 = sin * fx2 + cos * fy2 + worldOriginY;
/*      */     
/*  517 */     float x4 = x1 + x3 - x2;
/*  518 */     float y4 = y3 - y2 - y1;
/*      */     
/*  520 */     if (this.shapeType == ShapeType.Line) {
/*  521 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  522 */       this.renderer.vertex(x1, y1, 0.0F);
/*  523 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  524 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */       
/*  526 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  527 */       this.renderer.vertex(x2, y2, 0.0F);
/*  528 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  529 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  531 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  532 */       this.renderer.vertex(x3, y3, 0.0F);
/*  533 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  534 */       this.renderer.vertex(x4, y4, 0.0F);
/*      */       
/*  536 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  537 */       this.renderer.vertex(x4, y4, 0.0F);
/*  538 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  539 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } else {
/*  541 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  542 */       this.renderer.vertex(x1, y1, 0.0F);
/*  543 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  544 */       this.renderer.vertex(x2, y2, 0.0F);
/*  545 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  546 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  548 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  549 */       this.renderer.vertex(x3, y3, 0.0F);
/*  550 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  551 */       this.renderer.vertex(x4, y4, 0.0F);
/*  552 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  553 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void rectLine(float x1, float y1, float x2, float y2, float width) {
/*  560 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*  561 */     float colorBits = this.color.toFloatBits();
/*  562 */     Vector2 t = this.tmp.set(y2 - y1, x1 - x2).nor();
/*  563 */     width *= 0.5F;
/*  564 */     float tx = t.x * width;
/*  565 */     float ty = t.y * width;
/*  566 */     if (this.shapeType == ShapeType.Line) {
/*  567 */       this.renderer.color(colorBits);
/*  568 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*  569 */       this.renderer.color(colorBits);
/*  570 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */       
/*  572 */       this.renderer.color(colorBits);
/*  573 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  574 */       this.renderer.color(colorBits);
/*  575 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*      */       
/*  577 */       this.renderer.color(colorBits);
/*  578 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  579 */       this.renderer.color(colorBits);
/*  580 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*      */       
/*  582 */       this.renderer.color(colorBits);
/*  583 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*  584 */       this.renderer.color(colorBits);
/*  585 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */     } else {
/*  587 */       this.renderer.color(colorBits);
/*  588 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*  589 */       this.renderer.color(colorBits);
/*  590 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*  591 */       this.renderer.color(colorBits);
/*  592 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*      */       
/*  594 */       this.renderer.color(colorBits);
/*  595 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*  596 */       this.renderer.color(colorBits);
/*  597 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  598 */       this.renderer.color(colorBits);
/*  599 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void rectLine(Vector2 p1, Vector2 p2, float width) {
/*  605 */     rectLine(p1.x, p1.y, p2.x, p2.y, width);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void box(float x, float y, float z, float width, float height, float depth) {
/*  611 */     depth = -depth;
/*  612 */     float colorBits = this.color.toFloatBits();
/*  613 */     if (this.shapeType == ShapeType.Line) {
/*  614 */       check(ShapeType.Line, ShapeType.Filled, 24);
/*      */       
/*  616 */       this.renderer.color(colorBits);
/*  617 */       this.renderer.vertex(x, y, z);
/*  618 */       this.renderer.color(colorBits);
/*  619 */       this.renderer.vertex(x + width, y, z);
/*      */       
/*  621 */       this.renderer.color(colorBits);
/*  622 */       this.renderer.vertex(x + width, y, z);
/*  623 */       this.renderer.color(colorBits);
/*  624 */       this.renderer.vertex(x + width, y, z + depth);
/*      */       
/*  626 */       this.renderer.color(colorBits);
/*  627 */       this.renderer.vertex(x + width, y, z + depth);
/*  628 */       this.renderer.color(colorBits);
/*  629 */       this.renderer.vertex(x, y, z + depth);
/*      */       
/*  631 */       this.renderer.color(colorBits);
/*  632 */       this.renderer.vertex(x, y, z + depth);
/*  633 */       this.renderer.color(colorBits);
/*  634 */       this.renderer.vertex(x, y, z);
/*      */       
/*  636 */       this.renderer.color(colorBits);
/*  637 */       this.renderer.vertex(x, y, z);
/*  638 */       this.renderer.color(colorBits);
/*  639 */       this.renderer.vertex(x, y + height, z);
/*      */       
/*  641 */       this.renderer.color(colorBits);
/*  642 */       this.renderer.vertex(x, y + height, z);
/*  643 */       this.renderer.color(colorBits);
/*  644 */       this.renderer.vertex(x + width, y + height, z);
/*      */       
/*  646 */       this.renderer.color(colorBits);
/*  647 */       this.renderer.vertex(x + width, y + height, z);
/*  648 */       this.renderer.color(colorBits);
/*  649 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  651 */       this.renderer.color(colorBits);
/*  652 */       this.renderer.vertex(x + width, y + height, z + depth);
/*  653 */       this.renderer.color(colorBits);
/*  654 */       this.renderer.vertex(x, y + height, z + depth);
/*      */       
/*  656 */       this.renderer.color(colorBits);
/*  657 */       this.renderer.vertex(x, y + height, z + depth);
/*  658 */       this.renderer.color(colorBits);
/*  659 */       this.renderer.vertex(x, y + height, z);
/*      */       
/*  661 */       this.renderer.color(colorBits);
/*  662 */       this.renderer.vertex(x + width, y, z);
/*  663 */       this.renderer.color(colorBits);
/*  664 */       this.renderer.vertex(x + width, y + height, z);
/*      */       
/*  666 */       this.renderer.color(colorBits);
/*  667 */       this.renderer.vertex(x + width, y, z + depth);
/*  668 */       this.renderer.color(colorBits);
/*  669 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  671 */       this.renderer.color(colorBits);
/*  672 */       this.renderer.vertex(x, y, z + depth);
/*  673 */       this.renderer.color(colorBits);
/*  674 */       this.renderer.vertex(x, y + height, z + depth);
/*      */     } else {
/*  676 */       check(ShapeType.Line, ShapeType.Filled, 36);
/*      */ 
/*      */       
/*  679 */       this.renderer.color(colorBits);
/*  680 */       this.renderer.vertex(x, y, z);
/*  681 */       this.renderer.color(colorBits);
/*  682 */       this.renderer.vertex(x + width, y, z);
/*  683 */       this.renderer.color(colorBits);
/*  684 */       this.renderer.vertex(x + width, y + height, z);
/*      */       
/*  686 */       this.renderer.color(colorBits);
/*  687 */       this.renderer.vertex(x, y, z);
/*  688 */       this.renderer.color(colorBits);
/*  689 */       this.renderer.vertex(x + width, y + height, z);
/*  690 */       this.renderer.color(colorBits);
/*  691 */       this.renderer.vertex(x, y + height, z);
/*      */ 
/*      */       
/*  694 */       this.renderer.color(colorBits);
/*  695 */       this.renderer.vertex(x + width, y, z + depth);
/*  696 */       this.renderer.color(colorBits);
/*  697 */       this.renderer.vertex(x, y, z + depth);
/*  698 */       this.renderer.color(colorBits);
/*  699 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  701 */       this.renderer.color(colorBits);
/*  702 */       this.renderer.vertex(x, y + height, z + depth);
/*  703 */       this.renderer.color(colorBits);
/*  704 */       this.renderer.vertex(x, y, z + depth);
/*  705 */       this.renderer.color(colorBits);
/*  706 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */ 
/*      */       
/*  709 */       this.renderer.color(colorBits);
/*  710 */       this.renderer.vertex(x, y, z + depth);
/*  711 */       this.renderer.color(colorBits);
/*  712 */       this.renderer.vertex(x, y, z);
/*  713 */       this.renderer.color(colorBits);
/*  714 */       this.renderer.vertex(x, y + height, z);
/*      */       
/*  716 */       this.renderer.color(colorBits);
/*  717 */       this.renderer.vertex(x, y, z + depth);
/*  718 */       this.renderer.color(colorBits);
/*  719 */       this.renderer.vertex(x, y + height, z);
/*  720 */       this.renderer.color(colorBits);
/*  721 */       this.renderer.vertex(x, y + height, z + depth);
/*      */ 
/*      */       
/*  724 */       this.renderer.color(colorBits);
/*  725 */       this.renderer.vertex(x + width, y, z);
/*  726 */       this.renderer.color(colorBits);
/*  727 */       this.renderer.vertex(x + width, y, z + depth);
/*  728 */       this.renderer.color(colorBits);
/*  729 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  731 */       this.renderer.color(colorBits);
/*  732 */       this.renderer.vertex(x + width, y, z);
/*  733 */       this.renderer.color(colorBits);
/*  734 */       this.renderer.vertex(x + width, y + height, z + depth);
/*  735 */       this.renderer.color(colorBits);
/*  736 */       this.renderer.vertex(x + width, y + height, z);
/*      */ 
/*      */       
/*  739 */       this.renderer.color(colorBits);
/*  740 */       this.renderer.vertex(x, y + height, z);
/*  741 */       this.renderer.color(colorBits);
/*  742 */       this.renderer.vertex(x + width, y + height, z);
/*  743 */       this.renderer.color(colorBits);
/*  744 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  746 */       this.renderer.color(colorBits);
/*  747 */       this.renderer.vertex(x, y + height, z);
/*  748 */       this.renderer.color(colorBits);
/*  749 */       this.renderer.vertex(x + width, y + height, z + depth);
/*  750 */       this.renderer.color(colorBits);
/*  751 */       this.renderer.vertex(x, y + height, z + depth);
/*      */ 
/*      */       
/*  754 */       this.renderer.color(colorBits);
/*  755 */       this.renderer.vertex(x, y, z + depth);
/*  756 */       this.renderer.color(colorBits);
/*  757 */       this.renderer.vertex(x + width, y, z + depth);
/*  758 */       this.renderer.color(colorBits);
/*  759 */       this.renderer.vertex(x + width, y, z);
/*      */       
/*  761 */       this.renderer.color(colorBits);
/*  762 */       this.renderer.vertex(x, y, z + depth);
/*  763 */       this.renderer.color(colorBits);
/*  764 */       this.renderer.vertex(x + width, y, z);
/*  765 */       this.renderer.color(colorBits);
/*  766 */       this.renderer.vertex(x, y, z);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void x(float x, float y, float size) {
/*  773 */     line(x - size, y - size, x + size, y + size);
/*  774 */     line(x - size, y + size, x + size, y - size);
/*      */   }
/*      */ 
/*      */   
/*      */   public void x(Vector2 p, float size) {
/*  779 */     x(p.x, p.y, size);
/*      */   }
/*      */ 
/*      */   
/*      */   public void arc(float x, float y, float radius, float start, float degrees) {
/*  784 */     arc(x, y, radius, start, degrees, Math.max(1, (int)(6.0F * (float)Math.cbrt(radius) * degrees / 360.0F)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void arc(float x, float y, float radius, float start, float degrees, int segments) {
/*  789 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  790 */     float colorBits = this.color.toFloatBits();
/*  791 */     float theta = 6.2831855F * degrees / 360.0F / segments;
/*  792 */     float cos = MathUtils.cos(theta);
/*  793 */     float sin = MathUtils.sin(theta);
/*  794 */     float cx = radius * MathUtils.cos(start * 0.017453292F);
/*  795 */     float cy = radius * MathUtils.sin(start * 0.017453292F);
/*      */     
/*  797 */     if (this.shapeType == ShapeType.Line) {
/*  798 */       check(ShapeType.Line, ShapeType.Filled, segments * 2 + 2);
/*      */       
/*  800 */       this.renderer.color(colorBits);
/*  801 */       this.renderer.vertex(x, y, 0.0F);
/*  802 */       this.renderer.color(colorBits);
/*  803 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  804 */       for (int i = 0; i < segments; i++) {
/*  805 */         this.renderer.color(colorBits);
/*  806 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  807 */         float f = cx;
/*  808 */         cx = cos * cx - sin * cy;
/*  809 */         cy = sin * f + cos * cy;
/*  810 */         this.renderer.color(colorBits);
/*  811 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*  813 */       this.renderer.color(colorBits);
/*  814 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } else {
/*  816 */       check(ShapeType.Line, ShapeType.Filled, segments * 3 + 3);
/*      */       
/*  818 */       for (int i = 0; i < segments; i++) {
/*  819 */         this.renderer.color(colorBits);
/*  820 */         this.renderer.vertex(x, y, 0.0F);
/*  821 */         this.renderer.color(colorBits);
/*  822 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  823 */         float f = cx;
/*  824 */         cx = cos * cx - sin * cy;
/*  825 */         cy = sin * f + cos * cy;
/*  826 */         this.renderer.color(colorBits);
/*  827 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*  829 */       this.renderer.color(colorBits);
/*  830 */       this.renderer.vertex(x, y, 0.0F);
/*  831 */       this.renderer.color(colorBits);
/*  832 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } 
/*      */     
/*  835 */     float temp = cx;
/*  836 */     cx = 0.0F;
/*  837 */     cy = 0.0F;
/*  838 */     this.renderer.color(colorBits);
/*  839 */     this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void circle(float x, float y, float radius) {
/*  844 */     circle(x, y, radius, Math.max(1, (int)(6.0F * (float)Math.cbrt(radius))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void circle(float x, float y, float radius, int segments) {
/*  849 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  850 */     float colorBits = this.color.toFloatBits();
/*  851 */     float angle = 6.2831855F / segments;
/*  852 */     float cos = MathUtils.cos(angle);
/*  853 */     float sin = MathUtils.sin(angle);
/*  854 */     float cx = radius, cy = 0.0F;
/*  855 */     if (this.shapeType == ShapeType.Line) {
/*  856 */       check(ShapeType.Line, ShapeType.Filled, segments * 2 + 2);
/*  857 */       for (int i = 0; i < segments; i++) {
/*  858 */         this.renderer.color(colorBits);
/*  859 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  860 */         float f = cx;
/*  861 */         cx = cos * cx - sin * cy;
/*  862 */         cy = sin * f + cos * cy;
/*  863 */         this.renderer.color(colorBits);
/*  864 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*      */       
/*  867 */       this.renderer.color(colorBits);
/*  868 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } else {
/*  870 */       check(ShapeType.Line, ShapeType.Filled, segments * 3 + 3);
/*  871 */       segments--;
/*  872 */       for (int i = 0; i < segments; i++) {
/*  873 */         this.renderer.color(colorBits);
/*  874 */         this.renderer.vertex(x, y, 0.0F);
/*  875 */         this.renderer.color(colorBits);
/*  876 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  877 */         float f = cx;
/*  878 */         cx = cos * cx - sin * cy;
/*  879 */         cy = sin * f + cos * cy;
/*  880 */         this.renderer.color(colorBits);
/*  881 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*      */       
/*  884 */       this.renderer.color(colorBits);
/*  885 */       this.renderer.vertex(x, y, 0.0F);
/*  886 */       this.renderer.color(colorBits);
/*  887 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } 
/*      */     
/*  890 */     float temp = cx;
/*  891 */     cx = radius;
/*  892 */     cy = 0.0F;
/*  893 */     this.renderer.color(colorBits);
/*  894 */     this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height) {
/*  899 */     ellipse(x, y, width, height, Math.max(1, (int)(12.0F * (float)Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height, int segments) {
/*  904 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  905 */     check(ShapeType.Line, ShapeType.Filled, segments * 3);
/*  906 */     float colorBits = this.color.toFloatBits();
/*  907 */     float angle = 6.2831855F / segments;
/*      */     
/*  909 */     float cx = x + width / 2.0F, cy = y + height / 2.0F;
/*  910 */     if (this.shapeType == ShapeType.Line) {
/*  911 */       for (int i = 0; i < segments; i++) {
/*  912 */         this.renderer.color(colorBits);
/*  913 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos(i * angle), cy + height * 0.5F * MathUtils.sin(i * angle), 0.0F);
/*      */         
/*  915 */         this.renderer.color(colorBits);
/*  916 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos((i + 1) * angle), cy + height * 0.5F * 
/*  917 */             MathUtils.sin((i + 1) * angle), 0.0F);
/*      */       } 
/*      */     } else {
/*  920 */       for (int i = 0; i < segments; i++) {
/*  921 */         this.renderer.color(colorBits);
/*  922 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos(i * angle), cy + height * 0.5F * MathUtils.sin(i * angle), 0.0F);
/*      */         
/*  924 */         this.renderer.color(colorBits);
/*  925 */         this.renderer.vertex(cx, cy, 0.0F);
/*      */         
/*  927 */         this.renderer.color(colorBits);
/*  928 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos((i + 1) * angle), cy + height * 0.5F * 
/*  929 */             MathUtils.sin((i + 1) * angle), 0.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height, float rotation) {
/*  936 */     ellipse(x, y, width, height, rotation, Math.max(1, (int)(12.0F * (float)Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height, float rotation, int segments) {
/*  941 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  942 */     check(ShapeType.Line, ShapeType.Filled, segments * 3);
/*  943 */     float colorBits = this.color.toFloatBits();
/*  944 */     float angle = 6.2831855F / segments;
/*      */     
/*  946 */     rotation = 3.1415927F * rotation / 180.0F;
/*  947 */     float sin = MathUtils.sin(rotation);
/*  948 */     float cos = MathUtils.cos(rotation);
/*      */     
/*  950 */     float cx = x + width / 2.0F, cy = y + height / 2.0F;
/*  951 */     float x1 = width * 0.5F;
/*  952 */     float y1 = 0.0F;
/*  953 */     if (this.shapeType == ShapeType.Line) {
/*  954 */       for (int i = 0; i < segments; i++) {
/*  955 */         this.renderer.color(colorBits);
/*  956 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */         
/*  958 */         x1 = width * 0.5F * MathUtils.cos((i + 1) * angle);
/*  959 */         y1 = height * 0.5F * MathUtils.sin((i + 1) * angle);
/*      */         
/*  961 */         this.renderer.color(colorBits);
/*  962 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */       } 
/*      */     } else {
/*  965 */       for (int i = 0; i < segments; i++) {
/*  966 */         this.renderer.color(colorBits);
/*  967 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */         
/*  969 */         this.renderer.color(colorBits);
/*  970 */         this.renderer.vertex(cx, cy, 0.0F);
/*      */         
/*  972 */         x1 = width * 0.5F * MathUtils.cos((i + 1) * angle);
/*  973 */         y1 = height * 0.5F * MathUtils.sin((i + 1) * angle);
/*      */         
/*  975 */         this.renderer.color(colorBits);
/*  976 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cone(float x, float y, float z, float radius, float height) {
/*  984 */     cone(x, y, z, radius, height, Math.max(1, (int)(4.0F * (float)Math.sqrt(radius))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void cone(float x, float y, float z, float radius, float height, int segments) {
/*  989 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  990 */     check(ShapeType.Line, ShapeType.Filled, segments * 4 + 2);
/*  991 */     float colorBits = this.color.toFloatBits();
/*  992 */     float angle = 6.2831855F / segments;
/*  993 */     float cos = MathUtils.cos(angle);
/*  994 */     float sin = MathUtils.sin(angle);
/*  995 */     float cx = radius, cy = 0.0F;
/*  996 */     if (this.shapeType == ShapeType.Line) {
/*  997 */       for (int i = 0; i < segments; i++) {
/*  998 */         this.renderer.color(colorBits);
/*  999 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1000 */         this.renderer.color(colorBits);
/* 1001 */         this.renderer.vertex(x, y, z + height);
/* 1002 */         this.renderer.color(colorBits);
/* 1003 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1004 */         float f = cx;
/* 1005 */         cx = cos * cx - sin * cy;
/* 1006 */         cy = sin * f + cos * cy;
/* 1007 */         this.renderer.color(colorBits);
/* 1008 */         this.renderer.vertex(x + cx, y + cy, z);
/*      */       } 
/*      */       
/* 1011 */       this.renderer.color(colorBits);
/* 1012 */       this.renderer.vertex(x + cx, y + cy, z);
/*      */     } else {
/* 1014 */       segments--;
/* 1015 */       for (int i = 0; i < segments; i++) {
/* 1016 */         this.renderer.color(colorBits);
/* 1017 */         this.renderer.vertex(x, y, z);
/* 1018 */         this.renderer.color(colorBits);
/* 1019 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1020 */         float f1 = cx;
/* 1021 */         float f2 = cy;
/* 1022 */         cx = cos * cx - sin * cy;
/* 1023 */         cy = sin * f1 + cos * cy;
/* 1024 */         this.renderer.color(colorBits);
/* 1025 */         this.renderer.vertex(x + cx, y + cy, z);
/*      */         
/* 1027 */         this.renderer.color(colorBits);
/* 1028 */         this.renderer.vertex(x + f1, y + f2, z);
/* 1029 */         this.renderer.color(colorBits);
/* 1030 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1031 */         this.renderer.color(colorBits);
/* 1032 */         this.renderer.vertex(x, y, z + height);
/*      */       } 
/*      */       
/* 1035 */       this.renderer.color(colorBits);
/* 1036 */       this.renderer.vertex(x, y, z);
/* 1037 */       this.renderer.color(colorBits);
/* 1038 */       this.renderer.vertex(x + cx, y + cy, z);
/*      */     } 
/* 1040 */     float temp = cx;
/* 1041 */     float temp2 = cy;
/* 1042 */     cx = radius;
/* 1043 */     cy = 0.0F;
/* 1044 */     this.renderer.color(colorBits);
/* 1045 */     this.renderer.vertex(x + cx, y + cy, z);
/* 1046 */     if (this.shapeType != ShapeType.Line) {
/* 1047 */       this.renderer.color(colorBits);
/* 1048 */       this.renderer.vertex(x + temp, y + temp2, z);
/* 1049 */       this.renderer.color(colorBits);
/* 1050 */       this.renderer.vertex(x + cx, y + cy, z);
/* 1051 */       this.renderer.color(colorBits);
/* 1052 */       this.renderer.vertex(x, y, z + height);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void polygon(float[] vertices, int offset, int count) {
/* 1058 */     if (count < 6) throw new IllegalArgumentException("Polygons must contain at least 3 points."); 
/* 1059 */     if (count % 2 != 0) throw new IllegalArgumentException("Polygons must have an even number of vertices.");
/*      */     
/* 1061 */     check(ShapeType.Line, null, count);
/* 1062 */     float colorBits = this.color.toFloatBits();
/* 1063 */     float firstX = vertices[0];
/* 1064 */     float firstY = vertices[1];
/*      */     
/* 1066 */     for (int i = offset, n = offset + count; i < n; i += 2) {
/* 1067 */       float x2, y2, x1 = vertices[i];
/* 1068 */       float y1 = vertices[i + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1073 */       if (i + 2 >= count) {
/* 1074 */         x2 = firstX;
/* 1075 */         y2 = firstY;
/*      */       } else {
/* 1077 */         x2 = vertices[i + 2];
/* 1078 */         y2 = vertices[i + 3];
/*      */       } 
/*      */       
/* 1081 */       this.renderer.color(colorBits);
/* 1082 */       this.renderer.vertex(x1, y1, 0.0F);
/* 1083 */       this.renderer.color(colorBits);
/* 1084 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void polygon(float[] vertices) {
/* 1090 */     polygon(vertices, 0, vertices.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void polyline(float[] vertices, int offset, int count) {
/* 1095 */     if (count < 4) throw new IllegalArgumentException("Polylines must contain at least 2 points."); 
/* 1096 */     if (count % 2 != 0) throw new IllegalArgumentException("Polylines must have an even number of vertices.");
/*      */     
/* 1098 */     check(ShapeType.Line, null, count);
/* 1099 */     float colorBits = this.color.toFloatBits();
/* 1100 */     for (int i = offset, n = offset + count - 2; i < n; i += 2) {
/* 1101 */       float x1 = vertices[i];
/* 1102 */       float y1 = vertices[i + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1107 */       float x2 = vertices[i + 2];
/* 1108 */       float y2 = vertices[i + 3];
/*      */       
/* 1110 */       this.renderer.color(colorBits);
/* 1111 */       this.renderer.vertex(x1, y1, 0.0F);
/* 1112 */       this.renderer.color(colorBits);
/* 1113 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void polyline(float[] vertices) {
/* 1119 */     polyline(vertices, 0, vertices.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private void check(ShapeType preferred, ShapeType other, int newVertices) {
/* 1124 */     if (this.shapeType == null) throw new IllegalStateException("begin must be called first.");
/*      */     
/* 1126 */     if (this.shapeType != preferred && this.shapeType != other) {
/*      */       
/* 1128 */       if (!this.autoShapeType) {
/* 1129 */         if (other == null) {
/* 1130 */           throw new IllegalStateException("Must call begin(ShapeType." + preferred + ").");
/*      */         }
/* 1132 */         throw new IllegalStateException("Must call begin(ShapeType." + preferred + ") or begin(ShapeType." + other + ").");
/*      */       } 
/* 1134 */       end();
/* 1135 */       begin(preferred);
/* 1136 */     } else if (this.matrixDirty) {
/*      */       
/* 1138 */       ShapeType type = this.shapeType;
/* 1139 */       end();
/* 1140 */       begin(type);
/* 1141 */     } else if (this.renderer.getMaxVertices() - this.renderer.getNumVertices() < newVertices) {
/*      */       
/* 1143 */       ShapeType type = this.shapeType;
/* 1144 */       end();
/* 1145 */       begin(type);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/* 1151 */     this.renderer.end();
/* 1152 */     this.shapeType = null;
/*      */   }
/*      */   
/*      */   public void flush() {
/* 1156 */     ShapeType type = this.shapeType;
/* 1157 */     end();
/* 1158 */     begin(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public ShapeType getCurrentType() {
/* 1163 */     return this.shapeType;
/*      */   }
/*      */   
/*      */   public ImmediateModeRenderer getRenderer() {
/* 1167 */     return this.renderer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDrawing() {
/* 1172 */     return (this.shapeType != null);
/*      */   }
/*      */   
/*      */   public void dispose() {
/* 1176 */     this.renderer.dispose();
/*      */   }
/*      */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\glutils\ShapeRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */