package CryptoGarage.libcryptogarage.net;

/**
 * Created by spamd on 07.01.2018.
 */

public interface ConReceiver {
    void onResponseReceived(String responseData);

    void onError(String reason);
}
