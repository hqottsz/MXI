
package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.Impact;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefImpactKey;


public class ImpactBuilder {

   public static RefImpactKey build( Impact aImpact ) {
      int lDbId = ( int ) PrimaryKeyService.getInstance().getDatabaseId();
      RefImpactKey lImpactKey = new RefImpactKey( lDbId,
            ( String ) defaultIfNull( aImpact.getImpactCode(), "IMPACT" ) );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lImpactKey, new String[] { "impact_db_id", "impact_cd" } );

      MxDataAccess.getInstance().executeInsert( "ref_impact", lArgs );

      return lImpactKey;

   }

}
