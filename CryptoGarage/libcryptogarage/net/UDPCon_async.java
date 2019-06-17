package CryptoGarage.libcryptogarage.net;

/**
 * Created by spamd on 07.01.2018.
 */

public class UDPCon_async implements NetCon_async{
    public UDPCon UDPCon_sync;

    public UDPCon_async(String url, int port){
        UDPCon_sync = UDPCon.instance(url, port);
    }

    public UDPCon_async(String url, int port, boolean newinstance){
        if(!newinstance) {
            new UDPCon_async(url, port);
        } else {
            UDPCon_sync = new UDPCon(url, port);
        }
    }

    @Override
    public void sendMessage(final String message, final ConReceiver callback){
        sendMessage(message, false, callback);
    }

    public void sendMessage(final String message, boolean broadcast, final ConReceiver callback){
        new Thread(() -> {
            final String out = UDPCon_sync.sendMessage(message, broadcast);
            if (!out.contains(NetCon.ERROR_HEADER)) {
                callback.onResponseReceived(out);
            } else {
                callback.onError(out.substring(out.indexOf(":") + 1));
            }
        }).start();
    }

    @Override
    public void close() {

    }
}
