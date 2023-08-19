#include "qtrackasiagl.test.hpp"

#include <QTrackAsiaGL/Map>

#include <QFile>
#include <QGuiApplication>
#include <QOpenGLContext>
#include <QOpenGLFunctions>
#include <QTextStream>

#include <mbgl/test/util.hpp>

QTrackAsiaGLTest::QTrackAsiaGLTest() : size(512, 512), fbo((assert(widget.context()->isValid()), widget.makeCurrent(), size)), map(nullptr, settings, size) {
    connect(&map, &QTrackAsiaGL::mapChanged, this, &QTrackAsiaGLTest::onMapChanged);
    connect(&map, &QTrackAsiaGL::needsRendering, this, &QTrackAsiaGLTest::onNeedsRendering);
    map.resize(fbo.size());
    map.setFramebufferObject(fbo.handle(), fbo.size());
    map.setCoordinateZoom(QTrackAsiaGL::Coordinate(60.170448, 24.942046), 14);
}

void QTrackAsiaGLTest::runUntil(QTrackAsiaGL::MapChange status) {
    changeCallback = [&](QTrackAsiaGL::MapChange change) {
        if (change == status) {
            qApp->exit();
            changeCallback = nullptr;
        }
    };

    qApp->exec();
}

void QTrackAsiaGLTest::onMapChanged(QTrackAsiaGL::MapChange change) {
    if (changeCallback) {
        changeCallback(change);
    }
}

void QTrackAsiaGLTest::onNeedsRendering() {
    widget.makeCurrent();
    fbo.bind();
    QOpenGLContext::currentContext()->functions()->glViewport(0, 0, fbo.width(), fbo.height());
    map.render();
}


TEST_F(QTrackAsiaGLTest, TEST_DISABLED_ON_CI(styleJson)) {
    QFile f("test/fixtures/resources/style_vector.json");

    ASSERT_TRUE(f.open(QFile::ReadOnly | QFile::Text));

    QTextStream in(&f);
    QString json = in.readAll();

    map.setStyleJson(json);
    ASSERT_EQ(map.styleJson(), json);
    runUntil(QTrackAsiaGL::MapChangeDidFinishLoadingMap);

    map.setStyleJson("invalid json");
    runUntil(QTrackAsiaGL::MapChangeDidFailLoadingMap);

    map.setStyleJson("\"\"");
    runUntil(QTrackAsiaGL::MapChangeDidFailLoadingMap);

    map.setStyleJson(QString());
    runUntil(QTrackAsiaGL::MapChangeDidFailLoadingMap);
}

TEST_F(QTrackAsiaGLTest, TEST_DISABLED_ON_CI(styleUrl)) {
    QString url(QTrackAsiaGL::defaultStyles()[0].first);

    map.setStyleUrl(url);
    ASSERT_EQ(map.styleUrl(), url);
    runUntil(QTrackAsiaGL::MapChangeDidFinishLoadingMap);

    map.setStyleUrl("invalid://url");
    runUntil(QTrackAsiaGL::MapChangeDidFailLoadingMap);

    map.setStyleUrl(QString());
    runUntil(QTrackAsiaGL::MapChangeDidFailLoadingMap);
}
