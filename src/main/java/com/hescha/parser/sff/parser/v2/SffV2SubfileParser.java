package com.hescha.parser.sff.parser.v2;

import com.hescha.parser.sff.model.v2.SffV2Header;
import com.hescha.parser.sff.model.v2.SffV2Item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
|--------------------------------------------------*\
Sprite node header 2.00 28 bytes

dec  hex  size   meaning
0    0     2   groupno
2    2     2   itemno
4    4     2   width
6    6     2   height
8    8     2   axisx
10    A     2   axisy
12    C     2   Index number of the linked sprite (if linked)
14    E     1   fmt
15    F     1   coldepth
16   10     4   offset into ldata or tdata
20   14     4   Sprite data length (0: linked)
24   18     2   palette index
26   1A     2   flags
fmt

0 raw
1 invalid (no use)
2 RLE8
3 RLE5
4 LZ5
flags

0    unset: literal (use ldata); set: translate (use tdata; decompress on load)
1-15 unused

Compression Formats
Compression formats are consistent across SFF versions. The first 4 bytes of each compressed block is an integer representing the length of the data after decompression.

RLE8

Simple run-length encoding for 8-bit-per-pixel bitmaps. Any byte with bits 6 and 7 set to 1 and 0 respectively is an RLE control packet. All other bytes represent the value of the pixel. RLE packet (1 byte)

      bits 0-5: run length
      bits 6-7: run marker (01)
Pseudocode to decode an RLE8 chunk:

      one_byte = read(1 byte)
      if (one_byte & 0xC0) == 0x40, then
        color = read(1 byte)
        for run_count from 0 to (val & 0x3F) - 1, do
          output(color)
      else
        output(one_byte)
RLE5 RLE5 is a run-length encoding used for the compression of 5-bit-per-pixel bitmaps. RLE5 is a hybrid of two run-length encoding algorithms. The first allows encoding of long runs of color 0 pixels, the second is a 3-bit-RL/5-bit-color run-length algorithm.

RLE5 packet (2 bytes)

      byte 0         : run length
      byte 1 bit 7   : color bit; .2-.7
      byte 1 bits 0-6: data length
Pseudocode to decode an RLE5 chunk:

       RLE5packet = read(2 bytes)
       if RLE5packet.color_bit is 1, then
         color = read(1 byte)
       else
         color = 0
       for run_count from 0 to RLE5packet.run_length, do
         output(color)
       // Decode 3RL/5VAL
       for bytes_processed from 0 to RLE5packet.data_length - 1, do
         one_byte = read(1 byte)
         color = one_byte & 0x1F
         run_length = one_byte >> 5
         for run_count from 0 to run_length, do
           output(color)
LZ5 LZ5 is a run-length encoding used for the compression of 5-bit-per-pixel bitmaps. It is more efficient than RLE5 but is more complex to encode.
\*--------------------------------------------------------------------------*/
public class SffV2SubfileParser {
//    private int subfileOffsetStartpoint = 512;

    public List<SffV2Item> parse(SffV2Header header, File file) throws IOException {
        List<SffV2Item> list = new ArrayList<>();
        return list;
    }
}
