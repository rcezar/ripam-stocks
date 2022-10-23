package ca.ripam.stockmanagement;

import ca.ripam.stockmanagement.controller.StockController;
import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.exception.MethodNotImplemented;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void queryStockByTickerAndShouldReturnStockData() throws Exception {

        mockMvc.perform(get("/stock?tickerInput=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
    }

    @Test
    void queryStockByIdAndShouldReturnStockData() throws Exception {

        mockMvc.perform(get("/stock/635211bd67f90330d6533eba"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
    }

    @Test
    void createStockRecordAndShouldReturnStockData() throws Exception {

        CreateStockDto payload = CreateStockDto.builder().stock("AAA").build();

        mockMvc.perform(post("/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodNotImplemented));
    }

    @Test
    void createBulkStockRecordAndShouldReturnStockData() throws Exception {

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