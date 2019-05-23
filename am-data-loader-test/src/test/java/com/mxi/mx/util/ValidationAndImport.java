package com.mxi.mx.util;

/**
 * This interface is to define 2 abstract methods: validation only and Import
 *
 * @author ALICIA QIAN
 */

public interface ValidationAndImport {

   int runValidation( boolean allornone );


   int runImport( boolean allornone );

}
