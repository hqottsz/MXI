package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.tuple.Pair;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.ShiftKey;


public class Location {

   private String code;
   private LocationKey hubLocation;
   private Boolean isDefaultDock;
   private Boolean isPreventingAutoReservation;
   private Boolean isSupplyLocation;
   private String name;
   private ShiftKey overnightShift;
   private LocationKey parent;
   private LocationKey supplyLocation;
   private TimeZoneKey timeZone;
   private RefLocTypeKey type;
   private Boolean autoIssueBool;
   private Double inboundFlightsQt;

   private List<LocationKey> preferredLocations = new ArrayList<>();
   private List<Pair<AssemblyKey, RefWorkTypeKey>> capabilities = new ArrayList<>();


   public String getCode() {
      return code;
   }


   public void setCode( String code ) {
      this.code = code;
   }


   public LocationKey getHubLocation() {
      return hubLocation;
   }


   public void setHubLocation( LocationKey hubLocation ) {
      this.hubLocation = hubLocation;
   }


   public Optional<Double> getInboundFlightsQt() {
      return Optional.ofNullable( inboundFlightsQt );
   }


   public void setInboundFlightsQt( double inboundFlightsQt ) {
      this.inboundFlightsQt = inboundFlightsQt;
   }


   public Optional<Boolean> getIsDefaultDock() {
      return Optional.ofNullable( isDefaultDock );
   }


   public void setIsDefaultDock( boolean isDefaultDock ) {
      this.isDefaultDock = isDefaultDock;
   }


   public Optional<Boolean> getIsPreventingAutoReservation() {
      return Optional.ofNullable( isPreventingAutoReservation );
   }


   public void setIsPreventingAutoReservation( boolean isPreventingAutoReservation ) {
      this.isPreventingAutoReservation = isPreventingAutoReservation;
   }


   public Optional<Boolean> getIsSupplyLocation() {
      return Optional.ofNullable( isSupplyLocation );
   }


   public void setIsSupplyLocation( boolean isSupplyLocation ) {
      this.isSupplyLocation = isSupplyLocation;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }


   public ShiftKey getOvernightShift() {
      return overnightShift;
   }


   public void setOvernightShift( ShiftKey overnightShift ) {
      this.overnightShift = overnightShift;
   }


   public LocationKey getParent() {
      return parent;
   }


   public void setParent( LocationKey parent ) {
      this.parent = parent;
   }


   public LocationKey getSupplyLocation() {
      return supplyLocation;
   }


   public void setSupplyLocation( LocationKey supplyLocation ) {
      this.supplyLocation = supplyLocation;
   }


   public TimeZoneKey getTimeZone() {
      return timeZone;
   }


   public void setTimeZone( TimeZoneKey timeZone ) {
      this.timeZone = timeZone;
   }


   public RefLocTypeKey getType() {
      return type;
   }


   public void setType( RefLocTypeKey type ) {
      this.type = type;
   }


   public List<LocationKey> getPreferredLocations() {
      return preferredLocations;
   }


   public void addPreferredLocation( LocationKey preferredLocation ) {
      preferredLocations.add( preferredLocation );
   }


   public List<Pair<AssemblyKey, RefWorkTypeKey>> getCapabilities() {
      return capabilities;
   }


   public void addCapability( AssemblyKey assembly, RefWorkTypeKey workType ) {
      capabilities.add( new Pair<AssemblyKey, RefWorkTypeKey>( assembly, workType ) );
   }


   public Optional<Boolean> getAutoIssueBool() {
      return Optional.ofNullable( autoIssueBool );
   }


   public void setAutoIssueBool( Boolean autoIssueBool ) {
      this.autoIssueBool = autoIssueBool;
   }

}
