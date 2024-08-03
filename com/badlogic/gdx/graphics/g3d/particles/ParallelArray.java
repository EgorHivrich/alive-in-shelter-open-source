/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParallelArray
/*     */ {
/*     */   Array<Channel> arrays;
/*     */   public int capacity;
/*     */   public int size;
/*     */   
/*     */   public static class ChannelDescriptor
/*     */   {
/*     */     public int id;
/*     */     public Class<?> type;
/*     */     public int count;
/*     */     
/*     */     public ChannelDescriptor(int id, Class<?> type, int count) {
/*  23 */       this.id = id;
/*  24 */       this.type = type;
/*  25 */       this.count = count;
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class Channel {
/*     */     public int id;
/*     */     public Object data;
/*     */     public int strideSize;
/*     */     
/*     */     public Channel(int id, Object data, int strideSize) {
/*  35 */       this.id = id;
/*  36 */       this.strideSize = strideSize;
/*  37 */       this.data = data;
/*     */     }
/*     */     
/*     */     public abstract void add(int param1Int, Object... param1VarArgs);
/*     */     
/*     */     public abstract void swap(int param1Int1, int param1Int2);
/*     */     
/*     */     protected abstract void setCapacity(int param1Int); }
/*     */   
/*     */   public static interface ChannelInitializer<T extends Channel> {
/*     */     void init(T param1T);
/*     */   }
/*     */   
/*     */   public class FloatChannel extends Channel {
/*     */     public FloatChannel(int id, int strideSize, int size) {
/*  52 */       super(id, new float[size * strideSize], strideSize);
/*  53 */       this.data = (float[])super.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, Object... objects) {
/*  58 */       for (int i = this.strideSize * ParallelArray.this.size, c = i + this.strideSize, k = 0; i < c; i++, k++) {
/*  59 */         this.data[i] = ((Float)objects[k]).floatValue();
/*     */       }
/*     */     }
/*     */     
/*     */     public float[] data;
/*     */     
/*     */     public void swap(int i, int k) {
/*  66 */       i = this.strideSize * i;
/*  67 */       k = this.strideSize * k;
/*  68 */       for (int c = i + this.strideSize; i < c; i++, k++) {
/*  69 */         float t = this.data[i];
/*  70 */         this.data[i] = this.data[k];
/*  71 */         this.data[k] = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCapacity(int requiredCapacity) {
/*  77 */       float[] newData = new float[this.strideSize * requiredCapacity];
/*  78 */       System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newData.length));
/*  79 */       this.data = newData;
/*     */     }
/*     */   }
/*     */   
/*     */   public class IntChannel
/*     */     extends Channel {
/*     */     public IntChannel(int id, int strideSize, int size) {
/*  86 */       super(id, new int[size * strideSize], strideSize);
/*  87 */       this.data = (int[])super.data;
/*     */     }
/*     */     public int[] data;
/*     */     
/*     */     public void add(int index, Object... objects) {
/*  92 */       for (int i = this.strideSize * ParallelArray.this.size, c = i + this.strideSize, k = 0; i < c; i++, k++) {
/*  93 */         this.data[i] = ((Integer)objects[k]).intValue();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void swap(int i, int k) {
/* 100 */       i = this.strideSize * i;
/* 101 */       k = this.strideSize * k;
/* 102 */       for (int c = i + this.strideSize; i < c; i++, k++) {
/* 103 */         int t = this.data[i];
/* 104 */         this.data[i] = this.data[k];
/* 105 */         this.data[k] = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCapacity(int requiredCapacity) {
/* 111 */       int[] newData = new int[this.strideSize * requiredCapacity];
/* 112 */       System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newData.length));
/* 113 */       this.data = newData;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ObjectChannel<T> extends Channel {
/*     */     Class<T> componentType;
/*     */     public T[] data;
/*     */     
/*     */     public ObjectChannel(int id, int strideSize, int size, Class<T> type) {
/* 122 */       super(id, ArrayReflection.newInstance(type, size * strideSize), strideSize);
/* 123 */       this.componentType = type;
/* 124 */       this.data = (T[])super.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, Object... objects) {
/* 129 */       for (int i = this.strideSize * ParallelArray.this.size, c = i + this.strideSize, k = 0; i < c; i++, k++) {
/* 130 */         this.data[i] = (T)objects[k];
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void swap(int i, int k) {
/* 137 */       i = this.strideSize * i;
/* 138 */       k = this.strideSize * k;
/* 139 */       for (int c = i + this.strideSize; i < c; i++, k++) {
/* 140 */         T t = this.data[i];
/* 141 */         this.data[i] = this.data[k];
/* 142 */         this.data[k] = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCapacity(int requiredCapacity) {
/* 148 */       T[] newData = (T[])ArrayReflection.newInstance(this.componentType, this.strideSize * requiredCapacity);
/* 149 */       System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newData.length));
/* 150 */       this.data = newData;
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
/*     */   public ParallelArray(int capacity) {
/* 162 */     this.arrays = new Array(false, 2, Channel.class);
/* 163 */     this.capacity = capacity;
/* 164 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Channel> T addChannel(ChannelDescriptor channelDescriptor) {
/* 170 */     return addChannel(channelDescriptor, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Channel> T addChannel(ChannelDescriptor channelDescriptor, ChannelInitializer<T> initializer) {
/* 177 */     T channel = getChannel(channelDescriptor);
/* 178 */     if (channel == null) {
/* 179 */       channel = allocateChannel(channelDescriptor);
/* 180 */       if (initializer != null)
/* 181 */         initializer.init(channel); 
/* 182 */       this.arrays.add(channel);
/*     */     } 
/* 184 */     return channel;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends Channel> T allocateChannel(ChannelDescriptor channelDescriptor) {
/* 189 */     if (channelDescriptor.type == float.class) {
/* 190 */       return (T)new FloatChannel(channelDescriptor.id, channelDescriptor.count, this.capacity);
/*     */     }
/* 192 */     if (channelDescriptor.type == int.class) {
/* 193 */       return (T)new IntChannel(channelDescriptor.id, channelDescriptor.count, this.capacity);
/*     */     }
/*     */     
/* 196 */     return (T)new ObjectChannel(channelDescriptor.id, channelDescriptor.count, this.capacity, channelDescriptor.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void removeArray(int id) {
/* 202 */     this.arrays.removeIndex(findIndex(id));
/*     */   }
/*     */   
/*     */   private int findIndex(int id) {
/* 206 */     for (int i = 0; i < this.arrays.size; i++) {
/* 207 */       Channel array = ((Channel[])this.arrays.items)[i];
/* 208 */       if (array.id == id)
/* 209 */         return i; 
/*     */     } 
/* 211 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(Object... values) {
/* 218 */     if (this.size == this.capacity) {
/* 219 */       throw new GdxRuntimeException("Capacity reached, cannot add other elements");
/*     */     }
/* 221 */     int k = 0;
/* 222 */     for (Channel strideArray : this.arrays) {
/* 223 */       strideArray.add(k, values);
/* 224 */       k += strideArray.strideSize;
/*     */     } 
/* 226 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeElement(int index) {
/* 231 */     int last = this.size - 1;
/*     */     
/* 233 */     for (Channel strideArray : this.arrays) {
/* 234 */       strideArray.swap(index, last);
/*     */     }
/* 236 */     this.size = last;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Channel> T getChannel(ChannelDescriptor descriptor) {
/* 242 */     for (Channel array : this.arrays) {
/* 243 */       if (array.id == descriptor.id)
/* 244 */         return (T)array; 
/*     */     } 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 251 */     this.arrays.clear();
/* 252 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCapacity(int requiredCapacity) {
/* 258 */     if (this.capacity != requiredCapacity) {
/* 259 */       for (Channel channel : this.arrays) {
/* 260 */         channel.setCapacity(requiredCapacity);
/*     */       }
/* 262 */       this.capacity = requiredCapacity;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParallelArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */