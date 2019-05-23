
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;


/**
 * Builds a <code>utl_work_item</code> object
 */
public class WorkItemBuilder implements DomainBuilder<Integer> {

   private static final String TABLE_NAME = "utl_work_item";

   private Date iEndDate;
   private Date iScheduledDate;
   private String iServerId;
   private Date iStartDate;
   private String iKey;
   private String iData;

   // default 0-level utl_work_item_type value
   private String iType = "INVENTORY_SYNC";

   private Integer iWorkItemId;


   /**
    * {@inheritDoc}
    */
   @Override
   public Integer build() {
      int lWorkItemId = ( iWorkItemId != null ) ? iWorkItemId : generateWorkItemId();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "id", lWorkItemId );
      lArgs.add( "type", iType );
      lArgs.add( "key", iKey );
      lArgs.add( "data", iData );
      lArgs.add( "server_id", iServerId );
      lArgs.add( "scheduled_date", iScheduledDate );
      lArgs.add( "start_date", iStartDate );
      lArgs.add( "end_date", iEndDate );
      lArgs.add( "utl_id", Table.Util.getDatabaseId() );

      MxDataAccess.getInstance().executeInsert( TABLE_NAME, lArgs );

      return lWorkItemId;
   }


   /**
    * Set the end date.
    *
    * @param aEndDate
    *
    * @return The builder
    */
   public WorkItemBuilder withEndDate( Date aEndDate ) {
      iEndDate = aEndDate;

      return this;
   }


   /**
    * Set the scheduled date.
    *
    * @param aScheduledDate
    *
    * @return The builder
    */
   public WorkItemBuilder withScheduledDate( Date aScheduledDate ) {
      iScheduledDate = aScheduledDate;

      return this;
   }


   /**
    * Set the server id.
    *
    * @param aServerId
    *
    * @return The builder
    */
   public WorkItemBuilder withServerId( String aServerId ) {
      iServerId = aServerId;

      return this;
   }


   /**
    * Set the start date.
    *
    * @param aStartDate
    *
    * @return The builder
    */
   public WorkItemBuilder withStartDate( Date aStartDate ) {
      iStartDate = aStartDate;

      return this;
   }


   /**
    * Set the work item type.
    *
    * @param aType
    *           work item type that exists within utl_work_item_type
    *
    * @return The builder
    */
   public WorkItemBuilder withType( String aType ) {
      iType = aType;

      return this;
   }


   /**
    * Sets the work item id
    *
    * @param aWorkItemId
    *
    * @return The builder
    */
   public WorkItemBuilder withWorkItemId( int aWorkItemId ) {
      iWorkItemId = aWorkItemId;

      return this;
   }


   /**
    * Sets a new value for the key property.
    *
    * @param aKey
    *
    * @return The builder
    */
   public WorkItemBuilder withKey( String aKey ) {
      iKey = aKey;

      return this;
   }


   /**
    * Sets a new value for the data property.
    *
    * @param aData
    *
    * @return The builder
    */
   public WorkItemBuilder withData( String aData ) {
      iData = aData;

      return this;
   }


   /**
    * Generates a new primary key for the utl_work_item table.
    *
    * @return the utl_work_item pk
    */
   private int generateWorkItemId() {

      try {

         // Get the next id from the sequence
         int lNextId =
               EjbFactory.getInstance().createSequenceGenerator().nextValue( "UTL_WORK_ITEM_ID" );

         return lNextId;
      } catch ( Exception lException ) {
         throw new MxRuntimeException( "Could not generate utl_work_item_id", lException );
      }
   }
}
