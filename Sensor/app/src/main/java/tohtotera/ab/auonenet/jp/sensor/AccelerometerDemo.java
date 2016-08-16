package tohtotera.ab.auonenet.jp.sensor;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class AccelerometerDemo extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private MySurfaceView view;

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
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
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
		private Bitmap bitmap;
		private float x, y;

		public MySurfaceView(Context context) {
			super(context);
			getHolder().addCallback(this);
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			x = (getWidth() - bitmap.getWidth()) / 2;
			y = (getHeight() - bitmap.getHeight()) / 2;
			onValueChanged(new float[3]);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}

		void onValueChanged(float[] values) {
			x -= values[0];
			y += values[1];

			Canvas canvas = getHolder().lockCanvas();
			if (canvas != null) {
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(Color.BLUE);
				paint.setTextSize(24);

				canvas.drawColor(Color.WHITE);
				canvas.drawBitmap(bitmap, x, y, null);
				for (int i = 0; i < values.length; i++) {
					canvas.drawText("values[" + i + "]: " + values[i], 0, paint.getTextSize() * (i + 1), paint);
				}
				getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
}