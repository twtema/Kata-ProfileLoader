package org.kata.entity.blackList;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Component
@Getter
@Setter
public class BlackListDocuments extends Document {
    public BlackListDocuments() {
        series.add("0011");
        series.add("2222");
        numbers.add("333333");
        numbers.add("444444");
    }

    private String uuid = UUID.randomUUID().toString();
    private List<String> series = new ArrayList<>();
    private List<String> numbers = new ArrayList<>();

}