package com.sqshq.flattener;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        var mapper = new ObjectMapper();
        var result = new Flattener(mapper).flatten(System.in);
        System.out.println(result);
    }
}
