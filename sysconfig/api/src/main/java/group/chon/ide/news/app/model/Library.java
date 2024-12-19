package group.chon.ide.news.app.model;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public class Library {

    private final String name;

    private final InputStream inputStream;

}
