
package com.mxi.am.domain.builder;

import com.mxi.am.domain.Operator;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.table.org.OrgCarrierTable;


/**
 * Builds a <code>org_carrier</code> object
 */
public class CarrierBuilder implements DomainBuilder<CarrierKey> {

   private String iCarrierCode = null;
   private String iIATACode = null;
   private String iICAOCode = null;
   private OrgKey iOrgKey = null;


   /**
    * Creates a new {@linkplain CarrierBuilder} object.
    */
   public CarrierBuilder() {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public CarrierKey build() {

      OrgCarrierTable lTable = OrgCarrierTable.create();

      lTable.setOrganization( iOrgKey );
      lTable.setCode( iCarrierCode );
      lTable.setIATACode( iIATACode );
      lTable.setICAOCode( iICAOCode );

      return lTable.insert();
   }


   /**
    * Sets the carrier code.
    *
    * @param aCode
    *           The carrier code
    *
    * @return The builder
    */
   public CarrierBuilder withCode( String aCode ) {
      iCarrierCode = aCode;

      return this;
   }


   /**
    * Sets the IATA code.
    *
    * @param aIATACode
    *           The IATA code
    *
    * @return The builder
    */
   public CarrierBuilder withIATACode( String aIATACode ) {
      iIATACode = aIATACode;

      return this;
   }


   /**
    * Sets the ICAO code.
    *
    * @param aICAOCode
    *           The ICAO code
    *
    * @return The builder
    */
   public CarrierBuilder withICAOCode( String aICAOCode ) {
      iICAOCode = aICAOCode;

      return this;
   }


   /**
    * Sets the Org key.
    *
    * @param aOrgKey
    *           The organization key
    *
    * @return The builder
    */
   public CarrierBuilder withOrgKey( OrgKey aOrgKey ) {
      iOrgKey = aOrgKey;

      return this;
   }


   /*
    * Builds an operator
    */
   public static CarrierKey build( Operator aOperator ) {

      CarrierBuilder lBuilder = new CarrierBuilder();
      lBuilder.withCode( aOperator.getCarrierCode() );
      lBuilder.withIATACode( aOperator.getIATACode() );
      lBuilder.withICAOCode( aOperator.getICAOCode() );
      lBuilder.withOrgKey( aOperator.getOrgKey() );

      return lBuilder.build();
   }

}
