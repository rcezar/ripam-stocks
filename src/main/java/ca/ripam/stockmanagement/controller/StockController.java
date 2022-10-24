package ca.ripam.stockmanagement.controller;

import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.dto.StockBulkUploadDto;
import ca.ripam.stockmanagement.exception.DocumentNotFoundException;
import ca.ripam.stockmanagement.mapper.StockMapper;
import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.service.StockBulkService;
import ca.ripam.stockmanagement.service.StockService;
import ca.ripam.stockmanagement.validator.StockValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockValidator validator;
    private final StockService service;
    private final StockBulkService bulkService;
    private final StockMapper mapper;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Stock> retrieveStocksByName(@RequestParam String tickerInput){
        log.info("Retrieving stock by name {}", tickerInput);
        return service.searchStockByName(tickerInput);
    }

    @GetMapping("/{stockId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Stock retrieveStocksById(@PathVariable String stockId){

        validator.validateObjectId(stockId);
        log.info("Retrieving stock by id {}", stockId);
        return service.retrieveById(new ObjectId(stockId))
                .orElseThrow(() -> new DocumentNotFoundException(stockId));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Stock createStock(@RequestBody CreateStockDto payload){
        validator.validateCreateStockPayload(payload);
        log.info("Creating Stock record {}", payload.getStock());
        return service.createStock(mapper.dtoToModel(payload));
    }

    @PostMapping(value = "/in-bulk", consumes = "multipart/form-data", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public StockBulkUploadDto createStocksInBulk(@RequestParam MultipartFile file) throws IOException {
        log.info("Creating stocks in bulk with filename {}", file.getOriginalFilename());
        StockBulkUploadDto dto = bulkService.createStocksInBulk(file);
        bulkService.processFile(dto.getId());
        return dto;
    }

}
