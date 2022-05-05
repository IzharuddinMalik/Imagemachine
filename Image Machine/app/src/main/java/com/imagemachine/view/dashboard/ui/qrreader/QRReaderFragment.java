package com.imagemachine.view.dashboard.ui.qrreader;

import static java.sql.DriverManager.println;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.imagemachine.databinding.FragmentQrcodeBinding;
import com.imagemachine.view.detailmachine.DetailMachineActivity;

import java.util.ArrayList;
import java.util.Objects;

public class QRReaderFragment extends Fragment {
    private QRReaderViewModel qrReaderViewModel;
    private FragmentQrcodeBinding binding;
    private CodeScanner mCodeScanner;
    private CodeScannerView mCodeScannerView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        qrReaderViewModel =
                new ViewModelProvider(this).get(QRReaderViewModel.class);

        binding = FragmentQrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mCodeScannerView = binding.scanner;

        mCodeScanner = new CodeScanner(Objects.requireNonNull(getActivity()), mCodeScannerView);
        mCodeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(() -> {

            Intent intent = new Intent(getActivity(), DetailMachineActivity.class);
            intent.putExtra("machineqr", result.getText());
            intent.putExtra("from", "qr");
            startActivity(intent);
                }
        ));

        mCodeScanner.startPreview();

        mCodeScannerView.setOnClickListener(view -> mCodeScanner.startPreview());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }
    }

    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
