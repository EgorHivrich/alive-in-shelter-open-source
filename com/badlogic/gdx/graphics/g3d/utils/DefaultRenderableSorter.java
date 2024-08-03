/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultRenderableSorter
/*    */   implements RenderableSorter, Comparator<Renderable>
/*    */ {
/*    */   private Camera camera;
/* 30 */   private final Vector3 tmpV1 = new Vector3();
/* 31 */   private final Vector3 tmpV2 = new Vector3();
/*    */ 
/*    */   
/*    */   public void sort(Camera camera, Array<Renderable> renderables) {
/* 35 */     this.camera = camera;
/* 36 */     renderables.sort(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Renderable o1, Renderable o2) {
/* 41 */     boolean b1 = (o1.material.has(BlendingAttribute.Type) && ((BlendingAttribute)o1.material.get(BlendingAttribute.Type)).blended);
/* 42 */     boolean b2 = (o2.material.has(BlendingAttribute.Type) && ((BlendingAttribute)o2.material.get(BlendingAttribute.Type)).blended);
/* 43 */     if (b1 != b2) return b1 ? 1 : -1;
/*    */ 
/*    */ 
/*    */     
/* 47 */     o1.worldTransform.getTranslation(this.tmpV1);
/* 48 */     o2.worldTransform.getTranslation(this.tmpV2);
/* 49 */     float dst = ((int)(1000.0F * this.camera.position.dst2(this.tmpV1)) - (int)(1000.0F * this.camera.position.dst2(this.tmpV2)));
/* 50 */     int result = (dst < 0.0F) ? -1 : ((dst > 0.0F) ? 1 : 0);
/* 51 */     return b1 ? -result : result;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3\\utils\DefaultRenderableSorter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */