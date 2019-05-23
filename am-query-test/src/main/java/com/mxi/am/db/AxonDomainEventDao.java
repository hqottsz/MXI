package com.mxi.am.db;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Data access object which encapsulates persistence and retrieval operations for axon events.
 */
public final class AxonDomainEventDao {

   public static final String TABLE_NAME = "axon_domain_event_entry";
   public static final String INDEX_COLUMN = "globalindex";
   public static final String AGGREG_IDENTIFIER_COLUMN = "aggregateidentifier";
   public static final String SEQUENCE_COLUMN = "sequencenumber";
   public static final String EVENT_IDENTIFIER_COLUMN = "eventidentifier";
   public static final String PAYLOAD_COLUMN = "payload";
   public static final String PAYLOAD_TYPE_COLUMN = "payloadtype";

   private static final String[] COLUMNS = { INDEX_COLUMN, AGGREG_IDENTIFIER_COLUMN,
         SEQUENCE_COLUMN, EVENT_IDENTIFIER_COLUMN, PAYLOAD_COLUMN, PAYLOAD_TYPE_COLUMN };


   public void purgeAll() {
      MxDataAccess.getInstance().executeDelete( TABLE_NAME, null );
   }


   public QuerySet findAll() {
      return QuerySetFactory.getInstance().executeQuery( COLUMNS, TABLE_NAME, null );
   }


   public QuerySet findByPayLoadType( Class<? extends Object> klass ) {
      final DataSetArgument args = new DataSetArgument();
      args.add( PAYLOAD_TYPE_COLUMN, klass.getName() );
      return QuerySetFactory.getInstance().executeQuery( COLUMNS, TABLE_NAME, args );
   }
}
