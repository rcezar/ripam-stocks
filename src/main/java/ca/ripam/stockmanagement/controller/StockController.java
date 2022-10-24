package ca.ripam.stockmanagement.controller;

import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.exception.DocumentNotFoundException;
import ca.ripam.stockmanagement.exception.MethodNotImplemented;
import ca.ripam.stockmanagement.mapper.StockMapper;
import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.service.StockService;
import ca.ripam.stockmanagement.validator.StockValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockValidator validator;
    private final StockService service;
    private final StockMapper mapper;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Stock> retrieveStocksByName(@RequestParam String tickerInput){
        log.info("Retrieving stock by name {}", tickerInput);
        throw new MethodNotImplemented("Method not yet implemented");
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
    public String createStocksInBulk(@RequestParam MultipartFile file) {
        log.info("Creating stocks in bulk with filename {}", file.getOriginalFilename());
        throw new MethodNotImplemented("Method not yet implemented");
    }

}
