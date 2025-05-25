package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.KeyWord;
import ar.com.myldtos.cards.KeyWordDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KeyWordMapper {
    public static KeyWordDTO toDTO(KeyWord keyWord) {
        KeyWordDTO keyWordDTO = new KeyWordDTO();
        if (keyWord != null) {
            CardPropertiesMapper.toDTO(keyWord, keyWordDTO);
            keyWordDTO.setDefinition(keyWord.getDefinition());
        }
        return keyWordDTO;
    }

    public static List<KeyWordDTO> toDTO(Set<KeyWord> keyWords) {
        if (keyWords == null || keyWords.isEmpty()) {
            return new ArrayList<>();
        }
        return keyWords.stream().map(KeyWordMapper::toDTO).toList();
    }
}
