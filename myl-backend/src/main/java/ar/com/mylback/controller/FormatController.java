package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Format;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.FormatMapper;
import ar.com.myldtos.cards.FormatDTO;
import com.google.gson.Gson;

public class FormatController extends CardPropertyController<Format, FormatDTO, FormatMapper> {
    public FormatController(Gson gson, FormatMapper mapper) throws MylException {
        super(gson, mapper, Format.class);
    }
}
