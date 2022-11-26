package com.hescha.parser;

import com.hescha.parser.sff.model.v2.SffV2File;
import com.hescha.parser.sff.parser.v2.SffV2Parser;

import java.io.File;

public class KiraTEst {
    public static void main(String[] args) throws Exception{
        String filename = "C:\\Users\\Administrator\\Desktop\\SM_Hashirama.sff";
        SffV2Parser parser = new SffV2Parser();
        SffV2File parse = parser.parse(new File(filename));

        System.out.println(parse);
    }
}
