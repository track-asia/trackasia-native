#include <QTrackAsiaGL/Map>

#include <QGLWidget>
#include <QGLFramebufferObject>

#include <gtest/gtest.h>

class QTrackAsiaGLTest : public QObject, public ::testing::Test {
    Q_OBJECT

public:
    QTrackAsiaGLTest();

    void runUntil(QTrackAsiaGL::MapChange);

private:
    QGLWidget widget;
    const QSize size;
    QGLFramebufferObject fbo;

protected:
    Settings settings;
    Map map;

    std::function<void(QTrackAsiaGL::MapChange)> changeCallback;

private slots:
    void onMapChanged(QTrackAsiaGL::MapChange);
    void onNeedsRendering();
};
