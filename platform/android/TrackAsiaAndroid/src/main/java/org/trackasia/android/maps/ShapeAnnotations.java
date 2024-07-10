package org.trackasia.android.maps;

import android.graphics.RectF;

import org.trackasia.android.annotations.Annotation;

import java.util.List;

interface ShapeAnnotations {

  List<Annotation> obtainAllIn(RectF rectF);

}
