package com.hescha.parser.wrapper;

import com.google.common.primitives.Bytes;
import com.hescha.parser.sff.model.v2.SffV2Item;

//typedef struct _PcxHeader
//{
//BYTE	Identifier;        /* PCX Id Number (Always 0x0A) */
//BYTE	Version;           /* Version Number */
//BYTE	Encoding;          /* Encoding Format */
//BYTE	BitsPerPixel;      /* Bits per Pixel */
//WORD	XStart;            /* Left of image */
//WORD	YStart;            /* Top of Image */
//WORD	XEnd;              /* Right of Image
//WORD	YEnd;              /* Bottom of image */
//WORD	HorzRes;           /* Horizontal Resolution */
//WORD	VertRes;           /* Vertical Resolution */
//BYTE	Palette[48];       /* 16-Color EGA Palette */
//BYTE	Reserved1;         /* Reserved (Always 0) */
//BYTE	NumBitPlanes;      /* Number of Bit Planes */
//WORD	BytesPerLine;      /* Bytes per Scan-line */
//WORD	PaletteType;       /* Palette Type */
//WORD	HorzScreenSize;    /* Horizontal Screen Size */
//WORD	VertScreenSize;    /* Vertical Screen Size */
//BYTE	Reserved2[54];     /* Reserved (Always 0) */
//} PCXHEAD;
public class PcxHeaderTemplate {
    private static final byte[] HEADER_TEMPLATE = new byte[]{10, 5, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 120, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private static final byte[] staticFooter = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] pcxSignature = {
            10, 5, 0, 8, //headers
            0, 0, //xmin
            0, 0 //ymin
    };
    private static final byte[] staticSection = {
            0, 0,
            0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1
    };

    public static byte[] getPcxHeaderTemplate(SffV2Item item) {
        byte[] widthWord = toBytes(item.getWidth() - 1);
        byte[] heightWord = toBytes(item.getHeight() - 1);
        byte[] pixelPerLineWord = toBytes(item.getWidth());
        return Bytes.concat(
                pcxSignature,
                widthWord,
                heightWord,
                staticSection,
                pixelPerLineWord,
                staticFooter);
    }

    private static byte[] toBytes(int s) {
        return new byte[]{(byte) (s & 0x00FF), (byte) ((s & 0xFF00) >> 8)};
    }

}
