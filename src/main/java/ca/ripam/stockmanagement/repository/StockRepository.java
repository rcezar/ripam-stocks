package ca.ripam.stockmanagement.repository;

import ca.ripam.stockmanagement.model.Stock;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends MongoRepository<Stock, ObjectId> {
}
