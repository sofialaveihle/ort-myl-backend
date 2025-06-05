package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Format;
import ar.com.mylback.utils.entitydtomappers.cards.FormatMapper;
import ar.com.myldtos.cards.FormatDTO;

public class FormatController extends CardPropertyController<Format, FormatDTO, FormatMapper> {
    public FormatController(FormatMapper mapper) {
        super(mapper, Format.class);
    }
}
