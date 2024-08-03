/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Attributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.Shader;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
/*     */ import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class ParticleShader
/*     */   extends BaseShader
/*     */ {
/*     */   public enum ParticleType
/*     */   {
/*  46 */     Billboard,
/*  47 */     Point;
/*     */   }
/*     */   
/*     */   public enum AlignMode {
/*  51 */     Screen, ViewPoint;
/*     */   }
/*     */   
/*     */   public static class Config
/*     */   {
/*  56 */     public String vertexShader = null;
/*     */     
/*  58 */     public String fragmentShader = null;
/*     */     
/*     */     public boolean ignoreUnimplemented = true;
/*  61 */     public int defaultCullFace = -1;
/*     */     
/*  63 */     public int defaultDepthFunc = -1;
/*  64 */     public ParticleShader.AlignMode align = ParticleShader.AlignMode.Screen;
/*  65 */     public ParticleShader.ParticleType type = ParticleShader.ParticleType.Billboard;
/*     */     public Config() {}
/*     */     public Config(ParticleShader.AlignMode align, ParticleShader.ParticleType type) {
/*  68 */       this.align = align;
/*  69 */       this.type = type;
/*     */     }
/*     */     
/*     */     public Config(ParticleShader.AlignMode align) {
/*  73 */       this.align = align;
/*     */     }
/*     */     
/*     */     public Config(ParticleShader.ParticleType type) {
/*  77 */       this.type = type;
/*     */     }
/*     */     
/*     */     public Config(String vertexShader, String fragmentShader) {
/*  81 */       this.vertexShader = vertexShader;
/*  82 */       this.fragmentShader = fragmentShader;
/*     */     }
/*     */   }
/*     */   
/*  86 */   private static String defaultVertexShader = null;
/*     */   public static String getDefaultVertexShader() {
/*  88 */     if (defaultVertexShader == null)
/*  89 */       defaultVertexShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/particles/particles.vertex.glsl").readString(); 
/*  90 */     return defaultVertexShader;
/*     */   }
/*     */   
/*  93 */   private static String defaultFragmentShader = null;
/*     */   public static String getDefaultFragmentShader() {
/*  95 */     if (defaultFragmentShader == null)
/*  96 */       defaultFragmentShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/particles/particles.fragment.glsl").readString(); 
/*  97 */     return defaultFragmentShader;
/*     */   }
/*     */   
/* 100 */   protected static long implementedFlags = BlendingAttribute.Type | TextureAttribute.Diffuse; private Renderable renderable; private long materialMask;
/*     */   private long vertexMask;
/* 102 */   static final Vector3 TMP_VECTOR3 = new Vector3(); protected final Config config;
/*     */   
/* 104 */   public static class Inputs { public static final BaseShader.Uniform cameraRight = new BaseShader.Uniform("u_cameraRight");
/* 105 */     public static final BaseShader.Uniform cameraInvDirection = new BaseShader.Uniform("u_cameraInvDirection");
/* 106 */     public static final BaseShader.Uniform screenWidth = new BaseShader.Uniform("u_screenWidth");
/* 107 */     public static final BaseShader.Uniform regionSize = new BaseShader.Uniform("u_regionSize"); }
/*     */   
/*     */   public static class Setters {
/* 110 */     public static final BaseShader.Setter cameraRight = new BaseShader.Setter() { public boolean isGlobal(BaseShader shader, int inputID) {
/* 111 */           return true;
/*     */         } public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 113 */           shader.set(inputID, ParticleShader.TMP_VECTOR3.set(shader.camera.direction).crs(shader.camera.up).nor());
/*     */         } }
/*     */     ;
/*     */     
/* 117 */     public static final BaseShader.Setter cameraUp = new BaseShader.Setter() { public boolean isGlobal(BaseShader shader, int inputID) {
/* 118 */           return true;
/*     */         } public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 120 */           shader.set(inputID, ParticleShader.TMP_VECTOR3.set(shader.camera.up).nor());
/*     */         } }
/*     */     ;
/*     */     
/* 124 */     public static final BaseShader.Setter cameraInvDirection = new BaseShader.Setter() { public boolean isGlobal(BaseShader shader, int inputID) {
/* 125 */           return true;
/*     */         } public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 127 */           shader.set(inputID, ParticleShader.TMP_VECTOR3.set(-shader.camera.direction.x, -shader.camera.direction.y, -shader.camera.direction.z).nor());
/*     */         } }
/*     */     ;
/* 130 */     public static final BaseShader.Setter cameraPosition = new BaseShader.Setter() { public boolean isGlobal(BaseShader shader, int inputID) {
/* 131 */           return true;
/*     */         } public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 133 */           shader.set(inputID, shader.camera.position);
/*     */         } }
/*     */     ;
/* 136 */     public static final BaseShader.Setter screenWidth = new BaseShader.Setter() { public boolean isGlobal(BaseShader shader, int inputID) {
/* 137 */           return true;
/*     */         } public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 139 */           shader.set(inputID, Gdx.graphics.getWidth());
/*     */         } }
/*     */     ;
/* 142 */     public static final BaseShader.Setter worldViewTrans = new BaseShader.Setter() {
/* 143 */         final Matrix4 temp = new Matrix4(); public boolean isGlobal(BaseShader shader, int inputID) {
/* 144 */           return false;
/*     */         } public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 146 */           shader.set(inputID, this.temp.set(shader.camera.view).mul(renderable.worldTransform));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   private static final long optionalAttributes = IntAttribute.CullFace | DepthTestAttribute.Type; Material currentMaterial;
/*     */   
/*     */   public ParticleShader(Renderable renderable) {
/* 160 */     this(renderable, new Config());
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config) {
/* 164 */     this(renderable, config, createPrefix(renderable, config));
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config, String prefix) {
/* 168 */     this(renderable, config, prefix, (config.vertexShader != null) ? config.vertexShader : 
/* 169 */         getDefaultVertexShader(), (config.fragmentShader != null) ? config.fragmentShader : 
/* 170 */         getDefaultFragmentShader());
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
/* 174 */     this(renderable, config, new ShaderProgram(prefix + vertexShader, prefix + fragmentShader));
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
/* 178 */     this.config = config;
/* 179 */     this.program = shaderProgram;
/* 180 */     this.renderable = renderable;
/* 181 */     this.materialMask = renderable.material.getMask() | optionalAttributes;
/* 182 */     this.vertexMask = renderable.meshPart.mesh.getVertexAttributes().getMask();
/*     */     
/* 184 */     if (!config.ignoreUnimplemented && (implementedFlags & this.materialMask) != this.materialMask) {
/* 185 */       throw new GdxRuntimeException("Some attributes not implemented yet (" + this.materialMask + ")");
/*     */     }
/*     */     
/* 188 */     register(DefaultShader.Inputs.viewTrans, DefaultShader.Setters.viewTrans);
/* 189 */     register(DefaultShader.Inputs.projViewTrans, DefaultShader.Setters.projViewTrans);
/* 190 */     register(DefaultShader.Inputs.projTrans, DefaultShader.Setters.projTrans);
/* 191 */     register(Inputs.screenWidth, Setters.screenWidth);
/* 192 */     register(DefaultShader.Inputs.cameraUp, Setters.cameraUp);
/* 193 */     register(Inputs.cameraRight, Setters.cameraRight);
/* 194 */     register(Inputs.cameraInvDirection, Setters.cameraInvDirection);
/* 195 */     register(DefaultShader.Inputs.cameraPosition, Setters.cameraPosition);
/*     */ 
/*     */     
/* 198 */     register(DefaultShader.Inputs.diffuseTexture, DefaultShader.Setters.diffuseTexture);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 203 */     ShaderProgram program = this.program;
/* 204 */     this.program = null;
/* 205 */     init(program, this.renderable);
/* 206 */     this.renderable = null;
/*     */   }
/*     */   
/*     */   public static String createPrefix(Renderable renderable, Config config) {
/* 210 */     String prefix = "";
/* 211 */     if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
/* 212 */       prefix = prefix + "#version 120\n";
/*     */     } else {
/* 214 */       prefix = prefix + "#version 100\n";
/* 215 */     }  if (config.type == ParticleType.Billboard) {
/* 216 */       prefix = prefix + "#define billboard\n";
/* 217 */       if (config.align == AlignMode.Screen) {
/* 218 */         prefix = prefix + "#define screenFacing\n";
/* 219 */       } else if (config.align == AlignMode.ViewPoint) {
/* 220 */         prefix = prefix + "#define viewPointFacing\n";
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     return prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRender(Renderable renderable) {
/* 229 */     return (this.materialMask == (renderable.material.getMask() | optionalAttributes) && this.vertexMask == renderable.meshPart.mesh
/* 230 */       .getVertexAttributes().getMask());
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Shader other) {
/* 235 */     if (other == null) return -1; 
/* 236 */     if (other == this) return 0; 
/* 237 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 242 */     return (obj instanceof ParticleShader) ? equals((ParticleShader)obj) : false;
/*     */   }
/*     */   
/*     */   public boolean equals(ParticleShader obj) {
/* 246 */     return (obj == this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(Camera camera, RenderContext context) {
/* 251 */     super.begin(camera, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Renderable renderable) {
/* 256 */     if (!renderable.material.has(BlendingAttribute.Type))
/* 257 */       this.context.setBlending(false, 770, 771); 
/* 258 */     bindMaterial(renderable);
/* 259 */     super.render(renderable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 264 */     this.currentMaterial = null;
/* 265 */     super.end();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void bindMaterial(Renderable renderable) {
/* 270 */     if (this.currentMaterial == renderable.material) {
/*     */       return;
/*     */     }
/* 273 */     int cullFace = (this.config.defaultCullFace == -1) ? 1029 : this.config.defaultCullFace;
/* 274 */     int depthFunc = (this.config.defaultDepthFunc == -1) ? 515 : this.config.defaultDepthFunc;
/* 275 */     float depthRangeNear = 0.0F;
/* 276 */     float depthRangeFar = 1.0F;
/* 277 */     boolean depthMask = true;
/*     */     
/* 279 */     this.currentMaterial = renderable.material;
/* 280 */     for (Attribute attr : this.currentMaterial) {
/* 281 */       long t = attr.type;
/* 282 */       if (BlendingAttribute.is(t)) {
/* 283 */         this.context.setBlending(true, ((BlendingAttribute)attr).sourceFunction, ((BlendingAttribute)attr).destFunction); continue;
/*     */       } 
/* 285 */       if ((t & DepthTestAttribute.Type) == DepthTestAttribute.Type) {
/* 286 */         DepthTestAttribute dta = (DepthTestAttribute)attr;
/* 287 */         depthFunc = dta.depthFunc;
/* 288 */         depthRangeNear = dta.depthRangeNear;
/* 289 */         depthRangeFar = dta.depthRangeFar;
/* 290 */         depthMask = dta.depthMask; continue;
/*     */       } 
/* 292 */       if (!this.config.ignoreUnimplemented) {
/* 293 */         throw new GdxRuntimeException("Unknown material attribute: " + attr.toString());
/*     */       }
/*     */     } 
/* 296 */     this.context.setCullFace(cullFace);
/* 297 */     this.context.setDepthTest(depthFunc, depthRangeNear, depthRangeFar);
/* 298 */     this.context.setDepthMask(depthMask);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 303 */     this.program.dispose();
/* 304 */     super.dispose();
/*     */   }
/*     */   
/*     */   public int getDefaultCullFace() {
/* 308 */     return (this.config.defaultCullFace == -1) ? 1029 : this.config.defaultCullFace;
/*     */   }
/*     */   
/*     */   public void setDefaultCullFace(int cullFace) {
/* 312 */     this.config.defaultCullFace = cullFace;
/*     */   }
/*     */   
/*     */   public int getDefaultDepthFunc() {
/* 316 */     return (this.config.defaultDepthFunc == -1) ? 515 : this.config.defaultDepthFunc;
/*     */   }
/*     */   
/*     */   public void setDefaultDepthFunc(int depthFunc) {
/* 320 */     this.config.defaultDepthFunc = depthFunc;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParticleShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */