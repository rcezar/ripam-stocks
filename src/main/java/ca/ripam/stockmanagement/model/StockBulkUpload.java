package ca.ripam.stockmanagement.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document
public class StockBulkUpload {

    @Id
    private String id;

    @Indexed
    private String filename;


}
