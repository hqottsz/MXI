package com.mxi.am.domain;

import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.RefEngUnitKey;


/**
 * Domain class for Measurement definition entity
 *
 */
public class MeasurementDefinition {

   private String iCode;
   private String iName;
   private RefEngUnitKey iEngineeringUnit;
   private Integer iPrecision;
   private RefDomainTypeKey iDomainType;
   private String iDescription;


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public RefEngUnitKey getEngineeringUnit() {
      return iEngineeringUnit;
   }


   public void setEngineeringUnit( RefEngUnitKey aEngineeringUnit ) {
      iEngineeringUnit = aEngineeringUnit;
   }


   public Integer getPrecision() {
      return iPrecision;
   }


   public void setPrecision( Integer aPrecision ) {
      iPrecision = aPrecision;
   }


   public RefDomainTypeKey getDomainType() {
      return iDomainType;
   }


   public void setDomainType( RefDomainTypeKey aDomainType ) {
      iDomainType = aDomainType;
   }


   public String getDescription() {
      return iDescription;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }

}
