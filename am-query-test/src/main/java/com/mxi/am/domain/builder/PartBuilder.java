package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartAdvisory;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;


public class PartBuilder {

   public static PartNoKey build( Part part ) {

      PartNoBuilder partNoBuilder = new PartNoBuilder();

      RefInvClassKey inventoryClass =
            ( RefInvClassKey ) defaultIfNull( part.getInventoryClass(), RefInvClassKey.BATCH );
      partNoBuilder.withInventoryClass( inventoryClass );
      partNoBuilder.withStatus( part.getPartStatus() );
      partNoBuilder.withShortDescription( part.getShortDescription() );
      partNoBuilder.withAverageUnitPrice(
            ( BigDecimal ) defaultIfNull( part.getAverageUnitPrice(), BigDecimal.ZERO ) );
      partNoBuilder.withFinancialType( ( RefFinanceTypeKey ) defaultIfNull( part.getFinancialType(),
            new RefFinanceTypeKey( 0, "BLKOUT" ) ) );
      partNoBuilder.withTotalQuantity(
            ( BigDecimal ) defaultIfNull( part.getQuantity(), BigDecimal.ZERO ) );
      partNoBuilder.withTotalValue(
            ( BigDecimal ) defaultIfNull( part.getTotalValue(), BigDecimal.ZERO ) );
      partNoBuilder.withStorageDescription( part.getStorageDescription() );

      if ( part.isTool() ) {
         partNoBuilder.isTool();
      }
      partNoBuilder.isAlternateIn( part.getPartGroup() );

      partNoBuilder.withApplicabilityRange( part.getApplicabilityRange() );

      if ( part.isPartGroupApproved().orElse( false ) ) {
         partNoBuilder.isPartGroupApproved();
      }

      if ( part.isInDefaultPartGroup().orElse( false ) ) {
         partNoBuilder.withDefaultPartGroup();
      }

      partNoBuilder.withOemPartNo( part.getCode() );
      partNoBuilder.manufacturedBy( part.getManufacturer() );
      partNoBuilder.withUnitType( part.getQtyUnitKey() );
      partNoBuilder.withStock( part.getStockNoKey() );
      partNoBuilder.withAssetAccount( part.getFinancialAccount() );
      partNoBuilder.withPartType( part.getPartType() );
      partNoBuilder.withHazmat( part.getHazmat() );

      PartNoKey partNoKey = partNoBuilder.build();

      EqpPartNoTable eqpPartNoTable = EqpPartNoTable.findByPrimaryKey( partNoKey );
      if ( part.isRepairable().isPresent() ) {
         eqpPartNoTable.setRepairBool( part.isRepairable().get() );
      }
      eqpPartNoTable.update();

      // Add Part Advisory
      for ( PartAdvisory advisory : part.getPartAdvisories() ) {

         advisory.setPartNo( partNoKey );
         PartAdvisoryBuilder.build( advisory );
      }

      return partNoKey;
   }


   public static List<PartNoKey> buildAll( List<DomainConfiguration<Part>> aPartConfigurations ) {

      List<PartNoKey> lPartKeys = new ArrayList<PartNoKey>();

      for ( DomainConfiguration<Part> lPartConfig : aPartConfigurations ) {
         Part lPart = new Part();
         lPartConfig.configure( lPart );
         lPartKeys.add( PartBuilder.build( lPart ) );
      }

      return lPartKeys;
   }

}
