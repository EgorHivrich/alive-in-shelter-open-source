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
/*      */ public class PolygonSpriteBatch
/*      */   implements Batch
/*      */ {
/*      */   private Mesh mesh;
/*      */   private final float[] vertices;
/*      */   private final short[] triangles;
/*      */   private int vertexIndex;
/*      */   private int triangleIndex;
/*      */   private Texture lastTexture;
/*   67 */   private float invTexWidth = 0.0F; private float invTexHeight = 0.0F;
/*      */   
/*      */   private boolean drawing;
/*   70 */   private final Matrix4 transformMatrix = new Matrix4();
/*   71 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   72 */   private final Matrix4 combinedMatrix = new Matrix4();
/*      */   
/*      */   private boolean blendingDisabled;
/*   75 */   private int blendSrcFunc = 770;
/*   76 */   private int blendDstFunc = 771;
/*      */   
/*      */   private final ShaderProgram shader;
/*      */   
/*      */   private ShaderProgram customShader;
/*      */   private boolean ownsShader;
/*   82 */   float color = Color.WHITE.toFloatBits();
/*   83 */   private Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */ 
/*      */   
/*   86 */   public int renderCalls = 0;
/*      */ 
/*      */   
/*   89 */   public int totalRenderCalls = 0;
/*      */ 
/*      */   
/*   92 */   public int maxTrianglesInBatch = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public PolygonSpriteBatch() {
/*   97 */     this(2000, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PolygonSpriteBatch(int size) {
/*  103 */     this(size, null);
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
/*      */   
/*      */   public PolygonSpriteBatch(int size, ShaderProgram defaultShader) {
/*  117 */     if (size > 10920) throw new IllegalArgumentException("Can't have more than 10920 triangles per batch: " + size);
/*      */     
/*  119 */     Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
/*  120 */     if (Gdx.gl30 != null) {
/*  121 */       vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
/*      */     }
/*  123 */     this.mesh = new Mesh(vertexDataType, false, size, size * 3, new VertexAttribute[] { new VertexAttribute(1, 2, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0") });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  128 */     this.vertices = new float[size * 5];
/*  129 */     this.triangles = new short[size * 3];
/*      */     
/*  131 */     if (defaultShader == null) {
/*  132 */       this.shader = SpriteBatch.createDefaultShader();
/*  133 */       this.ownsShader = true;
/*      */     } else {
/*  135 */       this.shader = defaultShader;
/*      */     } 
/*  137 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin() {
/*  142 */     if (this.drawing) throw new IllegalStateException("PolygonSpriteBatch.end must be called before begin."); 
/*  143 */     this.renderCalls = 0;
/*      */     
/*  145 */     Gdx.gl.glDepthMask(false);
/*  146 */     if (this.customShader != null) {
/*  147 */       this.customShader.begin();
/*      */     } else {
/*  149 */       this.shader.begin();
/*  150 */     }  setupMatrices();
/*      */     
/*  152 */     this.drawing = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/*  157 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before end."); 
/*  158 */     if (this.vertexIndex > 0) flush(); 
/*  159 */     this.lastTexture = null;
/*  160 */     this.drawing = false;
/*      */     
/*  162 */     GL20 gl = Gdx.gl;
/*  163 */     gl.glDepthMask(true);
/*  164 */     if (isBlendingEnabled()) gl.glDisable(3042);
/*      */     
/*  166 */     if (this.customShader != null) {
/*  167 */       this.customShader.end();
/*      */     } else {
/*  169 */       this.shader.end();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setColor(Color tint) {
/*  174 */     this.color = tint.toFloatBits();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  179 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/*  180 */     this.color = NumberUtils.intToFloatColor(intBits);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float color) {
/*  185 */     this.color = color;
/*      */   }
/*      */ 
/*      */   
/*      */   public Color getColor() {
/*  190 */     int intBits = NumberUtils.floatToIntColor(this.color);
/*  191 */     Color color = this.tempColor;
/*  192 */     color.r = (intBits & 0xFF) / 255.0F;
/*  193 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/*  194 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/*  195 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/*  196 */     return color;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPackedColor() {
/*  201 */     return this.color;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(PolygonRegion region, float x, float y) {
/*  206 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  208 */     short[] triangles = this.triangles;
/*  209 */     short[] regionTriangles = region.triangles;
/*  210 */     int regionTrianglesLength = regionTriangles.length;
/*  211 */     float[] regionVertices = region.vertices;
/*  212 */     int regionVerticesLength = regionVertices.length;
/*      */     
/*  214 */     Texture texture = region.region.texture;
/*  215 */     if (texture != this.lastTexture) {
/*  216 */       switchTexture(texture);
/*  217 */     } else if (this.triangleIndex + regionTrianglesLength > triangles.length || this.vertexIndex + regionVerticesLength * 5 / 2 > this.vertices.length) {
/*  218 */       flush();
/*      */     } 
/*  220 */     int triangleIndex = this.triangleIndex;
/*  221 */     int vertexIndex = this.vertexIndex;
/*  222 */     int startVertex = vertexIndex / 5;
/*      */     
/*  224 */     for (int i = 0; i < regionTrianglesLength; i++)
/*  225 */       triangles[triangleIndex++] = (short)(regionTriangles[i] + startVertex); 
/*  226 */     this.triangleIndex = triangleIndex;
/*      */     
/*  228 */     float[] vertices = this.vertices;
/*  229 */     float color = this.color;
/*  230 */     float[] textureCoords = region.textureCoords;
/*      */     
/*  232 */     for (int j = 0; j < regionVerticesLength; j += 2) {
/*  233 */       vertices[vertexIndex++] = regionVertices[j] + x;
/*  234 */       vertices[vertexIndex++] = regionVertices[j + 1] + y;
/*  235 */       vertices[vertexIndex++] = color;
/*  236 */       vertices[vertexIndex++] = textureCoords[j];
/*  237 */       vertices[vertexIndex++] = textureCoords[j + 1];
/*      */     } 
/*  239 */     this.vertexIndex = vertexIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(PolygonRegion region, float x, float y, float width, float height) {
/*  244 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  246 */     short[] triangles = this.triangles;
/*  247 */     short[] regionTriangles = region.triangles;
/*  248 */     int regionTrianglesLength = regionTriangles.length;
/*  249 */     float[] regionVertices = region.vertices;
/*  250 */     int regionVerticesLength = regionVertices.length;
/*  251 */     TextureRegion textureRegion = region.region;
/*      */     
/*  253 */     Texture texture = textureRegion.texture;
/*  254 */     if (texture != this.lastTexture) {
/*  255 */       switchTexture(texture);
/*  256 */     } else if (this.triangleIndex + regionTrianglesLength > triangles.length || this.vertexIndex + regionVerticesLength * 5 / 2 > this.vertices.length) {
/*  257 */       flush();
/*      */     } 
/*  259 */     int triangleIndex = this.triangleIndex;
/*  260 */     int vertexIndex = this.vertexIndex;
/*  261 */     int startVertex = vertexIndex / 5;
/*      */     
/*  263 */     for (int i = 0, n = regionTriangles.length; i < n; i++)
/*  264 */       triangles[triangleIndex++] = (short)(regionTriangles[i] + startVertex); 
/*  265 */     this.triangleIndex = triangleIndex;
/*      */     
/*  267 */     float[] vertices = this.vertices;
/*  268 */     float color = this.color;
/*  269 */     float[] textureCoords = region.textureCoords;
/*  270 */     float sX = width / textureRegion.regionWidth;
/*  271 */     float sY = height / textureRegion.regionHeight;
/*      */     
/*  273 */     for (int j = 0; j < regionVerticesLength; j += 2) {
/*  274 */       vertices[vertexIndex++] = regionVertices[j] * sX + x;
/*  275 */       vertices[vertexIndex++] = regionVertices[j + 1] * sY + y;
/*  276 */       vertices[vertexIndex++] = color;
/*  277 */       vertices[vertexIndex++] = textureCoords[j];
/*  278 */       vertices[vertexIndex++] = textureCoords[j + 1];
/*      */     } 
/*  280 */     this.vertexIndex = vertexIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(PolygonRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*  289 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  291 */     short[] triangles = this.triangles;
/*  292 */     short[] regionTriangles = region.triangles;
/*  293 */     int regionTrianglesLength = regionTriangles.length;
/*  294 */     float[] regionVertices = region.vertices;
/*  295 */     int regionVerticesLength = regionVertices.length;
/*  296 */     TextureRegion textureRegion = region.region;
/*      */     
/*  298 */     Texture texture = textureRegion.texture;
/*  299 */     if (texture != this.lastTexture) {
/*  300 */       switchTexture(texture);
/*  301 */     } else if (this.triangleIndex + regionTrianglesLength > triangles.length || this.vertexIndex + regionVerticesLength * 5 / 2 > this.vertices.length) {
/*  302 */       flush();
/*      */     } 
/*  304 */     int triangleIndex = this.triangleIndex;
/*  305 */     int vertexIndex = this.vertexIndex;
/*  306 */     int startVertex = vertexIndex / 5;
/*      */     
/*  308 */     for (int i = 0; i < regionTrianglesLength; i++)
/*  309 */       triangles[triangleIndex++] = (short)(regionTriangles[i] + startVertex); 
/*  310 */     this.triangleIndex = triangleIndex;
/*      */     
/*  312 */     float[] vertices = this.vertices;
/*  313 */     float color = this.color;
/*  314 */     float[] textureCoords = region.textureCoords;
/*      */     
/*  316 */     float worldOriginX = x + originX;
/*  317 */     float worldOriginY = y + originY;
/*  318 */     float sX = width / textureRegion.regionWidth;
/*  319 */     float sY = height / textureRegion.regionHeight;
/*  320 */     float cos = MathUtils.cosDeg(rotation);
/*  321 */     float sin = MathUtils.sinDeg(rotation);
/*      */ 
/*      */     
/*  324 */     for (int j = 0; j < regionVerticesLength; j += 2) {
/*  325 */       float fx = (regionVertices[j] * sX - originX) * scaleX;
/*  326 */       float fy = (regionVertices[j + 1] * sY - originY) * scaleY;
/*  327 */       vertices[vertexIndex++] = cos * fx - sin * fy + worldOriginX;
/*  328 */       vertices[vertexIndex++] = sin * fx + cos * fy + worldOriginY;
/*  329 */       vertices[vertexIndex++] = color;
/*  330 */       vertices[vertexIndex++] = textureCoords[j];
/*  331 */       vertices[vertexIndex++] = textureCoords[j + 1];
/*      */     } 
/*  333 */     this.vertexIndex = vertexIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float[] polygonVertices, int verticesOffset, int verticesCount, short[] polygonTriangles, int trianglesOffset, int trianglesCount) {
/*  340 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  342 */     short[] triangles = this.triangles;
/*  343 */     float[] vertices = this.vertices;
/*      */     
/*  345 */     if (texture != this.lastTexture) {
/*  346 */       switchTexture(texture);
/*  347 */     } else if (this.triangleIndex + trianglesCount > triangles.length || this.vertexIndex + verticesCount > vertices.length) {
/*  348 */       flush();
/*      */     } 
/*  350 */     int triangleIndex = this.triangleIndex;
/*  351 */     int vertexIndex = this.vertexIndex;
/*  352 */     int startVertex = vertexIndex / 5;
/*      */     
/*  354 */     for (int i = trianglesOffset, n = i + trianglesCount; i < n; i++)
/*  355 */       triangles[triangleIndex++] = (short)(polygonTriangles[i] + startVertex); 
/*  356 */     this.triangleIndex = triangleIndex;
/*      */     
/*  358 */     System.arraycopy(polygonVertices, verticesOffset, vertices, vertexIndex, verticesCount);
/*  359 */     this.vertexIndex += verticesCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  365 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  367 */     short[] triangles = this.triangles;
/*  368 */     float[] vertices = this.vertices;
/*      */     
/*  370 */     if (texture != this.lastTexture) {
/*  371 */       switchTexture(texture);
/*  372 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  373 */       flush();
/*      */     } 
/*  375 */     int triangleIndex = this.triangleIndex;
/*  376 */     int startVertex = this.vertexIndex / 5;
/*  377 */     triangles[triangleIndex++] = (short)startVertex;
/*  378 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  379 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  380 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  381 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  382 */     triangles[triangleIndex++] = (short)startVertex;
/*  383 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/*  386 */     float worldOriginX = x + originX;
/*  387 */     float worldOriginY = y + originY;
/*  388 */     float fx = -originX;
/*  389 */     float fy = -originY;
/*  390 */     float fx2 = width - originX;
/*  391 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  394 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  395 */       fx *= scaleX;
/*  396 */       fy *= scaleY;
/*  397 */       fx2 *= scaleX;
/*  398 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  402 */     float p1x = fx;
/*  403 */     float p1y = fy;
/*  404 */     float p2x = fx;
/*  405 */     float p2y = fy2;
/*  406 */     float p3x = fx2;
/*  407 */     float p3y = fy2;
/*  408 */     float p4x = fx2;
/*  409 */     float p4y = fy;
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
/*  421 */     if (rotation != 0.0F) {
/*  422 */       float cos = MathUtils.cosDeg(rotation);
/*  423 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  425 */       x1 = cos * p1x - sin * p1y;
/*  426 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  428 */       x2 = cos * p2x - sin * p2y;
/*  429 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  431 */       x3 = cos * p3x - sin * p3y;
/*  432 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  434 */       x4 = x1 + x3 - x2;
/*  435 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  437 */       x1 = p1x;
/*  438 */       y1 = p1y;
/*      */       
/*  440 */       x2 = p2x;
/*  441 */       y2 = p2y;
/*      */       
/*  443 */       x3 = p3x;
/*  444 */       y3 = p3y;
/*      */       
/*  446 */       x4 = p4x;
/*  447 */       y4 = p4y;
/*      */     } 
/*      */     
/*  450 */     x1 += worldOriginX;
/*  451 */     y1 += worldOriginY;
/*  452 */     x2 += worldOriginX;
/*  453 */     y2 += worldOriginY;
/*  454 */     x3 += worldOriginX;
/*  455 */     y3 += worldOriginY;
/*  456 */     x4 += worldOriginX;
/*  457 */     y4 += worldOriginY;
/*      */     
/*  459 */     float u = srcX * this.invTexWidth;
/*  460 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  461 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  462 */     float v2 = srcY * this.invTexHeight;
/*      */     
/*  464 */     if (flipX) {
/*  465 */       float tmp = u;
/*  466 */       u = u2;
/*  467 */       u2 = tmp;
/*      */     } 
/*      */     
/*  470 */     if (flipY) {
/*  471 */       float tmp = v;
/*  472 */       v = v2;
/*  473 */       v2 = tmp;
/*      */     } 
/*      */     
/*  476 */     float color = this.color;
/*  477 */     int idx = this.vertexIndex;
/*  478 */     vertices[idx++] = x1;
/*  479 */     vertices[idx++] = y1;
/*  480 */     vertices[idx++] = color;
/*  481 */     vertices[idx++] = u;
/*  482 */     vertices[idx++] = v;
/*      */     
/*  484 */     vertices[idx++] = x2;
/*  485 */     vertices[idx++] = y2;
/*  486 */     vertices[idx++] = color;
/*  487 */     vertices[idx++] = u;
/*  488 */     vertices[idx++] = v2;
/*      */     
/*  490 */     vertices[idx++] = x3;
/*  491 */     vertices[idx++] = y3;
/*  492 */     vertices[idx++] = color;
/*  493 */     vertices[idx++] = u2;
/*  494 */     vertices[idx++] = v2;
/*      */     
/*  496 */     vertices[idx++] = x4;
/*  497 */     vertices[idx++] = y4;
/*  498 */     vertices[idx++] = color;
/*  499 */     vertices[idx++] = u2;
/*  500 */     vertices[idx++] = v;
/*  501 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*  507 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  509 */     short[] triangles = this.triangles;
/*  510 */     float[] vertices = this.vertices;
/*      */     
/*  512 */     if (texture != this.lastTexture) {
/*  513 */       switchTexture(texture);
/*  514 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  515 */       flush();
/*      */     } 
/*  517 */     int triangleIndex = this.triangleIndex;
/*  518 */     int startVertex = this.vertexIndex / 5;
/*  519 */     triangles[triangleIndex++] = (short)startVertex;
/*  520 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  521 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  522 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  523 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  524 */     triangles[triangleIndex++] = (short)startVertex;
/*  525 */     this.triangleIndex = triangleIndex;
/*      */     
/*  527 */     float u = srcX * this.invTexWidth;
/*  528 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  529 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  530 */     float v2 = srcY * this.invTexHeight;
/*  531 */     float fx2 = x + width;
/*  532 */     float fy2 = y + height;
/*      */     
/*  534 */     if (flipX) {
/*  535 */       float tmp = u;
/*  536 */       u = u2;
/*  537 */       u2 = tmp;
/*      */     } 
/*      */     
/*  540 */     if (flipY) {
/*  541 */       float tmp = v;
/*  542 */       v = v2;
/*  543 */       v2 = tmp;
/*      */     } 
/*      */     
/*  546 */     float color = this.color;
/*  547 */     int idx = this.vertexIndex;
/*  548 */     vertices[idx++] = x;
/*  549 */     vertices[idx++] = y;
/*  550 */     vertices[idx++] = color;
/*  551 */     vertices[idx++] = u;
/*  552 */     vertices[idx++] = v;
/*      */     
/*  554 */     vertices[idx++] = x;
/*  555 */     vertices[idx++] = fy2;
/*  556 */     vertices[idx++] = color;
/*  557 */     vertices[idx++] = u;
/*  558 */     vertices[idx++] = v2;
/*      */     
/*  560 */     vertices[idx++] = fx2;
/*  561 */     vertices[idx++] = fy2;
/*  562 */     vertices[idx++] = color;
/*  563 */     vertices[idx++] = u2;
/*  564 */     vertices[idx++] = v2;
/*      */     
/*  566 */     vertices[idx++] = fx2;
/*  567 */     vertices[idx++] = y;
/*  568 */     vertices[idx++] = color;
/*  569 */     vertices[idx++] = u2;
/*  570 */     vertices[idx++] = v;
/*  571 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  576 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  578 */     short[] triangles = this.triangles;
/*  579 */     float[] vertices = this.vertices;
/*      */     
/*  581 */     if (texture != this.lastTexture) {
/*  582 */       switchTexture(texture);
/*  583 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  584 */       flush();
/*      */     } 
/*  586 */     int triangleIndex = this.triangleIndex;
/*  587 */     int startVertex = this.vertexIndex / 5;
/*  588 */     triangles[triangleIndex++] = (short)startVertex;
/*  589 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  590 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  591 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  592 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  593 */     triangles[triangleIndex++] = (short)startVertex;
/*  594 */     this.triangleIndex = triangleIndex;
/*      */     
/*  596 */     float u = srcX * this.invTexWidth;
/*  597 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  598 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  599 */     float v2 = srcY * this.invTexHeight;
/*  600 */     float fx2 = x + srcWidth;
/*  601 */     float fy2 = y + srcHeight;
/*      */     
/*  603 */     float color = this.color;
/*  604 */     int idx = this.vertexIndex;
/*  605 */     vertices[idx++] = x;
/*  606 */     vertices[idx++] = y;
/*  607 */     vertices[idx++] = color;
/*  608 */     vertices[idx++] = u;
/*  609 */     vertices[idx++] = v;
/*      */     
/*  611 */     vertices[idx++] = x;
/*  612 */     vertices[idx++] = fy2;
/*  613 */     vertices[idx++] = color;
/*  614 */     vertices[idx++] = u;
/*  615 */     vertices[idx++] = v2;
/*      */     
/*  617 */     vertices[idx++] = fx2;
/*  618 */     vertices[idx++] = fy2;
/*  619 */     vertices[idx++] = color;
/*  620 */     vertices[idx++] = u2;
/*  621 */     vertices[idx++] = v2;
/*      */     
/*  623 */     vertices[idx++] = fx2;
/*  624 */     vertices[idx++] = y;
/*  625 */     vertices[idx++] = color;
/*  626 */     vertices[idx++] = u2;
/*  627 */     vertices[idx++] = v;
/*  628 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
/*  633 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  635 */     short[] triangles = this.triangles;
/*  636 */     float[] vertices = this.vertices;
/*      */     
/*  638 */     if (texture != this.lastTexture) {
/*  639 */       switchTexture(texture);
/*  640 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  641 */       flush();
/*      */     } 
/*  643 */     int triangleIndex = this.triangleIndex;
/*  644 */     int startVertex = this.vertexIndex / 5;
/*  645 */     triangles[triangleIndex++] = (short)startVertex;
/*  646 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  647 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  648 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  649 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  650 */     triangles[triangleIndex++] = (short)startVertex;
/*  651 */     this.triangleIndex = triangleIndex;
/*      */     
/*  653 */     float fx2 = x + width;
/*  654 */     float fy2 = y + height;
/*      */     
/*  656 */     float color = this.color;
/*  657 */     int idx = this.vertexIndex;
/*  658 */     vertices[idx++] = x;
/*  659 */     vertices[idx++] = y;
/*  660 */     vertices[idx++] = color;
/*  661 */     vertices[idx++] = u;
/*  662 */     vertices[idx++] = v;
/*      */     
/*  664 */     vertices[idx++] = x;
/*  665 */     vertices[idx++] = fy2;
/*  666 */     vertices[idx++] = color;
/*  667 */     vertices[idx++] = u;
/*  668 */     vertices[idx++] = v2;
/*      */     
/*  670 */     vertices[idx++] = fx2;
/*  671 */     vertices[idx++] = fy2;
/*  672 */     vertices[idx++] = color;
/*  673 */     vertices[idx++] = u2;
/*  674 */     vertices[idx++] = v2;
/*      */     
/*  676 */     vertices[idx++] = fx2;
/*  677 */     vertices[idx++] = y;
/*  678 */     vertices[idx++] = color;
/*  679 */     vertices[idx++] = u2;
/*  680 */     vertices[idx++] = v;
/*  681 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y) {
/*  686 */     draw(texture, x, y, texture.getWidth(), texture.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height) {
/*  691 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  693 */     short[] triangles = this.triangles;
/*  694 */     float[] vertices = this.vertices;
/*      */     
/*  696 */     if (texture != this.lastTexture) {
/*  697 */       switchTexture(texture);
/*  698 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  699 */       flush();
/*      */     } 
/*  701 */     int triangleIndex = this.triangleIndex;
/*  702 */     int startVertex = this.vertexIndex / 5;
/*  703 */     triangles[triangleIndex++] = (short)startVertex;
/*  704 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  705 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  706 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  707 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  708 */     triangles[triangleIndex++] = (short)startVertex;
/*  709 */     this.triangleIndex = triangleIndex;
/*      */     
/*  711 */     float fx2 = x + width;
/*  712 */     float fy2 = y + height;
/*  713 */     float u = 0.0F;
/*  714 */     float v = 1.0F;
/*  715 */     float u2 = 1.0F;
/*  716 */     float v2 = 0.0F;
/*      */     
/*  718 */     float color = this.color;
/*  719 */     int idx = this.vertexIndex;
/*  720 */     vertices[idx++] = x;
/*  721 */     vertices[idx++] = y;
/*  722 */     vertices[idx++] = color;
/*  723 */     vertices[idx++] = 0.0F;
/*  724 */     vertices[idx++] = 1.0F;
/*      */     
/*  726 */     vertices[idx++] = x;
/*  727 */     vertices[idx++] = fy2;
/*  728 */     vertices[idx++] = color;
/*  729 */     vertices[idx++] = 0.0F;
/*  730 */     vertices[idx++] = 0.0F;
/*      */     
/*  732 */     vertices[idx++] = fx2;
/*  733 */     vertices[idx++] = fy2;
/*  734 */     vertices[idx++] = color;
/*  735 */     vertices[idx++] = 1.0F;
/*  736 */     vertices[idx++] = 0.0F;
/*      */     
/*  738 */     vertices[idx++] = fx2;
/*  739 */     vertices[idx++] = y;
/*  740 */     vertices[idx++] = color;
/*  741 */     vertices[idx++] = 1.0F;
/*  742 */     vertices[idx++] = 1.0F;
/*  743 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
/*  748 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  750 */     short[] triangles = this.triangles;
/*  751 */     float[] vertices = this.vertices;
/*      */     
/*  753 */     int triangleCount = count / 20 * 6;
/*  754 */     if (texture != this.lastTexture) {
/*  755 */       switchTexture(texture);
/*  756 */     } else if (this.triangleIndex + triangleCount > triangles.length || this.vertexIndex + count > vertices.length) {
/*  757 */       flush();
/*      */     } 
/*  759 */     int vertexIndex = this.vertexIndex;
/*  760 */     int triangleIndex = this.triangleIndex;
/*  761 */     short vertex = (short)(vertexIndex / 5);
/*  762 */     for (int n = triangleIndex + triangleCount; triangleIndex < n; triangleIndex += 6, vertex = (short)(vertex + 4)) {
/*  763 */       triangles[triangleIndex] = vertex;
/*  764 */       triangles[triangleIndex + 1] = (short)(vertex + 1);
/*  765 */       triangles[triangleIndex + 2] = (short)(vertex + 2);
/*  766 */       triangles[triangleIndex + 3] = (short)(vertex + 2);
/*  767 */       triangles[triangleIndex + 4] = (short)(vertex + 3);
/*  768 */       triangles[triangleIndex + 5] = vertex;
/*      */     } 
/*  770 */     this.triangleIndex = triangleIndex;
/*      */     
/*  772 */     System.arraycopy(spriteVertices, offset, vertices, vertexIndex, count);
/*  773 */     this.vertexIndex += count;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y) {
/*  778 */     draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float width, float height) {
/*  783 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  785 */     short[] triangles = this.triangles;
/*  786 */     float[] vertices = this.vertices;
/*      */     
/*  788 */     Texture texture = region.texture;
/*  789 */     if (texture != this.lastTexture) {
/*  790 */       switchTexture(texture);
/*  791 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  792 */       flush();
/*      */     } 
/*  794 */     int triangleIndex = this.triangleIndex;
/*  795 */     int startVertex = this.vertexIndex / 5;
/*  796 */     triangles[triangleIndex++] = (short)startVertex;
/*  797 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  798 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  799 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  800 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  801 */     triangles[triangleIndex++] = (short)startVertex;
/*  802 */     this.triangleIndex = triangleIndex;
/*      */     
/*  804 */     float fx2 = x + width;
/*  805 */     float fy2 = y + height;
/*  806 */     float u = region.u;
/*  807 */     float v = region.v2;
/*  808 */     float u2 = region.u2;
/*  809 */     float v2 = region.v;
/*      */     
/*  811 */     float color = this.color;
/*  812 */     int idx = this.vertexIndex;
/*  813 */     vertices[idx++] = x;
/*  814 */     vertices[idx++] = y;
/*  815 */     vertices[idx++] = color;
/*  816 */     vertices[idx++] = u;
/*  817 */     vertices[idx++] = v;
/*      */     
/*  819 */     vertices[idx++] = x;
/*  820 */     vertices[idx++] = fy2;
/*  821 */     vertices[idx++] = color;
/*  822 */     vertices[idx++] = u;
/*  823 */     vertices[idx++] = v2;
/*      */     
/*  825 */     vertices[idx++] = fx2;
/*  826 */     vertices[idx++] = fy2;
/*  827 */     vertices[idx++] = color;
/*  828 */     vertices[idx++] = u2;
/*  829 */     vertices[idx++] = v2;
/*      */     
/*  831 */     vertices[idx++] = fx2;
/*  832 */     vertices[idx++] = y;
/*  833 */     vertices[idx++] = color;
/*  834 */     vertices[idx++] = u2;
/*  835 */     vertices[idx++] = v;
/*  836 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  842 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  844 */     short[] triangles = this.triangles;
/*  845 */     float[] vertices = this.vertices;
/*      */     
/*  847 */     Texture texture = region.texture;
/*  848 */     if (texture != this.lastTexture) {
/*  849 */       switchTexture(texture);
/*  850 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  851 */       flush();
/*      */     } 
/*  853 */     int triangleIndex = this.triangleIndex;
/*  854 */     int startVertex = this.vertexIndex / 5;
/*  855 */     triangles[triangleIndex++] = (short)startVertex;
/*  856 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  857 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  858 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  859 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  860 */     triangles[triangleIndex++] = (short)startVertex;
/*  861 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/*  864 */     float worldOriginX = x + originX;
/*  865 */     float worldOriginY = y + originY;
/*  866 */     float fx = -originX;
/*  867 */     float fy = -originY;
/*  868 */     float fx2 = width - originX;
/*  869 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  872 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  873 */       fx *= scaleX;
/*  874 */       fy *= scaleY;
/*  875 */       fx2 *= scaleX;
/*  876 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  880 */     float p1x = fx;
/*  881 */     float p1y = fy;
/*  882 */     float p2x = fx;
/*  883 */     float p2y = fy2;
/*  884 */     float p3x = fx2;
/*  885 */     float p3y = fy2;
/*  886 */     float p4x = fx2;
/*  887 */     float p4y = fy;
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
/*  899 */     if (rotation != 0.0F) {
/*  900 */       float cos = MathUtils.cosDeg(rotation);
/*  901 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  903 */       x1 = cos * p1x - sin * p1y;
/*  904 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  906 */       x2 = cos * p2x - sin * p2y;
/*  907 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  909 */       x3 = cos * p3x - sin * p3y;
/*  910 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  912 */       x4 = x1 + x3 - x2;
/*  913 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  915 */       x1 = p1x;
/*  916 */       y1 = p1y;
/*      */       
/*  918 */       x2 = p2x;
/*  919 */       y2 = p2y;
/*      */       
/*  921 */       x3 = p3x;
/*  922 */       y3 = p3y;
/*      */       
/*  924 */       x4 = p4x;
/*  925 */       y4 = p4y;
/*      */     } 
/*      */     
/*  928 */     x1 += worldOriginX;
/*  929 */     y1 += worldOriginY;
/*  930 */     x2 += worldOriginX;
/*  931 */     y2 += worldOriginY;
/*  932 */     x3 += worldOriginX;
/*  933 */     y3 += worldOriginY;
/*  934 */     x4 += worldOriginX;
/*  935 */     y4 += worldOriginY;
/*      */     
/*  937 */     float u = region.u;
/*  938 */     float v = region.v2;
/*  939 */     float u2 = region.u2;
/*  940 */     float v2 = region.v;
/*      */     
/*  942 */     float color = this.color;
/*  943 */     int idx = this.vertexIndex;
/*  944 */     vertices[idx++] = x1;
/*  945 */     vertices[idx++] = y1;
/*  946 */     vertices[idx++] = color;
/*  947 */     vertices[idx++] = u;
/*  948 */     vertices[idx++] = v;
/*      */     
/*  950 */     vertices[idx++] = x2;
/*  951 */     vertices[idx++] = y2;
/*  952 */     vertices[idx++] = color;
/*  953 */     vertices[idx++] = u;
/*  954 */     vertices[idx++] = v2;
/*      */     
/*  956 */     vertices[idx++] = x3;
/*  957 */     vertices[idx++] = y3;
/*  958 */     vertices[idx++] = color;
/*  959 */     vertices[idx++] = u2;
/*  960 */     vertices[idx++] = v2;
/*      */     
/*  962 */     vertices[idx++] = x4;
/*  963 */     vertices[idx++] = y4;
/*  964 */     vertices[idx++] = color;
/*  965 */     vertices[idx++] = u2;
/*  966 */     vertices[idx++] = v;
/*  967 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4;
/*  973 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  975 */     short[] triangles = this.triangles;
/*  976 */     float[] vertices = this.vertices;
/*      */     
/*  978 */     Texture texture = region.texture;
/*  979 */     if (texture != this.lastTexture) {
/*  980 */       switchTexture(texture);
/*  981 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  982 */       flush();
/*      */     } 
/*  984 */     int triangleIndex = this.triangleIndex;
/*  985 */     int startVertex = this.vertexIndex / 5;
/*  986 */     triangles[triangleIndex++] = (short)startVertex;
/*  987 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  988 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  989 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  990 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  991 */     triangles[triangleIndex++] = (short)startVertex;
/*  992 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/*  995 */     float worldOriginX = x + originX;
/*  996 */     float worldOriginY = y + originY;
/*  997 */     float fx = -originX;
/*  998 */     float fy = -originY;
/*  999 */     float fx2 = width - originX;
/* 1000 */     float fy2 = height - originY;
/*      */ 
/*      */     
/* 1003 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/* 1004 */       fx *= scaleX;
/* 1005 */       fy *= scaleY;
/* 1006 */       fx2 *= scaleX;
/* 1007 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/* 1011 */     float p1x = fx;
/* 1012 */     float p1y = fy;
/* 1013 */     float p2x = fx;
/* 1014 */     float p2y = fy2;
/* 1015 */     float p3x = fx2;
/* 1016 */     float p3y = fy2;
/* 1017 */     float p4x = fx2;
/* 1018 */     float p4y = fy;
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
/* 1030 */     if (rotation != 0.0F) {
/* 1031 */       float cos = MathUtils.cosDeg(rotation);
/* 1032 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/* 1034 */       x1 = cos * p1x - sin * p1y;
/* 1035 */       y1 = sin * p1x + cos * p1y;
/*      */       
/* 1037 */       x2 = cos * p2x - sin * p2y;
/* 1038 */       y2 = sin * p2x + cos * p2y;
/*      */       
/* 1040 */       x3 = cos * p3x - sin * p3y;
/* 1041 */       y3 = sin * p3x + cos * p3y;
/*      */       
/* 1043 */       x4 = x1 + x3 - x2;
/* 1044 */       y4 = y3 - y2 - y1;
/*      */     } else {
/* 1046 */       x1 = p1x;
/* 1047 */       y1 = p1y;
/*      */       
/* 1049 */       x2 = p2x;
/* 1050 */       y2 = p2y;
/*      */       
/* 1052 */       x3 = p3x;
/* 1053 */       y3 = p3y;
/*      */       
/* 1055 */       x4 = p4x;
/* 1056 */       y4 = p4y;
/*      */     } 
/*      */     
/* 1059 */     x1 += worldOriginX;
/* 1060 */     y1 += worldOriginY;
/* 1061 */     x2 += worldOriginX;
/* 1062 */     y2 += worldOriginY;
/* 1063 */     x3 += worldOriginX;
/* 1064 */     y3 += worldOriginY;
/* 1065 */     x4 += worldOriginX;
/* 1066 */     y4 += worldOriginY;
/*      */ 
/*      */     
/* 1069 */     if (clockwise) {
/* 1070 */       u1 = region.u2;
/* 1071 */       v1 = region.v2;
/* 1072 */       u2 = region.u;
/* 1073 */       v2 = region.v2;
/* 1074 */       u3 = region.u;
/* 1075 */       v3 = region.v;
/* 1076 */       u4 = region.u2;
/* 1077 */       v4 = region.v;
/*      */     } else {
/* 1079 */       u1 = region.u;
/* 1080 */       v1 = region.v;
/* 1081 */       u2 = region.u2;
/* 1082 */       v2 = region.v;
/* 1083 */       u3 = region.u2;
/* 1084 */       v3 = region.v2;
/* 1085 */       u4 = region.u;
/* 1086 */       v4 = region.v2;
/*      */     } 
/*      */     
/* 1089 */     float color = this.color;
/* 1090 */     int idx = this.vertexIndex;
/* 1091 */     vertices[idx++] = x1;
/* 1092 */     vertices[idx++] = y1;
/* 1093 */     vertices[idx++] = color;
/* 1094 */     vertices[idx++] = u1;
/* 1095 */     vertices[idx++] = v1;
/*      */     
/* 1097 */     vertices[idx++] = x2;
/* 1098 */     vertices[idx++] = y2;
/* 1099 */     vertices[idx++] = color;
/* 1100 */     vertices[idx++] = u2;
/* 1101 */     vertices[idx++] = v2;
/*      */     
/* 1103 */     vertices[idx++] = x3;
/* 1104 */     vertices[idx++] = y3;
/* 1105 */     vertices[idx++] = color;
/* 1106 */     vertices[idx++] = u3;
/* 1107 */     vertices[idx++] = v3;
/*      */     
/* 1109 */     vertices[idx++] = x4;
/* 1110 */     vertices[idx++] = y4;
/* 1111 */     vertices[idx++] = color;
/* 1112 */     vertices[idx++] = u4;
/* 1113 */     vertices[idx++] = v4;
/* 1114 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float width, float height, Affine2 transform) {
/* 1119 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/* 1121 */     short[] triangles = this.triangles;
/* 1122 */     float[] vertices = this.vertices;
/*      */     
/* 1124 */     Texture texture = region.texture;
/* 1125 */     if (texture != this.lastTexture) {
/* 1126 */       switchTexture(texture);
/* 1127 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/* 1128 */       flush();
/*      */     } 
/* 1130 */     int triangleIndex = this.triangleIndex;
/* 1131 */     int startVertex = this.vertexIndex / 5;
/* 1132 */     triangles[triangleIndex++] = (short)startVertex;
/* 1133 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/* 1134 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/* 1135 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/* 1136 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/* 1137 */     triangles[triangleIndex++] = (short)startVertex;
/* 1138 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/* 1141 */     float x1 = transform.m02;
/* 1142 */     float y1 = transform.m12;
/* 1143 */     float x2 = transform.m01 * height + transform.m02;
/* 1144 */     float y2 = transform.m11 * height + transform.m12;
/* 1145 */     float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
/* 1146 */     float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
/* 1147 */     float x4 = transform.m00 * width + transform.m02;
/* 1148 */     float y4 = transform.m10 * width + transform.m12;
/*      */     
/* 1150 */     float u = region.u;
/* 1151 */     float v = region.v2;
/* 1152 */     float u2 = region.u2;
/* 1153 */     float v2 = region.v;
/*      */     
/* 1155 */     float color = this.color;
/* 1156 */     int idx = this.vertexIndex;
/* 1157 */     vertices[idx++] = x1;
/* 1158 */     vertices[idx++] = y1;
/* 1159 */     vertices[idx++] = color;
/* 1160 */     vertices[idx++] = u;
/* 1161 */     vertices[idx++] = v;
/*      */     
/* 1163 */     vertices[idx++] = x2;
/* 1164 */     vertices[idx++] = y2;
/* 1165 */     vertices[idx++] = color;
/* 1166 */     vertices[idx++] = u;
/* 1167 */     vertices[idx++] = v2;
/*      */     
/* 1169 */     vertices[idx++] = x3;
/* 1170 */     vertices[idx++] = y3;
/* 1171 */     vertices[idx++] = color;
/* 1172 */     vertices[idx++] = u2;
/* 1173 */     vertices[idx++] = v2;
/*      */     
/* 1175 */     vertices[idx++] = x4;
/* 1176 */     vertices[idx++] = y4;
/* 1177 */     vertices[idx++] = color;
/* 1178 */     vertices[idx++] = u2;
/* 1179 */     vertices[idx++] = v;
/* 1180 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() {
/* 1185 */     if (this.vertexIndex == 0)
/*      */       return; 
/* 1187 */     this.renderCalls++;
/* 1188 */     this.totalRenderCalls++;
/* 1189 */     int trianglesInBatch = this.triangleIndex;
/* 1190 */     if (trianglesInBatch > this.maxTrianglesInBatch) this.maxTrianglesInBatch = trianglesInBatch;
/*      */     
/* 1192 */     this.lastTexture.bind();
/* 1193 */     Mesh mesh = this.mesh;
/* 1194 */     mesh.setVertices(this.vertices, 0, this.vertexIndex);
/* 1195 */     mesh.setIndices(this.triangles, 0, this.triangleIndex);
/*      */     
/* 1197 */     if (this.blendingDisabled) {
/* 1198 */       Gdx.gl.glDisable(3042);
/*      */     } else {
/* 1200 */       Gdx.gl.glEnable(3042);
/* 1201 */       if (this.blendSrcFunc != -1) Gdx.gl.glBlendFunc(this.blendSrcFunc, this.blendDstFunc);
/*      */     
/*      */     } 
/* 1204 */     mesh.render((this.customShader != null) ? this.customShader : this.shader, 4, 0, trianglesInBatch);
/*      */     
/* 1206 */     this.vertexIndex = 0;
/* 1207 */     this.triangleIndex = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void disableBlending() {
/* 1212 */     flush();
/* 1213 */     this.blendingDisabled = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enableBlending() {
/* 1218 */     flush();
/* 1219 */     this.blendingDisabled = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlendFunction(int srcFunc, int dstFunc) {
/* 1224 */     if (this.blendSrcFunc == srcFunc && this.blendDstFunc == dstFunc)
/* 1225 */       return;  flush();
/* 1226 */     this.blendSrcFunc = srcFunc;
/* 1227 */     this.blendDstFunc = dstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendSrcFunc() {
/* 1232 */     return this.blendSrcFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendDstFunc() {
/* 1237 */     return this.blendDstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1242 */     this.mesh.dispose();
/* 1243 */     if (this.ownsShader && this.shader != null) this.shader.dispose();
/*      */   
/*      */   }
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/* 1248 */     return this.projectionMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/* 1253 */     return this.transformMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 projection) {
/* 1258 */     if (this.drawing) flush(); 
/* 1259 */     this.projectionMatrix.set(projection);
/* 1260 */     if (this.drawing) setupMatrices();
/*      */   
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 transform) {
/* 1265 */     if (this.drawing) flush(); 
/* 1266 */     this.transformMatrix.set(transform);
/* 1267 */     if (this.drawing) setupMatrices(); 
/*      */   }
/*      */   
/*      */   private void setupMatrices() {
/* 1271 */     this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
/* 1272 */     if (this.customShader != null) {
/* 1273 */       this.customShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1274 */       this.customShader.setUniformi("u_texture", 0);
/*      */     } else {
/* 1276 */       this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1277 */       this.shader.setUniformi("u_texture", 0);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void switchTexture(Texture texture) {
/* 1282 */     flush();
/* 1283 */     this.lastTexture = texture;
/* 1284 */     this.invTexWidth = 1.0F / texture.getWidth();
/* 1285 */     this.invTexHeight = 1.0F / texture.getHeight();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShader(ShaderProgram shader) {
/* 1290 */     if (this.drawing) {
/* 1291 */       flush();
/* 1292 */       if (this.customShader != null) {
/* 1293 */         this.customShader.end();
/*      */       } else {
/* 1295 */         this.shader.end();
/*      */       } 
/* 1297 */     }  this.customShader = shader;
/* 1298 */     if (this.drawing) {
/* 1299 */       if (this.customShader != null) {
/* 1300 */         this.customShader.begin();
/*      */       } else {
/* 1302 */         this.shader.begin();
/* 1303 */       }  setupMatrices();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ShaderProgram getShader() {
/* 1309 */     if (this.customShader == null) {
/* 1310 */       return this.shader;
/*      */     }
/* 1312 */     return this.customShader;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlendingEnabled() {
/* 1317 */     return !this.blendingDisabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDrawing() {
/* 1322 */     return this.drawing;
/*      */   }
/*      */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g2d\PolygonSpriteBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */