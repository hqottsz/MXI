package com.mxi.am.domain.reader;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Reader for retrieving Sign Off Requirement information.
 */
public class SignOffRequirementReader {

   /**
    * Return all the sign off requirements of the provided workpackage.
    */
   public static List<SchedWPSignReqKey> read( TaskKey workpackage ) {
      if ( workpackage == null ) {
         throw new RuntimeException( "workpackage must be provided" );
      }

      List<SchedWPSignReqKey> signOffReqs = new ArrayList<>();

      DataSetArgument args = new DataSetArgument();
      args.add( workpackage, "sched_db_id", "sched_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_wp_sign_req", args );

      while ( qs.next() ) {
         signOffReqs.add( qs.getKey( SchedWPSignReqKey.class, "sign_req_db_id", "sign_req_id" ) );
      }

      return signOffReqs;
   }

}
