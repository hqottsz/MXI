
package com.mxi.mx.web.query.location.stationcapacity;

import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * DOCUMENT ME!
 *
 * @author dsewell
 */
public class ToolRequirementsData extends StationCapacityData {

   /**
    * Creates a new ToolRequirementsData object.
    */
   public ToolRequirementsData() {
      super();
   }


   /**
    * DOCUMENT ME!
    *
    * @author dsewell
    */
   public static class Task {

      /**
       * DOCUMENT ME!
       *
       * @author dsewell
       */
      public static class Tool {

         /** DOCUMENT ME! */
         public static final TaskKey TASK_2_WITH_UNAVAILABLE_TOOL_ON_DAY_1 =
               new TaskKey( 4650, 15 );
      }
   }

   /**
    * DOCUMENT ME!
    *
    * @author dsewell
    */
   public static class Tool {

      /** DOCUMENT ME! */
      public static final PartGroupKey TOOL_PART_GROUP_3 = new PartGroupKey( 4650, 6 );

      /** DOCUMENT ME! */
      public static final String TOOL_PART_GROUP_3_CODE = "tool 3 code";

      /** DOCUMENT ME! */
      public static final String TOOL_PART_GROUP_3_NAME = "tool 3 name";

      /** DOCUMENT ME! */
      public static final PartNoKey TOOL_PART_NO_3 = new PartNoKey( 4650, 6 );

      /** DOCUMENT ME! */
      public static final EqpPartBaselineKey TOOL_PART_BASELINE_1 =
            new EqpPartBaselineKey( TOOL_PART_GROUP_3, TOOL_PART_NO_3 );
   }
}
