package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Format;
import ar.com.myldtos.cards.FormatDTO;
import jakarta.validation.constraints.NotNull;

public class FormatMapper extends CardPropertiesMapper<Format, FormatDTO> {
    @NotNull
    @Override
    public FormatDTO toDTO(Format format) {
        FormatDTO formatDTO = new FormatDTO();
        super.toDTO(format, formatDTO);
        return formatDTO;
    }

    @NotNull
    @Override
    public Format toEntity(FormatDTO formatDTO) {
        Format format = new Format();
        super.toEntity(formatDTO, format);
        return format;
    }
}
