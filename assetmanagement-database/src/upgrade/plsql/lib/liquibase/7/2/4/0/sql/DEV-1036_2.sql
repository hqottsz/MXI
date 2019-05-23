--liquibase formatted sql


--changeSet DEV-1036_2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove all Consignment Orders with Parts of financial type EXPENSE
DECLARE
    
    ln_PoInvoiceDbId po_invoice.po_invoice_db_id%TYPE;
    ln_PoInvoiceId po_invoice.po_invoice_id%TYPE;
    
    ln_PoDbId po_line.po_db_id%TYPE;
    ln_PoId po_line.po_id%TYPE;
    ln_PoLineId po_line.po_line_id%TYPE;
        
    ln_PreviousPoDbId po_line.po_db_id%TYPE;
    ln_PreviousPoId po_line.po_id%TYPE;
    
    ln_ReqPartDbId req_part.req_part_db_id%TYPE;
    ln_ReqPartId req_part.req_part_id%TYPE;
    
    lb_PartLineCount number;

    CURSOR lcur_ConsignOrderPartLine IS
             
      -- Retrieve all part lines where part of financial type EXPENSE
      SELECT 
           po_line.po_db_id,
           po_line.po_id,
           po_line.po_line_id
      FROM
           po_header
           INNER JOIN po_line ON
                 po_line.po_db_id = po_header.po_db_id AND
                 po_line.po_id    = po_header.po_id
           INNER JOIN eqp_part_no ON
                 eqp_part_no.part_no_db_id = po_line.part_no_db_id AND
                 eqp_part_no.part_no_id    = po_line.part_no_id
           INNER JOIN ref_financial_class ON
                 ref_financial_class.financial_class_db_id = eqp_part_no.financial_class_db_id AND
                 ref_financial_class.financial_class_cd    = eqp_part_no.financial_class_cd
      WHERE
           po_header.po_type_cd = 'CONSIGN'
           AND
           ref_financial_class.finance_type_cd = 'EXPENSE'
      ORDER BY 
           po_db_id,
           po_id;
           
    lrec_ConsignOrderPartLine lcur_ConsignOrderPartLine%ROWTYPE;     
    
    
    -- Retrieve all invoices for the current consignment order
    CURSOR lcur_Invoice (
      an_PoDbId         po_line.po_db_id%TYPE,
      an_PoId           po_line.po_id%TYPE,
      an_PoLineId       po_line.po_line_id%TYPE
    ) IS
      SELECT DISTINCT
          po_invoice_line_map.po_invoice_db_id,
          po_invoice_line_map.po_invoice_id
      FROM
          po_invoice_line_map
          INNER JOIN po_line ON
              po_line.po_db_id = po_invoice_line_map.po_db_id AND
              po_line.po_id    = po_invoice_line_map.po_id
      WHERE
          po_invoice_line_map.po_db_id   = an_PoDbId AND
          po_invoice_line_map.po_id      = an_PoId AND
          po_invoice_line_map.po_line_id = an_PoLineId;
    
    lrec_Invoice lcur_Invoice%ROWTYPE;  
    
    
    -- Retrieve all part requests for the current consignment order
    CURSOR lcur_PartRequest (
      an_PoDbId         po_line.po_db_id%TYPE,
      an_PoId           po_line.po_id%TYPE,
      an_PoLineId       po_line.po_line_id%TYPE
    ) IS
      SELECT DISTINCT
          req_part.req_part_db_id,
          req_part.req_part_id
      FROM
          req_part          
      WHERE
          req_part.po_db_id   = an_PoDbId AND
          req_part.po_id      = an_PoId AND
          req_part.po_line_id = an_PoLineId;
    
    lrec_PartRequest lcur_PartRequest%ROWTYPE;     
     
