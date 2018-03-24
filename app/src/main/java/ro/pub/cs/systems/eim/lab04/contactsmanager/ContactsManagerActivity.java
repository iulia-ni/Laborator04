package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;

    Button showAdditionalDetailsBtn;
    Button saveBtn;
    Button cancelBtn;
    ButtonClickListener buttonClickListener = new ButtonClickListener();

    private LinearLayout additionalFieldsContainer;
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_show_additional_fields:
                    switch (additionalFieldsContainer.getVisibility()) {
                        case View.VISIBLE:
                            additionalFieldsContainer.setVisibility(View.INVISIBLE);
                            showAdditionalDetailsBtn.setText(R.string.show_additional_fields);
                            break;

                        case View.INVISIBLE:
                            additionalFieldsContainer.setVisibility(View.VISIBLE);
                            showAdditionalDetailsBtn.setText(R.string.hide_additional_details);
                            break;
                    }
                    break;

                case R.id.save_button:
                    String name = nameEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    String jobTitle = jobTitleEditText.getText().toString();
                    String company = companyEditText.getText().toString();
                    String website = websiteEditText.getText().toString();
                    String im = imEditText.getText().toString();

                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }
                    if (phone != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    }
                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }
                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }
                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }
                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }
                    ArrayList<ContentValues> contactData = new ArrayList<>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }
                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
//                    startActivity(intent);
                    startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
                    break;

                case R.id.cancel_button:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        showAdditionalDetailsBtn = findViewById(R.id.button_show_additional_fields);
        saveBtn = findViewById(R.id.save_button);
        cancelBtn = findViewById(R.id.cancel_button);

        nameEditText = (EditText)findViewById(R.id.edit_text_name);
        phoneEditText = (EditText)findViewById(R.id.edit_text_phone);
        emailEditText = (EditText)findViewById(R.id.edit_text_email);
        addressEditText = (EditText)findViewById(R.id.edit_text_address);
        jobTitleEditText = (EditText)findViewById(R.id.job_title_edit_text);
        companyEditText = (EditText)findViewById(R.id.company_edit_text);
        websiteEditText = (EditText)findViewById(R.id.website_edit_text);
        imEditText = (EditText)findViewById(R.id.im_edit_text);

        showAdditionalDetailsBtn.setOnClickListener(buttonClickListener);
        saveBtn.setOnClickListener(buttonClickListener);
        cancelBtn.setOnClickListener(buttonClickListener);

        additionalFieldsContainer = (LinearLayout)findViewById(R.id.additional_fields_container);
        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
