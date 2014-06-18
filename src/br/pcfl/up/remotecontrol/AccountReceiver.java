package br.pcfl.up.remotecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

class AccountReceiver extends BroadcastReceiver {
    UpAccountReceptor receptor = null;

    protected AccountReceiver(UpAccountReceptor nReceptor) {
        receptor = nReceptor;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (UpRemoteControl.K9_REQUEST_ACCOUNTS.equals(intent.getAction())) {
            Bundle bundle = getResultExtras(false);
            if (bundle == null) {
                Log.w(UpRemoteControl.LOG_TAG, "Response bundle is empty");
                return;
            }
            receptor.accounts(bundle.getStringArray(UpRemoteControl.K9_ACCOUNT_UUIDS), bundle.getStringArray(UpRemoteControl.K9_ACCOUNT_DESCRIPTIONS));
        }
    }

}
