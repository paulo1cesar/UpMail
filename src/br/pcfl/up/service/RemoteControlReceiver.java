
package br.pcfl.up.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import br.pcfl.up.Account;
import br.pcfl.up.Preferences;
import br.pcfl.up.Up;
import br.pcfl.up.remotecontrol.UpRemoteControl;


import static br.pcfl.up.remotecontrol.UpRemoteControl.*;

public class RemoteControlReceiver extends CoreReceiver {
    @Override
    public Integer receive(Context context, Intent intent, Integer tmpWakeLockId) {
        if (Up.DEBUG)
            Log.i(Up.LOG_TAG, "RemoteControlReceiver.onReceive" + intent);

        if (UpRemoteControl.K9_SET.equals(intent.getAction())) {
            RemoteControlService.set(context, intent, tmpWakeLockId);
            tmpWakeLockId = null;
        } else if (UpRemoteControl.K9_REQUEST_ACCOUNTS.equals(intent.getAction())) {
            try {
                Preferences preferences = Preferences.getPreferences(context);
                Account[] accounts = preferences.getAccounts();
                String[] uuids = new String[accounts.length];
                String[] descriptions = new String[accounts.length];
                for (int i = 0; i < accounts.length; i++) {
                    //warning: account may not be isAvailable()
                    Account account = accounts[i];

                    uuids[i] = account.getUuid();
                    descriptions[i] = account.getDescription();
                }
                Bundle bundle = getResultExtras(true);
                bundle.putStringArray(K9_ACCOUNT_UUIDS, uuids);
                bundle.putStringArray(K9_ACCOUNT_DESCRIPTIONS, descriptions);
            } catch (Exception e) {
                Log.e(Up.LOG_TAG, "Could not handle K9_RESPONSE_INTENT", e);
            }

        }

        return tmpWakeLockId;
    }

}
