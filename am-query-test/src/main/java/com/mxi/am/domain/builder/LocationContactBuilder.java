package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.LocationContactKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.services.location.LocationContactTO;
import com.mxi.mx.core.services.location.LocationService;


/**
 * Builds a <code>inv_loc_contact</code> object
 *
 */
public class LocationContactBuilder {

   private LocationKey iLocationKey;

   private String iContactName;
   private String iPhoneNumber;


   /**
    *
    * build the contact
    *
    * @return the location contact key
    * @throws Exception
    */
   public LocationContactKey build() throws Exception {

      LocationContactKey lLocationContactKey;

      // set up LocationContactTO
      LocationContactTO lTO = new LocationContactTO();
      lTO.setContactName( "Error setting contact name", iContactName );
      lTO.setPhoneNumber( "Error setting phone number", iPhoneNumber );

      lLocationContactKey = LocationService.addContact( iLocationKey, lTO );

      return lLocationContactKey;
   }


   /**
    *
    * set the location for the contact
    *
    * @param aLocationKey
    * @return
    */
   public LocationContactBuilder forLocation( LocationKey aLocationKey ) {
      iLocationKey = aLocationKey;

      return this;
   }


   /**
    *
    * set the contact name
    *
    * @param aContactName
    * @return
    */
   public LocationContactBuilder withContactName( String aContactName ) {
      iContactName = aContactName;

      return this;
   }


   /**
    *
    * set the phone number
    *
    * @param aPhoneNumber
    * @return
    */
   public LocationContactBuilder withPhoneNumber( String aPhoneNumber ) {
      iPhoneNumber = aPhoneNumber;

      return this;
   }

}
