package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Type;
import ar.com.mylback.utils.entitydtomappers.cards.TypeMapper;
import ar.com.myldtos.cards.TypeDTO;

public class TypeController extends CardPropertyController<Type, TypeDTO, TypeMapper> {
    public TypeController(TypeMapper mapper) {
        super(mapper, Type.class);
    }
}
