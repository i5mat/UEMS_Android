package my.edu.utem.uemsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;


public class Attendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        if (!attendance())
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            attendance();
        else
        {
            Toast.makeText(this, "This application requires camera to work.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean attendance()
    {
        boolean allowed = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        if (allowed)
        {
            ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

            listenableFuture.addListener(() -> process(listenableFuture), ContextCompat.getMainExecutor(this));
        }

        return allowed;
    }

    private void process(ListenableFuture<ProcessCameraProvider> listenableFuture)
    {
        try
        {
            ProcessCameraProvider cameraProvider = listenableFuture.get();
            CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
            PreviewView prvScan = findViewById(R.id.prvScan);

            cameraProvider.unbindAll();

            Preview preview = new Preview.Builder().build();
            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
            BarcodeScanner barcodeScanner = BarcodeScanning.getClient(new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

            getLifecycle().addObserver(barcodeScanner);
            preview.setSurfaceProvider(prvScan.getSurfaceProvider());
            //imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> process(imageProxy, barcodeScanner));
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void process(ImageProxy imageProxy, BarcodeScanner barcodeScanner)
    {
//        Image image = imageProxy.getImage();
//
//        if (image != null)
//            barcodeScanner.process(InputImage.fromMediaImage(Image, imageProxy.getImageInfo().getRotationDegrees())).addOnCanceledListener(barcode -> ()).addOnCanceledListener(task -> imageProxy.close());
    }
}