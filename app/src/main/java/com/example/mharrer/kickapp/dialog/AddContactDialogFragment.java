package com.example.mharrer.kickapp.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.fragments.CoachDetailsFragment;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by mharrer on 12.11.14.
 */
public class AddContactDialogFragment extends android.support.v4.app.DialogFragment {

    private CoachDetailsFragment parent;

    public AddContactDialogFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public AddContactDialogFragment(final CoachDetailsFragment parent) {
        super();
        this.parent = parent;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<ContactData> contacts = getAllContacts();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_add_contact);

        ArrayAdapter adapter = new ArrayAdapter<ContactData>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, contacts);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactData selectedContact = contacts.get(which);
                parent.setNewCoachData(selectedContact);
            }
        });
        return builder.create();

    }


    private List<ContactData> getAllContacts() {

        List<ContactData> possibleCoaches = Lists.newArrayList();

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                String familyName = StringUtils.EMPTY;
                String givenName = StringUtils.EMPTY;

                // firstname and lastname:
                String whereName = ContactsContract.Data.MIMETYPE + " = ?";
                String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                Cursor names = cr.query(ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.MIMETYPE + " = ? and " + ContactsContract.Data.CONTACT_ID
                                + " = " + id, whereNameParams, null);

                if (names.moveToNext()) {
                    givenName = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    familyName = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                }
                names.close();

                // Create query to use CommonDataKinds classes to fetch emails
                String email = StringUtils.EMPTY;
                Cursor emails = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = " + id, null, null);
                if (emails.moveToNext()) {

                    // This would allow you get several email addresses
                    email = emails
                            .getString(emails
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();

                // Create query to use CommonDataKinds classes to fetch phonenumber
                String phonenumber = StringUtils.EMPTY;
                Cursor phonenumbers = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + id, null, null);
                while (phonenumbers.moveToNext()) {

                    // This would allow you get several email addresses
                    phonenumber = phonenumbers
                            .getString(phonenumbers
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                }
                phonenumbers.close();

                // photo
                String photoUri = StringUtils.EMPTY;
                Cursor photos = cr
                        .query(ContactsContract.Data.CONTENT_URI,
                                null,
                                ContactsContract.Data.CONTACT_ID
                                        + "="
                                        + id
                                        + " AND "

                                        + ContactsContract.Data.MIMETYPE
                                        + "='"
                                        + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                        + "'", null, null);

                if (photos.moveToNext()) {
                    photoUri = photos.getString(photos.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
                }
                photos.close();

                if (StringUtils.isNotBlank(givenName) && StringUtils.isNotBlank(familyName)) {
                    ContactData possibleCoach = new ContactData();
                    possibleCoach.setFirstName(givenName);
                    possibleCoach.setLastName(familyName);
                    possibleCoach.setPhoneNumber(phonenumber);
                    possibleCoach.setEmail(email);
                    possibleCoach.setPhotoUri(photoUri);
                    possibleCoaches.add(possibleCoach);
                }

            }
        }
        cur.close();
        return possibleCoaches;
    }

    public static class ContactData {
        private String firstName;
        private String lastName;
        private String photoUri;
        private String email;
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhotoUri() {
            return photoUri;
        }

        public void setPhotoUri(String photoUri) {
            this.photoUri = photoUri;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }

}
