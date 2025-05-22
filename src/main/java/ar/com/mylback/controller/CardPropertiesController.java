package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.DAOCardProperties;
import ar.com.mylback.dal.entities.CardProperties;
import ar.com.mylback.utils.entitydtomappers.CardPropertiesMapper;
import ar.com.myldtos.cards.CardPropertiesDTO;
import com.google.gson.Gson;

import java.util.List;

public class CardPropertiesController<T extends CardProperties> {
    public String getAllEndpoint(Class<T> entityClass) throws Exception {
        DAOCardProperties<T, Integer> daoCardProperties = new DAOCardProperties<>(entityClass);
        List<T> cardPropertiesList = daoCardProperties.findAll();

        List<CardPropertiesDTO> cardPropertiesDTOList = cardPropertiesList.stream().map(CardPropertiesMapper::toDTO).toList();
        Gson gson = new Gson();
        return gson.toJson(cardPropertiesDTOList);
    }
}
