package org.aoclient.engine.renderer;

import static org.aoclient.engine.renderer.Drawn.drawGrhIndex;
import static org.aoclient.engine.utils.GameData.grhData;

public class FontTypes {
    public static final int NORMAL_FONT = 0;
    public static final int HIT_FONT = 1;

    static class Font {
        int fontSize;
        int[] ascii_code = new int[256];
    }

    public static Font[] font_types;

    public static void loadFonts() {
        font_types = new Font[2];

        font_types[0] = new Font();
        font_types[1] = new Font();

        font_types[0].fontSize = 9;

        // Numeros
        font_types[0].ascii_code[48] = 23703;
        font_types[0].ascii_code[49] = 23704;
        font_types[0].ascii_code[50] = 23705;
        font_types[0].ascii_code[51] = 23706;
        font_types[0].ascii_code[52] = 23707;
        font_types[0].ascii_code[53] = 23708;
        font_types[0].ascii_code[54] = 23709;
        font_types[0].ascii_code[55] = 23710;
        font_types[0].ascii_code[56] = 23711;
        font_types[0].ascii_code[57] = 23712;

        // abc
        for (int i = 0; i <= 25; i++) {
            font_types[0].ascii_code[i + 97] = 23651 + i;
        }

        // ABC
        for (int i = 0; i <= 25; i++) {
            font_types[0].ascii_code[i + 65] = 23677 + i;
        }


        font_types[0].ascii_code[33] = 23713;
        font_types[0].ascii_code[161] = 23714;
        font_types[0].ascii_code[34] = 23715;
        font_types[0].ascii_code[63] = 23716;
        font_types[0].ascii_code[191] = 23717;
        font_types[0].ascii_code[35] = 23718;
        font_types[0].ascii_code[36] = 23719;
        font_types[0].ascii_code[37] = 23720;
        font_types[0].ascii_code[38] = 23721;
        font_types[0].ascii_code[47] = 23722;
        font_types[0].ascii_code[92] = 23723;
        font_types[0].ascii_code[40] = 23724;
        font_types[0].ascii_code[41] = 23725;
        font_types[0].ascii_code[61] = 23726;
        font_types[0].ascii_code[39] = 23727;
        font_types[0].ascii_code[123] = 23728;
        font_types[0].ascii_code[125] = 23729;
        font_types[0].ascii_code[95] = 23730;
        font_types[0].ascii_code[45] = 23731;


        font_types[0].ascii_code[64] = 23732;
        font_types[0].ascii_code[94] = 23733;
        font_types[0].ascii_code[91] = 23734;
        font_types[0].ascii_code[93] = 23735;
        font_types[0].ascii_code[60] = 23736;
        font_types[0].ascii_code[62] = 23737;
        font_types[0].ascii_code[42] = 23738;
        font_types[0].ascii_code[43] = 23739;
        font_types[0].ascii_code[46] = 23740;
        font_types[0].ascii_code[44] = 23741;
        font_types[0].ascii_code[58] = 23742;
        font_types[0].ascii_code[59] = 23743;
        font_types[0].ascii_code[124] = 23744;

        font_types[0].ascii_code[252] = 23745;
        font_types[0].ascii_code[220] = 23746;
        font_types[0].ascii_code[225] = 23747;
        font_types[0].ascii_code[233] = 23748;
        font_types[0].ascii_code[237] = 23749;
        font_types[0].ascii_code[243] = 23750;
        font_types[0].ascii_code[250] = 23751;
        font_types[0].ascii_code[253] = 23752;
        font_types[0].ascii_code[193] = 23753;
        font_types[0].ascii_code[201] = 23754;
        font_types[0].ascii_code[205] = 23755;
        font_types[0].ascii_code[211] = 23756;
        font_types[0].ascii_code[218] = 23757;
        font_types[0].ascii_code[221] = 23758;
        font_types[0].ascii_code[224] = 23759;
        font_types[0].ascii_code[232] = 23760;
        font_types[0].ascii_code[236] = 23761;
        font_types[0].ascii_code[242] = 23762;
        font_types[0].ascii_code[249] = 23763;
        font_types[0].ascii_code[192] = 23764;
        font_types[0].ascii_code[200] = 23765;
        font_types[0].ascii_code[204] = 23766;
        font_types[0].ascii_code[210] = 23767;
        font_types[0].ascii_code[217] = 23768;
        font_types[0].ascii_code[241] = 23769;
        font_types[0].ascii_code[209] = 23770;

        font_types[0].ascii_code[196] = 23774;
        font_types[0].ascii_code[194] = 23775;
        font_types[0].ascii_code[203] = 23776;
        font_types[0].ascii_code[207] = 23777;
        font_types[0].ascii_code[214] = 23778;
        font_types[0].ascii_code[212] = 23779;

        font_types[1].fontSize = 9;

        font_types[1].ascii_code[97] = 23774;
        font_types[1].ascii_code[108] = 23775;
        font_types[1].ascii_code[115] = 23776;
        font_types[1].ascii_code[70] = 23777;
        font_types[1].ascii_code[48] = 23778;
        font_types[1].ascii_code[49] = 23779;
        font_types[1].ascii_code[50] = 23780;
        font_types[1].ascii_code[51] = 23781;
        font_types[1].ascii_code[52] = 23782;
        font_types[1].ascii_code[53] = 23783;
        font_types[1].ascii_code[54] = 23784;
        font_types[1].ascii_code[55] = 23785;
        font_types[1].ascii_code[56] = 23786;
        font_types[1].ascii_code[57] = 23787;
        font_types[1].ascii_code[33] = 23788;
        font_types[1].ascii_code[161] = 23789;
        font_types[1].ascii_code[42] = 23797;
    }


