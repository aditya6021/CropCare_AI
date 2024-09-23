package com.example.cropcareai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest; // Import statement for the Manifest permissions
import android.content.pm.PackageManager; // Import statement for PackageManager

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cropcareai.ml.Finalmodel;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class predict_fragment extends Fragment {
    Button predict;
    ImageButton camera, gallery;
    ImageView imgview;
    TextView textView,progressper;
    ProgressBar progressBar;
    int imageSize = 256;
    Bitmap selectedImage;
    ActivityResultLauncher<Intent> imagePickLauncher;
    public predict_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_predict_fragment, container, false);
        camera = view.findViewById(R.id.btn_take_picture);
        gallery = view.findViewById(R.id.btn_select_image);
        imgview = view.findViewById(R.id.imageView);
        textView = view.findViewById(R.id.textView_results);
        predict = view.findViewById(R.id.btn_predict_image);
        progressBar=view.findViewById(R.id.prediction_progress);
        progressper=view.findViewById(R.id.prediction_percentage);


        progressBar.setProgress(0,true);
        progressper.setText("0%");
        selectedImage=null;
        camera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1001);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });
        gallery.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent,1002);
        });
        predict.setOnClickListener(v -> {
            if (selectedImage != null) {
                predictresults();  // Call the method to run the prediction
            } else {
                textView.setText("No image selected!");
            }
        });
        return view;
    }
    private void processCapturedImage(Bitmap image) {
        if (image != null) {
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imgview.setImageBitmap(image);
            selectedImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);  // Set the selected image
        } else {
            textView.setText("Image capture failed. Please try again.");
        }
    }
    private void processGalleryImage(Uri imageUri) {
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image != null) {
            imgview.setImageBitmap(image);
            selectedImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);  // Set the selected image
        }
    }
    private void predictresults() {
        if (selectedImage != null) {
            classifyImage(selectedImage);
        } else {
            textView.setText("No image available for prediction.");
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            progressBar.setProgress(0);
            progressper.setText("0%");

            textView.setText("Press Analyse Image To get Results");
            if (requestCode == 1001) {
                // Camera image
                Bitmap image = (Bitmap) data.getExtras().get("data");
                processCapturedImage(image);
            } else if (requestCode == 1002) {
                // Gallery image
                Uri imageUri = data.getData();
                processGalleryImage(imageUri);
            }
        } else {
            textView.setText("Image selection failed.");
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data); // Call only if necessary
//
//        if (resultCode == android.app.Activity.RESULT_OK) {
//            if (requestCode == 3) {
//                // Capture image from camera
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                int dimension = Math.min(image.getWidth(), image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//                imgview.setImageBitmap(image);
//
//                // Resize image
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image); // Call the classification method
//            } else {
//                // Select image from gallery
//                Uri dat = data.getData();
//                Bitmap image = null;
//                try {
//                    // Use requireActivity() to get the content resolver in Fragment
//                    image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), dat);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imgview.setImageBitmap(image);
//
//                // Resize image
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image); // Call the classification method
//            }
//        }
//    }




    public void classifyImage(Bitmap image) {

            try {
                Finalmodel model = Finalmodel.newInstance(requireContext().getApplicationContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int[] intValues = new int[imageSize * imageSize];
                image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
                int pixel = 0;
                //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                for (int i = 0; i < imageSize; i++) {
                    for (int j = 0; j < imageSize; j++) {
                        int val = intValues[pixel++]; // RGB
                        byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                        byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                        byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                    }
                }

                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                Finalmodel.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                float[] confidences = outputFeature0.getFloatArray();
                // find the index of the class with the biggest confidence.
                for (int i = 0; i < confidences.length; i++) {
                   System.out.println(confidences[i]);
                }
                int maxPos = 0;
                float maxConfidence = 0;
                for (int i = 0; i < confidences.length; i++) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i];
                        maxPos = i;
                    }
                }
                String[] classes = {"The crop may be affected by early blight.", "The crop may be affected by late blight.", "The crop appears to be healthy!"};
                textView.setText(classes[maxPos]);
                float percent=maxConfidence*100;
                int progress=(int)Math.round(percent);
                float roundedNum = (float) (Math.round(percent * 10) / 10.0);
                progressBar.setProgress(progress,true);
                progressper.setText(roundedNum+"%");
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }



    }
}
