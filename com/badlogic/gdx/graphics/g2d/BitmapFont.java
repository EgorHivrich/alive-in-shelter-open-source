/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.FloatArray;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class BitmapFont
/*     */   implements Disposable
/*     */ {
/*     */   private static final int LOG2_PAGE_SIZE = 9;
/*     */   private static final int PAGE_SIZE = 512;
/*     */   private static final int PAGES = 128;
/*     */   final BitmapFontData data;
/*     */   Array<TextureRegion> regions;
/*     */   private final BitmapFontCache cache;
/*     */   private boolean flipped;
/*     */   boolean integer;
/*     */   private boolean ownsTexture;
/*     */   
/*     */   public BitmapFont() {
/*  70 */     this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(boolean flip) {
/*  78 */     this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), flip, true);
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
/*     */   public BitmapFont(FileHandle fontFile, TextureRegion region) {
/*  91 */     this(fontFile, region, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, TextureRegion region, boolean flip) {
/* 101 */     this(new BitmapFontData(fontFile, flip), region, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile) {
/* 107 */     this(fontFile, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, boolean flip) {
/* 114 */     this(new BitmapFontData(fontFile, flip), (TextureRegion)null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip) {
/* 121 */     this(fontFile, imageFile, flip, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
/* 129 */     this(new BitmapFontData(fontFile, flip), new TextureRegion(new Texture(imageFile, false)), integer);
/* 130 */     this.ownsTexture = true;
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
/*     */   public BitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
/* 142 */     this(data, (region != null) ? Array.with((Object[])new TextureRegion[] { region }, ) : null, integer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(BitmapFontData data, Array<TextureRegion> pageRegions, boolean integer) {
/* 150 */     this.flipped = data.flipped;
/* 151 */     this.data = data;
/* 152 */     this.integer = integer;
/*     */     
/* 154 */     if (pageRegions == null || pageRegions.size == 0) {
/*     */       
/* 156 */       int n = data.imagePaths.length;
/* 157 */       this.regions = new Array(n);
/* 158 */       for (int i = 0; i < n; i++) {
/*     */         FileHandle file;
/* 160 */         if (data.fontFile == null) {
/* 161 */           file = Gdx.files.internal(data.imagePaths[i]);
/*     */         } else {
/* 163 */           file = Gdx.files.getFileHandle(data.imagePaths[i], data.fontFile.type());
/* 164 */         }  this.regions.add(new TextureRegion(new Texture(file, false)));
/*     */       } 
/* 166 */       this.ownsTexture = true;
/*     */     } else {
/* 168 */       this.regions = pageRegions;
/* 169 */       this.ownsTexture = false;
/*     */     } 
/*     */     
/* 172 */     this.cache = newFontCache();
/*     */     
/* 174 */     load(data);
/*     */   }
/*     */   
/*     */   protected void load(BitmapFontData data) {
/* 178 */     for (Glyph[] page : data.glyphs) {
/* 179 */       if (page != null)
/* 180 */         for (Glyph glyph : page) {
/* 181 */           if (glyph != null) data.setGlyphRegion(glyph, (TextureRegion)this.regions.get(glyph.page)); 
/*     */         }  
/* 183 */     }  if (data.missingGlyph != null) data.setGlyphRegion(data.missingGlyph, (TextureRegion)this.regions.get(data.missingGlyph.page));
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y) {
/* 189 */     this.cache.clear();
/* 190 */     GlyphLayout layout = this.cache.addText(str, x, y);
/* 191 */     this.cache.draw(batch);
/* 192 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
/* 198 */     this.cache.clear();
/* 199 */     GlyphLayout layout = this.cache.addText(str, x, y, targetWidth, halign, wrap);
/* 200 */     this.cache.draw(batch);
/* 201 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap) {
/* 208 */     this.cache.clear();
/* 209 */     GlyphLayout layout = this.cache.addText(str, x, y, start, end, targetWidth, halign, wrap);
/* 210 */     this.cache.draw(batch);
/* 211 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap, String truncate) {
/* 218 */     this.cache.clear();
/* 219 */     GlyphLayout layout = this.cache.addText(str, x, y, start, end, targetWidth, halign, wrap, truncate);
/* 220 */     this.cache.draw(batch);
/* 221 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, GlyphLayout layout, float x, float y) {
/* 227 */     this.cache.clear();
/* 228 */     this.cache.addText(layout, x, y);
/* 229 */     this.cache.draw(batch);
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 234 */     return this.cache.getColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 239 */     this.cache.getColor().set(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 244 */     this.cache.getColor().set(r, g, b, a);
/*     */   }
/*     */   
/*     */   public float getScaleX() {
/* 248 */     return this.data.scaleX;
/*     */   }
/*     */   
/*     */   public float getScaleY() {
/* 252 */     return this.data.scaleY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion getRegion() {
/* 259 */     return (TextureRegion)this.regions.first();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<TextureRegion> getRegions() {
/* 265 */     return this.regions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion getRegion(int index) {
/* 271 */     return (TextureRegion)this.regions.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLineHeight() {
/* 276 */     return this.data.lineHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpaceWidth() {
/* 281 */     return this.data.spaceWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getXHeight() {
/* 286 */     return this.data.xHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCapHeight() {
/* 292 */     return this.data.capHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAscent() {
/* 297 */     return this.data.ascent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDescent() {
/* 303 */     return this.data.descent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlipped() {
/* 308 */     return this.flipped;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 313 */     if (this.ownsTexture) {
/* 314 */       for (int i = 0; i < this.regions.size; i++) {
/* 315 */         ((TextureRegion)this.regions.get(i)).getTexture().dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFixedWidthGlyphs(CharSequence glyphs) {
/* 322 */     BitmapFontData data = this.data;
/* 323 */     int maxAdvance = 0; int index, end;
/* 324 */     for (index = 0, end = glyphs.length(); index < end; index++) {
/* 325 */       Glyph g = data.getGlyph(glyphs.charAt(index));
/* 326 */       if (g != null && g.xadvance > maxAdvance) maxAdvance = g.xadvance; 
/*     */     } 
/* 328 */     for (index = 0, end = glyphs.length(); index < end; index++) {
/* 329 */       Glyph g = data.getGlyph(glyphs.charAt(index));
/* 330 */       if (g != null) {
/* 331 */         g.xoffset += Math.round(((maxAdvance - g.xadvance) / 2));
/* 332 */         g.xadvance = maxAdvance;
/* 333 */         g.kerning = (byte[][])null;
/* 334 */         g.fixedWidth = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setUseIntegerPositions(boolean integer) {
/* 340 */     this.integer = integer;
/* 341 */     this.cache.setUseIntegerPositions(integer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesIntegerPositions() {
/* 346 */     return this.integer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFontCache getCache() {
/* 353 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public BitmapFontData getData() {
/* 358 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ownsTexture() {
/* 363 */     return this.ownsTexture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwnsTexture(boolean ownsTexture) {
/* 370 */     this.ownsTexture = ownsTexture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFontCache newFontCache() {
/* 379 */     return new BitmapFontCache(this, this.integer);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 383 */     if (this.data.fontFile != null) return this.data.fontFile.nameWithoutExtension(); 
/* 384 */     return super.toString();
/*     */   }
/*     */   public static class Glyph { public int id;
/*     */     public int srcX;
/*     */     public int srcY;
/*     */     public int width;
/*     */     public int height;
/*     */     public float u;
/*     */     public float v;
/*     */     public float u2;
/*     */     public float v2;
/*     */     public int xoffset;
/*     */     public int yoffset;
/*     */     public int xadvance;
/*     */     public byte[][] kerning;
/*     */     public boolean fixedWidth;
/* 400 */     public int page = 0;
/*     */     
/*     */     public int getKerning(char ch) {
/* 403 */       if (this.kerning != null) {
/* 404 */         byte[] page = this.kerning[ch >>> 9];
/* 405 */         if (page != null) return page[ch & 0x1FF]; 
/*     */       } 
/* 407 */       return 0;
/*     */     }
/*     */     
/*     */     public void setKerning(int ch, int value) {
/* 411 */       if (this.kerning == null) this.kerning = new byte[128][]; 
/* 412 */       byte[] page = this.kerning[ch >>> 9];
/* 413 */       if (page == null) this.kerning[ch >>> 9] = page = new byte[512]; 
/* 414 */       page[ch & 0x1FF] = (byte)value;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 418 */       return Character.toString((char)this.id);
/*     */     } }
/*     */ 
/*     */   
/*     */   static int indexOf(CharSequence text, char ch, int start) {
/* 423 */     int n = text.length();
/* 424 */     for (; start < n; start++) {
/* 425 */       if (text.charAt(start) == ch) return start; 
/* 426 */     }  return n;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class BitmapFontData
/*     */   {
/*     */     public String[] imagePaths;
/*     */     public FileHandle fontFile;
/*     */     public boolean flipped;
/*     */     public float padTop;
/*     */     public float padRight;
/*     */     public float padBottom;
/*     */     public float padLeft;
/*     */     public float lineHeight;
/* 440 */     public float capHeight = 1.0F;
/*     */     
/*     */     public float ascent;
/*     */     
/*     */     public float descent;
/*     */     public float down;
/* 446 */     public float scaleX = 1.0F; public float scaleY = 1.0F;
/*     */     
/*     */     public boolean markupEnabled;
/*     */     
/*     */     public float cursorX;
/*     */     
/* 452 */     public final BitmapFont.Glyph[][] glyphs = new BitmapFont.Glyph[128][];
/*     */ 
/*     */     
/*     */     public BitmapFont.Glyph missingGlyph;
/*     */     
/*     */     public float spaceWidth;
/*     */     
/* 459 */     public float xHeight = 1.0F;
/*     */     
/*     */     public char[] breakChars;
/*     */     
/* 463 */     public char[] xChars = new char[] { 'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z' };
/* 464 */     public char[] capChars = new char[] { 'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
/*     */ 
/*     */ 
/*     */     
/*     */     public BitmapFontData() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public BitmapFontData(FileHandle fontFile, boolean flip) {
/* 473 */       this.fontFile = fontFile;
/* 474 */       this.flipped = flip;
/* 475 */       load(fontFile, flip);
/*     */     }
/*     */     
/*     */     public void load(FileHandle fontFile, boolean flip) {
/* 479 */       if (this.imagePaths != null) throw new IllegalStateException("Already loaded.");
/*     */       
/* 481 */       BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
/*     */       try {
/* 483 */         String line = reader.readLine();
/* 484 */         if (line == null) throw new GdxRuntimeException("File is empty.");
/*     */         
/* 486 */         line = line.substring(line.indexOf("padding=") + 8);
/* 487 */         String[] padding = line.substring(0, line.indexOf(' ')).split(",", 4);
/* 488 */         if (padding.length != 4) throw new GdxRuntimeException("Invalid padding."); 
/* 489 */         this.padTop = Integer.parseInt(padding[0]);
/* 490 */         this.padLeft = Integer.parseInt(padding[1]);
/* 491 */         this.padBottom = Integer.parseInt(padding[2]);
/* 492 */         this.padRight = Integer.parseInt(padding[3]);
/* 493 */         float padY = this.padTop + this.padBottom;
/*     */         
/* 495 */         line = reader.readLine();
/* 496 */         if (line == null) throw new GdxRuntimeException("Missing common header."); 
/* 497 */         String[] common = line.split(" ", 7);
/*     */ 
/*     */         
/* 500 */         if (common.length < 3) throw new GdxRuntimeException("Invalid common header.");
/*     */         
/* 502 */         if (!common[1].startsWith("lineHeight=")) throw new GdxRuntimeException("Missing: lineHeight"); 
/* 503 */         this.lineHeight = Integer.parseInt(common[1].substring(11));
/*     */         
/* 505 */         if (!common[2].startsWith("base=")) throw new GdxRuntimeException("Missing: base"); 
/* 506 */         float baseLine = Integer.parseInt(common[2].substring(5));
/*     */         
/* 508 */         int pageCount = 1;
/* 509 */         if (common.length >= 6 && common[5] != null && common[5].startsWith("pages=")) {
/*     */           try {
/* 511 */             pageCount = Math.max(1, Integer.parseInt(common[5].substring(6)));
/* 512 */           } catch (NumberFormatException numberFormatException) {}
/*     */         }
/*     */ 
/*     */         
/* 516 */         this.imagePaths = new String[pageCount];
/*     */ 
/*     */         
/* 519 */         for (int p = 0; p < pageCount; p++) {
/*     */           
/* 521 */           line = reader.readLine();
/* 522 */           if (line == null) throw new GdxRuntimeException("Missing additional page definitions."); 
/* 523 */           String[] pageLine = line.split(" ", 4);
/* 524 */           if (!pageLine[2].startsWith("file=")) throw new GdxRuntimeException("Missing: file");
/*     */ 
/*     */           
/* 527 */           if (pageLine[1].startsWith("id=")) {
/*     */             try {
/* 529 */               int pageID = Integer.parseInt(pageLine[1].substring(3));
/* 530 */               if (pageID != p)
/* 531 */                 throw new GdxRuntimeException("Page IDs must be indices starting at 0: " + pageLine[1].substring(3)); 
/* 532 */             } catch (NumberFormatException ex) {
/* 533 */               throw new GdxRuntimeException("Invalid page id: " + pageLine[1].substring(3), ex);
/*     */             } 
/*     */           }
/*     */           
/* 537 */           String fileName = null;
/* 538 */           if (pageLine[2].endsWith("\"")) {
/* 539 */             fileName = pageLine[2].substring(6, pageLine[2].length() - 1);
/*     */           } else {
/* 541 */             fileName = pageLine[2].substring(5, pageLine[2].length());
/*     */           } 
/*     */           
/* 544 */           this.imagePaths[p] = fontFile.parent().child(fileName).path().replaceAll("\\\\", "/");
/*     */         } 
/* 546 */         this.descent = 0.0F;
/*     */         
/*     */         while (true) {
/* 549 */           line = reader.readLine();
/* 550 */           if (line == null || 
/* 551 */             line.startsWith("kernings "))
/* 552 */             break;  if (!line.startsWith("char "))
/*     */             continue; 
/* 554 */           BitmapFont.Glyph glyph = new BitmapFont.Glyph();
/*     */           
/* 556 */           StringTokenizer tokens = new StringTokenizer(line, " =");
/* 557 */           tokens.nextToken();
/* 558 */           tokens.nextToken();
/* 559 */           int ch = Integer.parseInt(tokens.nextToken());
/* 560 */           if (ch <= 0) {
/* 561 */             this.missingGlyph = glyph;
/* 562 */           } else if (ch <= 65535) {
/* 563 */             setGlyph(ch, glyph);
/*     */           } else {
/*     */             continue;
/* 566 */           }  glyph.id = ch;
/* 567 */           tokens.nextToken();
/* 568 */           glyph.srcX = Integer.parseInt(tokens.nextToken());
/* 569 */           tokens.nextToken();
/* 570 */           glyph.srcY = Integer.parseInt(tokens.nextToken());
/* 571 */           tokens.nextToken();
/* 572 */           glyph.width = Integer.parseInt(tokens.nextToken());
/* 573 */           tokens.nextToken();
/* 574 */           glyph.height = Integer.parseInt(tokens.nextToken());
/* 575 */           tokens.nextToken();
/* 576 */           glyph.xoffset = Integer.parseInt(tokens.nextToken());
/* 577 */           tokens.nextToken();
/* 578 */           if (flip) {
/* 579 */             glyph.yoffset = Integer.parseInt(tokens.nextToken());
/*     */           } else {
/* 581 */             glyph.yoffset = -(glyph.height + Integer.parseInt(tokens.nextToken()));
/* 582 */           }  tokens.nextToken();
/* 583 */           glyph.xadvance = Integer.parseInt(tokens.nextToken());
/*     */ 
/*     */           
/* 586 */           if (tokens.hasMoreTokens()) tokens.nextToken(); 
/* 587 */           if (tokens.hasMoreTokens()) {
/*     */             try {
/* 589 */               glyph.page = Integer.parseInt(tokens.nextToken());
/* 590 */             } catch (NumberFormatException numberFormatException) {}
/*     */           }
/*     */ 
/*     */           
/* 594 */           if (glyph.width > 0 && glyph.height > 0) this.descent = Math.min(baseLine + glyph.yoffset, this.descent); 
/*     */         } 
/* 596 */         this.descent += this.padBottom;
/*     */         
/*     */         while (true) {
/* 599 */           line = reader.readLine();
/* 600 */           if (line == null || 
/* 601 */             !line.startsWith("kerning "))
/*     */             break; 
/* 603 */           StringTokenizer tokens = new StringTokenizer(line, " =");
/* 604 */           tokens.nextToken();
/* 605 */           tokens.nextToken();
/* 606 */           int first = Integer.parseInt(tokens.nextToken());
/* 607 */           tokens.nextToken();
/* 608 */           int second = Integer.parseInt(tokens.nextToken());
/* 609 */           if (first < 0 || first > 65535 || second < 0 || second > 65535)
/* 610 */             continue;  BitmapFont.Glyph glyph = getGlyph((char)first);
/* 611 */           tokens.nextToken();
/* 612 */           int amount = Integer.parseInt(tokens.nextToken());
/* 613 */           if (glyph != null) {
/* 614 */             glyph.setKerning(second, amount);
/*     */           }
/*     */         } 
/*     */         
/* 618 */         BitmapFont.Glyph spaceGlyph = getGlyph(' ');
/* 619 */         if (spaceGlyph == null) {
/* 620 */           spaceGlyph = new BitmapFont.Glyph();
/* 621 */           spaceGlyph.id = 32;
/* 622 */           BitmapFont.Glyph xadvanceGlyph = getGlyph('l');
/* 623 */           if (xadvanceGlyph == null) xadvanceGlyph = getFirstGlyph(); 
/* 624 */           spaceGlyph.xadvance = xadvanceGlyph.xadvance;
/* 625 */           setGlyph(32, spaceGlyph);
/*     */         } 
/* 627 */         if (spaceGlyph.width == 0) {
/* 628 */           spaceGlyph.width = (int)(this.padLeft + spaceGlyph.xadvance + this.padRight);
/* 629 */           spaceGlyph.xoffset = (int)-this.padLeft;
/*     */         } 
/* 631 */         this.spaceWidth = spaceGlyph.width;
/*     */         
/* 633 */         BitmapFont.Glyph xGlyph = null;
/* 634 */         for (char xChar : this.xChars) {
/* 635 */           xGlyph = getGlyph(xChar);
/* 636 */           if (xGlyph != null)
/*     */             break; 
/* 638 */         }  if (xGlyph == null) xGlyph = getFirstGlyph(); 
/* 639 */         this.xHeight = xGlyph.height - padY;
/*     */         
/* 641 */         BitmapFont.Glyph capGlyph = null;
/* 642 */         for (char capChar : this.capChars) {
/* 643 */           capGlyph = getGlyph(capChar);
/* 644 */           if (capGlyph != null)
/*     */             break; 
/* 646 */         }  if (capGlyph == null) {
/* 647 */           for (BitmapFont.Glyph[] page : this.glyphs) {
/* 648 */             if (page != null)
/* 649 */               for (BitmapFont.Glyph glyph : page) {
/* 650 */                 if (glyph != null && glyph.height != 0 && glyph.width != 0)
/* 651 */                   this.capHeight = Math.max(this.capHeight, glyph.height); 
/*     */               }  
/*     */           } 
/*     */         } else {
/* 655 */           this.capHeight = capGlyph.height;
/* 656 */         }  this.capHeight -= padY;
/*     */         
/* 658 */         this.ascent = baseLine - this.capHeight;
/* 659 */         this.down = -this.lineHeight;
/* 660 */         if (flip) {
/* 661 */           this.ascent = -this.ascent;
/* 662 */           this.down = -this.down;
/*     */         } 
/* 664 */       } catch (Exception ex) {
/* 665 */         throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
/*     */       } finally {
/* 667 */         StreamUtils.closeQuietly(reader);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setGlyphRegion(BitmapFont.Glyph glyph, TextureRegion region) {
/* 672 */       Texture texture = region.getTexture();
/* 673 */       float invTexWidth = 1.0F / texture.getWidth();
/* 674 */       float invTexHeight = 1.0F / texture.getHeight();
/*     */       
/* 676 */       float offsetX = 0.0F, offsetY = 0.0F;
/* 677 */       float u = region.u;
/* 678 */       float v = region.v;
/* 679 */       float regionWidth = region.getRegionWidth();
/* 680 */       float regionHeight = region.getRegionHeight();
/* 681 */       if (region instanceof TextureAtlas.AtlasRegion) {
/*     */         
/* 683 */         TextureAtlas.AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion)region;
/* 684 */         offsetX = atlasRegion.offsetX;
/* 685 */         offsetY = (atlasRegion.originalHeight - atlasRegion.packedHeight) - atlasRegion.offsetY;
/*     */       } 
/*     */       
/* 688 */       float x = glyph.srcX;
/* 689 */       float x2 = (glyph.srcX + glyph.width);
/* 690 */       float y = glyph.srcY;
/* 691 */       float y2 = (glyph.srcY + glyph.height);
/*     */ 
/*     */       
/* 694 */       if (offsetX > 0.0F) {
/* 695 */         x -= offsetX;
/* 696 */         if (x < 0.0F) {
/* 697 */           glyph.width = (int)(glyph.width + x);
/* 698 */           glyph.xoffset = (int)(glyph.xoffset - x);
/* 699 */           x = 0.0F;
/*     */         } 
/* 701 */         x2 -= offsetX;
/* 702 */         if (x2 > regionWidth) {
/* 703 */           glyph.width = (int)(glyph.width - x2 - regionWidth);
/* 704 */           x2 = regionWidth;
/*     */         } 
/*     */       } 
/* 707 */       if (offsetY > 0.0F) {
/* 708 */         y -= offsetY;
/* 709 */         if (y < 0.0F) {
/* 710 */           glyph.height = (int)(glyph.height + y);
/* 711 */           y = 0.0F;
/*     */         } 
/* 713 */         y2 -= offsetY;
/* 714 */         if (y2 > regionHeight) {
/* 715 */           float amount = y2 - regionHeight;
/* 716 */           glyph.height = (int)(glyph.height - amount);
/* 717 */           glyph.yoffset = (int)(glyph.yoffset + amount);
/* 718 */           y2 = regionHeight;
/*     */         } 
/*     */       } 
/*     */       
/* 722 */       glyph.u = u + x * invTexWidth;
/* 723 */       glyph.u2 = u + x2 * invTexWidth;
/* 724 */       if (this.flipped) {
/* 725 */         glyph.v = v + y * invTexHeight;
/* 726 */         glyph.v2 = v + y2 * invTexHeight;
/*     */       } else {
/* 728 */         glyph.v2 = v + y * invTexHeight;
/* 729 */         glyph.v = v + y2 * invTexHeight;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setLineHeight(float height) {
/* 735 */       this.lineHeight = height * this.scaleY;
/* 736 */       this.down = this.flipped ? this.lineHeight : -this.lineHeight;
/*     */     }
/*     */     
/*     */     public void setGlyph(int ch, BitmapFont.Glyph glyph) {
/* 740 */       BitmapFont.Glyph[] page = this.glyphs[ch / 512];
/* 741 */       if (page == null) this.glyphs[ch / 512] = page = new BitmapFont.Glyph[512]; 
/* 742 */       page[ch & 0x1FF] = glyph;
/*     */     }
/*     */     
/*     */     public BitmapFont.Glyph getFirstGlyph() {
/* 746 */       for (BitmapFont.Glyph[] page : this.glyphs) {
/* 747 */         if (page != null) {
/* 748 */           BitmapFont.Glyph[] arrayOfGlyph; int i; byte b; for (arrayOfGlyph = page, i = arrayOfGlyph.length, b = 0; b < i; ) { BitmapFont.Glyph glyph = arrayOfGlyph[b];
/* 749 */             if (glyph == null || glyph.height == 0 || glyph.width == 0) { b++; continue; }
/* 750 */              return glyph; }
/*     */         
/*     */         } 
/* 753 */       }  throw new GdxRuntimeException("No glyphs found.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasGlyph(char ch) {
/* 758 */       if (this.missingGlyph != null) return true; 
/* 759 */       return (getGlyph(ch) != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BitmapFont.Glyph getGlyph(char ch) {
/* 766 */       BitmapFont.Glyph[] page = this.glyphs[ch / 512];
/* 767 */       if (page != null) return page[ch & 0x1FF]; 
/* 768 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void getGlyphs(GlyphLayout.GlyphRun run, CharSequence str, int start, int end, boolean tightBounds) {
/* 778 */       boolean markupEnabled = this.markupEnabled;
/* 779 */       float scaleX = this.scaleX;
/* 780 */       BitmapFont.Glyph missingGlyph = this.missingGlyph;
/* 781 */       Array<BitmapFont.Glyph> glyphs = run.glyphs;
/* 782 */       FloatArray xAdvances = run.xAdvances;
/*     */ 
/*     */       
/* 785 */       glyphs.ensureCapacity(end - start);
/* 786 */       xAdvances.ensureCapacity(end - start + 1);
/*     */       
/* 788 */       BitmapFont.Glyph lastGlyph = null;
/* 789 */       while (start < end) {
/* 790 */         char ch = str.charAt(start++);
/* 791 */         BitmapFont.Glyph glyph = getGlyph(ch);
/* 792 */         if (glyph == null) {
/* 793 */           if (missingGlyph == null)
/* 794 */             continue;  glyph = missingGlyph;
/*     */         } 
/*     */         
/* 797 */         glyphs.add(glyph);
/*     */         
/* 799 */         if (lastGlyph == null) {
/* 800 */           xAdvances.add((!tightBounds || glyph.fixedWidth) ? 0.0F : (-glyph.xoffset * scaleX - this.padLeft));
/*     */         } else {
/* 802 */           xAdvances.add((lastGlyph.xadvance + lastGlyph.getKerning(ch)) * scaleX);
/* 803 */         }  lastGlyph = glyph;
/*     */ 
/*     */         
/* 806 */         if (markupEnabled && ch == '[' && start < end && str.charAt(start) == '[') start++; 
/*     */       } 
/* 808 */       if (lastGlyph != null) {
/* 809 */         float lastGlyphWidth = (!tightBounds || lastGlyph.fixedWidth) ? lastGlyph.xadvance : ((lastGlyph.xoffset + lastGlyph.width) - this.padRight);
/*     */         
/* 811 */         xAdvances.add(lastGlyphWidth * scaleX);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getWrapIndex(Array<BitmapFont.Glyph> glyphs, int start) {
/* 818 */       int i = start - 1;
/* 819 */       for (; i >= 1 && 
/* 820 */         isWhitespace((char)((BitmapFont.Glyph)glyphs.get(i)).id); i--);
/* 821 */       for (; i >= 1; i--) {
/* 822 */         char ch = (char)((BitmapFont.Glyph)glyphs.get(i)).id;
/* 823 */         if (isWhitespace(ch) || isBreakChar(ch)) return i + 1; 
/*     */       } 
/* 825 */       return 0;
/*     */     }
/*     */     
/*     */     public boolean isBreakChar(char c) {
/* 829 */       if (this.breakChars == null) return false; 
/* 830 */       for (char br : this.breakChars) {
/* 831 */         if (c == br) return true; 
/* 832 */       }  return false;
/*     */     }
/*     */     
/*     */     public boolean isWhitespace(char c) {
/* 836 */       switch (c) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\r':
/*     */         case ' ':
/* 841 */           return true;
/*     */       } 
/* 843 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getImagePath(int index) {
/* 849 */       return this.imagePaths[index];
/*     */     }
/*     */     
/*     */     public String[] getImagePaths() {
/* 853 */       return this.imagePaths;
/*     */     }
/*     */     
/*     */     public FileHandle getFontFile() {
/* 857 */       return this.fontFile;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setScale(float scaleX, float scaleY) {
/* 866 */       if (scaleX == 0.0F) throw new IllegalArgumentException("scaleX cannot be 0."); 
/* 867 */       if (scaleY == 0.0F) throw new IllegalArgumentException("scaleY cannot be 0."); 
/* 868 */       float x = scaleX / this.scaleX;
/* 869 */       float y = scaleY / this.scaleY;
/* 870 */       this.lineHeight *= y;
/* 871 */       this.spaceWidth *= x;
/* 872 */       this.xHeight *= y;
/* 873 */       this.capHeight *= y;
/* 874 */       this.ascent *= y;
/* 875 */       this.descent *= y;
/* 876 */       this.down *= y;
/* 877 */       this.padTop *= y;
/* 878 */       this.padLeft *= y;
/* 879 */       this.padBottom *= y;
/* 880 */       this.padRight *= y;
/* 881 */       this.scaleX = scaleX;
/* 882 */       this.scaleY = scaleY;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setScale(float scaleXY) {
/* 889 */       setScale(scaleXY, scaleXY);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void scale(float amount) {
/* 896 */       setScale(this.scaleX + amount, this.scaleY + amount);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g2d\BitmapFont.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */