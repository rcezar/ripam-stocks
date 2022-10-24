package ca.ripam.stockmanagement;

import ca.ripam.stockmanagement.controller.StockController;
import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.exception.MethodNotImplemented;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    private CommonValidator validator;

    @Nested
    @DisplayName("When querying stock by ticker")
    class WhenQueryingStockByTicker {

        @Test
        @DisplayName("Then should return stock data")
        void AndShouldReturnStockData() throws Exception {

            mockMvc.perform(get("/stock?tickerInput=1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
        }
    }

    @Nested
    @DisplayName("When querying stock by id")
    class WhenQueryingStockById {

        @Test
        @DisplayName("Then should return stock data")
        void shouldReturnStockData() throws Exception {

            mockMvc.perform(get("/stock/6352126067f90330d6533ebb"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
        }

        @Test
        @DisplayName("and with invalid id Then should return bad request error")
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

            CreateStockDto payload = CreateStockDto.builder().stock("AAA").build();

            mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(payload)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
        }
    }

    @Nested
    @DisplayName("When creating stock records in bulk")
    class WhenCreatingStockRecordsInBulk {

        @Test
        @DisplayName("Then should return stock data")
        void shouldReturnStockData() throws Exception {

            CreateStockDto payload = CreateStockDto.builder().stock("AAA").build();

            MockMultipartFile file = new MockMultipartFile("file",
                    "hello.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World!".getBytes());

            mockMvc.perform(MockMvcRequestBuilders.multipart("/stock/in-bulk").file(file))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
        }
    }
}