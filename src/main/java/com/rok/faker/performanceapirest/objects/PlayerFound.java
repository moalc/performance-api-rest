package com.rok.faker.performanceapirest.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerFound {

    private int index;
    private boolean found;

    public boolean isNotFound() {
        return this.found == false && this.index == -1 ? true : false;
    }
}
