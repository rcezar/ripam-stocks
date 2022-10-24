package ca.ripam.stockmanagement.service;

import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
}
