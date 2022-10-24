package ca.ripam.stockmanagement;

import ca.ripam.stockmanagement.exception.DocumentAlreadyExistsException;
import ca.ripam.stockmanagement.exception.DocumentNotFoundException;
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
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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

    @Nested
    @DisplayName("When creating new stock record")
    class WhenCreatingStock {

        @Test
        @DisplayName("Then should return stock data")
        void shouldReturnStockData() {

            Stock input = Stock.builder().id("6352126067f90330d6533ebb").build();

            Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(new ArrayList());
            Mockito.when(repository.save(Mockito.any())).thenReturn(input);

            Stock result = service.createStock(input);

            assertAll(
                    () -> assertTrue(result != null)
            );
        }

        @Test
        @DisplayName("And stock already exists Then should throw exception")
        void andExistingRecordShouldThrowError() {

            Stock input = Stock.builder().id("6352126067f90330d6533ebb").stock("AAA").build();

            Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(Arrays.asList(input));

            assertThatThrownBy(() -> service.createStock(input))
                    .isInstanceOf(DocumentAlreadyExistsException.class)
                    .hasMessage(input.getStock());
        }
    }

    @Nested
    @DisplayName("When querying stock by name")
    class WhenQueryingStockByName {

        @Test
        @DisplayName("Then should return list of stock data")
        void shouldReturnStockData() {

            Stock input = Stock.builder().id("6352126067f90330d6533ebb").build();

            Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(Arrays.asList(input));

            List<Stock> result = service.searchStockByName("aa");

            assertAll(
                    () -> assertTrue(result != null),
                    () -> assertEquals(1, result.size())
            );
        }

        @Test
        @DisplayName("And stock dont exist Then should throw exception")
        void andExistingRecordShouldThrowError() {

            Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(new ArrayList());

            assertThatThrownBy(() -> service.searchStockByName("aaa"))
                    .isInstanceOf(DocumentNotFoundException.class)
                    .hasMessage("aaa");
        }
    }
}