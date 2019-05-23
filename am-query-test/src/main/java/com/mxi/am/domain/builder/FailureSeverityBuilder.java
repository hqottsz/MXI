package com.mxi.am.domain.builder;

import org.apache.commons.lang.StringUtils;

import com.mxi.am.domain.FailureSeverity;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefFailureSeverityKey;


public class FailureSeverityBuilder {

   public static RefFailureSeverityKey build( FailureSeverity failureSeverity ) {

      DataSetArgument lArgs = failureSeverity.getId().getPKWhereArg();
      if ( !StringUtils.isEmpty( failureSeverity.getName() ) ) {
         lArgs.add( "desc_sdesc", failureSeverity.getName() );
      }

      MxDataAccess.getInstance().executeInsert( "ref_fail_sev", lArgs );
      return failureSeverity.getId();
   }
}
