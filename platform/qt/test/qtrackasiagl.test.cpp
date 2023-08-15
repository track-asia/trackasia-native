#include "qtrackasiagl.test.hpp"

#include <QTrackasiaGL/Map>

#include <QFile>
#include <QGuiApplication>
#include <QOpenGLContext>
#include <QOpenGLFunctions>
#include <QTextStream>

#include <mbgl/test/util.hpp>

QTrackasiaGLTest::QTrackasiaGLTest() : size(512, 512), fbo((assert(widget.context()->isValid()), widget.makeCurrent(), size)), map(nullptr, settings, size) {
    connect(&map, &QTrackasiaGL::mapChanged, this, &QTrackasiaGLTest::onMapChanged);
    connect(&map, &QTrackasiaGL::needsRendering, this, &QTrackasiaGLTest::onNeedsRendering);
    map.resize(fbo.size());
    map.setFramebufferObject(fbo.handle(), fbo.size());
    map.setCoordinateZoom(QTrackasiaGL::Coordinate(60.170448, 24.942046), 14);
}

void QTrackasiaGLTest::runUntil(QTrackasiaGL::MapChange status) {
    changeCallback = [&](QTrackasiaGL::MapChange change) {
        if (change == status) {
            qApp->exit();
            changeCallback = nullptr;
        }
    };

    qApp->exec();
}

void QTrackasiaGLTest::onMapChanged(QTrackasiaGL::MapChange change) {
    if (changeCallback) {
        changeCallback(change);
    }
}

void QTrackasiaGLTest::onNeedsRendering() {
    widget.makeCurrent();
    fbo.bind();
    QOpenGLContext::currentContext()->functions()->glViewport(0, 0, fbo.width(), fbo.height());
    map.render();
}


TEST_F(QTrackasiaGLTest, TEST_DISABLED_ON_CI(styleJson)) {
    QFile f("test/fixtures/resources/style_vector.json");

    ASSERT_TRUE(f.open(QFile::ReadOnly | QFile::Text));

    QTextStream in(&f);
    QString json = in.readAll();

    map.setStyleJson(json);
    ASSERT_EQ(map.styleJson(), json);
    runUntil(QTrackasiaGL::MapChangeDidFinishLoadingMap);

    map.setStyleJson("invalid json");
    runUntil(QTrackasiaGL::MapChangeDidFailLoadingMap);

    map.setStyleJson("\"\"");
    runUntil(QTrackasiaGL::MapChangeDidFailLoadingMap);

    map.setStyleJson(QString());
    runUntil(QTrackasiaGL::MapChangeDidFailLoadingMap);
}

TEST_F(QTrackasiaGLTest, TEST_DISABLED_ON_CI(styleUrl)) {
    QString url(QTrackasiaGL::defaultStyles()[0].first);

    map.setStyleUrl(url);
    ASSERT_EQ(map.styleUrl(), url);
    runUntil(QTrackasiaGL::MapChangeDidFinishLoadingMap);

    map.setStyleUrl("invalid://url");
    runUntil(QTrackasiaGL::MapChangeDidFailLoadingMap);

    map.setStyleUrl(QString());
    runUntil(QTrackasiaGL::MapChangeDidFailLoadingMap);
}
