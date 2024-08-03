/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Selection;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class Tree
/*     */   extends WidgetGroup
/*     */ {
/*     */   TreeStyle style;
/*  40 */   final Array<Node> rootNodes = new Array();
/*     */   final Selection<Node> selection;
/*  42 */   float ySpacing = 4.0F; float iconSpacingLeft = 2.0F; float iconSpacingRight = 2.0F; float padding = 0.0F;
/*     */   
/*     */   float indentSpacing;
/*     */   private float leftColumnWidth;
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   
/*     */   public Tree(Skin skin) {
/*  50 */     this(skin.<TreeStyle>get(TreeStyle.class));
/*     */   }
/*     */   private boolean sizeInvalid = true; private Node foundNode; Node overNode; private ClickListener clickListener;
/*     */   public Tree(Skin skin, String styleName) {
/*  54 */     this(skin.<TreeStyle>get(styleName, TreeStyle.class));
/*     */   }
/*     */   
/*     */   public Tree(TreeStyle style) {
/*  58 */     this.selection = new Selection();
/*  59 */     this.selection.setActor((Actor)this);
/*  60 */     this.selection.setMultiple(true);
/*  61 */     setStyle(style);
/*  62 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  66 */     addListener((EventListener)(this.clickListener = new ClickListener() {
/*     */           public void clicked(InputEvent event, float x, float y) {
/*  68 */             Tree.Node node = Tree.this.getNodeAt(y);
/*  69 */             if (node == null)
/*  70 */               return;  if (node != Tree.this.getNodeAt(getTouchDownY()))
/*  71 */               return;  if (Tree.this.selection.getMultiple() && Tree.this.selection.hasItems() && UIUtils.shift()) {
/*     */               
/*  73 */               float low = ((Tree.Node)Tree.this.selection.getLastSelected()).actor.getY();
/*  74 */               float high = node.actor.getY();
/*  75 */               if (!UIUtils.ctrl()) Tree.this.selection.clear(); 
/*  76 */               if (low > high) {
/*  77 */                 Tree.this.selectNodes(Tree.this.rootNodes, high, low);
/*     */               } else {
/*  79 */                 Tree.this.selectNodes(Tree.this.rootNodes, low, high);
/*  80 */               }  Tree.this.selection.fireChangeEvent();
/*     */               return;
/*     */             } 
/*  83 */             if (node.children.size > 0 && (!Tree.this.selection.getMultiple() || !UIUtils.ctrl())) {
/*     */               
/*  85 */               float rowX = node.actor.getX();
/*  86 */               if (node.icon != null) rowX -= Tree.this.iconSpacingRight + node.icon.getMinWidth(); 
/*  87 */               if (x < rowX) {
/*  88 */                 node.setExpanded(!node.expanded);
/*     */                 return;
/*     */               } 
/*     */             } 
/*  92 */             if (!node.isSelectable())
/*  93 */               return;  Tree.this.selection.choose(node);
/*     */           }
/*     */           
/*     */           public boolean mouseMoved(InputEvent event, float x, float y) {
/*  97 */             Tree.this.setOverNode(Tree.this.getNodeAt(y));
/*  98 */             return false;
/*     */           }
/*     */           
/*     */           public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/* 102 */             super.exit(event, x, y, pointer, toActor);
/* 103 */             if (toActor == null || !toActor.isDescendantOf((Actor)Tree.this)) Tree.this.setOverNode((Tree.Node)null); 
/*     */           }
/*     */         }));
/*     */   }
/*     */   
/*     */   public void setStyle(TreeStyle style) {
/* 109 */     this.style = style;
/* 110 */     this.indentSpacing = Math.max(style.plus.getMinWidth(), style.minus.getMinWidth()) + this.iconSpacingLeft;
/*     */   }
/*     */   
/*     */   public void add(Node node) {
/* 114 */     insert(this.rootNodes.size, node);
/*     */   }
/*     */   
/*     */   public void insert(int index, Node node) {
/* 118 */     remove(node);
/* 119 */     node.parent = null;
/* 120 */     this.rootNodes.insert(index, node);
/* 121 */     node.addToTree(this);
/* 122 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public void remove(Node node) {
/* 126 */     if (node.parent != null) {
/* 127 */       node.parent.remove(node);
/*     */       return;
/*     */     } 
/* 130 */     this.rootNodes.removeValue(node, true);
/* 131 */     node.removeFromTree(this);
/* 132 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearChildren() {
/* 137 */     super.clearChildren();
/* 138 */     setOverNode((Node)null);
/* 139 */     this.rootNodes.clear();
/* 140 */     this.selection.clear();
/*     */   }
/*     */   
/*     */   public Array<Node> getNodes() {
/* 144 */     return this.rootNodes;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 148 */     super.invalidate();
/* 149 */     this.sizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void computeSize() {
/* 153 */     this.sizeInvalid = false;
/* 154 */     this.prefWidth = this.style.plus.getMinWidth();
/* 155 */     this.prefWidth = Math.max(this.prefWidth, this.style.minus.getMinWidth());
/* 156 */     this.prefHeight = getHeight();
/* 157 */     this.leftColumnWidth = 0.0F;
/* 158 */     computeSize(this.rootNodes, this.indentSpacing);
/* 159 */     this.leftColumnWidth += this.iconSpacingLeft + this.padding;
/* 160 */     this.prefWidth += this.leftColumnWidth + this.padding;
/* 161 */     this.prefHeight = getHeight() - this.prefHeight;
/*     */   }
/*     */   
/*     */   private void computeSize(Array<Node> nodes, float indent) {
/* 165 */     float ySpacing = this.ySpacing;
/* 166 */     float spacing = this.iconSpacingLeft + this.iconSpacingRight;
/* 167 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 168 */       Node node = (Node)nodes.get(i);
/* 169 */       float rowWidth = indent + this.iconSpacingRight;
/* 170 */       Actor actor = node.actor;
/* 171 */       if (actor instanceof Layout) {
/* 172 */         Layout layout = (Layout)actor;
/* 173 */         rowWidth += layout.getPrefWidth();
/* 174 */         node.height = layout.getPrefHeight();
/* 175 */         layout.pack();
/*     */       } else {
/* 177 */         rowWidth += actor.getWidth();
/* 178 */         node.height = actor.getHeight();
/*     */       } 
/* 180 */       if (node.icon != null) {
/* 181 */         rowWidth += spacing + node.icon.getMinWidth();
/* 182 */         node.height = Math.max(node.height, node.icon.getMinHeight());
/*     */       } 
/* 184 */       this.prefWidth = Math.max(this.prefWidth, rowWidth);
/* 185 */       this.prefHeight -= node.height + ySpacing;
/* 186 */       if (node.expanded) computeSize(node.children, indent + this.indentSpacing); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void layout() {
/* 191 */     if (this.sizeInvalid) computeSize(); 
/* 192 */     layout(this.rootNodes, this.leftColumnWidth + this.indentSpacing + this.iconSpacingRight, getHeight() - this.ySpacing / 2.0F);
/*     */   }
/*     */   
/*     */   private float layout(Array<Node> nodes, float indent, float y) {
/* 196 */     float ySpacing = this.ySpacing;
/* 197 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 198 */       Node node = (Node)nodes.get(i);
/* 199 */       Actor actor = node.actor;
/* 200 */       float x = indent;
/* 201 */       if (node.icon != null) x += node.icon.getMinWidth(); 
/* 202 */       y -= node.height;
/* 203 */       node.actor.setPosition(x, y);
/* 204 */       y -= ySpacing;
/* 205 */       if (node.expanded) y = layout(node.children, indent + this.indentSpacing, y); 
/*     */     } 
/* 207 */     return y;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 211 */     Color color = getColor();
/* 212 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/* 213 */     if (this.style.background != null) this.style.background.draw(batch, getX(), getY(), getWidth(), getHeight()); 
/* 214 */     draw(batch, this.rootNodes, this.leftColumnWidth);
/* 215 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */ 
/*     */   
/*     */   private void draw(Batch batch, Array<Node> nodes, float indent) {
/* 220 */     Drawable plus = this.style.plus, minus = this.style.minus;
/* 221 */     float x = getX(), y = getY();
/* 222 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 223 */       Node node = (Node)nodes.get(i);
/* 224 */       Actor actor = node.actor;
/*     */       
/* 226 */       if (this.selection.contains(node) && this.style.selection != null) {
/* 227 */         this.style.selection.draw(batch, x, y + actor.getY() - this.ySpacing / 2.0F, getWidth(), node.height + this.ySpacing);
/* 228 */       } else if (node == this.overNode && this.style.over != null) {
/* 229 */         this.style.over.draw(batch, x, y + actor.getY() - this.ySpacing / 2.0F, getWidth(), node.height + this.ySpacing);
/*     */       } 
/*     */       
/* 232 */       if (node.icon != null) {
/* 233 */         float iconY = actor.getY() + Math.round((node.height - node.icon.getMinHeight()) / 2.0F);
/* 234 */         batch.setColor(actor.getColor());
/* 235 */         node.icon.draw(batch, x + node.actor.getX() - this.iconSpacingRight - node.icon.getMinWidth(), y + iconY, node.icon
/* 236 */             .getMinWidth(), node.icon.getMinHeight());
/* 237 */         batch.setColor(Color.WHITE);
/*     */       } 
/*     */       
/* 240 */       if (node.children.size != 0) {
/*     */         
/* 242 */         Drawable expandIcon = node.expanded ? minus : plus;
/* 243 */         float iconY = actor.getY() + Math.round((node.height - expandIcon.getMinHeight()) / 2.0F);
/* 244 */         expandIcon.draw(batch, x + indent - this.iconSpacingLeft, y + iconY, expandIcon.getMinWidth(), expandIcon.getMinHeight());
/* 245 */         if (node.expanded) draw(batch, node.children, indent + this.indentSpacing); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Node getNodeAt(float y) {
/* 251 */     this.foundNode = null;
/* 252 */     getNodeAt(this.rootNodes, y, getHeight());
/* 253 */     return this.foundNode;
/*     */   }
/*     */   
/*     */   private float getNodeAt(Array<Node> nodes, float y, float rowY) {
/* 257 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 258 */       Node node = (Node)nodes.get(i);
/* 259 */       if (y >= rowY - node.height - this.ySpacing && y < rowY) {
/* 260 */         this.foundNode = node;
/* 261 */         return -1.0F;
/*     */       } 
/* 263 */       rowY -= node.height + this.ySpacing;
/* 264 */       if (node.expanded) {
/* 265 */         rowY = getNodeAt(node.children, y, rowY);
/* 266 */         if (rowY == -1.0F) return -1.0F; 
/*     */       } 
/*     */     } 
/* 269 */     return rowY;
/*     */   }
/*     */   
/*     */   void selectNodes(Array<Node> nodes, float low, float high) {
/* 273 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 274 */       Node node = (Node)nodes.get(i);
/* 275 */       if (node.actor.getY() < low)
/* 276 */         break;  if (node.isSelectable()) {
/* 277 */         if (node.actor.getY() <= high) this.selection.add(node); 
/* 278 */         if (node.expanded) selectNodes(node.children, low, high); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public Selection<Node> getSelection() {
/* 283 */     return this.selection;
/*     */   }
/*     */   
/*     */   public TreeStyle getStyle() {
/* 287 */     return this.style;
/*     */   }
/*     */   
/*     */   public Array<Node> getRootNodes() {
/* 291 */     return this.rootNodes;
/*     */   }
/*     */   
/*     */   public Node getOverNode() {
/* 295 */     return this.overNode;
/*     */   }
/*     */   
/*     */   public void setOverNode(Node overNode) {
/* 299 */     this.overNode = overNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadding(float padding) {
/* 304 */     this.padding = padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getIndentSpacing() {
/* 309 */     return this.indentSpacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYSpacing(float ySpacing) {
/* 314 */     this.ySpacing = ySpacing;
/*     */   }
/*     */   
/*     */   public float getYSpacing() {
/* 318 */     return this.ySpacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIconSpacing(float left, float right) {
/* 323 */     this.iconSpacingLeft = left;
/* 324 */     this.iconSpacingRight = right;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 328 */     if (this.sizeInvalid) computeSize(); 
/* 329 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 333 */     if (this.sizeInvalid) computeSize(); 
/* 334 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   public void findExpandedObjects(Array objects) {
/* 338 */     findExpandedObjects(this.rootNodes, objects);
/*     */   }
/*     */   
/*     */   public void restoreExpandedObjects(Array objects) {
/* 342 */     for (int i = 0, n = objects.size; i < n; i++) {
/* 343 */       Node node = findNode(objects.get(i));
/* 344 */       if (node != null) {
/* 345 */         node.setExpanded(true);
/* 346 */         node.expandTo();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean findExpandedObjects(Array<Node> nodes, Array objects) {
/* 352 */     boolean expanded = false;
/* 353 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 354 */       Node node = (Node)nodes.get(i);
/* 355 */       if (node.expanded && !findExpandedObjects(node.children, objects)) objects.add(node.object); 
/*     */     } 
/* 357 */     return expanded;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node findNode(Object object) {
/* 362 */     if (object == null) throw new IllegalArgumentException("object cannot be null."); 
/* 363 */     return findNode(this.rootNodes, object);
/*     */   } static Node findNode(Array<Node> nodes, Object object) {
/*     */     int i;
/*     */     int n;
/* 367 */     for (i = 0, n = nodes.size; i < n; i++) {
/* 368 */       Node node = (Node)nodes.get(i);
/* 369 */       if (object.equals(node.object)) return node; 
/*     */     } 
/* 371 */     for (i = 0, n = nodes.size; i < n; i++) {
/* 372 */       Node node = (Node)nodes.get(i);
/* 373 */       Node found = findNode(node.children, object);
/* 374 */       if (found != null) return found; 
/*     */     } 
/* 376 */     return null;
/*     */   }
/*     */   
/*     */   public void collapseAll() {
/* 380 */     collapseAll(this.rootNodes);
/*     */   }
/*     */   
/*     */   static void collapseAll(Array<Node> nodes) {
/* 384 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 385 */       Node node = (Node)nodes.get(i);
/* 386 */       node.setExpanded(false);
/* 387 */       collapseAll(node.children);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void expandAll() {
/* 392 */     expandAll(this.rootNodes);
/*     */   }
/*     */   
/*     */   static void expandAll(Array<Node> nodes) {
/* 396 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 397 */       ((Node)nodes.get(i)).expandAll();
/*     */     }
/*     */   }
/*     */   
/*     */   public ClickListener getClickListener() {
/* 402 */     return this.clickListener;
/*     */   }
/*     */   
/*     */   public static class Node {
/*     */     Actor actor;
/*     */     Node parent;
/* 408 */     final Array<Node> children = new Array(0);
/*     */     boolean selectable = true;
/*     */     boolean expanded;
/*     */     Drawable icon;
/*     */     float height;
/*     */     Object object;
/*     */     
/*     */     public Node(Actor actor) {
/* 416 */       if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 417 */       this.actor = actor;
/*     */     }
/*     */     
/*     */     public void setExpanded(boolean expanded) {
/* 421 */       if (expanded == this.expanded)
/* 422 */         return;  this.expanded = expanded;
/* 423 */       if (this.children.size == 0)
/* 424 */         return;  Tree tree = getTree();
/* 425 */       if (tree == null)
/* 426 */         return;  if (expanded) {
/* 427 */         for (int i = 0, n = this.children.size; i < n; i++)
/* 428 */           ((Node)this.children.get(i)).addToTree(tree); 
/*     */       } else {
/* 430 */         for (int i = 0, n = this.children.size; i < n; i++)
/* 431 */           ((Node)this.children.get(i)).removeFromTree(tree); 
/*     */       } 
/* 433 */       tree.invalidateHierarchy();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addToTree(Tree tree) {
/* 438 */       tree.addActor(this.actor);
/* 439 */       if (!this.expanded)
/* 440 */         return;  for (int i = 0, n = this.children.size; i < n; i++) {
/* 441 */         ((Node)this.children.get(i)).addToTree(tree);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void removeFromTree(Tree tree) {
/* 446 */       tree.removeActor(this.actor);
/* 447 */       if (!this.expanded)
/* 448 */         return;  for (int i = 0, n = this.children.size; i < n; i++)
/* 449 */         ((Node)this.children.get(i)).removeFromTree(tree); 
/*     */     }
/*     */     
/*     */     public void add(Node node) {
/* 453 */       insert(this.children.size, node);
/*     */     }
/*     */     
/*     */     public void addAll(Array<Node> nodes) {
/* 457 */       for (int i = 0, n = nodes.size; i < n; i++)
/* 458 */         insert(this.children.size, (Node)nodes.get(i)); 
/*     */     }
/*     */     
/*     */     public void insert(int index, Node node) {
/* 462 */       node.parent = this;
/* 463 */       this.children.insert(index, node);
/* 464 */       updateChildren();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 468 */       Tree tree = getTree();
/* 469 */       if (tree != null) {
/* 470 */         tree.remove(this);
/* 471 */       } else if (this.parent != null) {
/* 472 */         this.parent.remove(this);
/*     */       } 
/*     */     }
/*     */     public void remove(Node node) {
/* 476 */       this.children.removeValue(node, true);
/* 477 */       if (!this.expanded)
/* 478 */         return;  Tree tree = getTree();
/* 479 */       if (tree == null)
/* 480 */         return;  node.removeFromTree(tree);
/* 481 */       if (this.children.size == 0) this.expanded = false; 
/*     */     }
/*     */     
/*     */     public void removeAll() {
/* 485 */       Tree tree = getTree();
/* 486 */       if (tree != null)
/* 487 */         for (int i = 0, n = this.children.size; i < n; i++) {
/* 488 */           ((Node)this.children.get(i)).removeFromTree(tree);
/*     */         } 
/* 490 */       this.children.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Tree getTree() {
/* 495 */       Group parent = this.actor.getParent();
/* 496 */       if (!(parent instanceof Tree)) return null; 
/* 497 */       return (Tree)parent;
/*     */     }
/*     */     
/*     */     public Actor getActor() {
/* 501 */       return this.actor;
/*     */     }
/*     */     
/*     */     public boolean isExpanded() {
/* 505 */       return this.expanded;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<Node> getChildren() {
/* 510 */       return this.children;
/*     */     }
/*     */     
/*     */     public void updateChildren() {
/* 514 */       if (!this.expanded)
/* 515 */         return;  Tree tree = getTree();
/* 516 */       if (tree == null)
/* 517 */         return;  for (int i = 0, n = this.children.size; i < n; i++) {
/* 518 */         ((Node)this.children.get(i)).addToTree(tree);
/*     */       }
/*     */     }
/*     */     
/*     */     public Node getParent() {
/* 523 */       return this.parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setIcon(Drawable icon) {
/* 528 */       this.icon = icon;
/*     */     }
/*     */     
/*     */     public Object getObject() {
/* 532 */       return this.object;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setObject(Object object) {
/* 537 */       this.object = object;
/*     */     }
/*     */     
/*     */     public Drawable getIcon() {
/* 541 */       return this.icon;
/*     */     }
/*     */     
/*     */     public int getLevel() {
/* 545 */       int level = 0;
/* 546 */       Node current = this;
/*     */       while (true) {
/* 548 */         level++;
/* 549 */         current = current.getParent();
/* 550 */         if (current == null)
/* 551 */           return level; 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Node findNode(Object object) {
/* 556 */       if (object == null) throw new IllegalArgumentException("object cannot be null."); 
/* 557 */       if (object.equals(this.object)) return this; 
/* 558 */       return Tree.findNode(this.children, object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void collapseAll() {
/* 563 */       setExpanded(false);
/* 564 */       Tree.collapseAll(this.children);
/*     */     }
/*     */ 
/*     */     
/*     */     public void expandAll() {
/* 569 */       setExpanded(true);
/* 570 */       if (this.children.size > 0) Tree.expandAll(this.children);
/*     */     
/*     */     }
/*     */     
/*     */     public void expandTo() {
/* 575 */       Node node = this.parent;
/* 576 */       while (node != null) {
/* 577 */         node.setExpanded(true);
/* 578 */         node = node.parent;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isSelectable() {
/* 583 */       return this.selectable;
/*     */     }
/*     */     
/*     */     public void setSelectable(boolean selectable) {
/* 587 */       this.selectable = selectable;
/*     */     }
/*     */     
/*     */     public void findExpandedObjects(Array objects) {
/* 591 */       if (this.expanded && !Tree.findExpandedObjects(this.children, objects)) objects.add(this.object); 
/*     */     }
/*     */     
/*     */     public void restoreExpandedObjects(Array objects) {
/* 595 */       for (int i = 0, n = objects.size; i < n; i++) {
/* 596 */         Node node = findNode(objects.get(i));
/* 597 */         if (node != null) {
/* 598 */           node.setExpanded(true);
/* 599 */           node.expandTo();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TreeStyle
/*     */   {
/*     */     public Drawable plus;
/*     */     public Drawable minus;
/*     */     public Drawable over;
/*     */     public Drawable selection;
/*     */     public Drawable background;
/*     */     
/*     */     public TreeStyle() {}
/*     */     
/*     */     public TreeStyle(Drawable plus, Drawable minus, Drawable selection) {
/* 616 */       this.plus = plus;
/* 617 */       this.minus = minus;
/* 618 */       this.selection = selection;
/*     */     }
/*     */     
/*     */     public TreeStyle(TreeStyle style) {
/* 622 */       this.plus = style.plus;
/* 623 */       this.minus = style.minus;
/* 624 */       this.selection = style.selection;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\Tree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */