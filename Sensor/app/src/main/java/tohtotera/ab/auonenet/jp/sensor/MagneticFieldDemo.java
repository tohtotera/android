package tohtotera.ab.auonenet.jp.sensor;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MagneticFieldDemo extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private MySurfaceView view;
	private float max;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		view = new MySurfaceView(this);
		setContentView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		if (sensors.size() > 0) {
			Sensor sensor = sensors.get(0);
			max = sensor.getMaximumRange();
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		view.onValueChanged(event.values);
	}

	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
		public MySurfaceView(Context context) {
			super(context);
			getHolder().addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			onValueChanged(new float[3]);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}

		void onValueChanged(float[] values) {
			Canvas canvas = getHolder().lockCanvas();
			if (canvas != null) {
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(Color.BLUE);
				paint.setTextSize(24);

				canvas.drawColor(Color.WHITE);
				for (int i = 0; i < values.length; i++) {
					canvas.drawText("values[" + i + "]: " + values[i], 0, paint.getTextSize() * (i + 1), paint);
				}
				canvas.drawText("max: " + max, 0, paint.getTextSize() * 5, paint);
				getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
}
