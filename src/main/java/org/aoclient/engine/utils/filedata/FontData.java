package org.aoclient.engine.utils.filedata;

public final class FontData {
    private int font_size;
    private int[] ascii_code = new int[256];

    public int getFont_size() {
        return font_size;
    }

    public void setFont_size(int font_size) {
        this.font_size = font_size;
    }

    public int getAscii_code(int index) {
        return ascii_code[index];
    }

    public void setAscii_code(int index, int ascii_code) {
        this.ascii_code[index] = ascii_code;
    }
}
