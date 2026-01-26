package pl.edu.resourceserver.book.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.edu.resourceserver.book.dto.BookFullViewResponseDto;
import pl.edu.resourceserver.book.dto.BookUpdateDto;
import pl.edu.resourceserver.book.dto.BookUploadRequestDto;
import pl.edu.resourceserver.book.dto.BookPreviewResponseDto;
import pl.edu.resourceserver.book.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "publisher", source = "publisher.name")
    @Mapping(target = "genre", source = "genre.name")
    BookPreviewResponseDto toBookPreviewResponseDto(Book book);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "publisher", source = "publisher.name")
    @Mapping(target = "genre", source = "genre.name")
    BookFullViewResponseDto toBookFullViewResponseDto(Book book);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "genre", ignore = true)
    Book toBook(BookUploadRequestDto dto);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(BookUpdateDto dto, @MappingTarget Book book);
}
