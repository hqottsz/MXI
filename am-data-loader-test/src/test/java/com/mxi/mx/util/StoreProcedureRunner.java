package com.mxi.mx.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;


/**
 * This is central place to hold all store AL store procedure runner.
 *
 */
public enum StoreProcedureRunner {

   Task {

      @Override
      public int runTasksParall( Connection aConnection, boolean blnOnlyValidation,
            boolean allornone, int aParallelismDegree, int aChunkSize, String aDataCD,
            int aGatherStats, int aLogging, int aAutocreateMissingTask ) {

         int rtValue = 0;
         ValidationAndImport Lvalidationandimport = new ValidationAndImport() {

            @Override
            public int runValidation( boolean allornone ) {
               int lReturn = 0;
               CallableStatement lPrepareCallInventory = null;

               try {
                  lPrepareCallInventory = aConnection.prepareCall(
                        "BEGIN    mx_al_ctrller_pkg.execute_task_validation(ain_gather_stats_bool => ?, "
                              + " aon_retcode => ?," + " aov_retmsg => ?); END;" );

                  lPrepareCallInventory.setInt( 1, aGatherStats );
                  lPrepareCallInventory.registerOutParameter( 2, Types.INTEGER );
                  lPrepareCallInventory.registerOutParameter( 3, Types.VARCHAR );
                  lPrepareCallInventory.executeUpdate();
                  aConnection.commit();
                  lReturn = lPrepareCallInventory.getInt( 2 );

               } catch ( SQLException e ) {
                  e.printStackTrace();
               }
               return lReturn;

            }


            @Override
            public int runImport( boolean allornone ) {
               int lReturn = 0;
               CallableStatement lPrepareCallInventory = null;

               try {
                  lPrepareCallInventory = aConnection.prepareCall(
                        "BEGIN mx_al_ctrller_pkg.execute_task_import( ain_parallelism_degree => ?, ain_chunk_size => ?"
                              + ", ain_gather_stats_bool => ?, ain_logging_bool => ?, ain_auto_create_bool => ?,aon_retcode => ?, aov_retmsg => ?); END;" );

                  lPrepareCallInventory.setInt( 1, aParallelismDegree );
                  lPrepareCallInventory.setInt( 2, aChunkSize );
                  lPrepareCallInventory.setInt( 3, aGatherStats );
                  lPrepareCallInventory.setInt( 4, aLogging );
                  lPrepareCallInventory.setInt( 5, aAutocreateMissingTask );
                  lPrepareCallInventory.registerOutParameter( 6, Types.INTEGER );
                  lPrepareCallInventory.registerOutParameter( 7, Types.VARCHAR );

                  lPrepareCallInventory.execute();
                  aConnection.commit();
                  lReturn = lPrepareCallInventory.getInt( 6 );

               } catch ( SQLException e ) {
                  e.printStackTrace();
               }
               return lReturn;
            }

         };

         rtValue = blnOnlyValidation ? Lvalidationandimport.runValidation( allornone )
               : Lvalidationandimport.runImport( allornone );

         return rtValue;

      }


      @Override
      public int runInventoryValidationAndImport( Connection aConnection,
            boolean aBlnOnlyValidation, boolean aAllornone, String aAaiv_complete_assy_bool,
            int aParallelismDegree, int aChunkSize, int aGatherStats ) {
         // TODO Auto-generated method stub
         return 0;
      }

   },

   Inventory {

      @Override
      public int runInventoryValidationAndImport( Connection aConnection, boolean blnOnlyValidation,
            boolean allornone, String aaiv_complete_assy_bool, int aParallelismDegree,
            int aChunkSize, int aGatherStats ) {

         int rtValue = 0;
         ValidationAndImport Lvalidationandimport = new ValidationAndImport() {

            @Override
            public int runValidation( boolean allornone ) {
               int lReturn = 0;
               CallableStatement lPrepareCallInventory = null;

               try {
                  lPrepareCallInventory = aConnection.prepareCall(
                        "BEGIN mx_al_ctrller_pkg.execute_inv_validation(aiv_complete_assy_bool => ?,"
                              + " ain_gather_stats_bool => ?," + " aon_retcode =>?,"
                              + " aov_retmsg => :aov_retmsg); END;" );

                  lPrepareCallInventory.setString( 1, aaiv_complete_assy_bool );
                  lPrepareCallInventory.setInt( 2, aGatherStats );
                  lPrepareCallInventory.registerOutParameter( 3, Types.INTEGER );
                  lPrepareCallInventory.registerOutParameter( 4, Types.VARCHAR );
                  lPrepareCallInventory.executeUpdate();
                  aConnection.commit();
                  lReturn = lPrepareCallInventory.getInt( 3 );

               } catch ( SQLException e ) {
                  e.printStackTrace();
               }
               return lReturn;

            }


            @Override
            public int runImport( boolean allornone ) {
               int lReturn = 0;
               CallableStatement lPrepareCallInventory = null;

               try {
                  lPrepareCallInventory = aConnection.prepareCall(
                        "BEGIN mx_al_ctrller_pkg.execute_inv_import(aiv_complete_assy_bool => ?,"
                              + " ain_parallelism_degree =>?," + " ain_chunk_size => ?,"
                              + " ain_gather_stats_bool =>?," + " aon_retcode => :aon_retcode,"
                              + " aov_retmsg => :aov_retmsg); END;" );

                  lPrepareCallInventory.setString( 1, aaiv_complete_assy_bool );
                  lPrepareCallInventory.setInt( 2, aParallelismDegree );
                  lPrepareCallInventory.setInt( 3, aChunkSize );
                  lPrepareCallInventory.setInt( 4, aGatherStats );
                  lPrepareCallInventory.registerOutParameter( 5, Types.INTEGER );
                  lPrepareCallInventory.registerOutParameter( 6, Types.VARCHAR );

                  lPrepareCallInventory.execute();
                  aConnection.commit();
                  lReturn = lPrepareCallInventory.getInt( 5 );

               } catch ( SQLException e ) {
                  e.printStackTrace();
               }
               return lReturn;
            }

         };

         rtValue = blnOnlyValidation ? Lvalidationandimport.runValidation( allornone )
               : Lvalidationandimport.runImport( allornone );

         return rtValue;

      }


      @Override
      public int runTasksParall( Connection aConnection, boolean aBlnOnlyValidation,
            boolean aAllornone, int aParallelismDegree, int aChunkSize, String aDataCD,
            int aGatherStats, int aLogging, int aAutocreateMissingTask ) {
         // TODO Auto-generated method stub
         return 0;
      }

   };

   public abstract int runTasksParall( Connection aConnection, boolean blnOnlyValidation,
         boolean allornone, int aParallelismDegree, int aChunkSize, String aDataCD,
         int aGatherStats, int aLogging, int aAutocreateMissingTask );


   public abstract int runInventoryValidationAndImport( Connection aConnection,
         boolean blnOnlyValidation, boolean allornone, String aaiv_complete_assy_bool,
         int aParallelismDegree, int aChunkSize, int aGatherStats );

}
