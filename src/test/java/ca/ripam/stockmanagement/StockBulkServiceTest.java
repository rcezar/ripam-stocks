package ca.ripam.stockmanagement;

import ca.ripam.stockmanagement.dto.StockBulkUploadDto;
import ca.ripam.stockmanagement.service.StockBulkServiceImpl;
import ca.ripam.stockmanagement.service.StockService;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StockBulkService - Unit Test")
@ExtendWith(MockitoExtension.class)
public class StockBulkServiceTest {

    @Mock
    private StockService stockService;

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private GridFsOperations operations;

    @InjectMocks
    private StockBulkServiceImpl service;

    @Nested
    @DisplayName("When creating stocks in bulk")
    class WhenCreatingStocksInBulk {

        @Test
        @DisplayName("Then should store file in db and return filename and id")
        void shouldReturnStoreFile() throws IOException {

            MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

            Mockito.when(gridFsTemplate.store(Mockito.any(InputStream.class),Mockito.anyString(),Mockito.anyString(),Mockito.any(DBObject.class)))
                    .thenReturn(new ObjectId("6352126067f90330d6533ebb"));

            StockBulkUploadDto result = service.createStocksInBulk(file);

            assertAll(
                    () -> assertTrue(result != null),
                    () -> assertTrue(result.getFilename() != null)
            );
        }
    }
}