package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Type;
import ar.com.myldtos.cards.TypeDTO;
import jakarta.validation.constraints.NotNull;

public class TypeMapper extends CardPropertiesMapper<Type, TypeDTO> {
    @NotNull
    @Override
    public TypeDTO toDTO(Type type) {
        TypeDTO typeDTO = new TypeDTO();
        super.toDTO(type, typeDTO);
        return typeDTO;
    }

    @NotNull
    @Override
    public Type toEntity(TypeDTO typeDTO) {
        Type type = new Type();
        super.toEntity(typeDTO, type);
        return type;
    }
}
