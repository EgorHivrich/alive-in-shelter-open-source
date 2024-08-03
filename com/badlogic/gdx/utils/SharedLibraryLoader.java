/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.UUID;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
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
/*     */ public class SharedLibraryLoader
/*     */ {
/*  37 */   public static boolean isWindows = System.getProperty("os.name").contains("Windows");
/*  38 */   public static boolean isLinux = System.getProperty("os.name").contains("Linux");
/*  39 */   public static boolean isMac = System.getProperty("os.name").contains("Mac");
/*     */   public static boolean isIos = false;
/*     */   public static boolean isAndroid = false;
/*  42 */   public static boolean isARM = System.getProperty("os.arch").startsWith("arm");
/*  43 */   public static boolean is64Bit = (System.getProperty("os.arch").equals("amd64") || 
/*  44 */     System.getProperty("os.arch").equals("x86_64"));
/*     */ 
/*     */   
/*  47 */   public static String abi = (System.getProperty("sun.arch.abi") != null) ? System.getProperty("sun.arch.abi") : "";
/*     */   
/*     */   static {
/*  50 */     String vm = System.getProperty("java.runtime.name");
/*  51 */     if (vm != null && vm.contains("Android Runtime")) {
/*  52 */       isAndroid = true;
/*  53 */       isWindows = false;
/*  54 */       isLinux = false;
/*  55 */       isMac = false;
/*  56 */       is64Bit = false;
/*     */     } 
/*  58 */     if (!isAndroid && !isWindows && !isLinux && !isMac) {
/*  59 */       isIos = true;
/*  60 */       is64Bit = false;
/*     */     } 
/*     */   }
/*     */   
/*  64 */   private static final HashSet<String> loadedLibraries = new HashSet<String>();
/*     */ 
/*     */   
/*     */   private String nativesJar;
/*     */ 
/*     */   
/*     */   public SharedLibraryLoader() {}
/*     */ 
/*     */   
/*     */   public SharedLibraryLoader(String nativesJar) {
/*  74 */     this.nativesJar = nativesJar;
/*     */   }
/*     */ 
/*     */   
/*     */   public String crc(InputStream input) {
/*  79 */     if (input == null) throw new IllegalArgumentException("input cannot be null."); 
/*  80 */     CRC32 crc = new CRC32();
/*  81 */     byte[] buffer = new byte[4096];
/*     */     try {
/*     */       while (true) {
/*  84 */         int length = input.read(buffer);
/*  85 */         if (length == -1)
/*  86 */           break;  crc.update(buffer, 0, length);
/*     */       } 
/*  88 */     } catch (Exception ex) {
/*  89 */       StreamUtils.closeQuietly(input);
/*     */     } 
/*  91 */     return Long.toString(crc.getValue(), 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public String mapLibraryName(String libraryName) {
/*  96 */     if (isWindows) return libraryName + (is64Bit ? "64.dll" : ".dll"); 
/*  97 */     if (isLinux) return "lib" + libraryName + (isARM ? ("arm" + abi) : "") + (is64Bit ? "64.so" : ".so"); 
/*  98 */     if (isMac) return "lib" + libraryName + (is64Bit ? "64.dylib" : ".dylib"); 
/*  99 */     return libraryName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void load(String libraryName) {
/* 106 */     if (isIos)
/*     */       return; 
/* 108 */     libraryName = mapLibraryName(libraryName);
/* 109 */     if (loadedLibraries.contains(libraryName))
/*     */       return; 
/*     */     try {
/* 112 */       if (isAndroid)
/* 113 */       { System.loadLibrary(libraryName); }
/*     */       else
/* 115 */       { loadFile(libraryName); } 
/* 116 */     } catch (Throwable ex) {
/* 117 */       throw new GdxRuntimeException("Couldn't load shared library '" + libraryName + "' for target: " + 
/* 118 */           System.getProperty("os.name") + (is64Bit ? ", 64-bit" : ", 32-bit"), ex);
/*     */     } 
/* 120 */     loadedLibraries.add(libraryName);
/*     */   }
/*     */   
/*     */   private InputStream readFile(String path) {
/* 124 */     if (this.nativesJar == null) {
/* 125 */       InputStream input = SharedLibraryLoader.class.getResourceAsStream("/" + path);
/* 126 */       if (input == null) throw new GdxRuntimeException("Unable to read file for extraction: " + path); 
/* 127 */       return input;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 132 */       ZipFile file = new ZipFile(this.nativesJar);
/* 133 */       ZipEntry entry = file.getEntry(path);
/* 134 */       if (entry == null) throw new GdxRuntimeException("Couldn't find '" + path + "' in JAR: " + this.nativesJar); 
/* 135 */       return file.getInputStream(entry);
/* 136 */     } catch (IOException ex) {
/* 137 */       throw new GdxRuntimeException("Error reading '" + path + "' in JAR: " + this.nativesJar, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File extractFile(String sourcePath, String dirName) throws IOException {
/*     */     try {
/* 148 */       String sourceCrc = crc(readFile(sourcePath));
/* 149 */       if (dirName == null) dirName = sourceCrc;
/*     */       
/* 151 */       File extractedFile = getExtractedFile(dirName, (new File(sourcePath)).getName());
/* 152 */       if (extractedFile == null) {
/* 153 */         extractedFile = getExtractedFile(UUID.randomUUID().toString(), (new File(sourcePath)).getName());
/* 154 */         if (extractedFile == null) throw new GdxRuntimeException("Unable to find writable path to extract file. Is the user home directory writable?");
/*     */       
/*     */       } 
/* 157 */       return extractFile(sourcePath, sourceCrc, extractedFile);
/* 158 */     } catch (RuntimeException ex) {
/*     */       
/* 160 */       File file = new File(System.getProperty("java.library.path"), sourcePath);
/* 161 */       if (file.exists()) return file; 
/* 162 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void extractFileTo(String sourcePath, File dir) throws IOException {
/* 171 */     extractFile(sourcePath, crc(readFile(sourcePath)), new File(dir, (new File(sourcePath)).getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File getExtractedFile(String dirName, String fileName) {
/* 179 */     File idealFile = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + dirName, fileName);
/* 180 */     if (canWrite(idealFile)) return idealFile;
/*     */ 
/*     */     
/*     */     try {
/* 184 */       File file1 = File.createTempFile(dirName, null);
/* 185 */       if (file1.delete()) {
/* 186 */         file1 = new File(file1, fileName);
/* 187 */         if (canWrite(file1)) return file1; 
/*     */       } 
/* 189 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 193 */     File file = new File(System.getProperty("user.home") + "/.libgdx/" + dirName, fileName);
/* 194 */     if (canWrite(file)) return file;
/*     */ 
/*     */     
/* 197 */     file = new File(".temp/" + dirName, fileName);
/* 198 */     if (canWrite(file)) return file;
/*     */ 
/*     */     
/* 201 */     if (System.getenv("APP_SANDBOX_CONTAINER_ID") != null) return idealFile;
/*     */     
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canWrite(File file) {
/* 208 */     File testFile, parent = file.getParentFile();
/*     */     
/* 210 */     if (file.exists()) {
/* 211 */       if (!file.canWrite() || !canExecute(file)) return false;
/*     */       
/* 213 */       testFile = new File(parent, UUID.randomUUID().toString());
/*     */     } else {
/* 215 */       parent.mkdirs();
/* 216 */       if (!parent.isDirectory()) return false; 
/* 217 */       testFile = file;
/*     */     } 
/*     */     try {
/* 220 */       (new FileOutputStream(testFile)).close();
/* 221 */       if (!canExecute(testFile)) return false; 
/* 222 */       return true;
/* 223 */     } catch (Throwable ex) {
/* 224 */       return false;
/*     */     } finally {
/* 226 */       testFile.delete();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canExecute(File file) {
/*     */     try {
/* 232 */       Method canExecute = File.class.getMethod("canExecute", new Class[0]);
/* 233 */       if (((Boolean)canExecute.invoke(file, new Object[0])).booleanValue()) return true;
/*     */       
/* 235 */       Method setExecutable = File.class.getMethod("setExecutable", new Class[] { boolean.class, boolean.class });
/* 236 */       setExecutable.invoke(file, new Object[] { Boolean.valueOf(true), Boolean.valueOf(false) });
/*     */       
/* 238 */       return ((Boolean)canExecute.invoke(file, new Object[0])).booleanValue();
/* 239 */     } catch (Exception exception) {
/*     */       
/* 241 */       return false;
/*     */     } 
/*     */   }
/*     */   private File extractFile(String sourcePath, String sourceCrc, File extractedFile) throws IOException {
/* 245 */     String extractedCrc = null;
/* 246 */     if (extractedFile.exists()) {
/*     */       try {
/* 248 */         extractedCrc = crc(new FileInputStream(extractedFile));
/* 249 */       } catch (FileNotFoundException fileNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 254 */     if (extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
/*     */       try {
/* 256 */         InputStream input = readFile(sourcePath);
/* 257 */         extractedFile.getParentFile().mkdirs();
/* 258 */         FileOutputStream output = new FileOutputStream(extractedFile);
/* 259 */         byte[] buffer = new byte[4096];
/*     */         while (true) {
/* 261 */           int length = input.read(buffer);
/* 262 */           if (length == -1)
/* 263 */             break;  output.write(buffer, 0, length);
/*     */         } 
/* 265 */         input.close();
/* 266 */         output.close();
/* 267 */       } catch (IOException ex) {
/* 268 */         throw new GdxRuntimeException("Error extracting file: " + sourcePath + "\nTo: " + extractedFile.getAbsolutePath(), ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 273 */     return extractedFile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadFile(String sourcePath) {
/* 279 */     String sourceCrc = crc(readFile(sourcePath));
/*     */     
/* 281 */     String fileName = (new File(sourcePath)).getName();
/*     */ 
/*     */     
/* 284 */     File file = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + sourceCrc, fileName);
/*     */     
/* 286 */     Throwable ex = loadFile(sourcePath, sourceCrc, file);
/* 287 */     if (ex == null) {
/*     */       return;
/*     */     }
/*     */     
/* 291 */     try { file = File.createTempFile(sourceCrc, null);
/* 292 */       if (file.delete() && loadFile(sourcePath, sourceCrc, file) == null)
/* 293 */         return;  } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */     
/* 297 */     file = new File(System.getProperty("user.home") + "/.libgdx/" + sourceCrc, fileName);
/* 298 */     if (loadFile(sourcePath, sourceCrc, file) == null) {
/*     */       return;
/*     */     }
/* 301 */     file = new File(".temp/" + sourceCrc, fileName);
/* 302 */     if (loadFile(sourcePath, sourceCrc, file) == null) {
/*     */       return;
/*     */     }
/* 305 */     file = new File(System.getProperty("java.library.path"), sourcePath);
/* 306 */     if (file.exists()) {
/* 307 */       System.load(file.getAbsolutePath());
/*     */       
/*     */       return;
/*     */     } 
/* 311 */     throw new GdxRuntimeException(ex);
/*     */   }
/*     */ 
/*     */   
/*     */   private Throwable loadFile(String sourcePath, String sourceCrc, File extractedFile) {
/*     */     try {
/* 317 */       System.load(extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
/* 318 */       return null;
/* 319 */     } catch (Throwable ex) {
/* 320 */       return ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gd\\utils\SharedLibraryLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */