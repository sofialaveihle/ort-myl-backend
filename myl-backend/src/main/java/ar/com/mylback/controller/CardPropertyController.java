package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.entities.cards.CardProperties;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.CardPropertiesMapper;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.cards.CardPropertiesDTO;
import com.google.gson.Gson;

import java.util.List;

public class CardPropertyController<T extends CardProperties, R extends CardPropertiesDTO, U extends CardPropertiesMapper<T, R>> {
    private final Gson gson;
    private final U mapper;
    private final Class<T> entityClass;

    protected CardPropertyController(Gson gson, U mapper, Class<T> entityClass) throws MylException {
        if (gson != null && mapper != null && entityClass != null) {
            this.gson = gson;
            this.mapper = mapper;
            this.entityClass = entityClass;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public HttpResponse getAll() {
        try {
            DAOCardProperties<T, Integer> daoCardProperties = new DAOCardProperties<>(entityClass);
            List<T> cardPropertiesList = daoCardProperties.findAll();

            List<R> cardPropertiesDTOList = cardPropertiesList.stream().map(mapper::toDTO).toList();
            return new HttpResponse(200, gson.toJson(cardPropertiesDTOList));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al obtener propiedades de carta", e.getMessage())));

        }
    }
}
