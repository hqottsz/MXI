package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Calculated Usage Parameter Domain Object
 *
 */
public class CalculatedUsageParameter {

   private String iCode;
   private String iName;
   private ConfigSlotKey iConfigSlotKey;
   private String iDatabaseCalculation;
   private Integer iPrecisionQt;
   private List<CalculationInput> iCalculationInputs =
         new ArrayList<CalculatedUsageParameter.CalculationInput>();
   private List<PartSpecificConstant> iPartSpecificConstants =
         new ArrayList<CalculatedUsageParameter.PartSpecificConstant>();
   private DataTypeKey iDataType;


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


   public ConfigSlotKey getConfigurationSlot() {
      return iConfigSlotKey;
   }


   public void setConfigurationSlot( ConfigSlotKey aConfigSlotKey ) {
      iConfigSlotKey = aConfigSlotKey;
   }


   public String getDatabaseCalculation() {
      return iDatabaseCalculation;
   }


   public void setDatabaseCalculation( String aDatabaseCalculation ) {
      iDatabaseCalculation = aDatabaseCalculation;
   }


   public Integer getPrecisionQt() {
      return iPrecisionQt;
   }


   public void setPrecisionQt( Integer aPrecisionQt ) {
      iPrecisionQt = aPrecisionQt;
   }


   public void addParameter( DataTypeKey aUsageParameter ) {
      iCalculationInputs.add( new Parameter( aUsageParameter ) );
   }


   public void addConstant( String aName, BigDecimal aValue ) {
      iCalculationInputs.add( new Constant( aName, aValue ) );
   }


   public List<CalculationInput> getCalculationInputs() {
      return iCalculationInputs;
   }


   public void addPartSpecificConstant( String aConfigurationSlotName, String aPartGroupName,
         PartNoKey aPartNumber, String aName, BigDecimal aValue ) {

      iPartSpecificConstants.add( new PartSpecificConstant( aConfigurationSlotName, aPartGroupName,
            aPartNumber, aName, aValue ) );

   }


   public List<PartSpecificConstant> getPartSpecificConstants() {
      return iPartSpecificConstants;
   }


   public abstract class CalculationInput {
   }

   public class Parameter extends CalculationInput {

      public DataTypeKey iDataType;


      public Parameter(DataTypeKey aUsageParameter) {
         iDataType = aUsageParameter;
      }
   }

   public class Constant extends CalculationInput {

      public String iName;
      public BigDecimal iValue;


      public Constant(String aName, BigDecimal aValue) {
         iName = aName;
         iValue = aValue;
      }
   }

   public class PartSpecificConstant extends Constant {

      // May be different than the config slot of the Calculated Usage Parameter, but must be found
      // in the tree under it.
      public String iConfigurationSlotCode;

      public String iPartGroupCode;
      public PartNoKey iPartNumber;


      public PartSpecificConstant(String aConfigurationSlotCode, String aPartGroupCode,
            PartNoKey aPartNumber, String aName, BigDecimal aValue) {
         super( aName, aValue );
         iConfigurationSlotCode = aConfigurationSlotCode;
         iPartGroupCode = aPartGroupCode;
         iPartNumber = aPartNumber;
      }
   }


   public void setDataType( DataTypeKey aDataType ) {
      iDataType = aDataType;
   }


   public DataTypeKey getDataType() {
      return iDataType;
   }

}
