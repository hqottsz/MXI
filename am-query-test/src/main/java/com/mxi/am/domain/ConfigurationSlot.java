package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefBOMClassKey;


public class ConfigurationSlot {

   private String iName;
   private String iCode;
   private String iStatus;
   private boolean iMandatoryFlag;
   private AssemblyKey iRootAssembly;
   private List<DomainConfiguration<PartGroup>> iPartGroupConfigurations =
         new ArrayList<DomainConfiguration<PartGroup>>();
   private List<DomainConfiguration<ConfigurationSlot>> iSubConfigurationSlotConfigurations =
         new ArrayList<DomainConfiguration<ConfigurationSlot>>();
   private List<String> iPositionCodes = new ArrayList<String>();
   private RefBOMClassKey iClass;
   private ConfigSlotKey iParentConfigSlot;
   private List<DataTypeKey> iUsageParameters = new ArrayList<DataTypeKey>();
   private List<DomainConfiguration<CalculatedUsageParameter>> iCalculatedUsageParameters =
         new ArrayList<Domain.DomainConfiguration<CalculatedUsageParameter>>();


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public String getStatus() {
      return iStatus;
   }


   public void setStatus( String aStatus ) {
      iStatus = aStatus;
   }


   public boolean isMandatoryFlag() {
      return iMandatoryFlag;
   }


   public void setMandatoryFlag( boolean aMandatoryFlag ) {
      iMandatoryFlag = aMandatoryFlag;
   }


   public AssemblyKey getRootAssembly() {
      return iRootAssembly;
   }


   public void setRootAssembly( AssemblyKey aRootAssembly ) {
      iRootAssembly = aRootAssembly;
   }


   public void addConfigurationSlot( DomainConfiguration<ConfigurationSlot> aDomainConfiguration ) {
      iSubConfigurationSlotConfigurations.add( aDomainConfiguration );
   }


   public List<DomainConfiguration<ConfigurationSlot>> getConfigurationSlotConfigurations() {
      return iSubConfigurationSlotConfigurations;
   }


   public void addPosition( String aPositionCode ) {
      iPositionCodes.add( aPositionCode );
   }


   public List<String> getPositionCodes() {
      return iPositionCodes;
   }


   public void addPartGroup( DomainConfiguration<PartGroup> aDomainConfiguration ) {
      iPartGroupConfigurations.add( aDomainConfiguration );
   }


   public List<DomainConfiguration<PartGroup>> getPartGroupConfigurations() {
      return iPartGroupConfigurations;
   }


   public RefBOMClassKey getConfigurationSlotClass() {
      return iClass;
   }


   public void setConfigurationSlotClass( RefBOMClassKey aClass ) {
      iClass = aClass;
   }


   public ConfigSlotKey getParentConfigurationSlot() {
      return iParentConfigSlot;
   }


   public void setParentConfigurationSlot( ConfigSlotKey aParentConfigSlot ) {
      iParentConfigSlot = aParentConfigSlot;
   }


   public List<DataTypeKey> getUsageParameters() {
      return iUsageParameters;
   }


   public void addUsageParameter( DataTypeKey aUsageParameter ) {
      iUsageParameters.add( aUsageParameter );
   }


   public List<DomainConfiguration<CalculatedUsageParameter>>
         getCalculatedUsageParameterConfigurations() {
      return iCalculatedUsageParameters;
   }


   public void addCalculatedUsageParameter(
         DomainConfiguration<CalculatedUsageParameter> aCalculatedUsageParameterConfiguration ) {
      iCalculatedUsageParameters.add( aCalculatedUsageParameterConfiguration );
   }

}
