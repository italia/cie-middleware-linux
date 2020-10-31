#!/bin/bash
exec java -Xms1G \
-Xmx1G \
-Djna.library.path=".:/usr/lib" \
-classpath "/usr/share/cieid/cieid.jar:/usr/lib" \
it.ipzs.cieid.MainApplication

