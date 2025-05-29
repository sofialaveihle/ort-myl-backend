package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.KeyWord;
import ar.com.myldtos.cards.KeyWordDTO;

public class KeyWordMapper extends CardPropertiesMapper<KeyWord, KeyWordDTO> {
    @Override
    public KeyWordDTO toDTO(KeyWord keyWord) {
        KeyWordDTO keyWordDTO = new KeyWordDTO();
        if (keyWord != null) {
            super.toDTO(keyWord, keyWordDTO);
            keyWordDTO.setDefinition(keyWord.getDefinition());
        }
        return keyWordDTO;
    }
}
