package CryptoGarage.libcryptogarage.net;

import CryptoGarage.Config;

public abstract class NetCon {

    public static final String ERROR_HEADER = "CONFAIL:";

    private final String BEGIN;
    private final String END;
    private final int MAX_MESSAGE_LEN;

    protected String ip = "";
    protected int port;

    public abstract String sendMessage(String message);

    public NetCon(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.BEGIN = Config.message_begin;
        this.END = Config.message_end;
        this.MAX_MESSAGE_LEN = Config.max_message_len;
    }

    protected String packMessage(String in){
        return BEGIN + in + END;
    }

    protected String unpackMessage(String in){
        String msg = "";
        int startIndex = in.indexOf(BEGIN);
        int endIndex = in.indexOf(END, startIndex);

        if ((startIndex != -1) && (endIndex != -1) && (endIndex - startIndex <= MAX_MESSAGE_LEN)){
            msg = in.substring(startIndex + BEGIN.length(), endIndex);
        }
        return msg;
    }
}
