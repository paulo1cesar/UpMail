
package br.pcfl.up;

import android.text.util.Rfc822Tokenizer;
import android.widget.AutoCompleteTextView.Validator;

public class EmailAddressValidator implements Validator {
    public CharSequence fixText(CharSequence invalidText) {
        return "";
    }

    public boolean isValid(CharSequence text) {
        return Rfc822Tokenizer.tokenize(text).length > 0;
    }

    public boolean isValidAddressOnly(CharSequence text) {
        return br.pcfl.up.helper.Regex.EMAIL_ADDRESS_PATTERN.matcher(text).matches();
    }
}
