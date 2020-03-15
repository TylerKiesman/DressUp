package com.example.dressup.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dressup.HomeActivity;
import com.example.dressup.R;
import com.example.dressup.TakePictureActivity;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {

    private TextView textTargetUri;
    ImageView targetImage;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    String currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private int dpToPx(int dp){
        return dp * (int) getActivity().getResources().getDisplayMetrics().density;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        LinearLayout.LayoutParams setupParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setupParams.setMargins(dpToPx(50), (height / 4)+ 5, dpToPx(50), 10);
        final Button button = root.findViewById(R.id.galBtn);
        final Button takePic = root.findViewById(R.id.takePic);
        button.setLayoutParams(setupParams);

        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 7);
            }});

        takePic.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                dispatchTakePictureIntent();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            galleryAddPic();
            //imageView.setImageBitmap(imageBitmap);
        }

        if (requestCode == 7 && resultCode == Activity.RESULT_OK){
            if (data == null) {
                //Display an error
                return;
            }
            try {
                String file = data.getDataString();
                Pattern pattern = Pattern.compile("\\/(?:.(?!\\/))+$");
                Matcher matcher = pattern.matcher(file);
                matcher.find();
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                byte[] bytes = ByteStreams.toByteArray(inputStream);
                FileOutputStream fos = getContext().openFileOutput(matcher.group(0).replace("/", ""), getContext().MODE_PRIVATE);
                fos.write(bytes);
            } catch (Exception e) { System.out.println(e); }
        }

//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            Uri targetUri = data.getData();
//            textTargetUri.setText(targetUri.toString());
//            Bitmap bitmap;
//            try {
//                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
//                targetImage.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

        Intent myIntent = new Intent(getActivity(), HomeActivity.class);
        startActivity(myIntent);
    }
}
