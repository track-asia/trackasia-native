package com.trackasia.android.maps;

import android.graphics.RectF;

import com.trackasia.android.annotations.Annotation;

import java.util.List;

interface ShapeAnnotations {

  List<Annotation> obtainAllIn(RectF rectF);

}
