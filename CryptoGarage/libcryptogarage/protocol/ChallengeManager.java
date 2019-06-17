package CryptoGarage.libcryptogarage.protocol;


import CryptoGarage.Config;

import java.util.Timer;
import java.util.TimerTask;

public class ChallengeManager {
    private static ChallengeManager instance;

    private String challenge = "";
    private int challenge_validity_timeout;

    private Timer timer;

    public static ChallengeManager instance(){
        if(instance == null){
            instance = new ChallengeManager();
        }
        return instance;
    }

    private ChallengeManager(){
        challenge_validity_timeout = Config.challenge_validity_timeout - 1000;
        timer = new Timer();
    }

    public void setChallenge(String last_challenge_response_b64){
        challenge = last_challenge_response_b64;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                resetChallenge();
            }
        }, challenge_validity_timeout);
    }

    public String getCurrentChallenge(){
        return challenge;
    }

    public void resetChallenge(){
        timer.cancel();
        challenge = "";
    }
}
