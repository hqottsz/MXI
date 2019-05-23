package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;


/**
 * Builds an <code>eqp_assmbl_bom</code>
 */
public class PartGroupDomainBuilder implements DomainBuilder<PartGroupKey> {

   private String iApplicabilityRange;
   private DomainBuilder<ConfigSlotKey> iConfigSlotBuilder = new ConfigSlotBuilder( "TEST" );
   private RefInvClassKey iInventoryClass;
   private Map<PartNoKey, String> iPartApplicabilityRanges = new HashMap<PartNoKey, String>();
   private String iPartGroupCode;
   private String iPartGroupName;
   private List<DomainBuilder<PartNoKey>> iPartNoBuilders =
         new ArrayList<DomainBuilder<PartNoKey>>();


   /**
    * Creates a new {@linkplain PartGroupDomainBuilder} object.
    *
    * @param aPartGroupCode
    *           the part group code
    */
   public PartGroupDomainBuilder(String aPartGroupCode) {
      iPartGroupCode = aPartGroupCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PartGroupKey build() {

      // EqpBomPart.setBomPartCd() requires global LOGIC parameter SPEC2000_UPPERCASE_BOM_PART_CD.
      if ( GlobalParameters.getInstance( "LOGIC" )
            .getBoolean( "SPEC2000_UPPERCASE_BOM_PART_CD" ) == null ) {

         // Throw a more helpful exception.
         throw new RuntimeException( "PartGroupBuilder requires global LOGIC parameter "
               + "'SPEC2000_UPPERCASE_BOM_PART_CD' to be set!" );
      }

      EqpBomPart lTable = EqpBomPart.create();
      lTable.setBomPartCd( iPartGroupCode );
      lTable.setBomPartName( iPartGroupName );
      lTable.setBomItem( iConfigSlotBuilder.build() );
      lTable.setApplEffLdesc( iApplicabilityRange );
      lTable.setInvClass( iInventoryClass );

      PartGroupKey lPartGroup = lTable.insert();

      for ( DomainBuilder<PartNoKey> lPartNoBuilder : iPartNoBuilders ) {
         PartNoKey lPartNo = lPartNoBuilder.build();
         EqpPartBaselineKey lAssignedPart = new EqpPartBaselineKey( lPartGroup, lPartNo );

         EqpPartBaselineTable lAssignedPartTable = EqpPartBaselineTable.create( lAssignedPart );
         lAssignedPartTable.setApprovedBool( true );

         String lApplRange = iPartApplicabilityRanges.get( lPartNo );
         if ( lApplRange != null ) {
            lAssignedPartTable.setApplEffLdesc( lApplRange );
         }

         lAssignedPartTable.setStandardBool( lPartNoBuilder.equals( iPartNoBuilders.get( 0 ) ) );

         lAssignedPartTable.insert();
      }

      return lPartGroup;
   }


   /**
    *
    * Makes this a part group for tools.
    *
    * @return the builder
    */
   public PartGroupDomainBuilder isToolGroup() {
      AssemblyKey lTSEAssembly = new AssemblyBuilder().withClass( RefAssmblClassKey.TSE ).build();
      iConfigSlotBuilder = new ConfigSlotBuilder( "TSE" ).withRootAssembly( lTSEAssembly );

      return this;
   }


   /**
    * Sets the applicability range expression.
    *
    * @param aApplicabilityRange
    *           the applicability range
    *
    * @return the builder
    */
   public PartGroupDomainBuilder withApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;

      return this;
   }


   /**
    * Sets the configuration slot builder
    *
    * @param aConfigSlotBuilder
    *           the configuration slot builder
    *
    * @return the builder
    */
   public PartGroupDomainBuilder withConfigSlot( DomainBuilder<ConfigSlotKey> aConfigSlotBuilder ) {
      iConfigSlotBuilder = aConfigSlotBuilder;

      return this;
   }


   /**
    * Sets the configuration slot
    *
    * @param aConfigSlot
    *           the configuration slot
    *
    * @return the builder
    */
   public PartGroupDomainBuilder withConfigSlot( ConfigSlotKey aConfigSlot ) {
      return withConfigSlot( new TestDataBuilderStub<ConfigSlotKey>( aConfigSlot ) );
   }


   /**
    * Sets the inventory class of the part group.
    *
    * @param aInventoryClass
    *           The inventory class
    *
    * @return The builder
    */
   public PartGroupDomainBuilder withInventoryClass( RefInvClassKey aInventoryClass ) {
      iInventoryClass = aInventoryClass;

      return this;
   }


   /**
    * Adds a part applicability range to the part group for the provided part.
    *
    * @param aPartNo
    * @param aApplicabilityRange
    *
    * @return The builder
    */
   public PartGroupDomainBuilder withPartApplicabilityRange( PartNoKey aPartNo,
         String aApplicabilityRange ) {
      iPartApplicabilityRanges.put( aPartNo, aApplicabilityRange );

      return this;
   }


   /**
    * Sets the part group name.
    *
    * @param aPartGroupName
    *           the part group name
    *
    * @return the builder
    */
   public PartGroupDomainBuilder withPartGroupName( String aPartGroupName ) {
      iPartGroupName = aPartGroupName;

      return this;
   }


   /**
    * Adds the part to the part group
    *
    * @param aPartNoBuilder
    *           the part no builder
    *
    * @return the builder
    */
   public PartGroupDomainBuilder withPartNo( DomainBuilder<PartNoKey> aPartNoBuilder ) {
      iPartNoBuilders.add( aPartNoBuilder );

      return this;
   }


   /**
    * Adds the part to the part group
    *
    * @param aPartNo
    *           the part
    *
    * @return the builder
    */
   public PartGroupDomainBuilder withPartNo( PartNoKey aPartNo ) {
      return withPartNo( new TestDataBuilderStub<PartNoKey>( aPartNo ) );
   }
}
