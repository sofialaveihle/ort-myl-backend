package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.Format;
import ar.com.myldtos.cards.FormatDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FormatMapper {
    public static FormatDTO toDTO(Format format) {
        FormatDTO formatDTO = new FormatDTO();
        CardPropertiesMapper.toDTO(format, formatDTO);
        return formatDTO;
    }

    public static List<FormatDTO> toDTO(Set<Format> formats) {
        if (formats == null || formats.isEmpty()) {
            return new ArrayList<>();
        }
        return formats.stream().map(FormatMapper::toDTO).toList();
    }
}
