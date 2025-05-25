package myldtos.cards;

import java.io.Serializable;

public class KeyWordDTO extends CardPropertiesDTO implements Serializable {
    private String definition;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
