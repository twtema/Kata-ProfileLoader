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
        numbervalue.add("+489001001010");
        numbervalue.add("89001654010");
        numbervalue.add("89074001010");
        numbervalue.add("89001012221");
        numbervalue.add("89844563210");
        numbervalue.add("89001077710");
    }

    private String uuid = UUID.randomUUID().toString();
    private List<String> numbervalue = new ArrayList<>();
}
