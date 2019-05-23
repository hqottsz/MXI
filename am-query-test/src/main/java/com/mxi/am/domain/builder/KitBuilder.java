package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.key.MxKey;
import com.mxi.mx.core.key.EqpKitPartGroupKey;
import com.mxi.mx.core.key.EqpKitPartGroupMapKey;
import com.mxi.mx.core.key.EqpKitPartMapKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.table.eqp.EqpKitPartGroupMapTable;
import com.mxi.mx.core.table.eqp.EqpKitPartGroupTable;
import com.mxi.mx.core.table.eqp.EqpKitPartMapTable;


/**
 * Builds a <code>eqp_part_no for kit</code> object
 */
public class KitBuilder implements DomainBuilder<PartNoKey> {

   // private PartNoKey iKit;
   private PartNoKey iKit;
   private List<MxKey[]> iKitContents = new ArrayList<MxKey[]>();
   private Double iKitQuantity = 1.0;


   // private boolean iReceiptInspBool = false;

   public KitBuilder(PartNoKey aKit) {
      iKit = aKit;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PartNoKey build() {

      int lKitContentSize = iKitContents.size();

      double lValuePct = 100 / lKitContentSize;

      for ( int i = 0; i < lKitContentSize; i++ ) {

         MxKey[] lKitContent = iKitContents.get( i );

         if ( i == lKitContentSize - 1 ) {
            lValuePct = 1 - ( lKitContentSize - 1 ) * lValuePct;
         }

         // Create the content part group in kit domain
         EqpKitPartGroupTable lEqpKitPartGroupTable = EqpKitPartGroupTable.create();
         lEqpKitPartGroupTable.setBomPart( ( PartGroupKey ) lKitContent[1] );
         lEqpKitPartGroupTable.setKitQt( iKitQuantity );
         lEqpKitPartGroupTable.setValuePct( lValuePct );
         EqpKitPartGroupKey lEqpKitPartGroupKey = lEqpKitPartGroupTable.insert();

         // Create the mapping between kit and the content part group
         EqpKitPartGroupMapTable lEqpKitPartGroupMap = EqpKitPartGroupMapTable
               .create( new EqpKitPartGroupMapKey( iKit, lEqpKitPartGroupKey ) );
         lEqpKitPartGroupMap.insert();

         // Create the mapping between kit and the content part
         EqpKitPartMapTable lEqpKitPartMapTable = EqpKitPartMapTable
               .create( new EqpKitPartMapKey( lEqpKitPartGroupKey, ( PartNoKey ) lKitContent[0] ) );
         lEqpKitPartMapTable.insert();

      }

      return iKit;
   }


   public KitBuilder withContent( PartNoKey aKitContent, PartGroupKey aKitContentPartGroupKey ) {
      iKitContents.add( new MxKey[] { aKitContent, aKitContentPartGroupKey } );
      return this;
   }


   public KitBuilder withKitQuantity( Double aKitQuantity ) {
      iKitQuantity = aKitQuantity;
      return this;
   }

}
