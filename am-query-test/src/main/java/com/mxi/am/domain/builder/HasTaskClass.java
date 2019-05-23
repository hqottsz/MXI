
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.RefTaskClassKey;


/**
 * This interface declares that a class has a task class.
 *
 * @author dsewell
 */
public interface HasTaskClass {

   public RefTaskClassKey getTaskClass();
}
