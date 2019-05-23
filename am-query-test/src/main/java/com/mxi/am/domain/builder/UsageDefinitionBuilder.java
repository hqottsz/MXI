package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefDataSourceKey.MXFL;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.AccumulatedParameter;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.table.eqp.EqpDataSource;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;


/**
 * Builder class for a Usage Definition
 *
 */
public class UsageDefinitionBuilder {

   public static UsageDefinitionKey build( UsageDefinition aUsageDefinition ) {

      AssemblyKey lAssembly = aUsageDefinition.getAssembly();
      if ( lAssembly == null ) {
         throw new RuntimeException( "Assembly key is mandatory for building a Usage Definition." );
      }

      // The type is mandatory so use a default if one is not provided.
      RefDataSourceKey lType =
            ( RefDataSourceKey ) defaultIfNull( aUsageDefinition.getDataSouce(), MXFL );

      UsageDefinitionKey lUsageDefinitionKey =
            new UsageDefinitionKey( aUsageDefinition.getAssembly(), lType );

      EqpDataSource.create( lUsageDefinitionKey, aUsageDefinition.getName() );

      // Add regular usage parameters.
      for ( DataTypeKey lUsageParameterKey : aUsageDefinition.getUsageParameters() ) {
         EqpDataSourceSpec.create( lUsageDefinitionKey, lUsageParameterKey );
      }

      // Add accumulated parameters
      for ( DomainConfiguration<AccumulatedParameter> lAccumulatedParameterConfiguration : aUsageDefinition
            .getAccumulatedParameterConfiguration() ) {
         AccumulatedParameter lAccumulatedParameter = new AccumulatedParameter();
         lAccumulatedParameterConfiguration.configure( lAccumulatedParameter );
         if ( lAccumulatedParameter.getUsageDefinition() != null ) {
            throw new RuntimeException(
                  "Accumulated usage parameter usage definition can only be set from the within the usage definition Builder" );

         }
         lAccumulatedParameter.setUsageDefinition( lUsageDefinitionKey );
         AccumulatedParameterBuilder.build( lAccumulatedParameter );
      }

      return lUsageDefinitionKey;
   }
}
