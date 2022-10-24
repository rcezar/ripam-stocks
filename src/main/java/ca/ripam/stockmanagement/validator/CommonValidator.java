package ca.ripam.stockmanagement.validator;

import ca.ripam.stockmanagement.exception.IllegalArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class CommonValidator {

    public void validateObjectId(String input){
        if(!StringUtils.isBlank(input) && !ObjectId.isValid(input)) {
            throw new IllegalArgumentException(String.format("Invalid Object Id: %s", input));
        }
    }
}
