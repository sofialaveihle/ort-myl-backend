package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Type;
import ar.com.myldtos.cards.TypeDTO;

public class TypeMapper extends CardPropertiesMapper<Type, TypeDTO> {
    @Override
    public TypeDTO toDTO(Type type) {
        TypeDTO typeDTO = new TypeDTO();
        super.toDTO(type, typeDTO);
        return typeDTO;
    }
}
