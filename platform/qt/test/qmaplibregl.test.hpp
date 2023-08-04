#include <QTrackasiaGL/Map>

#include <QGLWidget>
#include <QGLFramebufferObject>

#include <gtest/gtest.h>

class QTrackasiaGLTest : public QObject, public ::testing::Test {
    Q_OBJECT

public:
    QTrackasiaGLTest();

    void runUntil(QTrackasiaGL::MapChange);

private:
    QGLWidget widget;
    const QSize size;
    QGLFramebufferObject fbo;

protected:
    Settings settings;
    Map map;

    std::function<void(QTrackasiaGL::MapChange)> changeCallback;

private slots:
    void onMapChanged(QTrackasiaGL::MapChange);
    void onNeedsRendering();
};
