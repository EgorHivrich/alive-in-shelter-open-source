/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
/*     */ public class Window
/*     */   extends Table
/*     */ {
/*  40 */   private static final Vector2 tmpPosition = new Vector2();
/*  41 */   private static final Vector2 tmpSize = new Vector2(); private static final int MOVE = 32;
/*     */   private WindowStyle style;
/*     */   boolean isMovable = true;
/*     */   boolean isModal;
/*     */   boolean isResizable;
/*  46 */   int resizeBorder = 8;
/*     */   boolean dragging;
/*     */   boolean keepWithinStage = true;
/*     */   Label titleLabel;
/*     */   Table titleTable;
/*     */   boolean drawTitleTable;
/*     */   
/*     */   public Window(String title, Skin skin) {
/*  54 */     this(title, skin.<WindowStyle>get(WindowStyle.class));
/*  55 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public Window(String title, Skin skin, String styleName) {
/*  59 */     this(title, skin.<WindowStyle>get(styleName, WindowStyle.class));
/*  60 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public Window(String title, WindowStyle style) {
/*  64 */     if (title == null) throw new IllegalArgumentException("title cannot be null."); 
/*  65 */     setTouchable(Touchable.enabled);
/*  66 */     setClip(true);
/*     */     
/*  68 */     this.titleLabel = new Label(title, new Label.LabelStyle(style.titleFont, style.titleFontColor));
/*  69 */     this.titleLabel.setEllipsis(true);
/*     */     
/*  71 */     this.titleTable = new Table() {
/*     */         public void draw(Batch batch, float parentAlpha) {
/*  73 */           if (Window.this.drawTitleTable) super.draw(batch, parentAlpha); 
/*     */         }
/*     */       };
/*  76 */     this.titleTable.<Label>add(this.titleLabel).expandX().fillX().minWidth(0.0F);
/*  77 */     addActor((Actor)this.titleTable);
/*     */     
/*  79 */     setStyle(style);
/*  80 */     setWidth(150.0F);
/*  81 */     setHeight(150.0F);
/*     */     
/*  83 */     addCaptureListener((EventListener)new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  85 */             Window.this.toFront();
/*  86 */             return false;
/*     */           }
/*     */         });
/*  89 */     addListener((EventListener)new InputListener() {
/*     */           int edge;
/*     */           float startX;
/*     */           
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  94 */             if (button == 0) {
/*  95 */               int border = Window.this.resizeBorder;
/*  96 */               float width = Window.this.getWidth(), height = Window.this.getHeight();
/*  97 */               this.edge = 0;
/*  98 */               if (Window.this.isResizable && x >= 0.0F && x < width && y >= 0.0F && y < height) {
/*  99 */                 if (x < border) this.edge |= 0x8; 
/* 100 */                 if (x > width - border) this.edge |= 0x10; 
/* 101 */                 if (y < border) this.edge |= 0x4; 
/* 102 */                 if (y > height - border) this.edge |= 0x2; 
/* 103 */                 if (this.edge != 0) border += 25; 
/* 104 */                 if (x < border) this.edge |= 0x8; 
/* 105 */                 if (x > width - border) this.edge |= 0x10; 
/* 106 */                 if (y < border) this.edge |= 0x4; 
/* 107 */                 if (y > height - border) this.edge |= 0x2; 
/*     */               } 
/* 109 */               if (Window.this.isMovable && this.edge == 0 && y <= height && y >= height - Window.this.getPadTop() && x >= 0.0F && x <= width) this.edge = 32; 
/* 110 */               Window.this.dragging = (this.edge != 0);
/* 111 */               this.startX = x;
/* 112 */               this.startY = y;
/* 113 */               this.lastX = x - width;
/* 114 */               this.lastY = y - height;
/*     */             } 
/* 116 */             return (this.edge != 0 || Window.this.isModal);
/*     */           }
/*     */           float startY; float lastX; float lastY;
/*     */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/* 120 */             Window.this.dragging = false;
/*     */           }
/*     */           
/*     */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/* 124 */             if (!Window.this.dragging)
/* 125 */               return;  float width = Window.this.getWidth(), height = Window.this.getHeight();
/* 126 */             float windowX = Window.this.getX(), windowY = Window.this.getY();
/*     */             
/* 128 */             float minWidth = Window.this.getMinWidth(), maxWidth = Window.this.getMaxWidth();
/* 129 */             float minHeight = Window.this.getMinHeight(), maxHeight = Window.this.getMaxHeight();
/* 130 */             Stage stage = Window.this.getStage();
/* 131 */             boolean clampPosition = (Window.this.keepWithinStage && Window.this.getParent() == stage.getRoot());
/*     */             
/* 133 */             if ((this.edge & 0x20) != 0) {
/* 134 */               float amountX = x - this.startX, amountY = y - this.startY;
/* 135 */               windowX += amountX;
/* 136 */               windowY += amountY;
/*     */             } 
/* 138 */             if ((this.edge & 0x8) != 0) {
/* 139 */               float amountX = x - this.startX;
/* 140 */               if (width - amountX < minWidth) amountX = -(minWidth - width); 
/* 141 */               if (clampPosition && windowX + amountX < 0.0F) amountX = -windowX; 
/* 142 */               width -= amountX;
/* 143 */               windowX += amountX;
/*     */             } 
/* 145 */             if ((this.edge & 0x4) != 0) {
/* 146 */               float amountY = y - this.startY;
/* 147 */               if (height - amountY < minHeight) amountY = -(minHeight - height); 
/* 148 */               if (clampPosition && windowY + amountY < 0.0F) amountY = -windowY; 
/* 149 */               height -= amountY;
/* 150 */               windowY += amountY;
/*     */             } 
/* 152 */             if ((this.edge & 0x10) != 0) {
/* 153 */               float amountX = x - this.lastX - width;
/* 154 */               if (width + amountX < minWidth) amountX = minWidth - width; 
/* 155 */               if (clampPosition && windowX + width + amountX > stage.getWidth()) amountX = stage.getWidth() - windowX - width; 
/* 156 */               width += amountX;
/*     */             } 
/* 158 */             if ((this.edge & 0x2) != 0) {
/* 159 */               float amountY = y - this.lastY - height;
/* 160 */               if (height + amountY < minHeight) amountY = minHeight - height; 
/* 161 */               if (clampPosition && windowY + height + amountY > stage.getHeight())
/* 162 */                 amountY = stage.getHeight() - windowY - height; 
/* 163 */               height += amountY;
/*     */             } 
/* 165 */             Window.this.setBounds(Math.round(windowX), Math.round(windowY), Math.round(width), Math.round(height));
/*     */           }
/*     */           
/*     */           public boolean mouseMoved(InputEvent event, float x, float y) {
/* 169 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean scrolled(InputEvent event, float x, float y, int amount) {
/* 173 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean keyDown(InputEvent event, int keycode) {
/* 177 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean keyUp(InputEvent event, int keycode) {
/* 181 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean keyTyped(InputEvent event, char character) {
/* 185 */             return Window.this.isModal;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setStyle(WindowStyle style) {
/* 191 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 192 */     this.style = style;
/* 193 */     setBackground(style.background);
/* 194 */     this.titleLabel.setStyle(new Label.LabelStyle(style.titleFont, style.titleFontColor));
/* 195 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WindowStyle getStyle() {
/* 201 */     return this.style;
/*     */   }
/*     */   
/*     */   void keepWithinStage() {
/* 205 */     if (!this.keepWithinStage)
/* 206 */       return;  Stage stage = getStage();
/* 207 */     Camera camera = stage.getCamera();
/* 208 */     if (camera instanceof OrthographicCamera) {
/* 209 */       OrthographicCamera orthographicCamera = (OrthographicCamera)camera;
/* 210 */       float parentWidth = stage.getWidth();
/* 211 */       float parentHeight = stage.getHeight();
/* 212 */       if (getX(16) - camera.position.x > parentWidth / 2.0F / orthographicCamera.zoom)
/* 213 */         setPosition(camera.position.x + parentWidth / 2.0F / orthographicCamera.zoom, getY(16), 16); 
/* 214 */       if (getX(8) - camera.position.x < -parentWidth / 2.0F / orthographicCamera.zoom)
/* 215 */         setPosition(camera.position.x - parentWidth / 2.0F / orthographicCamera.zoom, getY(8), 8); 
/* 216 */       if (getY(2) - camera.position.y > parentHeight / 2.0F / orthographicCamera.zoom)
/* 217 */         setPosition(getX(2), camera.position.y + parentHeight / 2.0F / orthographicCamera.zoom, 2); 
/* 218 */       if (getY(4) - camera.position.y < -parentHeight / 2.0F / orthographicCamera.zoom)
/* 219 */         setPosition(getX(4), camera.position.y - parentHeight / 2.0F / orthographicCamera.zoom, 4); 
/* 220 */     } else if (getParent() == stage.getRoot()) {
/* 221 */       float parentWidth = stage.getWidth();
/* 222 */       float parentHeight = stage.getHeight();
/* 223 */       if (getX() < 0.0F) setX(0.0F); 
/* 224 */       if (getRight() > parentWidth) setX(parentWidth - getWidth()); 
/* 225 */       if (getY() < 0.0F) setY(0.0F); 
/* 226 */       if (getTop() > parentHeight) setY(parentHeight - getHeight()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 231 */     Stage stage = getStage();
/* 232 */     if (stage.getKeyboardFocus() == null) stage.setKeyboardFocus((Actor)this);
/*     */     
/* 234 */     keepWithinStage();
/*     */     
/* 236 */     if (this.style.stageBackground != null) {
/* 237 */       stageToLocalCoordinates(tmpPosition.set(0.0F, 0.0F));
/* 238 */       stageToLocalCoordinates(tmpSize.set(stage.getWidth(), stage.getHeight()));
/* 239 */       drawStageBackground(batch, parentAlpha, getX() + tmpPosition.x, getY() + tmpPosition.y, getX() + tmpSize.x, getY() + tmpSize.y);
/*     */     } 
/*     */ 
/*     */     
/* 243 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   protected void drawStageBackground(Batch batch, float parentAlpha, float x, float y, float width, float height) {
/* 247 */     Color color = getColor();
/* 248 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/* 249 */     this.style.stageBackground.draw(batch, x, y, width, height);
/*     */   }
/*     */   
/*     */   protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
/* 253 */     super.drawBackground(batch, parentAlpha, x, y);
/*     */ 
/*     */     
/* 256 */     (this.titleTable.getColor()).a = (getColor()).a;
/* 257 */     float padTop = getPadTop(), padLeft = getPadLeft();
/* 258 */     this.titleTable.setSize(getWidth() - padLeft - getPadRight(), padTop);
/* 259 */     this.titleTable.setPosition(padLeft, getHeight() - padTop);
/* 260 */     this.drawTitleTable = true;
/* 261 */     this.titleTable.draw(batch, parentAlpha);
/* 262 */     this.drawTitleTable = false;
/*     */   }
/*     */   
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 266 */     Actor hit = super.hit(x, y, touchable);
/* 267 */     if (hit == null && this.isModal && (!touchable || getTouchable() == Touchable.enabled)) return (Actor)this; 
/* 268 */     float height = getHeight();
/* 269 */     if (hit == null || hit == this) return hit; 
/* 270 */     if (y <= height && y >= height - getPadTop() && x >= 0.0F && x <= getWidth()) {
/*     */       Group group;
/* 272 */       Actor current = hit;
/* 273 */       while (current.getParent() != this)
/* 274 */         group = current.getParent(); 
/* 275 */       if (getCell(group) != null) return (Actor)this; 
/*     */     } 
/* 277 */     return hit;
/*     */   }
/*     */   
/*     */   public boolean isMovable() {
/* 281 */     return this.isMovable;
/*     */   }
/*     */   
/*     */   public void setMovable(boolean isMovable) {
/* 285 */     this.isMovable = isMovable;
/*     */   }
/*     */   
/*     */   public boolean isModal() {
/* 289 */     return this.isModal;
/*     */   }
/*     */   
/*     */   public void setModal(boolean isModal) {
/* 293 */     this.isModal = isModal;
/*     */   }
/*     */   
/*     */   public void setKeepWithinStage(boolean keepWithinStage) {
/* 297 */     this.keepWithinStage = keepWithinStage;
/*     */   }
/*     */   
/*     */   public boolean isResizable() {
/* 301 */     return this.isResizable;
/*     */   }
/*     */   
/*     */   public void setResizable(boolean isResizable) {
/* 305 */     this.isResizable = isResizable;
/*     */   }
/*     */   
/*     */   public void setResizeBorder(int resizeBorder) {
/* 309 */     this.resizeBorder = resizeBorder;
/*     */   }
/*     */   
/*     */   public boolean isDragging() {
/* 313 */     return this.dragging;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 317 */     return Math.max(super.getPrefWidth(), this.titleLabel.getPrefWidth() + getPadLeft() + getPadRight());
/*     */   }
/*     */   
/*     */   public Table getTitleTable() {
/* 321 */     return this.titleTable;
/*     */   }
/*     */   
/*     */   public Label getTitleLabel() {
/* 325 */     return this.titleLabel;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class WindowStyle
/*     */   {
/*     */     public Drawable background;
/*     */     
/*     */     public BitmapFont titleFont;
/*     */     
/* 335 */     public Color titleFontColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     public Drawable stageBackground;
/*     */ 
/*     */     
/*     */     public WindowStyle() {}
/*     */     
/*     */     public WindowStyle(BitmapFont titleFont, Color titleFontColor, Drawable background) {
/* 343 */       this.background = background;
/* 344 */       this.titleFont = titleFont;
/* 345 */       this.titleFontColor.set(titleFontColor);
/*     */     }
/*     */     
/*     */     public WindowStyle(WindowStyle style) {
/* 349 */       this.background = style.background;
/* 350 */       this.titleFont = style.titleFont;
/* 351 */       this.titleFontColor = new Color(style.titleFontColor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\Window.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */