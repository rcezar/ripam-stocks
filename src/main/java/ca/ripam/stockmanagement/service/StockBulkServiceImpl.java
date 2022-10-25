package ca.ripam.stockmanagement.service;

import ca.ripam.stockmanagement.dto.StockBulkUploadDto;
import ca.ripam.stockmanagement.model.Stock;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StockBulkServiceImpl implements StockBulkService{

    public static final String ID = "_id";
    public static final String FILE_NAME = "fileName";
    private final StockService stockService;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;

    public StockBulkServiceImpl(StockService stockService, GridFsTemplate gridFsTemplate, GridFsOperations operations) {
        this.stockService = stockService;
        this.gridFsTemplate = gridFsTemplate;
        this.operations = operations;
    }

    @Override
    public StockBulkUploadDto createStocksInBulk(MultipartFile file) throws IOException {
        String documentId = addFile(file);
        return StockBulkUploadDto.builder().id(documentId).filename(file.getOriginalFilename()).build();
    }

    @Async
    public void processFile(String documentId) {
        log.info("Processing document {}", documentId);
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where(ID).is(documentId)));

        try {
            readCSVFileAndMapToStock(documentId, file);

            log.info("Removing file...");
            gridFsTemplate.delete(new Query(Criteria.where(ID).is(documentId)));

            log.info("Finishing processing file {}", documentId);
        } catch (IOException e) {
            log.error("Error when processing file {}", documentId);
        }
    }

    @Override
    public List<String> retrieveNextFiles(int limit) {
        List<String> results = new ArrayList<>();

        gridFsTemplate.find(new Query())
                .limit(limit)
                .map(x->x.getObjectId().toString())
                .into(results);

        return results;
    }

    private String addFile(MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put(FILE_NAME, file.getOriginalFilename());
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    private void readCSVFileAndMapToStock(String documentId, GridFSFile file) throws IOException {

        GridFsResource gridFsResource = operations.getResource(file);

        if (gridFsResource != null) {

            Reader reader = new InputStreamReader(gridFsResource.getInputStream());

            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .build();
            String[] nextRecord;
            List<Stock> errorList = new ArrayList<>();
            Integer successfulItems = 0;

            while ((nextRecord = csvReader.readNext()) != null) {
                Stock local = Stock.builder()
                        .quarter(Integer.valueOf(nextRecord[0]))
                        .stock(String.valueOf(nextRecord[1]))
                        .date(String.valueOf(nextRecord[2]))
                        .open(String.valueOf(nextRecord[3]))
                        .high(String.valueOf(nextRecord[4]))
                        .low(String.valueOf(nextRecord[5]))
                        .close(String.valueOf(nextRecord[6]))
                        .volume(String.valueOf(nextRecord[7]))
                        .percent_change_price(String.valueOf(nextRecord[8]))
                        .percent_change_volume_over_last_wk(String.valueOf(nextRecord[9]))
                        .previous_weeks_volume(String.valueOf(nextRecord[10]))
                        .next_weeks_open(String.valueOf(nextRecord[11]))
                        .next_weeks_close(String.valueOf(nextRecord[12]))
                        .percent_change_next_weeks_price(String.valueOf(nextRecord[13]))
                        .days_to_next_dividend(String.valueOf(nextRecord[14]))
                        .percent_return_next_dividend(String.valueOf(nextRecord[15]))
                        .build();
                try {
                    stockService.createStock(local);
                    successfulItems++;

                } catch (Exception error) {
                    errorList.add(local);
                }
            }

            log.info("DocumentId = {} : #{} stocks successfully added", documentId, successfulItems);
            log.info("DocumentId = {} : #{} stocks not added", documentId, errorList.size());
        }else{
            throw new FileNotFoundException("File associated to document id not found" + documentId);
        }
    }
}
