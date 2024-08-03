/*      */ package com.badlogic.gdx.scenes.scene2d.ui;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.g2d.Batch;
/*      */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*      */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*      */ import com.badlogic.gdx.scenes.scene2d.Event;
/*      */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.Group;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.Clipboard;
/*      */ import com.badlogic.gdx.utils.FloatArray;
/*      */ import com.badlogic.gdx.utils.Pools;
/*      */ import com.badlogic.gdx.utils.TimeUtils;
/*      */ import com.badlogic.gdx.utils.Timer;
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
/*      */ public class TextField
/*      */   extends Widget
/*      */   implements Disableable
/*      */ {
/*      */   private static final char BACKSPACE = '\b';
/*      */   protected static final char ENTER_DESKTOP = '\r';
/*      */   protected static final char ENTER_ANDROID = '\n';
/*      */   private static final char TAB = '\t';
/*      */   private static final char DELETE = '';
/*      */   private static final char BULLET = '';
/*   73 */   private static final Vector2 tmp1 = new Vector2();
/*   74 */   private static final Vector2 tmp2 = new Vector2();
/*   75 */   private static final Vector2 tmp3 = new Vector2();
/*      */   
/*   77 */   public static float keyRepeatInitialTime = 0.4F;
/*   78 */   public static float keyRepeatTime = 0.1F;
/*      */   protected String text;
/*      */   protected int cursor;
/*      */   protected int selectionStart;
/*      */   protected boolean hasSelection;
/*      */   protected boolean writeEnters;
/*   84 */   protected final GlyphLayout layout = new GlyphLayout();
/*   85 */   protected final FloatArray glyphPositions = new FloatArray();
/*      */   
/*      */   TextFieldStyle style;
/*      */   private String messageText;
/*      */   protected CharSequence displayText;
/*      */   Clipboard clipboard;
/*      */   InputListener inputListener;
/*      */   TextFieldListener listener;
/*      */   TextFieldFilter filter;
/*   94 */   OnscreenKeyboard keyboard = new DefaultOnscreenKeyboard();
/*      */   boolean focusTraversal = true;
/*   96 */   private int textHAlign = 8; boolean onlyFontChars = true; boolean disabled;
/*      */   private float selectionX;
/*      */   private float selectionWidth;
/*   99 */   String undoText = "";
/*      */   
/*      */   long lastChangeTime;
/*      */   boolean passwordMode;
/*      */   private StringBuilder passwordBuffer;
/*  104 */   private char passwordCharacter = ''; protected float fontOffset; protected float textHeight;
/*      */   protected float textOffset;
/*      */   float renderOffset;
/*      */   private int visibleTextStart;
/*      */   private int visibleTextEnd;
/*  109 */   private int maxLength = 0;
/*      */   
/*  111 */   private float blinkTime = 0.32F;
/*      */   
/*      */   boolean cursorOn = true;
/*      */   long lastBlink;
/*  115 */   KeyRepeatTask keyRepeatTask = new KeyRepeatTask();
/*      */   boolean programmaticChangeEvents;
/*      */   
/*      */   public TextField(String text, Skin skin) {
/*  119 */     this(text, skin.<TextFieldStyle>get(TextFieldStyle.class));
/*      */   }
/*      */   
/*      */   public TextField(String text, Skin skin, String styleName) {
/*  123 */     this(text, skin.<TextFieldStyle>get(styleName, TextFieldStyle.class));
/*      */   }
/*      */   
/*      */   public TextField(String text, TextFieldStyle style) {
/*  127 */     setStyle(style);
/*  128 */     this.clipboard = Gdx.app.getClipboard();
/*  129 */     initialize();
/*  130 */     setText(text);
/*  131 */     setSize(getPrefWidth(), getPrefHeight());
/*      */   }
/*      */   
/*      */   protected void initialize() {
/*  135 */     addListener((EventListener)(this.inputListener = createInputListener()));
/*      */   }
/*      */   
/*      */   protected InputListener createInputListener() {
/*  139 */     return (InputListener)new TextFieldClickListener();
/*      */   }
/*      */   
/*      */   protected int letterUnderCursor(float x) {
/*  143 */     x -= this.textOffset + this.fontOffset - (this.style.font.getData()).cursorX - this.glyphPositions.get(this.visibleTextStart);
/*  144 */     int n = this.glyphPositions.size;
/*  145 */     float[] glyphPositions = this.glyphPositions.items;
/*  146 */     for (int i = 1; i < n; i++) {
/*  147 */       if (glyphPositions[i] > x) {
/*  148 */         if (glyphPositions[i] - x <= x - glyphPositions[i - 1]) return i; 
/*  149 */         return i - 1;
/*      */       } 
/*      */     } 
/*  152 */     return n - 1;
/*      */   }
/*      */   
/*      */   protected boolean isWordCharacter(char c) {
/*  156 */     return Character.isLetterOrDigit(c);
/*      */   }
/*      */   
/*      */   protected int[] wordUnderCursor(int at) {
/*  160 */     String text = this.text;
/*  161 */     int start = at, right = text.length(), left = 0, index = start;
/*  162 */     for (; index < right; index++) {
/*  163 */       if (!isWordCharacter(text.charAt(index))) {
/*  164 */         right = index;
/*      */         break;
/*      */       } 
/*      */     } 
/*  168 */     for (index = start - 1; index > -1; index--) {
/*  169 */       if (!isWordCharacter(text.charAt(index))) {
/*  170 */         left = index + 1;
/*      */         break;
/*      */       } 
/*      */     } 
/*  174 */     return new int[] { left, right };
/*      */   }
/*      */   
/*      */   int[] wordUnderCursor(float x) {
/*  178 */     return wordUnderCursor(letterUnderCursor(x));
/*      */   }
/*      */   
/*      */   boolean withinMaxLength(int size) {
/*  182 */     return (this.maxLength <= 0 || size < this.maxLength);
/*      */   }
/*      */   
/*      */   public void setMaxLength(int maxLength) {
/*  186 */     this.maxLength = maxLength;
/*      */   }
/*      */   
/*      */   public int getMaxLength() {
/*  190 */     return this.maxLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOnlyFontChars(boolean onlyFontChars) {
/*  197 */     this.onlyFontChars = onlyFontChars;
/*      */   }
/*      */   
/*      */   public void setStyle(TextFieldStyle style) {
/*  201 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  202 */     this.style = style;
/*  203 */     this.textHeight = style.font.getCapHeight() - style.font.getDescent() * 2.0F;
/*  204 */     invalidateHierarchy();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TextFieldStyle getStyle() {
/*  210 */     return this.style;
/*      */   }
/*      */   
/*      */   protected void calculateOffsets() {
/*  214 */     float visibleWidth = getWidth();
/*  215 */     if (this.style.background != null) visibleWidth -= this.style.background.getLeftWidth() + this.style.background.getRightWidth();
/*      */     
/*  217 */     int glyphCount = this.glyphPositions.size;
/*  218 */     float[] glyphPositions = this.glyphPositions.items;
/*      */ 
/*      */     
/*  221 */     float distance = glyphPositions[Math.max(0, this.cursor - 1)] + this.renderOffset;
/*  222 */     if (distance <= 0.0F) {
/*  223 */       this.renderOffset -= distance;
/*      */     } else {
/*  225 */       int index = Math.min(glyphCount - 1, this.cursor + 1);
/*  226 */       float minX = glyphPositions[index] - visibleWidth;
/*  227 */       if (-this.renderOffset < minX) {
/*  228 */         this.renderOffset = -minX;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  233 */     this.visibleTextStart = 0;
/*  234 */     float startX = 0.0F;
/*  235 */     for (int i = 0; i < glyphCount; i++) {
/*  236 */       if (glyphPositions[i] >= -this.renderOffset) {
/*  237 */         this.visibleTextStart = Math.max(0, i);
/*  238 */         startX = glyphPositions[i];
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  244 */     int length = this.displayText.length();
/*  245 */     this.visibleTextEnd = Math.min(length, this.cursor + 1);
/*  246 */     for (; this.visibleTextEnd <= length && 
/*  247 */       glyphPositions[this.visibleTextEnd] <= startX + visibleWidth; this.visibleTextEnd++);
/*  248 */     this.visibleTextEnd = Math.max(0, this.visibleTextEnd - 1);
/*      */     
/*  250 */     if ((this.textHAlign & 0x8) == 0) {
/*  251 */       this.textOffset = visibleWidth - glyphPositions[this.visibleTextEnd] - startX;
/*  252 */       if ((this.textHAlign & 0x1) != 0) this.textOffset = Math.round(this.textOffset * 0.5F); 
/*      */     } else {
/*  254 */       this.textOffset = startX + this.renderOffset;
/*      */     } 
/*      */     
/*  257 */     if (this.hasSelection) {
/*  258 */       int minIndex = Math.min(this.cursor, this.selectionStart);
/*  259 */       int maxIndex = Math.max(this.cursor, this.selectionStart);
/*  260 */       float minX = Math.max(glyphPositions[minIndex], -this.renderOffset);
/*  261 */       float maxX = Math.min(glyphPositions[maxIndex], visibleWidth - this.renderOffset);
/*  262 */       this.selectionX = minX;
/*  263 */       if (this.renderOffset == 0.0F) this.selectionX += this.textOffset; 
/*  264 */       this.selectionWidth = maxX - minX - (this.style.font.getData()).cursorX;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Batch batch, float parentAlpha) {
/*  270 */     Stage stage = getStage();
/*  271 */     boolean focused = (stage != null && stage.getKeyboardFocus() == this);
/*  272 */     if (!focused) this.keyRepeatTask.cancel();
/*      */     
/*  274 */     BitmapFont font = this.style.font;
/*  275 */     Color fontColor = (this.disabled && this.style.disabledFontColor != null) ? this.style.disabledFontColor : ((focused && this.style.focusedFontColor != null) ? this.style.focusedFontColor : this.style.fontColor);
/*      */     
/*  277 */     Drawable selection = this.style.selection;
/*  278 */     Drawable cursorPatch = this.style.cursor;
/*  279 */     Drawable background = (this.disabled && this.style.disabledBackground != null) ? this.style.disabledBackground : ((focused && this.style.focusedBackground != null) ? this.style.focusedBackground : this.style.background);
/*      */ 
/*      */     
/*  282 */     Color color = getColor();
/*  283 */     float x = getX();
/*  284 */     float y = getY();
/*  285 */     float width = getWidth();
/*  286 */     float height = getHeight();
/*      */     
/*  288 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*  289 */     float bgLeftWidth = 0.0F, bgRightWidth = 0.0F;
/*  290 */     if (background != null) {
/*  291 */       background.draw(batch, x, y, width, height);
/*  292 */       bgLeftWidth = background.getLeftWidth();
/*  293 */       bgRightWidth = background.getRightWidth();
/*      */     } 
/*      */     
/*  296 */     float textY = getTextY(font, background);
/*  297 */     calculateOffsets();
/*      */     
/*  299 */     if (focused && this.hasSelection && selection != null) {
/*  300 */       drawSelection(selection, batch, font, x + bgLeftWidth, y + textY);
/*      */     }
/*      */     
/*  303 */     float yOffset = font.isFlipped() ? -this.textHeight : 0.0F;
/*  304 */     if (this.displayText.length() == 0) {
/*  305 */       if (!focused && this.messageText != null) {
/*  306 */         if (this.style.messageFontColor != null) {
/*  307 */           font.setColor(this.style.messageFontColor.r, this.style.messageFontColor.g, this.style.messageFontColor.b, this.style.messageFontColor.a * color.a * parentAlpha);
/*      */         } else {
/*      */           
/*  310 */           font.setColor(0.7F, 0.7F, 0.7F, color.a * parentAlpha);
/*  311 */         }  BitmapFont messageFont = (this.style.messageFont != null) ? this.style.messageFont : font;
/*  312 */         messageFont.draw(batch, this.messageText, x + bgLeftWidth, y + textY + yOffset, 0, this.messageText.length(), width - bgLeftWidth - bgRightWidth, this.textHAlign, false, "...");
/*      */       } 
/*      */     } else {
/*      */       
/*  316 */       font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * color.a * parentAlpha);
/*  317 */       drawText(batch, font, x + bgLeftWidth, y + textY + yOffset);
/*      */     } 
/*  319 */     if (focused && !this.disabled) {
/*  320 */       blink();
/*  321 */       if (this.cursorOn && cursorPatch != null) {
/*  322 */         drawCursor(cursorPatch, batch, font, x + bgLeftWidth, y + textY);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected float getTextY(BitmapFont font, Drawable background) {
/*  328 */     float height = getHeight();
/*  329 */     float textY = this.textHeight / 2.0F + font.getDescent();
/*  330 */     if (background != null) {
/*  331 */       float bottom = background.getBottomHeight();
/*  332 */       textY = textY + (height - background.getTopHeight() - bottom) / 2.0F + bottom;
/*      */     } else {
/*  334 */       textY += height / 2.0F;
/*      */     } 
/*  336 */     if (font.usesIntegerPositions()) textY = (int)textY; 
/*  337 */     return textY;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void drawSelection(Drawable selection, Batch batch, BitmapFont font, float x, float y) {
/*  342 */     selection.draw(batch, x + this.selectionX + this.renderOffset + this.fontOffset, y - this.textHeight - font.getDescent(), this.selectionWidth, this.textHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void drawText(Batch batch, BitmapFont font, float x, float y) {
/*  347 */     font.draw(batch, this.displayText, x + this.textOffset, y, this.visibleTextStart, this.visibleTextEnd, 0.0F, 8, false);
/*      */   }
/*      */   
/*      */   protected void drawCursor(Drawable cursorPatch, Batch batch, BitmapFont font, float x, float y) {
/*  351 */     cursorPatch.draw(batch, x + this.textOffset + this.glyphPositions
/*  352 */         .get(this.cursor) - this.glyphPositions.get(this.visibleTextStart) + this.fontOffset + (font.getData()).cursorX, y - this.textHeight - font
/*  353 */         .getDescent(), cursorPatch.getMinWidth(), this.textHeight);
/*      */   }
/*      */   
/*      */   void updateDisplayText() {
/*  357 */     BitmapFont font = this.style.font;
/*  358 */     BitmapFont.BitmapFontData data = font.getData();
/*  359 */     String text = this.text;
/*  360 */     int textLength = text.length();
/*      */     
/*  362 */     StringBuilder buffer = new StringBuilder();
/*  363 */     for (int i = 0; i < textLength; i++) {
/*  364 */       char c = text.charAt(i);
/*  365 */       buffer.append(data.hasGlyph(c) ? c : 32);
/*      */     } 
/*  367 */     String newDisplayText = buffer.toString();
/*      */     
/*  369 */     if (this.passwordMode && data.hasGlyph(this.passwordCharacter)) {
/*  370 */       if (this.passwordBuffer == null) this.passwordBuffer = new StringBuilder(newDisplayText.length()); 
/*  371 */       if (this.passwordBuffer.length() > textLength) {
/*  372 */         this.passwordBuffer.setLength(textLength);
/*      */       } else {
/*  374 */         for (int j = this.passwordBuffer.length(); j < textLength; j++)
/*  375 */           this.passwordBuffer.append(this.passwordCharacter); 
/*      */       } 
/*  377 */       this.displayText = this.passwordBuffer;
/*      */     } else {
/*  379 */       this.displayText = newDisplayText;
/*      */     } 
/*  381 */     this.layout.setText(font, this.displayText);
/*  382 */     this.glyphPositions.clear();
/*  383 */     float x = 0.0F;
/*  384 */     if (this.layout.runs.size > 0) {
/*  385 */       GlyphLayout.GlyphRun run = (GlyphLayout.GlyphRun)this.layout.runs.first();
/*  386 */       FloatArray xAdvances = run.xAdvances;
/*  387 */       this.fontOffset = xAdvances.first();
/*  388 */       for (int j = 1, n = xAdvances.size; j < n; j++) {
/*  389 */         this.glyphPositions.add(x);
/*  390 */         x += xAdvances.get(j);
/*      */       } 
/*      */     } else {
/*  393 */       this.fontOffset = 0.0F;
/*  394 */     }  this.glyphPositions.add(x);
/*      */     
/*  396 */     if (this.selectionStart > newDisplayText.length()) this.selectionStart = textLength; 
/*      */   }
/*      */   
/*      */   private void blink() {
/*  400 */     if (!Gdx.graphics.isContinuousRendering()) {
/*  401 */       this.cursorOn = true;
/*      */       return;
/*      */     } 
/*  404 */     long time = TimeUtils.nanoTime();
/*  405 */     if ((float)(time - this.lastBlink) / 1.0E9F > this.blinkTime) {
/*  406 */       this.cursorOn = !this.cursorOn;
/*  407 */       this.lastBlink = time;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void copy() {
/*  413 */     if (this.hasSelection && !this.passwordMode) {
/*  414 */       this.clipboard.setContents(this.text.substring(Math.min(this.cursor, this.selectionStart), Math.max(this.cursor, this.selectionStart)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cut() {
/*  421 */     cut(this.programmaticChangeEvents);
/*      */   }
/*      */   
/*      */   void cut(boolean fireChangeEvent) {
/*  425 */     if (this.hasSelection && !this.passwordMode) {
/*  426 */       copy();
/*  427 */       this.cursor = delete(fireChangeEvent);
/*  428 */       updateDisplayText();
/*      */     } 
/*      */   }
/*      */   
/*      */   void paste(String content, boolean fireChangeEvent) {
/*  433 */     if (content == null)
/*  434 */       return;  StringBuilder buffer = new StringBuilder();
/*  435 */     int textLength = this.text.length();
/*  436 */     if (this.hasSelection) textLength -= Math.abs(this.cursor - this.selectionStart); 
/*  437 */     BitmapFont.BitmapFontData data = this.style.font.getData();
/*  438 */     for (int i = 0, n = content.length(); i < n && 
/*  439 */       withinMaxLength(textLength + buffer.length()); i++) {
/*  440 */       char c = content.charAt(i);
/*  441 */       if ((this.writeEnters && (c == '\n' || c == '\r')) || (
/*  442 */         c != '\r' && c != '\n' && (
/*  443 */         !this.onlyFontChars || data.hasGlyph(c)) && (
/*  444 */         this.filter == null || this.filter.acceptChar(this, c))))
/*      */       {
/*  446 */         buffer.append(c); } 
/*      */     } 
/*  448 */     content = buffer.toString();
/*      */     
/*  450 */     if (this.hasSelection) this.cursor = delete(fireChangeEvent); 
/*  451 */     if (fireChangeEvent) {
/*  452 */       changeText(this.text, insert(this.cursor, content, this.text));
/*      */     } else {
/*  454 */       this.text = insert(this.cursor, content, this.text);
/*  455 */     }  updateDisplayText();
/*  456 */     this.cursor += content.length();
/*      */   }
/*      */   
/*      */   String insert(int position, CharSequence text, String to) {
/*  460 */     if (to.length() == 0) return text.toString(); 
/*  461 */     return to.substring(0, position) + text + to.substring(position, to.length());
/*      */   }
/*      */   
/*      */   int delete(boolean fireChangeEvent) {
/*  465 */     int from = this.selectionStart;
/*  466 */     int to = this.cursor;
/*  467 */     int minIndex = Math.min(from, to);
/*  468 */     int maxIndex = Math.max(from, to);
/*      */     
/*  470 */     String newText = ((minIndex > 0) ? this.text.substring(0, minIndex) : "") + ((maxIndex < this.text.length()) ? this.text.substring(maxIndex, this.text.length()) : "");
/*  471 */     if (fireChangeEvent) {
/*  472 */       changeText(this.text, newText);
/*      */     } else {
/*  474 */       this.text = newText;
/*  475 */     }  clearSelection();
/*  476 */     return minIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void next(boolean up) {
/*  482 */     Stage stage = getStage();
/*  483 */     if (stage == null)
/*  484 */       return;  getParent().localToStageCoordinates(tmp1.set(getX(), getY()));
/*  485 */     TextField textField = findNextTextField(stage.getActors(), (TextField)null, tmp2, tmp1, up);
/*  486 */     if (textField == null) {
/*  487 */       if (up) {
/*  488 */         tmp1.set(Float.MIN_VALUE, Float.MIN_VALUE);
/*      */       } else {
/*  490 */         tmp1.set(Float.MAX_VALUE, Float.MAX_VALUE);
/*  491 */       }  textField = findNextTextField(getStage().getActors(), (TextField)null, tmp2, tmp1, up);
/*      */     } 
/*  493 */     if (textField != null) {
/*  494 */       stage.setKeyboardFocus(textField);
/*      */     } else {
/*  496 */       Gdx.input.setOnscreenKeyboardVisible(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private TextField findNextTextField(Array<Actor> actors, TextField best, Vector2 bestCoords, Vector2 currentCoords, boolean up) {
/*  501 */     for (int i = 0, n = actors.size; i < n; i++) {
/*  502 */       Actor actor = (Actor)actors.get(i);
/*  503 */       if (actor != this)
/*  504 */         if (actor instanceof TextField) {
/*  505 */           TextField textField = (TextField)actor;
/*  506 */           if (!textField.isDisabled() && textField.focusTraversal) {
/*  507 */             Vector2 actorCoords = actor.getParent().localToStageCoordinates(tmp3.set(actor.getX(), actor.getY()));
/*  508 */             if ((((actorCoords.y < currentCoords.y || (actorCoords.y == currentCoords.y && actorCoords.x > currentCoords.x)) ? 1 : 0) ^ up) != 0 && (
/*  509 */               best == null || (((actorCoords.y > bestCoords.y || (actorCoords.y == bestCoords.y && actorCoords.x < bestCoords.x)) ? 1 : 0) ^ up) != 0)) {
/*      */               
/*  511 */               best = (TextField)actor;
/*  512 */               bestCoords.set(actorCoords);
/*      */             } 
/*      */           } 
/*  515 */         } else if (actor instanceof Group) {
/*  516 */           best = findNextTextField((Array<Actor>)((Group)actor).getChildren(), best, bestCoords, currentCoords, up);
/*      */         }  
/*  518 */     }  return best;
/*      */   }
/*      */   
/*      */   public InputListener getDefaultInputListener() {
/*  522 */     return this.inputListener;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTextFieldListener(TextFieldListener listener) {
/*  527 */     this.listener = listener;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTextFieldFilter(TextFieldFilter filter) {
/*  532 */     this.filter = filter;
/*      */   }
/*      */   
/*      */   public TextFieldFilter getTextFieldFilter() {
/*  536 */     return this.filter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFocusTraversal(boolean focusTraversal) {
/*  541 */     this.focusTraversal = focusTraversal;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessageText() {
/*  546 */     return this.messageText;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageText(String messageText) {
/*  552 */     this.messageText = messageText;
/*      */   }
/*      */ 
/*      */   
/*      */   public void appendText(String str) {
/*  557 */     if (str == null) str = "";
/*      */     
/*  559 */     clearSelection();
/*  560 */     this.cursor = this.text.length();
/*  561 */     paste(str, this.programmaticChangeEvents);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setText(String str) {
/*  566 */     if (str == null) str = ""; 
/*  567 */     if (str.equals(this.text))
/*      */       return; 
/*  569 */     clearSelection();
/*  570 */     String oldText = this.text;
/*  571 */     this.text = "";
/*  572 */     paste(str, false);
/*  573 */     if (this.programmaticChangeEvents) changeText(oldText, this.text); 
/*  574 */     this.cursor = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getText() {
/*  579 */     return this.text;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean changeText(String oldText, String newText) {
/*  585 */     if (newText.equals(oldText)) return false; 
/*  586 */     this.text = newText;
/*  587 */     ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/*  588 */     boolean cancelled = fire((Event)changeEvent);
/*  589 */     this.text = cancelled ? oldText : newText;
/*  590 */     Pools.free(changeEvent);
/*  591 */     return !cancelled;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
/*  597 */     this.programmaticChangeEvents = programmaticChangeEvents;
/*      */   }
/*      */   
/*      */   public int getSelectionStart() {
/*  601 */     return this.selectionStart;
/*      */   }
/*      */   
/*      */   public String getSelection() {
/*  605 */     return this.hasSelection ? this.text.substring(Math.min(this.selectionStart, this.cursor), Math.max(this.selectionStart, this.cursor)) : "";
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSelection(int selectionStart, int selectionEnd) {
/*  610 */     if (selectionStart < 0) throw new IllegalArgumentException("selectionStart must be >= 0"); 
/*  611 */     if (selectionEnd < 0) throw new IllegalArgumentException("selectionEnd must be >= 0"); 
/*  612 */     selectionStart = Math.min(this.text.length(), selectionStart);
/*  613 */     selectionEnd = Math.min(this.text.length(), selectionEnd);
/*  614 */     if (selectionEnd == selectionStart) {
/*  615 */       clearSelection();
/*      */       return;
/*      */     } 
/*  618 */     if (selectionEnd < selectionStart) {
/*  619 */       int temp = selectionEnd;
/*  620 */       selectionEnd = selectionStart;
/*  621 */       selectionStart = temp;
/*      */     } 
/*      */     
/*  624 */     this.hasSelection = true;
/*  625 */     this.selectionStart = selectionStart;
/*  626 */     this.cursor = selectionEnd;
/*      */   }
/*      */   
/*      */   public void selectAll() {
/*  630 */     setSelection(0, this.text.length());
/*      */   }
/*      */   
/*      */   public void clearSelection() {
/*  634 */     this.hasSelection = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorPosition(int cursorPosition) {
/*  639 */     if (cursorPosition < 0) throw new IllegalArgumentException("cursorPosition must be >= 0"); 
/*  640 */     clearSelection();
/*  641 */     this.cursor = Math.min(cursorPosition, this.text.length());
/*      */   }
/*      */   
/*      */   public int getCursorPosition() {
/*  645 */     return this.cursor;
/*      */   }
/*      */ 
/*      */   
/*      */   public OnscreenKeyboard getOnscreenKeyboard() {
/*  650 */     return this.keyboard;
/*      */   }
/*      */   
/*      */   public void setOnscreenKeyboard(OnscreenKeyboard keyboard) {
/*  654 */     this.keyboard = keyboard;
/*      */   }
/*      */   
/*      */   public void setClipboard(Clipboard clipboard) {
/*  658 */     this.clipboard = clipboard;
/*      */   }
/*      */   
/*      */   public float getPrefWidth() {
/*  662 */     return 150.0F;
/*      */   }
/*      */   
/*      */   public float getPrefHeight() {
/*  666 */     float prefHeight = this.textHeight;
/*  667 */     if (this.style.background != null) {
/*  668 */       prefHeight = Math.max(prefHeight + this.style.background.getBottomHeight() + this.style.background.getTopHeight(), this.style.background
/*  669 */           .getMinHeight());
/*      */     }
/*  671 */     return prefHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlignment(int alignment) {
/*  677 */     this.textHAlign = alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPasswordMode(boolean passwordMode) {
/*  683 */     this.passwordMode = passwordMode;
/*  684 */     updateDisplayText();
/*      */   }
/*      */   
/*      */   public boolean isPasswordMode() {
/*  688 */     return this.passwordMode;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPasswordCharacter(char passwordCharacter) {
/*  694 */     this.passwordCharacter = passwordCharacter;
/*  695 */     if (this.passwordMode) updateDisplayText(); 
/*      */   }
/*      */   
/*      */   public void setBlinkTime(float blinkTime) {
/*  699 */     this.blinkTime = blinkTime;
/*      */   }
/*      */   
/*      */   public void setDisabled(boolean disabled) {
/*  703 */     this.disabled = disabled;
/*      */   }
/*      */   
/*      */   public boolean isDisabled() {
/*  707 */     return this.disabled;
/*      */   }
/*      */   
/*      */   protected void moveCursor(boolean forward, boolean jump) {
/*  711 */     int limit = forward ? this.text.length() : 0;
/*  712 */     int charOffset = forward ? 0 : -1; do {  }
/*  713 */     while ((forward ? (++this.cursor < limit) : (--this.cursor > limit)) && jump && 
/*  714 */       continueCursor(this.cursor, charOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean continueCursor(int index, int offset) {
/*  719 */     char c = this.text.charAt(index + offset);
/*  720 */     return isWordCharacter(c);
/*      */   }
/*      */   
/*      */   class KeyRepeatTask extends Timer.Task {
/*      */     int keycode;
/*      */     
/*      */     public void run() {
/*  727 */       TextField.this.inputListener.keyDown(null, this.keycode);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static interface TextFieldListener
/*      */   {
/*      */     void keyTyped(TextField param1TextField, char param1Char);
/*      */   }
/*      */   
/*      */   public static interface TextFieldFilter
/*      */   {
/*      */     boolean acceptChar(TextField param1TextField, char param1Char);
/*      */     
/*      */     public static class DigitsOnlyFilter
/*      */       implements TextFieldFilter
/*      */     {
/*      */       public boolean acceptChar(TextField textField, char c) {
/*  745 */         return Character.isDigit(c);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface OnscreenKeyboard
/*      */   {
/*      */     void show(boolean param1Boolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DefaultOnscreenKeyboard
/*      */     implements OnscreenKeyboard
/*      */   {
/*      */     public void show(boolean visible) {
/*  763 */       Gdx.input.setOnscreenKeyboardVisible(visible);
/*      */     }
/*      */   }
/*      */   
/*      */   public class TextFieldClickListener
/*      */     extends ClickListener {
/*      */     public void clicked(InputEvent event, float x, float y) {
/*  770 */       int count = getTapCount() % 4;
/*  771 */       if (count == 0) TextField.this.clearSelection(); 
/*  772 */       if (count == 2) {
/*  773 */         int[] array = TextField.this.wordUnderCursor(x);
/*  774 */         TextField.this.setSelection(array[0], array[1]);
/*      */       } 
/*  776 */       if (count == 3) TextField.this.selectAll(); 
/*      */     }
/*      */     
/*      */     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  780 */       if (!super.touchDown(event, x, y, pointer, button)) return false; 
/*  781 */       if (pointer == 0 && button != 0) return false; 
/*  782 */       if (TextField.this.disabled) return true; 
/*  783 */       setCursorPosition(x, y);
/*  784 */       TextField.this.selectionStart = TextField.this.cursor;
/*  785 */       Stage stage = TextField.this.getStage();
/*  786 */       if (stage != null) stage.setKeyboardFocus(TextField.this); 
/*  787 */       TextField.this.keyboard.show(true);
/*  788 */       TextField.this.hasSelection = true;
/*  789 */       return true;
/*      */     }
/*      */     
/*      */     public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  793 */       super.touchDragged(event, x, y, pointer);
/*  794 */       setCursorPosition(x, y);
/*      */     }
/*      */     
/*      */     public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  798 */       if (TextField.this.selectionStart == TextField.this.cursor) TextField.this.hasSelection = false; 
/*  799 */       super.touchUp(event, x, y, pointer, button);
/*      */     }
/*      */     
/*      */     protected void setCursorPosition(float x, float y) {
/*  803 */       TextField.this.lastBlink = 0L;
/*  804 */       TextField.this.cursorOn = false;
/*  805 */       TextField.this.cursor = TextField.this.letterUnderCursor(x);
/*      */     }
/*      */     
/*      */     protected void goHome(boolean jump) {
/*  809 */       TextField.this.cursor = 0;
/*      */     }
/*      */     
/*      */     protected void goEnd(boolean jump) {
/*  813 */       TextField.this.cursor = TextField.this.text.length();
/*      */     }
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
/*      */     public boolean keyDown(InputEvent event, int keycode) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   4: getfield disabled : Z
/*      */       //   7: ifeq -> 12
/*      */       //   10: iconst_0
/*      */       //   11: ireturn
/*      */       //   12: aload_0
/*      */       //   13: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   16: lconst_0
/*      */       //   17: putfield lastBlink : J
/*      */       //   20: aload_0
/*      */       //   21: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   24: iconst_0
/*      */       //   25: putfield cursorOn : Z
/*      */       //   28: aload_0
/*      */       //   29: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   32: invokevirtual getStage : ()Lcom/badlogic/gdx/scenes/scene2d/Stage;
/*      */       //   35: astore_3
/*      */       //   36: aload_3
/*      */       //   37: ifnull -> 51
/*      */       //   40: aload_3
/*      */       //   41: invokevirtual getKeyboardFocus : ()Lcom/badlogic/gdx/scenes/scene2d/Actor;
/*      */       //   44: aload_0
/*      */       //   45: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   48: if_acmpeq -> 53
/*      */       //   51: iconst_0
/*      */       //   52: ireturn
/*      */       //   53: iconst_0
/*      */       //   54: istore #4
/*      */       //   56: invokestatic ctrl : ()Z
/*      */       //   59: istore #5
/*      */       //   61: iload #5
/*      */       //   63: ifeq -> 80
/*      */       //   66: aload_0
/*      */       //   67: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   70: getfield passwordMode : Z
/*      */       //   73: ifne -> 80
/*      */       //   76: iconst_1
/*      */       //   77: goto -> 81
/*      */       //   80: iconst_0
/*      */       //   81: istore #6
/*      */       //   83: iload #5
/*      */       //   85: ifeq -> 217
/*      */       //   88: iload_2
/*      */       //   89: bipush #50
/*      */       //   91: if_icmpne -> 117
/*      */       //   94: aload_0
/*      */       //   95: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   98: aload_0
/*      */       //   99: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   102: getfield clipboard : Lcom/badlogic/gdx/utils/Clipboard;
/*      */       //   105: invokeinterface getContents : ()Ljava/lang/String;
/*      */       //   110: iconst_1
/*      */       //   111: invokevirtual paste : (Ljava/lang/String;Z)V
/*      */       //   114: iconst_1
/*      */       //   115: istore #4
/*      */       //   117: iload_2
/*      */       //   118: bipush #31
/*      */       //   120: if_icmpeq -> 130
/*      */       //   123: iload_2
/*      */       //   124: sipush #133
/*      */       //   127: if_icmpne -> 139
/*      */       //   130: aload_0
/*      */       //   131: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   134: invokevirtual copy : ()V
/*      */       //   137: iconst_1
/*      */       //   138: ireturn
/*      */       //   139: iload_2
/*      */       //   140: bipush #52
/*      */       //   142: if_icmpne -> 155
/*      */       //   145: aload_0
/*      */       //   146: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   149: iconst_1
/*      */       //   150: invokevirtual cut : (Z)V
/*      */       //   153: iconst_1
/*      */       //   154: ireturn
/*      */       //   155: iload_2
/*      */       //   156: bipush #29
/*      */       //   158: if_icmpne -> 170
/*      */       //   161: aload_0
/*      */       //   162: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   165: invokevirtual selectAll : ()V
/*      */       //   168: iconst_1
/*      */       //   169: ireturn
/*      */       //   170: iload_2
/*      */       //   171: bipush #54
/*      */       //   173: if_icmpne -> 217
/*      */       //   176: aload_0
/*      */       //   177: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   180: getfield text : Ljava/lang/String;
/*      */       //   183: astore #7
/*      */       //   185: aload_0
/*      */       //   186: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   189: aload_0
/*      */       //   190: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   193: getfield undoText : Ljava/lang/String;
/*      */       //   196: invokevirtual setText : (Ljava/lang/String;)V
/*      */       //   199: aload_0
/*      */       //   200: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   203: aload #7
/*      */       //   205: putfield undoText : Ljava/lang/String;
/*      */       //   208: aload_0
/*      */       //   209: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   212: invokevirtual updateDisplayText : ()V
/*      */       //   215: iconst_1
/*      */       //   216: ireturn
/*      */       //   217: invokestatic shift : ()Z
/*      */       //   220: ifeq -> 374
/*      */       //   223: iload_2
/*      */       //   224: sipush #133
/*      */       //   227: if_icmpne -> 250
/*      */       //   230: aload_0
/*      */       //   231: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   234: aload_0
/*      */       //   235: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   238: getfield clipboard : Lcom/badlogic/gdx/utils/Clipboard;
/*      */       //   241: invokeinterface getContents : ()Ljava/lang/String;
/*      */       //   246: iconst_1
/*      */       //   247: invokevirtual paste : (Ljava/lang/String;Z)V
/*      */       //   250: iload_2
/*      */       //   251: bipush #112
/*      */       //   253: if_icmpne -> 264
/*      */       //   256: aload_0
/*      */       //   257: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   260: iconst_1
/*      */       //   261: invokevirtual cut : (Z)V
/*      */       //   264: aload_0
/*      */       //   265: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   268: getfield cursor : I
/*      */       //   271: istore #7
/*      */       //   273: iload_2
/*      */       //   274: bipush #21
/*      */       //   276: if_icmpne -> 295
/*      */       //   279: aload_0
/*      */       //   280: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   283: iconst_0
/*      */       //   284: iload #6
/*      */       //   286: invokevirtual moveCursor : (ZZ)V
/*      */       //   289: iconst_1
/*      */       //   290: istore #4
/*      */       //   292: goto -> 344
/*      */       //   295: iload_2
/*      */       //   296: bipush #22
/*      */       //   298: if_icmpne -> 317
/*      */       //   301: aload_0
/*      */       //   302: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   305: iconst_1
/*      */       //   306: iload #6
/*      */       //   308: invokevirtual moveCursor : (ZZ)V
/*      */       //   311: iconst_1
/*      */       //   312: istore #4
/*      */       //   314: goto -> 344
/*      */       //   317: iload_2
/*      */       //   318: iconst_3
/*      */       //   319: if_icmpne -> 331
/*      */       //   322: aload_0
/*      */       //   323: iload #6
/*      */       //   325: invokevirtual goHome : (Z)V
/*      */       //   328: goto -> 344
/*      */       //   331: iload_2
/*      */       //   332: sipush #132
/*      */       //   335: if_icmpne -> 464
/*      */       //   338: aload_0
/*      */       //   339: iload #6
/*      */       //   341: invokevirtual goEnd : (Z)V
/*      */       //   344: aload_0
/*      */       //   345: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   348: getfield hasSelection : Z
/*      */       //   351: ifne -> 371
/*      */       //   354: aload_0
/*      */       //   355: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   358: iload #7
/*      */       //   360: putfield selectionStart : I
/*      */       //   363: aload_0
/*      */       //   364: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   367: iconst_1
/*      */       //   368: putfield hasSelection : Z
/*      */       //   371: goto -> 464
/*      */       //   374: iload_2
/*      */       //   375: bipush #21
/*      */       //   377: if_icmpne -> 400
/*      */       //   380: aload_0
/*      */       //   381: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   384: iconst_0
/*      */       //   385: iload #6
/*      */       //   387: invokevirtual moveCursor : (ZZ)V
/*      */       //   390: aload_0
/*      */       //   391: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   394: invokevirtual clearSelection : ()V
/*      */       //   397: iconst_1
/*      */       //   398: istore #4
/*      */       //   400: iload_2
/*      */       //   401: bipush #22
/*      */       //   403: if_icmpne -> 426
/*      */       //   406: aload_0
/*      */       //   407: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   410: iconst_1
/*      */       //   411: iload #6
/*      */       //   413: invokevirtual moveCursor : (ZZ)V
/*      */       //   416: aload_0
/*      */       //   417: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   420: invokevirtual clearSelection : ()V
/*      */       //   423: iconst_1
/*      */       //   424: istore #4
/*      */       //   426: iload_2
/*      */       //   427: iconst_3
/*      */       //   428: if_icmpne -> 444
/*      */       //   431: aload_0
/*      */       //   432: iload #6
/*      */       //   434: invokevirtual goHome : (Z)V
/*      */       //   437: aload_0
/*      */       //   438: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   441: invokevirtual clearSelection : ()V
/*      */       //   444: iload_2
/*      */       //   445: sipush #132
/*      */       //   448: if_icmpne -> 464
/*      */       //   451: aload_0
/*      */       //   452: iload #6
/*      */       //   454: invokevirtual goEnd : (Z)V
/*      */       //   457: aload_0
/*      */       //   458: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   461: invokevirtual clearSelection : ()V
/*      */       //   464: aload_0
/*      */       //   465: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   468: aload_0
/*      */       //   469: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   472: getfield cursor : I
/*      */       //   475: iconst_0
/*      */       //   476: aload_0
/*      */       //   477: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   480: getfield text : Ljava/lang/String;
/*      */       //   483: invokevirtual length : ()I
/*      */       //   486: invokestatic clamp : (III)I
/*      */       //   489: putfield cursor : I
/*      */       //   492: iload #4
/*      */       //   494: ifeq -> 502
/*      */       //   497: aload_0
/*      */       //   498: iload_2
/*      */       //   499: invokevirtual scheduleKeyRepeatTask : (I)V
/*      */       //   502: iconst_1
/*      */       //   503: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #817	-> 0
/*      */       //   #819	-> 12
/*      */       //   #820	-> 20
/*      */       //   #822	-> 28
/*      */       //   #823	-> 36
/*      */       //   #825	-> 53
/*      */       //   #826	-> 56
/*      */       //   #827	-> 61
/*      */       //   #829	-> 83
/*      */       //   #830	-> 88
/*      */       //   #831	-> 94
/*      */       //   #832	-> 114
/*      */       //   #834	-> 117
/*      */       //   #835	-> 130
/*      */       //   #836	-> 137
/*      */       //   #838	-> 139
/*      */       //   #839	-> 145
/*      */       //   #840	-> 153
/*      */       //   #842	-> 155
/*      */       //   #843	-> 161
/*      */       //   #844	-> 168
/*      */       //   #846	-> 170
/*      */       //   #847	-> 176
/*      */       //   #848	-> 185
/*      */       //   #849	-> 199
/*      */       //   #850	-> 208
/*      */       //   #851	-> 215
/*      */       //   #855	-> 217
/*      */       //   #856	-> 223
/*      */       //   #857	-> 250
/*      */       //   #860	-> 264
/*      */       //   #863	-> 273
/*      */       //   #864	-> 279
/*      */       //   #865	-> 289
/*      */       //   #866	-> 292
/*      */       //   #868	-> 295
/*      */       //   #869	-> 301
/*      */       //   #870	-> 311
/*      */       //   #871	-> 314
/*      */       //   #873	-> 317
/*      */       //   #874	-> 322
/*      */       //   #875	-> 328
/*      */       //   #877	-> 331
/*      */       //   #878	-> 338
/*      */       //   #883	-> 344
/*      */       //   #884	-> 354
/*      */       //   #885	-> 363
/*      */       //   #887	-> 371
/*      */       //   #890	-> 374
/*      */       //   #891	-> 380
/*      */       //   #892	-> 390
/*      */       //   #893	-> 397
/*      */       //   #895	-> 400
/*      */       //   #896	-> 406
/*      */       //   #897	-> 416
/*      */       //   #898	-> 423
/*      */       //   #900	-> 426
/*      */       //   #901	-> 431
/*      */       //   #902	-> 437
/*      */       //   #904	-> 444
/*      */       //   #905	-> 451
/*      */       //   #906	-> 457
/*      */       //   #909	-> 464
/*      */       //   #911	-> 492
/*      */       //   #912	-> 497
/*      */       //   #914	-> 502
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   185	32	7	oldText	Ljava/lang/String;
/*      */       //   273	98	7	temp	I
/*      */       //   0	504	0	this	Lcom/badlogic/gdx/scenes/scene2d/ui/TextField$TextFieldClickListener;
/*      */       //   0	504	1	event	Lcom/badlogic/gdx/scenes/scene2d/InputEvent;
/*      */       //   0	504	2	keycode	I
/*      */       //   36	468	3	stage	Lcom/badlogic/gdx/scenes/scene2d/Stage;
/*      */       //   56	448	4	repeat	Z
/*      */       //   61	443	5	ctrl	Z
/*      */       //   83	421	6	jump	Z
/*      */     }
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
/*      */     protected void scheduleKeyRepeatTask(int keycode) {
/*  918 */       if (!TextField.this.keyRepeatTask.isScheduled() || TextField.this.keyRepeatTask.keycode != keycode) {
/*  919 */         TextField.this.keyRepeatTask.keycode = keycode;
/*  920 */         TextField.this.keyRepeatTask.cancel();
/*  921 */         Timer.schedule(TextField.this.keyRepeatTask, TextField.keyRepeatInitialTime, TextField.keyRepeatTime);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean keyUp(InputEvent event, int keycode) {
/*  926 */       if (TextField.this.disabled) return false; 
/*  927 */       TextField.this.keyRepeatTask.cancel();
/*  928 */       return true;
/*      */     }
/*      */     
/*      */     public boolean keyTyped(InputEvent event, char character) {
/*  932 */       if (TextField.this.disabled) return false;
/*      */ 
/*      */       
/*  935 */       switch (character) {
/*      */         case '\b':
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\r':
/*      */           break;
/*      */         default:
/*  942 */           if (character < ' ') return false; 
/*      */           break;
/*      */       } 
/*  945 */       Stage stage = TextField.this.getStage();
/*  946 */       if (stage == null || stage.getKeyboardFocus() != TextField.this) return false;
/*      */       
/*  948 */       if (UIUtils.isMac && Gdx.input.isKeyPressed(63)) return true;
/*      */       
/*  950 */       if ((character == '\t' || character == '\n') && TextField.this.focusTraversal) {
/*  951 */         TextField.this.next(UIUtils.shift());
/*      */       } else {
/*  953 */         boolean delete = (character == '');
/*  954 */         boolean backspace = (character == '\b');
/*  955 */         boolean enter = (character == '\r' || character == '\n');
/*  956 */         boolean add = enter ? TextField.this.writeEnters : ((!TextField.this.onlyFontChars || TextField.this.style.font.getData().hasGlyph(character)));
/*  957 */         boolean remove = (backspace || delete);
/*  958 */         if (add || remove) {
/*  959 */           String oldText = TextField.this.text;
/*  960 */           int oldCursor = TextField.this.cursor;
/*  961 */           if (TextField.this.hasSelection) {
/*  962 */             TextField.this.cursor = TextField.this.delete(false);
/*      */           } else {
/*  964 */             if (backspace && TextField.this.cursor > 0) {
/*  965 */               TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor - 1) + TextField.this.text.substring(TextField.this.cursor--);
/*  966 */               TextField.this.renderOffset = 0.0F;
/*      */             } 
/*  968 */             if (delete && TextField.this.cursor < TextField.this.text.length()) {
/*  969 */               TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor) + TextField.this.text.substring(TextField.this.cursor + 1);
/*      */             }
/*      */           } 
/*  972 */           if (add && !remove) {
/*      */             
/*  974 */             if (!enter && TextField.this.filter != null && !TextField.this.filter.acceptChar(TextField.this, character)) return true; 
/*  975 */             if (!TextField.this.withinMaxLength(TextField.this.text.length())) return true; 
/*  976 */             String insertion = enter ? "\n" : String.valueOf(character);
/*  977 */             TextField.this.text = TextField.this.insert(TextField.this.cursor++, insertion, TextField.this.text);
/*      */           } 
/*  979 */           String tempUndoText = TextField.this.undoText;
/*  980 */           if (TextField.this.changeText(oldText, TextField.this.text)) {
/*  981 */             long time = System.currentTimeMillis();
/*  982 */             if (time - 750L > TextField.this.lastChangeTime) TextField.this.undoText = oldText; 
/*  983 */             TextField.this.lastChangeTime = time;
/*      */           } else {
/*  985 */             TextField.this.cursor = oldCursor;
/*  986 */           }  TextField.this.updateDisplayText();
/*      */         } 
/*      */       } 
/*  989 */       if (TextField.this.listener != null) TextField.this.listener.keyTyped(TextField.this, character); 
/*  990 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TextFieldStyle
/*      */   {
/*      */     public BitmapFont font;
/*      */     
/*      */     public Color fontColor;
/*      */     public Color focusedFontColor;
/*      */     public Color disabledFontColor;
/*      */     public Drawable background;
/*      */     public Drawable focusedBackground;
/*      */     public Drawable disabledBackground;
/*      */     public Drawable cursor;
/*      */     public Drawable selection;
/*      */     public BitmapFont messageFont;
/*      */     public Color messageFontColor;
/*      */     
/*      */     public TextFieldStyle() {}
/*      */     
/*      */     public TextFieldStyle(BitmapFont font, Color fontColor, Drawable cursor, Drawable selection, Drawable background) {
/* 1013 */       this.background = background;
/* 1014 */       this.cursor = cursor;
/* 1015 */       this.font = font;
/* 1016 */       this.fontColor = fontColor;
/* 1017 */       this.selection = selection;
/*      */     }
/*      */     
/*      */     public TextFieldStyle(TextFieldStyle style) {
/* 1021 */       this.messageFont = style.messageFont;
/* 1022 */       if (style.messageFontColor != null) this.messageFontColor = new Color(style.messageFontColor); 
/* 1023 */       this.background = style.background;
/* 1024 */       this.focusedBackground = style.focusedBackground;
/* 1025 */       this.disabledBackground = style.disabledBackground;
/* 1026 */       this.cursor = style.cursor;
/* 1027 */       this.font = style.font;
/* 1028 */       if (style.fontColor != null) this.fontColor = new Color(style.fontColor); 
/* 1029 */       if (style.focusedFontColor != null) this.focusedFontColor = new Color(style.focusedFontColor); 
/* 1030 */       if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor); 
/* 1031 */       this.selection = style.selection;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\TextField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */