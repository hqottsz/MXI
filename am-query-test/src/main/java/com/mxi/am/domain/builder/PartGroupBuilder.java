package com.mxi.am.domain.builder;

import java.util.List;

import com.mxi.am.domain.PartGroup;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.table.eqp.EqpBomPart;


public class PartGroupBuilder {

   public static PartGroupKey build( PartGroup aPartGroup ) {

      PartGroupDomainBuilder lPartGroupBuilder = new PartGroupDomainBuilder( aPartGroup.getCode() );

      lPartGroupBuilder.withApplicabilityRange( aPartGroup.getApplicabilityRange() );
      lPartGroupBuilder.withPartGroupName( aPartGroup.getName() );
      lPartGroupBuilder.withInventoryClass( aPartGroup.getInventoryClass() );

      if ( aPartGroup.getConfigurationSlot() != null ) {
         lPartGroupBuilder.withConfigSlot( aPartGroup.getConfigurationSlot() );
      }

      // Add parts to the part group (build the part if given a configuration).
      List<PartNoKey> lPartKeys = PartBuilder.buildAll( aPartGroup.getPartConfigurations() );
      lPartKeys.addAll( aPartGroup.getParts() );
      for ( PartNoKey lPartKey : lPartKeys ) {
         lPartGroupBuilder.withPartNo( lPartKey );
      }

      PartGroupKey lPartGroupKey = lPartGroupBuilder.build();
      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( lPartGroupKey );
      if ( aPartGroup.isLineReplaceableUnit().isPresent() ) {
         lEqpBomPart.setLineReplaceableUnit( aPartGroup.isLineReplaceableUnit().get() );
      }
      lEqpBomPart.update();
      return lPartGroupKey;
   }

}
