package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefDataSourceKey;


/**
 * Domain entity for Usage Definition
 *
 */
public class UsageDefinition {

   private String iName;
   private RefDataSourceKey iDataSource;
   private AssemblyKey iAssembly;
   private List<DataTypeKey> iUsageParameters = new ArrayList<>();
   private List<DomainConfiguration<AccumulatedParameter>> iAccumulatedParameterConfigurations =
         new ArrayList<>();


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public RefDataSourceKey getDataSouce() {
      return iDataSource;
   }


   public void setDataSource( RefDataSourceKey aType ) {
      iDataSource = aType;
   }


   public AssemblyKey getAssembly() {
      return iAssembly;
   }


   public void setAssembly( AssemblyKey aAssembly ) {
      iAssembly = aAssembly;
   }


   public List<DataTypeKey> getUsageParameters() {
      return iUsageParameters;
   }


   public void addUsageParameter( DataTypeKey aUsageParameter ) {
      iUsageParameters.add( aUsageParameter );
   }


   public List<DomainConfiguration<AccumulatedParameter>> getAccumulatedParameterConfiguration() {
      return iAccumulatedParameterConfigurations;
   }


   public void addAccumulatedParameterConfiguration(
         DomainConfiguration<AccumulatedParameter> aAccumulatedParameterConfiguration ) {
      iAccumulatedParameterConfigurations.add( aAccumulatedParameterConfiguration );
   }

}
