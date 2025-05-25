package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.Type;
import myldtos.cards.TypeDTO;

public class TypeMapper {
    public static TypeDTO toDTO(Type type) {
        TypeDTO typeDTO = new TypeDTO();
        CardPropertiesMapper.toDTO(type, typeDTO);
        return typeDTO;
    }
}
