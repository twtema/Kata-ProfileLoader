package org.kata;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.kata.service.mapper.DocumentMapper;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Inherited;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DocumentMapperTest implements MappersTestInterface<Document, DocumentDto, DocumentMapper> {

    @Autowired
    private DocumentMapper documentMapper;

    private Document document;

    @Before
    public void setUp() {
        document = new Document();
        setModelFields(document);
    }

    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        Field[] modelFields = Document.class.getDeclaredFields();
        Field[] dtoFields = DocumentDto.class.getDeclaredFields();
        modelFields = Arrays.stream(dtoFields).peek(f -> f.setAccessible(true)).toList();
        dtoFields = Arrays.stream(dtoFields).peek(f -> f.setAccessible(true)).toList();
        DocumentDto documentDto = documentMapper.toDto(document);



        assertThat(documentDto).
                hasFieldOrPropertyWithValue(dtoFieldsList.get(2).getName(),
                        Arrays.stream(modelFields)
                                .filter(f -> f.getName().equals(dtoFieldsList.get(2).getName()))
                                .findFirst().get().get(document));

    }

    @Override
    public void shouldMapDtoToEntity() {

    }

    @Override
    public void setModelFields(Document document) {
        document.setDocumentNumber("123");
    }

    @Override
    public void setDtoFields(DocumentDto documentDto) {
    }



}
