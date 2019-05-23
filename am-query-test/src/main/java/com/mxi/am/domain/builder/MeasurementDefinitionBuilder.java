package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.concurrent.atomic.AtomicInteger;

import com.mxi.am.domain.MeasurementDefinition;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Builds an <code>mim_data_type</code> measurement object
 *
 */
public class MeasurementDefinitionBuilder {

   private static AtomicInteger iMeasurementCode = new AtomicInteger( 1 );
   private static final Integer DEFAULT_PRECISION = 2;


   public static DataTypeKey build( MeasurementDefinition aMeasurementDefinition ) {

      String lMeasurementCode = ( String ) defaultIfNull( aMeasurementDefinition.getCode(),
            String.valueOf( iMeasurementCode.getAndIncrement() ) );

      Integer lMeasurementPrecision =
            ( Integer ) defaultIfNull( aMeasurementDefinition.getPrecision(), DEFAULT_PRECISION );

      RefDomainTypeKey lMeasurementDomainType =
            ( RefDomainTypeKey ) defaultIfNull( aMeasurementDefinition.getDomainType(),
                  RefDomainTypeKey.CHARACTER );

      return MimDataType.create( lMeasurementCode, aMeasurementDefinition.getName(),
            lMeasurementDomainType, aMeasurementDefinition.getEngineeringUnit(),
            lMeasurementPrecision, aMeasurementDefinition.getDescription() );

   }
}
