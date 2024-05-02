package br.com.chonide.domain.file.model;

public class Firmware extends CodeFile {

    public Firmware(String name) {
        super(name);
    }

    public Firmware(String name, String sourceCode) {
        super(name, sourceCode);
    }
}
