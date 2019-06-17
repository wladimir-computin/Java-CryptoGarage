package CryptoGarage;

import CryptoGarage.libcryptogarage.protocol.*;

public class CryptoGarage{
	public static void main(String[] args){
		String command = "lockDoor";

		CryptCon cc = new CryptCon(Config.key, Config.static_ip, true);
		cc.sendMessageEncrypted(command, new CryptConReceiver() {
			@Override
			public void onSuccess(Content response) {
				System.out.println(response);
			}

			@Override
			public void onFail() {

			}

			@Override
			public void onFinished() {
				System.exit(0);
			}

			@Override
			public void onProgress(String sprogress, int iprogress) {
				System.out.println(sprogress);
			}
		});

	}
}
