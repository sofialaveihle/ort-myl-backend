package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.KeyWord;
import ar.com.myldtos.cards.KeyWordDTO;
import jakarta.validation.constraints.NotNull;

public class KeyWordMapper extends CardPropertiesMapper<KeyWord, KeyWordDTO> {
    @NotNull
    @Override
    public KeyWordDTO toDTO(KeyWord keyWord) {
        KeyWordDTO keyWordDTO = new KeyWordDTO();
        if (keyWord != null) {
            super.toDTO(keyWord, keyWordDTO);
            keyWordDTO.setDefinition(keyWord.getDefinition());
        }
        return keyWordDTO;
    }

    @NotNull
    @Override
    public KeyWord toEntity(KeyWordDTO keyWordDTO) {
        KeyWord keyWord = new KeyWord();
        super.toEntity(keyWordDTO, keyWord);
        return keyWord;
    }
}
