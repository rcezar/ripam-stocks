package ca.ripam.stockmanagement.mapper;

import ca.ripam.stockmanagement.dto.CreateStockDto;
import ca.ripam.stockmanagement.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockMapper {

    Stock dtoToModel(CreateStockDto dto);
}
