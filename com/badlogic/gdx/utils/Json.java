/*      */ package com.badlogic.gdx.utils;
/*      */ 
/*      */ import com.badlogic.gdx.files.FileHandle;
/*      */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
/*      */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*      */ import com.badlogic.gdx.utils.reflect.Constructor;
/*      */ import com.badlogic.gdx.utils.reflect.Field;
/*      */ import com.badlogic.gdx.utils.reflect.ReflectionException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.security.AccessControlException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
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
/*      */ public class Json
/*      */ {
/*      */   private static final boolean debug = false;
/*      */   private JsonWriter writer;
/*   50 */   private String typeName = "class";
/*      */   private boolean usePrototypes = true;
/*      */   private JsonWriter.OutputType outputType;
/*      */   private boolean quoteLongValues;
/*      */   private boolean ignoreUnknownFields;
/*      */   private boolean enumNames = true;
/*      */   private Serializer defaultSerializer;
/*   57 */   private final ObjectMap<Class, OrderedMap<String, FieldMetadata>> typeToFields = new ObjectMap<Class<?>, OrderedMap<String, FieldMetadata>>();
/*   58 */   private final ObjectMap<String, Class> tagToClass = new ObjectMap<String, Class<?>>();
/*   59 */   private final ObjectMap<Class, String> classToTag = new ObjectMap<Class<?>, String>();
/*   60 */   private final ObjectMap<Class, Serializer> classToSerializer = new ObjectMap<Class<?>, Serializer>();
/*   61 */   private final ObjectMap<Class, Object[]> classToDefaultValues = (ObjectMap)new ObjectMap<Class<?>, Object>();
/*   62 */   private final Object[] equals1 = new Object[] { null }, equals2 = new Object[] { null };
/*      */   
/*      */   public Json() {
/*   65 */     this.outputType = JsonWriter.OutputType.minimal;
/*      */   }
/*      */   
/*      */   public Json(JsonWriter.OutputType outputType) {
/*   69 */     this.outputType = outputType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
/*   75 */     this.ignoreUnknownFields = ignoreUnknownFields;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOutputType(JsonWriter.OutputType outputType) {
/*   80 */     this.outputType = outputType;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setQuoteLongValues(boolean quoteLongValues) {
/*   85 */     this.quoteLongValues = quoteLongValues;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnumNames(boolean enumNames) {
/*   91 */     this.enumNames = enumNames;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addClassTag(String tag, Class type) {
/*   96 */     this.tagToClass.put(tag, type);
/*   97 */     this.classToTag.put(type, tag);
/*      */   }
/*      */ 
/*      */   
/*      */   public Class getClass(String tag) {
/*  102 */     return this.tagToClass.get(tag);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTag(Class type) {
/*  107 */     return this.classToTag.get(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTypeName(String typeName) {
/*  114 */     this.typeName = typeName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultSerializer(Serializer defaultSerializer) {
/*  120 */     this.defaultSerializer = defaultSerializer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> void setSerializer(Class<T> type, Serializer<T> serializer) {
/*  126 */     this.classToSerializer.put(type, serializer);
/*      */   }
/*      */   
/*      */   public <T> Serializer<T> getSerializer(Class<T> type) {
/*  130 */     return this.classToSerializer.get(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setUsePrototypes(boolean usePrototypes) {
/*  135 */     this.usePrototypes = usePrototypes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setElementType(Class type, String fieldName, Class elementType) {
/*  141 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  142 */     FieldMetadata metadata = fields.get(fieldName);
/*  143 */     if (metadata == null) throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")"); 
/*  144 */     metadata.elementType = elementType;
/*      */   }
/*      */   
/*      */   private OrderedMap<String, FieldMetadata> getFields(Class<Object> type) {
/*  148 */     OrderedMap<String, FieldMetadata> fields = this.typeToFields.get(type);
/*  149 */     if (fields != null) return fields;
/*      */     
/*  151 */     Array<Class<?>> classHierarchy = new Array<Class<?>>();
/*  152 */     Class<Object> nextClass = type;
/*  153 */     while (nextClass != Object.class) {
/*  154 */       classHierarchy.add(nextClass);
/*  155 */       nextClass = (Class)nextClass.getSuperclass();
/*      */     } 
/*  157 */     ArrayList<Field> allFields = new ArrayList<Field>();
/*  158 */     for (int i = classHierarchy.size - 1; i >= 0; i--) {
/*  159 */       Collections.addAll(allFields, ClassReflection.getDeclaredFields(classHierarchy.get(i)));
/*      */     }
/*  161 */     OrderedMap<String, FieldMetadata> nameToField = new OrderedMap<String, FieldMetadata>(allFields.size());
/*  162 */     for (int j = 0, n = allFields.size(); j < n; j++) {
/*  163 */       Field field = allFields.get(j);
/*      */       
/*  165 */       if (!field.isTransient() && 
/*  166 */         !field.isStatic() && 
/*  167 */         !field.isSynthetic()) {
/*      */         
/*  169 */         if (!field.isAccessible()) {
/*      */           try {
/*  171 */             field.setAccessible(true);
/*  172 */           } catch (AccessControlException ex) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  177 */         nameToField.put(field.getName(), new FieldMetadata(field));
/*      */       } 
/*  179 */     }  this.typeToFields.put(type, nameToField);
/*  180 */     return nameToField;
/*      */   }
/*      */   
/*      */   public String toJson(Object object) {
/*  184 */     return toJson(object, (object == null) ? null : object.getClass(), (Class)null);
/*      */   }
/*      */   
/*      */   public String toJson(Object object, Class knownType) {
/*  188 */     return toJson(object, knownType, (Class)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toJson(Object object, Class knownType, Class elementType) {
/*  194 */     StringWriter buffer = new StringWriter();
/*  195 */     toJson(object, knownType, elementType, buffer);
/*  196 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   public void toJson(Object object, FileHandle file) {
/*  200 */     toJson(object, (object == null) ? null : object.getClass(), (Class)null, file);
/*      */   }
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, FileHandle file) {
/*  205 */     toJson(object, knownType, (Class)null, file);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, Class elementType, FileHandle file) {
/*  211 */     Writer writer = null;
/*      */     try {
/*  213 */       writer = file.writer(false, "UTF-8");
/*  214 */       toJson(object, knownType, elementType, writer);
/*  215 */     } catch (Exception ex) {
/*  216 */       throw new SerializationException("Error writing file: " + file, ex);
/*      */     } finally {
/*  218 */       StreamUtils.closeQuietly(writer);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void toJson(Object object, Writer writer) {
/*  223 */     toJson(object, (object == null) ? null : object.getClass(), (Class)null, writer);
/*      */   }
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, Writer writer) {
/*  228 */     toJson(object, knownType, (Class)null, writer);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, Class elementType, Writer writer) {
/*  234 */     setWriter(writer);
/*      */     try {
/*  236 */       writeValue(object, knownType, elementType);
/*      */     } finally {
/*  238 */       StreamUtils.closeQuietly(this.writer);
/*  239 */       this.writer = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWriter(Writer writer) {
/*  245 */     if (!(writer instanceof JsonWriter)) writer = new JsonWriter(writer); 
/*  246 */     this.writer = (JsonWriter)writer;
/*  247 */     this.writer.setOutputType(this.outputType);
/*  248 */     this.writer.setQuoteLongValues(this.quoteLongValues);
/*      */   }
/*      */   
/*      */   public JsonWriter getWriter() {
/*  252 */     return this.writer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeFields(Object object) {
/*  257 */     Class<?> type = object.getClass();
/*      */     
/*  259 */     Object[] defaultValues = getDefaultValues(type);
/*      */     
/*  261 */     OrderedMap<String, FieldMetadata> fields = getFields(type);
/*  262 */     int i = 0;
/*  263 */     for (ObjectMap.Values<?> values = (new OrderedMap.OrderedMapValues(fields)).iterator(); values.hasNext(); ) { FieldMetadata metadata = (FieldMetadata)values.next();
/*  264 */       Field field = metadata.field;
/*      */       try {
/*  266 */         Object value = field.get(object);
/*  267 */         if (defaultValues != null) {
/*  268 */           Object defaultValue = defaultValues[i++];
/*  269 */           if (value == null && defaultValue == null)
/*  270 */             continue;  if (value != null && defaultValue != null) {
/*  271 */             if (value.equals(defaultValue))
/*  272 */               continue;  if (value.getClass().isArray() && defaultValue.getClass().isArray()) {
/*  273 */               this.equals1[0] = value;
/*  274 */               this.equals2[0] = defaultValue;
/*  275 */               if (Arrays.deepEquals(this.equals1, this.equals2)) {
/*      */                 continue;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*  281 */         this.writer.name(field.getName());
/*  282 */         writeValue(value, field.getType(), metadata.elementType);
/*  283 */       } catch (ReflectionException ex) {
/*  284 */         throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  285 */       } catch (SerializationException ex) {
/*  286 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  287 */         throw ex;
/*  288 */       } catch (Exception runtimeEx) {
/*  289 */         SerializationException ex = new SerializationException(runtimeEx);
/*  290 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  291 */         throw ex;
/*      */       }  }
/*      */   
/*      */   }
/*      */   private Object[] getDefaultValues(Class type) {
/*      */     Object object;
/*  297 */     if (!this.usePrototypes) return null; 
/*  298 */     if (this.classToDefaultValues.containsKey(type)) return this.classToDefaultValues.get(type);
/*      */     
/*      */     try {
/*  301 */       object = newInstance(type);
/*  302 */     } catch (Exception ex) {
/*  303 */       this.classToDefaultValues.put(type, null);
/*  304 */       return null;
/*      */     } 
/*      */     
/*  307 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  308 */     Object[] values = new Object[fields.size];
/*  309 */     this.classToDefaultValues.put(type, values);
/*      */     
/*  311 */     int i = 0;
/*  312 */     for (ObjectMap.Values<FieldMetadata> values1 = fields.values().iterator(); values1.hasNext(); ) { FieldMetadata metadata = values1.next();
/*  313 */       Field field = metadata.field;
/*      */       try {
/*  315 */         values[i++] = field.get(object);
/*  316 */       } catch (ReflectionException ex) {
/*  317 */         throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  318 */       } catch (SerializationException ex) {
/*  319 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  320 */         throw ex;
/*  321 */       } catch (RuntimeException runtimeEx) {
/*  322 */         SerializationException ex = new SerializationException(runtimeEx);
/*  323 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  324 */         throw ex;
/*      */       }  }
/*      */     
/*  327 */     return values;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String name) {
/*  332 */     writeField(object, name, name, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String name, Class elementType) {
/*  338 */     writeField(object, name, name, elementType);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String fieldName, String jsonName) {
/*  343 */     writeField(object, fieldName, jsonName, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String fieldName, String jsonName, Class elementType) {
/*  349 */     Class<?> type = object.getClass();
/*  350 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  351 */     FieldMetadata metadata = fields.get(fieldName);
/*  352 */     if (metadata == null) throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")"); 
/*  353 */     Field field = metadata.field;
/*  354 */     if (elementType == null) elementType = metadata.elementType;
/*      */     
/*      */     try {
/*  357 */       this.writer.name(jsonName);
/*  358 */       writeValue(field.get(object), field.getType(), elementType);
/*  359 */     } catch (ReflectionException ex) {
/*  360 */       throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  361 */     } catch (SerializationException ex) {
/*  362 */       ex.addTrace(field + " (" + type.getName() + ")");
/*  363 */       throw ex;
/*  364 */     } catch (Exception runtimeEx) {
/*  365 */       SerializationException ex = new SerializationException(runtimeEx);
/*  366 */       ex.addTrace(field + " (" + type.getName() + ")");
/*  367 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(String name, Object value) {
/*      */     try {
/*  376 */       this.writer.name(name);
/*  377 */     } catch (IOException ex) {
/*  378 */       throw new SerializationException(ex);
/*      */     } 
/*  380 */     if (value == null) {
/*  381 */       writeValue(value, (Class)null, (Class)null);
/*      */     } else {
/*  383 */       writeValue(value, value.getClass(), (Class)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(String name, Object value, Class knownType) {
/*      */     try {
/*  393 */       this.writer.name(name);
/*  394 */     } catch (IOException ex) {
/*  395 */       throw new SerializationException(ex);
/*      */     } 
/*  397 */     writeValue(value, knownType, (Class)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(String name, Object value, Class knownType, Class elementType) {
/*      */     try {
/*  407 */       this.writer.name(name);
/*  408 */     } catch (IOException ex) {
/*  409 */       throw new SerializationException(ex);
/*      */     } 
/*  411 */     writeValue(value, knownType, elementType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(Object value) {
/*  417 */     if (value == null) {
/*  418 */       writeValue(value, (Class)null, (Class)null);
/*      */     } else {
/*  420 */       writeValue(value, value.getClass(), (Class)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(Object value, Class knownType) {
/*  427 */     writeValue(value, knownType, (Class)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(Object value, Class<String> knownType, Class<?> elementType) {
/*      */     try {
/*      */       Class<ObjectMap> clazz2;
/*      */       Class<ArrayMap> clazz1;
/*      */       Class<HashMap> clazz;
/*  437 */       if (value == null) {
/*  438 */         this.writer.value(null);
/*      */         
/*      */         return;
/*      */       } 
/*  442 */       if ((knownType != null && knownType.isPrimitive()) || knownType == String.class || knownType == Integer.class || knownType == Boolean.class || knownType == Float.class || knownType == Long.class || knownType == Double.class || knownType == Short.class || knownType == Byte.class || knownType == Character.class) {
/*      */ 
/*      */         
/*  445 */         this.writer.value(value);
/*      */         
/*      */         return;
/*      */       } 
/*  449 */       Class<?> actualType = value.getClass();
/*      */       
/*  451 */       if (actualType.isPrimitive() || actualType == String.class || actualType == Integer.class || actualType == Boolean.class || actualType == Float.class || actualType == Long.class || actualType == Double.class || actualType == Short.class || actualType == Byte.class || actualType == Character.class) {
/*      */ 
/*      */         
/*  454 */         writeObjectStart(actualType, null);
/*  455 */         writeValue("value", value);
/*  456 */         writeObjectEnd();
/*      */         
/*      */         return;
/*      */       } 
/*  460 */       if (value instanceof Serializable) {
/*  461 */         writeObjectStart(actualType, knownType);
/*  462 */         ((Serializable)value).write(this);
/*  463 */         writeObjectEnd();
/*      */         
/*      */         return;
/*      */       } 
/*  467 */       Serializer<Object> serializer = this.classToSerializer.get(actualType);
/*  468 */       if (serializer != null) {
/*  469 */         serializer.write(this, value, knownType);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  474 */       if (value instanceof Array) {
/*  475 */         if (knownType != null && actualType != knownType && actualType != Array.class) {
/*  476 */           throw new SerializationException("Serialization of an Array other than the known type is not supported.\nKnown type: " + knownType + "\nActual type: " + actualType);
/*      */         }
/*  478 */         writeArrayStart();
/*  479 */         Array array = (Array)value;
/*  480 */         for (int i = 0, n = array.size; i < n; i++)
/*  481 */           writeValue(array.get(i), elementType, (Class)null); 
/*  482 */         writeArrayEnd();
/*      */         return;
/*      */       } 
/*  485 */       if (value instanceof Queue) {
/*  486 */         if (knownType != null && actualType != knownType && actualType != Queue.class) {
/*  487 */           throw new SerializationException("Serialization of a Queue other than the known type is not supported.\nKnown type: " + knownType + "\nActual type: " + actualType);
/*      */         }
/*  489 */         writeArrayStart();
/*  490 */         Queue queue = (Queue)value;
/*  491 */         for (int i = 0, n = queue.size; i < n; i++)
/*  492 */           writeValue(queue.get(i), elementType, (Class)null); 
/*  493 */         writeArrayEnd();
/*      */         return;
/*      */       } 
/*  496 */       if (value instanceof Collection) {
/*  497 */         if (this.typeName != null && actualType != ArrayList.class && (knownType == null || knownType != actualType)) {
/*  498 */           writeObjectStart(actualType, knownType);
/*  499 */           writeArrayStart("items");
/*  500 */           for (Object item : value)
/*  501 */             writeValue(item, elementType, (Class)null); 
/*  502 */           writeArrayEnd();
/*  503 */           writeObjectEnd();
/*      */         } else {
/*  505 */           writeArrayStart();
/*  506 */           for (Object item : value)
/*  507 */             writeValue(item, elementType, (Class)null); 
/*  508 */           writeArrayEnd();
/*      */         } 
/*      */         return;
/*      */       } 
/*  512 */       if (actualType.isArray()) {
/*  513 */         if (elementType == null) elementType = actualType.getComponentType(); 
/*  514 */         int length = ArrayReflection.getLength(value);
/*  515 */         writeArrayStart();
/*  516 */         for (int i = 0; i < length; i++)
/*  517 */           writeValue(ArrayReflection.get(value, i), elementType, (Class)null); 
/*  518 */         writeArrayEnd();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  523 */       if (value instanceof ObjectMap) {
/*  524 */         if (knownType == null) clazz2 = ObjectMap.class; 
/*  525 */         writeObjectStart(actualType, clazz2);
/*  526 */         for (ObjectMap.Entries<ObjectMap.Entry> entries = ((ObjectMap)value).entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry entry = entries.next();
/*  527 */           this.writer.name(convertToString(entry.key));
/*  528 */           writeValue(entry.value, elementType, (Class)null); }
/*      */         
/*  530 */         writeObjectEnd();
/*      */         return;
/*      */       } 
/*  533 */       if (value instanceof ArrayMap) {
/*  534 */         if (clazz2 == null) clazz1 = ArrayMap.class; 
/*  535 */         writeObjectStart(actualType, clazz1);
/*  536 */         ArrayMap map = (ArrayMap)value;
/*  537 */         for (int i = 0, n = map.size; i < n; i++) {
/*  538 */           this.writer.name(convertToString(map.keys[i]));
/*  539 */           writeValue(map.values[i], elementType, (Class)null);
/*      */         } 
/*  541 */         writeObjectEnd();
/*      */         return;
/*      */       } 
/*  544 */       if (value instanceof Map) {
/*  545 */         if (clazz1 == null) clazz = HashMap.class; 
/*  546 */         writeObjectStart(actualType, clazz);
/*  547 */         for (Map.Entry entry : ((Map)value).entrySet()) {
/*  548 */           this.writer.name(convertToString(entry.getKey()));
/*  549 */           writeValue(entry.getValue(), elementType, (Class)null);
/*      */         } 
/*  551 */         writeObjectEnd();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  556 */       if (ClassReflection.isAssignableFrom(Enum.class, actualType)) {
/*  557 */         if (this.typeName != null && (clazz == null || clazz != actualType)) {
/*      */           
/*  559 */           if (actualType.getEnumConstants() == null) actualType = actualType.getSuperclass();
/*      */           
/*  561 */           writeObjectStart(actualType, null);
/*  562 */           this.writer.name("value");
/*  563 */           this.writer.value(convertToString((Enum)value));
/*  564 */           writeObjectEnd();
/*      */         } else {
/*  566 */           this.writer.value(convertToString((Enum)value));
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*  571 */       writeObjectStart(actualType, clazz);
/*  572 */       writeFields(value);
/*  573 */       writeObjectEnd();
/*  574 */     } catch (IOException ex) {
/*  575 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeObjectStart(String name) {
/*      */     try {
/*  581 */       this.writer.name(name);
/*  582 */     } catch (IOException ex) {
/*  583 */       throw new SerializationException(ex);
/*      */     } 
/*  585 */     writeObjectStart();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeObjectStart(String name, Class actualType, Class knownType) {
/*      */     try {
/*  591 */       this.writer.name(name);
/*  592 */     } catch (IOException ex) {
/*  593 */       throw new SerializationException(ex);
/*      */     } 
/*  595 */     writeObjectStart(actualType, knownType);
/*      */   }
/*      */   
/*      */   public void writeObjectStart() {
/*      */     try {
/*  600 */       this.writer.object();
/*  601 */     } catch (IOException ex) {
/*  602 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeObjectStart(Class actualType, Class knownType) {
/*      */     try {
/*  610 */       this.writer.object();
/*  611 */     } catch (IOException ex) {
/*  612 */       throw new SerializationException(ex);
/*      */     } 
/*  614 */     if (knownType == null || knownType != actualType) writeType(actualType); 
/*      */   }
/*      */   
/*      */   public void writeObjectEnd() {
/*      */     try {
/*  619 */       this.writer.pop();
/*  620 */     } catch (IOException ex) {
/*  621 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeArrayStart(String name) {
/*      */     try {
/*  627 */       this.writer.name(name);
/*  628 */       this.writer.array();
/*  629 */     } catch (IOException ex) {
/*  630 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeArrayStart() {
/*      */     try {
/*  636 */       this.writer.array();
/*  637 */     } catch (IOException ex) {
/*  638 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeArrayEnd() {
/*      */     try {
/*  644 */       this.writer.pop();
/*  645 */     } catch (IOException ex) {
/*  646 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeType(Class type) {
/*  651 */     if (this.typeName == null)
/*  652 */       return;  String className = getTag(type);
/*  653 */     if (className == null) className = type.getName(); 
/*      */     try {
/*  655 */       this.writer.set(this.typeName, className);
/*  656 */     } catch (IOException ex) {
/*  657 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Reader reader) {
/*  665 */     return readValue(type, (Class)null, (new JsonReader()).parse(reader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, Reader reader) {
/*  672 */     return readValue(type, elementType, (new JsonReader()).parse(reader));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, InputStream input) {
/*  678 */     return readValue(type, (Class)null, (new JsonReader()).parse(input));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, InputStream input) {
/*  685 */     return readValue(type, elementType, (new JsonReader()).parse(input));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, FileHandle file) {
/*      */     try {
/*  692 */       return readValue(type, (Class)null, (new JsonReader()).parse(file));
/*  693 */     } catch (Exception ex) {
/*  694 */       throw new SerializationException("Error reading file: " + file, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, FileHandle file) {
/*      */     try {
/*  703 */       return readValue(type, elementType, (new JsonReader()).parse(file));
/*  704 */     } catch (Exception ex) {
/*  705 */       throw new SerializationException("Error reading file: " + file, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, char[] data, int offset, int length) {
/*  712 */     return readValue(type, (Class)null, (new JsonReader()).parse(data, offset, length));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, char[] data, int offset, int length) {
/*  719 */     return readValue(type, elementType, (new JsonReader()).parse(data, offset, length));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, String json) {
/*  725 */     return readValue(type, (Class)null, (new JsonReader()).parse(json));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, String json) {
/*  731 */     return readValue(type, elementType, (new JsonReader()).parse(json));
/*      */   }
/*      */   
/*      */   public void readField(Object object, String name, JsonValue jsonData) {
/*  735 */     readField(object, name, name, (Class)null, jsonData);
/*      */   }
/*      */   
/*      */   public void readField(Object object, String name, Class elementType, JsonValue jsonData) {
/*  739 */     readField(object, name, name, elementType, jsonData);
/*      */   }
/*      */   
/*      */   public void readField(Object object, String fieldName, String jsonName, JsonValue jsonData) {
/*  743 */     readField(object, fieldName, jsonName, (Class)null, jsonData);
/*      */   }
/*      */ 
/*      */   
/*      */   public void readField(Object object, String fieldName, String jsonName, Class elementType, JsonValue jsonMap) {
/*  748 */     Class<?> type = object.getClass();
/*  749 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  750 */     FieldMetadata metadata = fields.get(fieldName);
/*  751 */     if (metadata == null) throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")"); 
/*  752 */     Field field = metadata.field;
/*  753 */     if (elementType == null) elementType = metadata.elementType; 
/*  754 */     readField(object, field, jsonName, elementType, jsonMap);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void readField(Object object, Field field, String jsonName, Class elementType, JsonValue jsonMap) {
/*  760 */     JsonValue jsonValue = jsonMap.get(jsonName);
/*  761 */     if (jsonValue == null)
/*      */       return;  try {
/*  763 */       field.set(object, readValue(field.getType(), elementType, jsonValue));
/*  764 */     } catch (ReflectionException ex) {
/*  765 */       throw new SerializationException("Error accessing field: " + field
/*  766 */           .getName() + " (" + field.getDeclaringClass().getName() + ")", ex);
/*  767 */     } catch (SerializationException ex) {
/*  768 */       ex.addTrace(field.getName() + " (" + field.getDeclaringClass().getName() + ")");
/*  769 */       throw ex;
/*  770 */     } catch (RuntimeException runtimeEx) {
/*  771 */       SerializationException ex = new SerializationException(runtimeEx);
/*  772 */       ex.addTrace(jsonValue.trace());
/*  773 */       ex.addTrace(field.getName() + " (" + field.getDeclaringClass().getName() + ")");
/*  774 */       throw ex;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void readFields(Object object, JsonValue jsonMap) {
/*  779 */     Class<?> type = object.getClass();
/*  780 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  781 */     for (JsonValue child = jsonMap.child; child != null; child = child.next) {
/*  782 */       FieldMetadata metadata = fields.get(child.name);
/*  783 */       if (metadata == null) {
/*  784 */         if (!child.name.equals(this.typeName) && 
/*  785 */           !this.ignoreUnknownFields) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  790 */           SerializationException ex = new SerializationException("Field not found: " + child.name + " (" + type.getName() + ")");
/*  791 */           ex.addTrace(child.trace());
/*  792 */           throw ex;
/*      */         } 
/*      */       } else {
/*  795 */         Field field = metadata.field;
/*      */         try {
/*  797 */           field.set(object, readValue(field.getType(), metadata.elementType, child));
/*  798 */         } catch (ReflectionException ex) {
/*  799 */           throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  800 */         } catch (SerializationException ex) {
/*  801 */           ex.addTrace(field.getName() + " (" + type.getName() + ")");
/*  802 */           throw ex;
/*  803 */         } catch (RuntimeException runtimeEx) {
/*  804 */           SerializationException ex = new SerializationException(runtimeEx);
/*  805 */           ex.addTrace(child.trace());
/*  806 */           ex.addTrace(field.getName() + " (" + type.getName() + ")");
/*  807 */           throw ex;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, JsonValue jsonMap) {
/*  815 */     return readValue(type, (Class)null, jsonMap.get(name));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, T defaultValue, JsonValue jsonMap) {
/*  821 */     JsonValue jsonValue = jsonMap.get(name);
/*  822 */     if (jsonValue == null) return defaultValue; 
/*  823 */     return readValue(type, (Class)null, jsonValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, Class elementType, JsonValue jsonMap) {
/*  830 */     return readValue(type, elementType, jsonMap.get(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, Class elementType, T defaultValue, JsonValue jsonMap) {
/*  837 */     JsonValue jsonValue = jsonMap.get(name);
/*  838 */     return readValue(type, elementType, defaultValue, jsonValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Class<T> type, Class elementType, T defaultValue, JsonValue jsonData) {
/*  845 */     if (jsonData == null) return defaultValue; 
/*  846 */     return readValue(type, elementType, jsonData);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Class<T> type, JsonValue jsonData) {
/*  852 */     return readValue(type, (Class)null, jsonData);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Class<T> type, Class<?> elementType, JsonValue jsonData) {
/*      */     Class<Array> clazz;
/*  859 */     if (jsonData == null) return null;
/*      */     
/*  861 */     if (jsonData.isObject()) {
/*  862 */       String className = (this.typeName == null) ? null : jsonData.getString(this.typeName, null);
/*  863 */       if (className != null) {
/*  864 */         type = getClass(className);
/*  865 */         if (type == null) {
/*      */           try {
/*  867 */             type = ClassReflection.forName(className);
/*  868 */           } catch (ReflectionException ex) {
/*  869 */             throw new SerializationException(ex);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  874 */       if (type == null) {
/*  875 */         if (this.defaultSerializer != null) return this.defaultSerializer.read(this, jsonData, type); 
/*  876 */         return (T)jsonData;
/*      */       } 
/*      */       
/*  879 */       if (this.typeName != null && ClassReflection.isAssignableFrom(Collection.class, type)) {
/*      */         
/*  881 */         jsonData = jsonData.get("items");
/*      */       } else {
/*  883 */         Serializer<T> serializer = this.classToSerializer.get(type);
/*  884 */         if (serializer != null) return serializer.read(this, jsonData, type);
/*      */         
/*  886 */         if (type == String.class || type == Integer.class || type == Boolean.class || type == Float.class || type == Long.class || type == Double.class || type == Short.class || type == Byte.class || type == Character.class || 
/*      */           
/*  888 */           ClassReflection.isAssignableFrom(Enum.class, type)) {
/*  889 */           return readValue("value", type, jsonData);
/*      */         }
/*      */         
/*  892 */         Object object = newInstance(type);
/*      */         
/*  894 */         if (object instanceof Serializable) {
/*  895 */           ((Serializable)object).read(this, jsonData);
/*  896 */           return (T)object;
/*      */         } 
/*      */ 
/*      */         
/*  900 */         if (object instanceof ObjectMap) {
/*  901 */           ObjectMap result = (ObjectMap)object;
/*  902 */           for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  903 */             result.put(child.name, readValue(elementType, (Class)null, child)); 
/*  904 */           return (T)result;
/*      */         } 
/*  906 */         if (object instanceof ArrayMap) {
/*  907 */           ArrayMap result = (ArrayMap)object;
/*  908 */           for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  909 */             result.put(child.name, readValue(elementType, (Class)null, child)); 
/*  910 */           return (T)result;
/*      */         } 
/*  912 */         if (object instanceof Map) {
/*  913 */           Map result = (Map)object;
/*  914 */           for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  915 */             result.put(child.name, readValue(elementType, (Class)null, child)); 
/*  916 */           return (T)result;
/*      */         } 
/*      */         
/*  919 */         readFields(object, jsonData);
/*  920 */         return (T)object;
/*      */       } 
/*      */     } 
/*      */     
/*  924 */     if (type != null) {
/*  925 */       Serializer<T> serializer = this.classToSerializer.get(type);
/*  926 */       if (serializer != null) return serializer.read(this, jsonData, type);
/*      */     
/*      */     } 
/*  929 */     if (jsonData.isArray()) {
/*      */       
/*  931 */       if (type == null || type == Object.class) clazz = Array.class; 
/*  932 */       if (ClassReflection.isAssignableFrom(Array.class, clazz)) {
/*  933 */         Array result = (clazz == Array.class) ? new Array() : (Array)newInstance(clazz);
/*  934 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  935 */           result.add(readValue(elementType, (Class)null, child)); 
/*  936 */         return (T)result;
/*      */       } 
/*  938 */       if (ClassReflection.isAssignableFrom(Queue.class, clazz)) {
/*  939 */         Queue result = (clazz == Queue.class) ? new Queue() : (Queue)newInstance(clazz);
/*  940 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  941 */           result.addLast(readValue(elementType, (Class)null, child)); 
/*  942 */         return (T)result;
/*      */       } 
/*  944 */       if (ClassReflection.isAssignableFrom(Collection.class, clazz)) {
/*  945 */         Collection result = clazz.isInterface() ? new ArrayList() : (Collection)newInstance(clazz);
/*  946 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  947 */           result.add(readValue(elementType, (Class)null, child)); 
/*  948 */         return (T)result;
/*      */       } 
/*  950 */       if (clazz.isArray()) {
/*  951 */         Class<?> componentType = clazz.getComponentType();
/*  952 */         if (elementType == null) elementType = componentType; 
/*  953 */         Object result = ArrayReflection.newInstance(componentType, jsonData.size);
/*  954 */         int i = 0;
/*  955 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  956 */           ArrayReflection.set(result, i++, readValue(elementType, (Class)null, child)); 
/*  957 */         return (T)result;
/*      */       } 
/*  959 */       throw new SerializationException("Unable to convert value to required type: " + jsonData + " (" + clazz.getName() + ")");
/*      */     } 
/*      */     
/*  962 */     if (jsonData.isNumber()) {
/*      */       try {
/*  964 */         if (clazz == null || clazz == float.class || clazz == Float.class) return (T)Float.valueOf(jsonData.asFloat()); 
/*  965 */         if (clazz == int.class || clazz == Integer.class) return (T)Integer.valueOf(jsonData.asInt()); 
/*  966 */         if (clazz == long.class || clazz == Long.class) return (T)Long.valueOf(jsonData.asLong()); 
/*  967 */         if (clazz == double.class || clazz == Double.class) return (T)Double.valueOf(jsonData.asDouble()); 
/*  968 */         if (clazz == String.class) return (T)jsonData.asString(); 
/*  969 */         if (clazz == short.class || clazz == Short.class) return (T)Short.valueOf(jsonData.asShort()); 
/*  970 */         if (clazz == byte.class || clazz == Byte.class) return (T)Byte.valueOf(jsonData.asByte()); 
/*  971 */       } catch (NumberFormatException numberFormatException) {}
/*      */       
/*  973 */       jsonData = new JsonValue(jsonData.asString());
/*      */     } 
/*      */     
/*  976 */     if (jsonData.isBoolean()) {
/*      */       try {
/*  978 */         if (clazz == null || clazz == boolean.class || clazz == Boolean.class) return (T)Boolean.valueOf(jsonData.asBoolean()); 
/*  979 */       } catch (NumberFormatException numberFormatException) {}
/*      */       
/*  981 */       jsonData = new JsonValue(jsonData.asString());
/*      */     } 
/*      */     
/*  984 */     if (jsonData.isString()) {
/*  985 */       String string = jsonData.asString();
/*  986 */       if (clazz == null || clazz == String.class) return (T)string; 
/*      */       try {
/*  988 */         if (clazz == int.class || clazz == Integer.class) return (T)Integer.valueOf(string); 
/*  989 */         if (clazz == float.class || clazz == Float.class) return (T)Float.valueOf(string); 
/*  990 */         if (clazz == long.class || clazz == Long.class) return (T)Long.valueOf(string); 
/*  991 */         if (clazz == double.class || clazz == Double.class) return (T)Double.valueOf(string); 
/*  992 */         if (clazz == short.class || clazz == Short.class) return (T)Short.valueOf(string); 
/*  993 */         if (clazz == byte.class || clazz == Byte.class) return (T)Byte.valueOf(string); 
/*  994 */       } catch (NumberFormatException numberFormatException) {}
/*      */       
/*  996 */       if (clazz == boolean.class || clazz == Boolean.class) return (T)Boolean.valueOf(string); 
/*  997 */       if (clazz == char.class || clazz == Character.class) return (T)Character.valueOf(string.charAt(0)); 
/*  998 */       if (ClassReflection.isAssignableFrom(Enum.class, clazz)) {
/*  999 */         Enum[] constants = (Enum[])clazz.getEnumConstants();
/* 1000 */         for (int i = 0, n = constants.length; i < n; i++) {
/* 1001 */           Enum e = constants[i];
/* 1002 */           if (string.equals(convertToString(e))) return (T)e; 
/*      */         } 
/*      */       } 
/* 1005 */       if (clazz == CharSequence.class) return (T)string; 
/* 1006 */       throw new SerializationException("Unable to convert value to required type: " + jsonData + " (" + clazz.getName() + ")");
/*      */     } 
/*      */     
/* 1009 */     return null;
/*      */   }
/*      */   
/*      */   private String convertToString(Enum e) {
/* 1013 */     return this.enumNames ? e.name() : e.toString();
/*      */   }
/*      */   
/*      */   private String convertToString(Object object) {
/* 1017 */     if (object instanceof Enum) return convertToString((Enum)object); 
/* 1018 */     if (object instanceof Class) return ((Class)object).getName(); 
/* 1019 */     return String.valueOf(object);
/*      */   }
/*      */   
/*      */   protected Object newInstance(Class type) {
/*      */     try {
/* 1024 */       return ClassReflection.newInstance(type);
/* 1025 */     } catch (Exception ex) {
/*      */ 
/*      */       
/* 1028 */       try { Constructor constructor = ClassReflection.getDeclaredConstructor(type, new Class[0]);
/* 1029 */         constructor.setAccessible(true);
/* 1030 */         return constructor.newInstance(new Object[0]); }
/* 1031 */       catch (SecurityException securityException) {  }
/* 1032 */       catch (ReflectionException ignored)
/* 1033 */       { if (ClassReflection.isAssignableFrom(Enum.class, type)) {
/* 1034 */           if (type.getEnumConstants() == null) type = type.getSuperclass(); 
/* 1035 */           return type.getEnumConstants()[0];
/*      */         } 
/* 1037 */         if (type.isArray())
/* 1038 */           throw new SerializationException("Encountered JSON object when expected array of type: " + type.getName(), ex); 
/* 1039 */         if (ClassReflection.isMemberClass(type) && !ClassReflection.isStaticClass(type)) {
/* 1040 */           throw new SerializationException("Class cannot be created (non-static member class): " + type.getName(), ex);
/*      */         }
/* 1042 */         throw new SerializationException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex); }
/* 1043 */       catch (Exception privateConstructorException)
/* 1044 */       { ex = privateConstructorException; }
/*      */       
/* 1046 */       throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String prettyPrint(Object object) {
/* 1051 */     return prettyPrint(object, 0);
/*      */   }
/*      */   
/*      */   public String prettyPrint(String json) {
/* 1055 */     return prettyPrint(json, 0);
/*      */   }
/*      */   
/*      */   public String prettyPrint(Object object, int singleLineColumns) {
/* 1059 */     return prettyPrint(toJson(object), singleLineColumns);
/*      */   }
/*      */   
/*      */   public String prettyPrint(String json, int singleLineColumns) {
/* 1063 */     return (new JsonReader()).parse(json).prettyPrint(this.outputType, singleLineColumns);
/*      */   }
/*      */   
/*      */   public String prettyPrint(Object object, JsonValue.PrettyPrintSettings settings) {
/* 1067 */     return prettyPrint(toJson(object), settings);
/*      */   }
/*      */   
/*      */   public String prettyPrint(String json, JsonValue.PrettyPrintSettings settings) {
/* 1071 */     return (new JsonReader()).parse(json).prettyPrint(settings);
/*      */   }
/*      */   
/*      */   private static class FieldMetadata {
/*      */     Field field;
/*      */     Class elementType;
/*      */     
/*      */     public FieldMetadata(Field field) {
/* 1079 */       this.field = field;
/*      */       
/* 1081 */       int index = (ClassReflection.isAssignableFrom(ObjectMap.class, field.getType()) || ClassReflection.isAssignableFrom(Map.class, field.getType())) ? 1 : 0;
/* 1082 */       this.elementType = field.getElementType(index);
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface Serializer<T> {
/*      */     void write(Json param1Json, T param1T, Class param1Class);
/*      */     
/*      */     T read(Json param1Json, JsonValue param1JsonValue, Class param1Class);
/*      */   }
/*      */   
/*      */   public static abstract class ReadOnlySerializer<T> implements Serializer<T> {
/*      */     public void write(Json json, T object, Class knownType) {}
/*      */     
/*      */     public abstract T read(Json param1Json, JsonValue param1JsonValue, Class param1Class);
/*      */   }
/*      */   
/*      */   public static interface Serializable {
/*      */     void write(Json param1Json);
/*      */     
/*      */     void read(Json param1Json, JsonValue param1JsonValue);
/*      */   }
/*      */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gd\\utils\Json.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */