package ca.ripam.stockmanagement;

import ca.ripam.stockmanagement.controller.StockController;
import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.dto.StockBulkUploadDto;
import ca.ripam.stockmanagement.exception.DocumentNotFoundException;
import ca.ripam.stockmanagement.exception.IllegalArgumentException;
import ca.ripam.stockmanagement.mapper.StockMapper;
import ca.ripam.stockmanagement.model.Stock;
import ca.ripam.stockmanagement.service.StockBulkService;
import ca.ripam.stockmanagement.service.StockService;
import ca.ripam.stockmanagement.validator.StockValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("StockController - Unit Test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @SpyBean
    private StockValidator validator;

    @MockBean
    private StockService service;

    @MockBean
    private StockBulkService bulkService;

    @MockBean
    private StockMapper stockMapper;

    @Nested
    @DisplayName("When querying stock by ticker")
    class WhenQueryingStockByTicker {

        @Test
        @DisplayName("Then should return stock data")
        void AndShouldReturnStockData() throws Exception {

            Mockito.when(service.searchStockByName(Mockito.anyString())).thenReturn(Arrays.asList(Stock.builder().id("6352126067f90330d6533ebb").build()));

            mockMvc.perform(get("/stock?tickerInput=1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("When querying stock by id")
    class WhenQueryingStockById {

        @Test
        @DisplayName("Then should return stock data")
        void shouldReturnStockData() throws Exception {

            Mockito.when(service.retrieveById(Mockito.any())).thenReturn(Optional.of(Stock.builder().id("6352126067f90330d6533ebb").build()));

            mockMvc.perform(get("/stock/6352126067f90330d6533ebb"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("And result is empty Then should return stock data")
        void andResultIsEmptyThenShouldReturnStockData() throws Exception {

            mockMvc.perform(get("/stock/6352126067f90330d6533ebb"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof DocumentNotFoundException));
        }

        @Test
        @DisplayName("And with invalid id Then should return bad request error")
        void andWithNotValidIdThenShouldReturnStockData() throws Exception {

            mockMvc.perform(get("/stock/6352126067f90330d6533ebba"))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ca.ripam.stockmanagement.exception.IllegalArgumentException));
        }
    }

    @Nested
    @DisplayName("When creating stock record")
    class WhenCreatingStockRecord {

        @Test
        @DisplayName("With new valid data Then should save and return stock data with Id")
        void shouldSaveAndReturnStockDataWithId() throws Exception {

            CreateStockDto payload = CreateStockDto.builder()
                    .stock("AAA")
                    .quarter(1)
                    .build();

            Mockito.when(service.createStock(Mockito.any())).thenReturn(Stock.builder().build());

            mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(payload)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("With invalid quarter information Then should return bad request status")
        void AndWithInvalidQuarterShouldReturnBadRequest() throws Exception {

            CreateStockDto payload = CreateStockDto.builder().stock("AAA").build();

            mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(payload)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof IllegalArgumentException));
        }
    }

    @Nested
    @DisplayName("When creating stock records in bulk")
    class WhenCreatingStockRecordsInBulk {

        @Test
        @DisplayName("Then should return file id and run process method")
        void shouldReturnStockData() throws Exception {

            StockBulkUploadDto payload = StockBulkUploadDto.builder().id("AAA").build();

            MockMultipartFile file = new MockMultipartFile("file",
                    "hello.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World!".getBytes());

            Mockito.when(bulkService.createStocksInBulk(Mockito.any())).thenReturn(payload);

            mockMvc.perform(MockMvcRequestBuilders.multipart("/stock/in-bulk").file(file))
                    .andDo(print())
                    .andExpect(status().isAccepted());

            Mockito.verify(bulkService,Mockito.times(1)).processFile(ArgumentMatchers.any());
        }
    }
}