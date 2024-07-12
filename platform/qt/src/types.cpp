#include <QTrackAsiaGL/Types>

#include "mbgl/util/geometry.hpp"
#include "mbgl/util/traits.hpp"

// mbgl::FeatureType
static_assert(mbgl::underlying_type(QTrackAsiaGL::Feature::PointType) == mbgl::underlying_type(mbgl::FeatureType::Point), "error");
static_assert(mbgl::underlying_type(QTrackAsiaGL::Feature::LineStringType) == mbgl::underlying_type(mbgl::FeatureType::LineString), "error");
static_assert(mbgl::underlying_type(QTrackAsiaGL::Feature::PolygonType) == mbgl::underlying_type(mbgl::FeatureType::Polygon), "error");

namespace QTrackAsiaGL {

/*!
    \namespace QTrackAsiaGL
    \inmodule TrackAsia Maps SDK for Qt

    Contains miscellaneous TrackAsia types and utilities used throughout QTrackAsiaGLGL.
*/

/*!
    \typedef QTrackAsiaGL::Coordinate

    Alias for QPair<double, double>.
    Representation for geographical coordinates - latitude and longitude, respectively.
*/

/*!
    \typedef QTrackAsiaGL::CoordinateZoom

    Alias for QPair<Coordinate, double>.
    Used as return value in QTrackAsiaGLGL::coordinateZoomForBounds.
*/

/*!
    \typedef QTrackAsiaGL::ProjectedMeters

    Alias for QPair<double, double>.
    Representation for projected meters - northing and easting, respectively.
*/

/*!
    \typedef QTrackAsiaGL::Coordinates

    Alias for QVector<QTrackAsiaGL::Coordinate>.
    A list of QTrackAsiaGL::Coordinate objects.
*/

/*!
    \typedef QTrackAsiaGL::CoordinatesCollection

    Alias for QVector<QTrackAsiaGL::Coordinates>.
    A list of QTrackAsiaGL::Coordinates objects.
*/

/*!
    \typedef QTrackAsiaGL::CoordinatesCollections

    Alias for QVector<QTrackAsiaGL::CoordinatesCollection>.
    A list of QTrackAsiaGL::CoordinatesCollection objects.
*/

/*!
    \class QTrackAsiaGL::Feature

    \inmodule TrackAsia Maps SDK for Qt

    Represents \l {https://www.mapbox.com/help/define-features/}{map features}
    via its \a type (PointType, LineStringType or PolygonType), \a geometry, \a
    properties map and \a id (optional).
*/

/*!
    \enum QTrackAsiaGL::Feature::Type

    This enum is used as basis for geometry disambiguation in QTrackAsiaGL::Feature.

    \value PointType      A point geometry type. Means a single or a collection of points.
    \value LineStringType A line string geometry type. Means a single or a collection of line strings.
    \value PolygonType    A polygon geometry type. Means a single or a collection of polygons.
*/

/*!
    \class QTrackAsiaGL::ShapeAnnotationGeometry

    \inmodule TrackAsia Maps SDK for Qt

    Represents a shape annotation geometry.
*/

/*!
    \enum QTrackAsiaGL::ShapeAnnotationGeometry::Type

    This enum is used as basis for shape annotation geometry disambiguation.

    \value PolygonType         A polygon geometry type.
    \value LineStringType      A line string geometry type.
    \value MultiPolygonType    A polygon geometry collection type.
    \value MultiLineStringType A line string geometry collection type.
*/

/*!
    \class QTrackAsiaGL::SymbolAnnotation

    \inmodule TrackAsia Maps SDK for Qt

    A symbol annotation comprises of its geometry and an icon identifier.
*/

/*!
    \class QTrackAsiaGL::LineAnnotation

    \inmodule TrackAsia Maps SDK for Qt

    Represents a line annotation object, along with its properties.

    A line annotation comprises of its geometry and line properties such as opacity, width and color.
*/

/*!
    \class QTrackAsiaGL::FillAnnotation

    \inmodule TrackAsia Maps SDK for Qt

    Represents a fill annotation object, along with its properties.

    A fill annotation comprises of its geometry and fill properties such as opacity, color and outline color.
*/

/*!
    \typedef QTrackAsiaGL::Annotation

    Alias for QVariant.
    Container that encapsulates either a symbol, a line, a fill or a style sourced annotation.
*/

/*!
    \typedef QTrackAsiaGL::AnnotationID

    Alias for quint32 representing an annotation identifier.
*/

/*!
    \typedef QTrackAsiaGL::AnnotationIDs

    Alias for QVector<quint32> representing a container of annotation identifiers.
*/

/*!
    \class QTrackAsiaGL::CameraOptions
    \inmodule TrackAsia Maps SDK for Qt

    QTrackAsiaGL::CameraOptions provides camera options to the renderer.
*/

/*!
    \class QTrackAsiaGL::CustomLayerHostInterface

    Represents a host interface to be implemented for rendering custom layers.

    \warning This is used for delegating the rendering of a layer to the user of
    this API and is not officially supported. Use at your own risk.
*/

/*!
    \class QTrackAsiaGL::CustomLayerRenderParameters
    \inmodule TrackAsia Maps SDK for Qt

    QTrackAsiaGL::CustomLayerRenderParameters provides the data passed on each render
    pass for a custom layer.
*/

} // namespace QTrackAsiaGL
