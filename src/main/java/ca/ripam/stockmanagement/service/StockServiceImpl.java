package ca.ripam.stockmanagement.service;

import ca.ripam.stockmanagement.exception.DocumentAlreadyExistsException;
import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StockServiceImpl implements StockService{

    private final StockRepository repository;

    public StockServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

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

        log.info("Creating new stock record...");
        return repository.save(localStock);
    }

    public void throwException(Stock stock) {
        log.warn("stock already exists. Id={}", stock.getId());
        throw new DocumentAlreadyExistsException(stock.getStock());
    }
}