BEGIN

    -- Loop through all consignment order part lines
    FOR lrec_ConsignOrderPartLine IN lcur_ConsignOrderPartLine LOOP       
    
        ln_PoDbId     :=  lrec_ConsignOrderPartLine.po_db_id;
        ln_PoId       :=  lrec_ConsignOrderPartLine.po_id; 
        ln_PoLineId   :=  lrec_ConsignOrderPartLine.po_line_id;  
        
        
        /****************************         
        /* Loop through all invoices
        /****************************/
        FOR lrec_Invoice IN lcur_Invoice( ln_PoDbId, ln_PoId, ln_PoLineId ) LOOP
           
           ln_PoInvoiceDbId :=  lrec_Invoice.po_invoice_db_id;
           ln_PoInvoiceId   :=  lrec_Invoice.po_invoice_id;
           
           -- Delete invoice references
           DELETE po_invoice_line_tax WHERE po_invoice_db_id = ln_PoInvoiceDbId AND po_invoice_id = ln_PoInvoiceId; 
           
           DELETE po_invoice_line_charge WHERE po_invoice_db_id = ln_PoInvoiceDbId AND po_invoice_id = ln_PoInvoiceId; 
           
           DELETE po_invoice_line_map WHERE po_invoice_db_id = ln_PoInvoiceDbId AND po_invoice_id = ln_PoInvoiceId; 
             
           DELETE po_invoice_line WHERE po_invoice_db_id = ln_PoInvoiceDbId AND po_invoice_id = ln_PoInvoiceId; 
            
           DELETE po_invoice WHERE po_invoice_db_id = ln_PoInvoiceDbId AND po_invoice_id = ln_PoInvoiceId;
           
           -- Delete event references
           DELETE evt_stage WHERE event_db_id = ln_PoInvoiceDbId AND event_id = ln_PoInvoiceId;
            
           DELETE evt_event WHERE event_db_id = ln_PoInvoiceDbId AND event_id = ln_PoInvoiceId;  
                  
        END LOOP;
        
        
        /***********************************         
        /* Loop through all part requests
        /**********************************/
        FOR lrec_PartRequest IN lcur_PartRequest( ln_PoDbId, ln_PoId, ln_PoLineId ) LOOP
           
           ln_ReqPartDbId := lrec_PartRequest.req_part_db_id;
           ln_ReqPartId   := lrec_PartRequest.req_part_id; 
                      
           -- Delete part request references                   
           UPDATE inv_xfer 
           SET 
               init_event_db_id = NULL,
               init_event_id    = NULL
           WHERE
               init_event_db_id = ln_ReqPartDbId AND
               init_event_id    = ln_ReqPartId;
               
           DELETE req_part WHERE req_part_db_id = ln_ReqPartDbId AND req_part_id = ln_ReqPartId;
           
           DELETE evt_stage WHERE event_db_id = ln_ReqPartDbId AND event_id = ln_ReqPartId;
                   
           DELETE evt_event WHERE event_db_id = ln_ReqPartDbId AND event_id = ln_ReqPartId;
                           
        END LOOP;
             
        
        /***********************************************   
        /** Delete / update part line references    
        /***********************************************/
        
        -- Clear po foreign keys for inventory
        UPDATE inv_inv
        SET 
            inv_inv.po_db_id     = NULL,
            inv_inv.po_id        = NULL,
            inv_inv.po_line_id   = NULL
        WHERE
            inv_inv.po_db_id   = ln_PoDbId AND
            inv_inv.po_id      = ln_PoId AND
            inv_inv.po_line_id = ln_PoLineId;
        
        -- Clear po line foreign keys for shipment line
        UPDATE ship_shipment_line
        SET
           po_db_id      = NULL,
           po_id         = NULL,
           po_line_id    = NULL
        WHERE            
           ship_shipment_line.po_db_id   = ln_PoDbId AND
           ship_shipment_line.po_id      = ln_PoId AND
           ship_shipment_line.po_line_id = ln_PoLineId; 
        
        -- Delete po_line_tax       
        DELETE 
              po_line_tax 
        WHERE 
              po_line_tax.po_db_id   = ln_PoDbId AND 
              po_line_tax.po_id      = ln_PoId AND
              po_line_tax.po_line_id = ln_PoLineId;        
        
        -- Delete po_line_charge
        DELETE 
              po_line_charge 
        WHERE 
              po_line_charge.po_db_id   = ln_PoDbId AND 
              po_line_charge.po_id      = ln_PoId AND
              po_line_charge.po_line_id = ln_PoLineId;   
        
        -- Delete po_line_kit_line       
        DELETE 
              po_line_kit_line
        WHERE 
              po_line_kit_line.po_db_id   = ln_PoDbId AND 
              po_line_kit_line.po_id      = ln_PoId AND
              po_line_kit_line.po_line_id = ln_PoLineId;  
        
        -- Delete po_line
        DELETE 
              po_line 
        WHERE 
              po_line.po_db_id   = ln_PoDbId AND 
              po_line.po_id      = ln_PoId AND
              po_line.po_line_id = ln_PoLineId;  
                 
        
         /***********************************************   
        /** Delete / update consignment order references   
        /***********************************************/  
                
        -- If the PO primary key are not the same, then it means we are looking at a new PO.
        -- Determine if the previous PO has anymore part lines.  If not, then remove PO
        IF( ( ln_PreviousPoDbId IS NULL ) OR ( ln_PreviousPoDbId != ln_PoDbId OR ln_PreviousPoId != ln_PoId ) ) THEN
        
      
            SELECT
                COUNT( po_line.po_db_id )
            INTO
                lb_PartLineCount
            FROM 
                 po_line
            WHERE
                 po_line.po_db_id = ln_PoDbId AND
                 po_line.po_id    = ln_PoId;                 
                 
            -- Delete all consignment order references if it does not have any part lines     
            IF ( lb_PartLineCount = 0 ) THEN

                -- Clear po foreign keys for shipment   
                UPDATE ship_shipment
                SET
                   ship_shipment.po_db_id = NULL,
                   ship_shipment.po_id    = NULL
                WHERE            
                   ship_shipment.po_db_id = ln_PoDbId AND
                   ship_shipment.po_id    = ln_PoId; 
                
                -- Delete PO references
                DELETE po_auth WHERE po_auth.po_db_id = ln_PoDbId AND po_auth.po_id = ln_PoId;
                
                DELETE po_header WHERE po_header.po_db_id = ln_PoDbId AND po_header.po_id = ln_PoId;      
            
                -- Delete event references
                DELETE evt_stage WHERE event_db_id = ln_PoDbId AND event_id = ln_PoId;
                
                DELETE evt_event WHERE event_db_id = ln_PoDbId AND event_id = ln_PoId;
                
                -- Reassign the previous reference
                ln_PreviousPoDbId := ln_PoDbId;
                ln_PreviousPoId   := ln_PoId;
                
            END IF;
            
        END IF;        
    
    END LOOP;    

END;
/