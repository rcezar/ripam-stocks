package ca.ripam.stockmanagement.service;

import ca.ripam.stockmanagement.exception.DocumentAlreadyExistsException;
import ca.ripam.stockmanagement.exception.DocumentNotFoundException;
import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService{

    private final StockRepository repository;

    @Override
    public Optional<Stock> retrieveById(ObjectId objectId) {
        return repository.findById(objectId);
    }

    @Override
    public Stock createStock(Stock localStock) {
        repository.findAll(Example.of(Stock.builder()
                        .stock(localStock.getStock())
                        .quarter(localStock.getQuarter())
                        .date(localStock.getDate())
                        .build()))
                .stream()
                .findFirst()
                .ifPresent(this::throwException);

        log.debug("Creating new stock record...");
        return repository.save(localStock);
    }

    public void throwException(Stock stock) {
        log.warn("stock already exists. Id={}", stock.getId());
        throw new DocumentAlreadyExistsException(stock.getStock());
    }

    @Override
    public List<Stock> searchStockByName(String stockName) {
        List<Stock> results = repository.findAll(
                Example.of(Stock.builder()
                        .stock(stockName)
                        .build()));

        if (results.isEmpty()){
            throw new DocumentNotFoundException( stockName);
        }else{
            return results;
        }
    }
}
