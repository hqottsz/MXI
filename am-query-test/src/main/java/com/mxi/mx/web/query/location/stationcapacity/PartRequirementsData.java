
package com.mxi.mx.web.query.location.stationcapacity;

import java.util.TimeZone;

import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.TaskInstPartKey;


/**
 * DOCUMENT ME!
 *
 * @author dsewell
 */
public class PartRequirementsData {

   /** DOCUMENT ME! */
   public static final TaskInstPartKey AVAILABLE_SCHED_INST_PART_KEY =
         new TaskInstPartKey( 4650, 6, 1, 1 );

   /** DOCUMENT ME! */
   public static final TaskInstPartKey RESERVED_SCHED_INST_PART_KEY =
         new TaskInstPartKey( 4650, 7, 1, 1 );

   /** DOCUMENT ME! */
   public static final TaskInstPartKey ISSUED_SCHED_INST_PART_KEY =
         new TaskInstPartKey( 4650, 8, 1, 1 );


   /**
    * DOCUMENT ME!
    *
    * @author dsewell
    */
   public static class PartRequest {

      /** DOCUMENT ME! */
      public static final PartRequestKey PART_REQ_FOR_ISSUED_PART_TASK =
            new PartRequestKey( 4650, 15 );

      /** DOCUMENT ME! */
      public static final String PART_REQ_FOR_ISSUED_PART_TASK_SDESC = "part request 1";

      /** DOCUMENT ME! */
      public static final String PART_REQ_FOR_ISSUED_PART_TASK_ETA =
            "03-JAN-2006 08:00 " + TimeZone.getDefault().getDisplayName( false, TimeZone.SHORT );
   }
}
