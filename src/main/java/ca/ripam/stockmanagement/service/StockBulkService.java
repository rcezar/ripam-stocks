package ca.ripam.stockmanagement.service;

import ca.ripam.stockmanagement.dto.StockBulkUploadDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StockBulkService {

    StockBulkUploadDto createStocksInBulk(MultipartFile file) throws IOException;

    void processFile(String documentId);

    List<String> retrieveNextFiles(int limit);
}
