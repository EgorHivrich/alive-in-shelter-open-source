/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.audio.Music;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.openal.AL10;
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
/*     */ public abstract class OpenALMusic
/*     */   implements Music
/*     */ {
/*     */   private static final int bufferSize = 40960;
/*     */   private static final int bufferCount = 3;
/*     */   private static final int bytesPerSample = 2;
/*  37 */   private static final byte[] tempBytes = new byte[40960];
/*  38 */   private static final ByteBuffer tempBuffer = BufferUtils.createByteBuffer(40960);
/*     */   
/*     */   private final OpenALAudio audio;
/*     */   private IntBuffer buffers;
/*  42 */   private int sourceID = -1;
/*     */   private int format;
/*     */   private int sampleRate;
/*  45 */   private float volume = 1.0F; private boolean isLooping; private boolean isPlaying;
/*  46 */   private float pan = 0.0F;
/*     */   private float renderedSeconds;
/*     */   private float secondsPerBuffer;
/*     */   protected final FileHandle file;
/*  50 */   protected int bufferOverhead = 0;
/*     */   
/*     */   private Music.OnCompletionListener onCompletionListener;
/*     */   
/*     */   public OpenALMusic(OpenALAudio audio, FileHandle file) {
/*  55 */     this.audio = audio;
/*  56 */     this.file = file;
/*  57 */     this.onCompletionListener = null;
/*     */   }
/*     */   
/*     */   protected void setup(int channels, int sampleRate) {
/*  61 */     this.format = (channels > 1) ? 4355 : 4353;
/*  62 */     this.sampleRate = sampleRate;
/*  63 */     this.secondsPerBuffer = (40960 - this.bufferOverhead) / (2 * channels * sampleRate);
/*     */   }
/*     */   
/*     */   public void play() {
/*  67 */     if (this.audio.noDevice)
/*  68 */       return;  if (this.sourceID == -1) {
/*  69 */       this.sourceID = this.audio.obtainSource(true);
/*  70 */       if (this.sourceID == -1)
/*     */         return; 
/*  72 */       this.audio.music.add(this);
/*     */       
/*  74 */       if (this.buffers == null) {
/*  75 */         this.buffers = BufferUtils.createIntBuffer(3);
/*  76 */         AL10.alGenBuffers(this.buffers);
/*  77 */         if (AL10.alGetError() != 0) throw new GdxRuntimeException("Unable to allocate audio buffers."); 
/*     */       } 
/*  79 */       AL10.alSourcei(this.sourceID, 4103, 0);
/*  80 */       setPan(this.pan, this.volume);
/*     */       
/*  82 */       boolean filled = false;
/*  83 */       for (int i = 0; i < 3; i++) {
/*  84 */         int bufferID = this.buffers.get(i);
/*  85 */         if (!fill(bufferID))
/*  86 */           break;  filled = true;
/*  87 */         AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/*     */       } 
/*  89 */       if (!filled && this.onCompletionListener != null) this.onCompletionListener.onCompletion(this);
/*     */       
/*  91 */       if (AL10.alGetError() != 0) {
/*  92 */         stop();
/*     */         return;
/*     */       } 
/*     */     } 
/*  96 */     if (!this.isPlaying) {
/*  97 */       AL10.alSourcePlay(this.sourceID);
/*  98 */       this.isPlaying = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 103 */     if (this.audio.noDevice)
/* 104 */       return;  if (this.sourceID == -1)
/* 105 */       return;  this.audio.music.removeValue(this, true);
/* 106 */     reset();
/* 107 */     this.audio.freeSource(this.sourceID);
/* 108 */     this.sourceID = -1;
/* 109 */     this.renderedSeconds = 0.0F;
/* 110 */     this.isPlaying = false;
/*     */   }
/*     */   
/*     */   public void pause() {
/* 114 */     if (this.audio.noDevice)
/* 115 */       return;  if (this.sourceID != -1) AL10.alSourcePause(this.sourceID); 
/* 116 */     this.isPlaying = false;
/*     */   }
/*     */   
/*     */   public boolean isPlaying() {
/* 120 */     if (this.audio.noDevice) return false; 
/* 121 */     if (this.sourceID == -1) return false; 
/* 122 */     return this.isPlaying;
/*     */   }
/*     */   
/*     */   public void setLooping(boolean isLooping) {
/* 126 */     this.isLooping = isLooping;
/*     */   }
/*     */   
/*     */   public boolean isLooping() {
/* 130 */     return this.isLooping;
/*     */   }
/*     */   
/*     */   public void setVolume(float volume) {
/* 134 */     this.volume = volume;
/* 135 */     if (this.audio.noDevice)
/* 136 */       return;  if (this.sourceID != -1) AL10.alSourcef(this.sourceID, 4106, volume); 
/*     */   }
/*     */   
/*     */   public float getVolume() {
/* 140 */     return this.volume;
/*     */   }
/*     */   
/*     */   public void setPan(float pan, float volume) {
/* 144 */     this.volume = volume;
/* 145 */     this.pan = pan;
/* 146 */     if (this.audio.noDevice)
/* 147 */       return;  if (this.sourceID == -1)
/* 148 */       return;  AL10.alSource3f(this.sourceID, 4100, MathUtils.cos((pan - 1.0F) * 3.1415927F / 2.0F), 0.0F, 
/* 149 */         MathUtils.sin((pan + 1.0F) * 3.1415927F / 2.0F));
/* 150 */     AL10.alSourcef(this.sourceID, 4106, volume);
/*     */   }
/*     */   
/*     */   public void setPosition(float position) {
/* 154 */     if (this.audio.noDevice)
/* 155 */       return;  if (this.sourceID == -1)
/* 156 */       return;  boolean wasPlaying = this.isPlaying;
/* 157 */     this.isPlaying = false;
/* 158 */     AL10.alSourceStop(this.sourceID);
/* 159 */     AL10.alSourceUnqueueBuffers(this.sourceID, this.buffers);
/* 160 */     this.renderedSeconds += this.secondsPerBuffer * 3.0F;
/* 161 */     if (position <= this.renderedSeconds) {
/* 162 */       reset();
/* 163 */       this.renderedSeconds = 0.0F;
/*     */     } 
/* 165 */     while (this.renderedSeconds < position - this.secondsPerBuffer && 
/* 166 */       read(tempBytes) > 0) {
/* 167 */       this.renderedSeconds += this.secondsPerBuffer;
/*     */     }
/* 169 */     boolean filled = false;
/* 170 */     for (int i = 0; i < 3; i++) {
/* 171 */       int bufferID = this.buffers.get(i);
/* 172 */       if (!fill(bufferID))
/* 173 */         break;  filled = true;
/* 174 */       AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/*     */     } 
/* 176 */     if (!filled) {
/* 177 */       stop();
/* 178 */       if (this.onCompletionListener != null) this.onCompletionListener.onCompletion(this); 
/*     */     } 
/* 180 */     AL10.alSourcef(this.sourceID, 4132, position - this.renderedSeconds);
/* 181 */     if (wasPlaying) {
/* 182 */       AL10.alSourcePlay(this.sourceID);
/* 183 */       this.isPlaying = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPosition() {
/* 188 */     if (this.audio.noDevice) return 0.0F; 
/* 189 */     if (this.sourceID == -1) return 0.0F; 
/* 190 */     return this.renderedSeconds + AL10.alGetSourcef(this.sourceID, 4132);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int read(byte[] paramArrayOfbyte);
/*     */ 
/*     */   
/*     */   public abstract void reset();
/*     */ 
/*     */   
/*     */   protected void loop() {
/* 202 */     reset();
/*     */   }
/*     */   
/*     */   public int getChannels() {
/* 206 */     return (this.format == 4355) ? 2 : 1;
/*     */   }
/*     */   
/*     */   public int getRate() {
/* 210 */     return this.sampleRate;
/*     */   }
/*     */   
/*     */   public void update() {
/* 214 */     if (this.audio.noDevice)
/* 215 */       return;  if (this.sourceID == -1)
/*     */       return; 
/* 217 */     boolean end = false;
/* 218 */     int buffers = AL10.alGetSourcei(this.sourceID, 4118);
/* 219 */     while (buffers-- > 0) {
/* 220 */       int bufferID = AL10.alSourceUnqueueBuffers(this.sourceID);
/* 221 */       if (bufferID == 40963)
/* 222 */         break;  this.renderedSeconds += this.secondsPerBuffer;
/* 223 */       if (end)
/* 224 */         continue;  if (fill(bufferID)) {
/* 225 */         AL10.alSourceQueueBuffers(this.sourceID, bufferID); continue;
/*     */       } 
/* 227 */       end = true;
/*     */     } 
/* 229 */     if (end && AL10.alGetSourcei(this.sourceID, 4117) == 0) {
/* 230 */       stop();
/* 231 */       if (this.onCompletionListener != null) this.onCompletionListener.onCompletion(this);
/*     */     
/*     */     } 
/*     */     
/* 235 */     if (this.isPlaying && AL10.alGetSourcei(this.sourceID, 4112) != 4114) AL10.alSourcePlay(this.sourceID); 
/*     */   }
/*     */   
/*     */   private boolean fill(int bufferID) {
/* 239 */     tempBuffer.clear();
/* 240 */     int length = read(tempBytes);
/* 241 */     if (length <= 0)
/* 242 */       if (this.isLooping) {
/* 243 */         loop();
/* 244 */         this.renderedSeconds = 0.0F;
/* 245 */         length = read(tempBytes);
/* 246 */         if (length <= 0) return false; 
/*     */       } else {
/* 248 */         return false;
/*     */       }  
/* 250 */     tempBuffer.put(tempBytes, 0, length).flip();
/* 251 */     AL10.alBufferData(bufferID, this.format, tempBuffer, this.sampleRate);
/* 252 */     return true;
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 256 */     stop();
/* 257 */     if (this.audio.noDevice)
/* 258 */       return;  if (this.buffers == null)
/* 259 */       return;  AL10.alDeleteBuffers(this.buffers);
/* 260 */     this.buffers = null;
/* 261 */     this.onCompletionListener = null;
/*     */   }
/*     */   
/*     */   public void setOnCompletionListener(Music.OnCompletionListener listener) {
/* 265 */     this.onCompletionListener = listener;
/*     */   }
/*     */   
/*     */   public int getSourceId() {
/* 269 */     return this.sourceID;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\backends\lwjgl\audio\OpenALMusic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */