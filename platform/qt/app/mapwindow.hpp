#ifndef MAPWINDOW_H
#define MAPWINDOW_H

#include <QTrackasiaGL/Map>
#include <QTrackasiaGL/Settings>

#include <QtGlobal>
#include <QOpenGLWidget>
#include <QPropertyAnimation>

#include <memory>

QT_BEGIN_NAMESPACE

class QKeyEvent;
class QMouseEvent;
class QWheelEvent;

QT_END_NAMESPACE

class MapWindow : public QOpenGLWidget
{
    Q_OBJECT

public:
    MapWindow(const QTrackasiaGL::Settings &);
    ~MapWindow();

    void selfTest();

protected slots:
    void animationValueChanged();
    void animationFinished();

private:
    void changeStyle();
    qreal pixelRatio();

    // QWidget implementation.
    void keyPressEvent(QKeyEvent *ev) final;
    void mousePressEvent(QMouseEvent *ev) final;
    void mouseMoveEvent(QMouseEvent *ev) final;
    void wheelEvent(QWheelEvent *ev) final;

    // Q{,Open}GLWidget implementation.
    void initializeGL() final;
    void paintGL() final;

    QPointF m_lastPos;

    QTrackasiaGL::Settings m_settings;
    std::unique_ptr<QTrackasiaGL::Map> m_map{};

    QPropertyAnimation *m_bearingAnimation{};
    QPropertyAnimation *m_zoomAnimation{};

    unsigned m_animationTicks{};
    unsigned m_frameDraws{};

    QVariant m_symbolAnnotationId;
    QVariant m_lineAnnotationId;
    QVariant m_fillAnnotationId;

    bool m_sourceAdded{};
};

#endif
