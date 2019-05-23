package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.BarcodeNotAnInventoryException;


public class BarcodeNotAnInventoryExceptionMatcher
      extends TypeSafeMatcher<BarcodeNotAnInventoryException> {

   private String iBarcode;


   public BarcodeNotAnInventoryExceptionMatcher(String aBarcode) {
      super();
      iBarcode = aBarcode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "expected Barcode=" + iBarcode );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void describeMismatchSafely( BarcodeNotAnInventoryException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "was Barcode=" + aItem.getInventoryBarcode().get() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( BarcodeNotAnInventoryException aException ) {
      return ( iBarcode.equals( aException.getInventoryBarcode().get() ) );
   }

}
