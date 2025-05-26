package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Type;
import ar.com.myldtos.cards.TypeDTO;

public class TypeMapper {
    public static TypeDTO toDTO(Type type) {
        TypeDTO typeDTO = new TypeDTO();
        CardPropertiesMapper.toDTO(type, typeDTO);
        return typeDTO;
    }
}
