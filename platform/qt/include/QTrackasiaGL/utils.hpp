#ifndef QTRACKASIAGL_UTILS_H
#define QTRACKASIAGL_UTILS_H

#include "export.hpp"
#include "types.hpp"

// This header follows the Qt coding style: https://wiki.qt.io/Qt_Coding_Style

namespace QTrackAsiaGL {

enum NetworkMode {
    Online, // Default
    Offline,
};

Q_TRACKASIAGL_EXPORT NetworkMode networkMode();
Q_TRACKASIAGL_EXPORT void setNetworkMode(NetworkMode);

Q_TRACKASIAGL_EXPORT double metersPerPixelAtLatitude(double latitude, double zoom);
Q_TRACKASIAGL_EXPORT ProjectedMeters projectedMetersForCoordinate(const Coordinate &);
Q_TRACKASIAGL_EXPORT Coordinate coordinateForProjectedMeters(const ProjectedMeters &);

} // namespace QTrackAsiaGL

#endif // QTRACKASIAGL_UTILS_H
