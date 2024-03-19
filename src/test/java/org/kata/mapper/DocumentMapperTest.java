package org.kata.mapper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;

@RunWith(SpringRunner.class)
public class DocumentMapperTest implements MapperTest<Document, DocumentDto> {

    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("documentSetter")
    private Setter setter;

    private Document documentFrom;
    private DocumentDto documentDtoFrom;

    @Before
    public void setUp() {
        documentFrom = new Document();
        documentDtoFrom = documentMapper.toDto(documentFrom);
        setter.setEntityFields(documentFrom);
        setter.setDtoFields(documentDtoFrom);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                documentFrom,
                documentMapper.toDto(documentFrom));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                documentDtoFrom,
                documentMapper.toEntity(documentDtoFrom));
    }

}
