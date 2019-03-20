#!/bin/sh
PROJ_ROOT=`pwd`

BUILD_DIR="$PROJ_ROOT/build"
mkdir -p $BUILD_DIR
cd $BUILD_DIR

cmake -DCMAKE_MODULE_PATH=$PROJ_ROOT -DCMAKE_INSTALL_PREFIX=$PROJ_ROOT/dist/usr $PROJ_ROOT
#/Build/Linux/GCC
make && make install/strip
find $PROJ_ROOT/dist/ -type d | xargs chmod 755
rm $PROJ_ROOT/*.deb
fakeroot dpkg-deb --build $PROJ_ROOT/dist
lintian $PROJ_ROOT/dist.deb
mv $PROJ_ROOT/dist.deb $PROJ_ROOT/libciepki1.0-1.deb

cd $PROJ_ROOT