    public static void drawText(String text, int x, int y, RGBColor color, int fontIndex, boolean multiLine) {
        int a, b = 0, c, d, e, f;
        if (text.isEmpty()) return;

        d = 0;
        if (!multiLine) {
            for (a = 0; a < text.length(); a++) {
                b = text.charAt(a);
                if (b > 255) b = 0;

                if (b != 32) {
                    if (font_types[fontIndex].ascii_code[b] != 0) {
                        drawGrhIndex(font_types[fontIndex].ascii_code[b], (x + d), y, color);
                        d = d + grhData[font_types[fontIndex].ascii_code[b]].getPixelWidth();
                    }
                } else {
                    d = d + 4;
                }
            }
        } else {
            e = 0;
            f = 0;
            for (a = 0; a < text.length(); a++) {
                b = text.charAt(a);
                if (b > 255) b = 0;

                if (b == 32 || b == 13) {
                    if (e >= 20) {
                        f++;
                        e = 0;
                        d = 0;
                    } else {
                        if (b == 32) d = d + 4;
                    }
                } else {
                    if (font_types[fontIndex].ascii_code[b] > 12) {
                        drawGrhIndex(font_types[fontIndex].ascii_code[b], (x + d), y + f * 14, color);
                        d = d + grhData[font_types[fontIndex].ascii_code[b]].getPixelWidth();
                    }
                }

                e = e + 1;
            }
        }
    }


    public static int getTextWidth(String text, boolean multi) {
        int retVal = 0;
        int b, e, f, d = 0;

        if(!multi) {
            for (int a = 0; a < text.length(); a++) {
                b = text.charAt(a);

                if( (b != 32) && (b != 5) && (b != 129) && (b != 9) && (b != 4) && (b != 255) && (b != 2) && (b != 151) && (b != 152)) {
                    retVal = retVal + grhData[font_types[NORMAL_FONT].ascii_code[b]].getPixelWidth();
                } else {
                    retVal = retVal + 4;
                }
            }

        } else {
            e = 0;
            f = 0;

            for (int a = 0; a < text.length(); a++) {
                b = text.charAt(a);

                if(b == 32 || b == 13){
                    if (e >= 20) { // reemplazar por lo que quieran
                        f++;
                        e = 0;
                        d = 0;
                    } else {
                        if (b == 32) d += 4;
                    }

                } else {
                    if (font_types[NORMAL_FONT].ascii_code[b] > 12) {
                        d += grhData[font_types[NORMAL_FONT].ascii_code[b]].getPixelWidth();
                        if (d > retVal) retVal = d;
                    }
                }

                e++;
            }

        }

        return retVal;
    }



    //Function Engine_Text_Width(Texto As String, Optional multi As Boolean = False) As Integer
    //Dim a As Integer, b As Integer, d As Integer, e As Integer, f As Integer
    //Dim graf As Grh
    //
    //    If multi = False Then
    //        For a = 1 To Len(Texto)
    //            b = Asc(Mid$(Texto, a, 1))
    //            graf.GrhIndex = font_types(1).ascii_code(b)
    //            If (b <> 32) And (b <> 5) And (b <> 129) And (b <> 9) And (b <> 4) And (b <> 255) And (b <> 2) And (b <> 151) And (b <> 152) Then
    //                Engine_Text_Width = Engine_Text_Width + GrhData(GrhData(graf.GrhIndex).Frames(1)).pixelWidth '+ 1
    //            Else
    //                Engine_Text_Width = Engine_Text_Width + 4
    //            End If
    //        Next a
    //    Else
    //        e = 0
    //        f = 0
    //        For a = 1 To Len(Texto)
    //            b = Asc(Mid$(Texto, a, 1))
    //            graf.GrhIndex = font_types(1).ascii_code(b)
    //            If b = 32 Or b = 13 Then
    //                If e >= 20 Then 'reemplazar por lo que os plazca
    //                    f = f + 1
    //                    e = 0
    //                    d = 0
    //                Else
    //                    If b = 32 Then d = d + 4
    //                End If
    //            Else
    //                If graf.GrhIndex > 12 Then
    //                    d = d + GrhData(GrhData(graf.GrhIndex).Frames(1)).pixelWidth '+ 1
    //                    If d > Engine_Text_Width Then Engine_Text_Width = d
    //                End If
    //            End If
    //            e = e + 1
    //        Next a
    //    End If
    //End Function


    public static int getTextHeight(String text, boolean multi) {
        int retVal = 0;
        int b, e, f, d = 0;

        if (!multi) {
            return retVal;
        } else {
            e = 0;
            f = 0;

            for (int a = 0; a < text.length(); a++) {
                b = text.charAt(a);
                if(b == 32 || b == 13) {
                    if (e >= 20) {
                        f++;
                        e = 0;
                        d = 0;
                    } else {
                        if (b == 32) {
                            d += 4;
                        }
                    }
                }

                e++;
            }

            retVal = f * 14;
        }

        return retVal;
    }



    //Function Engine_Text_Height(Texto As String, Optional multi As Boolean = False) As Integer
    //Dim a As Integer, b As Integer, c  As Integer, d  As Integer, e As Integer, f As Integer
    //    If multi = False Then
    //        Engine_Text_Height = 0
    //    Else
    //        e = 0
    //        f = 0

    //        For a = 1 To Len(Texto)
    //            b = Asc(Mid$(Texto, a, 1))
    //            If b = 32 Or b = 13 Then
    //                If e >= 20 Then
    //                    f = f + 1
    //                    e = 0
    //                    d = 0
    //                Else
    //                    If b = 32 Then
    //                        d = d + 4
    //                    End If
    //                End If
    //            End If
    //            e = e + 1
    //        Next a

    //        Engine_Text_Height = f * 14
    //    End If
    //End Function
}
