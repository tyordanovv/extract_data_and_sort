package main;

import utils.IOHandler;

public class Application {
    public static void main(String[] args) {
        if (args.length != 1){
            System.err.println("Please specify PATH to input file!");
            System.exit(-1);
        }
        IOHandler io = new IOHandler();
        io.fetchData(args[0]);
    }
}
