package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.entities.cards.Type;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.TypeMapper;
import ar.com.myldtos.cards.TypeDTO;
import com.google.gson.Gson;

public class TypeController extends CardPropertyController<Type, TypeDTO, TypeMapper> {
    public TypeController(Gson gson, TypeMapper mapper, DAOCardProperties<Type, Integer> daoType) throws MylException {
        super(gson, mapper, daoType);
    }
}
