# MUGEN SFF Parser

A Java project for reading MUGEN sprite archives in **SFF v1 and v2** formats. It exposes file headers, sprite/subfile metadata and palettes, with experimental decoding and sprite-viewing helpers.

## Build

Requires **JDK 11** and Maven:

```sh
mvn compile
```

The project uses Lombok, Guava and Apache Commons libraries. Maven resolves the versions declared in `pom.xml`; enable Lombok annotation processing in your IDE.

## Read a sprite archive

For an SFF v1 file:

```java
import com.hescha.parser.sff.parser.v1.SffV1Parser;
import java.io.File;

var sprites = new SffV1Parser().parse(new File("character-v1.sff"));
System.out.println(sprites.getHeader());
System.out.println(sprites.getSubfiles().size());
```

For an SFF v2 file:

```java
import com.hescha.parser.sff.parser.v2.SffV2Parser;
import java.io.File;

var sprites = new SffV2Parser().parse(new File("character-v2.sff"));
System.out.println(sprites.getSubfiles().size());
System.out.println(sprites.getPalettes().size());
```

Choose the parser matching the input format. Parsing archive metadata and decoding a sprite's pixel data are separate operations.

## Decoding scope

The v2 decoder dispatches raw, RLE8, RLE5 and LZ5 data. PNG-based encoding IDs are not implemented in the decoder and raise an unsupported-algorithm exception.

The LZ5 implementation uses a hard-coded `/mugenimages/` temporary-file location. Adapt that path and resource handling before relying on it in a portable application. The code is experimental; validate decoded output with your own sprite files.

## Source layout

- `model/v1`, `model/v2`: archive headers, items and palettes.
- `parser/v1`, `parser/v2`: binary format parsing.
- `parser/v2/decoder`: compression decoding helpers.
- `exception`: signature, version and decoding errors.
- `src/test/java/com/hescha/parser/`: manual sprite viewers and examples.

The viewer classes use local sample paths and are executable examples rather than an automated test suite.
