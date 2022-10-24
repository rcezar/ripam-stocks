package ca.ripam.stockmanagement.service;

import ca.ripam.stockmanagement.model.Stock;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface StockService {

    Optional<Stock> retrieveById(ObjectId objectId);

    Stock createStock(Stock dtoToModel);
}
