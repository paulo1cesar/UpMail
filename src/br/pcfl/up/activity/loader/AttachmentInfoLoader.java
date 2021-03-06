package br.pcfl.up.activity.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import br.pcfl.up.Up;
import br.pcfl.up.activity.misc.Attachment;
import br.pcfl.up.mail.internet.MimeUtility;


import java.io.File;

/**
 * Loader to fetch metadata of an attachment.
 */
public class AttachmentInfoLoader  extends AsyncTaskLoader<Attachment> {
    private final Attachment mAttachment;

    public AttachmentInfoLoader(Context context, Attachment attachment) {
        super(context);
        mAttachment = attachment;
    }

    @Override
    protected void onStartLoading() {
        if (mAttachment.state == Attachment.LoadingState.METADATA) {
            deliverResult(mAttachment);
        }

        if (takeContentChanged() || mAttachment.state == Attachment.LoadingState.URI_ONLY) {
            forceLoad();
        }
    }

    @Override
    public Attachment loadInBackground() {
        Uri uri = mAttachment.uri;
        String contentType = mAttachment.contentType;

        long size = -1;
        String name = null;

        ContentResolver contentResolver = getContext().getContentResolver();

        Cursor metadataCursor = contentResolver.query(
                uri,
                new String[] { OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE },
                null,
                null,
                null);

        if (metadataCursor != null) {
            try {
                if (metadataCursor.moveToFirst()) {
                    name = metadataCursor.getString(0);
                    size = metadataCursor.getInt(1);
                }
            } finally {
                metadataCursor.close();
            }
        }

        if (name == null) {
            name = uri.getLastPathSegment();
        }

        String usableContentType = contentType;
        if ((usableContentType == null) || (usableContentType.indexOf('*') != -1)) {
            usableContentType = contentResolver.getType(uri);
        }
        if (usableContentType == null) {
            usableContentType = MimeUtility.getMimeTypeByExtension(name);
        }

        if (size <= 0) {
            String uriString = uri.toString();
            if (uriString.startsWith("file://")) {
                Log.v(Up.LOG_TAG, uriString.substring("file://".length()));
                File f = new File(uriString.substring("file://".length()));
                size = f.length();
            } else {
                Log.v(Up.LOG_TAG, "Not a file: " + uriString);
            }
        } else {
            Log.v(Up.LOG_TAG, "old attachment.size: " + size);
        }
        Log.v(Up.LOG_TAG, "new attachment.size: " + size);

        mAttachment.contentType = usableContentType;
        mAttachment.name = name;
        mAttachment.size = size;
        mAttachment.state = Attachment.LoadingState.METADATA;

        return mAttachment;
    }
}
