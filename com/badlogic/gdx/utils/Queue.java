/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class Queue<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   protected T[] values;
/*  33 */   protected int head = 0;
/*     */ 
/*     */ 
/*     */   
/*  37 */   protected int tail = 0;
/*     */ 
/*     */   
/*  40 */   public int size = 0;
/*     */   
/*     */   private QueueIterable iterable;
/*     */ 
/*     */   
/*     */   public Queue() {
/*  46 */     this(16);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue(int initialSize) {
/*  52 */     this.values = (T[])new Object[initialSize];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue(int initialSize, Class<T> type) {
/*  59 */     this.values = (T[])ArrayReflection.newInstance(type, initialSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLast(T object) {
/*  65 */     T[] values = this.values;
/*     */     
/*  67 */     if (this.size == values.length) {
/*  68 */       resize(values.length << 1);
/*  69 */       values = this.values;
/*     */     } 
/*     */     
/*  72 */     values[this.tail++] = object;
/*  73 */     if (this.tail == values.length) {
/*  74 */       this.tail = 0;
/*     */     }
/*  76 */     this.size++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(T object) {
/*  83 */     T[] values = this.values;
/*     */     
/*  85 */     if (this.size == values.length) {
/*  86 */       resize(values.length << 1);
/*  87 */       values = this.values;
/*     */     } 
/*     */     
/*  90 */     int head = this.head;
/*  91 */     head--;
/*  92 */     if (head == -1) {
/*  93 */       head = values.length - 1;
/*     */     }
/*  95 */     values[head] = object;
/*     */     
/*  97 */     this.head = head;
/*  98 */     this.size++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additional) {
/* 104 */     int needed = this.size + additional;
/* 105 */     if (this.values.length < needed) {
/* 106 */       resize(needed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resize(int newSize) {
/* 112 */     T[] values = this.values;
/* 113 */     int head = this.head;
/* 114 */     int tail = this.tail;
/*     */ 
/*     */     
/* 117 */     T[] newArray = (T[])ArrayReflection.newInstance(values.getClass().getComponentType(), newSize);
/* 118 */     if (head < tail) {
/*     */       
/* 120 */       System.arraycopy(values, head, newArray, 0, tail - head);
/* 121 */     } else if (this.size > 0) {
/*     */       
/* 123 */       int rest = values.length - head;
/* 124 */       System.arraycopy(values, head, newArray, 0, rest);
/* 125 */       System.arraycopy(values, 0, newArray, rest, tail);
/*     */     } 
/* 127 */     this.values = newArray;
/* 128 */     this.head = 0;
/* 129 */     this.tail = this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T removeFirst() {
/* 136 */     if (this.size == 0)
/*     */     {
/* 138 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/*     */     
/* 141 */     T[] values = this.values;
/*     */     
/* 143 */     T result = values[this.head];
/* 144 */     values[this.head] = null;
/* 145 */     this.head++;
/* 146 */     if (this.head == values.length) {
/* 147 */       this.head = 0;
/*     */     }
/* 149 */     this.size--;
/*     */     
/* 151 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T removeLast() {
/* 159 */     if (this.size == 0) {
/* 160 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/*     */     
/* 163 */     T[] values = this.values;
/* 164 */     int tail = this.tail;
/* 165 */     tail--;
/* 166 */     if (tail == -1) {
/* 167 */       tail = values.length - 1;
/*     */     }
/* 169 */     T result = values[tail];
/* 170 */     values[tail] = null;
/* 171 */     this.tail = tail;
/* 172 */     this.size--;
/*     */     
/* 174 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(T value, boolean identity) {
/* 181 */     if (this.size == 0) return -1; 
/* 182 */     T[] values = this.values;
/* 183 */     int head = this.head, tail = this.tail;
/* 184 */     if (identity || value == null)
/* 185 */     { if (head < tail)
/* 186 */       { for (int i = head, n = tail; i < n; i++) {
/* 187 */           if (values[i] == value) return i; 
/*     */         }  }
/* 189 */       else { int i; int n; for (i = head, n = values.length; i < n; i++) {
/* 190 */           if (values[i] == value) return i - head; 
/* 191 */         }  for (i = 0, n = tail; i < n; i++) {
/* 192 */           if (values[i] == value) return i + values.length - head; 
/*     */         }  }
/*     */        }
/* 195 */     else if (head < tail)
/* 196 */     { for (int i = head, n = tail; i < n; i++) {
/* 197 */         if (value.equals(values[i])) return i; 
/*     */       }  }
/* 199 */     else { int i; int n; for (i = head, n = values.length; i < n; i++) {
/* 200 */         if (value.equals(values[i])) return i - head; 
/* 201 */       }  for (i = 0, n = tail; i < n; i++) {
/* 202 */         if (value.equals(values[i])) return i + values.length - head; 
/*     */       }  }
/*     */     
/* 205 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeValue(T value, boolean identity) {
/* 212 */     int index = indexOf(value, identity);
/* 213 */     if (index == -1) return false; 
/* 214 */     removeIndex(index);
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   public T removeIndex(int index) {
/*     */     T value;
/* 220 */     if (index < 0) throw new IndexOutOfBoundsException("index can't be < 0: " + index); 
/* 221 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
/*     */     
/* 223 */     T[] values = this.values;
/* 224 */     int head = this.head, tail = this.tail;
/* 225 */     index += head;
/*     */     
/* 227 */     if (head < tail) {
/* 228 */       value = values[index];
/* 229 */       System.arraycopy(values, index + 1, values, index, tail - index);
/* 230 */       values[tail] = null;
/* 231 */       this.tail--;
/* 232 */     } else if (index >= values.length) {
/* 233 */       index -= values.length;
/* 234 */       value = values[index];
/* 235 */       System.arraycopy(values, index + 1, values, index, tail - index);
/* 236 */       this.tail--;
/*     */     } else {
/* 238 */       value = values[index];
/* 239 */       System.arraycopy(values, head, values, head + 1, index - head);
/* 240 */       values[head] = null;
/* 241 */       this.head++;
/*     */     } 
/* 243 */     this.size--;
/* 244 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T first() {
/* 252 */     if (this.size == 0)
/*     */     {
/* 254 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/* 256 */     return this.values[this.head];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T last() {
/* 264 */     if (this.size == 0)
/*     */     {
/* 266 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/* 268 */     T[] values = this.values;
/* 269 */     int tail = this.tail;
/* 270 */     tail--;
/* 271 */     if (tail == -1) {
/* 272 */       tail = values.length - 1;
/*     */     }
/* 274 */     return values[tail];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(int index) {
/* 281 */     if (index < 0) throw new IndexOutOfBoundsException("index can't be < 0: " + index); 
/* 282 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 283 */     T[] values = this.values;
/*     */     
/* 285 */     int i = this.head + index;
/* 286 */     if (i >= values.length) {
/* 287 */       i -= values.length;
/*     */     }
/* 289 */     return values[i];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 295 */     if (this.size == 0)
/* 296 */       return;  T[] values = this.values;
/* 297 */     int head = this.head;
/* 298 */     int tail = this.tail;
/*     */     
/* 300 */     if (head < tail) {
/*     */       
/* 302 */       for (int i = head; i < tail; i++) {
/* 303 */         values[i] = null;
/*     */       }
/*     */     } else {
/*     */       int i;
/* 307 */       for (i = head; i < values.length; i++) {
/* 308 */         values[i] = null;
/*     */       }
/* 310 */       for (i = 0; i < tail; i++) {
/* 311 */         values[i] = null;
/*     */       }
/*     */     } 
/* 314 */     this.head = 0;
/* 315 */     this.tail = 0;
/* 316 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 322 */     if (this.iterable == null) this.iterable = new QueueIterable<T>(this); 
/* 323 */     return this.iterable.iterator();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 327 */     if (this.size == 0) {
/* 328 */       return "[]";
/*     */     }
/* 330 */     T[] values = this.values;
/* 331 */     int head = this.head;
/* 332 */     int tail = this.tail;
/*     */     
/* 334 */     StringBuilder sb = new StringBuilder(64);
/* 335 */     sb.append('[');
/* 336 */     sb.append(values[head]); int i;
/* 337 */     for (i = (head + 1) % values.length; i != tail; i = (i + 1) % values.length) {
/* 338 */       sb.append(", ").append(values[i]);
/*     */     }
/* 340 */     sb.append(']');
/* 341 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 345 */     int size = this.size;
/* 346 */     T[] values = this.values;
/* 347 */     int backingLength = values.length;
/* 348 */     int index = this.head;
/*     */     
/* 350 */     int hash = size + 1;
/* 351 */     for (int s = 0; s < size; s++) {
/* 352 */       T value = values[index];
/*     */       
/* 354 */       hash *= 31;
/* 355 */       if (value != null) hash += value.hashCode();
/*     */       
/* 357 */       index++;
/* 358 */       if (index == backingLength) index = 0;
/*     */     
/*     */     } 
/* 361 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 365 */     if (this == o) return true; 
/* 366 */     if (o == null || !(o instanceof Queue)) return false;
/*     */     
/* 368 */     Queue<?> q = (Queue)o;
/* 369 */     int size = this.size;
/*     */     
/* 371 */     if (q.size != size) return false;
/*     */     
/* 373 */     T[] myValues = this.values;
/* 374 */     int myBackingLength = myValues.length;
/* 375 */     T[] arrayOfT1 = q.values;
/* 376 */     int itsBackingLength = arrayOfT1.length;
/*     */     
/* 378 */     int myIndex = this.head;
/* 379 */     int itsIndex = q.head;
/* 380 */     for (int s = 0; s < size; ) {
/* 381 */       T myValue = myValues[myIndex];
/* 382 */       Object itsValue = arrayOfT1[itsIndex];
/*     */       
/* 384 */       if ((myValue == null) ? (itsValue == null) : myValue.equals(itsValue)) {
/* 385 */         myIndex++;
/* 386 */         itsIndex++;
/* 387 */         if (myIndex == myBackingLength) myIndex = 0; 
/* 388 */         if (itsIndex == itsBackingLength) itsIndex = 0;  s++;
/*     */       }  return false;
/* 390 */     }  return true;
/*     */   }
/*     */   
/*     */   public static class QueueIterator<T>
/*     */     implements Iterator<T>, Iterable<T>
/*     */   {
/*     */     private final Queue<T> queue;
/*     */     private final boolean allowRemove;
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public QueueIterator(Queue<T> queue) {
/* 402 */       this(queue, true);
/*     */     }
/*     */     
/*     */     public QueueIterator(Queue<T> queue, boolean allowRemove) {
/* 406 */       this.queue = queue;
/* 407 */       this.allowRemove = allowRemove;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 411 */       if (!this.valid)
/*     */       {
/* 413 */         throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       }
/* 415 */       return (this.index < this.queue.size);
/*     */     }
/*     */     
/*     */     public T next() {
/* 419 */       if (this.index >= this.queue.size) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 420 */       if (!this.valid)
/*     */       {
/* 422 */         throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       }
/* 424 */       return this.queue.get(this.index++);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 428 */       if (!this.allowRemove) throw new GdxRuntimeException("Remove not allowed."); 
/* 429 */       this.index--;
/* 430 */       this.queue.removeIndex(this.index);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 434 */       this.index = 0;
/*     */     }
/*     */     
/*     */     public Iterator<T> iterator() {
/* 438 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class QueueIterable<T>
/*     */     implements Iterable<T> {
/*     */     private final Queue<T> queue;
/*     */     private final boolean allowRemove;
/*     */     private Queue.QueueIterator iterator1;
/*     */     private Queue.QueueIterator iterator2;
/*     */     
/*     */     public QueueIterable(Queue<T> queue) {
/* 450 */       this(queue, true);
/*     */     }
/*     */     
/*     */     public QueueIterable(Queue<T> queue, boolean allowRemove) {
/* 454 */       this.queue = queue;
/* 455 */       this.allowRemove = allowRemove;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 461 */       if (this.iterator1 == null) {
/* 462 */         this.iterator1 = new Queue.QueueIterator<T>(this.queue, this.allowRemove);
/* 463 */         this.iterator2 = new Queue.QueueIterator<T>(this.queue, this.allowRemove);
/*     */       } 
/*     */ 
/*     */       
/* 467 */       if (!this.iterator1.valid) {
/* 468 */         this.iterator1.index = 0;
/* 469 */         this.iterator1.valid = true;
/* 470 */         this.iterator2.valid = false;
/* 471 */         return this.iterator1;
/*     */       } 
/* 473 */       this.iterator2.index = 0;
/* 474 */       this.iterator2.valid = true;
/* 475 */       this.iterator1.valid = false;
/* 476 */       return this.iterator2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gd\\utils\Queue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */