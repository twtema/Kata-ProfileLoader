package org.kata.entity.blackList;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.ContactMedium;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BlackListContacts extends ContactMedium {

    public BlackListContacts() {
        nombervalue.add("89001001010");
        nombervalue.add("89001654010");
        nombervalue.add("89074001010");
        nombervalue.add("89001012221");
        nombervalue.add("89844563210");
        nombervalue.add("89001077710");
    }

    private String uuid = UUID.randomUUID().toString();
    private List<String> nombervalue = new ArrayList<>();
}
