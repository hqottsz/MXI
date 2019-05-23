package com.mxi.mx.core.services.inventory.reservation.auto;

import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;


/**
 * Helper class to create parts.
 *
 */
public class CreatePartUtils {

   /**
    *
    * Create a new part with a default part group.
    *
    * @param aInventoryClass
    * @return the part key
    */
   public static PartNoKey createPart( String aPartNoOem, RefInvClassKey aInventoryClass ) {

      return new PartNoBuilder().withOemPartNo( aPartNoOem ).withInventoryClass( aInventoryClass )
            .withStatus( RefPartStatusKey.ACTV ).withDefaultPartGroup().build();
   }


   /**
    *
    * Create a part group.
    *
    * @param aGroupName
    * @param aInventoryClass
    * @return the part group key
    */
   public static PartGroupKey createPartGroup( String aGroupName, RefInvClassKey aInventoryClass ) {
      return new PartGroupDomainBuilder( aGroupName ).withInventoryClass( aInventoryClass ).build();
   }


   /**
    *
    * Add a part to a part group.
    *
    * @param aPartNo
    * @param aPartGroup
    * @param aIsStandard
    *           true if this part is the standard in the part group
    */
   public static void addPartToPartGroup( PartNoKey aPartNo, PartGroupKey aPartGroup,
         boolean aIsStandard ) {
      EqpPartBaselineTable lEqpPartBaseline =
            EqpPartBaselineTable.create( new EqpPartBaselineKey( aPartGroup, aPartNo ) );
      lEqpPartBaseline.setApprovedBool( true );
      lEqpPartBaseline.setStandardBool( aIsStandard );
      lEqpPartBaseline.insert();
   }

}
