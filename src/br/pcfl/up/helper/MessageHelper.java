package br.pcfl.up.helper;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;

import br.pcfl.up.Account;
import br.pcfl.up.R;
import br.pcfl.up.Up;
import br.pcfl.up.activity.FolderInfoHolder;
import br.pcfl.up.activity.MessageInfoHolder;
import br.pcfl.up.mail.Address;
import br.pcfl.up.mail.Flag;
import br.pcfl.up.mail.Message;
import br.pcfl.up.mail.MessagingException;
import br.pcfl.up.mail.Message.RecipientType;

public class MessageHelper {

    private static MessageHelper sInstance;

    public synchronized static MessageHelper getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new MessageHelper(context);
        }
        return sInstance;
    }

    private Context mContext;

    private MessageHelper(final Context context) {
        mContext = context;
    }

    public void populate(final MessageInfoHolder target, final Message message,
                         final FolderInfoHolder folder, final Account account) {
        final Contacts contactHelper = Up.showContactName() ? Contacts.getInstance(mContext) : null;
        try {
            target.message = message;
            target.compareArrival = message.getInternalDate();
            target.compareDate = message.getSentDate();
            if (target.compareDate == null) {
                target.compareDate = message.getInternalDate();
            }

            target.folder = folder;

            target.read = message.isSet(Flag.SEEN);
            target.answered = message.isSet(Flag.ANSWERED);
            target.forwarded = message.isSet(Flag.FORWARDED);
            target.flagged = message.isSet(Flag.FLAGGED);

            Address[] addrs = message.getFrom();

            if (addrs.length > 0 &&  account.isAnIdentity(addrs[0])) {
                CharSequence to = Address.toFriendly(message .getRecipients(RecipientType.TO), contactHelper);
                target.compareCounterparty = to.toString();
                target.sender = new SpannableStringBuilder(mContext.getString(R.string.message_to_label)).append(to);
            } else {
                target.sender = Address.toFriendly(addrs, contactHelper);
                target.compareCounterparty = target.sender.toString();
            }

            if (addrs.length > 0) {
                target.senderAddress = addrs[0].getAddress();
            } else {
                // a reasonable fallback "whomever we were corresponding with
                target.senderAddress = target.compareCounterparty;
            }




            target.uid = message.getUid();

            target.account = account.getUuid();
            target.uri = "email://messages/" + account.getAccountNumber() + "/" + message.getFolder().getName() + "/" + message.getUid();

        } catch (MessagingException me) {
            Log.w(Up.LOG_TAG, "Unable to load message info", me);
        }
    }

    public CharSequence getDisplayName(Account account, Address[] fromAddrs, Address[] toAddrs) {
        final Contacts contactHelper = Up.showContactName() ? Contacts.getInstance(mContext) : null;

        CharSequence displayName;
        if (fromAddrs.length > 0 && account.isAnIdentity(fromAddrs[0])) {
            CharSequence to = Address.toFriendly(toAddrs, contactHelper);
            displayName = new SpannableStringBuilder(
                    mContext.getString(R.string.message_to_label)).append(to);
        } else {
            displayName = Address.toFriendly(fromAddrs, contactHelper);
        }

        return displayName;
    }

    public boolean toMe(Account account, Address[] toAddrs) {
        for (Address address : toAddrs) {
            if (account.isAnIdentity(address)) {
                return true;
            }
        }
        return false;
    }
}
