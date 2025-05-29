package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Format;
import ar.com.myldtos.cards.FormatDTO;

public class FormatMapper extends CardPropertiesMapper<Format, FormatDTO> {
    @Override
    public FormatDTO toDTO(Format format) {
        FormatDTO formatDTO = new FormatDTO();
        super.toDTO(format, formatDTO);
        return formatDTO;
    }
}
