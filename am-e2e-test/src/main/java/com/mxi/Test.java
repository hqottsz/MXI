package com.mxi;

import java.util.Date;

import com.mxi.driver.standard.Wait;
import com.mxi.driver.standard.condition.Condition;


/**
 * DOCUMENT_ME
 *
 */
public class Test {

   @org.junit.Test
   public void itWorks() {

      Wait.until( new Condition() {

         @Override
         public boolean evaluate() {
            return new Date().getSeconds() > 30;
         }


         @Override
         public String toString() {
            return "current second is greater than 45 seconds";
         }
      }, 10L );
   }
}
