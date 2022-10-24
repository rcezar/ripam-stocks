package ca.ripam.stockmanagement.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document
public class StockBulkUploadDto {

    private String id;
    private String filename;
}
