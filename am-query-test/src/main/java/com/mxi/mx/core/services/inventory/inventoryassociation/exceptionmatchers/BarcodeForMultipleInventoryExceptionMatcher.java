package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.isIn;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.BarcodeForMultipleInventoryException;


public class BarcodeForMultipleInventoryExceptionMatcher
      extends TypeSafeMatcher<BarcodeForMultipleInventoryException> {

   private String iBarcode;
   private Map<InventoryKey, String> iInvKeyToDescMap;


   public BarcodeForMultipleInventoryExceptionMatcher(String aBarcode,
         Map<InventoryKey, String> aInvKeyToDescMap) {
      super();
      iBarcode = aBarcode;
      iInvKeyToDescMap = aInvKeyToDescMap;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      String lMapStr = "";
      for ( Map.Entry<InventoryKey, String> lEntry : iInvKeyToDescMap.entrySet() ) {
         lMapStr += ( "[inv key=" + lEntry.getKey() + ",desc=" + lEntry.getValue() + "]" );
      }
      aDescription.appendText( "expected Barcode=" + iBarcode + ", invkey to desc map=" + lMapStr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void describeMismatchSafely( BarcodeForMultipleInventoryException aItem,
         Description aMismatchDescription ) {

      String lMapStr = "";
      for ( Map.Entry<InventoryKey, String> lEntry : aItem.getInvKeyToDescMap().entrySet() ) {
         lMapStr += ( "[inv key=" + lEntry.getKey() + ",desc=" + lEntry.getValue() + "]" );
      }
      aMismatchDescription.appendText( "was Barcode=" + aItem.getInventoryBarcode().get()
            + ", invkey to desc map=" + lMapStr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( BarcodeForMultipleInventoryException aException ) {

      if ( !iBarcode.equals( aException.getInventoryBarcode().get() ) ) {
         return false;
      }

      if ( iInvKeyToDescMap.size() != aException.getInvKeyToDescMap().size() ) {
         return false;
      }

      Set<Entry<InventoryKey, String>> lActualEntrySet = aException.getInvKeyToDescMap().entrySet();
      Set<Entry<InventoryKey, String>> lExpectedEntrySet = iInvKeyToDescMap.entrySet();

      if ( !everyItem( isIn( lExpectedEntrySet ) ).matches( lActualEntrySet ) ) {
         return false;
      }

      return true;
   }

}
