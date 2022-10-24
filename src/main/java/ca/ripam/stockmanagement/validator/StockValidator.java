package ca.ripam.stockmanagement.validator;

import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.exception.IllegalArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class StockValidator {

    public void validateObjectId(String input){
        if(StringUtils.isBlank(input) || !ObjectId.isValid(input)) {
            throw new IllegalArgumentException(String.format("Invalid Object Id: %s", input));
        }
    }

    public void validateCreateStockPayload(CreateStockDto input){

        if (input == null){
            throw new IllegalArgumentException(String.format("Payload cannot be null or empty"));
        }

        if(StringUtils.isBlank(input.getStock())){
            throw new IllegalArgumentException(String.format("Stock cannot be null or empty : %s", input.getStock()));
        }

        if(input.getQuarter() == null){
            throw new IllegalArgumentException(String.format("Quarter cannot be null or empty"));
        }

        if(input.getQuarter() <= 0 || input.getQuarter() > 4){
            throw new IllegalArgumentException(String.format("Quarter is outside (1-4) range: %s", input.getQuarter()));
        }

    }
}
