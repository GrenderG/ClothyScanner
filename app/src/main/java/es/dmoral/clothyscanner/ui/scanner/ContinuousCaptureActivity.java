package es.dmoral.clothyscanner.ui.scanner;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.clothyscanner.R;
import es.dmoral.clothyscanner.udp.UdpClient;
import es.dmoral.toasty.Toasty;

/**
 * This file is part of ClothyScanner.
 *
 * ClothyScanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClothyScanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClothyScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ContinuousCaptureActivity extends AppCompatActivity implements UdpClient.UdpCallback {
    public static final String IP_ARG = "IP_ARG";
    @BindView(R.id.capture_view) DecoratedBarcodeView captureView;
    private Vibrator vibrator;
    private String ip;
    private UdpClient udpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_capture);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initArgs();
        udpClient = new UdpClient(this, ip);

        setupViews();
        setListeners();
    }

    private void initArgs() {
        if (getIntent().hasExtra(IP_ARG)) {
            ip = getIntent().getStringExtra(IP_ARG);
        }
    }

    private void setupViews() {
        ButterKnife.bind(this);
    }

    private void setListeners() {
        captureView.decodeContinuous(new BarcodeCallback() {
            public static final long DELAY_BETWEEN_SCANS = 4000;
            private long lastTimestamp = 0;

            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    if (System.currentTimeMillis() - lastTimestamp < DELAY_BETWEEN_SCANS) {
                        return;
                    }
                    lastTimestamp = System.currentTimeMillis();
                    vibrator.vibrate(50);
                    udpClient.sendMessage(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // unused
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return captureView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.success(ContinuousCaptureActivity.this, getString(R.string.data_received_msg)).show();
            }
        });
    }

    @Override
    public void onError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.error(ContinuousCaptureActivity.this, getString(R.string.error_msg)).show();
            }
        });
    }
}
