/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ShortBuffer;
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
/*     */ public class IndexBufferObjectSubData
/*     */   implements IndexData
/*     */ {
/*     */   final ShortBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   int bufferHandle;
/*     */   final boolean isDirect;
/*     */   boolean isDirty = true;
/*     */   boolean isBound = false;
/*     */   final int usage;
/*     */   
/*     */   public IndexBufferObjectSubData(boolean isStatic, int maxIndices) {
/*  61 */     this.byteBuffer = BufferUtils.newByteBuffer(maxIndices * 2);
/*  62 */     this.isDirect = true;
/*     */     
/*  64 */     this.usage = isStatic ? 35044 : 35048;
/*  65 */     this.buffer = this.byteBuffer.asShortBuffer();
/*  66 */     this.buffer.flip();
/*  67 */     this.byteBuffer.flip();
/*  68 */     this.bufferHandle = createBufferObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexBufferObjectSubData(int maxIndices) {
/*  75 */     this.byteBuffer = BufferUtils.newByteBuffer(maxIndices * 2);
/*  76 */     this.isDirect = true;
/*     */     
/*  78 */     this.usage = 35044;
/*  79 */     this.buffer = this.byteBuffer.asShortBuffer();
/*  80 */     this.buffer.flip();
/*  81 */     this.byteBuffer.flip();
/*  82 */     this.bufferHandle = createBufferObject();
/*     */   }
/*     */   
/*     */   private int createBufferObject() {
/*  86 */     int result = Gdx.gl20.glGenBuffer();
/*  87 */     Gdx.gl20.glBindBuffer(34963, result);
/*  88 */     Gdx.gl20.glBufferData(34963, this.byteBuffer.capacity(), null, this.usage);
/*  89 */     Gdx.gl20.glBindBuffer(34963, 0);
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumIndices() {
/*  95 */     return this.buffer.limit();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxIndices() {
/* 100 */     return this.buffer.capacity();
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
/*     */   public void setIndices(short[] indices, int offset, int count) {
/* 116 */     this.isDirty = true;
/* 117 */     this.buffer.clear();
/* 118 */     this.buffer.put(indices, offset, count);
/* 119 */     this.buffer.flip();
/* 120 */     this.byteBuffer.position(0);
/* 121 */     this.byteBuffer.limit(count << 1);
/*     */     
/* 123 */     if (this.isBound) {
/* 124 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 125 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setIndices(ShortBuffer indices) {
/* 130 */     int pos = indices.position();
/* 131 */     this.isDirty = true;
/* 132 */     this.buffer.clear();
/* 133 */     this.buffer.put(indices);
/* 134 */     this.buffer.flip();
/* 135 */     indices.position(pos);
/* 136 */     this.byteBuffer.position(0);
/* 137 */     this.byteBuffer.limit(this.buffer.limit() << 1);
/*     */     
/* 139 */     if (this.isBound) {
/* 140 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 141 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateIndices(int targetOffset, short[] indices, int offset, int count) {
/* 147 */     this.isDirty = true;
/* 148 */     int pos = this.byteBuffer.position();
/* 149 */     this.byteBuffer.position(targetOffset * 2);
/* 150 */     BufferUtils.copy(indices, offset, this.byteBuffer, count);
/* 151 */     this.byteBuffer.position(pos);
/* 152 */     this.buffer.position(0);
/*     */     
/* 154 */     if (this.isBound) {
/* 155 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 156 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBuffer getBuffer() {
/* 168 */     this.isDirty = true;
/* 169 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind() {
/* 174 */     if (this.bufferHandle == 0) throw new GdxRuntimeException("buuh");
/*     */     
/* 176 */     Gdx.gl20.glBindBuffer(34963, this.bufferHandle);
/* 177 */     if (this.isDirty) {
/* 178 */       this.byteBuffer.limit(this.buffer.limit() * 2);
/* 179 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 180 */       this.isDirty = false;
/*     */     } 
/* 182 */     this.isBound = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind() {
/* 187 */     Gdx.gl20.glBindBuffer(34963, 0);
/* 188 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 193 */     this.bufferHandle = createBufferObject();
/* 194 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 199 */     GL20 gl = Gdx.gl20;
/* 200 */     gl.glBindBuffer(34963, 0);
/* 201 */     gl.glDeleteBuffer(this.bufferHandle);
/* 202 */     this.bufferHandle = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\glutils\IndexBufferObjectSubData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */