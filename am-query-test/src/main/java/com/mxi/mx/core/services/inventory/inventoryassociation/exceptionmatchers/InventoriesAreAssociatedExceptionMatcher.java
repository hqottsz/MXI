package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.InventoriesAreAssociatedException;


public class InventoriesAreAssociatedExceptionMatcher
      extends TypeSafeMatcher<InventoriesAreAssociatedException> {

   private InventoryKey iFromInventory;
   private String iFromInvDescription;
   private InventoryKey iToInventory;
   private String iToInvDescription;
   private Integer iAssociation;


   public InventoriesAreAssociatedExceptionMatcher(InventoryKey aFromInventory,
         String aFromInvDescription, InventoryKey aToInventory, String aToInvDescription,
         Integer aAssociation) {
      super();
      iFromInventory = aFromInventory;
      iFromInvDescription = aFromInvDescription;
      iToInventory = aToInventory;
      iToInvDescription = aToInvDescription;
      iAssociation = aAssociation;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "expected From-InventoryKey=" + iFromInventory
            + ", From-Description=" + iFromInvDescription + ", To-InventoryKey=" + iToInventory
            + ", To-Description=" + iToInvDescription + ", Association Id=" + iAssociation );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void describeMismatchSafely( InventoriesAreAssociatedException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "expected From-InventoryKey=" + aItem.getFromInventory()
            + ", From-Description=" + aItem.getFromInventoryDescription() + ", To-InventoryKey="
            + aItem.getToInventory() + ", To-Description=" + aItem.getToInventoryDescription()
            + ", Association Id=" + aItem.getAssociationId() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( InventoriesAreAssociatedException aException ) {
      return ( iFromInventory.equals( aException.getFromInventory() )
            && iFromInvDescription.equals( aException.getFromInventoryDescription() )
            && iToInventory.equals( aException.getToInventory() )
            && iToInvDescription.equals( aException.getToInventoryDescription() )
            && iAssociation.equals( aException.getAssociationId() ) );
   }

}
