/*
 * Copyright (C) 2015 Daniel Nilsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.danielnilsson9.colorpickerview.preference;

import com.github.danielnilsson9.colorpickerview.R;
import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerPreferenceDialog;
import com.github.danielnilsson9.colorpickerview.view.ColorPanelView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

public class ColorPreference extends Preference  implements ColorPickerPreferenceDialog.ColorPickerDialogListener{


	/*public interface OnShowDialogListener {
		public void onShowColorPickerDialog(String title, int currentColor);
	}*/
	
	//private OnShowDialogListener mListener;
	
	private int mColor = -24832;
	
	
	public ColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public ColorPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);		
		init(attrs); 
		
	}
	
	private void init(AttributeSet attrs) {
		setPersistent(true);
		setWidgetLayoutResource(R.layout.colorpickerview__preference_preview_layout);

	}
	

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		
		ColorPanelView preview = (ColorPanelView) view.findViewById(R.id.colorpickerview__preference_preview_color_panel);
		if(preview != null) {
			preview.setColor(mColor);
		}
		
	}

	@Override
	protected void onClick() {
		super.onClick();

		ColorPickerPreferenceDialog fragment = ColorPickerPreferenceDialog.newInstance(mColor);
		fragment.setColorPickerDialogListener(this);
		Activity activity = (Activity) getContext();
		activity.getFragmentManager().beginTransaction()
				.add(fragment, "ColorPickerPreference")
				.commit();

	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {		
		if(restorePersistedValue) {
			mColor = getPersistedInt(-24832);

		}
		else {
			mColor = (Integer)defaultValue;
			persistInt(mColor);
		}
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, -24832);
	}
	
	
	public void saveValue(int color) {
		mColor = color;
		persistInt(mColor);
		notifyChanged();
	}

	@Override
	public void onColorSelected(int color) {
		saveValue(color);
	}


}
