package com.example.androidresources;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Class<android.R.drawable> klass = android.R.drawable.class;

		final DrawableAdapter constants = new DrawableAdapter();
		for (final Field field : klass.getDeclaredFields()) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				constants.add(field);
			}
		}

		final ListView listView = (ListView)findViewById(R.id.main_list);
		listView.setAdapter(constants);
	}

	private class DrawableAdapter extends ArrayAdapter<Field> {
		DrawableAdapter() {
			super(MainActivity.this, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.drawable_info, null);
			}

			final ImageView imageView = (ImageView)convertView.findViewById(R.id.drawable_image);
			final TextView  textView  = (TextView)convertView.findViewById(R.id.drawable_name);

			final Field field = getItem(position);

			// get the integer value of android.R.drawable.*
			int resId = 0;
			try {
				resId = field.getInt(null);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "failed", e);
			} catch (IllegalAccessException e) {
				Log.e(TAG, "failed", e);
			}

			imageView.setImageResource(resId);
			textView.setText(field.getName());

			return convertView;
		}
	}

}
