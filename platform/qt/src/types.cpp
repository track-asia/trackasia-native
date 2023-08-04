#include <QTrackasiaGL/Types>

#include "mbgl/util/geometry.hpp"
#include "mbgl/util/traits.hpp"

// mbgl::FeatureType
static_assert(mbgl::underlying_type(QTrackasiaGL::Feature::PointType) ==
                  mbgl::underlying_type(mbgl::FeatureType::Point),
              "error");
static_assert(mbgl::underlying_type(QTrackasiaGL::Feature::LineStringType) ==
                  mbgl::underlying_type(mbgl::FeatureType::LineString),
              "error");
static_assert(mbgl::underlying_type(QTrackasiaGL::Feature::PolygonType) ==
                  mbgl::underlying_type(mbgl::FeatureType::Polygon),
              "error");

namespace QTrackasiaGL {

/*!
    \namespace QTrackasiaGL
    \inmodule Trackasia Maps SDK for Qt

    Contains miscellaneous Trackasia types and utilities used throughout QTrackasiaGLGL.
*/

/*!
    \typedef QTrackasiaGL::Coordinate

    Alias for QPair<double, double>.
    Representation for geographical coordinates - latitude and longitude, respectively.
*/

/*!
    \typedef QTrackasiaGL::CoordinateZoom

    Alias for QPair<Coordinate, double>.
    Used as return value in QTrackasiaGLGL::coordinateZoomForBounds.
*/

/*!
    \typedef QTrackasiaGL::ProjectedMeters

    Alias for QPair<double, double>.
    Representation for projected meters - northing and easting, respectively.
*/

/*!
    \typedef QTrackasiaGL::Coordinates

    Alias for QVector<QTrackasiaGL::Coordinate>.
    A list of QTrackasiaGL::Coordinate objects.
*/

/*!
    \typedef QTrackasiaGL::CoordinatesCollection

    Alias for QVector<QTrackasiaGL::Coordinates>.
    A list of QTrackasiaGL::Coordinates objects.
*/

/*!
    \typedef QTrackasiaGL::CoordinatesCollections

    Alias for QVector<QTrackasiaGL::CoordinatesCollection>.
    A list of QTrackasiaGL::CoordinatesCollection objects.
*/

/*!
    \class QTrackasiaGL::Feature

    \inmodule Trackasia Maps SDK for Qt

    Represents \l {https://www.mapbox.com/help/define-features/}{map features}
    via its \a type (PointType, LineStringType or PolygonType), \a geometry, \a
    properties map and \a id (optional).
*/

/*!
    \enum QTrackasiaGL::Feature::Type

    This enum is used as basis for geometry disambiguation in
   QTrackasiaGL::Feature.

    \value PointType      A point geometry type. Means a single or a collection
   of points. \value LineStringType A line string geometry type. Means a single
   or a collection of line strings. \value PolygonType    A polygon geometry
   type. Means a single or a collection of polygons.
*/

/*!
    \class QTrackasiaGL::ShapeAnnotationGeometry

    \inmodule Trackasia Maps SDK for Qt

    Represents a shape annotation geometry.
*/

/*!
    \enum QTrackasiaGL::ShapeAnnotationGeometry::Type

    This enum is used as basis for shape annotation geometry disambiguation.

    \value PolygonType         A polygon geometry type.
    \value LineStringType      A line string geometry type.
    \value MultiPolygonType    A polygon geometry collection type.
    \value MultiLineStringType A line string geometry collection type.
*/

/*!
    \class QTrackasiaGL::SymbolAnnotation

    \inmodule Trackasia Maps SDK for Qt

    A symbol annotation comprises of its geometry and an icon identifier.
*/

/*!
    \class QTrackasiaGL::LineAnnotation

    \inmodule Trackasia Maps SDK for Qt

    Represents a line annotation object, along with its properties.

    A line annotation comprises of its geometry and line properties such as
   opacity, width and color.
*/

/*!
    \class QTrackasiaGL::FillAnnotation

    \inmodule Trackasia Maps SDK for Qt

    Represents a fill annotation object, along with its properties.

    A fill annotation comprises of its geometry and fill properties such as
   opacity, color and outline color.
*/

/*!
    \typedef QTrackasiaGL::Annotation

    Alias for QVariant.
    Container that encapsulates either a symbol, a line, a fill or a style
   sourced annotation.
*/

/*!
    \typedef QTrackasiaGL::AnnotationID

    Alias for quint32 representing an annotation identifier.
*/

/*!
    \typedef QTrackasiaGL::AnnotationIDs

    Alias for QVector<quint32> representing a container of annotation identifiers.
*/

/*!
    \class QTrackasiaGL::CameraOptions
    \inmodule Trackasia Maps SDK for Qt

    QTrackasiaGL::CameraOptions provides camera options to the renderer.
*/

/*!
    \class QTrackasiaGL::CustomLayerHostInterface

    Represents a host interface to be implemented for rendering custom layers.

    \warning This is used for delegating the rendering of a layer to the user of
    this API and is not officially supported. Use at your own risk.
*/

/*!
    \class QTrackasiaGL::CustomLayerRenderParameters
    \inmodule Trackasia Maps SDK for Qt

    QTrackasiaGL::CustomLayerRenderParameters provides the data passed on each
   render pass for a custom layer.
*/

} // namespace QTrackasiaGL
