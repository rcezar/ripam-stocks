package ca.ripam.stockmanagement.controller;

import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.exception.MethodNotImplemented;
import ca.ripam.stockmanagement.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Stock> retrieveStocksByName(@RequestParam String tickerInput){
        log.info("Retrieving stock by name {}", tickerInput);
        throw new MethodNotImplemented("Method not yet implemented");
    }

    @GetMapping("/{stockId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Stock retrieveStocksById(@PathVariable String stockId){
        //TODO: validate payload
        log.info("Retrieving stock by id {}", stockId);
        throw new MethodNotImplemented("Method not yet implemented");
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Stock createStock(@RequestBody CreateStockDto payload){
        //TODO: validate payload
        log.info("Creating Stock record {}", payload.getStock());
        throw new MethodNotImplemented("Method not yet implemented");
    }

    @PostMapping(value = "/in-bulk", consumes = "multipart/form-data", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public String createStocksInBulk(@RequestParam MultipartFile file) {
        log.info("Creating stocks in bulk with filename {}", file.getOriginalFilename());
        throw new MethodNotImplemented("Method not yet implemented");
    }

}
