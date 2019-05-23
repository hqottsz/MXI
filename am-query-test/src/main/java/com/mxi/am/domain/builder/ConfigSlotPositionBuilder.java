
package com.mxi.am.domain.builder;

import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;


/**
 * Builds a <code>eqp_assmbl_pos</code> object
 */
public class ConfigSlotPositionBuilder implements DomainBuilder<ConfigSlotPositionKey> {

   private DomainBuilder<ConfigSlotKey> iConfigSlotBuilder;
   private ConfigSlotPositionKey iParentPosition;
   private String iPositionCode;

   // Class variable to hold a unique id in order to generate unique config slot codes.
   private static Integer sConfigSlotCodeUniqueId = 0;


   /**
    * {@inheritDoc}
    */
   @Override
   public ConfigSlotPositionKey build() {

      if ( iConfigSlotBuilder == null ) {
         iConfigSlotBuilder = new ConfigSlotBuilder( generateUniqueConfigSlotCode() );
      }

      ConfigSlotKey lConfigSlot = iConfigSlotBuilder.build();

      int lPosition = EqpAssmblPos.getNextAvailPosId( lConfigSlot );

      ConfigSlotPositionKey lConfigSlotPosition =
            new ConfigSlotPositionKey( lConfigSlot, lPosition );

      EqpAssmblPos lTable = EqpAssmblPos.create( lConfigSlotPosition );

      if ( StringUtils.isBlank( iPositionCode ) ) {
         lTable.setEqpPosCd( lConfigSlotPosition.toString() );
      } else {
         lTable.setEqpPosCd( iPositionCode );
      }
      lTable.setParentBomItemPosition( iParentPosition );

      return lTable.insert();
   }


   public ConfigSlotPositionBuilder withPositionCode( String aPositionCode ) {
      iPositionCode = aPositionCode;

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
   public ConfigSlotPositionBuilder
         withConfigSlot( DomainBuilder<ConfigSlotKey> aConfigSlotBuilder ) {
      iConfigSlotBuilder = aConfigSlotBuilder;

      return this;
   }


   /**
    * Sets the configuration slot builder
    *
    * @param aConfigSlot
    *           the configuration slot
    *
    * @return the builder
    */
   public ConfigSlotPositionBuilder withConfigSlot( ConfigSlotKey aConfigSlot ) {
      withConfigSlot( new TestDataBuilderStub<ConfigSlotKey>( aConfigSlot ) );

      return this;
   }


   /**
    * Sets the parent position of this position.
    *
    * @param aParentPosition
    *           The parent position.
    *
    * @return The builder
    */
   public ConfigSlotPositionBuilder withParentPosition( ConfigSlotPositionKey aParentPosition ) {
      iParentPosition = aParentPosition;

      return this;
   }


   private String generateUniqueConfigSlotCode() {
      return "CS" + sConfigSlotCodeUniqueId++;
   }
}
