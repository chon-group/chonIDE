package group.chon.ide.api.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
@Getter
public class ReturnedFile {

    private final String fileName;

    private final InputStream inputStream;

}
