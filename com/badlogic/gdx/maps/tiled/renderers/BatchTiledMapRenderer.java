/*     */ package com.badlogic.gdx.maps.tiled.renderers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapLayer;
/*     */ import com.badlogic.gdx.maps.MapObject;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.utils.Disposable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BatchTiledMapRenderer
/*     */   implements TiledMapRenderer, Disposable
/*     */ {
/*     */   protected static final int NUM_VERTICES = 20;
/*     */   protected TiledMap map;
/*     */   protected float unitScale;
/*     */   protected Batch batch;
/*     */   protected Rectangle viewBounds;
/*  68 */   protected Rectangle imageBounds = new Rectangle();
/*     */   
/*     */   protected boolean ownsBatch;
/*     */   
/*  72 */   protected float[] vertices = new float[20];
/*     */   
/*     */   public TiledMap getMap() {
/*  75 */     return this.map;
/*     */   }
/*     */   
/*     */   public void setMap(TiledMap map) {
/*  79 */     this.map = map;
/*     */   }
/*     */   
/*     */   public float getUnitScale() {
/*  83 */     return this.unitScale;
/*     */   }
/*     */   
/*     */   public Batch getBatch() {
/*  87 */     return this.batch;
/*     */   }
/*     */   
/*     */   public Rectangle getViewBounds() {
/*  91 */     return this.viewBounds;
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map) {
/*  95 */     this(map, 1.0F);
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map, float unitScale) {
/*  99 */     this.map = map;
/* 100 */     this.unitScale = unitScale;
/* 101 */     this.viewBounds = new Rectangle();
/* 102 */     this.batch = (Batch)new SpriteBatch();
/* 103 */     this.ownsBatch = true;
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map, Batch batch) {
/* 107 */     this(map, 1.0F, batch);
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
/* 111 */     this.map = map;
/* 112 */     this.unitScale = unitScale;
/* 113 */     this.viewBounds = new Rectangle();
/* 114 */     this.batch = batch;
/* 115 */     this.ownsBatch = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setView(OrthographicCamera camera) {
/* 120 */     this.batch.setProjectionMatrix(camera.combined);
/* 121 */     float width = camera.viewportWidth * camera.zoom;
/* 122 */     float height = camera.viewportHeight * camera.zoom;
/* 123 */     this.viewBounds.set(camera.position.x - width / 2.0F, camera.position.y - height / 2.0F, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setView(Matrix4 projection, float x, float y, float width, float height) {
/* 128 */     this.batch.setProjectionMatrix(projection);
/* 129 */     this.viewBounds.set(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/* 134 */     beginRender();
/* 135 */     for (MapLayer layer : this.map.getLayers()) {
/* 136 */       if (layer.isVisible()) {
/* 137 */         if (layer instanceof TiledMapTileLayer)
/* 138 */           renderTileLayer((TiledMapTileLayer)layer); 
/* 139 */         if (layer instanceof TiledMapImageLayer) {
/* 140 */           renderImageLayer((TiledMapImageLayer)layer); continue;
/*     */         } 
/* 142 */         renderObjects(layer);
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     endRender();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int[] layers) {
/* 151 */     beginRender();
/* 152 */     for (int layerIdx : layers) {
/* 153 */       MapLayer layer = this.map.getLayers().get(layerIdx);
/* 154 */       if (layer.isVisible()) {
/* 155 */         if (layer instanceof TiledMapTileLayer) {
/* 156 */           renderTileLayer((TiledMapTileLayer)layer);
/* 157 */         } else if (layer instanceof TiledMapImageLayer) {
/* 158 */           renderImageLayer((TiledMapImageLayer)layer);
/*     */         } else {
/* 160 */           renderObjects(layer);
/*     */         } 
/*     */       }
/*     */     } 
/* 164 */     endRender();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderObjects(MapLayer layer) {
/* 169 */     for (MapObject object : layer.getObjects()) {
/* 170 */       renderObject(object);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderObject(MapObject object) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderImageLayer(TiledMapImageLayer layer) {
/* 181 */     Color batchColor = this.batch.getColor();
/* 182 */     float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer
/*     */ 
/*     */         
/* 185 */         .getOpacity());
/*     */     
/* 187 */     float[] vertices = this.vertices;
/*     */     
/* 189 */     TextureRegion region = layer.getTextureRegion();
/*     */     
/* 191 */     if (region == null) {
/*     */       return;
/*     */     }
/*     */     
/* 195 */     float x = layer.getX();
/* 196 */     float y = layer.getY();
/* 197 */     float x1 = x * this.unitScale;
/* 198 */     float y1 = y * this.unitScale;
/* 199 */     float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 200 */     float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */     
/* 202 */     this.imageBounds.set(x1, y1, x2 - x1, y2 - y1);
/*     */     
/* 204 */     if (this.viewBounds.contains(this.imageBounds) || this.viewBounds.overlaps(this.imageBounds)) {
/* 205 */       float u1 = region.getU();
/* 206 */       float v1 = region.getV2();
/* 207 */       float u2 = region.getU2();
/* 208 */       float v2 = region.getV();
/*     */       
/* 210 */       vertices[0] = x1;
/* 211 */       vertices[1] = y1;
/* 212 */       vertices[2] = color;
/* 213 */       vertices[3] = u1;
/* 214 */       vertices[4] = v1;
/*     */       
/* 216 */       vertices[5] = x1;
/* 217 */       vertices[6] = y2;
/* 218 */       vertices[7] = color;
/* 219 */       vertices[8] = u1;
/* 220 */       vertices[9] = v2;
/*     */       
/* 222 */       vertices[10] = x2;
/* 223 */       vertices[11] = y2;
/* 224 */       vertices[12] = color;
/* 225 */       vertices[13] = u2;
/* 226 */       vertices[14] = v2;
/*     */       
/* 228 */       vertices[15] = x2;
/* 229 */       vertices[16] = y1;
/* 230 */       vertices[17] = color;
/* 231 */       vertices[18] = u2;
/* 232 */       vertices[19] = v1;
/*     */       
/* 234 */       this.batch.draw(region.getTexture(), vertices, 0, 20);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beginRender() {
/* 240 */     AnimatedTiledMapTile.updateAnimationBaseTime();
/* 241 */     this.batch.begin();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endRender() {
/* 246 */     this.batch.end();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 251 */     if (this.ownsBatch)
/* 252 */       this.batch.dispose(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\maps\tiled\renderers\BatchTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */