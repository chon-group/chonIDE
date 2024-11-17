package group.chon.ide.news.domain.model.chonos;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public class Library {

    private final String name;

    private final InputStream inputStream;

}
