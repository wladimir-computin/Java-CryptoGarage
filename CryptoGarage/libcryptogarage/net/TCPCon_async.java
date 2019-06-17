package CryptoGarage.libcryptogarage.net;

/**
 * Created by spamd on 07.01.2018.
 */

public class TCPCon_async implements NetCon_async {
    public TCPCon tcpCon_sync;

    public TCPCon_async(String url, int port){
        tcpCon_sync = TCPCon.instance(url, port);
    }

    public TCPCon_async(String url, int port, boolean newinstance){
        if(!newinstance) {
            new TCPCon_async(url, port);
        } else {
            tcpCon_sync = new TCPCon(url, port);
        }
    }

    @Override
    public void sendMessage(final String message, final ConReceiver callback){
        new Thread(() -> {
            final String out = tcpCon_sync.sendMessage(message);
            if (!out.contains(NetCon.ERROR_HEADER)) {
                callback.onResponseReceived(out);
            } else {
                callback.onError(out.substring(out.indexOf(":") + 1));
            }
        }).start();
    }

    @Override
    public void close(){
        new Thread(() -> {
            tcpCon_sync.close();
        }).start();
    }
}
