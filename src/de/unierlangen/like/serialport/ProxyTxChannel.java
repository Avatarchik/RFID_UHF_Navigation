package de.unierlangen.like.serialport;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

/**
 * ProxyTxChannel is a representative for the connection type Tx (Serial port,
 * Bluetooth or Emulator). Nobody knows, what is current connection type. They
 * use Proxy instead.
 * 
 * @author lyavinskova
 * 
 */
public class ProxyTxChannel implements TxChannel, OnSharedPreferenceChangeListener {

    private static final String COMM_TYPE = "COMM_TYPE";
    private TxChannel activeTxChannel;

    /**
     * User can choose a connection type in preferences. Proxy becomes chosen
     * connection type (TxChannel).
     * 
     * @param txChannels
     *            are mapped to their names
     * @param sp
     *            SharedPreferences containing {@link ProxyTxChannel#COMM_TYPE}
     */
    public ProxyTxChannel(Map<String, TxChannel> txChannels, SharedPreferences sp) {
        String activeTxChannelName = sp.getString(COMM_TYPE, "emulation");
        activeTxChannel = txChannels.get(activeTxChannelName);
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    public void sendString(String stringToSend) {
        activeTxChannel.sendString(stringToSend);
    }

    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        if (key.equals(COMM_TYPE)) {
            String activeTxChannelName = sp.getString(COMM_TYPE, "emulation");
            Log.d("ProxyTxChannel", "activeTxChannelName = " + activeTxChannelName);
        }

    }
}