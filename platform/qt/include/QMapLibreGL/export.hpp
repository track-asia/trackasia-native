#ifndef QTRACKASIA_EXPORT_H
#define QTRACKASIA_EXPORT_H

// This header follows the Qt coding style: https://wiki.qt.io/Qt_Coding_Style

#if !defined(QT_TRACKASIAGL_STATIC)
#if defined(QT_BUILD_TRACKASIAGL_LIB)
#define Q_TRACKASIAGL_EXPORT Q_DECL_EXPORT
#else
#define Q_TRACKASIAGL_EXPORT Q_DECL_IMPORT
#endif
#else
#define Q_TRACKASIAGL_EXPORT
#endif

#endif // QTRACKASIA_EXPORT_H
