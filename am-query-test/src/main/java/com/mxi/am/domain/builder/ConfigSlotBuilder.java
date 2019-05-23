
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefConfigSlotStatusKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;


/**
 * Builds a <code>eqp_assmbl_bom</code> object
 */
public class ConfigSlotBuilder implements DomainBuilder<ConfigSlotKey> {

   private DomainBuilder<AssemblyKey> iAssemblyBuilder = new AssemblyBuilder();
   private RefBOMClassKey iConfigSlotClass = RefBOMClassKey.ROOT;
   private String iConfigSlotCode;

   /** The config slot status defaults to active unless overridden. */
   private RefConfigSlotStatusKey iConfigSlotStatus = RefConfigSlotStatusKey.ACTIVE;

   // The mandatory flag defaults to false to match the default value 0 for
   // eqp_assmbl_bom.mandatory_bool
   private boolean iMandatoryFlag = false;
   private String iName;
   private int iNumberOfPositions = 1;
   private ConfigSlotKey iParentConfigSlot;


   /**
    * Creates a new {@linkplain ConfigSlotBuilder} object.
    *
    * @param aConfigSlotCode
    *           the config slot code
    */
   public ConfigSlotBuilder(String aConfigSlotCode) {
      iConfigSlotCode = aConfigSlotCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ConfigSlotKey build() {
      AssemblyKey lAssembly = iAssemblyBuilder.build();
      EqpAssmblBom lTable;
      if ( RefBOMClassKey.ROOT.equals( iConfigSlotClass ) ) {
         lTable = EqpAssmblBom.create( new ConfigSlotKey( lAssembly, 0 ) );
      } else {
         lTable = new EqpAssmblBom();
      }

      lTable.setParentConfigSlot( iParentConfigSlot );
      lTable.setConfigSlotCode( iConfigSlotCode );
      lTable.setConfigClass( iConfigSlotClass );
      lTable.setConfigSlotStatus( iConfigSlotStatus );
      lTable.setName( iName );
      lTable.setMandatoryFlag( iMandatoryFlag );
      lTable.setNoOfPosition( iNumberOfPositions );

      if ( RefBOMClassKey.ROOT.equals( iConfigSlotClass ) ) {
         return lTable.insert();
      } else {
         return lTable.insert( lAssembly );
      }
   }


   /**
    * Makes the config slot mandatory.
    *
    * @return The builder
    */
   public ConfigSlotBuilder isMandatory() {
      iMandatoryFlag = true;

      return this;
   }


   /**
    * Makes the config slot optional.
    *
    * @return The builder
    */
   public ConfigSlotBuilder isOptional() {
      iMandatoryFlag = false;

      return this;
   }


   /**
    * Sets the assembly
    *
    * @param aAssemblyBuilder
    *           the assembly builder
    *
    * @return the builder
    */
   public ConfigSlotBuilder withRootAssembly( DomainBuilder<AssemblyKey> aAssemblyBuilder ) {
      iAssemblyBuilder = aAssemblyBuilder;

      return this;
   }


   /**
    * Sets the root assembly
    *
    * @param aAssembly
    *           the assembly
    *
    * @return the builder
    */
   public ConfigSlotBuilder withRootAssembly( AssemblyKey aAssembly ) {
      return withRootAssembly( new TestDataBuilderStub<AssemblyKey>( aAssembly ) );
   }


   /**
    * Sets the config slot class
    *
    * @param aConfigSlotClass
    *           the config slot class
    *
    * @return the builder
    */
   public ConfigSlotBuilder withClass( RefBOMClassKey aConfigSlotClass ) {
      iConfigSlotClass = aConfigSlotClass;

      return this;
   }


   /**
    * Sets the name.
    *
    * @param aName
    *           The name
    *
    * @return The builder
    */
   public ConfigSlotBuilder withName( String aName ) {
      iName = aName;

      return this;
   }


   /**
    * Sets the number of postiions.
    *
    * @param aNumberOfPositions
    *
    * @return The builder
    */
   public ConfigSlotBuilder withNumberOfPositions( int aNumberOfPositions ) {
      iNumberOfPositions = aNumberOfPositions;

      return this;
   }


   /**
    * Sets the parent config slot
    *
    * @param aParentConfigSlot
    *           The parent slot
    *
    * @return The builder
    */
   public ConfigSlotBuilder withParent( ConfigSlotKey aParentConfigSlot ) {
      iParentConfigSlot = aParentConfigSlot;
      if ( iParentConfigSlot != null ) {
         return withRootAssembly( iParentConfigSlot.getAssemblyKey() );
      }

      return this;
   }


   /**
    * Sets the config slot status
    *
    * @param aConfigSlotStatus
    *           The status
    *
    * @return The builder
    */
   public ConfigSlotBuilder withStatus( RefConfigSlotStatusKey aConfigSlotStatus ) {
      iConfigSlotStatus = aConfigSlotStatus;

      return this;
   }
}
