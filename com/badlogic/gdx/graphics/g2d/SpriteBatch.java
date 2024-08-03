/*      */ package com.badlogic.gdx.graphics.g2d;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.GL20;
/*      */ import com.badlogic.gdx.graphics.Mesh;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.VertexAttribute;
/*      */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*      */ import com.badlogic.gdx.math.Affine2;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.utils.NumberUtils;
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
/*      */ public class SpriteBatch
/*      */   implements Batch
/*      */ {
/*      */   @Deprecated
/*   40 */   public static Mesh.VertexDataType defaultVertexDataType = Mesh.VertexDataType.VertexArray;
/*      */   
/*      */   private Mesh mesh;
/*      */   
/*      */   final float[] vertices;
/*   45 */   int idx = 0;
/*   46 */   Texture lastTexture = null;
/*   47 */   float invTexWidth = 0.0F; float invTexHeight = 0.0F;
/*      */   
/*      */   boolean drawing = false;
/*      */   
/*   51 */   private final Matrix4 transformMatrix = new Matrix4();
/*   52 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   53 */   private final Matrix4 combinedMatrix = new Matrix4();
/*      */   
/*      */   private boolean blendingDisabled = false;
/*   56 */   private int blendSrcFunc = 770;
/*   57 */   private int blendDstFunc = 771;
/*      */   
/*      */   private final ShaderProgram shader;
/*   60 */   private ShaderProgram customShader = null;
/*      */   
/*      */   private boolean ownsShader;
/*   63 */   float color = Color.WHITE.toFloatBits();
/*   64 */   private Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */ 
/*      */   
/*   67 */   public int renderCalls = 0;
/*      */ 
/*      */   
/*   70 */   public int totalRenderCalls = 0;
/*      */ 
/*      */   
/*   73 */   public int maxSpritesInBatch = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteBatch() {
/*   78 */     this(1000, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteBatch(int size) {
/*   84 */     this(size, null);
/*      */   }
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
/*      */   public SpriteBatch(int size, ShaderProgram defaultShader) {
/*   97 */     if (size > 5460) throw new IllegalArgumentException("Can't have more than 5460 sprites per batch: " + size);
/*      */     
/*   99 */     Mesh.VertexDataType vertexDataType = (Gdx.gl30 != null) ? Mesh.VertexDataType.VertexBufferObjectWithVAO : defaultVertexDataType;
/*      */     
/*  101 */     this.mesh = new Mesh(vertexDataType, false, size * 4, size * 6, new VertexAttribute[] { new VertexAttribute(1, 2, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0") });
/*      */ 
/*      */ 
/*      */     
/*  105 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*      */     
/*  107 */     this.vertices = new float[size * 20];
/*      */     
/*  109 */     int len = size * 6;
/*  110 */     short[] indices = new short[len];
/*  111 */     short j = 0;
/*  112 */     for (int i = 0; i < len; i += 6, j = (short)(j + 4)) {
/*  113 */       indices[i] = j;
/*  114 */       indices[i + 1] = (short)(j + 1);
/*  115 */       indices[i + 2] = (short)(j + 2);
/*  116 */       indices[i + 3] = (short)(j + 2);
/*  117 */       indices[i + 4] = (short)(j + 3);
/*  118 */       indices[i + 5] = j;
/*      */     } 
/*  120 */     this.mesh.setIndices(indices);
/*      */     
/*  122 */     if (defaultShader == null) {
/*  123 */       this.shader = createDefaultShader();
/*  124 */       this.ownsShader = true;
/*      */     } else {
/*  126 */       this.shader = defaultShader;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static ShaderProgram createDefaultShader() {
/*  131 */     String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_color.a = v_color.a * (255.0/254.0);\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projTrans * a_position;\n}\n";
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
/*  145 */     String fragmentShader = "#ifdef GL_ES\n#define LOWP lowp\nprecision mediump float;\n#else\n#define LOWP \n#endif\nvarying LOWP vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}";
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
/*  159 */     ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
/*  160 */     if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog()); 
/*  161 */     return shader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin() {
/*  166 */     if (this.drawing) throw new IllegalStateException("SpriteBatch.end must be called before begin."); 
/*  167 */     this.renderCalls = 0;
/*      */     
/*  169 */     Gdx.gl.glDepthMask(false);
/*  170 */     if (this.customShader != null) {
/*  171 */       this.customShader.begin();
/*      */     } else {
/*  173 */       this.shader.begin();
/*  174 */     }  setupMatrices();
/*      */     
/*  176 */     this.drawing = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/*  181 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before end."); 
/*  182 */     if (this.idx > 0) flush(); 
/*  183 */     this.lastTexture = null;
/*  184 */     this.drawing = false;
/*      */     
/*  186 */     GL20 gl = Gdx.gl;
/*  187 */     gl.glDepthMask(true);
/*  188 */     if (isBlendingEnabled()) gl.glDisable(3042);
/*      */     
/*  190 */     if (this.customShader != null) {
/*  191 */       this.customShader.end();
/*      */     } else {
/*  193 */       this.shader.end();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setColor(Color tint) {
/*  198 */     this.color = tint.toFloatBits();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  203 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/*  204 */     this.color = NumberUtils.intToFloatColor(intBits);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float color) {
/*  209 */     this.color = color;
/*      */   }
/*      */ 
/*      */   
/*      */   public Color getColor() {
/*  214 */     int intBits = NumberUtils.floatToIntColor(this.color);
/*  215 */     Color color = this.tempColor;
/*  216 */     color.r = (intBits & 0xFF) / 255.0F;
/*  217 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/*  218 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/*  219 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/*  220 */     return color;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPackedColor() {
/*  225 */     return this.color;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  231 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  233 */     float[] vertices = this.vertices;
/*      */     
/*  235 */     if (texture != this.lastTexture) {
/*  236 */       switchTexture(texture);
/*  237 */     } else if (this.idx == vertices.length) {
/*  238 */       flush();
/*      */     } 
/*      */     
/*  241 */     float worldOriginX = x + originX;
/*  242 */     float worldOriginY = y + originY;
/*  243 */     float fx = -originX;
/*  244 */     float fy = -originY;
/*  245 */     float fx2 = width - originX;
/*  246 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  249 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  250 */       fx *= scaleX;
/*  251 */       fy *= scaleY;
/*  252 */       fx2 *= scaleX;
/*  253 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  257 */     float p1x = fx;
/*  258 */     float p1y = fy;
/*  259 */     float p2x = fx;
/*  260 */     float p2y = fy2;
/*  261 */     float p3x = fx2;
/*  262 */     float p3y = fy2;
/*  263 */     float p4x = fx2;
/*  264 */     float p4y = fy;
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
/*  276 */     if (rotation != 0.0F) {
/*  277 */       float cos = MathUtils.cosDeg(rotation);
/*  278 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  280 */       x1 = cos * p1x - sin * p1y;
/*  281 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  283 */       x2 = cos * p2x - sin * p2y;
/*  284 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  286 */       x3 = cos * p3x - sin * p3y;
/*  287 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  289 */       x4 = x1 + x3 - x2;
/*  290 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  292 */       x1 = p1x;
/*  293 */       y1 = p1y;
/*      */       
/*  295 */       x2 = p2x;
/*  296 */       y2 = p2y;
/*      */       
/*  298 */       x3 = p3x;
/*  299 */       y3 = p3y;
/*      */       
/*  301 */       x4 = p4x;
/*  302 */       y4 = p4y;
/*      */     } 
/*      */     
/*  305 */     x1 += worldOriginX;
/*  306 */     y1 += worldOriginY;
/*  307 */     x2 += worldOriginX;
/*  308 */     y2 += worldOriginY;
/*  309 */     x3 += worldOriginX;
/*  310 */     y3 += worldOriginY;
/*  311 */     x4 += worldOriginX;
/*  312 */     y4 += worldOriginY;
/*      */     
/*  314 */     float u = srcX * this.invTexWidth;
/*  315 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  316 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  317 */     float v2 = srcY * this.invTexHeight;
/*      */     
/*  319 */     if (flipX) {
/*  320 */       float tmp = u;
/*  321 */       u = u2;
/*  322 */       u2 = tmp;
/*      */     } 
/*      */     
/*  325 */     if (flipY) {
/*  326 */       float tmp = v;
/*  327 */       v = v2;
/*  328 */       v2 = tmp;
/*      */     } 
/*      */     
/*  331 */     float color = this.color;
/*  332 */     int idx = this.idx;
/*  333 */     vertices[idx++] = x1;
/*  334 */     vertices[idx++] = y1;
/*  335 */     vertices[idx++] = color;
/*  336 */     vertices[idx++] = u;
/*  337 */     vertices[idx++] = v;
/*      */     
/*  339 */     vertices[idx++] = x2;
/*  340 */     vertices[idx++] = y2;
/*  341 */     vertices[idx++] = color;
/*  342 */     vertices[idx++] = u;
/*  343 */     vertices[idx++] = v2;
/*      */     
/*  345 */     vertices[idx++] = x3;
/*  346 */     vertices[idx++] = y3;
/*  347 */     vertices[idx++] = color;
/*  348 */     vertices[idx++] = u2;
/*  349 */     vertices[idx++] = v2;
/*      */     
/*  351 */     vertices[idx++] = x4;
/*  352 */     vertices[idx++] = y4;
/*  353 */     vertices[idx++] = color;
/*  354 */     vertices[idx++] = u2;
/*  355 */     vertices[idx++] = v;
/*  356 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*  362 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  364 */     float[] vertices = this.vertices;
/*      */     
/*  366 */     if (texture != this.lastTexture) {
/*  367 */       switchTexture(texture);
/*  368 */     } else if (this.idx == vertices.length) {
/*  369 */       flush();
/*      */     } 
/*  371 */     float u = srcX * this.invTexWidth;
/*  372 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  373 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  374 */     float v2 = srcY * this.invTexHeight;
/*  375 */     float fx2 = x + width;
/*  376 */     float fy2 = y + height;
/*      */     
/*  378 */     if (flipX) {
/*  379 */       float tmp = u;
/*  380 */       u = u2;
/*  381 */       u2 = tmp;
/*      */     } 
/*      */     
/*  384 */     if (flipY) {
/*  385 */       float tmp = v;
/*  386 */       v = v2;
/*  387 */       v2 = tmp;
/*      */     } 
/*      */     
/*  390 */     float color = this.color;
/*  391 */     int idx = this.idx;
/*  392 */     vertices[idx++] = x;
/*  393 */     vertices[idx++] = y;
/*  394 */     vertices[idx++] = color;
/*  395 */     vertices[idx++] = u;
/*  396 */     vertices[idx++] = v;
/*      */     
/*  398 */     vertices[idx++] = x;
/*  399 */     vertices[idx++] = fy2;
/*  400 */     vertices[idx++] = color;
/*  401 */     vertices[idx++] = u;
/*  402 */     vertices[idx++] = v2;
/*      */     
/*  404 */     vertices[idx++] = fx2;
/*  405 */     vertices[idx++] = fy2;
/*  406 */     vertices[idx++] = color;
/*  407 */     vertices[idx++] = u2;
/*  408 */     vertices[idx++] = v2;
/*      */     
/*  410 */     vertices[idx++] = fx2;
/*  411 */     vertices[idx++] = y;
/*  412 */     vertices[idx++] = color;
/*  413 */     vertices[idx++] = u2;
/*  414 */     vertices[idx++] = v;
/*  415 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  420 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  422 */     float[] vertices = this.vertices;
/*      */     
/*  424 */     if (texture != this.lastTexture) {
/*  425 */       switchTexture(texture);
/*  426 */     } else if (this.idx == vertices.length) {
/*  427 */       flush();
/*      */     } 
/*  429 */     float u = srcX * this.invTexWidth;
/*  430 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  431 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  432 */     float v2 = srcY * this.invTexHeight;
/*  433 */     float fx2 = x + srcWidth;
/*  434 */     float fy2 = y + srcHeight;
/*      */     
/*  436 */     float color = this.color;
/*  437 */     int idx = this.idx;
/*  438 */     vertices[idx++] = x;
/*  439 */     vertices[idx++] = y;
/*  440 */     vertices[idx++] = color;
/*  441 */     vertices[idx++] = u;
/*  442 */     vertices[idx++] = v;
/*      */     
/*  444 */     vertices[idx++] = x;
/*  445 */     vertices[idx++] = fy2;
/*  446 */     vertices[idx++] = color;
/*  447 */     vertices[idx++] = u;
/*  448 */     vertices[idx++] = v2;
/*      */     
/*  450 */     vertices[idx++] = fx2;
/*  451 */     vertices[idx++] = fy2;
/*  452 */     vertices[idx++] = color;
/*  453 */     vertices[idx++] = u2;
/*  454 */     vertices[idx++] = v2;
/*      */     
/*  456 */     vertices[idx++] = fx2;
/*  457 */     vertices[idx++] = y;
/*  458 */     vertices[idx++] = color;
/*  459 */     vertices[idx++] = u2;
/*  460 */     vertices[idx++] = v;
/*  461 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
/*  466 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  468 */     float[] vertices = this.vertices;
/*      */     
/*  470 */     if (texture != this.lastTexture) {
/*  471 */       switchTexture(texture);
/*  472 */     } else if (this.idx == vertices.length) {
/*  473 */       flush();
/*      */     } 
/*  475 */     float fx2 = x + width;
/*  476 */     float fy2 = y + height;
/*      */     
/*  478 */     float color = this.color;
/*  479 */     int idx = this.idx;
/*  480 */     vertices[idx++] = x;
/*  481 */     vertices[idx++] = y;
/*  482 */     vertices[idx++] = color;
/*  483 */     vertices[idx++] = u;
/*  484 */     vertices[idx++] = v;
/*      */     
/*  486 */     vertices[idx++] = x;
/*  487 */     vertices[idx++] = fy2;
/*  488 */     vertices[idx++] = color;
/*  489 */     vertices[idx++] = u;
/*  490 */     vertices[idx++] = v2;
/*      */     
/*  492 */     vertices[idx++] = fx2;
/*  493 */     vertices[idx++] = fy2;
/*  494 */     vertices[idx++] = color;
/*  495 */     vertices[idx++] = u2;
/*  496 */     vertices[idx++] = v2;
/*      */     
/*  498 */     vertices[idx++] = fx2;
/*  499 */     vertices[idx++] = y;
/*  500 */     vertices[idx++] = color;
/*  501 */     vertices[idx++] = u2;
/*  502 */     vertices[idx++] = v;
/*  503 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y) {
/*  508 */     draw(texture, x, y, texture.getWidth(), texture.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height) {
/*  513 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  515 */     float[] vertices = this.vertices;
/*      */     
/*  517 */     if (texture != this.lastTexture) {
/*  518 */       switchTexture(texture);
/*  519 */     } else if (this.idx == vertices.length) {
/*  520 */       flush();
/*      */     } 
/*  522 */     float fx2 = x + width;
/*  523 */     float fy2 = y + height;
/*  524 */     float u = 0.0F;
/*  525 */     float v = 1.0F;
/*  526 */     float u2 = 1.0F;
/*  527 */     float v2 = 0.0F;
/*      */     
/*  529 */     float color = this.color;
/*  530 */     int idx = this.idx;
/*  531 */     vertices[idx++] = x;
/*  532 */     vertices[idx++] = y;
/*  533 */     vertices[idx++] = color;
/*  534 */     vertices[idx++] = 0.0F;
/*  535 */     vertices[idx++] = 1.0F;
/*      */     
/*  537 */     vertices[idx++] = x;
/*  538 */     vertices[idx++] = fy2;
/*  539 */     vertices[idx++] = color;
/*  540 */     vertices[idx++] = 0.0F;
/*  541 */     vertices[idx++] = 0.0F;
/*      */     
/*  543 */     vertices[idx++] = fx2;
/*  544 */     vertices[idx++] = fy2;
/*  545 */     vertices[idx++] = color;
/*  546 */     vertices[idx++] = 1.0F;
/*  547 */     vertices[idx++] = 0.0F;
/*      */     
/*  549 */     vertices[idx++] = fx2;
/*  550 */     vertices[idx++] = y;
/*  551 */     vertices[idx++] = color;
/*  552 */     vertices[idx++] = 1.0F;
/*  553 */     vertices[idx++] = 1.0F;
/*  554 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
/*  559 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  561 */     int verticesLength = this.vertices.length;
/*  562 */     int remainingVertices = verticesLength;
/*  563 */     if (texture != this.lastTexture) {
/*  564 */       switchTexture(texture);
/*      */     } else {
/*  566 */       remainingVertices -= this.idx;
/*  567 */       if (remainingVertices == 0) {
/*  568 */         flush();
/*  569 */         remainingVertices = verticesLength;
/*      */       } 
/*      */     } 
/*  572 */     int copyCount = Math.min(remainingVertices, count);
/*      */     
/*  574 */     System.arraycopy(spriteVertices, offset, this.vertices, this.idx, copyCount);
/*  575 */     this.idx += copyCount;
/*  576 */     count -= copyCount;
/*  577 */     while (count > 0) {
/*  578 */       offset += copyCount;
/*  579 */       flush();
/*  580 */       copyCount = Math.min(verticesLength, count);
/*  581 */       System.arraycopy(spriteVertices, offset, this.vertices, 0, copyCount);
/*  582 */       this.idx += copyCount;
/*  583 */       count -= copyCount;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y) {
/*  589 */     draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float width, float height) {
/*  594 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  596 */     float[] vertices = this.vertices;
/*      */     
/*  598 */     Texture texture = region.texture;
/*  599 */     if (texture != this.lastTexture) {
/*  600 */       switchTexture(texture);
/*  601 */     } else if (this.idx == vertices.length) {
/*  602 */       flush();
/*      */     } 
/*  604 */     float fx2 = x + width;
/*  605 */     float fy2 = y + height;
/*  606 */     float u = region.u;
/*  607 */     float v = region.v2;
/*  608 */     float u2 = region.u2;
/*  609 */     float v2 = region.v;
/*      */     
/*  611 */     float color = this.color;
/*  612 */     int idx = this.idx;
/*  613 */     vertices[idx++] = x;
/*  614 */     vertices[idx++] = y;
/*  615 */     vertices[idx++] = color;
/*  616 */     vertices[idx++] = u;
/*  617 */     vertices[idx++] = v;
/*      */     
/*  619 */     vertices[idx++] = x;
/*  620 */     vertices[idx++] = fy2;
/*  621 */     vertices[idx++] = color;
/*  622 */     vertices[idx++] = u;
/*  623 */     vertices[idx++] = v2;
/*      */     
/*  625 */     vertices[idx++] = fx2;
/*  626 */     vertices[idx++] = fy2;
/*  627 */     vertices[idx++] = color;
/*  628 */     vertices[idx++] = u2;
/*  629 */     vertices[idx++] = v2;
/*      */     
/*  631 */     vertices[idx++] = fx2;
/*  632 */     vertices[idx++] = y;
/*  633 */     vertices[idx++] = color;
/*  634 */     vertices[idx++] = u2;
/*  635 */     vertices[idx++] = v;
/*  636 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  642 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  644 */     float[] vertices = this.vertices;
/*      */     
/*  646 */     Texture texture = region.texture;
/*  647 */     if (texture != this.lastTexture) {
/*  648 */       switchTexture(texture);
/*  649 */     } else if (this.idx == vertices.length) {
/*  650 */       flush();
/*      */     } 
/*      */     
/*  653 */     float worldOriginX = x + originX;
/*  654 */     float worldOriginY = y + originY;
/*  655 */     float fx = -originX;
/*  656 */     float fy = -originY;
/*  657 */     float fx2 = width - originX;
/*  658 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  661 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  662 */       fx *= scaleX;
/*  663 */       fy *= scaleY;
/*  664 */       fx2 *= scaleX;
/*  665 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  669 */     float p1x = fx;
/*  670 */     float p1y = fy;
/*  671 */     float p2x = fx;
/*  672 */     float p2y = fy2;
/*  673 */     float p3x = fx2;
/*  674 */     float p3y = fy2;
/*  675 */     float p4x = fx2;
/*  676 */     float p4y = fy;
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
/*  688 */     if (rotation != 0.0F) {
/*  689 */       float cos = MathUtils.cosDeg(rotation);
/*  690 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  692 */       x1 = cos * p1x - sin * p1y;
/*  693 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  695 */       x2 = cos * p2x - sin * p2y;
/*  696 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  698 */       x3 = cos * p3x - sin * p3y;
/*  699 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  701 */       x4 = x1 + x3 - x2;
/*  702 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  704 */       x1 = p1x;
/*  705 */       y1 = p1y;
/*      */       
/*  707 */       x2 = p2x;
/*  708 */       y2 = p2y;
/*      */       
/*  710 */       x3 = p3x;
/*  711 */       y3 = p3y;
/*      */       
/*  713 */       x4 = p4x;
/*  714 */       y4 = p4y;
/*      */     } 
/*      */     
/*  717 */     x1 += worldOriginX;
/*  718 */     y1 += worldOriginY;
/*  719 */     x2 += worldOriginX;
/*  720 */     y2 += worldOriginY;
/*  721 */     x3 += worldOriginX;
/*  722 */     y3 += worldOriginY;
/*  723 */     x4 += worldOriginX;
/*  724 */     y4 += worldOriginY;
/*      */     
/*  726 */     float u = region.u;
/*  727 */     float v = region.v2;
/*  728 */     float u2 = region.u2;
/*  729 */     float v2 = region.v;
/*      */     
/*  731 */     float color = this.color;
/*  732 */     int idx = this.idx;
/*  733 */     vertices[idx++] = x1;
/*  734 */     vertices[idx++] = y1;
/*  735 */     vertices[idx++] = color;
/*  736 */     vertices[idx++] = u;
/*  737 */     vertices[idx++] = v;
/*      */     
/*  739 */     vertices[idx++] = x2;
/*  740 */     vertices[idx++] = y2;
/*  741 */     vertices[idx++] = color;
/*  742 */     vertices[idx++] = u;
/*  743 */     vertices[idx++] = v2;
/*      */     
/*  745 */     vertices[idx++] = x3;
/*  746 */     vertices[idx++] = y3;
/*  747 */     vertices[idx++] = color;
/*  748 */     vertices[idx++] = u2;
/*  749 */     vertices[idx++] = v2;
/*      */     
/*  751 */     vertices[idx++] = x4;
/*  752 */     vertices[idx++] = y4;
/*  753 */     vertices[idx++] = color;
/*  754 */     vertices[idx++] = u2;
/*  755 */     vertices[idx++] = v;
/*  756 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4;
/*  762 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  764 */     float[] vertices = this.vertices;
/*      */     
/*  766 */     Texture texture = region.texture;
/*  767 */     if (texture != this.lastTexture) {
/*  768 */       switchTexture(texture);
/*  769 */     } else if (this.idx == vertices.length) {
/*  770 */       flush();
/*      */     } 
/*      */     
/*  773 */     float worldOriginX = x + originX;
/*  774 */     float worldOriginY = y + originY;
/*  775 */     float fx = -originX;
/*  776 */     float fy = -originY;
/*  777 */     float fx2 = width - originX;
/*  778 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  781 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  782 */       fx *= scaleX;
/*  783 */       fy *= scaleY;
/*  784 */       fx2 *= scaleX;
/*  785 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  789 */     float p1x = fx;
/*  790 */     float p1y = fy;
/*  791 */     float p2x = fx;
/*  792 */     float p2y = fy2;
/*  793 */     float p3x = fx2;
/*  794 */     float p3y = fy2;
/*  795 */     float p4x = fx2;
/*  796 */     float p4y = fy;
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
/*  808 */     if (rotation != 0.0F) {
/*  809 */       float cos = MathUtils.cosDeg(rotation);
/*  810 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  812 */       x1 = cos * p1x - sin * p1y;
/*  813 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  815 */       x2 = cos * p2x - sin * p2y;
/*  816 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  818 */       x3 = cos * p3x - sin * p3y;
/*  819 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  821 */       x4 = x1 + x3 - x2;
/*  822 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  824 */       x1 = p1x;
/*  825 */       y1 = p1y;
/*      */       
/*  827 */       x2 = p2x;
/*  828 */       y2 = p2y;
/*      */       
/*  830 */       x3 = p3x;
/*  831 */       y3 = p3y;
/*      */       
/*  833 */       x4 = p4x;
/*  834 */       y4 = p4y;
/*      */     } 
/*      */     
/*  837 */     x1 += worldOriginX;
/*  838 */     y1 += worldOriginY;
/*  839 */     x2 += worldOriginX;
/*  840 */     y2 += worldOriginY;
/*  841 */     x3 += worldOriginX;
/*  842 */     y3 += worldOriginY;
/*  843 */     x4 += worldOriginX;
/*  844 */     y4 += worldOriginY;
/*      */ 
/*      */     
/*  847 */     if (clockwise) {
/*  848 */       u1 = region.u2;
/*  849 */       v1 = region.v2;
/*  850 */       u2 = region.u;
/*  851 */       v2 = region.v2;
/*  852 */       u3 = region.u;
/*  853 */       v3 = region.v;
/*  854 */       u4 = region.u2;
/*  855 */       v4 = region.v;
/*      */     } else {
/*  857 */       u1 = region.u;
/*  858 */       v1 = region.v;
/*  859 */       u2 = region.u2;
/*  860 */       v2 = region.v;
/*  861 */       u3 = region.u2;
/*  862 */       v3 = region.v2;
/*  863 */       u4 = region.u;
/*  864 */       v4 = region.v2;
/*      */     } 
/*      */     
/*  867 */     float color = this.color;
/*  868 */     int idx = this.idx;
/*  869 */     vertices[idx++] = x1;
/*  870 */     vertices[idx++] = y1;
/*  871 */     vertices[idx++] = color;
/*  872 */     vertices[idx++] = u1;
/*  873 */     vertices[idx++] = v1;
/*      */     
/*  875 */     vertices[idx++] = x2;
/*  876 */     vertices[idx++] = y2;
/*  877 */     vertices[idx++] = color;
/*  878 */     vertices[idx++] = u2;
/*  879 */     vertices[idx++] = v2;
/*      */     
/*  881 */     vertices[idx++] = x3;
/*  882 */     vertices[idx++] = y3;
/*  883 */     vertices[idx++] = color;
/*  884 */     vertices[idx++] = u3;
/*  885 */     vertices[idx++] = v3;
/*      */     
/*  887 */     vertices[idx++] = x4;
/*  888 */     vertices[idx++] = y4;
/*  889 */     vertices[idx++] = color;
/*  890 */     vertices[idx++] = u4;
/*  891 */     vertices[idx++] = v4;
/*  892 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float width, float height, Affine2 transform) {
/*  897 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  899 */     float[] vertices = this.vertices;
/*      */     
/*  901 */     Texture texture = region.texture;
/*  902 */     if (texture != this.lastTexture) {
/*  903 */       switchTexture(texture);
/*  904 */     } else if (this.idx == vertices.length) {
/*  905 */       flush();
/*      */     } 
/*      */ 
/*      */     
/*  909 */     float x1 = transform.m02;
/*  910 */     float y1 = transform.m12;
/*  911 */     float x2 = transform.m01 * height + transform.m02;
/*  912 */     float y2 = transform.m11 * height + transform.m12;
/*  913 */     float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
/*  914 */     float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
/*  915 */     float x4 = transform.m00 * width + transform.m02;
/*  916 */     float y4 = transform.m10 * width + transform.m12;
/*      */     
/*  918 */     float u = region.u;
/*  919 */     float v = region.v2;
/*  920 */     float u2 = region.u2;
/*  921 */     float v2 = region.v;
/*      */     
/*  923 */     float color = this.color;
/*  924 */     int idx = this.idx;
/*  925 */     vertices[idx++] = x1;
/*  926 */     vertices[idx++] = y1;
/*  927 */     vertices[idx++] = color;
/*  928 */     vertices[idx++] = u;
/*  929 */     vertices[idx++] = v;
/*      */     
/*  931 */     vertices[idx++] = x2;
/*  932 */     vertices[idx++] = y2;
/*  933 */     vertices[idx++] = color;
/*  934 */     vertices[idx++] = u;
/*  935 */     vertices[idx++] = v2;
/*      */     
/*  937 */     vertices[idx++] = x3;
/*  938 */     vertices[idx++] = y3;
/*  939 */     vertices[idx++] = color;
/*  940 */     vertices[idx++] = u2;
/*  941 */     vertices[idx++] = v2;
/*      */     
/*  943 */     vertices[idx++] = x4;
/*  944 */     vertices[idx++] = y4;
/*  945 */     vertices[idx++] = color;
/*  946 */     vertices[idx++] = u2;
/*  947 */     vertices[idx++] = v;
/*  948 */     this.idx = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() {
/*  953 */     if (this.idx == 0)
/*      */       return; 
/*  955 */     this.renderCalls++;
/*  956 */     this.totalRenderCalls++;
/*  957 */     int spritesInBatch = this.idx / 20;
/*  958 */     if (spritesInBatch > this.maxSpritesInBatch) this.maxSpritesInBatch = spritesInBatch; 
/*  959 */     int count = spritesInBatch * 6;
/*      */     
/*  961 */     this.lastTexture.bind();
/*  962 */     Mesh mesh = this.mesh;
/*  963 */     mesh.setVertices(this.vertices, 0, this.idx);
/*  964 */     mesh.getIndicesBuffer().position(0);
/*  965 */     mesh.getIndicesBuffer().limit(count);
/*      */     
/*  967 */     if (this.blendingDisabled) {
/*  968 */       Gdx.gl.glDisable(3042);
/*      */     } else {
/*  970 */       Gdx.gl.glEnable(3042);
/*  971 */       if (this.blendSrcFunc != -1) Gdx.gl.glBlendFunc(this.blendSrcFunc, this.blendDstFunc);
/*      */     
/*      */     } 
/*  974 */     mesh.render((this.customShader != null) ? this.customShader : this.shader, 4, 0, count);
/*      */     
/*  976 */     this.idx = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void disableBlending() {
/*  981 */     if (this.blendingDisabled)
/*  982 */       return;  flush();
/*  983 */     this.blendingDisabled = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enableBlending() {
/*  988 */     if (!this.blendingDisabled)
/*  989 */       return;  flush();
/*  990 */     this.blendingDisabled = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlendFunction(int srcFunc, int dstFunc) {
/*  995 */     if (this.blendSrcFunc == srcFunc && this.blendDstFunc == dstFunc)
/*  996 */       return;  flush();
/*  997 */     this.blendSrcFunc = srcFunc;
/*  998 */     this.blendDstFunc = dstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendSrcFunc() {
/* 1003 */     return this.blendSrcFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendDstFunc() {
/* 1008 */     return this.blendDstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1013 */     this.mesh.dispose();
/* 1014 */     if (this.ownsShader && this.shader != null) this.shader.dispose();
/*      */   
/*      */   }
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/* 1019 */     return this.projectionMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/* 1024 */     return this.transformMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 projection) {
/* 1029 */     if (this.drawing) flush(); 
/* 1030 */     this.projectionMatrix.set(projection);
/* 1031 */     if (this.drawing) setupMatrices();
/*      */   
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 transform) {
/* 1036 */     if (this.drawing) flush(); 
/* 1037 */     this.transformMatrix.set(transform);
/* 1038 */     if (this.drawing) setupMatrices(); 
/*      */   }
/*      */   
/*      */   private void setupMatrices() {
/* 1042 */     this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
/* 1043 */     if (this.customShader != null) {
/* 1044 */       this.customShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1045 */       this.customShader.setUniformi("u_texture", 0);
/*      */     } else {
/* 1047 */       this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1048 */       this.shader.setUniformi("u_texture", 0);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void switchTexture(Texture texture) {
/* 1053 */     flush();
/* 1054 */     this.lastTexture = texture;
/* 1055 */     this.invTexWidth = 1.0F / texture.getWidth();
/* 1056 */     this.invTexHeight = 1.0F / texture.getHeight();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShader(ShaderProgram shader) {
/* 1061 */     if (this.drawing) {
/* 1062 */       flush();
/* 1063 */       if (this.customShader != null) {
/* 1064 */         this.customShader.end();
/*      */       } else {
/* 1066 */         this.shader.end();
/*      */       } 
/* 1068 */     }  this.customShader = shader;
/* 1069 */     if (this.drawing) {
/* 1070 */       if (this.customShader != null) {
/* 1071 */         this.customShader.begin();
/*      */       } else {
/* 1073 */         this.shader.begin();
/* 1074 */       }  setupMatrices();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ShaderProgram getShader() {
/* 1080 */     if (this.customShader == null) {
/* 1081 */       return this.shader;
/*      */     }
/* 1083 */     return this.customShader;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlendingEnabled() {
/* 1088 */     return !this.blendingDisabled;
/*      */   }
/*      */   
/*      */   public boolean isDrawing() {
/* 1092 */     return this.drawing;
/*      */   }
/*      */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g2d\SpriteBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */