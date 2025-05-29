package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.entities.cards.CardProperties;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.CardPropertiesMapper;
import ar.com.myldtos.cards.CardPropertiesDTO;
import com.google.gson.Gson;

import java.util.List;

public class CardPropertyController<T extends CardProperties, R extends CardPropertiesDTO, U extends CardPropertiesMapper<T, R>> {
    private final U mapper;
    private final Class<T> entityClass;

    protected CardPropertyController(U mapper, Class<T> entityClass) {
        this.mapper = mapper;
        this.entityClass = entityClass;
    }

    public String getAll() throws MylException {
        DAOCardProperties<T, Integer> daoCardProperties = new DAOCardProperties<>(entityClass);
        List<T> cardPropertiesList = daoCardProperties.findAll();

        List<R> cardPropertiesDTOList = cardPropertiesList.stream().map(mapper::toDTO).toList();
        Gson gson = new Gson();
        return gson.toJson(cardPropertiesDTOList);
    }
}
