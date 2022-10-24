package ca.ripam.stockmanagement;

import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.repository.StockRepository;
import ca.ripam.stockmanagement.service.StockServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StockService - Unit Test")
@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @InjectMocks
    private StockServiceImpl service;

    @Mock
    private StockRepository repository;

    @Nested
    @DisplayName("When querying stock by id")
    class WhenQueryingStockById {

        @Test
        @DisplayName("Then should return stock data")
        void shouldReturnStockData() {

            Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(Stock.builder().id("6352126067f90330d6533ebb").build()));

            Optional<Stock> result = service.retrieveById(new ObjectId("6352126067f90330d6533ebb"));

            assertAll(
                    () -> assertTrue(result.isPresent())
            );
        }
    }
}