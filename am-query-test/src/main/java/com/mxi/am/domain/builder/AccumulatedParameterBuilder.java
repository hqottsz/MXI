package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.AccumulatedParameter;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.mim.MimPartNumData;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNo;


/**
 * Builds an accumulated parameter object
 *
 */
public class AccumulatedParameterBuilder {

   public static DataTypeKey build( AccumulatedParameter aAccumulatedParameter ) {
      String lPartNoCode =
            EqpPartNo.findByPrimaryKey( aAccumulatedParameter.getPartNoKey() ).getOEMPartNo();
      MimDataType lAggregatedDataType =
            MimDataType.findByPrimaryKey( aAccumulatedParameter.getAggregatedDataType() );

      String lAccumulatedParameterCode = ( String ) defaultIfNull( aAccumulatedParameter.getCode(),
            "ACC" + aAccumulatedParameter.getAggregatedDataType()
                  + aAccumulatedParameter.getPartNoKey() );
      DataTypeKey lDataType = MimDataType.create( lAccumulatedParameterCode,
            lAggregatedDataType.getDataTypeSdesc() + " at " + lPartNoCode,
            RefDomainTypeKey.USAGE_PARM, lAggregatedDataType.getEngUnit(),
            MimDataType.getEntryPrecQt( aAccumulatedParameter.getAggregatedDataType() ), "" );
      MimPartNumDataKey lMimPartNumDataKey = new MimPartNumDataKey(
            new ConfigSlotKey( aAccumulatedParameter.getUsageDefinition().getAssemblyKey(), 0 ),
            lDataType );
      MimPartNumData.create( lMimPartNumDataKey );
      MimPartNumData lMimPartNumData = MimPartNumData.findByPrimaryKey( lMimPartNumDataKey );
      lMimPartNumData.setAggregatedDataType(
            aAccumulatedParameter.getAggregatedDataType().getDbId(),
            aAccumulatedParameter.getAggregatedDataType().getId() );
      lMimPartNumData.setAssemblyPartNo( aAccumulatedParameter.getPartNoKey().getDbId(),
            aAccumulatedParameter.getPartNoKey().getId() );
      lMimPartNumData.update();
      EqpDataSourceSpec.create( aAccumulatedParameter.getUsageDefinition(), lDataType );
      return lDataType;
   }

}
