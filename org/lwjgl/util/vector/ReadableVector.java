package org.lwjgl.util.vector;

import java.nio.FloatBuffer;

public interface ReadableVector {
  float length();
  
  float lengthSquared();
  
  Vector store(FloatBuffer paramFloatBuffer);
}


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\org\lwjg\\util\vector\ReadableVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */